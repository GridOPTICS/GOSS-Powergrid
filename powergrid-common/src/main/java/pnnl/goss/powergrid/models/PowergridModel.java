package pnnl.goss.powergrid.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import pnnl.goss.powergrid.datamodel.Bus;
import pnnl.goss.powergrid.datamodel.Powergrid;

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

    public List<Bus> getBuses(){
        return buses;
    }

    public void setBuses(List<Bus> buses){
        this.buses = buses;
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
