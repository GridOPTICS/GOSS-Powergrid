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
class TransformerEntity {
    @Id
    @Column(name="transformer_mrid")
    String mrid
    @Embedded FromToEntity fromToBuses
    int control
    double rma
    double rmi
    double vma
    double vmi
    double step
    int table
    int importOrder
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="powergridmodel_mrid")
	PowergridModelEntity powergridModel


}

//[field: 'fromBus', datatype: int, description: ''],
//[field: 'toBus', datatype: int, description: ''],
//[field: 'ckt', datatype: String, description: ''],
//[field: 'control', datatype: int, description: ''],
//[field: 'rma', datatype: double, description: ''],
//[field: 'rmi', datatype: double, description: ''],
//[field: 'rma', datatype: double, description: ''],
//[field: 'vma', datatype: double, description: ''],
//[field: 'vmi', datatype: double, description: ''],
//[field: 'step', datatype: double, description: ''],
//[field: 'table', datatype: int, description: '']