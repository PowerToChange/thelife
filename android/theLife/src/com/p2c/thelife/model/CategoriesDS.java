package com.p2c.thelife.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.p2c.thelife.TheLifeConfiguration;


public class CategoriesDS extends AbstractDS<GroupModel> {
	
	public CategoriesDS(Context context) {
		
		super(
				context, 
				"CategoriesDS", 
				"categories.json",
				"refresh_categories_timestamp_key",
				TheLifeConfiguration.SERVER_URL + "categories.json",
				"refresh_categories_delta_key",
				TheLifeConfiguration.REFRESH_CATEGORIES_DELTA
			);		
		
	}
	
	/**
	 * Needed by the abstract superclass.
	 */
	protected GroupModel createFromJSON(JSONObject json, boolean useServer) throws JSONException {
		return GroupModel.fromJSON(json, useServer);
	}	
}
