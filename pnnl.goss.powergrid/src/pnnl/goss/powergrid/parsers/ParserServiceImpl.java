package pnnl.goss.powergrid.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.io.IOUtils;
import org.apache.felix.dm.annotation.api.Component;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import pnnl.goss.powergrid.parser.api.InvalidDataException;
import pnnl.goss.powergrid.parser.api.ParserResults;
import pnnl.goss.powergrid.parser.api.ParserService;
import pnnl.goss.powergrid.parser.api.PropertyGroup;

import com.google.gson.Gson;

@Component
public class ParserServiceImpl implements ParserService{
	
	private final Map<String, ParserDefinition> availableDefinitions = new HashMap<>();

	/**
	 * Reads in a definition file (json format) and parses it into a
	 * ColumnMetaGroup array
	 * 
	 * @param definition	The definition file
	 * @return				A populated ColumnMetaGroup array.
	 */
	private ColumnMetaGroup[] getMetaGroupsFromDefinition(String definition){
		Bundle b = null; 
		try{
			b = FrameworkUtil.getBundle(this.getClass());
		}catch (NoSuchMethodError e){
			// Not in framework context.
		}
		

		// create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        
        // create JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        
        // evaluate JavaScript code from given file - specified by first argument
        // engine.eval(new java.io.FileReader(filePath));
        InputStream in=null;
        String defPath = "/resources/definitions/"+definition+".js";
		try {
			if (b != null){
				in = b.getEntry(defPath).openStream();
			}
			else {
				String path = getClass().getResource("/").toString();
				String parent = new File(path).getParent(); //.getParentFile();
				System.out.println(parent); //.getAbsolutePath());
				//File defFile = new File(parent.getAbsolutePath()+"/definitions/"+definition+".js");
				Path p = Paths.get(parent.substring(6, parent.length()), "resources", "definitions", definition+".js");
				in = new FileInputStream(p.toString()); //.tofile());
			}
			engine.eval(IOUtils.toString(in));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (javax.script.ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //JSObject cards = engine.get("cards");
		// Build objects from the json string.
        Gson gson =  new Gson();
        if (engine.get("cardModel") != null){
        	return gson.fromJson(engine.get("cardModel").toString(), ColumnMetaGroup[].class);
        }
        
        return null;
        
//		Bundle b = FrameworkUtil.getBundle(this.getClass());
//		String data = null;
//		
//		try {
//			InputStream in = b.getEntry("/resources/definitions/Psse23Definitions.groovy").openStream();
//			data = IOUtils.toString(in);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		return data;
	}
	
	
	
	
	@Override
	public ParserResults parse(String parserDefinition,
			InputStream dataToParse) throws InvalidDataException {
				
		List<String> problems = new ArrayList<String>();
		
		// TODO for now we only support pti23 input files.
		ParserDefinition def = new ParserDefinition();
		def.setLineSeperated(true);
		def.setDescription("Pss/e 23 Definition File");
		def.setName("PsseDefinitions");
		
		// TODO add this back when ready to have multiple definition files.
		/*
		if (!availableDefinitions.containsKey(parserDefinition.toLowerCase())){
			problems.add("Invalid parser definition specified: " + parserDefinition);
		}*/
	
		if (dataToParse == null){
			problems.add("Null or empty data detected.");
		}

		if (problems.size() > 0){
			throw new InvalidDataException(problems);
		}
		
		// The return value from the parsing of data.
		Map<String, List<PropertyGroup>> powergridPropertyMap = null;
		
		// Handle line seperated models such as pti files.
		if (def.isLineSeperated()){
			try(BufferedReader reader = new BufferedReader(new InputStreamReader(dataToParse))){
				powergridPropertyMap = parseLineSeperatedModel(def.getName(), reader, problems);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			// TODO Implement streaming model here aka cim.
			throw new UnsupportedOperationException("Model definition not implemented!");
		}
		
		boolean errorsFound = false;
		for(String s: problems){
			if(s.toUpperCase().startsWith("ERR")) {
				errorsFound = true;
				break;
			}
		}
				
		return new ParserResults(powergridPropertyMap, problems, 
				errorsFound);
	}
	
	
	private Map<String, List<PropertyGroup>> parseLineSeperatedModel(String parserDefinition,
			BufferedReader reader, List<String> problems){
		
		ColumnMetaGroup[] metaGroups = getMetaGroupsFromDefinition(parserDefinition);
		
		Path tempRoot = FileSystems.getDefault().getPath(System.getProperty("user.home"), ".goss/temp");
		
		if (!Files.exists(tempRoot, LinkOption.NOFOLLOW_LINKS)){
			
			try {
				Files.createDirectories(tempRoot);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		Path tempDir = null;
		try {
			tempDir = Files.createTempDirectory(tempRoot, "dat");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		if (tempDir == null){
			problems.add("Couldn't create temp directory!");
			return null;
		}
		
		PsseParser parser = new PsseParser();
		try {
			ResultLog log = parser.parse(metaGroups, tempDir.toFile(), reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return parser.getDataModel();
		
	}

//	@Override
//	public List<String> getAvailableDefinitions() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
