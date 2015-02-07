package pnnl.goss.powergrid.server.impl;

import java.util.ArrayList;
import java.util.List;

import pnnl.goss.powergrid.datamodel.Area;
import pnnl.goss.powergrid.datamodel.Branch;
import pnnl.goss.powergrid.datamodel.Bus;
import pnnl.goss.powergrid.datamodel.Line;
import pnnl.goss.powergrid.datamodel.Machine;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.SwitchedShunt;
import pnnl.goss.powergrid.datamodel.Transformer;
import pnnl.goss.powergrid.datamodel.Zone;
import pnnl.goss.powergrid.entities.AreaEntity;
import pnnl.goss.powergrid.entities.BusEntity;
import pnnl.goss.powergrid.entities.GeneratorEntity;
import pnnl.goss.powergrid.entities.BranchEntity;
import pnnl.goss.powergrid.entities.PowergridModelEntity;
import pnnl.goss.powergrid.entities.SwitchedShuntEntity;
import pnnl.goss.powergrid.entities.TransformerEntity;
import pnnl.goss.powergrid.entities.ZoneEntity;
import pnnl.goss.powergrid.models.PowergridModel;

public class EntityToModelConverter {

    public static PowergridModel toPowergridModel(PowergridModelEntity entity){
        Powergrid powergrid = new Powergrid();

        //powergrid.setCaseIdentifier(entity.get);
        powergrid.setMrid(entity.getMrid());
        powergrid.setName(entity.getPowergridName());
        powergrid.setSbase(entity.getSBase());
        // Only return 0 for powergrid.
        powergrid.setPsseCaseId(0);


        PowergridModel model = new PowergridModel();
        model.setPowergrid(powergrid);
        model.setTimestep(entity.getTimeStep());

        model.setBuses(toBusList(entity.getBusEntities()));
        model.setMachines(toMachineList(entity.getGeneratorEntities()));
        model.setAreas(toAreaList(entity.getAreaEntities()));

        model.setZones(toZoneList(entity.getZoneEntities()));
        //model.setTransformers(transformers);
        model.setSwitchedShunts(toSwitchedShuntList(entity.getSwitchedShuntEntities()));

        populateTransmissionElements(entity, model, entity.getBranchEntities(), entity.getTransformerEntities());

        //model.setBranches(toBranchList(entity.getLineEntities(), entity.getTransformerEntities()));
        //model.setLines(toLineList(entity.getLineEntities()));
        //model.setTransformers(toTransformerList(entity.getTransformerEntities()));

        return model;
    }

    private static void populateTransmissionElements(PowergridModelEntity powerModelEntity,
            PowergridModel model,
            List<BranchEntity> branchEntities,
            List<TransformerEntity> transformerEntities){

        List<Branch> branches = new ArrayList<Branch>();
        List<Line> lines = new ArrayList<Line>();
        List<Transformer> transformers = new ArrayList<Transformer>();

        int branchIndex = 0;
        int lineIndex = 0;
        int transformerIndex = 0;

        if (branchEntities != null) {

            for(int i=0; i< branchEntities.size(); i++){
                Branch branch = new Branch();

                BranchEntity entity = branchEntities.get(i);

                BusEntity fromBus = powerModelEntity.getBusByMrid(entity.getFromToBuses().getFromBusMrid());
                BusEntity toBus = powerModelEntity.getBusByMrid(entity.getFromToBuses().getToBusMrid());

                branch.setMrid(entity.getMrid());

                branch.setFromBusNumber(fromBus.getBusNumber());
                branch.setToBusNumber(toBus.getBusNumber());

                branch.setCkt(entity.getCkt());
                branch.setR(entity.getR());
                branch.setX(entity.getX());
                branch.setB(entity.getB());
                branch.setRateA(entity.getRatingA());
                branch.setRateB(entity.getRatingB());
                branch.setRateC(entity.getRatingC());
                branch.setStatus(entity.getStatus());
                branch.setBi(entity.getShuntB1());
                branch.setGi(entity.getShuntG1());
                branch.setBj(entity.getShuntB2());
                branch.setGj(entity.getShuntG2());
                branch.setIsLineElement(true);
                branch.setIndexNum(i);
                branches.add(branch);
            }
        }

        if (transformerEntities != null){
            for(int i=0; i< transformerEntities.size(); i++){
                Branch branch = new Branch();
                Transformer transformer = new Transformer();

                TransformerEntity entity = transformerEntities.get(i);

                BusEntity fromBus = powerModelEntity.getBusByMrid(entity.getFromToBuses().getFromBusMrid());
                BusEntity toBus = powerModelEntity.getBusByMrid(entity.getFromToBuses().getToBusMrid());

                branch.setMrid(entity.getMrid());

                branch.setFromBusNumber(fromBus.getBusNumber());
                branch.setToBusNumber(toBus.getBusNumber());

                transformer.setBranch(branch);
                transformer.setRma(entity.getRma());
                transformer.setRmi(entity.getRmi());
                //transformer.setTable1(entity.getTable());
                //transformer.setTapPosition(entity.getStep());
                transformer.setIcont(entity.getControl());


            }
        }

        model.setBranches(branches);
        model.setTransformers(transformers);

    }


    public static List<Branch> toBranchList(List<BranchEntity> lineEntities,
            List<TransformerEntity> transformerEntities){

        List<Branch> modelList = new ArrayList<Branch>();

        if (lineEntities != null) {

        }

        return modelList;
    }

    public static List<Line> toLineList(List<BranchEntity> entities) {
        List<Line> modelList = new ArrayList<Line>();

        if (entities != null) {
            for(int i=0; i<entities.size(); i++){
                Line model = new Line();
                BranchEntity entity = entities.get(i);

//                model.setCkt(entity.getCkt());
//                model.setBcap(entity.getB());
//                model.set
                modelList.add(model);
            }
        }

        return modelList;
    }

