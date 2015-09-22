package pnnl.goss.powergrid.server;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pnnl.goss.core.server.DataSourceBuilder;
import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.core.server.DataSourceRegistry;

@Component
public class PowergridDataSources implements PowergridDataSourceEntries {

	public static final String DS_NAME = "goss.powergrids";
	private static final Logger log = LoggerFactory.getLogger(PowergridDataSources.class);
//	private DataSource datasource;
//
//	// Eventually to hold more than one connection
//	// private Map<String, ConnectionPoolDataSource> pooledMap = new ConcurrentHashMap<>();
//	private ConnectionPoolDataSource pooledDataSource;

	@ServiceDependency
	private DataSourceBuilder datasourceBuilder;

	@ServiceDependency
	private DataSourceRegistry datasourceRegistry;

	// These are the datasources that this module has registered.
	private List<String> registeredDatasources = new ArrayList<>();

	public List<String> getRegisteredDatasources(){
		return registeredDatasources;
	}


	@Start
	public void start(){
		log.debug("Starting "+this.getClass().getName());
		// Need to read properties from a file and then create the data
		Properties properties = new Properties();

		properties.put(DataSourceBuilder.DATASOURCE_NAME, DS_NAME+".north");
		properties.put(DataSourceBuilder.DATASOURCE_USER, "powergrid");
		properties.put(DataSourceBuilder.DATASOURCE_PASSWORD, "manager");
		properties.put(DataSourceBuilder.DATASOURCE_URL, "jdbc:mysql://localhost:3306/dev_powergrids");
		properties.put("driverClassName", "com.mysql.jdbc.Driver");

		try {
			datasourceBuilder.create(DS_NAME+".north", properties);
			registeredDatasources.add(DS_NAME+".north");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Stop
	public void stop(){
		log.debug("Stopping "+this.getClass().getName());
		for(String s: registeredDatasources){
			datasourceRegistry.remove(s);
		}
		registeredDatasources.clear();
	}

	@Override
	public Collection<String> getDataSourceKeys() {
		return this.registeredDatasources;
	}

	@Override
	public Connection getConnectionByPowergrid(String mrid) {
		String key = this.registeredDatasources.get(0);
		Connection conn = null;
		try {
			conn = ((DataSourcePooledJdbc) datasourceRegistry.get(key)).getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}



	@Override
	public DataSourcePooledJdbc getDataSourceByKey(String datasourcekey) {
		return (DataSourcePooledJdbc) datasourceRegistry.get(datasourcekey);
	}



	@Override
	public DataSourcePooledJdbc getDataSourceByPowergrid(String mrid) {
		String key = this.registeredDatasources.get(0);
		return getDataSourceByKey(key);
	}



//	@Override
//	public String getName() {
//		return DS_NAME;
//	}
//
//	@Override
//	public DataSourceType getDataSourceType() {
//		return DataSourceType.DS_TYPE_JDBC;
//	}



//	public PowergridDataSource(String username, String password, String url){
//		try {
//			createMysql("dont.worry", url, username, password);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	private void createMysql(String dsName, String url, String username, String password) throws Exception{
//		create(dsName, url, username, password, "com.mysql.jdbc.Driver");
//	}
//
//	private void create(String dsName, String url, String username, String password,
//			String driver) throws Exception {
//
//		Properties propertiesForDataSource = new Properties();
//		propertiesForDataSource.setProperty("username", username);
//		propertiesForDataSource.setProperty("password", password);
//		propertiesForDataSource.setProperty("url", url);
//		propertiesForDataSource.setProperty("driverClassName", driver);
//
//		create(dsName, propertiesForDataSource);
//	}
//
//	private void create(String dsName, Properties properties) throws Exception {
//
//		List<String> checkItems = Arrays.asList(new String[]{"username", "password", "url", "driverClassName"});
//
//		for (String item: checkItems){
//			if(properties.containsKey(item)){
//				String value = properties.getProperty(item);
//				if (value == null || value.isEmpty()){
//					throw new IllegalArgumentException(item + " was specified incorrectly!");
//				}
//			}
//			else{
//				throw new IllegalArgumentException(item+" must be specified!");
//			}
//		}
//
//		if (!properties.containsKey("maxOpenPreparedStatements")){
//			properties.setProperty("maxOpenPreparedStatements", "10");
//		}
//
//		log.debug("Creating BasicDataSource\n\tURI:"+properties.getProperty("url")+"\n\tUser:\n\t"+properties.getProperty("username"));
//
//		Class.forName(properties.getProperty("driverClassName"));
//
//		datasource = BasicDataSourceFactory.createDataSource(properties);
//
//
//		//registry.add(dsName, new DataSourceObjectImpl(dsName, DataSourceType.DS_TYPE_JDBC, ds));
//	}


}
