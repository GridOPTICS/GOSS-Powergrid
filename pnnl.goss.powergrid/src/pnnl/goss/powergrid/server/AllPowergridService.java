package pnnl.goss.powergrid.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger log = LoggerFactory.getLogger(RequestPowergridHandler.class);

    @Start
    public void start(){
    	System.out.println("STARTING "+this.getClass().getName());
    }

	@Override
	public List<Powergrid> getPowergrids() {
		List<Powergrid> availablePowergrids = new ArrayList<>();

		for(String k: dataSourceEntries.getDataSourceKeys()){
    		PowergridDao dao = new PowergridDaoMySql(dataSourceEntries.getDataSourceByKey(k));
    		availablePowergrids.addAll(dao.getAvailablePowergrids());
    	}
		return availablePowergrids;
	}

	@Override
	public PowergridModel getPowergridModel(String mrid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PowergridModel getPowergridModel(String mrid, String timestep) {
		// TODO Auto-generated method stub
		return null;
	}

}
