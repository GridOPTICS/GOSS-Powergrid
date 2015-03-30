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
class BranchEntity {
    @Id
    @Column(name="branch_mrid")
    String mrid
    @Embedded FromToEntity fromToBuses

    String ckt
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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="powergridmodel_mrid")
    PowergridModelEntity powergridModel
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