package pnnl.goss.syntheticdata;

import pnnl.goss.sytheticdata.PowerflowExecutor;

public class TestPowerflowExecutor {

	public static void main(String[] args) {
		PowerflowExecutor exec = new PowerflowExecutor();
		
		exec.execute("C:/Users/d3m614/git/GOSS-Powergrid/pnnl.goss.syntheticdata/cases/dopf.py");
		
		//System.out.println("Error Length is: " +exec.getError().length());
		if (!exec.getError().isEmpty()){
			System.out.println("An error occured");
			System.out.println(exec.getError());
			System.out.println("--------------------------------------------------------------");
		}
		
		
		
		System.out.println(exec.getResults().toString());
		
		//System.out.println(exec.getOutput());
		

	}

}
