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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Branch complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Branch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BranchId">
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
 *         &lt;element name="FromBusNumber">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ToBusNumber">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IndexNum">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Ckt">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="R">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="X">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Rating">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RateA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RateB">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RateC">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="P">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Q">
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
@XmlType(name = "Branch", propOrder = {
    "branchId",
    "powergridId",
    "fromBusNumber",
    "toBusNumber",
    "indexNum",
    "ckt",
    "r",
    "x",
    "b",
    "rateA",
    "rateB",
    "rateC",
    "status",
    "gI",
    "bI",
    "gJ",
    "bJ",
    "p",
    "q",
    "mrid"
})
public class Branch
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "BranchId")
    protected int branchId;
    @XmlElement(name = "PowergridId")
    protected int powergridId;
    @XmlElement(name = "FromBusNumber")
    protected int fromBusNumber;
    @XmlElement(name = "ToBusNumber")
    protected int toBusNumber;
    @XmlElement(name = "IndexNum")
    protected int indexNum;
    @XmlElement(name = "Ckt", required = true)
    protected String ckt;
    @XmlElement(name = "R")
    protected double r;
    @XmlElement(name = "X")
    protected double x;
    @XmlElement(name = "B")
    protected double b;
    @XmlElement(name = "RateA")
    protected double rateA;
    @XmlElement(name = "RateB")
    protected double rateB;
    @XmlElement(name = "RateC")
    protected double rateC;
    @XmlElement(name = "Gi")
    protected double gI;
    @XmlElement(name = "Bi")
    protected double bI;
    @XmlElement(name = "Gj")
    protected double gJ;
    @XmlElement(name = "Bj")
    protected double bJ;
    @XmlElement(name = "Status")
    protected int status;
    @XmlElement(name = "P")
    protected double p;
    @XmlElement(name = "Q")
    protected double q;
    @XmlElement(name = "Mrid", required = true)
    protected String mrid;

    @XmlTransient
    private boolean isLineElement;

    public boolean isLine(){
        return isLineElement;
    }

    public boolean isTransformer(){
        return !isLineElement;
    }

    public void setIsLineElement(boolean value){
        isLineElement = value;
    }


    public double getGi() {
        return gI;
    }

    public void setGi(double gI) {
        this.gI = gI;
    }

    public double getBi() {
        return bI;
    }

    public void setBi(double bI) {
        this.bI = bI;
    }

    public double getGj() {
        return gJ;
    }

    public void setGj(double gJ) {
        this.gJ = gJ;
    }

    public double getBj() {
        return bJ;
    }

    public void setBj(double bJ) {
        this.bJ = bJ;
    }

    /**
     * Gets the value of the branchId property.
     *
     */
    public int getBranchId() {
        return branchId;
    }

    /**
     * Sets the value of the branchId property.
     *
     */
    public void setBranchId(int value) {
        this.branchId = value;
    }

    public boolean isSetBranchId() {
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
     * Gets the value of the fromBusNumber property.
     *
     */
    public int getFromBusNumber() {
        return fromBusNumber;
    }

    /**
     * Sets the value of the fromBusNumber property.
     *
     */
    public void setFromBusNumber(int value) {
        this.fromBusNumber = value;
    }

    public boolean isSetFromBusNumber() {
        return true;
    }

    /**
     * Gets the value of the toBusNumber property.
     *
     */
    public int getToBusNumber() {
        return toBusNumber;
    }

    /**
     * Sets the value of the toBusNumber property.
     *
     */
    public void setToBusNumber(int value) {
        this.toBusNumber = value;
    }

    public boolean isSetToBusNumber() {
        return true;
    }

    /**
     * Gets the value of the indexNum property.
     *
     */
    public int getIndexNum() {
        return indexNum;
    }

    /**
     * Sets the value of the indexNum property.
     *
     */
    public void setIndexNum(int value) {
        this.indexNum = value;
    }

    public boolean isSetIndexNum() {
        return true;
    }

    /**
     * Gets the value of the ckt property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCkt() {
        return ckt;
    }

    /**
     * Sets the value of the ckt property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCkt(String value) {
        this.ckt = value;
    }

    public boolean isSetCkt() {
        return (this.ckt!= null);
    }

    /**
     * Gets the value of the r property.
     *
     */
    public double getR() {
        return r;
    }

    /**
     * Sets the value of the r property.
     *
     */
    public void setR(double value) {
        this.r = value;
    }

    public boolean isSetR() {
        return true;
    }

    /**
     * Gets the value of the x property.
     *
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     *
     */
    public void setX(double value) {
        this.x = value;
    }

    public boolean isSetX() {
        return true;
    }


    /**
     * Gets the value of the rateA property.
     *
     */
    public double getRateA() {
        return rateA;
    }

    /**
     * Sets the value of the rateA property.
     *
     */
    public void setRateA(double value) {
        this.rateA = value;
    }

    public boolean isSetRateA() {
        return true;
    }

    /**
     * Gets the value of the rateB property.
     *
     */
    public double getRateB() {
        return rateB;
    }

    /**
     * Sets the value of the rateB property.
     *
     */
    public void setRateB(double value) {
        this.rateB = value;
    }

    public boolean isSetRateB() {
        return true;
    }

    /**
     * Gets the value of the rateC property.
     *
     */
    public double getRateC() {
        return rateC;
    }

    /**
     * Sets the value of the rateC property.
     *
     */
    public void setRateC(double value) {
        this.rateC = value;
    }

    public boolean isSetRateC() {
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
     * Gets the value of the p property.
     *
     */
    public double getP() {
        return p;
    }

    /**
     * Sets the value of the p property.
     *
     */
    public void setP(double value) {
        this.p = value;
    }

    public boolean isSetP() {
        return true;
    }

    /**
     * Gets the value of the q property.
     *
     */
    public double getQ() {
        return q;
    }

    /**
     * Sets the value of the q property.
     *
     */
    public void setQ(double value) {
        this.q = value;
    }

    public boolean isSetQ() {
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

    public void setB(double b) {
        this.b = b;
    }

    public double getB(){
        return b;
    }

}
