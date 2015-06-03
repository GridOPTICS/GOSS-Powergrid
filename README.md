GOSS-Powergrid
==============

A powergrid project cabable of dealing with node/breaker and bus/branch constructs.


Installation Windows 7

1.  Install sourcetree  https://www.atlassian.com/software/sourcetree/overview 
2.  Install Eclipse   https://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/lunasr2
3.  Clone the goss repository using sourcetree (https://github.com/GridOPTICS/GOSS-Powergrid)  
4.  Open eclipse with workspace set to powergrid download location
5.  Install BNDTools plugin: Help->Eclipse Marketplace->Find bndtools  and Install Bndtools 2.4.1 REL
6.  Import projects into workspace 
    a. File->Import    General->Existing Projects into workspace
    b. Select root directory, powergrid download location
    c. Select cnf and pnnl.goss.powergrid and import
7.  





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
