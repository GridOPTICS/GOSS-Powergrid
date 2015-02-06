package pnnl.goss.powergrid.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne
import javax.persistence.CascadeType
import javax.persistence.FetchType

@Entity
class GeneratorEntity {

    @Id
    @Column(name="generator_mrid")
    String mrid
    @ManyToOne(targetEntity=BusEntity.class)
    @JoinColumn(name="bus_mrid")
    BusEntity parentBus
    String machineId
    double pGen
    double qGen
    double qMax
    double qMin
    double vSetPoint
    int iRegBusNumber
    double mBase
    double zSource
    double zTran
    double rTran
    double xTran
    double gTap
    int status
    double rmPcnt
    double pMax
    double pMin
    int importOrder

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="powergridmodel_mrid")
    PowergridModelEntity powergridModel

}
