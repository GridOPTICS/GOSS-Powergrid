package pnnl.goss.powergrid.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Junk {
    private String help;

    public String getHelp(){
        return help;
    }

    public void setHelp(String message){
        help = message;
    }
}
