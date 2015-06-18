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
 * <p>Java class for SwitchedShunt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SwitchedShunt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SwitchedShuntId">
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
 *         &lt;element name="BusNumber">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SwitchedShuntName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bshunt">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Binit">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ModSw">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VswHi">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VswLo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SwRem">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="N1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="B1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="N2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="B2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="N3">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="B3">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Mrid">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="36"/>
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
@XmlType(name = "SwitchedShunt", propOrder = {
    "switchedShuntId",
    "powergridId",
    "busNumber",
    "status",
    "bshunt",
    "binit",
    "modSw",
    "vswHi",
    "vswLo",
    "swRem",
    "n1",
    "b1",
    "n2",
    "b2",
    "n3",
    "b3",
    "mrid"
})
public class SwitchedShunt
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "SwitchedShuntId")
    protected String switchedShuntId;
    @XmlElement(name = "PowergridId")
    protected int powergridId;
    @XmlElement(name = "BusNumber")
    protected int busNumber;
    @XmlElement(name = "Status")
    protected int status;
    @XmlElement(name = "Bshunt")
    protected double bshunt;
    @XmlElement(name = "Binit")
    protected double binit;
    @XmlElement(name = "ModSw")
    protected int modSw;
    @XmlElement(name = "VswHi")
    protected double vswHi;
    @XmlElement(name = "VswLo")
    protected double vswLo;
    @XmlElement(name = "SwRem")
    protected int swRem;
    @XmlElement(name = "N1")
    protected double n1;
    @XmlElement(name = "B1")
    protected double b1;
    @XmlElement(name = "N2")
    protected double n2;
    @XmlElement(name = "B2")
    protected double b2;
    @XmlElement(name = "N3")
    protected double n3;
    @XmlElement(name = "B3")
    protected double b3;
    @XmlElement(name = "Mrid", required = true)
    protected String mrid;

    /**
     * Gets the value of the switchedShuntId property.
     * 
     */
    public String getSwitchedShuntId() {
        return switchedShuntId;
    }

    /**
     * Sets the value of the switchedShuntId property.
     * 
     */
    public void setSwitchedShuntId(String value) {
        this.switchedShuntId = value;
    }

    public boolean isSetSwitchedShuntId() {
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
     * Gets the value of the busNumber property.
     * 
     */
    public int getBusNumber() {
        return busNumber;
    }

    /**
     * Sets the value of the busNumber property.
     * 
     */
    public void setBusNumber(int value) {
        this.busNumber = value;
    }

    public boolean isSetBusNumber() {
        return true;
    }

    /**
     * Gets the value of the status property.
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return true;
    }

    /**
     * Gets the value of the bshunt property.
     * 
     */
    public double getBshunt() {
        return bshunt;
    }

    /**
     * Sets the value of the bshunt property.
     * 
     */
    public void setBshunt(double value) {
        this.bshunt = value;
    }

    public boolean isSetBshunt() {
        return true;
    }

    /**
     * Gets the value of the binit property.
     * 
     */
    public double getBinit() {
        return binit;
    }

    /**
     * Sets the value of the binit property.
     * 
     */
    public void setBinit(double value) {
        this.binit = value;
    }

    public boolean isSetBinit() {
        return true;
    }

    /**
     * Gets the value of the modSw property.
     * 
     */
    public int getModSw() {
        return modSw;
    }

    /**
     * Sets the value of the modSw property.
     * 
     */
    public void setModSw(int value) {
        this.modSw = value;
    }

    public boolean isSetModSw() {
        return true;
    }

    /**
     * Gets the value of the vswHi property.
     * 
     */
    public double getVswHi() {
        return vswHi;
    }

    /**
     * Sets the value of the vswHi property.
     * 
     */
    public void setVswHi(double value) {
        this.vswHi = value;
    }

    public boolean isSetVswHi() {
        return true;
    }

    /**
     * Gets the value of the vswLo property.
     * 
     */
    public double getVswLo() {
        return vswLo;
    }

    /**
     * Sets the value of the vswLo property.
     * 
     */
    public void setVswLo(double value) {
        this.vswLo = value;
    }

    public boolean isSetVswLo() {
        return true;
    }

    /**
     * Gets the value of the swRem property.
     * 
     */
    public int getSwRem() {
        return swRem;
    }

    /**
     * Sets the value of the swRem property.
     * 
     */
    public void setSwRem(int value) {
        this.swRem = value;
    }

    public boolean isSetSwRem() {
        return true;
    }

    /**
     * Gets the value of the n1 property.
     * 
     */
    public double getN1() {
        return n1;
    }

    /**
     * Sets the value of the n1 property.
     * 
     */
    public void setN1(double value) {
        this.n1 = value;
    }

    public boolean isSetN1() {
        return true;
    }

    /**
     * Gets the value of the b1 property.
     * 
     */
    public double getB1() {
        return b1;
    }

    /**
     * Sets the value of the b1 property.
     * 
     */
    public void setB1(double value) {
        this.b1 = value;
    }

    public boolean isSetB1() {
        return true;
    }

    /**
     * Gets the value of the n2 property.
     * 
     */
    public double getN2() {
        return n2;
    }

    /**
     * Sets the value of the n2 property.
     * 
     */
    public void setN2(double value) {
        this.n2 = value;
    }

    public boolean isSetN2() {
        return true;
    }

    /**
     * Gets the value of the b2 property.
     * 
     */
    public double getB2() {
        return b2;
    }

    /**
     * Sets the value of the b2 property.
     * 
     */
    public void setB2(double value) {
        this.b2 = value;
    }

    public boolean isSetB2() {
        return true;
    }

    /**
     * Gets the value of the n3 property.
     * 
     */
    public double getN3() {
        return n3;
    }

    /**
     * Sets the value of the n3 property.
     * 
     */
    public void setN3(double value) {
        this.n3 = value;
    }

    public boolean isSetN3() {
        return true;
    }

    /**
     * Gets the value of the b3 property.
     * 
     */
    public double getB3() {
        return b3;
    }

    /**
     * Sets the value of the b3 property.
     * 
     */
    public void setB3(double value) {
        this.b3 = value;
    }

    public boolean isSetB3() {
        return true;
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

    public boolean isSetMrid() {
        return (this.mrid!= null);
    }

}
