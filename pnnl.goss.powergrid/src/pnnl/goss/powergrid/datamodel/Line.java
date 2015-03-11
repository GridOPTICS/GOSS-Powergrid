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


/**
 * <p>Java class for Line complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Line">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LineId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PowergridId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BranchId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Gi">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bi">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Gj">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bj">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bcap">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Line", propOrder = {
    "lineId",
    "powergridId",
    "branchId",
    "gi",
    "bi",
    "gj",
    "bj",
    "bcap"
})
public class Line
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "LineId")
    protected int lineId;
    @XmlElement(name = "PowergridId")
    protected int powergridId;
    @XmlElement(name = "BranchId")
    protected Integer branchId;
    @XmlElement(name = "Gi")
    protected double gi;
    @XmlElement(name = "Bi")
    protected double bi;
    @XmlElement(name = "Gj")
    protected double gj;
    @XmlElement(name = "Bj")
    protected double bj;
    @XmlElement(name = "Bcap")
    protected double bcap;

    /**
     * Gets the value of the lineId property.
     * 
     */
    public int getLineId() {
        return lineId;
    }

    /**
     * Sets the value of the lineId property.
     * 
     */
    public void setLineId(int value) {
        this.lineId = value;
    }

    public boolean isSetLineId() {
        return true;
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

    public boolean isSetPowergridId() {
        return true;
    }

    /**
     * Gets the value of the branchId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * Sets the value of the branchId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBranchId(Integer value) {
        this.branchId = value;
    }

    public boolean isSetBranchId() {
        return (this.branchId!= null);
    }

    /**
     * Gets the value of the gi property.
     * 
     */
    public double getGi() {
        return gi;
    }

    /**
     * Sets the value of the gi property.
     * 
     */
    public void setGi(double value) {
        this.gi = value;
    }

    public boolean isSetGi() {
        return true;
    }

    /**
     * Gets the value of the bi property.
     * 
     */
    public double getBi() {
        return bi;
    }

    /**
     * Sets the value of the bi property.
     * 
     */
    public void setBi(double value) {
        this.bi = value;
    }

    public boolean isSetBi() {
        return true;
    }

    /**
     * Gets the value of the gj property.
     * 
     */
    public double getGj() {
        return gj;
    }

    /**
     * Sets the value of the gj property.
     * 
     */
    public void setGj(double value) {
        this.gj = value;
    }

    public boolean isSetGj() {
        return true;
    }

    /**
     * Gets the value of the bj property.
     * 
     */
    public double getBj() {
        return bj;
    }

    /**
     * Sets the value of the bj property.
     * 
     */
    public void setBj(double value) {
        this.bj = value;
    }

    public boolean isSetBj() {
        return true;
    }

    /**
     * Gets the value of the bcap property.
     * 
     */
    public double getBcap() {
        return bcap;
    }

    /**
     * Sets the value of the bcap property.
     * 
     */
    public void setBcap(double value) {
        this.bcap = value;
    }

    public boolean isSetBcap() {
        return true;
    }

}