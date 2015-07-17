package pnnl.goss.powergrid.requests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;

import pnnl.goss.core.Request;

public class CreatePowergridRequest extends Request {

	private static final long serialVersionUID = -5144546768914835330L;
	private String powergridContent;
	private String substationMappingContent;
	private String coordinateSystem;
	private String powergridName;
	private String importVersion;
	private String accessLevel;
	private String originalFilename;
	private String description;
		

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public CreatePowergridRequest() {
		importVersion = "PSSE_23";
		powergridName = "Powergrid Model";
	}

	public void setSubstationMap(String substationMap) {
		this.substationMappingContent = substationMap;
	}

	public String getSubstationMap() {
		return this.substationMappingContent;
	}

	public void setImportVersion(String version) {
		this.importVersion = version;
	}

	public String getImportVersion() {
		return this.importVersion;
	}

	public void setPowergridContent(String powergridContent) {
		this.powergridContent = powergridContent;
	}

	public String fileContent() {
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

	public void setSubstationMappingFile(File file)
			throws FileNotFoundException {
		if (!file.exists())
			throw new FileNotFoundException();

		try {
			this.substationMappingContent = FileUtils.readFileToString(file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPowergridFile(File file) throws FileNotFoundException {
		if (!file.exists())
			throw new FileNotFoundException();

		try {
			powergridContent = FileUtils.readFileToString(file);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
