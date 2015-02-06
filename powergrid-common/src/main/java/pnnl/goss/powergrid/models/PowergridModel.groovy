package pnnl.goss.powergrid.models

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import pnnl.goss.powergrid.datamodel.Powergrid;

@XmlRootElement(name="PowergridModel")
class PowergridModel extends BaseModel {

    /**
     * The current instances of a powergrid that this model is representing.
     */
    private Powergrid powergrid;

    /**
     * The timestep that this powergrid represents.
     */
    private Date timestep = null;

}
