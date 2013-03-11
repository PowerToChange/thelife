package com.p2c.thelife;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;


public class GroupsDS {
	
	private ArrayList<GroupModel> m_data = new ArrayList<GroupModel>();
	
	public GroupsDS(Context context) {
		ArrayList<Integer> member_ids = new ArrayList<Integer>();
		
		member_ids.add(1);
		member_ids.add(2);
		member_ids.add(3);		
		m_data.add(new GroupModel(1, "John Martin's Group", 1, (ArrayList<Integer>)member_ids.clone()));

		member_ids.clear();
		member_ids.add(1);
		member_ids.add(2);		
		m_data.add(new GroupModel(2, "St-Marc Life Group", 1, member_ids));
	}
	
	public Collection<GroupModel> findAll() {
		return m_data;
	}

	public GroupModel findById(int groupId) {
		
		for (GroupModel m:m_data) {
			if (m.group_id == groupId) {
				return m;
			}		
		}
		
		return null;
	}

}
