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

import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.api.PowergridService;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.handlers.RequestPowergridHandler;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;
import pnnl.goss.powergrid.server.api.PowergridDao;
import pnnl.goss.powergrid.server.dao.PowergridDaoMySql;

@Component
public class AllPowergridService implements PowergridService {

	@ServiceDependency
	private volatile PowergridDataSourceEntries dataSourceEntries;

	@ServiceDependency
	private volatile RequestSubjectService subjectService;

	private volatile Map<String, String> mridToDatasourceKeyMap = new HashMap<>();
	private volatile Map<String, Powergrid> mridToPowergridMap = new HashMap<>();

    private static Logger log = LoggerFactory.getLogger(RequestPowergridHandler.class);

    @Start
    public void start(){
    	System.out.println("STARTING "+this.getClass().getName());
    }

	@Override
	public List<Powergrid> getPowergrids(String identifier) {
		List<Powergrid> availablePowergrids = new ArrayList<>();

		for(String k: dataSourceEntries.getDataSourceKeys()){
			DataSourcePooledJdbc ds = dataSourceEntries.getDataSourceByKey(k);
    		PowergridDao dao = new PowergridDaoMySql(ds, identifier);
    		for(Powergrid g:dao.getAvailablePowergrids()){
    			if (identifier == null || "public".equalsIgnoreCase(g.getAccessLevel()) ||
    					(identifier.equals(g.getCreatedBy()) && "private".equalsIgnoreCase(g.getAccessLevel()))) {
	    			mridToDatasourceKeyMap.put(g.getMrid(), k);
	    			mridToPowergridMap.put(g.getMrid(), g);
	    			availablePowergrids.add(g);
    			}
    		}
    	}

		return availablePowergrids;
	}

	@Override
	public PowergridModel getPowergridModel(String mrid, String identifier) {
		PowergridModel model = null;
		// Load the powergrid map so we know which datasource to look
		// up the powergrid model from.
		if (mridToDatasourceKeyMap.isEmpty()){
			getPowergrids(identifier);
		}

		if (mridToDatasourceKeyMap.containsKey(mrid)){
			String dsKey = mridToDatasourceKeyMap.get(mrid);
			DataSourcePooledJdbc ds = dataSourceEntries.getDataSourceByPowergrid(dsKey);
			PowergridDao dao = null; // new PowergridDaoMySql(ds, iden);
			model = dao.getPowergridModel(mridToPowergridMap.get(mrid).getPowergridId());
		}

		return model;
	}

	@Override
	public PowergridModel getPowergridModel(String mrid, String timestep, String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

}
