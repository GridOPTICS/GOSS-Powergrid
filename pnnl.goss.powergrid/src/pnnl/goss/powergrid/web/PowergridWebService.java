package pnnl.goss.powergrid.web;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.Notes;
import org.amdatu.web.rest.doc.ResponseMessage;
import org.amdatu.web.rest.doc.ResponseMessages;

import pnnl.goss.powergrid.api.PowergridService;
import pnnl.goss.powergrid.datamodel.Powergrid;

@Path("/powergrid/api")
public class PowergridWebService {

	private volatile PowergridService powergridService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Powergrid> list(){
		return powergridService.getPowergrids();
	}

	@GET
	@Path("myPath")
	@Produces({ MediaType.APPLICATION_JSON })
	@Description("Returns a friendly JSON message")
	@Notes("This is an example")
	@ResponseMessages({ @ResponseMessage(code = 200, message = "In case of success") })
	//@ReturnType(String.class)
	public Response helloResponse() {
	  return Response.ok("\"hello world\"").build();
	}

}
