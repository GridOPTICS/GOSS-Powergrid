GOSS-Powergrid
==============

A powergrid project cabable of dealing with node/breaker and bus/branch constructs.

This project can be downloaded and built two different ways.  You can build it and run it from the command line, or you can build and run it within the Eclipse development platform.

Installation for testing  (command-line)

1.  Download or clone the repository from github
	a.  Install github desktop https://desktop.github.com/ or sourcetree https://www.atlassian.com/software/sourcetree/overview  and Clone the GOSS Powergrid repository (https://github.com/GridOPTICS/GOSS-Powergrid)  
	b.  Or download the source  (https://github.com/GridOPTICS/GOSS-Powergrid/archive/master.zip)
2.  Install java 1.8 SDK and set JAVA_HOME variable
3.  Run From the location of the GOSS Powergrid repository (will be referred as $PG_REPO)  run     gradlew clean runbundles
4.  Download and unpackage Felix  http://felix.apache.org/downloads.cgi  (will be referred to as $FELIX_HOME)
5.  Download file install jar from same location and put file-install bundle in $FELIX_HOME/bundle directory
7.  Add felix.fileinstall.dir=.load to the $FELIX_HOME/conf/config.properties file
8.  Test installation by open opening prompt at $FELIX_HOME and executing java -jar bin/felix.jar
9.  Copy all jars from $PG_REPO/pnnl.goss.powergrid.runner/generated/distributions/runbundles/runpowergrid into $FELIX_HOME/bundle
10.  Copy all config files (except config.properties) from $PG_REPO/pnnl.goss.powergrid.runner/conf to $FELIX_HOME/conf
11.  Copy lines from $PG_REPO/pnnl.goss.powergrid.runner/conf/config.properties into $FELIX_HOME/conf/config.properties
11.  Run via felix - from $FELIX_HOME execute java -jar bin/felix.jar
12.  Verify by going to http://localhost:8181/powergrid/list.html  and log in as test/test


Installation for development (Eclipse)

1.  Download or clone the repository from github
    a.  Install github desktop https://desktop.github.com/ or sourcetree https://www.atlassian.com/software/sourcetree/overview  and Clone the GOSS Powergrid repository (https://github.com/GridOPTICS/GOSS-Powergrid)  
    b.  Or download the source  (https://github.com/GridOPTICS/GOSS-Powergrid/archive/master.zip)
2.  Install java 1.8 SDK and set JAVA_HOME variable
3.  Install Eclipse   http://www.eclipse.org/downloads/packages/release/Mars/1   (Mars 4.5.1 or earlier,  4.5.2 appears to have bugs related to bundle processing)
4.  Open eclipse with workspace set to powergrid download location,   eg. C:\Users\username\Documents\GOSS-Powergrid
5.  Install BNDTools plugin: Help->Install New Software->Work with: http://dl.bintray.com/bndtools/bndtools/3.0.0  and Install Bndtools 3.0.0 or earlier
6.  Import projects into workspace 
    a. File->Import    General->Existing Projects into workspace
    b. Select root directory, powergrid download location
    c. Select cnf, pnnl.goss.powergrid, pnnl.goss.powergrid.runner, and  pnnl.goss.powergrid.itests and import (may need to check the  Search for nested projects)
7.  If errors are detected, Right click on the powergrid project and select release, then release all bundles
8.  If you would like to you a local version of GOSS-Core  (Optional)
    a.  Update cnf/ext/repositories.bnd, 
    b.  Select source view and add the following
    c.  	aQute.bnd.deployer.repository.LocalIndexedRepo;name=GOSS Local Release;local=<location>/GOSS-Core2/cnf/releaserepo;pretty=true,\
    d. verify by switching to bndtools and verify that there are packages under GOSS Local Relase
9.  Open pnnl.goss.powergrid/bnd.bnd, Rebuild project, you should not have errors
10.  Open pnnl.goss.powergrid.runner/runpowergrid.bndrun and click Run OSGI
11.  Verify by going to http://localhost:8181/powergrid/list.html  and log in as test/test





OLD
_____________________________________

Open command line to the repository root (i.e. git/GOSS folder)
Execute gradlew install 
Installation Linux

Open terminal
Clone repository (git clone https://github.com/GridOPTICS/GOSS-Powergrid.git)
Change directory to goss (cd GOSS)
Add execute to gradlew (chmod +x gradlew)
Build core project (./gradlew install )
