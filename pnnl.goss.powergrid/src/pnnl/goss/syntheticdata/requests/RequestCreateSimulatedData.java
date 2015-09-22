package pnnl.goss.syntheticdata.requests;

import pnnl.goss.core.Request;

public class RequestCreateSimulatedData  extends Request {

	private static final long serialVersionUID = -865180786048472378L;


	public static RequestCreateSimulatedData create() {
		return new RequestCreateSimulatedData();
	}

}
