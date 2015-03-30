package pnnl.goss.powergrid.entities

import javax.persistence.Embeddable
import javax.persistence.Column

@Embeddable
class TimestepKeyEntity implements Serializable {

    /**
     * A unique entity in the system.
     */
    @Column
    String mrid

    /**
     * The time that the entity is recorded.
     */
    @Column
    Date timestep

    @Override
    public boolean equals(Object obj) {
        TimestepKeyEntity other = (TimestepKeyEntity) obj

        return other.mrid.equals(mrid) && timestep.equals(other.timestep)
    }

    @Override
    public int hashCode() {
        return mrid.hashCode() + timestep.hashCode()
    }

}
