package pnnl.goss.powergrid;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="PowergridCreationReport")
public class PowergridCreationReport implements Serializable{
    private static final long serialVersionUID = -8588648862177544208L;
    @XmlElement(name = "Success")
    protected boolean successful = false;

    @XmlElement(name = "PowergridMrid")
    protected String powergridMrid;

    @XmlElementWrapper(name="Log")
    @XmlElement(name="Entry", type=String.class)
    protected List<String> items;

    public PowergridCreationReport(){
        // Required for jaxb
    }

    public PowergridCreationReport(List<String> items, boolean successful){
        this.items= items;
        this.successful = successful;

    }

    public void setPowergridMrid(String mrid){
        powergridMrid = mrid;
    }

    public boolean wasSuccessful(){
        return successful;
    }

    public Collection<String> getReport(){
        return items;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Successful? ");
        buf.append(successful);
        buf.append("\n");
        for(String s: items){
            buf.append(s);
            buf.append("\n");
        }
        return buf.toString();
    }
}
