

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
@XmlType(name = "PowergridObjectAnnotation", propOrder = {
    "powerGridObjectAnnotationId",
    "powerGridId",
    "objectId",
    "user",
    "rating",
    "comments",
    "created",
    "objectType"
})
public class PowergridObjectAnnotation
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "PowerGridObjectAnnotationId")
    protected int powerGridObjectAnnotationId;
    @XmlElement(name = "PowerGridId")
    protected int powergridId;
    @XmlElement(name = "ObjectId")
    protected int objectId;
    @XmlElement(name = "ObjectType", required = true)
    protected String objectType;
    @XmlElement(name = "User", required = true)
    protected String user;
    @XmlElement(name = "Comments", required = true)
    protected String comments;
    @XmlElement(name = "Created", required = true)
    protected Date created;
    
    
	public int getPowerGridObjectAnnotationId() {
		return powerGridObjectAnnotationId;
	}
	public void setPowerGridObjectAnnotationId(int powerGridObjectAnnotationId) {
		this.powerGridObjectAnnotationId = powerGridObjectAnnotationId;
	}
	public int getPowergridId() {
		return powergridId;
	}
	public void setPowergridId(int powergridId) {
		this.powergridId = powergridId;
	}
	public int getObjectId() {
		return objectId;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
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
