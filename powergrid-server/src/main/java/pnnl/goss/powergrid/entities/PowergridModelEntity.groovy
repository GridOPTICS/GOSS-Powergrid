package pnnl.goss.powergrid.entities

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import pnnl.goss.powergrid.models.PowergridModel;

@Entity
class PowergridModelEntity {

    /**
     * Unique id for this powergrid.
     */
    @Id
    @Column(name="powergridmodel_mrid")
    String mrid

    /**
     * A name that the user can use to lookup the powergrid from webservice.
     * The name does not need to be unique.
     */
    String powergridName

    /**
     * User entered data about the characteristics of the model.  This is not
     * a required field.
     */
    String characteristics

    /**
     *
     */
    double SBase;

    /**
     * The timestep that this entity was created/recorded at.
     */
    Date timeStep

    /**
     * A list of buses that belong to this powergrid.
     */
    @OneToMany (targetEntity=BusEntity.class)
    //@JoinColumn(name="bus_mrid")
    List<BusEntity> busEntities
//
    @OneToMany (targetEntity=GeneratorEntity.class)
    List<GeneratorEntity> generatorEntities;

    @OneToMany (targetEntity=BranchEntity.class)
    List<BranchEntity> branchEntities;

    @OneToMany(targetEntity=TransformerEntity.class)
    List<TransformerEntity> transformerEntities

    @OneToMany(targetEntity=OwnerEntity.class)
    List<OwnerEntity> ownerEntities

    @OneToMany(targetEntity=ZoneEntity.class)
    List<ZoneEntity> zoneEntities

    @OneToMany(targetEntity=AreaEntity.class)
    List<AreaEntity> areaEntities

    @OneToMany(targetEntity=SwitchedShuntEntity.class)
    List<SwitchedShuntEntity> switchedShuntEntities


    BusEntity getBusByBusNumber(int busNumber){
        BusEntity entity = busEntities.find { b ->b.busNumber == busNumber}
        entity
    }

    BusEntity getBusByMrid(String mrid){
        BusEntity entity = busEntities.find { b ->b.mrid == mrid}
        entity
    }


//    void from(PowergridModel powergrid){
//        mrid = powergrid.mrid
//        powergridName = powergrid.name
//        timeStep = powergrid.timeStep
//
//    }
//
//    void to(PowergridModel powergrid) {
//        powergrid.mrid = mrid
//        powergrid.name = powergridName
////        powergrid.timeStep = timeStep
//    }
//
}
