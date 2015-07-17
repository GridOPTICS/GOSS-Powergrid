package pnnl.goss.powergrid.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.dm.annotation.api.Component;

import pnnl.goss.core.Request;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;

@Component
public class RequestSubjectMap implements RequestSubjectService{

	Map<String, String> idAuthMap = new HashMap<>();

	@Override
	public void addRequest(Request request, String identity) {
		idAuthMap.put(request.getId(), identity);
	}

	@Override
	public String getIdentity(Request request) {
		return idAuthMap.get(request.getId());
	}

}
