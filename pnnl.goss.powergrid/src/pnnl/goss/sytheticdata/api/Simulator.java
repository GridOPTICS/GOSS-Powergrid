package pnnl.goss.sytheticdata.api;

public interface Simulator {
	
	/**
	 * Starts a simulation process using the passed JSON simulation
	 * options.
	 * 
	 * @param simulationOptions
	 * 
	 * @return A string simulation token that can be used to check the status.
	 */
	public String runSimulation(String jsonSimulationOptions);
		
	/**
	 * Retrieve a json status from a job token.
	 * 
	 * @param simulationToken
	 * @return
	 */
	public String getStatus(String simulationToken);
}
