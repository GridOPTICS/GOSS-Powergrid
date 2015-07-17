package pnnl.goss.powergrid.requests;

import com.google.gson.JsonObject;

import pnnl.goss.core.Request;


public class RequestEnvelope extends Request {

	private static final long serialVersionUID = 6890406898866512695L;
	private final JsonObject params;
	private final Request request;
	
	public JsonObject getParams(){
		return this.params;
	}
	
	public Request getWrappedRequest(){
		return request;
	}
	
	public RequestEnvelope(Request request, JsonObject params){
		this.request = request;
		this.params = params;
	}
	
}
