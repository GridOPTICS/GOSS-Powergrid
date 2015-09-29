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
import pnnl.goss.core.server.RequestHandler;
import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.api.SavePowergridResults;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.PowergridProvenance;
import pnnl.goss.powergrid.datamodel.PowergridRating;
import pnnl.goss.powergrid.datamodel.collections.PowergridList;
import pnnl.goss.powergrid.datamodel.collections.PowergridObjectAnnotationList;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;
import pnnl.goss.powergrid.requests.RequestEnvelope;
import pnnl.goss.powergrid.requests.RequestPowergrid;
import pnnl.goss.powergrid.requests.RequestPowergridAnnotation;
import pnnl.goss.powergrid.requests.RequestPowergridList;
import pnnl.goss.powergrid.requests.RequestPowergridPart;
import pnnl.goss.powergrid.requests.RequestPowergridPart.PowergridPartType;
import pnnl.goss.powergrid.requests.RequestPowergridProvenance;
import pnnl.goss.powergrid.requests.RequestPowergridRating;

@Path("/powergridprovenance/api")
@Produces(MediaType.APPLICATION_JSON)
public class PowergridProvenanceWebService {

	private volatile RequestHandlerRegistry handlers;

	private volatile RequestSubjectService subjectService;

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description("Parse and store a powergrid rating in the database.  The" +
			"The passed data must be a base64 encoded file with "+
			"associated properties.")
	@Produces(MediaType.APPLICATION_JSON)
	@ReturnType(SavePowergridResults.class)
	public Response create(@Context HttpServletRequest req) {
		Response response = null;
		String identifier = (String) req.getAttribute("identifier");
		JsonObject requestBody = WebUtil.getRequestJsonBody(req);

//		List<String> errors = new ArrayList<>();
//
//		if (!requestBody.has("name") ||
//				requestBody.get("name").getAsString().isEmpty()){
//			errors.add("Invalid powergrid name specified");
//		}
//		if (!requestBody.has("model_file_content") ||
//				requestBody.get("model_file_content").getAsString().isEmpty()){
//			errors.add("Invalid powergridContent specified");
//		}
//
//		if (!requestBody.has("access_level") ||
//				requestBody.get("access_level").getAsString().isEmpty()){
//			errors.add("Invalid access level specified");
//		}
//
//		if (errors.size() > 0){
//			response = Response.status(Response.Status.BAD_REQUEST)
//					.entity(errors).build();
//
//		}
//		else{
//
//			CreatePowergridRequest createReq = new CreatePowergridRequest();
//
//			// Make sure the user has access to do this request.
//			if (!handlers.checkAccess(createReq, identifier)){
//				response = Response.status(Response.Status.UNAUTHORIZED).build();
//			}
//			else{
//				JsonObject params = new JsonObject();
//				subjectService.addRequest(createReq, identifier);
//				createReq.setAccessLevel(requestBody.get("access_level").getAsString());
//				createReq.setOriginalFilename("original_file.raw");
//				createReq.setDescription(requestBody.get("description").getAsString());
//				DataResponse res;
//				try{
//
//					String content = requestBody.get("model_file_content").getAsString();
//					byte[] decoded = Base64.decodeBase64(content.split(";")[1].split(",")[1]);
//					File tmpFile = File.createTempFile("upload", "tmp");
//					FileUtils.writeByteArrayToFile(tmpFile,  decoded);
//					createReq.setPowergridFile(tmpFile);
//					res = (DataResponse)handlers.handle(createReq);
//					if (WebUtil.wasError(res.getData())){
//						response = Response.status(Response.Status.BAD_REQUEST)
//								.entity(res.getData()).build();
//					}
//					else {
//						SavePowergridResults results = ((SavePowergridResults)res.getData());
//						//PowergridModel model = ((PowergridModel)res.getData());
//						response = Response.ok(results).build();
//						//response = Response.status(Response.Status.OK).build();
//					}
//
//				} catch (HandlerNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//
		return response;
	}


