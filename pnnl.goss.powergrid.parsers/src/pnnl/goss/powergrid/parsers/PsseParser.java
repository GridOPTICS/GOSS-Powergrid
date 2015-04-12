package pnnl.goss.powergrid.parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pnnl.goss.powergrid.parser.api.Property;
import pnnl.goss.powergrid.parser.api.PropertyGroup;

public class PsseParser {

    ResultLog resultLog;
    
    boolean modelValid = false;
    
    private ColumnMetaGroup[] metaGroups = null;
    private Map<String, List<PropertyGroup>> resultMap = null;
    private String columnSeperator = ",";

    private int defaultInt = 0;
    private double defaultDouble = 0.0;

    public ResultLog parse(ColumnMetaGroup[] metaGroups, File tempDir, BufferedReader reader) throws IOException{
    	
    	if (!tempDir.isDirectory()){
    		throw new IOException("Invalid temp directory: " + tempDir.getPath());
    	}
    	    	
    	this.metaGroups = metaGroups;
    	resultLog = new ResultLog();
    	
    	createTempCards(tempDir, reader);
    	createObjects(tempDir);
    	
		return resultLog;
    }
    
//    public ResultLog parse(ColumnMetaGroup[] metaGroups, File tempDir, File modelDataFile) throws IOException{
//    	
//    	if (!tempDir.isDirectory()){
//    		throw new IOException("Invalid temp directory: " + tempDir.getPath());
//    	}
//    	
//    	if (!modelDataFile.isFile()){
//    		throw new IOException("Invalid model data file: " + modelDataFile.getPath());
//    	}
//    	
//    	this.metaGroups = metaGroups;
//    	resultLog = new ResultLog();
//    	
//    	createTempCards(tempDir, modelDataFile);
//    	createObjects(tempDir);
//    	
//		return resultLog;
//    	
////    	this.metaGroups = metaGroups;
////
////        resultLog = new ResultLog();
////        createTempCards(tempDir, modelData); 
////        model = createObjects(tempDir);
////        validateModel();
////        
////        // True if no errors
////        modelValid = resultLog.errors.size() == 0
////        resultLog.successful = modelValid
////        resultLog
//    }
    
    public Map<String, List<PropertyGroup>> getDataModel(){
    	return resultMap;
    }

    /**
     * Validates the model based upon whether a validator has been specified
     * in the definition file.
     *
     * @param cards
     * @return
     */
    private void validateModel(){
    	for (ColumnMetaGroup card: metaGroups){
    		
    	}
//        cards.each{ card ->
//            if (card?.validator){
//                resultLog.debug("validating ${card.name}")
//                // Each object of type ${card.name}
//                model[card.name].each{ obj ->obj
//                    // All validators get the object to validate and the model
//                    card.validator(obj, model).each { message ->
//                        resultLog.error(card.name, 0, message+obj)
//                    }
//                }
//            }
//        }
    }

    /**
     * Create the busbranch model from the raw input file.
     *
     * @param inDir - A directory of cards.  Must contain a file named ${card.name}.card
     * 					for each psse section that is represented in the cards
     * 					list.
     * @param cards - A list of objects with a name and column definition attribute
     * @return
     */
    private void createObjects(File inDir){
    	resultMap = new ConcurrentHashMap<>();
    	
    	for (ColumnMetaGroup card: metaGroups){
    		if (card.getColumns() != null && card.getColumns().length > 0){
    			System.out.println(card.getName().toUpperCase());
    			resultLog.debug("Creating card: "+ card.getName());
    			
    			Path cardPath = Paths.get(inDir.getPath(), card.getName()+".card");
    			// Loop over each card file and create a PropertyGroup object from
    			// each of the lines in the card file.
    			List<PropertyGroup> cardObjects = new ArrayList<>();
    			
    			try (BufferedReader br = new BufferedReader(new FileReader(cardPath.toString())))
    			{    	 
    				String line = null;
    				int lineNum = 0;
    				int intValue = defaultInt;
    				double doubleValue = defaultDouble;
    				
    				
    				while ((line = br.readLine()) != null) {
    					// 
    					PropertyGroup obj = new PropertyGroup();
    					String fields[] = line.split(columnSeperator);
    					lineNum +=1;
    					
    					if (fields.length != card.getColumns().length){
    						resultLog.error(card.getName(), lineNum, 
    								String.format("Invalid number of columns js source: %d raw file: %d ",  
    										card.getColumns().length, fields.length));
    						continue;
    					}
    					
    					for(int i=0; i<card.getColumns().length; i++){
    						ColumnMeta column = card.getColumn(i);
    						String value = fields[i].replace("'", "").trim(); 						
    						Property prop = new Property();
    						prop.setName(column.getFieldName())
    							.setDataType(column.getDataType());
    						
    						if (column.getDataType().toLowerCase().equals("int")){
    							try {
    								intValue = Integer.parseInt(value);    								
    							}catch(NumberFormatException e){
    								resultLog.warn(card.getName(), lineNum, 
    										String.format("Unparsable integer detected: <%s> using default.", value));
    								intValue = defaultInt;
    							}
    							
    							prop.setValue(intValue);
    						}
    						else if (column.getDataType().toLowerCase().equals("double")){
    							try {
    								doubleValue = Double.parseDouble(value);    								
    							}catch(NumberFormatException e){
    								resultLog.warn(card.getName(), lineNum, 
    										String.format("Unparsable double detected: <%s> using default.", value));
    								doubleValue = defaultDouble;
    							}
    							prop.setValue(doubleValue);
    						}
    						else {
    							prop.setValue(value);
    						}
    						
    						obj.addProperty(prop);    						
    					}
    					
    					cardObjects.add(obj);
    					System.out.println(String.format("Line %d: %s", lineNum, line));
    				}
    				
    	 
    			} catch (IOException e) {
    				resultLog.warn("No data found for: " + card.getName());
    				//e.printStackTrace();
    			} 
    			
    			resultMap.put(card.getName(), cardObjects);
    		}
    	}
    }


    /**
     * Creates temporary .card files in an output directory for each of the passed
     * sectionCard.  This function puts the first 3 lines in a header.card file
     * in the same directory as the other cards.
     *
     * @param tempDir 		- A directory for temp storage of cards to process.
     * @param reader - A psse file that has been read into a string.
     */
    private void createTempCards(File tempDir, BufferedReader reader){
        // Clone the list so we don't modify the passed list.
    	List<ColumnMetaGroup> cards = new ArrayList<>(Arrays.asList(this.metaGroups));
    	
    	File output = new File(Paths.get(tempDir.toString(), "header.card").toString());
    	int lineNum = 0;
    	ColumnMetaGroup currentCard = null;
    	String line = null;
    	 
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))){
			while ((line = reader.readLine()) != null) {
				writer.write(line+"\n");
				
				// The first three lines are header information.
				if (lineNum < 2){
					lineNum += 1;
					continue;
				}
				// Exit this while loop 
				break;
			}    			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		// increment to card 0.
		currentCard = cards.remove(0);
		
		while(currentCard != null){
			
			output = new File(Paths.get(tempDir.toString(), currentCard.getName() + ".card").toString());
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))){
				
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("0")){
						currentCard = cards.remove(0);
						break;  // exit out of the inner loop
					}
					
					writer.write(line+"\n");    					
    			}
				// Should be last card and therefore return null.
				if (cards.size() > 0){
					currentCard = cards.remove(0);
				}
				else{
					currentCard = null;
				}    				
			}
    		catch(IOException e){
    			e.printStackTrace();
    		}
		}
    }
}
