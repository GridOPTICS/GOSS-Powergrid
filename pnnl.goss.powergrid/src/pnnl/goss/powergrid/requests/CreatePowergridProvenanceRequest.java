package pnnl.goss.powergrid.requests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import pnnl.goss.core.Request;

public class CreatePowergridProvenanceRequest extends Request {

	private static final long serialVersionUID = -5144546768914835330L;
	private String action;
	private String user;
	private String comments;
	private String mrid;
	private String previousMrid;
	private Date created;
		
	
	public CreatePowergridProvenanceRequest(){
		created = new Date();
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	public String getMrid() {
		return mrid;
	}


	public void setMrid(String mrid) {
		this.mrid = mrid;
	}


	public String getPreviousMrid() {
		return previousMrid;
	}


	public void setPreviousMrid(String previousMrid) {
		this.previousMrid = previousMrid;
	}


	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}
	
	

}
