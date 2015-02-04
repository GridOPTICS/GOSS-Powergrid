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
class AreaEntity {

    @Id
    @Column(name="area_mrid")
    String mrid
    String name
    int areaNumber
    int isw
    double pDesired
    double pTolerance
    int importOrder

}
