package com.p2c.thelife;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.util.CharArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;


public class ActivitiesDS extends AbstractDS {
	
	private ArrayList<ActivityModel> m_data = new ArrayList<ActivityModel>();
	
	private Drawable m_genericIcon = null;
	private volatile boolean m_is_refreshing = false;
	private SharedPreferences m_system_settings = null;
	private String m_cache_file_name = null;

	
	public ActivitiesDS(Context context) {
		m_genericIcon = context.getResources().getDrawable(R.drawable.pray);
		
		// load activity list from cache
		try {
			// data/data/com.p2c.thelife/cache/activities.json
			m_cache_file_name = context.getCacheDir().getAbsolutePath() + "/activities.json";
System.out.println("THE CACHE FILE IS " + m_cache_file_name);
			File activitiesFile = new File(m_cache_file_name);
			if (activitiesFile.exists())
			{
				System.out.println("THE CACHE FILE EXISTS ");
				
				String jsonString = readJSONStream(new FileReader(activitiesFile));
				JSONArray jsonArray = new JSONArray(jsonString);					
				addActivities(jsonArray, m_data);
			} else {
				System.out.println("THE CACHE FILE DOES NOT EXISTS ");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();			
		}
	}
	
	// UI thread only
	public void refresh(Context context) {
		
		// find when the activities were most recently loaded
		m_system_settings = context.getSharedPreferences(TheLifeApplication.SYSTEM_PREFERENCES_FILE, Context.MODE_PRIVATE);
		long last_activity_load = m_system_settings.getLong("last_activity_load", 0);
		System.out.println("the last activity load was " + last_activity_load);
		
		// TODO: for debugging
		last_activity_load = 0;
		
		// if the activities were not refreshed recently
		if (System.currentTimeMillis() - last_activity_load > TheLifeApplication.RELOAD_ACTIVITIES_DELTA) {
				
			// if the activities are not currently being refreshed
			if (!m_is_refreshing) {  // okay to test, since this is only in the UI (main) thread
				try {
					m_is_refreshing = true;
					System.out.println("WILL NOW RUN LOAD ACTIVITIES");
					new readJSON().execute(new URL("http://thelife.ballistiq.com/activities.json"));
				} catch (MalformedURLException e) {
					e.printStackTrace();		
				} finally {
					m_is_refreshing = false;
				}
			}
			
		}
	}
	
	private class readJSON extends AsyncTask<URL, Void, String> {
		
		// background thread		
		@Override
		protected String doInBackground(URL... urls) {
			String jsonString = null;
				
			HttpURLConnection activitiesConnection = null;
			try {
				Thread.sleep(5000); // TODO: testing
				
				System.out.println("AM NOW RUNNING READJSON with" + urls[0]);				
				URL activitiesEP = urls[0];
				activitiesConnection = (HttpURLConnection)activitiesEP.openConnection();
				activitiesConnection.setConnectTimeout(TheLifeApplication.HTTP_CONNECTION_TIMEOUT);
				activitiesConnection.setConnectTimeout(TheLifeApplication.HTTP_READ_TIMEOUT);
				
				Log.e("JSON", "GOT THE RESPONSE CODE" + activitiesConnection.getResponseCode());

				if (activitiesConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {							
					jsonString = readJSONStream(new InputStreamReader(activitiesConnection.getInputStream()));
				}
			} catch (InterruptedException e) {
				;			
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();				
			} finally {
				if (activitiesConnection != null) {
					activitiesConnection.disconnect();
				}
				
			}	
			
			return jsonString;
		}
		
		// UI thread		
		@Override
		protected void onPostExecute(String jsonString) {
			
			System.out.println("HERE IN ON POST EXECUTE");
			
			try {
				JSONArray jsonArray = new JSONArray(jsonString);					
			
				// use a separate list in case of an error
				ArrayList<ActivityModel> data2 = new ArrayList<ActivityModel>();
				addActivities(jsonArray, data2);
				
				// no error, so use the new data
				m_data = data2;
				notifyDataStoreListeners(); // tell listeners that the data has changed
				
				writeJSONCache(jsonString);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			// release lock
			m_is_refreshing = false;
			
			// remember the timestamp of this successful load
			SharedPreferences.Editor system_settings_editor = m_system_settings.edit();
			system_settings_editor.putLong("last_activity_load", System.currentTimeMillis());
			system_settings_editor.commit();
		}
		
	}	
	
	
	// InputStreamReader is buffered
	private String readJSONStream(InputStreamReader is) {

		String jsonString = null;
		
		try {
			char buffer[] = new char[1024];
			CharArrayBuffer jsonBuffer = new CharArrayBuffer(1024);			

			int numBytesRead = is.read(buffer);
			while (numBytesRead != -1) {
				jsonBuffer.append(buffer, 0, numBytesRead);
				numBytesRead = is.read(buffer);
			}
			
			jsonString = new String(jsonBuffer.buffer());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonString;
	}
	
	private void addActivities(JSONArray jsonArray, ArrayList<ActivityModel> list) {
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				Log.e("JSON", "ADD ANOTHER JSON OBJECT WITH TITLE " + json.optString("title", ""));
				
				// create the activity
				ActivityModel activity = ActivityModel.fromJSON(json, m_genericIcon);
				list.add(activity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void writeJSONCache(String jsonString) {
		
		FileWriter fileWriter = null;
		try {
			File activitiesFile = new File(m_cache_file_name);
			fileWriter = new FileWriter(activitiesFile); // buffered
			fileWriter.write(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try { fileWriter.close(); } catch (IOException e) { }
			}
		}
	}
	
	
	/**
	 * @return all activity model objects in the database.
	 */
	public Collection<ActivityModel> findAll() {
		return m_data;
	}

	/**
	 * @param activity_id
	 * @return the activity model object with the given id
	 */
	public ActivityModel findById(int activity_id) {
		
		for (ActivityModel m:m_data) {
			if (m.activity_id == activity_id) {
				return m;
			}		
		}
		
		return null;
	}
	
	/**
	 * @param threshold
	 * @return all activity model objects applicable to the given threshold
	 */
	public Collection<ActivityModel> findByThreshold(FriendModel.Threshold threshold) {
		ArrayList<ActivityModel> activities = new ArrayList<ActivityModel>();
		
		for (ActivityModel m:m_data) {
			if (m.is_applicable(threshold)) {
				activities.add(m);
			}		
		}
		
		return activities;
	}

}
