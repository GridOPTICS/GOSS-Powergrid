package pnnl.goss.powergrid.parsers;

public class ColumnMetaGroup {
	private String name;
	private ColumnMeta[] columns;
	
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
