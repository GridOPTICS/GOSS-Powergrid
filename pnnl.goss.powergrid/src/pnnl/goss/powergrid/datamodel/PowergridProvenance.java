

package pnnl.goss.powergrid.datamodel;

import java.io.Serializable;
import java.util.Date;

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
@XmlType(name = "PowergridProvenance", propOrder = {
    "powergridProvenanceId",
    "powergridId",
    "previousPowerGridId",
    "user",
    "rating",
    "comment",
    "created"
})
public class PowergridProvenance
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "PowerGridProvenanceId")
    protected int powergridProvenanceId;
    @XmlElement(name = "PowerGridId")
    protected int powergridId;
    @XmlElement(name = "PreviousPowerGridId")
    protected int previousPowerGridId;
    @XmlElement(name = "Action", required = true)
    protected String action;
    @XmlElement(name = "User", required = true)
    protected String user;
    @XmlElement(name = "Comments", required = true)
    protected String comments;
    @XmlElement(name = "Created", required = true)
    protected Date created;
    
    
	public int getPowergridProvenanceId() {
		return powergridProvenanceId;
	}
	public void setPowergridProvenanceId(int powergridProvenanceId) {
		this.powergridProvenanceId = powergridProvenanceId;
	}
	public int getPreviousPowerGridId() {
		return previousPowerGridId;
	}
	public void setPreviousPowerGridId(int previousPowerGridId) {
		this.previousPowerGridId = previousPowerGridId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getPowergridId() {
		return powergridId;
	}
	public void setPowergridId(int powergridId) {
		this.powergridId = powergridId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}


}