    public static List<Transformer> toTransformerList(List<TransformerEntity> entities) {
        List<Transformer> modelList = new ArrayList<Transformer>();

        if (entities != null) {
            for(int i=0; i<entities.size(); i++){
                Transformer model = new Transformer();
                TransformerEntity entity = entities.get(i);
                modelList.add(model);
            }
        }

        return modelList;
    }


    public static List<SwitchedShunt> toSwitchedShuntList(List<SwitchedShuntEntity> entities) {
        List<SwitchedShunt> modelList = new ArrayList<SwitchedShunt>();

        if (entities != null) {
            for(int i=0; i<entities.size(); i++){

                SwitchedShunt model = new SwitchedShunt();
                SwitchedShuntEntity entity = entities.get(i);
                model.setBusNumber(entity.getParentBus().getBusNumber());
                model.setBinit(entity.getbInit());
                model.setMrid(entity.getMrid());
                model.setModSw(entity.getControlMode());
                model.setVswHi(entity.getVswHi());
                model.setVswLo(entity.getVswLo());
                model.setSwRem(entity.getSwRem());
                model.setN1(entity.getN1());
                model.setB1(entity.getB1());
                model.setN2(entity.getN2());
                model.setB2(entity.getB2());
                model.setN3(entity.getN3());
                model.setB3(entity.getB3());
    //            model.setN4(entity.getN4());
    //            model.setB4(entity.getB4());
    //            model.setN5(entity.getN5());
    //            model.setB5(entity.getB5());
    //            model.setN6(entity.getN6());
    //            model.setB6(entity.getB6());
    //            model.setN7(entity.getN7());
    //            model.setB7(entity.getB7());
    //            model.setN8(entity.getN8());
    //            model.setB8(entity.getB8());


                modelList.add(model);
            }
        }

        return modelList;
    }

    public static List<Zone> toZoneList(List<ZoneEntity> entities) {
        List<Zone> modelList = new ArrayList<Zone>();

        if (entities != null) {
            for(int i=0; i<entities.size(); i++){

                Zone model = new Zone();
                ZoneEntity entity = entities.get(i);
                model.setMrid(entity.getMrid());
                model.setZoneName(entity.getName());

                modelList.add(model);
            }
        }

        return modelList;
    }

    public static List<Area> toAreaList(List<AreaEntity> entities) {
        List<Area> modelList = new ArrayList<Area>();

        if (entities != null ) {
            for(int i=0; i<entities.size(); i++){

                Area model = new Area();
                AreaEntity entity = entities.get(i);
                model.setAreaName(entity.getName());
                model.setAreaId(entity.getAreaNumber());
                model.setMrid(entity.getMrid());
                model.setIsw(entity.getIsw());
                model.setPdes(entity.getpDesired());
                model.setPtol(entity.getpTolerance());

                modelList.add(model);
            }
        }

        return modelList;
    }

    public static Powergrid toPowergrid(PowergridModelEntity entity){
        Powergrid powergrid = new Powergrid();

        if (entity != null ) {
            powergrid.setMrid(entity.getMrid());
            powergrid.setName(entity.getPowergridName());
            // TODO: Added entity caseid getter
            powergrid.setPsseCaseId(0);
            //powergrid.setSbase(entity.getSBase());
            powergrid.setCoordinateSystem("Universal Transverse Mercator (UTM)");
            //powergrid.setCaseIdentifier(entity..get);
        }

        return powergrid;
    }

    public static List<Bus> toBusList(List<BusEntity> entities){

        List<Bus> busList = new ArrayList<Bus>();

        if (entities != null ) {
            for(int i=0; i<entities.size(); i++){

                Bus b = new Bus();
                BusEntity ent = entities.get(i);

                b.setBaseKv(ent.getBaseKv());
                b.setBl(ent.getBShunt());
                b.setGl(ent.getGShunt());
                b.setBusName(ent.getBusName());
                b.setCode(ent.getCode());
                b.setMrid(ent.getMrid());
                b.setBusNumber(ent.getBusNumber());
                b.setVa(ent.getVa());
                b.setVm(ent.getVm());
                b.setZoneId(ent.getZoneId());
                b.setAreaId(ent.getAreaId());
                b.setQl(ent.getQLoad());
                b.setPl(ent.getPLoad());

                busList.add(b);
            }
        }

        return busList;

    }

    public static List<Machine> toMachineList(List<GeneratorEntity> entities){

        List<Machine> modelList = new ArrayList<Machine>();

        if (entities != null ) {
            for(int i=0; i<entities.size(); i++){

                Machine model = new Machine();
                GeneratorEntity entity = entities.get(i);

                model.setMrid(entity.getMrid());
                model.setBusNumber(entity.getParentBus().getBusNumber());
                model.setMachineName(entity.getMachineId());
                model.setGtap(entity.getgTap());
                model.setIreg(entity.getiRegBusNumber());
                model.setMaxPgen(entity.getpMax());
                model.setMinPgen(entity.getpMin());
                model.setMinQgen(entity.getqMin());
                model.setMaxQgen(entity.getqMax());
                model.setPgen(entity.getpGen());
                model.setQgen(entity.getqGen());
                model.setIreg(entity.getiRegBusNumber());
                model.setRmPct(entity.getRmPcnt());
                model.setRt(entity.getrTran());
                model.setStatus(entity.getStatus());
                model.setVs(entity.getvSetPoint());
                model.setXt(entity.getxTran());
                model.setZr(entity.getzSource());
                model.setZx(entity.getzTran());

                modelList.add(model);
            }
        }

        return modelList;

    }





}
