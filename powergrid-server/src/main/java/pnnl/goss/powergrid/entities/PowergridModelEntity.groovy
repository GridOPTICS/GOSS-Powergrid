package pnnl.goss.powergrid.entities

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Column

@Entity
class PowergridModelEntity {

    /**
     * Unique id for this powergrid.
     */
    @Id
    String mrid

    /**
     * A name that the user can use to lookup the powergrid from webservice.
     * The name does not need to be unique.
     */

    String powergridName

    /**
     * User entered data about the characteristics of the model.  This is not
     * a required field.
     */

    String characteristics

    /**
     * A list of buses that belong to this powergrid.
     */
    //List<BusEntity> busEntities;

}
