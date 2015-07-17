package pnnl.goss.powergrid.web;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.powergrid.api.PowergridService;
import pnnl.goss.powergrid.parser.api.RequestSubjectService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(PowergridWebService.class)
				.add(createServiceDependency()
						.setService(RequestHandlerRegistry.class))
				.add(createServiceDependency()
						.setService(PowergridService.class))
				.add(createServiceDependency()
						.setService(RequestSubjectService.class)));
		manager.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(SyntheticDataWebService.class)
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
