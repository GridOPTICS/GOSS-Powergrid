package pnnl.goss.powergrid.web;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.ReturnType;

import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;
import pnnl.goss.sytheticdata.GenerationStatus;

@Path("/sythetic")
@Produces(MediaType.APPLICATION_JSON)
public class SyntheticDataWebService {

	private volatile RequestHandlerRegistry handlers;

	private volatile RequestSubjectService subjectService;
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns a list of Sythetic Generation opertions that have been or are in the " +
		"process of running.")
	@ReturnType(GenerationStatus.class)
	public Response list(@Context HttpServletRequest request){
		// "{\"id\":\""+mrid+"\",\"name\":\"Scenario X\",\"profile\":\"winter\",\"startTime\":\"10:01\",\"events\":[{\"timeOffset\":\"5 min\",\"event\":\"line trip\"},{\"timeOffset\":\"10 min\",\"event\":\"line trip\"},{\"timeOffset\":\"12 min\",\"event\":\"generator outage\"}]}";
		return Response.ok().build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Starts a sythetic generation process on the server and then returns the details of the " +
		"running process.")
	@ReturnType(GenerationStatus.class)
	public Response start(@Context HttpServletRequest request) {
		return Response.ok().build();
	}

}
