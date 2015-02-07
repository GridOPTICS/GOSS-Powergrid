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
package pnnl.goss.powergrid.server;

import java.io.File;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import pnnl.goss.powergrid.PowergridCreationReport;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.entities.Junk;
import pnnl.goss.powergrid.models.PowergridModel;

@Path("/")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface PowergridServiceREST {

    /**
     * Returns a list of Powergrid objects that are available.  The properties
     * specified are used in the other service functions available.
     * @param datasourceKey
     * @return
     */
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Powergrid> getPowergrids();


//    @GET
//    @Path("/")
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Junk getJunk();

    /**
     * Returns the powergrid model for the
     *
     * @param powergridId
     * @return
     */
    @GET
    @Path("/{powergridMrid}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PowergridModel getPowergridModel(
            @PathParam(value = "powergridMrid") String powergridMrid);

    /**
     * Retrieves a powergridmodel with the values updated for a particular timestep.
     * @param powergridName
     * @param timestep
     * @return
     */
    @GET
    @Path("/{powergridName}/{timestep}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PowergridModel getPowergridModelAt(
            @PathParam(value = "powergridName") String powergridName,
            @PathParam(value = "timestep") String timestep);


    @POST
    @Path("/create")  //Your Path or URL to call this service
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Multipart(value = "root", type = "application/octet-stream")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public PowergridCreationReport createModelFromFile(
            @Multipart(value = "powergridName", type="text/plain") String powergridName,
            @Multipart(value = "file", type = "application/octet-stream") File file);

    //public String handleUpload(@FormParam("file") FileInputStream uploadedInputStream);
}
