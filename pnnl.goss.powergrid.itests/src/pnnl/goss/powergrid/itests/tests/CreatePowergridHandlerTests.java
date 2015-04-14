package pnnl.goss.powergrid.itests.tests;

import static org.amdatu.testing.configurator.TestConfigurator.cleanUp;
import static org.amdatu.testing.configurator.TestConfigurator.configure;
import static org.amdatu.testing.configurator.TestConfigurator.serviceDependency;
import static org.amdatu.testing.configurator.TestConfigurator.configuration;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.amdatu.testing.configurator.TestConfiguration;
import org.apache.commons.io.Charsets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pnnl.goss.core.Response;
import pnnl.goss.core.server.RequestHandler;
import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.powergrid.parser.api.ParserService;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;



public class CreatePowergridHandlerTests {
	
	private volatile RequestHandler createPowergridHandler;
	
	// all configuration of services that need to be added to the
	// osgi container will be added to this via the static methods
	// from the configurator class.
	private TestConfiguration testConfig;
	
	@Before
	public void before(){
		
		// In order to be able to test the configuration handler the handler must
		// be registered with the osgi container.
		testConfig = configure(this)
				.add(serviceDependency(RequestHandlerRegistry.class))
				.add(serviceDependency(ParserService.class))
				.add(serviceDependency(RequestHandler.class));
		
		
	}
	
	@After
	public void after(){
		// Cleanup is required in order to clean up the osgi container properly.
		cleanUp(this);		
	}
	
	@Test
	public void createHandlerTest(){
		
		// Do any extra configuration for this test here before calling apply.
		
		// Calling apply actually does the injection of services into the osgi container.
		testConfig.apply();
		
		CreatePowergridRequest request = new CreatePowergridRequest();
		Response response = null;

		try {
			String data = readFile("resources/118.raw", Charsets.UTF_8);
			request.setPowergridContent(data);
			response = createPowergridHandler.handle(request);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull(response);
	}
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}

}
