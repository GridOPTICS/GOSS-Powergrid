package pnnl.goss.powergrid.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import pnnl.goss.core.DataError;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WebUtil {

	public static boolean wasError(Object obj) {
		return obj instanceof DataError;
	}

	public static synchronized JsonObject getRequestJsonBody(HttpServletRequest request){
		Gson gson = new Gson();
		return gson.fromJson(getRequestBody(request), JsonObject.class);
	}

	public static synchronized String getRequestBody(HttpServletRequest request){
		StringBuilder body = new StringBuilder();
		char[] charBuffer = new char[128];
		InputStream inputStream;
		try {
			inputStream = request.getInputStream();
			int bytesRead = -1;
    		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    		while ((bytesRead = reader.read(charBuffer)) > 0) {
    			body.append(charBuffer, 0, bytesRead);
    		}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return body.toString();
	}
}
