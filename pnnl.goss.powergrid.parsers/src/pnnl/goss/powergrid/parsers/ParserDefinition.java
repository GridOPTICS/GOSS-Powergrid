package pnnl.goss.powergrid.parsers;

public class ParserDefinition {
	
	private boolean isLineSeperated;
	private String description;
	private String name;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLineSeperated() {
		return isLineSeperated;
	}
	public void setLineSeperated(boolean isLineSeperated) {
		this.isLineSeperated = isLineSeperated;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
