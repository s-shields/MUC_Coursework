package com.example.glasgowcarparkdata;
/*
 * Author: Steven Shields
 * Student ID: s1434230
 * Student User: sshiel202
 * */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;


public class CPPullParser {

	List<CarParkClass> carParkLocations;
	
	private CarParkClass locations;
	private String data;
	
	
	public CPPullParser(){		
		carParkLocations = new ArrayList<CarParkClass>();
	}
	
	
	public List<CarParkClass> getCarParkLocations() {
		return carParkLocations;
	}
	
	public List<CarParkClass> parse(String dataURL) throws IOException{
		
		InputStream sourceListingURL = null;
		int response = -1;
		URL url = new URL(dataURL);
		URLConnection conn = url.openConnection();	
		
		// Check that the connection can be opened
    	if (!(conn instanceof HttpURLConnection))
    			throw new IOException("Not an HTTP connection");
		
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		Log.e("PullParserVariables", "variables have been made" + factory);
		
		try{
			Log.e("Before Connection", "Before connection has been attempted");
			// Open connection
    		HttpURLConnection httpConn = (HttpURLConnection) conn;
    		Log.e("HttpURLConnection Variables", "variables have been made");
    		httpConn.setAllowUserInteraction(false);
    		Log.e("HttpURLConnection Variables", "2nd variables have been made");
    		httpConn.setInstanceFollowRedirects(true);
    		Log.e("HttpURLConnection Variables", "3rd variables have been made");
    		httpConn.setRequestMethod("GET");
    		Log.e("HttpURLConnection Variables", "4th variables have been made");
    		httpConn.connect();
    		Log.e("HttpURLConnection Variables", "5th variables have been made");
    		response = httpConn.getResponseCode();
    		
    		Log.e("Connection Open", "Connection has been opened");
    		
    		// Check that connection is Ok
    		if (response == HttpURLConnection.HTTP_OK)
    		{
    			Log.e("Connection ok", "Connection is ok");
    			// Connection is Ok so open a reader 
    			sourceListingURL = httpConn.getInputStream();
    			InputStreamReader in = new InputStreamReader(sourceListingURL);
    			BufferedReader bin = new BufferedReader(in);
    			
    			Log.e("Readers Open", "reader has been opened");
    			
    			// Read in the data from the XML stream
    			bin.readLine(); // Throw away the header
    			
    			Log.e("Header binned", "The header has been thrown away");
    						
				factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				
				parser = factory.newPullParser();
				parser.setInput(bin);
				
				int eventType = parser.getEventType();
				while(eventType != XmlPullParser.END_DOCUMENT){
					String tagname = parser.getName();
					
					switch(eventType){
					case XmlPullParser.START_TAG:
						
						if(tagname.equalsIgnoreCase("situation")){
							locations = new CarParkClass();
						}
						break;
					
					case XmlPullParser.TEXT:
						
						data = parser.getText();
						break;
						
					case XmlPullParser.END_TAG:
						
						if(tagname.equalsIgnoreCase("situation")){
							carParkLocations.add(locations);
						}
						else if(tagname.equalsIgnoreCase("latitude")){
							locations.setCpMapLat(data);
						}
						else if(tagname.equalsIgnoreCase("longitude")){
							locations.setCpMapLong(data);
						}
						else if(tagname.equalsIgnoreCase("carParkIdentity")){
							locations.setCarParkIdentity(data);
							data = locations.getCarParkIdentity();
							String[] splits = data.split(":");
							locations.setCarParkName(splits[0]);
							locations.setCarParkId(splits[1]);
						}
						else if(tagname.equalsIgnoreCase("carParkOccupancy")) {
							locations.setCarParkOccupancy(data);
						}
						else if(tagname.equalsIgnoreCase("carParkStatus")){
							locations.setCarParkStatus(data);
						}
						else if(tagname.equalsIgnoreCase("occupiedSpaces")){
							locations.setOccupiedSpaces(data);
						}
						else if(tagname.equalsIgnoreCase("totalCapacity")){
							locations.setTotalCapacity(data);
						}						
						
						break;
						
					default:
						break;
					}// End of Switch
					
					eventType = parser.next();
				}
			}}catch (Exception ex)
	    	{
				throw new IOException("Error connecting");
	    	}
		
		return carParkLocations;
	}	
	
}
