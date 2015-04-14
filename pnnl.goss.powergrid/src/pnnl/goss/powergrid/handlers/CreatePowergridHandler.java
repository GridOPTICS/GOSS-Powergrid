package pnnl.goss.powergrid.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;

import pnnl.goss.core.DataError;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.Response;
import pnnl.goss.core.security.AuthorizationHandler;
import pnnl.goss.core.security.AuthorizeAll;
import pnnl.goss.core.server.RequestHandler;
import pnnl.goss.powergrid.parser.api.InvalidDataException;
import pnnl.goss.powergrid.parser.api.ParserService;
import pnnl.goss.powergrid.parsers.ParserServiceImpl;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;

@Component
public class CreatePowergridHandler implements RequestHandler {
	
	//@ServiceDependency
	private volatile ParserService parserService = new ParserServiceImpl();
	

	@Override
	public Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> getHandles() {
		Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> handles = new HashMap<>();
		handles.put(CreatePowergridRequest.class, AuthorizeAll.class);
		return handles;
	}

	@Override
	public Response handle(Request request) {

		CreatePowergridRequest pgRequest = (CreatePowergridRequest)request;
		
		if (pgRequest.getPowergridContent() == null || pgRequest.getPowergridContent().trim().isEmpty()){
			return new DataResponse(new DataError("Invalid powergrid content specified!"));
		}
			
		DataResponse response = new DataResponse();
		
		try {
			parserService.parse("Psse23Definitions", IOUtils.toInputStream(pgRequest.getPowergridContent()));
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.setData(UUID.randomUUID().toString());
		
		return response;
	}

}
