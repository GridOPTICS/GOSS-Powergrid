package pnnl.goss.powergrid.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import pnnl.goss.powergrid.datamodel.Area;
import pnnl.goss.powergrid.datamodel.Branch;
import pnnl.goss.powergrid.datamodel.Bus;
import pnnl.goss.powergrid.datamodel.Line;
import pnnl.goss.powergrid.datamodel.Load;
import pnnl.goss.powergrid.datamodel.Machine;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.SwitchedShunt;
import pnnl.goss.powergrid.datamodel.Transformer;
import pnnl.goss.powergrid.datamodel.Zone;

@XmlRootElement(name="PowergridModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class PowergridModel implements Serializable{
    /**
     * The current instances of a powergrid that this model is representing.
     */
    //@XmlElementWrapper(name="Powergrids")
    @XmlElement(name="Powergrid")
    private Powergrid powergrid;

    /**
     * The timestep that this powergrid represents.
     */
    @XmlElement
    private Date timestep = null;

    @XmlElementWrapper(name="Buses")
    @XmlElement(name="Bus" )
    List<Bus> buses = null;

    @XmlElementWrapper(name="Machines")
    @XmlElement(name="Machine" )
    List<Machine> machines = null;

    //List<Owner> owners = null;
    @XmlElementWrapper(name="Areas")
    @XmlElement(name="Area" )
    List<Area> areas = null;

    @XmlElementWrapper(name="Zones")
    @XmlElement(name="Zone" )
    List<Zone> zones = null;

    @XmlElementWrapper(name="Transformers")
    @XmlElement(name="Transformer" )
    List<Transformer> transformers = null;

    @XmlElementWrapper(name="Branches")
    @XmlElement(name="Branch" )
    List<Branch> branches = null;

    @XmlElementWrapper(name="Lines")
    @XmlElement(name="Line" )
    List<Line> lines = null;

    @XmlElementWrapper(name="Loads")
    @XmlElement(name="Load" )
    List<Load> loads = null;

    @XmlElementWrapper(name="SwitchedShunts")
    @XmlElement(name="SwitchedShunt" )
    List<SwitchedShunt> switchedShunts = null;

    @XmlTransient
    Map<Integer, Bus> busMap = new HashMap<>();


    public List<SwitchedShunt> getSwitchedShunts() {
        return switchedShunts;
    }

    public void setSwitchedShunts(List<SwitchedShunt> switchedShunts) {
        this.switchedShunts = switchedShunts;
    }

    public List<Bus> getBuses(){
        return buses;
    }

    public void setBuses(List<Bus> buses){
        for(Bus b: buses){
            busMap.put(b.getBusNumber(), b);
        }
        this.buses = buses;
    }

    public Bus getBus(int busNumber){
        return busMap.get(busNumber);
    }

    public void setMachines(List<Machine> machineList) {
        machines = machineList;
    }

    public List<Machine> getMachines(){
        return machines;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public List<Transformer> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<Transformer> transformers) {
        this.transformers = transformers;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines(){
        return this.lines;
    }


    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public List<Load> getLoads() {
        return loads;
    }

    public void setLoads(List<Load> loads) {
        this.loads = loads;
    }

    public Powergrid getPowergrid() {
        return powergrid;
    }

    public void setPowergrid(Powergrid powergrid) {
        this.powergrid = powergrid;
    }

    public Date getTimestep() {
        return timestep;
    }

    public void setTimestep(Date timestep) {
        this.timestep = timestep;
    }


    public ValidationErrors validate() {
        return null;
        //return super.validate();

//        ValidationErrors errors = super.validate()
//
//        if (timestep == null) {
//            errors.addError("Invalid timestep for powergrid model.")
//        }
//        if (powergrid == null){
//            errors.addError("Powergrid object not valid for powergrid model.")
//        }
//
//        return errors
    }



}
