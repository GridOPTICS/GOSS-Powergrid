package pnnl.goss.powergrid.server.datasources;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pnnl.goss.core.server.DataSourceBuilder;
import pnnl.goss.core.server.DataSourceObject;
import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.core.server.DataSourceType;

public class PowergridDataSource implements DataSourcePooledJdbc, DataSourceObject {

	private static final Logger log = LoggerFactory.getLogger(PowergridDataSource.class);
	private DataSource datasource;
	
	public PowergridDataSource(String username, String password, String url){
		try {
			createMysql("dont.worry", url, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataSource getDatasource(){
		return datasource;
	}
			
	private void createMysql(String dsName, String url, String username, String password) throws Exception{
		create(dsName, url, username, password, "com.mysql.jdbc.Driver");
	}

	private void create(String dsName, String url, String username, String password,
			String driver) throws Exception {
		
		Properties propertiesForDataSource = new Properties();
		propertiesForDataSource.setProperty("username", username);
		propertiesForDataSource.setProperty("password", password);
		propertiesForDataSource.setProperty("url", url);
		propertiesForDataSource.setProperty("driverClassName", driver);
		
		create(dsName, propertiesForDataSource);
	}
	
	private void create(String dsName, Properties properties) throws Exception {
		
		List<String> checkItems = Arrays.asList(new String[]{"username", "password", "url", "driverClassName"});
		
		for (String item: checkItems){
			if(properties.containsKey(item)){
				String value = properties.getProperty(item);
				if (value == null || value.isEmpty()){
					throw new IllegalArgumentException(item + " was specified incorrectly!");
				}
			}
			else{
				throw new IllegalArgumentException(item+" must be specified!");
			}
		}
		
		if (!properties.containsKey("maxOpenPreparedStatements")){
			properties.setProperty("maxOpenPreparedStatements", "10");
		}
		
		log.debug("Creating BasicDataSource\n\tURI:"+properties.getProperty("url")+"\n\tUser:\n\t"+properties.getProperty("username"));
		
		Class.forName(properties.getProperty("driverClassName"));
		
		datasource = BasicDataSourceFactory.createDataSource(properties);
		
		
		//registry.add(dsName, new DataSourceObjectImpl(dsName, DataSourceType.DS_TYPE_JDBC, ds));		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataSourceType getDataSourceType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}
}
