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

import com.google.gson.JsonObject;

import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;
import pnnl.goss.syntheticdata.api.SimulatorService;

@Path("/synthetic")
@Produces(MediaType.APPLICATION_JSON)
public class SyntheticDataWebService {

	private volatile RequestHandlerRegistry handlers;

	private volatile SimulatorService simulators;

	String lastStartedToken = null;

	private volatile RequestSubjectService subjectService;
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Returns a list of Sythetic Generation opertions that have been or are in the " +
		"process of running.")
	public Response list(@Context HttpServletRequest request){
		// "{\"id\":\""+mrid+"\",\"name\":\"Scenario X\",\"profile\":\"winter\",\"startTime\":\"10:01\",\"events\":[{\"timeOffset\":\"5 min\",\"event\":\"line trip\"},{\"timeOffset\":\"10 min\",\"event\":\"line trip\"},{\"timeOffset\":\"12 min\",\"event\":\"generator outage\"}]}";
		return Response.ok(simulators.getStatus(lastStartedToken)).build();
	}

	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Description(
		"Starts a sythetic generation process on the server and then returns the details of the " +
		"running process.")
	public Response start(@Context HttpServletRequest request) {
		JsonObject obj = new JsonObject();
		obj.addProperty("Woot", "Data");
		String token = simulators.runSimulation(obj.toString());
		lastStartedToken = token;
		JsonObject ret = new JsonObject();
		ret.addProperty("simulatorToken", token);
		return Response.ok(ret.toString()).build();
	}

}
