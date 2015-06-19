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
package pnnl.goss.powergrid.server.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.naming.ConfigurationException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pnnl.goss.core.server.DataSourceObject;
import pnnl.goss.core.server.DataSourcePooledJdbc;
import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.api.SavePowergridResults;
import pnnl.goss.powergrid.datamodel.AlertContext;
import pnnl.goss.powergrid.datamodel.AlertContextItem;
import pnnl.goss.powergrid.datamodel.AlertSeverity;
import pnnl.goss.powergrid.datamodel.AlertType;
import pnnl.goss.powergrid.datamodel.Area;
import pnnl.goss.powergrid.datamodel.Branch;
import pnnl.goss.powergrid.datamodel.Bus;
import pnnl.goss.powergrid.datamodel.Line;
import pnnl.goss.powergrid.datamodel.Load;
import pnnl.goss.powergrid.datamodel.Machine;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.PowergridTimingOptions;
import pnnl.goss.powergrid.datamodel.Substation;
import pnnl.goss.powergrid.datamodel.SwitchedShunt;
import pnnl.goss.powergrid.datamodel.Transformer;
import pnnl.goss.powergrid.datamodel.Zone;
import pnnl.goss.powergrid.parser.api.PropertyGroup;
import pnnl.goss.powergrid.server.PowergridDataSources;

public class PowergridDaoMySql implements PowergridDao {

    private static Logger log = LoggerFactory.getLogger(PowergridDaoMySql.class);
    protected DataSourcePooledJdbc pooledDatasource;
    private AlertContext alertContext;
    private PowergridTimingOptions powergridTimingOptions;
    //private Key2ValueValue2KeyMap psseToPowergridSchemaMap = new Key2ValueValue2KeyMap();
    
