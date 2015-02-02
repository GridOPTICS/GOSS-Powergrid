package pnnl.goss.powergrid.entities

import javax.persistence.Embeddable

@Embeddable
class FromToEntity {

    String fromBusMrid
    String toBusMrid
    //BusEntity fromBus
    //BusEntity toBus
    String ckt

}
