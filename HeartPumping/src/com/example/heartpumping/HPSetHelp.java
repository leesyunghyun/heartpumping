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

		groups.add("두근두근 지수는 무엇인가요?");
		groups.add("프포필 수정은 어떻게 하나요?");
		groups.add("내 게시물 확인은 어떻게 하나요?");
		groups.add("친구를 찾고 싶은데 어떻게 하나요?");
		groups.add("원하지 않는 친구는 어떻게 하나요?");
		groups.add("테마 기능이 무엇인가요?");
		groups.add("ID/PW를 잊어버렸는데 어떻게 하나요?");
		groups.add("회원탈퇴를 하고 싶은데 어떻게 하나요?");

		childlist
				.add("이 지수는 호감도를 나타냅니다. 만남을 가진 후 서로의 호감도를 평가할 수 있으며 이는 이 후의 만남에 영향을 줄 것입니다.");
		childlist.add("홈 화면에서 자신의 프로필을 눌러 보세요. 프로필과 대화명을 수정할 수 있습니다.");
		childlist
				.add("게시판 탭에서 맨 위에 보이는 글이 당신이 참여하고 있는 글입니다. 글 선택시 미팅요청목록 확인, 수정이 가능합니다.");
		childlist.add("친구 찾기 탭에서 성별, 대학교, 닉네임 검색을 통해서 친구를 찾을 수 있습니다.");
		childlist
				.add("마음에 들지 않는 친구는 홈 화면에서 선택 후 삭제 & 차단할 수 있습니다. 이 후에 홈화면 차단목록에서 차단을 해제할 수 있습니다.");
		childlist.add("테마 기능은 두근두근의 주요 버튼과 탭 버튼의 색상을 취향에 맞게 변경할 수 있습니다.");
		childlist.add("로그인 화면에서 ID/PW찾기를 통하여 아이디, 비밀번호를 찾을 수 있습니다.");
		childlist.add("설정 탭 -> 개인정보에서 회원탈퇴를 할 수 있습니다.");

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
