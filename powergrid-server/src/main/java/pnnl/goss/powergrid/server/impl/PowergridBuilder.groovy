package pnnl.goss.powergrid.server.impl

import static java.util.UUID.randomUUID
import pnnl.goss.powergrid.entities.AreaEntity
import pnnl.goss.powergrid.entities.BusEntity
import pnnl.goss.powergrid.entities.FromToEntity;
import pnnl.goss.powergrid.entities.GeneratorEntity
import pnnl.goss.powergrid.entities.BranchEntity
import pnnl.goss.powergrid.entities.OwnerEntity
import pnnl.goss.powergrid.entities.PowergridModelEntity;
import pnnl.goss.powergrid.entities.SwitchedShuntEntity
import pnnl.goss.powergrid.entities.TransformerEntity
import pnnl.goss.powergrid.entities.ZoneEntity
//import pnnl.goss.powergrid.models.PowergridModel;
import pnnl.goss.powergrid.parsers.PsseParser
import pnnl.goss.powergrid.parsers.ResultLog;
//import pnnl.goss.powergrid.topology.bb.impl.BbBusImpl
//
class PowergridBuilder {

    PsseParser parser

    String myRandomString(){
        randomUUID().toString()
    }

    PowergridBuilder() {

    }

    PowergridModelEntity createFromParser(PsseParser parser, ResultLog log, def attr){
        this.parser = parser
        println parser.configuration
        PowergridModelEntity powergridModel = new PowergridModelEntity();

        attr.each { k, v->
            powergridModel."${k}" = v
        }

        if (powergridModel.mrid == null){
            powergridModel.mrid = randomUUID().toString()
        }

        powergridModel.setModelFileType(parser.configuration.fileType)
        powergridModel.setModelImportDate(new Date())

        createBuses(powergridModel, parser.model.buses, parser.model)
        createGenerators(powergridModel, parser.model.generators)
        createBranches(powergridModel, parser.model.branches)
        createTransformers(powergridModel, parser.model.transformer_adjustments)
        createZones(powergridModel, parser.model.zones)
        createAreas(powergridModel, parser.model.areas)
        createOwners(powergridModel, parser.model.owners)
        createSwitchedShunts(powergridModel, parser.model.switched_shunts)

        return powergridModel
    }


//

//    }
//
//
    private PowergridModelEntity createBuses(PowergridModelEntity powergridEntity,
        List parsedBuses, def parsedModel){

        def entityBuses = []
        parsedBuses.eachWithIndex { bus, i ->
            def entity = new BusEntity (
                name: bus.name,
                busName: bus.name,
                // code should change to busType as it is more explicit.
                code: bus.busType,
                PLoad: bus.pLoad,
                QLoad: bus.qLoad,
                GShunt: bus.bShunt,
                BShunt: bus.gShunt,
                areaId: bus.area,
                zoneId: bus.zone,
                va: bus.vAng,
                vm: bus.vMag,
                busNumber: bus.busNumber,
                baseKv: bus.baseKv,
                mrid: randomUUID(),
                importOrder: i
            )

            entityBuses << entity
        }

        powergridEntity.setBusEntities(entityBuses)
        powergridEntity
    }

    private PowergridModelEntity createSwitchedShunts(PowergridModelEntity powergridEntity,
        List parsedShunts) {

        def entityShunts = []
        parsedShunts.eachWithIndex { shunt, i ->
            def entity = new SwitchedShuntEntity (
                controlMode: shunt.controlMode,
                vswHi: shunt.vswHi,
                vswLo: shunt.vswLo,
                swRem: shunt.swRem,
                bInit: shunt.bInit,
                n1: shunt.n1,
                b1: shunt.b1,
                n2: shunt.n2,
                b2: shunt.b2,
                n3: shunt.n3,
                b3: shunt.b3,
                n4: shunt.n4,
                b4: shunt.b4,
                n5: shunt.n5,
                b5: shunt.b5,
                n6: shunt.n6,
                b6: shunt.b6,
                n7: shunt.n7,
                b7: shunt.b7,
                n8: shunt.n8,
                b8: shunt.b8,

                importOrder: i
            )

            entityShunts << entity
        }

        powergridEntity.setSwitchedShuntEntities(entityShunts)
        powergridEntity

    }


