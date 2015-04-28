package pnnl.goss.powergrid.itests.tests;
//package pnnl.goss.fusion.itests;
//
//import static org.amdatu.testing.configurator.TestConfigurator.configuration;
//import static org.amdatu.testing.configurator.TestConfigurator.configure;
//import static org.amdatu.testing.configurator.TestConfigurator.serviceDependency;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//import org.amdatu.testing.configurator.TestConfiguration;
//import org.amdatu.testing.configurator.TestConfigurator;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.SimpleAccount;
//import org.apache.shiro.realm.AuthenticatingRealm;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import pnnl.goss.core.ClientFactory;
//import pnnl.goss.core.security.GossRealm;
//import pnnl.goss.core.server.DataSourceRegistry;
//import pnnl.goss.core.server.ServerControl;
//
//public class TestClientRequests {
//	
//	private static final Logger log = LoggerFactory.getLogger(TestClientRequests.class);
//	
//	private GossIntegrationTestSupport support;
//	private TestConfiguration testConfig;
//	//private volatile DataSourceRegistry dsRegistry;
//		
//	
//	@Before
//	public void before() throws InterruptedException{
//		support = new GossIntegrationTestSupport();
//		testConfig = support.startConfiguration();
//		testConfig.apply();
//		
//		// Configuration update is asyncronous, so give a bit of time to catch up
//		TimeUnit.MILLISECONDS.sleep(500);
//	}
//	
//	@After
//	public void after(){
//		support.cleanupServer();
//	}
//	
//	@Test
//	public void serverCanGetInjectedServicesFromSupport() {
//		assertNotNull(support.getServerControl());
//		assertNotNull(support.getTestConfig());
//		assertNotNull(support.getClientFactory());
//		assertNotNull(support.getDSRegistry());
//		assertTrue(support.getDSRegistry().getAvailable().size() > 0);
//		for(String k: support.getDSRegistry().getAvailable().keySet()){
//			System.out.println(k);
//		}
//		//assertNotNull(clientFactory);
//	}
//
//	
////	@Test
////	public void testCapacityRequirements(){
////		fail();
////	}
//
//}
