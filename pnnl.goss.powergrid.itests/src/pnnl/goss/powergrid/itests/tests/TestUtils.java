package pnnl.goss.powergrid.itests.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.osgi.framework.BundleContext;

public class TestUtils {

	public static void setupDatabase(BundleContext context, Connection conn){
		//TODO
//		URL url = context.getBundle().getResource("/resources/setup.sql");
//
//		try(BufferedReader in = new BufferedReader(
//				new InputStreamReader(url.openStream()))){
//
//			String inputLine;
//			StringBuilder sb = new StringBuilder();
//	        while ((inputLine = in.readLine()) != null){
//	            System.out.println(inputLine);
//	            sb.append(inputLine);
//	        }
//
//	        in.close();
//
//	        try(Statement stmt = conn.createStatement()){
//	        	stmt.execute(sb.toString());
//	        }
//	        catch(SQLException e1){
//	        	System.out.println("sql exception: ");
//	        	e1.printStackTrace();
//	        }
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


	}

	public static void tearDownDatabase(){

	}

}
