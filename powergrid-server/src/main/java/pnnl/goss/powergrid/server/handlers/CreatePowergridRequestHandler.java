package pnnl.goss.powergrid.server.handlers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pnnl.goss.core.DataError;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.Response;
import pnnl.goss.core.server.AbstractRequestHandler;
import pnnl.goss.core.server.annotations.RequestHandler;
import pnnl.goss.core.server.annotations.RequestItem;
import pnnl.goss.powergrid.PowergridCreationReport;
import pnnl.goss.powergrid.entities.PowergridModelEntity;
import pnnl.goss.powergrid.parsers.PsseParser;
import pnnl.goss.powergrid.parsers.ResultLog;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;
//import pnnl.goss.powergrid.server.impl.PowergridBuilder;
import pnnl.goss.powergrid.server.impl.PowergridPersist;

@RequestHandler(value = @RequestItem(value = CreatePowergridRequest.class))
public class CreatePowergridRequestHandler extends AbstractRequestHandler {
    private static Logger log = LoggerFactory
            .getLogger(CreatePowergridRequestHandler.class);

    @Override
    public Response handle(Request request) throws Exception {
        return createPowergrid((CreatePowergridRequest) request);
    }

    private DataResponse createPowergrid(CreatePowergridRequest request) {
        log.debug("Creating powergrid named: " + request.getPowergridName());

        DataResponse response = new DataResponse();

        PowergridCreationReport powergridReport = null;

        File tempDir = null;
        File outFile = null;

        try {
            tempDir = createTempDir("pgc");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.setData(new DataError("Problem creating temp directory!"));
            return response;
        }

        try {
            outFile = File.createTempFile(tempDir.getAbsolutePath(), "raw");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.setData(new DataError("Problem creating temp file!"));
            return response;
        }
        try {
            FileUtils.writeStringToFile(outFile, request.getPowergridContent());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.setData(new DataError("Problem writing temp file!"));
            return response;
        }
        PsseParser parser = new PsseParser();
        File config = new File(
                "src/main/java/pnnl/goss/powergrid/parsers/Psse23Definitions.groovy");
        ResultLog resultLog = parser.parse(config, tempDir, outFile);

        if (resultLog.getSuccessful()) {
            // Set success = false so that we know whether the next step
            // is successful.
            resultLog.setSuccessful(false);

//            PowergridBuilder builder = new PowergridBuilder();
//            HashMap<String, String> props = new HashMap<>();
//            props.put("powergridName", request.getPowergridName());
//            PowergridModelEntity model = builder.createFromParser(parser,
//                    resultLog, props);
//
//            EntityManagerFactory emf = Persistence
//                    .createEntityManagerFactory("mysqlPU");
//
//            EntityManager manager = emf.createEntityManager();
//
//            PowergridPersist persist = new PowergridPersist(manager);
//            persist.persist(model, resultLog);
//            manager.close();
//            emf.close();


        }

        powergridReport = new PowergridCreationReport(resultLog.getLog(),  resultLog.getSuccessful());
        response.setData(powergridReport);
        return response;

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
