package pnnl.goss.powergrid.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;

import com.google.gson.JsonObject;

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
import pnnl.goss.powergrid.api.SavePowergridResults;
import pnnl.goss.powergrid.parser.api.InvalidDataException;
import pnnl.goss.powergrid.parser.api.ParserResults;
import pnnl.goss.powergrid.parser.api.ParserService;
import pnnl.goss.powergrid.parser.api.PropertyGroup;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;
import pnnl.goss.powergrid.parsers.ParserServiceImpl;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;
import pnnl.goss.powergrid.server.PowergridDataSources;
import pnnl.goss.powergrid.server.dao.PowergridDaoMySql;

@Component
public class CreatePowergridHandler implements RequestHandler {

	@ServiceDependency
	private volatile DataSourceRegistry datasourceRegistry;

	@ServiceDependency
	private volatile ParserService parserService;

	@ServiceDependency
	private volatile RequestSubjectService subjectService;

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

			ParserResults results = parserService.parse(
					"PsseDefinitions",
					IOUtils.toInputStream(pgRequest.getPowergridContent()));

			if (results.hasErrors()){
				response.setData(results);
			}
			else{
				// Add access control and variables to the results map
				JsonObject params = new JsonObject();
				params.addProperty("access_level", pgRequest.getAccessLevel());
				params.addProperty("original_filename", pgRequest.getOriginalFilename());
				params.addProperty("md5_content_hash", DigestUtils.md5Hex(pgRequest.getPowergridContent()));
				params.addProperty("description", pgRequest.getDescription());
				results.getSectionMap().add("params", params);
				SavePowergridResults saveResult = saveData(pgRequest, results.getSectionMap());
				response.setData(saveResult);
			}
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			response.setResponseComplete(true);
		}

		return response;
	}

	private SavePowergridResults saveData(CreatePowergridRequest request,
			JsonObject parsedData) {
		DataSourcePooledJdbc obj = (DataSourcePooledJdbc)datasourceRegistry.get("goss.powergrids.north");
		//DataSourceObject obj = datasourceRegistry.get("goss.powergrids");
		PowergridDaoMySql mydata = new PowergridDaoMySql(obj, subjectService.getIdentity(request));

		// PowergridDaoMySql mydata = new PowergridDaoMySql((DataSource) obj);
		return mydata.createPowergrid(request.getPowergridName(),  parsedData);
	}

}
