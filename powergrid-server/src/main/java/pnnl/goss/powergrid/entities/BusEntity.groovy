package pnnl.goss.powergrid.entities

import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.ManyToOne
import javax.persistence.CascadeType
import javax.persistence.FetchType


@Entity
class BusEntity {
    @Id
    @Column(name="bus_mrid")
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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="powergridmodel_mrid")
    PowergridModelEntity powergridModel

//    @OneToMany (cascade=[CascadeType.ALL])
//    List<GeneratorEntity> generators

    // The numbered order that this entity was located in the original file.
    int importOrder
}
