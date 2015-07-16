package pnnl.goss.powergrid.server.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class PrefixMap {
	
	private JsonObject obj = new JsonObject();
	private static PrefixMap inst;
	// prefix.model.property = goss_property
	private Map<String, Map<String, Map<String, String>>> map = new HashMap<>();
	
	private PrefixMap(Connection connection){
		String sql = "select prefix, goss_property, pti23 from model_map";
		
		try(Statement stmt = connection.createStatement()){
			try(ResultSet rs = stmt.executeQuery(sql)){
				while(rs.next()){
					addName(rs.getString("prefix"), 
							rs.getString("goss_property"),
							"goss_property",
							rs.getString("goss_property"));
					if (rs.getString("pti23") != null){
						addName(rs.getString("prefix"), 
								rs.getString("goss_property"),
								"PTI_23",
								rs.getString("pti23"));
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static PrefixMap instance(Connection connection){
		inst = new PrefixMap(connection);
		return inst;
	}
	
	public static PrefixMap instance(){
		return inst;
	}	
	
	public String getGossPropertyName(String prefix, String fromModel, String fromModelProperty){
		String value = null;
		try{
			value = map.get(prefix).get(fromModel).get(fromModelProperty);
		}
		catch(Exception ex){
			System.err.println("Value not found in map! "+prefix+"."+fromModel+"."+fromModelProperty);
		}
		return value;
	}
	
	public void addName(String prefix, String gossProperty, String model, String model_property){
		
		if (!map.containsKey(prefix)){
			map.put(prefix, new HashMap<>());
		}
		
		if (!map.get(prefix).containsKey(model)){
			map.get(prefix).put(model, new HashMap<>());
		}
		
		map.get(prefix).get(model).put(model_property, gossProperty);
//		
//		
//		if (!obj.has(prefix)){
//			obj.add(prefix, new JsonObject());
//		}
//		
//				
//		
//		JsonObject prefixObj = obj.get(prefix).getAsJsonObject();
//		if (!prefixObj.has(property)){
//			prefixObj.add(property, new JsonObject());
//		}
//		
//		JsonObject propertyObj = prefixObj.get(property).getAsJsonObject();
//		propertyObj.add(model, new JsonPrimitive(name));		
		
	}

}
