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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import pnnl.goss.core.DataError;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.Response;
import pnnl.goss.core.security.AuthorizationHandler;
import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.core.server.RequestHandler;
import pnnl.goss.powergrid.api.PowergridService;
import pnnl.goss.powergrid.datamodel.PowergridRating;
import pnnl.goss.powergrid.datamodel.collections.PowergridList;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;
import pnnl.goss.powergrid.requests.RequestEnvelope;
import pnnl.goss.powergrid.requests.RequestPowergrid;
import pnnl.goss.powergrid.requests.RequestPowergridRating;
import pnnl.goss.powergrid.server.PowergridDataSourceEntries;
import pnnl.goss.powergrid.server.api.PowergridProvenanceDao;
import pnnl.goss.powergrid.server.dao.PowergridProvenanceDaoMySql;

@Component
public class RequestPowergridProvenanceHandler implements RequestHandler {

	
	@ServiceDependency
	private volatile PowergridService powergridService;

	@ServiceDependency
	private volatile PowergridDataSourceEntries dataSourceEntries;

	@ServiceDependency
	private volatile RequestSubjectService subjectService;

    private static Logger log = LoggerFactory.getLogger(RequestPowergridProvenanceHandler.class);
    private static PowergridList availablePowergrids = null;

    private DataResponse getPowergridRatingResponse(RequestPowergridRating request) {
    	DataSourcePooledJdbc ds = dataSourceEntries.getDataSourceByPowergrid(request.getMrid());
    	PowergridProvenanceDao dao = new PowergridProvenanceDaoMySql(ds, subjectService.getIdentity(request));

    	List<PowergridRating> ratings = dao.getPowergridRatingsById(request.getMrid());
    	

        DataResponse response = new DataResponse();
        if (ratings!=null && ratings.size()>0) {
        	
        	
//            PowergridModel model = dao.getPowergridModelAtTime(grid.getPowergridId(), request.getTimestep());
//            response.setData(model);
        } else {
            response.setData(new DataError("Powergrid not found!"));
        }

        return response;
    }


    public DataResponse getResponse(Request request) {
        DataResponse response = null;
        Request subRequest = request;
        RequestEnvelope env = null;
        if (request instanceof RequestEnvelope){
        	env = ((RequestEnvelope) request);
        	subRequest = ((RequestEnvelope) request).getWrappedRequest();
        }

        // All of the requests must stem from RequestPowergrid.
        if (!(subRequest instanceof RequestPowergrid)){
            response = new DataResponse(new DataError("Unkown request: " + request.getClass().getName()));
            return response;
        }




        if (request instanceof RequestPowergridRating) {
            response = getPowergridRatingResponse((RequestPowergridRating) request);
        } 
//        else if (request instanceof RequestPowergridList) {
//            response = getAvailablePowergrids();
//        } else if (request instanceof RequestPowergridTimeStepValues) {
//            response = new DataResponse(new DataError("RequestPowergridTimeStepValues not implemented yet!"));
//        } else if (request instanceof RequestPowergridPart) {
//        	response = getPowergridPartType((RequestPowergridPart) request);
//        } else{
//            response = getPowergridModelResponse((RequestPowergrid) request);
//        }

        // A data response if there is an invalid request type.
        if (response == null) {
            response = new DataResponse();
            response.setData(new DataError("Invalid request type specified!"));
        }
        response.setResponseComplete(true);

        return response;
    }


	@Override
    public Response handle(Request request) {
        Response response = null;
        try {
            response = getResponse(request);
        } catch (Exception e) {
            log.error("handler", e);
            DataResponse dr = new DataResponse();
            dr.setData(new DataError("Handling data error."));
            response = dr;

        }

        return response;
    }


    @Override
	public Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> getHandles() {
		Map<Class<? extends Request>, Class<? extends AuthorizationHandler>> auths = new HashMap<>();
//		auths.put(RequestPowergrid.class, AuthorizeAll.class);
//		auths.put(RequestPowergridPart.class, AuthorizeAll.class);
//		auths.put(RequestPowergridTimeStep.class, AuthorizeAll.class);
//		auths.put(RequestPowergridList.class, AuthorizeAll.class);
//		auths.put(RequestPowergridTimeStepValues.class, AuthorizeAll.class);
		return auths;
	}
}
