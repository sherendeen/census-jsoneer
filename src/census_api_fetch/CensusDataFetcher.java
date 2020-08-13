/**
Copyright 2020 Seth G. R. Herendeen
Licensed MIT
*/
package census_api_fetch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CensusDataFetcher {
	final static String KEY = "INSERT_YOUR_KEY_HERE";
	final static String DEFAULT_URL = "https://api.census.gov/data/2020/dec/responserate?get=GEO_ID,NAME,CRRALL,DRRALL,CRRINT,DRRINT,RESP_DATE&for=county%20subdivision:*&in=state:36%20county:117&key=" + KEY;
	final static String PLACES_URL = "https://api.census.gov/data/2020/dec/responserate?get=GEO_ID,NAME,CRRALL,DRRALL,CRRINT,DRRINT,RESP_DATE&for=place:*&in=state:36&key=" + KEY;
	public static final String OUTPUT_FILE_PATH = System.getProperty("user.dir") + File.separator  + "the__absolute_output.csv";
	
	
	public static void main(String[] args) {
		
		String line = Helper.getLineOfCharacter('*', 12);
		
		System.out.println(line);
		System.out.println("This product uses the Census Bureau data API "
				+ "but is not endorsed or certified by the Census Bureau.");
		System.out.println(line);
		
		JSONArray jsonArray = null;
		
		String url = "", outputPath = "";
		boolean removeBrackets = true;
		
		switch(args.length) {
		case 5:
			if (args[5].equals("places") || args[5].equals("p") ) {
				System.out.println("Attempting to use places URL.");
				url = PLACES_URL;
			} else {
				System.out.println("Invalid option for 5th argument ``" + 
						args[5].toString() + ".`` Ignoring...");
			}
		case 4:
			System.out.println("Running in \"Remove Brackets\" mode");
			removeBrackets = Boolean.getBoolean( args[3] );
		case 3:
			System.out.println("Attempting to use custom path");
			outputPath = args[2];
		case 2:
			System.out.println("Attempting to use custom URL");
			url = args[1];
			outputPath = OUTPUT_FILE_PATH;
			break;
			default:
				Scanner scn = new Scanner(System.in);
				System.out.println("Do you want to use the places URL instead of the default? y/n");
				
				char opt = scn.nextLine().charAt(0);
				
				if (opt != 'y') {
					url = DEFAULT_URL;
				} else {
					url = PLACES_URL;
				}
				
				outputPath = OUTPUT_FILE_PATH;
				break; 
		}
		
		try {
			jsonArray = getJsonArrayFromURL(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(jsonArray.toString());
		
		try {
			convertAndSaveAsCSV(jsonArray, outputPath, removeBrackets);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Takes a JSON array and converts it to a form that is writeable to comma separated values formated file.
	 * @param arrayToBeConverted the jsonarray
	 * @param filePath where you want it to be saved
	 * @param getRidOfSquareBrackets do we want to get rid of some square brackets? you decide
	 * @throws IOException since there is a chance the file path does not exist or is read-only
	 */
	private static void convertAndSaveAsCSV(JSONArray arrayToBeConverted, 
			String filePath, 
			boolean getRidOfSquareBrackets) throws IOException {
		var file = new File(filePath);
		
		System.out.println();
		ArrayList<String> rows = new ArrayList<String>();
		
		for (int i = 0 ; i < arrayToBeConverted.length(); i++ ) {
			
			String row = arrayToBeConverted.getJSONArray(i).toString(); 
			
			if (getRidOfSquareBrackets) {
				row = row.replace("[", "");
				row = row.replace("]","");
			}
			
			rows.add(row);
		}
		
		System.out.println();

		FileUtils.writeLines(file, rows);	
		
	}

	/**
	 * gets the json as a string from the specified URL as expressed as string
	 * @param urlString the URL but as a string
	 * @return a fully qualified json array object
	 * @throws MalformedURLException in case the URL is malformed
	 * @throws IOException in case of 404
	 */
	private static JSONArray getJsonArrayFromURL(String urlString) throws MalformedURLException, IOException {
		// Use jsoup to download the string content of the json file
		String retrievedString = Jsoup.connect(urlString).ignoreContentType(true).execute().body();
		
		System.out.println("json retrieved as string");
		//put the string into the json array
		var jsonObj = new JSONArray(retrievedString);
		
		return jsonObj;
	}

	
	
}
