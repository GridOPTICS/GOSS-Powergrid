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
class OwnerEntity {

    @Id
    @Column(name="zone_mrid")
    String mrid
    String name
    int ownerNumber
    int importOrder

}

