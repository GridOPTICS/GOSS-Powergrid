package pnnl.goss.powergrid.entities

import javax.persistence.EmbeddedId;
import javax.persistence.Entity
import javax.persistence.Column

@Entity
class PowergridModelEntityTimestep {

    @EmbeddedId
    TimestepKeyEntity timestepKey

}
