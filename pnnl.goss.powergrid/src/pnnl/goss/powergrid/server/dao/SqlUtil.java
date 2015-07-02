package pnnl.goss.powergrid.server.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class SqlUtil {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getJSONFromResultSet(ResultSet rs,String keyName) {
	    
		Map json = new HashMap(); 
		List list = new ArrayList();
	    Gson gson = new Gson();
	    
	    if(rs!=null)
	    {
	        try {
	            ResultSetMetaData metaData = rs.getMetaData();
	            while(rs.next())
	            {
	                Map<String,Object> columnMap = new HashMap<String, Object>();
	                for(int columnIndex=1;columnIndex<=metaData.getColumnCount();columnIndex++)
	                {
	                    if(rs.getString(metaData.getColumnName(columnIndex))!=null)
	                        columnMap.put(metaData.getColumnLabel(columnIndex),     rs.getString(metaData.getColumnName(columnIndex)));
	                    else
	                        columnMap.put(metaData.getColumnLabel(columnIndex), "");
	                }
	                list.add(columnMap);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        json.put(keyName, list);
	     }
	    
	     return gson.toJson(json);
	}
}
