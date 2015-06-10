package pnnl.goss.powergrid.requests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;

import pnnl.goss.core.Request;
import pnnl.goss.core.UploadRequest;

public class CreatePowergridRequest extends Request {

    private static final long serialVersionUID = -5144546768914835330L;
    private String powergridContent;
    private String coordinateSystem;
    private String powergridName;
    private String mrid;
    private String powergridType;
    
    public void setPowergridContent(String powergridContent) {
		this.powergridContent = powergridContent;
	}

	public void setPowergridType(String value){
    	this.powergridType = value;
    }
    
    public String getPowergridType(){
    	return this.powergridType;
    }

    public String fileContent(){
        return powergridContent;
    }

    public String getPowergridContent() {
        return powergridContent;
    }


    public String getCoordinateSystem() {
        return coordinateSystem;
    }



    public void setCoordinateSystem(String coordinateSystem) {
        this.coordinateSystem = coordinateSystem;
    }



    public String getPowergridName() {
        return powergridName;
    }



    public void setPowergridName(String powergridName) {
        this.powergridName = powergridName;
    }



    public String getMrid() {
        return mrid;
    }



    public void setMrid(String mrid) {
        this.mrid = mrid;
    }



    public void setFile(File file) throws FileNotFoundException{
        if (!file.exists()) throw new FileNotFoundException();

        try {
        	powergridContent = FileUtils.readFileToString(file);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }







}
