package com.example.heartpumping;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HPSetHelp extends Activity {

	LinearLayout li;

	HPDatabase hpdb;
	Cursor c;
	SQLiteDatabase sql;

	ExpandableListView exlist;
	ArrayList<String> groups;
	ArrayList<String> childlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hpsethelp);
		li = (LinearLayout) findViewById(R.id.lihpsethelp);
		exlist = (ExpandableListView) findViewById(R.id.helpexpanlist);

		groups = new ArrayList<String>();
		childlist = new ArrayList<String>();

		groups.add("�αٵα� ������ �����ΰ���?");
		groups.add("������ ������ ��� �ϳ���?");
		groups.add("�� �Խù� Ȯ���� ��� �ϳ���?");
		groups.add("ģ���� ã�� ������ ��� �ϳ���?");
		groups.add("������ �ʴ� ģ���� ��� �ϳ���?");
		groups.add("�׸� ����� �����ΰ���?");
		groups.add("ID/PW�� �ؾ���ȴµ� ��� �ϳ���?");
		groups.add("ȸ��Ż�� �ϰ� ������ ��� �ϳ���?");

		childlist
				.add("�� ������ ȣ������ ��Ÿ���ϴ�. ������ ���� �� ������ ȣ������ ���� �� ������ �̴� �� ���� ������ ������ �� ���Դϴ�.");
		childlist.add("Ȩ ȭ�鿡�� �ڽ��� �������� ���� ������. �����ʰ� ��ȭ���� ������ �� �ֽ��ϴ�.");
		childlist
				.add("�Խ��� �ǿ��� �� ���� ���̴� ���� ����� �����ϰ� �ִ� ���Դϴ�. �� ���ý� ���ÿ�û��� Ȯ��, ������ �����մϴ�.");
		childlist.add("ģ�� ã�� �ǿ��� ����, ���б�, �г��� �˻��� ���ؼ� ģ���� ã�� �� �ֽ��ϴ�.");
		childlist
				.add("������ ���� �ʴ� ģ���� Ȩ ȭ�鿡�� ���� �� ���� & ������ �� �ֽ��ϴ�. �� �Ŀ� Ȩȭ�� ���ܸ�Ͽ��� ������ ������ �� �ֽ��ϴ�.");
		childlist.add("�׸� ����� �αٵα��� �ֿ� ��ư�� �� ��ư�� ������ ���⿡ �°� ������ �� �ֽ��ϴ�.");
		childlist.add("�α��� ȭ�鿡�� ID/PWã�⸦ ���Ͽ� ���̵�, ��й�ȣ�� ã�� �� �ֽ��ϴ�.");
		childlist.add("���� �� -> ������������ ȸ��Ż�� �� �� �ֽ��ϴ�.");

		exlist.setAdapter(new BaseExpandableAdapter(this, groups, childlist));

		hpdb = new HPDatabase(HPSetHelp.this);
		sql = hpdb.getReadableDatabase();
		c = sql.rawQuery("select * from personal", null);

		if (c.moveToFirst()) {
			switch (c.getInt(6)) {
			case 0:
				li.setBackgroundResource(R.drawable.help_blue);
				break;
			case 1:
				li.setBackgroundResource(R.drawable.help_pupple);
				break;
			case 2:
				li.setBackgroundResource(R.drawable.help_green);
				break;
			case 3:
				li.setBackgroundResource(R.drawable.help_red);
				break;
			case 4:
				li.setBackgroundResource(R.drawable.help_gold);
				break;
			}
		}
	}

	class BaseExpandableAdapter extends BaseExpandableListAdapter {

		private ArrayList<String> groups;
		private ArrayList<String> children;
		private Context context;

		public BaseExpandableAdapter(Context context, ArrayList<String> groups,
				ArrayList<String> children) {
			this.context = context;
			this.groups = groups;
			this.children = children;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groups.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groups.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childlist.get(groupPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.hpfindfriendlist, null);
			}
			convertView.setMinimumHeight(150);
			TextView tv = (TextView) convertView.findViewById(R.id.textView1);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
			tv.setText((String) getGroup(groupPosition));
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.hpfindfriendlist, null);
			}
			convertView.setMinimumHeight(150);
			convertView.setBackgroundColor(Color.argb(155,255, 255, 255));
			TextView tv = (TextView) convertView.findViewById(R.id.textView1);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			tv.setText((String) getChild(groupPosition, childPosition));
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return false;
		}

	}

}