    /**
     * An internal function that will allow use to get connections from either the DataSource
     * or DataSourceObject depending on how this object was instantiated.
     * 
     * @return java.sql.Connection
     */
    private Connection getConnection(){
    	Connection conn = null;
    	try {
			conn = pooledDatasource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    	return conn;
    }
        
    
    public SavePowergridResults createPowergrid(String powergridName, 
    		Map<String, List<PropertyGroup>> data){
    	
    	List<String> problems = new ArrayList<>();
    	String pgUUID = UUID.randomUUID().toString();
    	int pgId = insertPowerGrid(pgUUID, powergridName);
    	
    	if (data.get("buses") == null) {
    		problems.add("ERROR: Buses are empty");
    	}
    	else{
    		insertBuses(pgId, data.get("buses"));
    	}
    	
    	if (data.get("generators") == null) {
    		problems.add("ERROR: Generators are empty");
    	}
    	else{
    		insertGenerators(pgId, data.get("generators"));
    	}
    	
    	if (data.get("switched_shunts") == null) {
    		problems.add("ERROR: Shunts are empty");
    	}
    	else{
    		insertSwitchedShunts(pgId, data.get("switched_shunts"));
    	}
    	
    	
    	if (data.get("branches") == null) {
    		problems.add("ERROR: Branches are empty");
    	}
    	else{
    		insertBranches(pgId, data.get("branches"));
    	}
    	
    	if (data.get("areas") == null) {
    		problems.add("WARN: Areas are empty");
    	}
    	else{
    		insertAreas(pgId, data.get("areas"));
    	}
    	
    	if (data.get("zone") == null) {
    		problems.add("WARN: Zones are empty");
    	}
    	else{
    		insertZones(pgId, data.get("zone"));
    	}
    	
    	    	
    	if (pgUUID != null){
    		
    	}
    	
    	return new SavePowergridResults(pgUUID, problems);
    }
    
    private String getInsertMark(String mark, int count){
    	int i=0;
    	String data = "";
    	if (count > 0){
    		data = mark;
    		i++;
    	}
    	while(i<count){
    		data += "," + mark;
    		i++;
    	}
    	
    	return data;
    }
    
    private void logPropertyGroup(String name, PropertyGroup pg){
    	System.out.println("LOGGING Group: "+ name);
    	StringBuilder output =new StringBuilder();
    	for(String n: pg.getPropertyNames()){
    		output.append(n+ "->");
			if(pg.getProperty(n).getDataType().equals("double")){
				output.append(pg.getProperty(n).asDouble());
			}
			else if(pg.getProperty(n).getDataType().equals("int")){
				output.append(pg.getProperty(n).asInt());
			}
			else{
				output.append(pg.getProperty(n).asString());
			}
			output.append(" ");
		}
    	
    	System.out.println(name + " "+ output.toString());
    }
    
    private List<SwitchedShunt> insertSwitchedShunts(int powergridId, List<PropertyGroup> switchedShuntPropertyGroups){
    	String insert = "INSERT INTO switchedshunt ("+
    			"SwitchedShuntId, PowergridId, BusNumber, Status, BShunt, BInit, ModSw, VswHi, VswLo, " +
    			"SwRem, N1, B1, N2, B2, N3, B3, N4, B4, B5, N5, N6, B6, N7, B7, N8, B8, Mrid) " +
    			"VALUES (" + 
    			"@SwitchedShuntId, @PowergridId, @BusNumber, @Status, @BShunt, @BInit, @ModSw, @VswHi, " +
    			"@VswLo, @SwRem, @N1, @B1, @N2, @B2, @N3, @B3, @N4, @B4, @N5, @B5, @N6, @B6, @N7, @B7, " +
    			"@N8, @B8, @Mrid);";
    	Map<Integer, Integer> busnumberCount = new HashMap<>();
    	
    	for(PropertyGroup pg: switchedShuntPropertyGroups){
    		if (!busnumberCount.containsKey(pg.getProperty("busNumber").asInt())){
    			busnumberCount.put(pg.getProperty("busNumber").asInt(), 0);
    		}
    		
    		try(Connection conn = getConnection()){
    			// Increase the count for this specific bus.
    			busnumberCount.put(pg.getProperty("busNumber").asInt(), 
    					busnumberCount.get(pg.getProperty("busNumber").asInt())+1);
	    		NamedParamStatement namedStmt = new NamedParamStatement(conn, insert);
	    		// This will be auto populated for us.
	    		namedStmt.setString("SwitchedShuntId", 
	    				busnumberCount.get(pg.getProperty("busNumber").asInt()).toString());
	    		namedStmt.setInt("PowergridId", powergridId);
				namedStmt.setInt("BusNumber", pg.getProperty("busNumber").asInt());
	    		namedStmt.setInt("Status", pg.getProperty("status").asInt());
	    		namedStmt.setDouble("BShunt", 0); //pg.getProperty("").asDouble());
	    		namedStmt.setDouble("BInit", pg.getProperty("bInit").asDouble());
	    		namedStmt.setInt("ModSw", 0); // pg.getProperty("mod").asInt());
	    		namedStmt.setDouble("VswHi", pg.getProperty("vswHi").asDouble());
	    		namedStmt.setDouble("VswLo", pg.getProperty("vswLo").asDouble());
	    		namedStmt.setInt("SwRem", pg.getProperty("swRem").asInt());
	    		namedStmt.setInt("N1", pg.getProperty("").asInt());
	    		namedStmt.setDouble("B1", pg.getProperty("").asDouble());
	    		namedStmt.setInt("N2", pg.getProperty("").asInt());
	    		namedStmt.setDouble("B2", pg.getProperty("").asDouble());
	    		namedStmt.setInt("N3", pg.getProperty("").asInt());
	    		namedStmt.setDouble("B3", pg.getProperty("").asDouble());
	    		namedStmt.setInt("N4", pg.getProperty("").asInt());
	    		namedStmt.setDouble("B4", pg.getProperty("").asDouble());
	    		namedStmt.setInt("N5", pg.getProperty("").asInt());
	    		namedStmt.setDouble("B5", pg.getProperty("").asDouble());
	    		namedStmt.setInt("N6", pg.getProperty("").asInt());
	    		namedStmt.setDouble("B6", pg.getProperty("").asDouble());
	    		namedStmt.setInt("N7", pg.getProperty("").asInt());
	    		namedStmt.setDouble("B7", pg.getProperty("").asDouble());
	    		namedStmt.setInt("N8", pg.getProperty("").asInt());
	    		namedStmt.setDouble("B8", pg.getProperty("").asDouble());
	    		namedStmt.setString("Mrid", UUID.randomUUID().toString());
				
				namedStmt.execute();
				
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    		
    	return getSwitchedShunts(powergridId);
    }
    private List<Machine> insertGenerators(int powergridId, List<PropertyGroup> generatorPropertyGroups){
    	String insert = "INSERT INTO machine (" +
    			"MachineId, PowergridId, BusNumber, PGen, QGen, MaxPGen, MaxQGen, MinPGen,"+
    			"MinQGen, Status, IsSvc, Vs, Ireg, Zr, Zx, Rt, Xt, Gtap, RmPct, Mrid, MBase) "+
    			"VALUES (" +
    			"@MachineId, @PowergridId, @BusNumber, @PGen, @QGen, @MaxPGen, @MaxQGen, " +
    			"@MinPGen, @MinQGen, @Status, @IsSvc, @Vs, @Ireg, @Zr, @Zx, @Rt, @Xt, @Gtap, @RmPct, @Mrid, "+
    			"@MBase);";
    	 
    	for(PropertyGroup pg: generatorPropertyGroups){
	    	try(Connection conn = getConnection()){
	    		
	    		NamedParamStatement namedStmt = new NamedParamStatement(conn, insert);
	    		// This will be auto populated for us.
	    		namedStmt.setString("MachineId", pg.getProperty("machineId").asString());
				namedStmt.setInt("PowergridId", powergridId);
				namedStmt.setInt("BusNumber", pg.getProperty("busNumber").asInt());
				namedStmt.setDouble("PGen", pg.getProperty("pGen").asDouble());
				namedStmt.setDouble("QGen", pg.getProperty("qGen").asDouble());
				namedStmt.setDouble("MaxPGen", pg.getProperty("pMax").asDouble());
				namedStmt.setDouble("MaxQGen", pg.getProperty("qMax").asDouble());
				namedStmt.setDouble("MinPGen", pg.getProperty("pMin").asDouble());
				namedStmt.setDouble("MinQGen", pg.getProperty("qMin").asDouble());
				namedStmt.setDouble("Status", pg.getProperty("status").asInt());
				namedStmt.setInt("IsSvc", 0);
				namedStmt.setDouble("Vs", pg.getProperty("vSetPoint").asDouble());
				namedStmt.setDouble("Ireg", pg.getProperty("iRegBusNumber").asInt());
				namedStmt.setDouble("Zr", pg.getProperty("zSource").asDouble());
				namedStmt.setDouble("Zx", pg.getProperty("zTran").asDouble());
				namedStmt.setDouble("Rt", pg.getProperty("rTran").asDouble());
				namedStmt.setDouble("Xt", pg.getProperty("xTran").asDouble());
				namedStmt.setDouble("Gtap", pg.getProperty("gTap").asDouble());
				namedStmt.setDouble("RmPct", pg.getProperty("rmPcnt").asDouble());
				namedStmt.setString("Mrid", UUID.randomUUID().toString());
				namedStmt.setDouble("MBase", 0.0);
				
				namedStmt.execute();
				
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return getMachines(powergridId);
    }
    
    private List<Branch> insertBranches(int powergridId, List<PropertyGroup> branchPropertyGroups){
    	List<Branch> zone = new ArrayList<>();
    	String insert = "INSERT INTO branch ("+
    			"PowergridId, FromBusNumber, ToBusNumber, Ckt, R, X, RateA, RateB, RateC, "+
    			"Status, P, Q, Mrid) " + 
    			"VALUES ( " + 
    			"@PowergridId, @FromBusNumber, @ToBusNumber, @Ckt, @R, @X, @RateA, @RateB, "+
    			"@RateC, @Status, @P, @Q, @Mrid);";
    	
    	
    	for(PropertyGroup pg: branchPropertyGroups){
    		
	    	try(Connection conn = getConnection()){
	    		
	    		NamedParamStatement namedStmt = new NamedParamStatement(conn, insert);
	    		// This will be auto populated for us.
	    		namedStmt.setInt("PowergridId", powergridId);
	    		namedStmt.setInt("FromBusNumber", pg.getProperty("fromBus").asInt());
	    		namedStmt.setInt("ToBusNumber", pg.getProperty("toBus").asInt());
	    		namedStmt.setString("Ckt", pg.getProperty("ckt").asString());
	    		namedStmt.setDouble("R", pg.getProperty("r").asDouble());
	    		namedStmt.setDouble("X", pg.getProperty("x").asDouble());
	    		namedStmt.setDouble("RateA", pg.getProperty("ratingA").asDouble());
	    		namedStmt.setDouble("RateB", pg.getProperty("ratingB").asDouble());
	    		namedStmt.setDouble("RateC", pg.getProperty("ratingC").asDouble());
	    		namedStmt.setInt("Status", pg.getProperty("status").asInt());
	    		
	    		// Now we need to create a line or transformer depending on the
	    		// data.
	    		namedStmt.setDouble("P", 0.0); // pg.getProperty("").asDouble());
	    		namedStmt.setDouble("Q", 0.0); //pg.getProperty("").asDouble());
	    		namedStmt.setString("Mrid", UUID.randomUUID().toString());
	    		namedStmt.execute();
//    	    		
//	    			stmt.setInt(1, indx);
//	    			stmt.setInt(2, powergridId);
//	    			stmt.setInt(3,  pg.getProperty("fromBus").asInt());
//	    			stmt.setInt(4,  pg.getProperty("toBus").asInt());
//	    			stmt.setInt(5, 1);
//	    			stmt.setString(6, pg.getProperty("ckt").asString());
//	    			stmt.setDouble(7, pg.getProperty("r").asDouble());
//	    			stmt.setDouble(8, pg.getProperty("x").asDouble());
//	    			// also insert b?
//	    			stmt.setDouble(9, pg.getProperty("ratingA").asDouble());
//	    			stmt.setDouble(10, pg.getProperty("ratingB").asDouble());
//	    			stmt.setDouble(11, pg.getProperty("ratingC").asDouble());
//	    			// also insert ratio
//	    			stmt.setDouble(12, pg.getProperty("").asDouble());
//	    			stmt.setInt(13, pg.getProperty("").asInt());
//	    			stmt.setDouble(14, pg.getProperty("").asDouble());
//	    			stmt.setDouble(15, pg.getProperty("").asDouble());
//	    			
//	    			stmt.setString(16, UUID.randomUUID().toString());
//	    			stmt.execute();
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return getBranches(powergridId);
    }
    
    private List<Area> insertAreas(int powergridId, List<PropertyGroup> areaPropertyGroups){
    	List<Area> zone = new ArrayList<>();
    	String insert = "INSERT INTO area("
    			+"PowergridId,AreaName,AreaId,Isw,Pdes,Ptol,Mrid)"
    			+"VALUES("+getInsertMark("?", 7)+");";
    	
    	for(PropertyGroup pg: areaPropertyGroups){
	    	try(Connection conn = getConnection()){
	    		try(PreparedStatement stmt = conn.prepareStatement(insert,  Statement.RETURN_GENERATED_KEYS)){
	    			stmt.setInt(1, powergridId);
	    			stmt.setString(2, pg.getProperty("name").asString());
	    			stmt.setInt(3,  pg.getProperty("areaNumber").asInt());
	    			stmt.setInt(4,  pg.getProperty("isw").asInt());
	    			stmt.setDouble(5, 0);
	    			stmt.setDouble(6, pg.getProperty("pTolerance").asDouble());
	    			stmt.setString(7, UUID.randomUUID().toString());
	    			stmt.execute();
	    		}
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return getAreas(powergridId);
    }
    
    private List<Zone> insertZones(int powergridId, List<PropertyGroup> zonePropertyGroups){
    	List<Zone> zone = new ArrayList<>();
    	String insert = "INSERT INTO zone("
    			+"PowergridId, ZoneNumber, ZoneName, Mrid)"
    			+"VALUES(@PowergridId, @ZoneNumber, @ZoneName, @Mrid);";
    	
    	for(PropertyGroup pg: zonePropertyGroups){
	    	try(Connection conn = getConnection()){	    		
	    		NamedParamStatement namedStmt = new NamedParamStatement(conn, insert);
	    		namedStmt.setInt("PowergridId", powergridId);
	    		namedStmt.setInt("ZoneNumber", pg.getProperty("zoneNumber").asInt());
	    		namedStmt.setString("ZoneName", pg.getProperty("name").asString());
	    		namedStmt.setString("Mrid", UUID.randomUUID().toString());

	    		namedStmt.execute();	    			
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return getZones(powergridId);
    }
    
    private List<Bus> insertBuses(int powergridId, List<PropertyGroup> busPropertyGroups){
       	String insert = "INSERT INTO bus(" 
    			+"BusNumber,PowergridId,SubstationId,BusName,BaseKV,Code,Pl,Ql,Gl,Bl,AreaId,Va,Vm,ZoneId,Mrid)"
    			+"VALUES("+ getInsertMark("?", 15)+");";
    	
		
    	for(PropertyGroup pg: busPropertyGroups){
    		String busUUID = UUID.randomUUID().toString();
    		try(Connection conn = getConnection()){
	    		try(PreparedStatement stmt = conn.prepareStatement(insert,  Statement.RETURN_GENERATED_KEYS)){    			
	    			stmt.setInt(1, pg.getProperty("busNumber").asInt());    			
	    			stmt.setInt(2, powergridId);
	    			stmt.setInt(3, 0);
	    			stmt.setString(4, pg.getProperty("name").asString());
	    			stmt.setDouble(5, pg.getProperty("baseKv").asDouble());
	    			stmt.setInt(6, pg.getProperty("busType").asInt());
	    			stmt.setDouble(7, pg.getProperty("pLoad").asDouble());
	    			stmt.setDouble(8, pg.getProperty("qLoad").asDouble());
	    			stmt.setDouble(9, pg.getProperty("gShunt").asDouble());
	    			stmt.setDouble(10, pg.getProperty("bShunt").asDouble());
	    			
	    			stmt.setInt(11, pg.getProperty("area").asInt());
	    			stmt.setDouble(12, pg.getProperty("vMag").asDouble());
	    			stmt.setDouble(13, pg.getProperty("vAng").asDouble());
	    			stmt.setInt(14, pg.getProperty("zone").asInt());
	    			stmt.setString(15, busUUID);
	    			
	    			stmt.execute();
	    		}
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	
    	return getBuses(powergridId);
    	
    }
    
    private int insertPowerGrid(String uuid, String name){
    	List<Powergrid> grids = getAvailablePowergrids();
    	int maxPg = 0;
    	for(Powergrid g: grids){
    		if(g.getPowergridId() > maxPg){
    			maxPg = g.getPowergridId();
    		}
    	}
    	
    	String insert = "INSERT INTO powergrid(PowergridId, Name,CoordinateSystem,Mrid) VALUES(?,?,?,?);";
    	
    	int pgId = maxPg + 1;
    	try(Connection conn = getConnection()){
    		try(PreparedStatement stmt = conn.prepareStatement(insert,  Statement.RETURN_GENERATED_KEYS)){
    			stmt.setInt(1, pgId);
    			stmt.setString(2, name);
    			stmt.setString(3,  "");
    			stmt.setString(4, uuid);
    			stmt.execute();
    		}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return pgId;
    }

//    public PowergridDaoMySql() {
//        log.debug("Creating " + PowergridDaoMySql.class);
//        alertContext = new AlertContext();
//        initializeAlertContext();
//        //initializeSchemaMap();
//
//    }
    
    /**
     * The assumption is 
     * @param datasource
     */
    public PowergridDaoMySql(DataSourcePooledJdbc datasource) {
        log.debug("Creating " + PowergridDaoMySql.class + " with DataSourceObject.");
        this.pooledDatasource = datasource;
        //PowergridDataSource ds = (PowergridDataSource)datasource;
        //this.datasource = ds.getDatasource();
        alertContext = new AlertContext();
        initializeAlertContext();
//        initializeSchemaMap();
    }
    
//    private void initializeSchemaMap(){
//    	Key2ValueValue2KeyMap nd = psseToPowergridSchemaMap;
//    	try {
//			nd.loadFromFile(new File("map.json"));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//    	
//    	nd.setKeyContainerName("psse");
//    	nd.setValueContainerName("powergrid");
//    	
//    	nd.add("busNumber", "BusNumber");
//    	nd.add("busType", "Code");
//    	
//    	try {
//			nd.saveToFile(new File("map.json"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    }

//    public PowergridDaoMySql(DataSource datasource) {
//        log.debug("Creating " + PowergridDaoMySql.class);
//        this.datasource = datasource;
//        alertContext = new AlertContext();
//        initializeAlertContext();
//    }

    public AlertContext getAlertContext(int powergridId){
        return alertContext;
    }

    public void setPowergridTimingOptions(PowergridTimingOptions timingOptions){
        this.powergridTimingOptions = timingOptions;
    }

    public PowergridTimingOptions getPowergridTimingOptions(){
        return this.powergridTimingOptions;
    }

    private void initializeAlertContext(){

        alertContext.addContextElement(new AlertContextItem(AlertSeverity.SEVERITY_HIGH, AlertType.ALERTTYPE_BRANCH, 95.5, "mvar"));
        alertContext.addContextElement(new AlertContextItem(AlertSeverity.SEVERITY_WARN, AlertType.ALERTTYPE_BRANCH, 90.0, "mvar"));

        alertContext.addContextElement(new AlertContextItem(AlertSeverity.SEVERITY_HIGH, AlertType.ALERTTYPE_SUBSTATION, 0.1, "+- % nominal buses"));
        alertContext.addContextElement(new AlertContextItem(AlertSeverity.SEVERITY_WARN, AlertType.ALERTTYPE_SUBSTATION, 0.05, "+- % nominal buses"));
    }

    public List<Powergrid> getAvailablePowergrids() {
        List<Powergrid> grids = new ArrayList<Powergrid>();

        String dbQuery = "select pg.powergridId, pg.Name, a.mrid from powergrid pg inner join area a on pg.Powergridid=a.PowergridId";
        
        try (Statement stmt = getConnection().createStatement()){
        	try (ResultSet rs = stmt.executeQuery(dbQuery.toLowerCase())){
	            while (rs.next()) {
	                Powergrid item = new Powergrid();
	                item.setPowergridId(rs.getInt(1));
	                item.setName(rs.getString(2));
	                item.setMrid(rs.getString("mrid"));
	                grids.add(item);
	            }
        	}

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return grids;
    }

    public List<String> getPowergridNames() {
        List<Powergrid> grids = getAvailablePowergrids();
        List<String> names = new ArrayList<String>();
        for (Powergrid g : grids) {
            names.add(g.getName());
        }
        return names;
    }

    public Powergrid getPowergridById(int powergridId) {
        String dbQuery = "select pg.PowergridId, pg.Name, a.mrid from powergrid pg INNER JOIN area a ON pg.PowergridId=a.PowergridId where pg.PowergridId = " + powergridId;
        Powergrid grid = new Powergrid();
        ResultSet rs = null;
        Connection conn = null;

        try {
            log.debug(dbQuery);
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());
            rs.next();
            grid.setPowergridId(rs.getInt(1));
            grid.setName(rs.getString(2));
            grid.setMrid(rs.getString("mrid"));
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return grid;
    }

    public Powergrid getPowergridByName(String powergridName) {
        String dbQuery = "select * from powergrid where name = '" + powergridName + "'";
        Powergrid grid = new Powergrid();
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());
            rs.next();
            grid.setPowergridId(rs.getInt(1));
            grid.setName(rs.getString(2));
            grid.setMrid(rs.getString("mrid"));
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return grid;
    }

    public PowergridModel getPowergridModelAtTime(int powergridId, Timestamp timestep) {
        PowergridModel model = getPowergridModel(powergridId);
        updateModelToTimestep(model, timestep);
        return model;
    }

    /**
     * Constructs a powergrid model out of the mysql database. The caller can
     * then use the powergrid model passed back as it's datasource.
     */
    public PowergridModel getPowergridModel(int powergridId) {
        PowergridModel model = new PowergridModel(alertContext);

        model.setAreas(getAreas(powergridId));
        model.setBranches(getBranches(powergridId));
        model.setSubstations(getSubstations(powergridId));
        try {
            model.setBuses(getBuses(powergridId));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        model.setLines(getLines(powergridId));
        model.setLoads(getLoads(powergridId));
        model.setMachines(getMachines(powergridId));
        model.setPowergrid(getPowergridById(powergridId));


        model.setSwitchedShunts(getSwitchedShunts(powergridId));
        // model.setTimesteps(getTimeSteps(powergridId));
        model.setTransformers(getTransformers(powergridId));
        model.setZones(getZones(powergridId));

        return model;
    }

    public List<Timestamp> getTimeSteps(int powergridId) {
        List<Timestamp> items = new ArrayList<Timestamp>();
        String dbQuery = "select * from powergridtimesteps where PowerGridId = " + powergridId;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());

            while (rs.next()) {
                items.add(rs.getTimestamp(2));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;

    }

    public List<Area> getAreas(int powergridId) {
        List<Area> items = new ArrayList<Area>();
        String dbQuery = "select * from area where PowerGridId = " + powergridId;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());

            while (rs.next()) {
                Area area = new Area();
                area.setPowergridId(powergridId);
                area.setAreaName(rs.getString(2));
                area.setMrid(rs.getString("mrid"));
                area.setPtol(rs.getDouble("Ptol"));
                area.setIsw(rs.getInt("Isw"));
                items.add(area);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;
    }

    public List<Branch> getBranches(int powergridId) {
        List<Branch> items = new ArrayList<Branch>();
        String dbQuery = "select * from branch where PowerGridId = " + powergridId;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());

            while (rs.next()) {
                Branch branch = new Branch();
                branch.setPowergridId(powergridId);
                branch.setFromBusNumber(rs.getInt("FromBusNumber"));
                branch.setToBusNumber(rs.getInt("ToBusNumber"));
                branch.setCkt(rs.getString("Ckt"));
                branch.setR(rs.getDouble("R"));
                branch.setX(rs.getDouble("X"));
                branch.setRating(rs.getDouble("RateA"));
                branch.setRateA(rs.getDouble("RateA"));
                branch.setRateB(rs.getDouble("RateB"));
                branch.setRateC(rs.getDouble("RateC"));
                branch.setStatus(rs.getInt("Status"));
                branch.setP(rs.getDouble("P"));
                branch.setQ(rs.getDouble("Q"));
                branch.setMrid(rs.getString("Mrid"));
                items.add(branch);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;
    }

    public List<Bus> getBuses(int powergridId) {
        List<Bus> items = new ArrayList<Bus>();
        String dbQuery = "select * from bus where PowerGridId = " + powergridId + " ORDER BY BusNumber";

        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());

            while (rs.next()) {
                Bus bus = new Bus();
                bus.setPowergridId(powergridId);
                bus.setBusNumber(rs.getInt(1));
                bus.setSubstationId(rs.getInt(3));
                bus.setBusName(rs.getString(4));
                bus.setBaseKv(rs.getDouble(5));
                bus.setCode(rs.getInt(6));
                bus.setVa(rs.getDouble(7));
                bus.setVm(rs.getDouble(8));
                bus.setMrid(rs.getString("mrid"));
                items.add(bus);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;
    }

    public List<Line> getLines(int powergridId) {
        List<Line> items = new ArrayList<Line>();
        String dbQuery = "select * from lines_ where PowerGridId = " + powergridId + " ORDER BY LineId";
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());

            while (rs.next()) {
                Line line = new Line();
                line.setPowergridId(powergridId);
                line.setLineId(rs.getInt(1));
                line.setBcap(rs.getDouble(4));
                line.setBranchId(rs.getInt(3));
                items.add(line);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;

    }

    public List<Load> getLoads(int powergridId) {
        List<Load> items = new ArrayList<Load>();
        String dbQuery = "select * from loads where PowerGridId = " + powergridId;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());

            while (rs.next()) {
                Load load = new Load();
                load.setPowergridId(powergridId);
                load.setBusNumber(rs.getInt(3));
                load.setLoadId(rs.getInt(1));
                load.setLoadName(rs.getString(4));
                load.setPload(rs.getDouble(5));
                load.setQload(rs.getDouble(6));
                items.add(load);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;
    }

    public List<Machine> getMachines(int powergridId) {
        List<Machine> items = new ArrayList<Machine>();
        String dbQuery = "select * from machine where PowerGridId = " + powergridId;
        ResultSet rs = null;

        try (Connection conn = getConnection()){
            try (Statement stmt = conn.createStatement()){
	            rs = stmt.executeQuery(dbQuery.toLowerCase());
	
	            while (rs.next()) {
	                Machine machine = new Machine();
	                machine.setPowergridId(powergridId);
	                machine.setMachineId(rs.getString("MachineId"));
	                machine.setBusNumber(rs.getInt("BusNumber"));
	                machine.setIsSvc(rs.getInt("IsSvc"));
	                machine.setMaxPgen(rs.getDouble("MaxPGen"));
	                machine.setMaxQgen(rs.getDouble("MaxQGen"));
	                machine.setMinPgen(rs.getDouble("MinPGen"));
	                machine.setMinQgen(rs.getDouble("MinQGen"));
	                machine.setPgen(rs.getDouble("PGen"));
	                machine.setQgen(rs.getDouble("QGen"));
	                machine.setStatus(rs.getInt("Status"));
	                items.add(machine);
	            }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return items;
    }

    public List<SwitchedShunt> getSwitchedShunts(int powergridId) {
        List<SwitchedShunt> items = new ArrayList<SwitchedShunt>();
        String dbQuery = "select * from switchedshunt where PowerGridId = " + powergridId;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());

            while (rs.next()) {
                SwitchedShunt shunt = new SwitchedShunt();
                shunt.setPowergridId(powergridId);
                shunt.setBinit(rs.getDouble("BInit"));
                shunt.setBshunt(rs.getDouble("BShunt"));
                shunt.setBusNumber(rs.getInt("BusNumber"));
                shunt.setStatus(rs.getInt("Status"));
                shunt.setSwitchedShuntId(rs.getString("SwitchedShuntId"));
                items.add(shunt);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;
    }

    public List<Substation> getSubstations(int powergridId) {
        List<Substation> items = new ArrayList<Substation>();
        String dbQuery = "select * from substation where PowerGridId = " + powergridId;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();

            rs = stmt.executeQuery(dbQuery.toLowerCase());

            while (rs.next()) {
                Substation substation = new Substation();
                substation.setPowergridId(powergridId);
                substation.setAreaName(rs.getString(3));
                substation.setLatitude(rs.getDouble(6));
                substation.setLongitude(rs.getDouble(7));
                substation.setSubstationId(rs.getInt(1));
                substation.setSubstationName(rs.getString(5));
                substation.setZoneName(rs.getString(4));
                substation.setMrid(rs.getString("mrid"));
                items.add(substation);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;
    }

    public List<Transformer> getTransformers(int powergridId) {
        List<Transformer> items = new ArrayList<Transformer>();
        String dbQuery = "select * from transformers where PowerGridId = " + powergridId;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());
            while (rs.next()) {
                Transformer transformer = new Transformer();
                transformer.setPowergridId(powergridId);
                transformer.setBranchId(rs.getInt(3));
                transformer.setRatio(rs.getDouble(4));
                transformer.setTapPosition(rs.getDouble(5));
                transformer.setTransformerId(rs.getInt(1));
                items.add(transformer);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;
    }

    public List<Zone> getZones(int powergridId) {
        List<Zone> items = new ArrayList<Zone>();
        String dbQuery = "select * from zone where PowerGridId = " + powergridId;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(dbQuery.toLowerCase());
            while (rs.next()) {
                Zone zone = new Zone();
                zone.setPowergridId(powergridId);
                zone.setZoneName(rs.getString(1));
                items.add(zone);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return items;

    }

    private void updateModelToTimestep(PowergridModel model, Timestamp timestamp) {
        // Build the sql using the databasename as a format parameter
        String queryLine = "SELECT lts.LineId, lts.Status, lts.P as PFlow, lts.Q as QFlow from linetimesteps lts WHERE lts.PowergridId=? and lts.TimeStep=?";
        String queryMachine = "SELECT mts.MachineId, mts.PGen, mts.QGen, mts.Status from machinetimesteps mts WHERE mts.PowergridId=? and mts.TimeStep=?";
        String queryLoads = "SELECT lts.LoadId, lts.PLoad, lts.QLoad from loadtimesteps lts WHERE lts.PowergridId=? and lts.TimeStep=?";
        String queryShunts = "SELECT sts.SwitchedShuntId, sts.Status from switchedshunttimesteps sts WHERE sts.PowergridId=? and sts.TimeStep=?";
        try {
            int powergridId = model.getPowergrid().getPowergridId();
            String timestep = timestamp.toString();

            // Prepare and execute results.
            ResultSet rs = prepareAndExecute(queryLine, powergridId, timestep);
            HashSet<Integer> doneLines = new HashSet<Integer>();
            while (rs.next()) {
                int lineId = rs.getInt("lts.LineId");

                if (!doneLines.contains(lineId)) {
                    Branch branch = model.getBranch(lineId);
                    branch.setP(rs.getDouble("PFlow"));
                    branch.setQ(rs.getDouble("QFlow"));
                    branch.setStatus(rs.getInt("lts.Status"));
                    doneLines.add(lineId);
                }
            }

            // Prepare and execute results.
            rs = prepareAndExecute(queryMachine, powergridId, timestep);
            HashSet<Integer> doneMachines = new HashSet<Integer>();
            while (rs.next()) {
                int id = rs.getInt("mts.MachineId");

                if (!doneMachines.contains(id)) {
                    Machine item = model.getMachine(id);

                    if (item == null){
                        log.error("Machine is null can't update it! for id " + id);
                        continue;
                    }
                    item.setPgen(rs.getDouble("mts.PGen"));
                    item.setQgen(rs.getDouble("mts.QGen"));
                    item.setStatus(rs.getInt("mts.Status"));

                    doneMachines.add(id);
                }
            }

            // Prepare and execute results.
            rs = prepareAndExecute(queryLoads, powergridId, timestep);
            HashSet<Integer> doneLoads = new HashSet<Integer>();
            while (rs.next()) {
                int id = rs.getInt("lts.LoadId");

                if (!doneLoads.contains(id)) {
                    Load item = model.getLoad(id);
                    if (item == null){
                        log.error("Load is null can't update it! for id " + id);
                        continue;
                    }
                    item.setPload(rs.getDouble("lts.PLoad"));
                    item.setQload(rs.getDouble("lts.QLoad"));

                    doneLoads.add(id);
                }
            }

            // Prepare and execute results.
            rs = prepareAndExecute(queryShunts, powergridId, timestep);
            HashSet<Integer> doneShunts = new HashSet<Integer>();
            while (rs.next()) {
                int id = rs.getInt("sts.SwitchedShuntId");

                if (!doneShunts.contains(id)) {
                    SwitchedShunt item = model.getSwitchedShunt(id);
                    item.setStatus(rs.getInt("sts.Status"));

                    doneShunts.add(id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Null powergrid == something failed!
            model = null;
        }
    }

    private ResultSet prepareAndExecute(String query, int powergridId, String timestep) throws ParseException, SQLException, ConfigurationException {
        PreparedStatement stmt = getConnection().prepareStatement(query);
        stmt.setInt(1, powergridId);
        stmt.setString(2, timestep);
        return stmt.executeQuery();
    }

    @Override
    public Powergrid getPowergridByMrid(String mrid) {
    	String dbQuery = "select pg.PowergridId, pg.Name, a.mrid from powergrid pg INNER JOIN area a ON pg.PowergridId=a.PowergridId where pg.Mrid = ?";
        Powergrid grid = new Powergrid();

        try (Connection conn = pooledDatasource.getConnection()) {
        	try (PreparedStatement stmt = conn.prepareStatement(dbQuery)){
        		stmt.setString(1, mrid);
        		try(ResultSet rs = stmt.executeQuery()){
        			rs.next();
                    grid.setPowergridId(rs.getInt("PowergridId"));
                    grid.setName(rs.getString("name"));
                    grid.setMrid(rs.getString("mrid"));
        		}
        	}
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        return grid;
    }

    @Override
    public void persist(Powergrid powergrid) {
        // TODO Auto-generated method stub

    }

}
