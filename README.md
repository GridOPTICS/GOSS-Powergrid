GOSS-Powergrid
==============

A powergrid project cabable of dealing with node/breaker and bus/branch constructs.

Installation for testing
1.  Download or clone the repository from github
    a.  Install sourcetree https://www.atlassian.com/software/sourcetree/overview  and Clone the goss repository using sourcetree (https://github.com/GridOPTICS/GOSS-Powergrid)  
    b.  Or download the source  (https://github.com/GridOPTICS/GOSS-Powergrid/archive/master.zip)
2.  Install java 1.8 SDK and set java_home
3.  Run gradlew build
4.  Copy jar into felix
5.  Run via felix


Installation for development 

1.  Download or clone the repository from github
    a.  Install sourcetree https://www.atlassian.com/software/sourcetree/overview  and Clone the goss repository using sourcetree (https://github.com/GridOPTICS/GOSS-Powergrid)  
    b.  Or download the source  (https://github.com/GridOPTICS/GOSS-Powergrid/archive/master.zip)
2.  Install java 1.8 SDK and set java_home
3.  Install Eclipse   http://www.eclipse.org/downloads/packages/release/Mars/1   (Mars 4.5.1 or earlier,  4.5.2 appears to have bugs related to bundle processing)
4.  Open eclipse with workspace set to powergrid download location,   eg. C:\Users\username\Documents\GOSS-Powergrid
5.  Install BNDTools plugin: Help->Install New Software->Work with: http://dl.bintray.com/bndtools/bndtools/3.0.0  and Install Bndtools 3.0.0 or earlier
6.  Import projects into workspace 
    a. File->Import    General->Existing Projects into workspace
    b. Select root directory, powergrid download location
    c. Select cnf, pnnl.goss.powergrid, pnnl.goss.powergrid.runner, and  pnnl.goss.powergrid.itests and import (may need to check the  Search for nested projects)
7.  If you would like to you a local version of GOSS-Core  (Optional)
    a.  Update cnf/ext/repositories.bnd, 
    b.  Select source view and add the following
    c.  	aQute.bnd.deployer.repository.LocalIndexedRepo;name=GOSS Local Release;local=<location>/GOSS-Core2/cnf/releaserepo;pretty=true,\
    d. verify by switching to bndtools and verify that there are packages under GOSS Local Relase
8.  Open pnnl.goss.powergrid/bnd.bnd, Rebuild project, you should not have errors
9.  Open pnnl.goss.powergrid.runner/runpowergrid.bndrun and click Run OSGI
    a. Verify with gs:listDatasources
    b. and gs:listHandlers





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