    private PowergridModelEntity createGenerators(PowergridModelEntity powergridEntity,
        List parsedGenerators) {

        def entityGenerators = []
        parsedGenerators.eachWithIndex { generator, i ->
            def entity = new GeneratorEntity (
                parentBus: powergridEntity.getBusByBusNumber(generator.busNumber),
                machineId: generator.machineId,
                pGen: generator.pGen,
                qGen: generator.qGen,
                qMax: generator.qMax,
                qMin: generator.qMin,
                vSetPoint: generator.vSetPoint,
                iRegBusNumber: generator.iRegBusNumber,
                mBase: generator.mBase,
                zSource: generator.zSource,
                zTran: generator.zTran,
                rTran: generator.rTran,
                xTran: generator.xTran,
                gTap: generator.gTap,
                status: generator.status,
                rmPcnt: generator.rmPcnt,
                pMax: generator.pMax,
                pMin: generator.pMin,
                mrid: randomUUID(),
                importOrder: i
            )

            entityGenerators << entity
        }

        powergridEntity.setGeneratorEntities(entityGenerators)
        powergridEntity

    }

    private PowergridModelEntity createBranches(PowergridModelEntity powergridEntity,
        List parsedLines){

        List<BranchEntity> entityLines = []
        parsedLines.eachWithIndex {line, i ->
            FromToEntity fromTo = new FromToEntity(
                    fromBusMrid: powergridEntity.getBusByBusNumber(line.fromBus).mrid,
                    toBusMrid: powergridEntity.getBusByBusNumber(line.toBus).mrid,
                    ckt: line.ckt
                )
            BranchEntity entity= new BranchEntity(
                fromToBuses: fromTo,
                //fromBus: powergridEntity.getBusByBusNumber(line.fromBus),
                //toBus: powergridEntity.getBusByBusNumber(line.toBus),
                //ckt: line.ckt,
                r: line.r,
                x: line.x,
                ratingA: line.ratingA,
                ratingB: line.ratingB,
                ratingC: line.ratingC,
                ratio: line.ratio,
                angle: line.angle,
                shuntG1: line.shuntG1,
                shuntG2: line.shuntG2,
                shuntB1: line.shuntB1,
                status: line.status,
                mrid: randomUUID(),
                importOrder: i
            )

            entityLines << entity
        }

        powergridEntity.setBranchEntities(entityLines)
        powergridEntity

    }

    private PowergridModelEntity createTransformers(PowergridModelEntity powergridEntity,
        List parsedTransformers){

        List<TransformerEntity> entityTransformers = []
        parsedTransformers.eachWithIndex {transformer, i ->
            FromToEntity fromTo = new FromToEntity(
                    fromBusMrid: powergridEntity.getBusByBusNumber(transformer.fromBus).mrid,
                    toBusMrid: powergridEntity.getBusByBusNumber(transformer.toBus).mrid,
                    ckt: transformer.ckt
                )
            TransformerEntity entity= new TransformerEntity(
                fromToBuses: fromTo,
                //fromBus: powergridEntity.getBusByBusNumber(line.fromBus),
                //toBus: powergridEntity.getBusByBusNumber(line.toBus),
                //ckt: transformer.ckt,
                control: transformer.control,
                rma: transformer.rma,
                rmi: transformer.rmi,
                vma: transformer.vma,
                vmi: transformer.vmi,
                step: transformer.step,
                table: transformer.table,

                mrid: randomUUID(),
                importOrder: i
            )

            entityTransformers << entity
        }

        powergridEntity.setTransformerEntities(entityTransformers)
        powergridEntity

    }

     private PowergridModelEntity createAreas(PowergridModelEntity powergridEntity,
        List parsedAreas){

        List<AreaEntity> areaEntities = []
        parsedAreas.eachWithIndex { area, i ->

            AreaEntity entity = new AreaEntity (
                areaNumber: area.areaNumber,
                name: area.name,
                isw: area.isw,
                pDesired: area.pDesired,
                pTolerance: area.pTolerance,
                mrid: randomUUID(),
                importOrder: i
            )

            areaEntities<< entity
        }


        powergridEntity.setAreaEntities(areaEntities)
        powergridEntity
     }

    private PowergridModelEntity createZones(PowergridModelEntity powergridEntity,
        List parsedZones){
        List<ZoneEntity> zoneEntities = []

        parsedZones.eachWithIndex { zone, i ->
            ZoneEntity entity = new ZoneEntity(
                zoneNumber: zone.zoneNumber,
                name: zone.name,
                mrid: randomUUID(),
                importOrder: i
            )

            zoneEntities<< entity
        }

        powergridEntity.setZoneEntities(zoneEntities)
        powergridEntity
    }

    private PowergridModelEntity createOwners(PowergridModelEntity powergridEntity,
        List parsedOwners){
        List<OwnerEntity> ownerEntities = []

        parsedOwners.eachWithIndex { owner, i ->
            OwnerEntity entity = new OwnerEntity(
                ownerNumber: owner.ownerNumber,
                name: owner.name,
                mrid: randomUUID(),
                importOrder: i
            )

            ownerEntities<< entity
        }

        powergridEntity.setOwnerEntities(ownerEntities)
        powergridEntity
     }



}