package pnnl.goss.powergrid.parsers;


public class ColumnMetaGroup {
	private String name;
	private ColumnMeta[] columns;
	
	public ColumnMetaGroup(){	}
	
	public ColumnMetaGroup(ColumnMetaGroup meta){
		// ok because strings are immutable.
		name = meta.name;
		
		columns = new ColumnMeta[meta.columns.length];
		System.arraycopy(meta.columns, 0, columns, 0, meta.columns.length);
	}
	
	public ColumnMeta getColumn(int indx){
		return columns[indx];
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public ColumnMeta[] getColumns() {
		return columns;
	}
	
	
	public void setColumns(ColumnMeta[] columns) {
		this.columns = columns;
	}
	
}
