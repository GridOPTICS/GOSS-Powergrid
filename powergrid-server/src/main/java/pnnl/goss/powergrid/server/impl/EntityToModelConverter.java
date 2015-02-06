package pnnl.goss.powergrid.server.impl;

import java.util.ArrayList;
import java.util.List;

import pnnl.goss.powergrid.datamodel.Bus;
import pnnl.goss.powergrid.datamodel.Machine;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.entities.BusEntity;
import pnnl.goss.powergrid.entities.BusTimestepEntity;
import pnnl.goss.powergrid.entities.GeneratorEntity;
import pnnl.goss.powergrid.entities.PowergridModelEntity;
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

        return model;
    }

    public static Powergrid toPowergrid(PowergridModelEntity entity){
        Powergrid powergrid = new Powergrid();

        powergrid.setMrid(entity.getMrid());
        powergrid.setName(entity.getPowergridName());
        // TODO: Added entity caseid getter
        powergrid.setPsseCaseId(0);
        //powergrid.setSbase(entity.getSBase());
        powergrid.setCoordinateSystem("Universal Transverse Mercator (UTM)");
        //powergrid.setCaseIdentifier(entity..get);

        return powergrid;
    }


    public static List<Bus> toBusList(List<BusEntity> busEntities){

        List<Bus> busList = new ArrayList<Bus>();

        for(int i=0; i<busEntities.size(); i++){

            Bus b = new Bus();
            BusEntity ent = busEntities.get(i);

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


        return busList;

    }

    public static List<Machine> toMachineList(List<GeneratorEntity> entities){

        List<Machine> modelList = new ArrayList<Machine>();

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


        return modelList;

    }





}