	@POST
	@Path("/provenance/{mrid}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns the details for hte scenario. "
	)
	@ReturnType(PowergridProvenance.class)
	public Response getPowerGridProvenanceChain(String identifier,
			@PathParam("mrid") String mrid,
			@Context HttpServletRequest req) throws HandlerNotFoundException {
	
		RequestHandler handler = handlers.getHandler(RequestPowergridProvenance.class);
		RequestPowergridProvenance pgRequest = new RequestPowergridProvenance(mrid);
		Response response = null;
//
		if (handlers.checkAccess((Request)pgRequest, identifier)){
			DataResponse res;
			try {
				res = (DataResponse)handlers.handle(pgRequest);
				if (WebUtil.wasError(res.getData())){
					response = Response.status(Response.Status.BAD_REQUEST)
							.entity(res.getData()).build();
				}
				else {
					Object data = res.getData();
					response = Response.status(Response.Status.OK).entity(data).build();
				}
			} catch (HandlerNotFoundException e) {
				e.printStackTrace();

			}
		}

		return response;
	}
	
	
	@POST
	@Path("/provenance/annotations")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns the details for hte scenario. "
	)
	@ReturnType(PowergridObjectAnnotationList.class)
	public Response getPowerGridProvenanceChain(String identifier,
			@Context HttpServletRequest req) throws HandlerNotFoundException {
	
		RequestHandler handler = handlers.getHandler(RequestPowergridAnnotation.class);
		JsonObject requestBody = WebUtil.getRequestJsonBody(req);

		
		
		RequestPowergridAnnotation pgRequest = new RequestPowergridAnnotation();
		Response response = null;
//
		if (handlers.checkAccess((Request)pgRequest, identifier)){
			DataResponse res;
			
			if (requestBody!=null && requestBody.has("PowergridMrid") ){
				pgRequest.setPowergridMrid(requestBody.get("PowergridMrid").getAsString());
			}
			if (requestBody!=null && requestBody.has("ObjectMrid") ){
				pgRequest.setObjectMrid(requestBody.get("ObjectMrid").getAsString());
			}
			if (requestBody!=null && requestBody.has("ObjectType") ){
				pgRequest.setObjectType(requestBody.get("ObjectType").getAsString());
			}
			try {
				res = (DataResponse)handlers.handle(pgRequest);
				if (WebUtil.wasError(res.getData())){
					response = Response.status(Response.Status.BAD_REQUEST)
							.entity(res.getData()).build();
				}
				else {
					Object data = res.getData();
					response = Response.status(Response.Status.OK).entity(data).build();
				}
			} catch (HandlerNotFoundException e) {
				e.printStackTrace();

			}
		}

		return response;
	}
	
//	@POST
//	@Path("/pg_ratings/{pgid}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Description(
//		"Returns the details for hte scenario. "
//	)
//	@ReturnType(PowergridRating.class)
//	public Response getPowerGridRaging(String identifier,
//			@PathParam("pgid") String pgid,
//			@Context HttpServletRequest req) {
//
//		System.out.println("Retrieving powergrid ratings for mrid: "+ pgid);
//
////		RequestHandler handler = handlers.getHandler(RequestPowergridRating.class);
//		
////		Response response =  Response.status(Response.Status.OK)
////				.entity(modelDetails.get(mrid)).build();
//
//		RequestPowergridRating pgRequest = new RequestPowergridRating(pgid);
////		pgRequest.addExtesion("ext_table", extensionType);
//		Response response = null;
////
//		if (handlers.checkAccess((Request)pgRequest, identifier)){
//			DataResponse res;
//			try {
//				res = (DataResponse)handlers.handle(pgRequest);
//				if (WebUtil.wasError(res.getData())){
//					response = Response.status(Response.Status.BAD_REQUEST)
//							.entity(res.getData()).build();
//				}
//				else {
//					Object data = res.getData();
//					//TODO 
//					response = Response.status(Response.Status.OK).entity(data).build();
//				}
//			} catch (HandlerNotFoundException e) {
//				e.printStackTrace();
//
//			}
//		}
//
//		return response;
//	}


}
