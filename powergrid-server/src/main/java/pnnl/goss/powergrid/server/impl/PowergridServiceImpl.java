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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.jms.JMSException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.sql.DataSource;
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
import pnnl.goss.powergrid.PowergridCreationReport;
import pnnl.goss.powergrid.collections.PowergridList;
import pnnl.goss.powergrid.dao.PowergridDao;
import pnnl.goss.powergrid.dao.PowergridDaoMySql;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.entities.FromToEntity;
import pnnl.goss.powergrid.entities.Junk;
import pnnl.goss.powergrid.entities.BranchEntity;
import pnnl.goss.powergrid.entities.PowergridModelEntity;
import pnnl.goss.powergrid.parsers.ResultLog;
import pnnl.goss.powergrid.parsers.PsseParser;
import pnnl.goss.powergrid.requests.RequestPowergrid;
import pnnl.goss.powergrid.requests.RequestPowergridList;
import pnnl.goss.powergrid.models.PowergridModel;
import pnnl.goss.powergrid.models.Stuff;
import pnnl.goss.powergrid.server.PowergridServerActivator;
import pnnl.goss.powergrid.server.PowergridService;
import pnnl.goss.powergrid.server.PowergridServiceREST;
import pnnl.goss.powergrid.server.WebDataException;

// This class will be turning into a dao class rather than a rest class.
public class PowergridServiceImpl implements PowergridServiceREST, PowergridService {

    private static Logger log = LoggerFactory.getLogger(PowergridServiceImpl.class);

    private ClientFactory gossClientFactory;

    /**
     * Entity factory for getting information out of the database via the
     * java persistence framework.
     */
    private EntityManagerFactory factory;

    public PowergridServiceImpl(){
        log.debug("Constructing");
    }

    /**
     * Injection of the entity management factory so that we can create enitity
     * managers from it to access database resources.
     *
     * @param An EntityManagerFactory factory
     */
    public void setEntityManagerFactory(EntityManagerFactory factory) {
        log.debug("Setting factory!");
        this.factory = factory;
    }

    /**
     * Injection of the client factory to allow us to use goss's request handler
     * framework on the server side.
     *
     * @param A ClientFactory factory
     */
    public void setClientFactory(ClientFactory factory){
        log.debug("Setting client factory");
        gossClientFactory = factory;
    }

    public Stuff getStuff(){
        return new Stuff();
    }

//    @XmlElementWrapper(name="Powergrids")
//    @XmlElement(name="Powergrid", type=Powergrid.class)
    public List<Powergrid> getPowergrids() {

        EntityManager em = factory.createEntityManager();
        PowergridPersist persist = new PowergridPersist(em);
        List<Powergrid> powergrids = persist.getAvailablePowergrids();
        em.close();
        return powergrids;


//        PowergridDao dao = new PowergridDaoMySql();
//        dao.getAvailablePowergrids()
//
//        Client client = gossClientFactory.create(PROTOCOL.OPENWIRE);
//        Response response = null;
//
//        try {
//            response = (Response) client.getResponse(new RequestPowergridList());
//        } catch (JMSException e) {
//            log.error(e.getMessage(), e);
//            throw new WebDataException(e.getMessage());
//        }
//        finally{
//            client.close();
//        }
//        throwDataError(response);
//
//        DataResponse dataResponse= (DataResponse)response;
//        PowergridList pgList = (PowergridList) dataResponse.getData();
//        return pgList.toList();
    }





