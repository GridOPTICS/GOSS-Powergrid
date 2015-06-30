package pnnl.goss.powergrid.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.ReturnType;

import com.google.gson.JsonObject;

import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.server.HandlerNotFoundException;
import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.api.SavePowergridResults;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.collections.PowergridList;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;
import pnnl.goss.powergrid.requests.RequestPowergrid;
import pnnl.goss.powergrid.requests.RequestPowergridList;

@Path("/powergrid/api")
@Produces(MediaType.APPLICATION_JSON)
public class PowergridWebService {

	private volatile RequestHandlerRegistry handlers;

	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns a list of Powergrid instances which contains it's mrid.  " +
		"Using the mrid the application can then post a request for a specific " +
		"PowergridModel instance or lists of it's components.")
	public Collection<Powergrid> list(String identifier, @Context HttpServletRequest request){
		System.out.println("Listing powergrids. "+ identifier);
		RequestPowergridList reqList = new RequestPowergridList();
		String subject = identifier;
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

	@POST
	@Path("/{mrid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description("Returns a PowergridModel for the requested mrid.")
	@ReturnType(PowergridModel.class)
	public Response getPowergridByMrid(String identifier, @PathParam("mrid") String mrid, @Context HttpServletRequest req) {
		System.out.println("Retrieving powergrid for mrid: "+ mrid);
		RequestPowergrid pgRequest = new RequestPowergrid(mrid);
		String subject = identifier;
		PowergridModel model = null;
		Response response = null;

		if (handlers.checkAccess((Request)pgRequest, subject)){
			DataResponse res;
			try {
				res = (DataResponse)handlers.handle(pgRequest);
				if (WebUtil.wasError(res.getData())){
					response = Response.status(Response.Status.BAD_REQUEST)
							.entity(res.getData()).build();
				}
				else {
					model = ((PowergridModel)res.getData());
					response = Response.status(Response.Status.OK).entity(model).build();
				}
			} catch (HandlerNotFoundException e) {
				e.printStackTrace();

			}
		}

		return response;
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description("Parse and store a powergrid model in the database.  The" +
			"The passed data must be a base64 encoded file with "+
			"associated properties.")
	@ReturnType(SavePowergridResults.class)
	public Response create(String identifier,  @Context HttpServletRequest req) {
		String subject = identifier;
		Response response = null;
		JsonObject obj = WebUtil.getRequestJsonBody(req);
		CreatePowergridRequest createReq = new CreatePowergridRequest();
		List<String> errors = new ArrayList<>();

		if (!obj.has("powergridName") ||
				obj.get("powergridName").getAsString().isEmpty()){
			errors.add("Invalid powergridName specified");
		}
		if (!obj.has("powergridContent") ||
				obj.get("powergridContent").getAsString().isEmpty()){
			errors.add("Invalid powergridContent specified");
		}

		if (errors.size() > 0){
			response = Response.status(Response.Status.BAD_REQUEST)
					.entity(errors).build();

		}
		else{


//		if (handlers.checkAccess((Request)pgRequest, subject)){
//			DataResponse res;
//			try {
//				res = (DataResponse)handlers.handle(pgRequest);
//				if (WebUtil.wasError(res.getData())){
//					response = Response.status(Response.Status.BAD_REQUEST)
//							.entity(res.getData()).build();
//				}
//				else {
//					model = ((PowergridModel)res.getData());
//					response = Response.status(Response.Status.OK).entity(model).build();
//				}
//			} catch (HandlerNotFoundException e) {
//				e.printStackTrace();
//
//			}
//		}
			response = Response.status(Response.Status.OK).build();
		}
		return response;
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
