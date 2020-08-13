/**
MIT License
Copyright (c) 2020 Seth G. R. Herendeen
*/
package org.jsoneer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import org.apache.commons.io.FileUtils;

import java.sql.Time;

public class Jsonator {
	private static final String DEFAULT_URL = "https://api.census.gov/data/2020/dec/responserate?get=GEO_ID,NAME,CRRALL,DRRALL,CRRINT,DRRINT,RESP_DATE&for=county%20subdivision:*&in=state:09&in=county:*";

	public static void main(String[] args) {
		
		JSONArray record = null;
		
		try {
			record = getJsonFromUrl(DEFAULT_URL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getCause().getLocalizedMessage());
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println(record.toString());
		try {
			//convertAndSaveAsCSV(record,"output.csv");
			convertAndSaveAsCSV(record, "alt_output.csv");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void convertAndSaveAsCSV(JSONArray arrayToBeConverted, String filePath) throws IOException {
		var file = new File(filePath);
		
		System.out.println();
		ArrayList<String> rows = new ArrayList<String>();
		
		for (int i = 0 ; i < arrayToBeConverted.length(); i++ ) {
			
			String row =arrayToBeConverted.getJSONArray(i).toString(); 
			
			row = row.replace("[", "");
			row = row.replace("]","");
			
			rows.add(row);
		}
		
		System.out.println();

		FileUtils.writeLines(file, rows);	
		
	}
	
	public static void saveAsCSV(JSONArray array, String filePath) throws IOException {
		var file = new File(filePath);
		
		System.out.println();
		ArrayList<JSONObject> arrayOfJsonObjects = new ArrayList<JSONObject>();
		
		for (int i = 0 ; i < array.length(); i++ ) {
		//	System.out.println(i+") "+array.getJSONObject(i).toString());
			arrayOfJsonObjects.add(  array.getJSONObject(i));
			
		}
		
		System.out.println();
		ArrayList<String> strings = new ArrayList<String>();
		
		
		JSONArray jsonArray = new JSONArray(arrayOfJsonObjects);
		
		String output = jsonArray.toString();
		
		FileUtils.writeStringToFile(file, output, "utf-8");
		
	}
	
	public static JSONArray getJsonFromUrl(String urlString) throws IOException, MalformedURLException {
		URL url = new URL(urlString);
		//var sb = new StringBuffer();
		
		String retrievedString = Jsoup.connect(urlString).ignoreContentType(true).execute().body();
		
		//retrievedString = fixBrackets(retrievedString);
		
		System.out.println("json retrieved as string");
		//System.out.println(sb.toString());
		
		var jsonObj = new JSONArray(retrievedString);
		//var jsonArray = jsonObj.getJ
		return jsonObj;
	}
}
