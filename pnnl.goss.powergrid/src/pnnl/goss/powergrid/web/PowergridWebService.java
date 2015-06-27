package pnnl.goss.powergrid.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.Notes;
import org.amdatu.web.rest.doc.ResponseMessage;
import org.amdatu.web.rest.doc.ResponseMessages;
import org.amdatu.web.rest.doc.ReturnType;

import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.server.HandlerNotFoundException;
import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.api.PowergridService;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.collections.PowergridList;
import pnnl.goss.powergrid.requests.RequestPowergridList;

@Path("/powergrid/api")
public class PowergridWebService {

	private volatile PowergridService powergridService;
	private volatile RequestHandlerRegistry handlers;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GET
	@Produces({MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.TEXT_HTML})
//	@Description(
//			"Returns a powergrid instance that contains information"
//			a friendly JSON message")
	public Collection<Powergrid> list(@Context HttpServletRequest req){
		RequestPowergridList reqList = new RequestPowergridList();
		String subject = (String)req.getAttribute("subject");
		List<Powergrid> data = null;
		if (handlers.checkAccess((Request)reqList, subject)){
			DataResponse res;
			try {
				res = (DataResponse)handlers.handle(reqList);
				data = ((PowergridList)res.getData()).toList();
			} catch (HandlerNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				data = new ArrayList<>();
			}
		}
		return data;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON,
		MediaType.APPLICATION_XML})
	@Path("{mrid}")
	public PowergridModel getPowergridByMrid(@PathParam("mrid") String mrid){
		return powergridService.getPowergridModel(mrid);

	}

//	@GET
//	@Path("myPath")
//	@Produces({ MediaType.APPLICATION_JSON })
//	@Description("Returns a friendly JSON message")
//	@Notes("This is an example")
//	@ResponseMessages({ @ResponseMessage(code = 200, message = "In case of success") })
//	@ReturnType(String.class)
//	public Response helloResponse() {
//	  return Response.ok("\"hello world\"").build();
//	}

}
