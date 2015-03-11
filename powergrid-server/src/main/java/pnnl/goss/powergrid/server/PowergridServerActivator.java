/*
    Copyright (c) 2014, Battelle Memorial Institute
    All rights reserved.
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:
    1. Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.
    2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.
    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
    ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
    WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE

    DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
    ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
    (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
    LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
    ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
    The views and conclusions contained in the software and documentation are those
    of the authors and should not be interpreted as representing official policies,
    either expressed or implied, of the FreeBSD Project.
    This material was prepared as an account of work sponsored by an
    agency of the United States Government. Neither the United States
    Government nor the United States Department of Energy, nor Battelle,
    nor any of their employees, nor any jurisdiction or organization
    that has cooperated in the development of these materials, makes
    any warranty, express or implied, or assumes any legal liability
    or responsibility for the accuracy, completeness, or usefulness or
    any information, apparatus, product, software, or process disclosed,
    or represents that its use would not infringe privately owned rights.
    Reference herein to any specific commercial product, process, or
    service by trade name, trademark, manufacturer, or otherwise does
    not necessarily constitute or imply its endorsement, recommendation,
    or favoring by the United States Government or any agency thereof,
    or Battelle Memorial Institute. The views and opinions of authors
    expressed herein do not necessarily state or reflect those of the
    United States Government or any agency thereof.
    PACIFIC NORTHWEST NATIONAL LABORATORY
    operated by BATTELLE for the UNITED STATES DEPARTMENT OF ENERGY
    under Contract DE-AC05-76RL01830
*/
package pnnl.goss.powergrid.server;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pnnl.goss.core.server.GossDataServices;
import pnnl.goss.core.server.GossRequestHandlerRegistrationService;
import pnnl.goss.core.server.internal.GossDataServicesImpl;
import pnnl.goss.powergrid.server.impl.PowergridServiceImpl;

public class PowergridServerActivator implements BundleActivator, ManagedService{

    private static Logger log = LoggerFactory.getLogger(PowergridServerActivator.class);
    public static final String CONFIG_PID = "pnnl.goss.powergrid";
    private static final String PROP_DS_KEY = "datasource-key";

    private Hashtable<String, Object> properties = new Hashtable<>();
    private static String powergridDsKey;

    private BundleContext context;
    private PowergridServiceREST powergridService;
    private ServiceRegistration<PowergridServiceREST> powergridServiceRegistration;

    private ServiceTracker<GossRequestHandlerRegistrationService, Object> registrationServiceTracker;


    public static String getPowergridDsKey(){
        return powergridDsKey;
    }

    @Override
    public void updated(Dictionary<String, ?> properties)
            throws ConfigurationException {
        log.debug("Updating Bundle Configuration");
        this.properties.clear();
        Enumeration<String> keys = properties.keys();
        while (keys.hasMoreElements()){
            String k = keys.nextElement();
            log.debug("CONFIG: "+k);
            if (k.equals(PROP_DS_KEY)){
                PowergridServerActivator.powergridDsKey = (String) properties.get(k);
            }
            this.properties.put(k, properties.get(k));
        }

    }

    @Override
    public void start(BundleContext context) throws Exception {
        log.debug("Starting Bundle");
//        try{
//            this.context = context;
//
//            Hashtable<String, Object> properties = new Hashtable<>();
//            properties.put(Constants.SERVICE_PID, CONFIG_PID);
//            context.registerService(ManagedService.class, this, properties);
//
//            registrationServiceTracker =
//                    new ServiceTracker<GossRequestHandlerRegistrationService, Object>(context,
//                            GossRequestHandlerRegistrationService.class,  null);
//            registrationServiceTracker.open();
//            GossRequestHandlerRegistrationService regService = (GossRequestHandlerRegistrationService)
//                    registrationServiceTracker.getService();
//
//            regService.addHandlerFromBundleContext(context);
//        }
//        catch(Exception e){
//            log.error("Staring bundle error!", e);
//            throw e;
//        }
    }


    @Override
    public void stop(BundleContext context) throws Exception {
        log.debug("Stoping Bundle");
//        GossRequestHandlerRegistrationService regService = (GossRequestHandlerRegistrationService)
//                registrationServiceTracker.getService();
//        registrationServiceTracker.close();
//        registrationServiceTracker = null;
    }
}