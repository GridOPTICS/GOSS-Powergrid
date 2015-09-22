package pnnl.goss.syntheticdata.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PowerflowExecutor {

	final static String python = "C:/Users/d3m614/git/python/Scripts/python.exe";
	final static String command = "C:/Users/d3m614/git/GOSS-Powergrid/cases/dopf.py";
	final static String ep = "C:/Users/d3m614/git/GOSS-Powergrid/cases/case14.py";
	private String rawError = null;
	private String rawOutput = null;
	private JsonObject jsonObject = null;
	private String prettyResults = null;

	public String getError(){
		return rawError;
	}

	public JsonObject getResults(){
		return jsonObject;
	}

	public String getPrettyResults(){
		return prettyResults;
	}

	private void parseResults(){
		try{
			// The output of this execution will have a pretty header followed by a json string with
			// the output values in it.
			int jsonStart = rawOutput.indexOf("{");
			prettyResults = rawOutput.substring(0, jsonStart-1);
			jsonObject = new Gson().fromJson(rawOutput.substring(jsonStart), JsonObject.class);
		}
		catch(StringIndexOutOfBoundsException e){
			// Invalid data was returned from the function.
			prettyResults = "Invalid Results Returned!";
			System.err.println("Output from stderr: "+rawError);
		}
	}

	public void execute(String case_file){
		ProcessBuilder builder = new ProcessBuilder(python, command, case_file);
		//ProcessBuilder builder = new ProcessBuilder(python, command, ep);
		Process process = null;
		try {

			process = builder.start();
			IOThreadHandler outputHandler = new IOThreadHandler(process.getInputStream());
			IOThreadHandler errorHandler = new IOThreadHandler(process.getErrorStream());

			errorHandler.start();
			outputHandler.start();

			process.waitFor();
			rawOutput = outputHandler.getOutput().toString();
			rawError = errorHandler.getOutput().toString();
			parseResults();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{

		}

	}

	private static class IOThreadHandler extends Thread {
		private InputStream inputStream;
		private StringBuilder output = new StringBuilder();

		IOThreadHandler(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public void run(){
			Scanner br = null;

			try{
				br = new Scanner(new InputStreamReader(inputStream));

				String line = null;
				while (br.hasNextLine()){
					line = br.nextLine();
					output.append(line+System.getProperty("line.separator"));
				}
			}
			finally{
				br.close();
			}
		}

		public StringBuilder getOutput(){
			return output;
		}

	}

}
