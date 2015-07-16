package pnnl.goss.powergrid.web;

import java.io.File;
import java.io.IOException;
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
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

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
	@Path("/ext/{mrid}/{type}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns the appropriate extension to the powergrid. "+
		"Available types are: <ul>" +
		"<li>area</li>"+
		"<li>branchinput</li>"+
		"<li>branchstate</li>" +
		"<li>bus</li>"+
		"<li>generator</li>"+
		"<li>interface</li>"+
		"<li>load</li>"+
		"<li>switchedshunt</li>\n"+
		"<li>tieline</li></ul>"
	)
	@ReturnType(PowergridModel.class)
	public Response getPowergridExt(String identifier,
			@PathParam("mrid") String mrid,
			@PathParam("type") String extensionType,
			@Context HttpServletRequest req) {

		System.out.println("Retrieving powergrid for mrid: "+ mrid);

		RequestPowergrid pgRequest = new RequestPowergrid(mrid);
		pgRequest.addExtesion("ext_table", extensionType);
		Response response = null;

		if (handlers.checkAccess((Request)pgRequest, identifier)){
			DataResponse res;
			try {
				res = (DataResponse)handlers.handle(pgRequest);
				if (WebUtil.wasError(res.getData())){
					response = Response.status(Response.Status.BAD_REQUEST)
							.entity(res.getData()).build();
				}
				else {
					String data = ((String)res.getData());
					response = Response.status(Response.Status.OK).entity(data).build();
				}
			} catch (HandlerNotFoundException e) {
				e.printStackTrace();

			}
		}

		return response;
	}

	@POST
	@Path("/format/{mrid}/{format_code}")
	@Produces(MediaType.TEXT_PLAIN)
	@Description(
		"Returns a PowergridModel formatted as directed.  Currently supported "+
		"is: <ul><li>matpower</li></ul>")
	public Response getPowergridFormatted(String identifier,
			@PathParam("mrid") String mrid,
			@PathParam("format_code") String format,
			@Context HttpServletRequest req) {

		System.out.println("Retrieving powergrid for mrid: "+ mrid);

		RequestPowergrid pgRequest = new RequestPowergrid(mrid);

		if (format.equalsIgnoreCase("matpower")){
			pgRequest.addExtesion("format", "MATPOWER");
		}
		else if (format.equalsIgnoreCase("pypower")){
			pgRequest.addExtesion("format", "PYPOWER");
		}
		else{
			JsonObject json = new JsonObject();
			json.addProperty("error", "Invalid format specifier chose (matpower)");

			// NOTE EXITING HERE !
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(json).build();

		}
		Response response = null;

		if (handlers.checkAccess((Request)pgRequest, identifier)){
			DataResponse res;
			try {
				res = (DataResponse)handlers.handle(pgRequest);
				if (WebUtil.wasError(res.getData())){
					response = Response.status(Response.Status.BAD_REQUEST)
							.entity(res.getData()).build();
				}
				else {
					String data = ((String)res.getData());
					response = Response.status(Response.Status.OK).entity(data).build();
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
	public Response create(@Context HttpServletRequest req) {
		Response response = null;
		String identifier = (String) req.getAttribute("identifier");
		JsonObject obj = WebUtil.getRequestJsonBody(req);
		CreatePowergridRequest createReq = new CreatePowergridRequest();
		List<String> errors = new ArrayList<>();

		if (!obj.has("name") ||
				obj.get("name").getAsString().isEmpty()){
			errors.add("Invalid powergrid name specified");
		}
		if (!obj.has("model_file_content") ||
				obj.get("model_file_content").getAsString().isEmpty()){
			errors.add("Invalid powergridContent specified");
		}

		if (errors.size() > 0){
			response = Response.status(Response.Status.BAD_REQUEST)
					.entity(errors).build();

		}
		else{
			// Make sure the user has access to do this request.
			if (!handlers.checkAccess(createReq, identifier)){
				response = Response.status(Response.Status.UNAUTHORIZED).build();
			}
			else{
				DataResponse res;
				try{
					String content = obj.get("model_file_content").getAsString();
					byte[] decoded = Base64.decodeBase64(content.split(";")[1].split(",")[1]);
					File tmpFile = File.createTempFile("upload", "tmp");
					FileUtils.writeByteArrayToFile(tmpFile,  decoded);
					createReq.setPowergridFile(tmpFile);
					res = (DataResponse)handlers.handle(createReq);
					if (WebUtil.wasError(res.getData())){
						response = Response.status(Response.Status.BAD_REQUEST)
								.entity(res.getData()).build();
					}
					else {

						//model = ((PowergridModel)res.getData());
						//response = Response.status(Response.Status.OK).entity(model).build();
						response = Response.status(Response.Status.OK).build();
					}

				} catch (HandlerNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

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
	
	
	@POST
	@Path("/details/{mrid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns the details for hte scenario. "
	)
	@ReturnType(PowergridModel.class)
	public Response getPowergridExt(String identifier,
			@PathParam("mrid") String mrid,
			@Context HttpServletRequest req) {

		System.out.println("Retrieving powergrid details for mrid: "+ mrid);
		
		
		
		String respStr = "{\"id\":\""+mrid+"\",\"name\":\"Scenario X\",\"profile\":\"winter\",\"startTime\":\"10:01\",\"events\":[{\"timeOffset\":\"5 min\",\"event\":\"line trip\"},{\"timeOffset\":\"10 min\",\"event\":\"line trip\"},{\"timeOffset\":\"12 min\",\"event\":\"generator outage\"}]}";
		
		Response response =  Response.status(Response.Status.OK)
				.entity(respStr).build();
		
//		RequestPowergrid pgRequest = new RequestPowergrid(mrid);
//		pgRequest.addExtesion("ext_table", extensionType);
//		Response response = null;
//
//		if (handlers.checkAccess((Request)pgRequest, identifier)){
//			DataResponse res;
//			try {
//				res = (DataResponse)handlers.handle(pgRequest);
//				if (WebUtil.wasError(res.getData())){
//					response = Response.status(Response.Status.BAD_REQUEST)
//							.entity(res.getData()).build();
//				}
//				else {
//					String data = ((String)res.getData());
//					response = Response.status(Response.Status.OK).entity(data).build();
//				}
//			} catch (HandlerNotFoundException e) {
//				e.printStackTrace();
//
//			}
//		}

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
