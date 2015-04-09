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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import pnnl.goss.powergrid.parser.api.PropertyGroup;

public class PsseParser {

    ResultLog resultLog;
    
    boolean modelValid = false;
    
    private ColumnMetaGroup[] metaGroups;
    
    private String columnSeperator = ",";

    public ResultLog parse(ColumnMetaGroup[] metaGroups, File tempDir, File modelDataFile) throws IOException{
    	
    	if (!tempDir.isDirectory()){
    		throw new IOException("Invalid temp directory: " + tempDir.getPath());
    	}
    	
    	if (!modelDataFile.isFile()){
    		throw new IOException("Invalid model data file: " + modelDataFile.getPath());
    	}
    	
    	this.metaGroups = metaGroups;
    	resultLog = new ResultLog();
    	
    	createTempCards(tempDir, modelDataFile);
    	
		return resultLog;
    	
//    	this.metaGroups = metaGroups;
//
//        resultLog = new ResultLog();
//        createTempCards(tempDir, modelData); 
//        model = createObjects(tempDir);
//        validateModel();
//        
//        // True if no errors
//        modelValid = resultLog.errors.size() == 0
//        resultLog.successful = modelValid
//        resultLog
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
    	for (ColumnMetaGroup card: metaGroups){
    		if (card.getColumns() != null && card.getColumns().length > 0){
    			resultLog.debug("Creating card: "+ card.getName());
    			
    			Map<String, List<PropertyGroup>> cardMap = new LinkedHashMap<>();
    			
    			Path cardPath = Paths.get(inDir.getPath(), card.getName()+".card");
    			// Loop over each card file and create a PropertyGroup object from
    			// each of the lines in the card file.
    			
    			try (BufferedReader br = new BufferedReader(new FileReader(cardPath.toString())))
    			{
    	 
    				String line;
    	 
    				while ((line = br.readLine()) != null) {
    					System.out.println(line);
    				}
    	 
    			} catch (IOException e) {
    				e.printStackTrace();
    			} 
    
    			
    		}
    	}

//        cards.each { card ->
//
//            // Only deal with the cards that have the object meta data defined.
//            if (card?.columns){
//
//                resultLog.debug("Creating ${card.name}")
//
//                def objDef = card.columns
//
//                // prepare for the output of the current card's items.
//                objMap[card.name] = []
//
//                def cardFile = new File("${inDir}/${card.name}.card").readLines().each{ line ->
//                    def obj = new Expando()
//                    line.split(",").eachWithIndex { item, i ->
//
//                        // Dynamically  create properties on the object  based upon
//                        // the object's datatype and cast the value correctly.
//                        if (objDef[i].datatype == int){
//                            obj."${objDef[i]['field']}" = item.trim().toInteger()
//                        }
//                        else if(objDef[i].datatype == double){
//                            obj."${objDef[i]['field']}" = item.trim().toDouble()
//                        }
//                        else {
//                            obj."${objDef[i]['field']}" = item.replace("'", "").trim()
//                        }
//                    }
//
//                    objMap[card.name] << obj
//                }
//            }
//        }
//
//        objMap
    }


    /**
     * Creates temporary .card files in an output directory for each of the passed
     * sectionCard.  This function puts the first 3 lines in a header.card file
     * in the same directory as the other cards.
     *
     * @param tempDir 		- A directory for temp storage of cards to process.
     * @param modelDataFile - A psse file that has been read into a string.
     */
    private void createTempCards(File tempDir, File modelDataFile){
        // Clone the list so we don't modify the passed list.
    	List<ColumnMetaGroup> cards = new ArrayList<>(Arrays.asList(this.metaGroups));
    	
    	File output = new File(Paths.get(tempDir.toString(), "header.card").toString());
    	int lineNum = 0;
    	ColumnMetaGroup currentCard = null;
    	String line = null;
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(modelDataFile))){
    		
       	 
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
    				
    			}
        		catch(IOException e){
        			e.printStackTrace();
        		}
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
