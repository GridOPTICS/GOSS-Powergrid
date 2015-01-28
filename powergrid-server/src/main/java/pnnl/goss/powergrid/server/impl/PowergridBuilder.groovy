package pnnl.goss.powergrid.server.impl

import static java.util.UUID.randomUUID

import pnnl.goss.powergrid.entities.BusEntity
import pnnl.goss.powergrid.entities.PowergridModelEntity;
import pnnl.goss.powergrid.models.PowergridModel
import pnnl.goss.powergrid.parsers.PsseParser
import pnnl.goss.powergrid.topology.bb.impl.BbBusImpl

class PowergridBuilder {

    PowergridModelEntity createFromParser(PsseParser parser, def attr){

        println parser.configuration
        PowergridModelEntity entity = new PowergridModelEntity();

        attr.each { k, v->
            entity."${k}" = v
        }

        if (entity.mrid == null){
            entity.mrid = randomUUID()
        }

        createBuses(entity, parser.model.buses, parser.model)

    }

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
                fileOrder: i
            )

            entityBuses << entity
        }

        powergridEntity.setBusEntities(entityBuses)
        powergridEntity
    }



}
