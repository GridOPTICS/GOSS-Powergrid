package pnnl.goss.powergrid.itests.tests;

import static org.amdatu.testing.configurator.TestConfigurator.cleanUp;
import static org.amdatu.testing.configurator.TestConfigurator.configuration;
import static org.amdatu.testing.configurator.TestConfigurator.configure;
import static org.amdatu.testing.configurator.TestConfigurator.serviceDependency;

import java.util.concurrent.TimeUnit;

import org.amdatu.testing.configurator.TestConfiguration;
import org.apache.shiro.mgt.SecurityManager;

import pnnl.goss.core.ClientFactory;
import pnnl.goss.core.server.DataSourceObject;
import pnnl.goss.core.server.DataSourceRegistry;
import pnnl.goss.core.server.ServerControl;
import pnnl.goss.powergrid.itests.testsupport.BasicFakeRealm;

public class GossIntegrationTestSupport {
	
	private final BasicFakeRealm gossRealm = new BasicFakeRealm();
	private TestConfiguration testConfig;
	private volatile ClientFactory clientFactory;
	private volatile ServerControl serverControl;
	private volatile DataSourceRegistry dsRegistry;
	
	
	public static final String OPENWIRE_CLIENT_CONNECTION = "tcp://localhost:6000";
	public static final String STOMP_CLIENT_CONNECTION = "tcp://localhost:6000";
		
	public BasicFakeRealm getFakeRealm() {
		return gossRealm;
	}
	
	public DataSourceRegistry getDSRegistry(){
		return dsRegistry;
	}
	
	public TestConfiguration getTestConfig() {
		return testConfig;
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	public ServerControl getServerControl() {
		return serverControl;
	}

	public TestConfiguration startConfiguration() throws InterruptedException{
		return startConfiguration(true);
	}
	
	public TestConfiguration startConfiguration(boolean applyConfiguration) throws InterruptedException{
		testConfig = configure(this)
					.add(configuration("pnnl.goss.core.server")
							.set("goss.openwire.uri", "tcp://localhost:6000")
							.set("goss.stomp.uri",  "tcp://localhost:6001") //vm:(broker:(tcp://localhost:6001)?persistent=false)?marshal=false")
							.set("goss.start.broker", "true")
							.set("goss.broker.uri", "tcp://localhost:6000"))
					.add(serviceDependency(ServerControl.class))
					.add(configuration(ClientFactory.CONFIG_PID)
							.set("goss.openwire.uri", "tcp://localhost:6000")
							.set("goss.stomp.uri",  "tcp://localhost:6001"))
					.add(serviceDependency(ClientFactory.class).setRequired(true)).setTimeout(10, TimeUnit.SECONDS)
					.add(serviceDependency(SecurityManager.class))
					.add(configuration("pnnl.goss.fusion")
						.set("db.uri", "jdbc:h2:mem:")
						.set("db.username", "sa")
						.set("db.password", "sa"))
					.add(serviceDependency(DataSourceObject.class))
					.add(serviceDependency(DataSourceRegistry.class));
				
		if (applyConfiguration){
			testConfig.apply();
			
			// Configuration update is asyncronous, so give a bit of time to catch up
			TimeUnit.MILLISECONDS.sleep(500);
		}
		
		return testConfig;
	}
	
	public void cleanupServer(){
		try {
			//if (serverControl != null) {serverControl.stop();}
			cleanUp(this);
		}
		catch (Exception e) {
			System.err.println("Ignoring exception!");
		}
		finally {
			if (clientFactory != null){
				clientFactory.destroy();
			}
		}
	}

}
