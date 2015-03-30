package pnnl.goss.powergrid.parser.itests;

import static org.amdatu.testing.configurator.TestConfigurator.cleanUp;
import static org.amdatu.testing.configurator.TestConfigurator.configuration;
import static org.amdatu.testing.configurator.TestConfigurator.configure;
import static org.amdatu.testing.configurator.TestConfigurator.serviceDependency;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.amdatu.testing.configurator.TestConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pnnl.goss.powergrid.parser.api.InvalidDataException;
import pnnl.goss.powergrid.parser.api.ParserService;
import pnnl.goss.powergrid.parser.api.PropertyGroup;

public class ParsePsse23Tests {
	///private Logger log = LoggerFactory.getLogger(this.getClass());
	private TestConfiguration testConfig;
	private volatile ParserService parserService;
	
	@Before
	public void before(){
		testConfig = configure(this)
				.add(serviceDependency().setService(ParserService.class));
		testConfig.apply();
	}
	
	@After
	public void after(){
		cleanUp(this);
	}
	
	@Test
	public void wrongDefinitionTest(){
		try {
			parserService.parse("Invalid Definition", "This is bad data!");
			fail("Exception wasn't thrown properly");
		} catch (InvalidDataException e) {
			
		}
	}
	
	@Test
	public void nullOrEmptyDataTest(){
		try {
			parserService.parse(ParserService.DEFINITION_PSSE_23, null);
			fail("Exception wasn't thrown properly");
		} catch (InvalidDataException e) {
			
		}
		
		try {
			parserService.parse(ParserService.DEFINITION_PSSE_23, "");
			fail("Exception wasn't thrown properly");
		} catch (InvalidDataException e) {
			
		}
	}
	
	@Test
	public void canParseIEEE14(){
		File file = new File("resources/IEEE14.raw");
		String data = null;
		Map<String, PropertyGroup> propertyGroups = null;
		
		try {
			data = FileUtils.readFileToString(file);
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		assertTrue(true);
		assertNotNull(parserService);
		try {
			propertyGroups = parserService.parse(ParserService.DEFINITION_PSSE_23, data);
		} catch (InvalidDataException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		assertNotNull(propertyGroups);
	}

}
