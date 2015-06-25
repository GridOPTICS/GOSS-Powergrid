package pnnl.goss.powergrid.web;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pnnl.goss.powergrid.api.PowergridService;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.server.dao.PowergridDao;

@Path("/powergrid/api")
public class PowergridWebService {

	private volatile PowergridService powergridService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Powergrid> list(){
		return powergridService.getPowergrids();
	}

}
