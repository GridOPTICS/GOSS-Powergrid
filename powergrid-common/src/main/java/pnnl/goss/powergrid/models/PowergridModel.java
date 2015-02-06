package pnnl.goss.powergrid.models;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import pnnl.goss.powergrid.datamodel.Powergrid;

@XmlRootElement(name="PowergridModel")
public class PowergridModel extends BaseModel {

    /**
     * The current instances of a powergrid that this model is representing.
     */
    private Powergrid powergrid;

    /**
     * The timestep that this powergrid represents.
     */
    private Date timestep = null;

    public PowergridModel(){

    }

    public PowergridModel(Powergrid powergrid, Date timestep){
        this.powergrid = powergrid;
        this.timestep = timestep;
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


}