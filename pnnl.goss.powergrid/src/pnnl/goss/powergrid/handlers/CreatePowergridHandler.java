package pnnl.goss.powergrid.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;

import pnnl.goss.core.DataError;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.Response;
import pnnl.goss.core.security.AuthorizationHandler;
import pnnl.goss.core.security.AuthorizeAll;
import pnnl.goss.core.server.DataSourceObject;
import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.core.server.DataSourceRegistry;
import pnnl.goss.core.server.RequestHandler;
import pnnl.goss.powergrid.parser.api.InvalidDataException;
import pnnl.goss.powergrid.parser.api.ParserService;
import pnnl.goss.powergrid.parser.api.PropertyGroup;
import pnnl.goss.powergrid.parsers.ParserServiceImpl;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;
import pnnl.goss.powergrid.server.dao.PowergridDaoMySql;
import pnnl.goss.powergrid.server.datasources.PowergridDataSource;

@Component
public class CreatePowergridHandler implements RequestHandler {

	@ServiceDependency
	private volatile DataSourceRegistry datasourceRegistry;

	// @ServiceDependency
	private volatile ParserService parserService = new ParserServiceImpl();

	// private PowergridDataSource datasource = new
	// PowergridDataSource("powergrid", "manager",
	// "jdbc:mysql://localhost:3306/dev_powergrids");

	@Override
	public Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> getHandles() {
		Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> handles = new HashMap<>();
		handles.put(CreatePowergridRequest.class, AuthorizeAll.class);
		return handles;
	}

	@Override
	public Response handle(Request request) {

		CreatePowergridRequest pgRequest = (CreatePowergridRequest) request;

		if (pgRequest.getPowergridContent() == null
				|| pgRequest.getPowergridContent().trim().isEmpty()) {
			return new DataResponse(new DataError(
					"Invalid powergrid content specified!"));
		}

		DataResponse response = new DataResponse();

		try {

			Map<String, List<PropertyGroup>> properties = parserService.parse(
					"Psse23Definitions",
					IOUtils.toInputStream(pgRequest.getPowergridContent()));
			response.setData(saveData(properties));

		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setData(UUID.randomUUID().toString());

		return response;
	}

	private String saveData(Map<String, List<PropertyGroup>> parsedData) {
		DataSourcePooledJdbc obj = (DataSourcePooledJdbc)datasourceRegistry.get("goss.powergrids.north");
		//DataSourceObject obj = datasourceRegistry.get("goss.powergrids");
		PowergridDaoMySql mydata = new PowergridDaoMySql(obj);

		// PowergridDaoMySql mydata = new PowergridDaoMySql((DataSource) obj);
		return mydata.createPowergrid("new powergrid", parsedData);
	}

}
