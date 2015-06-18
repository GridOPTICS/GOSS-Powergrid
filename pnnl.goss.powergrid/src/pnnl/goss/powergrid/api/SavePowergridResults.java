package pnnl.goss.powergrid.api;

import java.io.Serializable;
import java.util.List;

public class SavePowergridResults implements Serializable{
	
	private static final long serialVersionUID = 1538240556922528221L;
	private final List<String> errorsAndWarnings;
	private final String powergridGuid;
	private final boolean success;
	
	public SavePowergridResults(String guid, 
			List<String> errorsAndWarnings ){
		this.success = (guid != null);
		this.powergridGuid = guid;
		this.errorsAndWarnings = errorsAndWarnings;
	}

	public List<String> getErrorsAndWarnings() {
		return errorsAndWarnings;
	}

	public String getPowergridGuid() {
		return powergridGuid;
	}

	public boolean isSuccess() {
		return success;
	}

}
