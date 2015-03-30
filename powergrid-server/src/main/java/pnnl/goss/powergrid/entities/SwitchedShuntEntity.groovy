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
class SwitchedShuntEntity {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="powergridmodel_mrid")
    PowergridModelEntity powergridModel

    String mrid

    @ManyToOne(targetEntity=BusEntity.class)
    @JoinColumn(name="bus_mrid")
    BusEntity parentBus

    int controlMode
    double vswHi
    double vswLo
    int swRem
    int bInit
    int n1
    double b1
    int n2
    double b2
    int n3
    double b3
    int n4
    double b4
    int n5
    double b5
    int n6
    double b6
    int n7
    double b7
    int n8
    double b8

    // The numbered order that this entity was located in the original file.
    int importOrder
}


//switchedShuntColumnsDef = [
//	[field: 'busNumber', datatype: int, description: ''],
//	[field: 'controlMode', datatype: int, description: ''],
//	[field: 'vswHi', datatype: double, description: ''],
//	[field: 'vswLo', datatype: double, description: ''],
//	[field: 'swRem', datatype: int, description: ''],
//	[field: 'bInit', datatype: int, description: ''],
//	[field: 'n1', datatype: int, description: ''],
//	[field: 'b1', datatype: double, description: ''],
//	[field: 'n2', datatype: int, description: ''],
//	[field: 'b2', datatype: double, description: ''],
//	[field: 'n3', datatype: int, description: ''],
//	[field: 'b3', datatype: double, description: ''],
//	[field: 'n4', datatype: int, description: ''],
//	[field: 'b4', datatype: double, description: ''],
//	[field: 'n5', datatype: int, description: ''],
//	[field: 'b5', datatype: double, description: ''],
//	[field: 'n6', datatype: int, description: ''],
//	[field: 'b6', datatype: double, description: ''],
//	[field: 'n7', datatype: int, description: ''],
//	[field: 'b7', datatype: double, description: ''],
//	[field: 'n8', datatype: int, description: ''],
//	[field: 'b8', datatype: double, description: '']
//]
