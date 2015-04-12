package pnnl.goss.powergrid.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.google.gson.Gson;

public class TestJSHandler {
	private static String filePath="/C:/Users/d3m614/git/GOSS-Powergrid/pnnl.goss.powergrid.parsers/resources/definitions/Psse23Definitions.js";
	private static String rawFile = "/C:/Users/d3m614/git/GOSS-Powergrid/pnnl.goss.powergrid.parsers/resources/118.raw";
	
	public static void main(String[] args) throws Exception {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        // evaluate JavaScript code from given file - specified by first argument
        engine.eval(new java.io.FileReader(filePath));
        //JSObject cards = engine.get("cards");
        Gson gson =  new Gson();
        
        ColumnMetaGroup[] groups = gson.fromJson(engine.get("cardModel").toString(), ColumnMetaGroup[].class);
        
        System.out.println("Back in java!");
        for(ColumnMetaGroup g: groups){
        	System.out.println(g.getName());
        }
        System.out.println(engine.get("cardModel").toString());
        System.out.println("Here I am!");
        PsseParser parser = new PsseParser();
        ResultLog log = parser.parse(groups, new File("C:\\temp\\scratch") , 
        		new BufferedReader(
        				new InputStreamReader(
        						new FileInputStream(
        								new File(rawFile)))));
        
    }
}
