package pnnl.goss.powergrid.parser.api;

import pnnl.goss.core.Request;

public interface RequestSubjectService {

	void addRequest(Request request, String identity);

	String getIdentity(Request request);

}
