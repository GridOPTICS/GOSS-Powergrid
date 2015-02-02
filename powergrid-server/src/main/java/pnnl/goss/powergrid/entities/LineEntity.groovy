package pnnl.goss.powergrid.entities

import javax.persistence.Entity
import javax.persistence.Embedded
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.CascadeType
import javax.persistence.FetchType

@Entity
class LineEntity {
    @Id
    @Column(name="line_mrid")
    String mrid
    @Embedded FromToEntity fromToBuses
//    @ManyToOne(targetEntity=BusEntity.class, optional=false)
//    @JoinColumn(name="bus_mrid", insertable=false, updatable=false)
//    BusEntity fromBus
//    @ManyToOne(targetEntity=BusEntity.class, optional=false)
//    @JoinColumn(name="bus_mrid", insertable=false, updatable=false)
//    BusEntity toBus
//    String ckt
    double r
    double x
    double b
    double ratingA
    double ratingB
    double ratingC
    double ratio
    double angle
    double shuntG1
    double shuntB1
    double shuntG2
    double shuntB2
    int status
    int importOrder
}
//	"branchId",
//	"powergridId",
//	"fromBusNumber",
//	"toBusNumber",
//	"indexNum",
//	"ckt",
//	"r",
//	"x",
//	"rating",
//	"rateA",
//	"rateB",
//	"rateC",
//	"status",
//	"p",
//	"q",
//	"mrid"

//}