//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2014.03.12 at 11:59:10 AM PDT
//


package pnnl.goss.powergrid.datamodel;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@JsonIgnoreProperties({ "powergridId",
	"setPowergridId", "setCoordinateSystem", "coordinateSystem",
	"identifiedObject"})
public class Area
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected int powergridId;
    protected String areaName;
    protected int areaId;
    protected int isw;
    protected double pdes;
    protected double ptol;
    protected String mrid;

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
     * Gets the value of the areaName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * Sets the value of the areaName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAreaName(String value) {
        this.areaName = value;
    }

    /**
     * Gets the value of the areaId property.
     *
     */
    public int getAreaId() {
        return areaId;
    }

    /**
     * Sets the value of the areaId property.
     *
     */
    public void setAreaId(int value) {
        this.areaId = value;
    }

    /**
     * Gets the value of the isw property.
     *
     */
    public int getIsw() {
        return isw;
    }

    /**
     * Sets the value of the isw property.
     *
     */
    public void setIsw(int value) {
        this.isw = value;
    }

    /**
     * Gets the value of the pdes property.
     *
     */
    public double getPdes() {
        return pdes;
    }

    /**
     * Sets the value of the pdes property.
     *
     */
    public void setPdes(double value) {
        this.pdes = value;
    }

    /**
     * Gets the value of the ptol property.
     *
     */
    public double getPtol() {
        return ptol;
    }

    /**
     * Sets the value of the ptol property.
     *
     */
    public void setPtol(double value) {
        this.ptol = value;
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

}
