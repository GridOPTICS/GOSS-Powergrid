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
package pnnl.goss.powergrid.server.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pnnl.goss.core.Client;
import pnnl.goss.core.Client.PROTOCOL;
import pnnl.goss.core.ClientFactory;
import pnnl.goss.core.DataError;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Response;
import pnnl.goss.core.server.GossDataServices;
import pnnl.goss.core.server.GossRequestHandlerRegistrationService;
import pnnl.goss.powergrid.PowergridModel;
import pnnl.goss.powergrid.collections.PowergridList;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.requests.RequestPowergrid;
import pnnl.goss.powergrid.requests.RequestPowergridList;
import pnnl.goss.powergrid.server.PowergridService;
import pnnl.goss.powergrid.server.WebDataException;
import pnnl.goss.powergrid.server.handlers.RequestPowergridHandler;

public class PowergridServiceImpl implements PowergridService {

    private static Logger log = LoggerFactory.getLogger(PowergridServiceImpl.class);

    private ClientFactory gossClientFactory;
    private GossDataServices dataServices;
    private GossRequestHandlerRegistrationService registrationServices;

    public PowergridServiceImpl(){
        log.debug("Constructing");
    }

    public void setRegistrationHandlerService(GossRequestHandlerRegistrationService service){
        log.debug("Setting registration services");
        registrationServices = service;
    }

    public void setDataServices(GossDataServices dataServices){
        log.debug("Setting dataServices");
        this.dataServices = dataServices;
    }

    public void setClientFactory(ClientFactory client){
        log.debug("Setting client factory");
        gossClientFactory = client;
    }


    @XmlElementWrapper(name="Powergrids")
    @XmlElement(name="Powergrid", type=Powergrid.class)
    public List<Powergrid> getPowergrids() {

        Client client = gossClientFactory.create(PROTOCOL.OPENWIRE);
        Response response = null;

        try {
            response = (Response) client.getResponse(new RequestPowergridList());
        } catch (JMSException e) {
            log.error(e.getMessage(), e);
            throw new WebDataException(e.getMessage());
        }
        finally{
            client.close();
        }
        throwDataError(response);

        DataResponse dataResponse= (DataResponse)response;
        PowergridList pgList = (PowergridList) dataResponse.getData();
        return pgList.toList();
    }

    public PowergridModel getPowergridModel(String powergridName) {
        Client client = gossClientFactory.create(PROTOCOL.OPENWIRE);
        Response response = null;

        try {
            response = (Response) client.getResponse(new RequestPowergrid(powergridName));
        } catch (JMSException e) {
            log.error(e.getMessage(), e);
            throw new WebDataException(e.getMessage());
        }
        finally{
            client.close();
        }
        throwDataError(response);

        DataResponse dataResponse= (DataResponse)response;
        return (PowergridModel)dataResponse.getData();
    }

    public PowergridModel getPowergridModelAt(String powergridName, String timestep) {

        throw new WebDataException("Not Implemented Yet!");

    }

    private void throwDataError(Response response) {
        DataResponse dataRes = null;
        if (response instanceof DataResponse) {
            dataRes = (DataResponse) response;
            if (dataRes.getData() instanceof DataError) {
                DataError de = (DataError) dataRes.getData();
                throw new WebDataException(de.getMessage());
            }
        }
    }
}
