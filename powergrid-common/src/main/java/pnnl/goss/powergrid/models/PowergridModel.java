package pnnl.goss.powergrid.models;

import java.util.Collection;

public interface PowergridModel {

    Bus getBus(int busNumber);

    Collection<Bus> getBuses();

    Substation getSubstation(int id);

    Substation getSubstation(Bus bus);

//    Transformer getTransformer(int id);
//
//    Load getLoad(int id);
//
//    Machine getMachine(int id);
//
//    SwitchedShunt getSwitchedShunt(int id);
//
//    Branch getBranch(int id);
//
//    Bus getBus(int busNumber);
//

//
//    AlertContext getAlertContext();
//
//    List<Line> getLines();
//
//    List<Transformer> getTransformers();
//
//    List<Zone> getZones();
//
//    List<Area> getAreas();
//
//    Powergrid getPowergrid();
//
//    List<Branch> getBranches();
//
//    List<Substation> getSubstations();
//
//    List<Load> getLoads();
//
//    List<Machine> getMachines();
//
//    List<SwitchedShunt> getSwitchedShunts();
//
//    List<Bus> getBuses();
//
//    List<Alert> getAlerts();

}
