package pnnl.goss.powergrid.server;

import java.sql.Connection;
import java.util.Collection;

import pnnl.goss.core.server.DataSourcePooledJdbc;

public interface PowergridDataSourceEntries {
	
	/**
	 * Returns the keys that can query against the @{link: DataSourceRegistry}
	 * 
	 * @return
	 */
	public Collection<String> getDataSourceKeys();
	
	/**
	 * Returns an @{link: DataSourcePooledJdbc} object by key.
	 * 
	 * @param mrid
	 * @return
	 */
	public DataSourcePooledJdbc getDataSourceByKey(String datasourcekey);
	
	/**
	 * Returns an @{link: Connection} object to where the powergrid mrid is located.
	 * 
	 * @param mrid
	 * @return
	 */
	public Connection getConnectionByPowergrid(String mrid);
	
	
	/**
	 * Returns an @{link: DataSourcePooledJdbc} object to where the powergrid mrid is located.
	 * 
	 * @param mrid
	 * @return
	 */
	public DataSourcePooledJdbc getDataSourceByPowergrid(String mrid);
	
		
}
