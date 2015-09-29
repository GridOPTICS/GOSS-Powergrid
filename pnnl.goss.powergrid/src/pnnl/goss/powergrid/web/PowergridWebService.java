package pnnl.goss.powergrid.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import javax.ws.rs.core.Response.Status;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.ReturnType;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
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
import pnnl.goss.powergrid.parser.api.RequestSubjectService;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;
import pnnl.goss.powergrid.requests.RequestEnvelope;
import pnnl.goss.powergrid.requests.RequestPowergrid;
import pnnl.goss.powergrid.requests.RequestPowergridList;
import pnnl.goss.powergrid.requests.RequestPowergridPart;
import pnnl.goss.powergrid.requests.RequestPowergridPart.PowergridPartType;

@Path("/powergrid/api")
@Produces(MediaType.APPLICATION_JSON)
public class PowergridWebService {

	private volatile RequestHandlerRegistry handlers;

	private volatile RequestSubjectService subjectService;

	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns a list of Powergrid instances which contains it's mrid.  " +
		"Using the mrid the application can then post a request for a specific " +
		"PowergridModel instance or lists of it's components.")
	public Response list(String identifier, @Context HttpServletRequest request){
		JsonObject jsonBody = WebUtil.getRequestJsonBody(request);
		RequestPowergridList reqList = new RequestPowergridList();

//		String identifier = (String) request.getAttribute("identifier");
		List<Powergrid> data = null;
		Response response= null;

		if (handlers.checkAccess((Request)reqList, identifier)){
			subjectService.addRequest(reqList, identifier);
			DataResponse res;

			try {

				res = (DataResponse)handlers.handle(reqList);
				if(WebUtil.wasError(res)){
					response = Response.status(Response.Status.BAD_REQUEST)
							.entity(res.getData()).build();
				}else{
					data = ((PowergridList)res.getData()).toList();
					response = Response.ok(data).build();
				}
			} catch (HandlerNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				data = new ArrayList<>();
			}
		}

		return response;
	}

	@POST
	@Path("/{mrid}/part/{part}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description("Returns a list of powergrid elements.  The available list is as follows: "
			+"<ul><li>bus</li>"
			+"<li>branch (lines and transformers)</li>"
			+"<li>line</li>"
			+"<li>transformer</li>"
			+"<li>generator</li>"
			+"<li>load</li>"
			+"<li>shunt</li></ul>")
	public Response getPowergridPart(@PathParam("mrid") String powergridMrid,
			@PathParam("part") String part,
			@Context HttpServletRequest req){

		Response response = null;
		RequestPowergridPart pgRequest = null;
		String identifier = (String)req.getAttribute("identifier");
		//try{
		pgRequest = new RequestPowergridPart(powergridMrid, PowergridPartType.valueOf(part.toUpperCase()));

		if (handlers.checkAccess(pgRequest, identifier)){
			subjectService.addRequest(pgRequest, identifier);
			DataResponse dataResp;
			try {
				dataResp = (DataResponse)handlers.handle(pgRequest);

				if (WebUtil.wasError(dataResp.getData())){
					response = Response.status(Response.Status.BAD_REQUEST)
							.entity(dataResp.getData()).build();
				}
				else {
					//model = ((PowergridModel)dataResp.getData());
					response = Response.status(Response.Status.OK).entity(dataResp.getData()).build();
				}
			} catch (HandlerNotFoundException e) {
				e.printStackTrace();

			}
		}
		else{
			response = Response.status(Status.UNAUTHORIZED).build();
		}


		return response;
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
			subjectService.addRequest(pgRequest, identifier);
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
			subjectService.addRequest(pgRequest, identifier);
			try {
				res = (DataResponse)handlers.handle(pgRequest);
				if (WebUtil.wasError(res.getData())){
					response = Response.status(Response.Status.BAD_REQUEST)
							.entity(res.getData()).build();
				}
				else {
					String data = ((String)res.getData());
					response = Response.ok(data).build(); //.header("Access-Control-Allow-Origin", "*").build();
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
			subjectService.addRequest(pgRequest, identifier);
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
	@Produces(MediaType.APPLICATION_JSON)
	@ReturnType(SavePowergridResults.class)
	public Response create(@Context HttpServletRequest req) {
		Response response = null;
		String identifier = (String) req.getAttribute("identifier");
		JsonObject requestBody = WebUtil.getRequestJsonBody(req);

		List<String> errors = new ArrayList<>();

		if (!requestBody.has("name") ||
				requestBody.get("name").getAsString().isEmpty()){
			errors.add("Invalid powergrid name specified");
		}
		if (!requestBody.has("model_file_content") ||
				requestBody.get("model_file_content").getAsString().isEmpty()){
			errors.add("Invalid powergridContent specified");
		}

		if (!requestBody.has("access_level") ||
				requestBody.get("access_level").getAsString().isEmpty()){
			errors.add("Invalid access level specified");
		}

		if (errors.size() > 0){
			response = Response.status(Response.Status.BAD_REQUEST)
					.entity(errors).build();

		}
		else{

			CreatePowergridRequest createReq = new CreatePowergridRequest();

			// Make sure the user has access to do this request.
			if (!handlers.checkAccess(createReq, identifier)){
				response = Response.status(Response.Status.UNAUTHORIZED).build();
			}
			else{
				JsonObject params = new JsonObject();
				subjectService.addRequest(createReq, identifier);
				createReq.setAccessLevel(requestBody.get("access_level").getAsString());
				createReq.setOriginalFilename("original_file.raw");
				createReq.setDescription(requestBody.get("description").getAsString());
				DataResponse res;
				try{

					String content = requestBody.get("model_file_content").getAsString();
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
						SavePowergridResults results = ((SavePowergridResults)res.getData());
						//PowergridModel model = ((PowergridModel)res.getData());
						response = Response.ok(results).build();
						//response = Response.status(Response.Status.OK).build();
					}

				} catch (HandlerNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return response;
	}


	@POST
	@Path("/scenario_details/{mrid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns the details for hte scenario. "
	)
	@ReturnType(PowergridModel.class)
	public Response getScenarioDetails(String identifier,
			@PathParam("mrid") String mrid,
			@Context HttpServletRequest req) {

		System.out.println("Retrieving powergrid details for mrid: "+ mrid);

		RequestPowergrid pgReq = new RequestPowergrid(mrid);
		
		Response response;
		if (!handlers.checkAccess(pgReq, identifier)){
			response = Response.status(Response.Status.UNAUTHORIZED).build();
		} else {
		String respStr = "{\"id\":\""+mrid+"\",\"name\":\"Scenario X\",\"profile\":\"winter\",\"startTime\":\"10:01\",\"events\":[{\"timeOffset\":\"5 min\",\"event\":\"line trip\"},{\"timeOffset\":\"10 min\",\"event\":\"line trip\"},{\"timeOffset\":\"12 min\",\"event\":\"generator outage\"}]}";

			response =  Response.status(Response.Status.OK)
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

			
		}
		return response;
	}

	@POST
	@Path("/model_details/{mrid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns the details for hte scenario. "
	)
	@ReturnType(PowergridModel.class)
	public Response getModelDetails(String identifier,
			@PathParam("mrid") String mrid,
			@Context HttpServletRequest req) {

		System.out.println("Retrieving powergrid details for mrid: "+ mrid);

		HashMap<String, String> modelDetails = new HashMap<String, String>();

		modelDetails.put("1", "{\"id\":\"1\",\"name\":\"Greek 118\",\"created_by\":\"Poorva Sharma\",\"filename\":\"Greepti118.raw\",\"desc\":\"this is a test\",\"format\":\"PTI(23,26,29,31)\",\"useraccess\":\"Public\"}");
		modelDetails.put("2", "{\"id\":\"2\",\"name\":\"Greek 118\",\"created_by\":\"Poorva Sharma\",\"filename\":\"Greepti118.raw\",\"desc\":\"this is a test\",\"format\":\"PTI(23,26,29,31)\",\"useraccess\":\"Public\"}");
		modelDetails.put("3", "{\"id\":\"3\",\"name\":\"Greek 118\",\"created_by\":\"Poorva Sharma\",\"filename\":\"Greepti118.raw\",\"desc\":\"this is a test\",\"format\":\"PTI(23,26,29,31)\",\"useraccess\":\"Public\"}");
		modelDetails.put("4", "{\"id\":\"4\",\"name\":\"Greek 118\",\"created_by\":\"Poorva Sharma\",\"filename\":\"Greepti118.raw\",\"desc\":\"this is a test\",\"format\":\"PTI(23,26,29,31)\",\"useraccess\":\"Public\"}");
		modelDetails.put("5", "{\"id\":\"5\",\"name\":\"Greek 118\",\"created_by\":\"Poorva Sharma\",\"filename\":\"Greepti118.raw\",\"desc\":\"this is a test\",\"format\":\"PTI(23,26,29,31)\",\"useraccess\":\"Public\"}");
		modelDetails.put("6", "{\"id\":\"6\",\"name\":\"Greek 118\",\"created_by\":\"Poorva Sharma\",\"filename\":\"Greepti118.raw\",\"desc\":\"this is a test\",\"format\":\"PTI(23,26,29,31)\",\"useraccess\":\"Public\"}");
		modelDetails.put("7", "{\"id\":\"7\",\"name\":\"Greek 118\",\"created_by\":\"Poorva Sharma\",\"filename\":\"Greepti118.raw\",\"desc\":\"this is a test\",\"format\":\"PTI(23,26,29,31)\",\"useraccess\":\"Public\"}");
		modelDetails.put("8", "{\"id\":\"8\",\"name\":\"Greek 118\",\"created_by\":\"Poorva Sharma\",\"filename\":\"Greepti118.raw\",\"desc\":\"this is a test\",\"format\":\"PTI(23,26,29,31)\",\"useraccess\":\"Public\"}");

//		"{\"id\":\"1\",\"name\":\"North\",\"created_by\":\"Poorva Sharma\",\"filename\":\"north.raw\",\"desc\":\"this is a test\",\"details":\"This is detail\"}";
//		String respStr = "{\"id\":\""+mrid+"\",\"name\":\"Scenario X\",\"profile\":\"winter\",\"startTime\":\"10:01\",\"events\":[{\"timeOffset\":\"5 min\",\"event\":\"line trip\"},{\"timeOffset\":\"10 min\",\"event\":\"line trip\"},{\"timeOffset\":\"12 min\",\"event\":\"generator outage\"}]}";

		Response response =  Response.status(Response.Status.OK)
				.entity(modelDetails.get(mrid)).build();

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
