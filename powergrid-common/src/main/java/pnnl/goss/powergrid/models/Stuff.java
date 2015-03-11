package pnnl.goss.powergrid.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Stuff {

    @XmlElement
    private String data;
    private String data2;

    public Stuff(){
        data = "Data stuff";
        data2 = "Data2 stuff";
    }

}
