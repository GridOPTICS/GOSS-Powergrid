package pnnl.goss.powergrid.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.api.PowergridService;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.handlers.RequestPowergridHandler;
import pnnl.goss.powergrid.server.dao.PowergridDao;
import pnnl.goss.powergrid.server.dao.PowergridDaoMySql;

@Component
public class AllPowergridService implements PowergridService {

	@ServiceDependency
	private volatile PowergridDataSourceEntries dataSourceEntries;

	private volatile Map<String, String> mridToDatasourceKeyMap = new HashMap<>();
	private volatile Map<String, Powergrid> mridToPowergridMap = new HashMap<>();

    private static Logger log = LoggerFactory.getLogger(RequestPowergridHandler.class);

    @Start
    public void start(){
    	System.out.println("STARTING "+this.getClass().getName());
    }

	@Override
	public List<Powergrid> getPowergrids(JsonObject params) {
		List<Powergrid> availablePowergrids = new ArrayList<>();
		
		String identifier = params.get("identifier").getAsString();

		for(String k: dataSourceEntries.getDataSourceKeys()){
    		PowergridDao dao = new PowergridDaoMySql(dataSourceEntries.getDataSourceByKey(k));
    		for(Powergrid g:dao.getAvailablePowergrids(identifier)){
    			mridToDatasourceKeyMap.put(g.getMrid(), k);
    			mridToPowergridMap.put(g.getMrid(), g);
    			availablePowergrids.add(g);
    		}
    	}

		return availablePowergrids;
	}

	@Override
	public PowergridModel getPowergridModel(String mrid, JsonObject params) {
		PowergridModel model = null;
		// Load the powergrid map so we know which datasource to look
		// up the powergrid model from.
		if (mridToDatasourceKeyMap.isEmpty()){
			getPowergrids(params);
		}

		if (mridToDatasourceKeyMap.containsKey(mrid)){
			String dsKey = mridToDatasourceKeyMap.get(mrid);
			PowergridDao dao = new PowergridDaoMySql(dataSourceEntries.getDataSourceByKey(dsKey));
			model = dao.getPowergridModel(mridToPowergridMap.get(mrid).getPowergridId());
		}

		return model;
	}

	@Override
	public PowergridModel getPowergridModel(String mrid, String timestep, JsonObject params) {
		// TODO Auto-generated method stub
		return null;
	}

}
