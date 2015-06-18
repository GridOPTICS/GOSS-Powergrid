package pnnl.goss.powergrid.server;

import java.sql.Connection;
import java.util.Collection;

public interface PowergridDataSourceEntries {
	
	/**
	 * Returns the keys that can query against the @{link: DataSourceRegistry}
	 * 
	 * @return
	 */
	public Collection<String> getDataSourceKeys();
	
	/**
	 * Returns an @{link: Connection} object to where the powergrid mrid is located.
	 * 
	 * @param mrid
	 * @return
	 */
	public Connection getConnectionByPowergrid(String mrid);
	
}
