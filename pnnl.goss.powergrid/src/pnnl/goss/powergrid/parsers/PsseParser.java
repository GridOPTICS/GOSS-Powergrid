package pnnl.goss.powergrid.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class PsseParser {
	public static String HEADERS="headers";
	public static String BUSES="buses";	
	public static String LOADS = "loads";
	public static String GENERATORS="generators";
	public static String BRANCHES="branches";
	public static String LINES = "lines";
	public static String TRANSFORMERS = "transformers";
	public static String TRANSFORMER_ADJS= "transformer_adjs";
	public static String AREAS = "areas";
	public static String TWO_TERM_DC = "two_term_dc";
	public static String SWITCHED_SHUNTS = "switched_shunts";
	public static String IMPEDENCE_CORRECTIONS = "impedance_corrections";
	public static String MULTI_TERM_DC = "multi_term_dc";
	public static String MULTI_SECTION_LINE = "multi_section_line";
	public static String ZONES = "zones";
	public static String INTER_AREA_TRANSFERS = "iter_area_transfers";
	public static String OWNERS = "owners";
	public static String FACTS = "facts";
	public static String VSC_DC_LINES = "vcs_dc_lines";
	
	public enum PTI_VERSION{
		PTI_23,
		PTI_29
	}

    ResultLog resultLog;
//    private static String[] sections = {"headings", "buses", "loads", "generator", "branch", "transformer", "area", 
//    	"two_term_dc", "voltage_src_converter", "switched_shunt", "impedance_correction", "multi_term_dc", 
//    	"multi_section_line", "zone", "iter_area_transfer", "owner", "fact"};

    
    // Assumes pti-23 sections below
    private static String[] pti23Sections = {HEADERS, BUSES, GENERATORS, BRANCHES, TRANSFORMER_ADJS,
    	AREAS, TWO_TERM_DC, SWITCHED_SHUNTS, IMPEDENCE_CORRECTIONS, MULTI_TERM_DC, MULTI_SECTION_LINE,
    	ZONES, INTER_AREA_TRANSFERS, OWNERS, FACTS};
    
    private static String[] pti29Sections = {HEADERS, BUSES, LOADS, GENERATORS, BRANCHES, TRANSFORMERS,
    	AREAS, TWO_TERM_DC, VSC_DC_LINES, SWITCHED_SHUNTS, IMPEDENCE_CORRECTIONS, MULTI_TERM_DC, MULTI_SECTION_LINE,
    	ZONES, INTER_AREA_TRANSFERS, OWNERS, FACTS};
    
    
    private static String[][] sections = {pti23Sections, pti29Sections};
    
    JsonObject jsonSections;
    private PTI_VERSION ptiVersion = null;
    
    public JsonObject getParsedSections(){
    	return jsonSections;
    }
    
    public String[] getSections(PTI_VERSION version){
    	return sections[version.ordinal()];
    }
        
    public ResultLog parse(PTI_VERSION version, BufferedReader br){
    	ResultLog log = new ResultLog();
    	jsonSections = new JsonObject();
    	jsonSections.add("version", new JsonPrimitive(version.toString()));

    	ptiVersion = version;
    	Map<String, List<String>> map = readSections(br);

    	
    	for(String sec: sections[ptiVersion.ordinal()]){
    		JsonObject newSection = null;
    		String template = null;
    		String names = null;
    		System.out.println("Dealing with section: "+sec);
    		if(sec.equals(HEADERS)){
    			// Header is handled differently than any other section.
    			if (ptiVersion == PTI_VERSION.PTI_23){
    				List<String> lines = map.get(sec);
    				String line = lines.get(0);
    				line = line.replace("  ", " ");
    				
    				String[] items = line.split("\\s");
    				newSection = new JsonObject();
    				newSection.add("IC", new JsonPrimitive(Integer.parseInt(items[0].trim())));
    				newSection.add("SBASE", new JsonPrimitive(Double.parseDouble(items[1].trim())));
    				newSection.add("line2", new JsonPrimitive(lines.get(1).trim()));
    				newSection.add("line3", new JsonPrimitive(lines.get(2).trim()));    				
    			}
    			else{
    				System.out.println("Woops unknown!");
    			}
    		}    		
    		else if(sec.equals(BUSES)){
    			if (ptiVersion == PTI_VERSION.PTI_23){
	    			names = "I,IDE,PL,QL,GL,BL,IA,VM,VA,NAME,BASKV,ZONE";
	    			template = "iiffffiffsfi";
    			}
    			else{
    				System.out.println("Wooops unknown.");
    			}

    		}
    		else if (sec.equals(GENERATORS)){
    			if (ptiVersion == PTI_VERSION.PTI_23){
    				names = "I,ID,PG,QG,QT,QB,VS,IREG,MBASE,ZR,ZX,RT,XT,GTAP,STAT,RMPCT,PT,PB";
	    			template = "isfffffiffffffifff";
    			}
    			else{
    				System.out.println("Wooops unknown.");
    			}
    		}
    		else if (sec.equals(BRANCHES)){
    			if (ptiVersion == PTI_VERSION.PTI_23){
	    			names = "I,J,CKT,R,X,B,RATEA,RATEB,RATEC,RATIO,ANGLE,GI,BI,GJ,BJ,ST";
	    			template = "iisffffffffffffi";
    			}
    			else{
    				System.out.println("Wooops unknown.");
    			}
    		}
    		else if (sec.equals(TRANSFORMER_ADJS)){
    			if (ptiVersion == PTI_VERSION.PTI_23){
	    			names = "I,J,CKT,ICONT,RMA,RMI,VMA,VMI,STEP,TABLE";
	    			template = "iiifffffi";
    			}
    			else{
    				System.out.println("Wooops unknown.");
    			}
    		}
    		else if (sec.equals(AREAS)){
    			if (ptiVersion == PTI_VERSION.PTI_23){
	    			names = "I,ISW,PDES,PTOL,ARNAME";
	    			template = "iiffs";
    			}
    			else{
    				System.out.println("Wooops unknown.");
    			}
    		}
    		else if (sec.equals(TWO_TERM_DC)){
    			// skip this section
    		}
    		else if (sec.equals(SWITCHED_SHUNTS)){
    			//I,MODSW,VSWHI,VSWLO,SWREM,BINIT,N1,B1,N2,B2...N8,B8
    			if (ptiVersion == PTI_VERSION.PTI_23){
	    			template = "iifffff16f";
    			}
    			else{
    				System.out.println("Wooops unknown.");
    			}
    		}
    		
    		
    		if (template != null && names != null){
    			newSection = buildTemplateAndNameOrder(template, names);
    			for(String ln: map.get(sec)){
					parseLine(ln, newSection);
				}
    		}
    		else if (template != null) {
	    		newSection = buildTemplateOrder(template);
				for(String ln: map.get(sec)){
					parseLine(ln, newSection);
				}
    		} 
    		else{
    			// The header will produce it's own newSection so only create one
    			// if it's not been created e.g. if we aren't using it at all.
    			if (newSection == null){
    				newSection = new JsonObject();
    			}
    		}
    		jsonSections.add(sec, newSection);
    	}

    	return log;
    }
    
    private JsonObject buildTemplateAndNameOrder(String template, String names){
    	JsonObject obj = buildTemplateOrder(template);
    	JsonArray arr = new JsonArray();
    	for(String n:names.split(",")){
    		arr.add(new JsonPrimitive(n.trim()));
    	}
    	obj.add("pti_names", arr);
    	return obj;
    }
    
    private JsonObject buildTemplateOrder(String template){
    	JsonObject obj = new JsonObject();
    	JsonArray ele = new JsonArray();
    	
    	for(int i=0; i<template.length(); i++){
    		char ch = template.charAt(i);
    		int times = 1;
    		
    		if (Character.isDigit(ch)){
    			String sttimes = "";
    			while(Character.isDigit(ch)){
    				sttimes += ch;
    				i++;
        			ch = template.charAt(i);
    			}
    			
    			times = Integer.parseInt(sttimes);    			
    		}
    		
    		for(int j=0; j<times; j++){
	    		switch (ch){
	    		case 'i':
	    			ele.add(new JsonPrimitive("int"));
	    			break;
	    		case 'f':
	    			ele.add(new JsonPrimitive("float"));
	    			break;
	    		case 's':
	    			ele.add(new JsonPrimitive("string"));
	    			break;
	    		case 'd':
	    			ele.add(new JsonPrimitive("double"));
	    			break;
	    		}
    		}
    	}    		
    	
    	obj.add("type_order", ele);
    	
    	return obj;
    }
    
    private String dequote(String data){
    	data = data.replace("'", "");
    	data = data.replace("\"", "");
    	return data.trim();
    }
    
    private void parseLine(String line, JsonObject section){
  	
    	JsonArray arr = section.getAsJsonArray("type_order");
    	JsonArray data = section.getAsJsonArray("data");
    	if (data == null){
    		data = new JsonArray();
    		section.add("data", data);
    	}
    	    	
    	Pattern pat = Pattern.compile(",");
    	String[] items = pat.split(line);
    	JsonArray newData = new JsonArray();
    	
    	for(int i=0; i<items.length;i++){
    		switch(arr.get(i).getAsString()){
    		case "int":
    			newData.add(new JsonPrimitive(Integer.parseInt(items[i].trim())));
    			break;
    		case "float": case "double":
    			newData.add(new JsonPrimitive(Double.parseDouble(items[i].trim())));
    			break;
    		case "string":
    			newData.add(new JsonPrimitive(dequote(items[i])));
    			break;
    		}
    		
    	}
    	
    	data.add(newData);
    	    	
    	//return obj;
    }
    
           

    private Map<String, List<String>> readSections(BufferedReader br){
    	Map<String, List<String>> map = new LinkedHashMap<>();
    	String ln = null;
    	// Section end.
    	Pattern eos = Pattern.compile("^(Q|\\s*0)[\\s]*(/.*)?$");
    	    	
    	try {
    		int line = 0;
    		int sectionNum = 0;
    		String[] section = sections[ptiVersion.ordinal()];
			while((ln = br.readLine()) != null){
				// First lines are heading.
				if (line < 3){
					if (line == 0){
						map.put(section[sectionNum], new ArrayList<String>());
					}
					map.get(section[sectionNum]).add(ln);
					line++;
					continue;
				}
								
				Matcher m = eos.matcher(ln);
				// if match is found then start new section and close the old one.
				// TODO modify so that this is dynamic based upon different psse versions.
				if (m.find()){
					sectionNum++;
					map.put(section[sectionNum], new ArrayList<String>());
					System.out.println("Section is now: " + section[sectionNum]); //ln.substring(m.start(), m.end()));
				}
				else{
					if (line == 3){
						sectionNum++;
						map.put(section[sectionNum], new ArrayList<String>());
					}
					map.get(section[sectionNum]).add(ln);
				}
				line++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return map;
    }




//
//    boolean modelValid = false;
//
//    private ColumnMetaGroup[] metaGroups = null;
//    private Map<String, List<PropertyGroup>> resultMap = null;
//    private String columnSeperator = ",";
//
//    private int defaultInt = 0;
//    private double defaultDouble = 0.0;
//
//
//
//    public ResultLog parse(ColumnMetaGroup[] metaGroups, File tempDir, BufferedReader reader) throws IOException{
//
//    	if (!tempDir.isDirectory()){
//    		throw new IOException("Invalid temp directory: " + tempDir.getPath());
//    	}
//
//    	this.metaGroups = metaGroups;
//    	resultLog = new ResultLog();
//
//    	createTempCards(tempDir, reader);
//    	createObjects(tempDir);
//
//		return resultLog;
//    }
//
////    public ResultLog parse(ColumnMetaGroup[] metaGroups, File tempDir, File modelDataFile) throws IOException{
////
////    	if (!tempDir.isDirectory()){
////    		throw new IOException("Invalid temp directory: " + tempDir.getPath());
////    	}
////
////    	if (!modelDataFile.isFile()){
////    		throw new IOException("Invalid model data file: " + modelDataFile.getPath());
////    	}
////
////    	this.metaGroups = metaGroups;
////    	resultLog = new ResultLog();
////
////    	createTempCards(tempDir, modelDataFile);
////    	createObjects(tempDir);
////
////		return resultLog;
////
//////    	this.metaGroups = metaGroups;
//////
//////        resultLog = new ResultLog();
//////        createTempCards(tempDir, modelData);
//////        model = createObjects(tempDir);
//////        validateModel();
//////
//////        // True if no errors
//////        modelValid = resultLog.errors.size() == 0
//////        resultLog.successful = modelValid
//////        resultLog
////    }
//
//    public Map<String, List<PropertyGroup>> getDataModel(){
//    	return resultMap;
//    }
//
//    /**
//     * Validates the model based upon whether a validator has been specified
//     * in the definition file.
//     *
//     * @param cards
//     * @return
//     */
//    private void validateModel(){
//    	for (ColumnMetaGroup card: metaGroups){
//
//    	}
////        cards.each{ card ->
////            if (card?.validator){
////                resultLog.debug("validating ${card.name}")
////                // Each object of type ${card.name}
////                model[card.name].each{ obj ->obj
////                    // All validators get the object to validate and the model
////                    card.validator(obj, model).each { message ->
////                        resultLog.error(card.name, 0, message+obj)
////                    }
////                }
////            }
////        }
//    }
//
//    /**
//     * Create the busbranch model from the raw input file.
//     *
//     * @param inDir - A directory of cards.  Must contain a file named ${card.name}.card
//     * 					for each psse section that is represented in the cards
//     * 					list.
//     * @param cards - A list of objects with a name and column definition attribute
//     * @return
//     */
//    private void createObjects(File inDir){
//    	resultMap = new ConcurrentHashMap<>();
//
//    	for (ColumnMetaGroup card: metaGroups){
//    		if (card.getColumns() != null && card.getColumns().length > 0){
//    			System.out.println(card.getName().toUpperCase());
//    			resultLog.debug("Creating card: "+ card.getName());
//
//    			Path cardPath = Paths.get(inDir.getPath(), card.getName()+".card");
//    			// Loop over each card file and create a PropertyGroup object from
//    			// each of the lines in the card file.
//    			List<PropertyGroup> cardObjects = new ArrayList<>();
//
//    			try (BufferedReader br = new BufferedReader(new FileReader(cardPath.toString())))
//    			{
//    				String line = null;
//    				int lineNum = 0;
//    				int intValue = defaultInt;
//    				double doubleValue = defaultDouble;
//
//
//    				while ((line = br.readLine()) != null) {
//    					//
//    					PropertyGroup obj = new PropertyGroup();
//    					String fields[] = line.split(columnSeperator);
//    					lineNum +=1;
//
//    					if (fields.length != card.getColumns().length){
//    						resultLog.error(card.getName(), lineNum,
//    								String.format("Invalid number of columns js source: %d raw file: %d ",
//    										card.getColumns().length, fields.length));
//    						continue;
//    					}
//
//    					for(int i=0; i<card.getColumns().length; i++){
//    						ColumnMeta column = card.getColumn(i);
//    						String value = fields[i].replace("'", "").trim();
//    						Property prop = new Property();
//    						prop.setName(column.getFieldName())
//    							.setDataType(column.getDataType());
//
//    						if (column.getDataType().toLowerCase().equals("int")){
//    							try {
//    								intValue = Integer.parseInt(value);
//    							}catch(NumberFormatException e){
//    								resultLog.warn(card.getName(), lineNum,
//    										String.format("Unparsable integer detected: <%s> using default.", value));
//    								intValue = defaultInt;
//    							}
//
//    							prop.setValue(intValue);
//    						}
//    						else if (column.getDataType().toLowerCase().equals("double")){
//    							try {
//    								doubleValue = Double.parseDouble(value);
//    							}catch(NumberFormatException e){
//    								resultLog.warn(card.getName(), lineNum,
//    										String.format("Unparsable double detected: <%s> using default.", value));
//    								doubleValue = defaultDouble;
//    							}
//    							prop.setValue(doubleValue);
//    						}
//    						else {
//    							prop.setValue(value);
//    						}
//
//    						obj.addProperty(prop);
//    					}
//
//    					cardObjects.add(obj);
//    					System.out.println(String.format("Line %d: %s", lineNum, line));
//    				}
//
//
//    			} catch (IOException e) {
//    				System.err.println("No data found for card: "+card.getName());
//    				resultLog.warn("No data found for: " + card.getName());
//    				//e.printStackTrace();
//    			}
//
//    			resultMap.put(card.getName(), cardObjects);
//    		}
//    	}
//    }
//
//
//    /**
//     * Creates temporary .card files in an output directory for each of the passed
//     * sectionCard.  This function puts the first 3 lines in a header.card file
//     * in the same directory as the other cards.
//     *
//     * @param tempDir 		- A directory for temp storage of cards to process.
//     * @param reader - A psse file that has been read into a string.
//     */
//    private void createTempCards(File tempDir, BufferedReader reader){
//        // Clone the list so we don't modify the passed list.
//    	List<ColumnMetaGroup> cards = new ArrayList<>(Arrays.asList(this.metaGroups));
//
//    	File output = new File(Paths.get(tempDir.toString(), "header.card").toString());
//    	int lineNum = 0;
//    	ColumnMetaGroup currentCard = null;
//    	boolean zeroLineFound = false;
//    	String line = null;
//
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))){
//			while ((line = reader.readLine()) != null) {
//				writer.write(line+"\n");
//
//				// The first three lines are header information.
//				if (lineNum < 2){
//					lineNum += 1;
//					continue;
//				}
//				// Exit this while loop
//				break;
//			}
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}
//
//		// increment to card 0.
//		currentCard = cards.remove(0);
//
//		while(currentCard != null){
//
//			output = new File(Paths.get(tempDir.toString(), currentCard.getName() + ".card").toString());
//			try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))){
//
//				while ((line = reader.readLine()) != null) {
//					if (line.startsWith("0")){
//						zeroLineFound = true;
//						currentCard = cards.remove(0);
//						break;  // exit out of the inner loop
//					}
//
//					writer.write(line+"\n");
//    			}
//				if (zeroLineFound){
//					zeroLineFound = false;
//					continue;
//				}
//				// Should be last card and therefore return null.
//				if (cards.size() > 0){
//					currentCard = cards.remove(0);
//				}
//				else{
//					currentCard = null;
//				}
//			}
//    		catch(IOException e){
//    			e.printStackTrace();
//    		}
//		}
//    }
}
