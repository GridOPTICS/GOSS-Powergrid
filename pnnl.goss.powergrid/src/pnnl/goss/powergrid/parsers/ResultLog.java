package pnnl.goss.powergrid.parsers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultLog implements Serializable {

	private static final long serialVersionUID = -921466412391214592L;
	
	private List<String> warnings = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    private List<String> debug = new ArrayList<>();
    private List<String> ordered = new ArrayList<>();
        
    /**
     * Determined by the number of errors that were logged to the
     * ResultLog object.  If 0 then returns true else false.
     * 
     * @return true if no error messages were logged.
     */
    public boolean wasSuccessful(){
    	return (errors.size() == 0);
    }

    public void debug(String message) {
    	debug.add(message);
    	ordered.add("DEBUG: "+message);
    }
    public void warn(String message) {
    	warnings.add(message);
    	ordered.add("WARN: "+message);
    }
    public void warn(String section, int linenum, String error){
    	String formatted = String.format("%s: %5d %s", section, linenum, error);
    	warn(formatted);
    }

    public void error(String message){
    	errors.add(message);
    	ordered.add("ERROR: "+message);
    }

    public void error(String section, int linenum, String error){
    	String formatted = String.format("%s: %5d %s", section, linenum, error);
    	error(formatted);
    }

    public List<String> getLog() {
    	return ordered;
    }
}
