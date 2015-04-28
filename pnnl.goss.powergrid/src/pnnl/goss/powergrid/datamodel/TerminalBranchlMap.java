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
 * <p>Java class for TerminalBranchlMap complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TerminalBranchlMap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FromOrToTerminal">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
 *         &lt;element name="TerminalId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
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
@XmlType(name = "TerminalBranchlMap", propOrder = {
    "fromOrToTerminal",
    "branchId",
    "powergridId",
    "terminalId"
})
public class TerminalBranchlMap
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "FromOrToTerminal")
    protected int fromOrToTerminal;
    @XmlElement(name = "BranchId")
    protected int branchId;
    @XmlElement(name = "PowergridId")
    protected int powergridId;
    @XmlElement(name = "TerminalId")
    protected int terminalId;

    /**
     * Gets the value of the fromOrToTerminal property.
     * 
     */
    public int getFromOrToTerminal() {
        return fromOrToTerminal;
    }

    /**
     * Sets the value of the fromOrToTerminal property.
     * 
     */
    public void setFromOrToTerminal(int value) {
        this.fromOrToTerminal = value;
    }

    public boolean isSetFromOrToTerminal() {
        return true;
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
     * Gets the value of the terminalId property.
     * 
     */
    public int getTerminalId() {
        return terminalId;
    }

    /**
     * Sets the value of the terminalId property.
     * 
     */
    public void setTerminalId(int value) {
        this.terminalId = value;
    }

    public boolean isSetTerminalId() {
        return true;
    }

}
