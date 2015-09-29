
package pnnl.goss.powergrid.datamodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PowergridRating complex type.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PowergridProvenanceChanges", propOrder = {
    "powerGridProvenanceChangeId",
    "powerGridProvenanceId",
    "objectNewId",
    "objectPreviousId",
    "objectType"
})
public class PowergridProvenanceChanges
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "PowerGridProvenanceChangeId")
    protected int powerGridProvenanceChangeId;
    @XmlElement(name = "PowerGridProvenanceId")
    protected int powerGridProvenanceId;
    @XmlElement(name = "ObjectNewId", required = true)
    protected int objectNewId;
    @XmlElement(name = "ObjectOldId", required = true)
    protected int objectOldId;
    @XmlElement(name = "ObjectType", required = true)
    protected String objectType;
	public int getPowerGridProvenanceChangeId() {
		return powerGridProvenanceChangeId;
	}
	public void setPowerGridProvenanceChangeId(int powerGridProvenanceChangeId) {
		this.powerGridProvenanceChangeId = powerGridProvenanceChangeId;
	}
	public int getPowerGridProvenanceId() {
		return powerGridProvenanceId;
	}
	public void setPowerGridProvenanceId(int powerGridProvenanceId) {
		this.powerGridProvenanceId = powerGridProvenanceId;
	}
	public int getObjectNewId() {
		return objectNewId;
	}
	public void setObjectNewId(int objectNewId) {
		this.objectNewId = objectNewId;
	}
	public int getObjectOldId() {
		return objectOldId;
	}
	public void setObjectOldId(int objectOldId) {
		this.objectOldId = objectOldId;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
    
    

}
