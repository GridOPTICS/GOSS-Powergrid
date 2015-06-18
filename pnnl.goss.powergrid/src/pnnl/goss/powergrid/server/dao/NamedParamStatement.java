package pnnl.goss.powergrid.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedParamStatement {

    private PreparedStatement prepStmt;
    
    private List<String> order = new ArrayList<>();
    private Map<String, List<Integer>> orderMap =new HashMap<>();
    
	
    public NamedParamStatement(Connection conn, String sql) throws SQLException {
    	Pattern pattern = Pattern.compile("@[a-zA-Z_]*(\\s|,|\\)|;)");
    	Matcher matcher = pattern.matcher(sql);
    	String prepSql = sql;
    	int matchNumber = 1;
        // check all occurance
        while (matcher.find()) {
        	// Because the ending of the match is either a space or a comma we need
        	// to trim that off through matcher.end()
        	String fieldToReplace = sql.substring(matcher.start(), matcher.end());
        	String field = fieldToReplace.substring(1, fieldToReplace.length() - 1);
        	
        	if (!orderMap.containsKey(field)){
        		orderMap.put(field, new ArrayList<>());
        	}
        	
        	orderMap.get(field).add(matchNumber);
        	order.add(field);
        	
        	// Handle comma correctly for small field names that could be prefixes of other
        	// field names such as @R and @Rate.  Doing the replace all would remove the @R
        	// from @Rate if we didn't do this.
        	if (fieldToReplace.endsWith(",")){
        		prepSql = prepSql.replaceFirst(fieldToReplace, "?,");
        	}
        	else if (fieldToReplace.endsWith(")")){
        		fieldToReplace = fieldToReplace.substring(0, fieldToReplace.length()-1);
        		prepSql = prepSql.replaceFirst(fieldToReplace, "?");
        	}
        	else{
        		prepSql = prepSql.replaceFirst(fieldToReplace, "?");
        	}
        	matchNumber++;
        }
        
//        for(String d: order){
//        	System.out.println("Field "+d);
//        	System.out.println(orderMap.get(d));
//        }
        
        System.out.println(prepSql);
        prepStmt = conn.prepareStatement(prepSql);
    }
    
    public boolean readyToExecute(){
    	boolean ready = true;
    	for(Entry<String, List<Integer>> item: orderMap.entrySet()){
    		if (item.getValue().size() > 0){
    			System.out.println("Missing: "+item.getKey()+" from parameter list");
    			ready=false;
    		}    		
    	}
    	
    	return ready;
    }

    public PreparedStatement getPreparedStatement() {
        return prepStmt;
    }
    public ResultSet executeQuery() throws SQLException {
    	if (!readyToExecute()){
    		throw new SQLException("All parameters have not been added.");
    	}
        return prepStmt.executeQuery();
    }
    
    public boolean execute() throws SQLException{
    	if (!readyToExecute()){
    		throw new SQLException("All parameters have not been added.");
    	}
    	return prepStmt.execute();
    }
    
    public void close() throws SQLException {
        prepStmt.close();
    }
    
    public void setNullInt(String name) throws SQLException{
    	prepStmt.setNull(getIndex(name), Types.INTEGER);
    }
    
    public void setNullString(String name) throws SQLException{
    	prepStmt.setNull(getIndex(name), Types.VARCHAR);
    }
    
    public void setNullDouble(String name) throws SQLException{
    	prepStmt.setNull(getIndex(name), Types.DOUBLE);
    }
    
    public void setDate(String name, java.sql.Date value) throws SQLException {        
        prepStmt.setDate(getIndex(name), value);
    }

    public void setInt(String name, int value) throws SQLException {        
        prepStmt.setInt(getIndex(name), value);
    }
    public void setString(String name, String value) throws SQLException {        
        prepStmt.setString(getIndex(name), value);
    }
    public void setDouble(String name, double value) throws SQLException {        
        prepStmt.setDouble(getIndex(name), value);
    }

    private int getIndex(String name) {
    	List<Integer> items = orderMap.get(name);
    	return items.remove(0);
        //return fields.indexOf(name)+1;
    }
}