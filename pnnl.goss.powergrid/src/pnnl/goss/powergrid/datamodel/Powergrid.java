//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2014.03.12 at 11:59:10 AM PDT
//


package pnnl.goss.powergrid.datamodel;

import java.io.Serializable;

import pnnl.goss.powergrid.api.topology.IdentifiedObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;

@JsonIgnoreProperties({ "powergridId",
	"setPowergridId", "setCoordinateSystem", "coordinateSystem",
	"identifiedObject"})
public class Powergrid
    implements Serializable
{

	public final static String POWERGRID_DATATYPE = "Powergrid";

    private final static long serialVersionUID = 12343L;

    protected int powergridId;

    @JsonProperty("powergridName")
    protected String name;
    //@XmlElement(name = "CoordinateSystem", required = true)

    protected String coordinateSystem;

    @JsonProperty("mrid")
    protected String mrid;


    /**
     * PTI-Case identifier
     */
    @JsonProperty("caseIdentifier")
    private String caseIdentifier;

    /**
     * System mva base
     */
    @JsonProperty("mvaSystemBase")
    private Double sbase;

    /**
     * The identifing element for this object.
     */
    private IdentifiedObject identifiedObject;
    
    private JsonObject otherProperties;

    public Powergrid(){
    	// TODO Remove hard coding here!
    	sbase=100.0;
    	caseIdentifier = "Greek-118-North";
    }
    
    public void setOtherProperties(JsonObject object){
    	otherProperties = object;
    }
    
    public JsonObject getOtherProperties(){
    	return otherProperties;
    }


    /**
     * Gets the value of the powergridId property.
     *
     */
    public int getPowergridId() {
        return powergridId;
    }

    /**
     * Sets the value of the powergridId property.
     *
     */
    public void setPowergridId(int value) {
        this.powergridId = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the coordinateSystem property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * Sets the value of the coordinateSystem property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCoordinateSystem(String value) {
        this.coordinateSystem = value;
    }

    /**
     * Gets the value of the mrid property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMrid() {
        return mrid;
    }

    /**
     * Sets the value of the mrid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMrid(String value) {
        this.mrid = value;
    }

	public String getCaseIdentifier() {
		return caseIdentifier;
	}

	public void setCaseIdentifier(String caseIdentifier) {
		this.caseIdentifier = caseIdentifier;
	}

	public Double getSbase() {
		return sbase;
	}

	public void setSbase(Double sbase) {
		this.sbase = sbase;
	}

	public IdentifiedObject getIdentifiedObject() {
		return identifiedObject;
	}

	public void setIdentifiedObject(IdentifiedObject identifiedObject) {
		this.identifiedObject = identifiedObject;
	}
}
