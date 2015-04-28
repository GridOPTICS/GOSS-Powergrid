package pnnl.goss.powergrid;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.Charsets;
import org.junit.Test;

import pnnl.goss.core.Response;
import pnnl.goss.powergrid.handlers.CreatePowergridHandler;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;

public class CreatePowergridHandlerTests {
	
	@Test
	public void createHandlerTest(){
		CreatePowergridHandler handler = new CreatePowergridHandler();
		CreatePowergridRequest request = new CreatePowergridRequest();
		Response response = null;

		try {
			String data = readFile("resources/118.raw", Charsets.UTF_8);
			request.setPowergridContent(data);
			response = handler.handle(request);
			
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