    public PowergridModel getPowergridModel(String powergridMrid) {

        EntityManager em = factory.createEntityManager();
        PowergridPersist persist = new PowergridPersist(em);
        PowergridModelEntity model = persist.retrieve(powergridMrid);
        PowergridModel pgModel = EntityToModelConverter.toPowergridModel(model);
        em.close();

        return pgModel;


//        Client client = gossClientFactory.create(PROTOCOL.OPENWIRE);
//        Response response = null;
//
//        try {
//            response = (Response) client.getResponse(new RequestPowergrid(powergridName));
//        } catch (JMSException e) {
//            log.error(e.getMessage(), e);
//            throw new WebDataException(e.getMessage());
//        }
//        finally{
//            client.close();
//        }
//        throwDataError(response);
//
//        DataResponse dataResponse= (DataResponse)response;
//        return (PowergridModel)dataResponse.getData();
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



    private void saveToFile(InputStream uploadedInputStream,
            String uploadedFileLocation) {

        try {
            OutputStream out = null;
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    //@Override
    public String handleUpload(FileInputStream uploadedInputStream) {
        saveToFile(uploadedInputStream, "c:/temp/data.txt");
        return "Success";
    }

    @Override
    public PowergridCreationReport createModelFromFile(String powergridName, File file) {
        log.debug("Got file: " + file.getAbsolutePath());

        //PowergridCreationReport powergridReport = new PowergridCreationReport();
        List<String> results = new ArrayList<String>();
        boolean successful = false;
        PowergridCreationReport powergridReport = null;
        String modelMrid = null;
        try {
            File tempDir = createTempDir("pgc");
            PsseParser parser = new PsseParser();
            File config = new File("src/main/java/pnnl/goss/powergrid/parsers/Psse23Definitions.groovy");
            ResultLog resultLog = parser.parse(config, tempDir, file);

            if (resultLog.getSuccessful()){
//                // Set success = false so that we know whether the next step
//                // is successful.
                resultLog.setSuccessful(false);

                PowergridBuilder builder = new PowergridBuilder();
                HashMap<String, String> props = new HashMap<>();
                props.put("powergridName", powergridName);
                PowergridModelEntity model = builder.createFromParser(parser,
                        resultLog, props);

                EntityManager manager = factory.createEntityManager();

                PowergridPersist persist = new PowergridPersist(manager);
                persist.persist(model, resultLog);
                manager.close();
                if (!resultLog.getSuccessful()){
                    throw new Exception("Failed with mysqlPU");
                }
                modelMrid = model.getMrid();
////
////                persist = new PowergridPersist("cassandra_pu");
////                persist.persist(model, resultLog);
////                powergridReport.setSuccessful(resultLog.getSuccessful());
//
//            }

//            if(builder != null){
//                log.debug("Build != null!");
            }


            powergridReport = new PowergridCreationReport(resultLog.getLog(), resultLog.getSuccessful());
            powergridReport.setPowergridMrid(modelMrid);

            //persist.persist(powergrid);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Exception thrown: ", e);

            throw new WebDataException(e.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Exception thrown: ", e);
            e.printStackTrace();
            throw new WebDataException(e.getMessage());
        }

        return powergridReport;
    }

    /**
     * Creates a new and empty directory in the default temp directory using the
     * given prefix. This methods uses {@link File#createTempFile} to create a
     * new tmp file, deletes it and creates a directory for it instead.
     *
     * @param prefix
     *            The prefix string to be used in generating the diretory's
     *            name; must be at least three characters long.
     * @return A newly-created empty directory.
     * @throws IOException
     *             If no directory could be created.
     */
    private static File createTempDir(String prefix) throws IOException {
        String tmpDirStr = System.getProperty("java.io.tmpdir");
        if (tmpDirStr == null) {
            throw new IOException(
                    "System property 'java.io.tmpdir' does not specify a tmp dir");
        }

        File tmpDir = new File(tmpDirStr);
        if (!tmpDir.exists()) {
            boolean created = tmpDir.mkdirs();
            if (!created) {
                throw new IOException("Unable to create tmp dir " + tmpDir);
            }
        }

        File resultDir = null;
        int suffix = (int) System.currentTimeMillis();
        int failureCount = 0;
        do {
            resultDir = new File(tmpDir, prefix + suffix % 10000);
            suffix++;
            failureCount++;
        } while (resultDir.exists() && failureCount < 50);

        if (resultDir.exists()) {
            throw new IOException(
                    failureCount
                            + " attempts to generate a non-existent directory name failed, giving up");
        }
        boolean created = resultDir.mkdir();
        if (!created) {
            throw new IOException("Failed to create tmp directory");
        }

        return resultDir;
    }

}
