package pnnl.goss.powergrid.requests;

public class RequestPowergridPart extends RequestPowergrid {

	private static final long serialVersionUID = 5043449567938118846L;
	
	public enum PowergridPartType{
		BUSES, BRANCHES, SWITCHED_SHUNTS, MACHINES, LOADS
	}
	
	private PowergridPartType requestedPartType;
	
	public RequestPowergridPart(String mrid, PowergridPartType requestedPartType) {
		super(mrid);
		this.requestedPartType = requestedPartType;
	}

	public PowergridPartType getRequestedPartType(){
		return this.requestedPartType;
	}
}
