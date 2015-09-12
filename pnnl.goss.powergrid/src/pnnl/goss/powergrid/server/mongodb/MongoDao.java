package pnnl.goss.powergrid.server.mongodb;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.List;

import com.google.gson.JsonObject;
import com.mongodb.DB;
import com.mongodb.client.MongoDatabase;

import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.datamodel.AlertContext;
import pnnl.goss.powergrid.datamodel.Area;
import pnnl.goss.powergrid.datamodel.Branch;
import pnnl.goss.powergrid.datamodel.Bus;
import pnnl.goss.powergrid.datamodel.Load;
import pnnl.goss.powergrid.datamodel.Machine;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.Substation;
import pnnl.goss.powergrid.datamodel.SwitchedShunt;
import pnnl.goss.powergrid.datamodel.Zone;
import pnnl.goss.powergrid.server.api.PowergridDao;

public class MongoDao implements PowergridDao {
	
	private MongoDatabase database = null;
	
	public MongoDao(MongoDataSource ds) throws UnknownHostException{
		this.database = ds.getDb();
	}

	@Override
	public Powergrid getPowergridByMrid(String mrid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Powergrid getPowergridById(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlertContext getAlertContext(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PowergridModel getPowergridModel(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PowergridModel getPowergridModelAtTime(int powergridId,
			Timestamp timestep) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Powergrid> getAvailablePowergrids() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Timestamp> getTimeSteps(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Area> getAreas(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Branch> getBranches(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Bus> getBuses(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Branch> getLines(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Load> getLoads(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Machine> getMachines(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SwitchedShunt> getSwitchedShunts(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Substation> getSubstations(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Branch> getTransformers(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Zone> getZones(int powergridId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getExtension(int poewrgridId, String ext_table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persist(Powergrid powergrid) {
		// TODO Auto-generated method stub
		
	}

}
