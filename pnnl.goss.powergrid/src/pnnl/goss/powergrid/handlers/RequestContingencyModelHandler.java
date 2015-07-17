/*
	Copyright (c) 2014, Battelle Memorial Institute
    All rights reserved.
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:
    1. Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.
    2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.
    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
    ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
    WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE

    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
    ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
    (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
    LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
    ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
    The views and conclusions contained in the software and documentation are those
    of the authors and should not be interpreted as representing official policies,
    either expressed or implied, of the FreeBSD Project.
    This material was prepared as an account of work sponsored by an
    agency of the United States Government. Neither the United States
    Government nor the United States Department of Energy, nor Battelle,
    nor any of their employees, nor any jurisdiction or organization
    that has cooperated in the development of these materials, makes
    any warranty, express or implied, or assumes any legal liability
    or responsibility for the accuracy, completeness, or usefulness or
    any information, apparatus, product, software, or process disclosed,
    or represents that its use would not infringe privately owned rights.
    Reference herein to any specific commercial product, process, or
    service by trade name, trademark, manufacturer, or otherwise does
    not necessarily constitute or imply its endorsement, recommendation,
    or favoring by the United States Government or any agency thereof,
    or Battelle Memorial Institute. The views and opinions of authors
    expressed herein do not necessarily state or reflect those of the
    United States Government or any agency thereof.
    PACIFIC NORTHWEST NATIONAL LABORATORY
    operated by BATTELLE for the UNITED STATES DEPARTMENT OF ENERGY
    under Contract DE-AC05-76RL01830
*/
package pnnl.goss.powergrid.handlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;

import pnnl.goss.core.DataError;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.Response;
import pnnl.goss.core.security.AuthorizationHandler;
import pnnl.goss.core.security.AuthorizeAll;
import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.core.server.RequestHandler;
import pnnl.goss.powergrid.api.ContingencyModel;
import pnnl.goss.powergrid.datamodel.Contingency;
import pnnl.goss.powergrid.datamodel.ContingencyBranchOut;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.requests.RequestContingencyModel;
import pnnl.goss.powergrid.requests.RequestPowergrid;
import pnnl.goss.powergrid.server.PowergridDataSourceEntries;
import pnnl.goss.powergrid.server.dao.PowergridDao;
import pnnl.goss.powergrid.server.dao.PowergridDaoMySql;


@Component
public class RequestContingencyModelHandler implements RequestHandler {

	@ServiceDependency
	private volatile PowergridDataSourceEntries dataSourceEntries;

	@Override
	public Response handle(Request request) {

		ContingencyModel model = new ContingencyModel();

		DataResponse response = null;

		// All of the requests must stem from RequestPowergrid.
		if (!(request instanceof RequestPowergrid)) {
			response = new DataResponse(new DataError("Unkown request: " + request.getClass().getName()));
			return response;
		}

		RequestPowergrid requestPowergrid = (RequestPowergrid) request;

		//TODO change logic to be if not mrid do lookup on powergrid name if not found then register error.

		// Make sure there is a valid name.
		if (requestPowergrid.getPowergridName() == null || requestPowergrid.getPowergridName().isEmpty()) {
			response = new DataResponse(new DataError("Bad powergrid name"));
			return response;
		}

		DataSourcePooledJdbc datasource = dataSourceEntries.getDataSourceByPowergrid(requestPowergrid.getMrid());
		PowergridDao dao = null; // new PowergridDaoMySql(datasource); //PowergridDataSources.instance().getConnectionPool(datasourceKey));
		Powergrid grid = null; // dao.getPowergridByName(requestPowergrid.getPowergridName());

		if (request instanceof RequestContingencyModel) {
			int powergridId = grid.getPowergridId();
			List<Timestamp> timesteps = null;
			List<Contingency> contingencies = new ArrayList<Contingency>();

			try (Statement stmt = datasource.getConnection().createStatement()){
				String dbQuery = "select * from contingencies where powergridid =" + powergridId;
				ResultSet rs = stmt.executeQuery(dbQuery.toLowerCase());
				;

				while (rs.next()) {
					Contingency contingency = new Contingency();

					contingency.setContingencyId(rs.getInt(1));
					contingency.setName(rs.getString(3));
					contingency.setPowergridId(powergridId);
					contingency.setPowerFlowStatus(rs.getInt(4));
					contingencies.add(contingency);

				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Contingency contingency : contingencies) {
				try {
					model.addContingency(contingency, getContingencyBranchesOut(datasource.getConnection(), powergridId,
							contingency.getContingencyId()));
				} catch (SQLException e) {
					e.printStackTrace();
				}

				if (timesteps == null) {
					try {
						timesteps = getContingencyTimesteps(datasource.getConnection(), powergridId, contingency.getContingencyId());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				model.setTimeSteps(timesteps);
			}
		}

		response = new DataResponse(model);

		return response;
	}

	private List<ContingencyBranchOut> getContingencyBranchesOut(Connection connection, int powergridId, int contingencyId) {

		List<ContingencyBranchOut> branchesOuts = new ArrayList<ContingencyBranchOut>();
		try (Statement stmt = connection.createStatement()) {
			String dbQuery = "select * from contingencybranchesout where contingencyid=" + contingencyId + " AND powergridId=" + powergridId;
			try (ResultSet rs = stmt.executeQuery(dbQuery.toLowerCase())){


				while (rs.next()) {
					ContingencyBranchOut branchesOut = new ContingencyBranchOut();
					branchesOut.setPowergridId(rs.getInt(3));
					branchesOut.setBranchId(rs.getInt(1));
					branchesOut.setContingencyId(rs.getInt(2));
					branchesOuts.add(branchesOut);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return branchesOuts;
	}

	private List<Timestamp> getContingencyTimesteps(Connection connection, int powergridId, int contingencyId) {
		List<Timestamp> timesteps = new ArrayList<Timestamp>();
		try (Statement stmt = connection.createStatement()){
			String dbQuery = "select timestep from contingencytimesteps where contingencyid=" + contingencyId + " AND powergridId=" + powergridId;
			try(ResultSet rs = stmt.executeQuery(dbQuery.toLowerCase())) {

				while (rs.next()) {
					timesteps.add(rs.getTimestamp(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return timesteps;
	}

	@Override
	public Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> getHandles() {
		Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> auths = new HashMap<>();
		auths.put(RequestContingencyModel.class, AuthorizeAll.class);
		return auths;
	}
}
