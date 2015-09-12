package pnnl.goss.powergrid.web;


import java.util.Hashtable;

import javax.servlet.Filter;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.core.server.TokenIdentifierMap;
import pnnl.goss.powergrid.api.PowergridService;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;
import pnnl.goss.sytheticdata.api.SimulatorService;

public class Activator extends DependencyActivatorBase {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		
		Hashtable loggedProps = new Hashtable();
		loggedProps.put("pattern", "/powergrid/api/.*");
		loggedProps.put("contextId", "GossContext");
			
		// Login filter
		manager.add(createComponent()
				.setInterface(Filter.class.getName(), loggedProps)
				.setImplementation(PowergridLoggedInFilter.class)
				.add(createServiceDependency()
						.setService(TokenIdentifierMap.class)));
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(PowergridWebService.class)
				.add(createServiceDependency()
						.setService(RequestHandlerRegistry.class).setRequired(true))
				.add(createServiceDependency()
						.setService(PowergridService.class).setRequired(true))
				.add(createServiceDependency()
						.setService(RequestSubjectService.class).setRequired(true)));
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(SyntheticDataWebService.class)
				.add(createServiceDependency()
						.setService(SimulatorService.class).setRequired(true))
				.add(createServiceDependency()
						.setService(RequestHandlerRegistry.class))
				.add(createServiceDependency()
						.setService(PowergridService.class))
				.add(createServiceDependency()
						.setService(RequestSubjectService.class)));
//		manager.add(createComponent()
//				.setInterface(Filter.class.getName(), null)
//				.setImplementation(LoggedInFilter.class)
//				.add(createServiceDependency()
//						.setService(HttpService.class)));
//				.add(createServiceDependency()
//						.setService(PowergridService.class)
//						.setRequired(false)));
	}

	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
		// nop
	}
}
