package pnnl.goss.powergrid.server.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import pnnl.goss.powergrid.dao.PowergridDao;
import pnnl.goss.powergrid.datamodel.AlertContext;
import pnnl.goss.powergrid.datamodel.Area;
import pnnl.goss.powergrid.datamodel.Branch;
import pnnl.goss.powergrid.datamodel.Bus;
import pnnl.goss.powergrid.datamodel.Line;
import pnnl.goss.powergrid.datamodel.Load;
import pnnl.goss.powergrid.datamodel.Machine;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.Substation;
import pnnl.goss.powergrid.datamodel.SwitchedShunt;
import pnnl.goss.powergrid.datamodel.Transformer;
import pnnl.goss.powergrid.datamodel.Zone;
import pnnl.goss.powergrid.entities.AreaEntity;
import pnnl.goss.powergrid.entities.BusEntity;
import pnnl.goss.powergrid.entities.GeneratorEntity;
import pnnl.goss.powergrid.entities.BranchEntity;
import pnnl.goss.powergrid.entities.OwnerEntity;
import pnnl.goss.powergrid.entities.PowergridModelEntity;
import pnnl.goss.powergrid.entities.SwitchedShuntEntity;
import pnnl.goss.powergrid.entities.TransformerEntity;
import pnnl.goss.powergrid.entities.ZoneEntity;
import pnnl.goss.powergrid.models.PowergridModel;
import pnnl.goss.powergrid.parsers.ResultLog;

public class PowergridPersist implements PowergridDao {

    /**
     * The connenction to be used for retrieval/storage of entity data.
     */
    EntityManager em;

    public PowergridPersist(EntityManager manager){
        em = manager;
    }

    public PowergridModelEntity retrieve(String powergridMrid){
        Query q= em.createQuery("Select p from PowergridModelEntity p where p.mrid= :mrid"); // where p.powergridName = :powergridName");
        q.setParameter("mrid", powergridMrid);

        PowergridModelEntity entity = (PowergridModelEntity)q.getSingleResult();

        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Powergrid> getAvailablePowergrids() {
        Query q= em.createQuery("Select p from PowergridModelEntity p");
        List<Powergrid> powergrids = new ArrayList<Powergrid>();

        for(PowergridModelEntity entity: (List<PowergridModelEntity>)q.getResultList()){
            powergrids.add(EntityToModelConverter.toPowergrid(entity));
        }

        return powergrids;
    }



    public void persist(PowergridModelEntity model, ResultLog log){

        em.persist(model);

        log.debug("Persisting: "+model.getBusEntities().size()+" Buses.");
        for(BusEntity b: model.getBusEntities()){
            b.setPowergridModel(model);
            em.persist(b);
        }

        log.debug("Persisting: "+model.getGeneratorEntities().size()+" Generators.");
        for(GeneratorEntity g: model.getGeneratorEntities()){
            g.setPowergridModel(model);
            em.persist(g);
        }

        log.debug("Persisting: "+model.getSwitchedShuntEntities().size()+" Switched Shunts.");
        for(SwitchedShuntEntity e: model.getSwitchedShuntEntities()){
            e.setPowergridModel(model);
            em.persist(e);
        }

        log.debug("Persisting: "+model.getBranchEntities().size()+" Lines.");
        for(BranchEntity entity: model.getBranchEntities()){
            entity.setPowergridModel(model);
            em.persist(entity);
        }

        log.debug("Persisting: "+model.getTransformerEntities().size()+" Transformers.");
        for(TransformerEntity entity: model.getTransformerEntities()){
            entity.setPowergridModel(model);
            em.persist(entity);
        }

        log.debug("Persisting: "+model.getZoneEntities().size()+" Zones.");
        for(ZoneEntity entity: model.getZoneEntities()){
            entity.setPowergridModel(model);
            em.persist(entity);
        }

        log.debug("Persisting: "+model.getAreaEntities().size()+" Areas.");
        for(AreaEntity entity: model.getAreaEntities()){
            entity.setPowergridModel(model);
            em.persist(entity);
        }

        log.debug("Persisting: "+model.getOwnerEntities().size()+" Owners.");
        for(OwnerEntity entity: model.getOwnerEntities()){
            em.persist(entity);
        }

        log.setSuccessful(true);
    }

    @Override
    public void persist(Powergrid powergrid) {
//        em = emf.createEntityManager();
//        /*for(Terminal t: car.getTerminals()){
//            em.persist(t);
//
//        }*/
//        for(BusEntity b: powergrid.get)
//        em.persist(powergrid);
//        if (em != null)
//        {
//            em.close();
//            em = null;
//        }
    }



    public Object get(Class<?> entityClass, String id)
    {
        //EntityManager em = emf.createEntityManager();
        Object p = em.find(entityClass, id);
        return p;
    }

    @Override
    public List<String> getPowergridNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Powergrid getPowergridByMrid(String mrid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Powergrid getPowergridByName(String powergridName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Powergrid getPowergridById(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AlertContext getAlertContext(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PowergridModel getPowergridModel(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PowergridModel getPowergridModelAtTime(int powergridId,
            Timestamp timestep) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Timestamp> getTimeSteps(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Area> getAreas(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Branch> getBranches(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Bus> getBuses(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Line> getLines(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Load> getLoads(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Machine> getMachines(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SwitchedShunt> getSwitchedShunts(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Substation> getSubstations(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Transformer> getTransformers(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Zone> getZones(int powergridId) {
        // TODO Auto-generated method stub
        return null;
    }



}
