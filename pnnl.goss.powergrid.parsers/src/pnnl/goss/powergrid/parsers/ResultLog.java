package pnnl.goss.powergrid.parsers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultLog implements Serializable {
    private boolean debugEnabled = true;

    private List<String> warnings = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    private List<String> debug = new ArrayList<>();
    private List<String> ordered = new ArrayList<>();
        
    boolean successful = false;

    public void debug(String message) {
    	debug.add(message);
    	ordered.add("DEBUG: "+message);
    }
    public void warn(String message) {
    	warnings.add(message);
    	ordered.add("WARN: "+message);
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
