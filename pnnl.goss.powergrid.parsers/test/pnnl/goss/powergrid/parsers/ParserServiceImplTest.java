package pnnl.goss.powergrid.parsers;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import pnnl.goss.powergrid.parser.api.InvalidDataException;
import pnnl.goss.powergrid.parser.api.ParserService;
import pnnl.goss.powergrid.parser.api.PropertyGroup;

public class ParserServiceImplTest {

	@Test
	public void canParseIEEE14CaseProperly(){
		ParserService service = new ParserServiceImpl();
		Map<String, List<PropertyGroup>> resultMap = null;
		
		try(InputStream stream = new FileInputStream(new File("resources/IEEE14.raw"))){
			resultMap = service.parse("Psse23Definitions", stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull("resultMap was null", resultMap);
				
	}
}
