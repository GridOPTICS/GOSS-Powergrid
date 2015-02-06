package pnnl.goss.powergrid.server.impl;

import java.util.Date;

import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.entities.PowergridModelEntity;
import pnnl.goss.powergrid.models.PowergridModel;

public class EntityToModelConverter {

    public static PowergridModel toPowergridModel(PowergridModelEntity entity){
        Powergrid powergrid = new Powergrid();

        //powergrid.setCaseIdentifier(entity.get);
        powergrid.setMrid(entity.getMrid());
        powergrid.setName(entity.getPowergridName());
        //powergrid.setSbase(sbase);
        PowergridModel model = new PowergridModel(powergrid, new Date());
        
        

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
    
    



}
