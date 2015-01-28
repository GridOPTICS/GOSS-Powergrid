package pnnl.goss.powergrid.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class BusEntity {
    @Id
    String mrid
    String name
    String busName
    int busNumber
    int powergridId
    int substationId
    double baseKv
    int code
    @Column(name="qLoad")
    double QLoad
    @Column(name="pLoad")
    double PLoad
    @Column(name="gShunt")
    double GShunt
    @Column(name="bShunt")
    double BShunt
    int areaId
    int zoneId
    double va
    double vm
    int fileOrder
}
