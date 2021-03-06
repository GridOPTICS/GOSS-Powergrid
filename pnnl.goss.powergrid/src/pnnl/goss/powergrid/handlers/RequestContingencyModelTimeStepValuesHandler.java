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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;

import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.Response;
import pnnl.goss.core.security.AuthorizationHandler;
import pnnl.goss.core.security.AuthorizeAll;
import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.core.server.RequestHandler;
import pnnl.goss.powergrid.api.ContingencyTimeStepModelValues;
import pnnl.goss.powergrid.datamodel.ContingencyBranchViolation;
import pnnl.goss.powergrid.datamodel.ContingencyBusViolation;
import pnnl.goss.powergrid.requests.RequestContingencyModelTimeStepValues;
import pnnl.goss.powergrid.server.PowergridDataSourceEntries;

@Component
public class RequestContingencyModelTimeStepValuesHandler implements RequestHandler {

	@ServiceDependency
	private volatile PowergridDataSourceEntries dataSourceEntries;
	
    String datasourceKey = null;

    @Override
    public Response handle(Request request) {
        //datasourceKey = (request.getDatasourceKey() == null)?"northandsouth":request.getDatasourceKey();
        return null;
    }

    public DataResponse getResponse(Request request){

        RequestContingencyModelTimeStepValues ctgRequest = null;
        ContingencyTimeStepModelValues model = null;

        if(request instanceof RequestContingencyModelTimeStepValues){

            ctgRequest = (RequestContingencyModelTimeStepValues)request;

            DataSourcePooledJdbc datasource = dataSourceEntries.getDataSourceByPowergrid(ctgRequest.getPowergridMrid());
            try (Connection conn = datasource.getConnection()) {

                model = new ContingencyTimeStepModelValues();
                model.setContingencyId(ctgRequest.getContingencyId());
                model.setPowergridId(ctgRequest.getPowerGridId());

                //Add Branch Violations
                String dbQuery = "select * from contingencybranchviolations where contingencyid ="+ctgRequest.getContingencyId()+" and powergridid ="+ctgRequest.getPowerGridId();
                try (Statement stmt = conn.createStatement()) {
	                try (ResultSet rs = stmt.executeQuery(dbQuery.toLowerCase())) {
		                List<ContingencyBranchViolation> branchViolations = new ArrayList<ContingencyBranchViolation>();
		                while(rs.next()){
		                    ContingencyBranchViolation violations = new ContingencyBranchViolation();
		                    violations.setPowergridId(rs.getInt(2));
		                    violations.setBranchId(rs.getInt(3));
		                    violations.setContingencyId(rs.getInt(1));
		                    violations.setVoltage(rs.getDouble(4));
		                    branchViolations.add(violations);
		                }
		                model.setBranchViolations(branchViolations);
	                }

	                //Add Bus violation
	                dbQuery = "select * from contingencybusviolations where contingencyid ="+ctgRequest.getContingencyId()+" and powergridid ="+ctgRequest.getPowerGridId();
	                try(ResultSet rs = stmt.executeQuery(dbQuery.toLowerCase())) {
		                List<ContingencyBusViolation> busViolations = new ArrayList<ContingencyBusViolation>();
		                while(rs.next()){
		                    ContingencyBusViolation violations = new ContingencyBusViolation();
		                    violations.setPowergridId(rs.getInt(2));
		                    violations.setBusNumber(rs.getInt(3));
		                    violations.setContingencyId(rs.getInt(1));
		                    violations.setVoltage(rs.getDouble(4));
		                    busViolations.add(violations);
		                }
		                model.setBusViolations(busViolations);
	                }
                }
            }
            catch(SQLException e){
                e.printStackTrace();

            }
        }

        DataResponse response = new DataResponse();
        response.setData(model);

        return response;
    }

	@Override
	public Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> getHandles() {
		Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> auths = new HashMap<>();
		auths.put(RequestContingencyModelTimeStepValues.class, AuthorizeAll.class);
		return auths;
	}

}
