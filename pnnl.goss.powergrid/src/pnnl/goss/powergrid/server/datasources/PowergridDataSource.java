package pnnl.goss.powergrid.server.datasources;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pnnl.goss.core.server.DataSourceBuilder;
import pnnl.goss.core.server.DataSourceObject;
import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.core.server.DataSourceRegistry;
import pnnl.goss.core.server.DataSourceType;

@Component
public class PowergridDataSource implements DataSourcePooledJdbc, DataSourceObject {

	public static final String DS_NAME = "goss.powergrids";
	private static final Logger log = LoggerFactory.getLogger(PowergridDataSource.class);
	private DataSource datasource;
	
	// Eventually to hold more than one connection
	// private Map<String, ConnectionPoolDataSource> pooledMap = new ConcurrentHashMap<>();
	private ConnectionPoolDataSource pooledDataSource;
	
	@ServiceDependency
	private DataSourceBuilder datasourceBuilder;
	
	
	@Start
	public void start(){
		// Need to read properties from a file and then create the data
		Properties properties = new Properties();
		
		properties.put(DataSourceBuilder.DATASOURCE_NAME, DS_NAME);
		properties.put(DataSourceBuilder.DATASOURCE_USER, "manager");
		properties.put(DataSourceBuilder.DATASOURCE_URL, "jdbc:mysql://localhost:3306/dev_powergrids");
			
	}
	
	@Override
	public String getName() {
		return DS_NAME;
	}

	@Override
	public DataSourceType getDataSourceType() {
		return DataSourceType.DS_TYPE_JDBC;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}
}
