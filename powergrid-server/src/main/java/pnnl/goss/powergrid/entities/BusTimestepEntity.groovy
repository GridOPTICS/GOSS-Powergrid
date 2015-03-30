package pnnl.goss.powergrid.entities


import java.util.Date;

import javax.persistence.Entity
import javax.persistence.Column
import javax.persistence.EmbeddedId

@Entity
class BusTimestepEntity {

    @EmbeddedId
    TimestepKeyEntity timestepKey

    @Column(name="qLoad")
    double QLoad
    @Column(name="pLoad")
    double PLoad
    @Column(name="gShunt")
    double GShunt
    @Column(name="bShunt")
    double BShunt
    double va
    double vm
}
