package com.example.heartpumping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HPData {
	private Connection con;
	private Statement stmt;
	private Statement stmt2;
	private Statement stmt3;
	private Statement stmt4;
	private ResultSet rs = null;
	private ResultSet rs2 = null;
	private ResultSet rs3 = null;
	private ResultSet rs4 = null;

	public void Dataopen() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager
					.getConnection("jdbc:mysql://localhost/heartpumping?user=root&password=0000");
			stmt = con.createStatement();
			stmt2 = con.createStatement();
			stmt3 = con.createStatement();
			stmt4 = con.createStatement();
		} catch (Exception e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}

	public HPData() {
		Dataopen();
	}

	public void InsertMember(String id, String pw, String nic, String sex,
			String univ_1, String univ_2, String phone, String studentnum) {
		String str = "Insert into heart_pumping(id,password,nicname,sex,univ_1,univ_2,phone,student) values('"
				+ id
				+ "','"
				+ pw
				+ "','"
				+ nic
				+ "',"
				+ Integer.parseInt(sex)
				+ ",'"
				+ univ_1
				+ "','"
				+ univ_2
				+ "','"
				+ phone
				+ "','"
				+ studentnum + "');";

		try {
			stmt.execute(str);
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}

	public void UpdateMember(String id, String pw, String nic, String sex,
			String univ_1, String univ_2, String phone, String studentnum) {
		String str = "Update heart_pumping set password = '" + pw
				+ "', nicname ='" + nic + "', sex =" + Integer.parseInt(sex)
				+ ", univ_1 ='" + univ_1 + "', univ_2='" + univ_2
				+ "',student ='" + studentnum + "' where id ='" + id + "';";

		try {
			stmt.executeUpdate(str);
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}

	public void UpdateProfile(String id, String message) {
		String str = "Update heart_pumping set profile = '" + message
				+ "' where id ='" + id + "';";
		try {
			stmt.executeUpdate(str);
		} catch (SQLException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}

	public boolean LoginCheck(String id, String pw) {
		String str = "Select * from heart_pumping where id='" + id + "';";
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			if (rs.getString("password").equals(pw)) {

				return true;

			} else {
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean PhoneCheck(String phone) {
		String str = "Select phone from heart_pumping where phone='" + phone
				+ "';";
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			if (rs.getString("phone").equals(phone)) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			return true;
		}
	}

	public boolean IDCheck(String id) {
		String str = "Select * from heart_pumping where id='" + id + "';";
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			if (rs.getString("id").equals(id)) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			return true;
		}
	}

	public String FindID(String nicname, String phone) {
		String str = "Select id from heart_pumping where nicname='" + nicname
				+ "' and phone = '" + phone + "';";
		String str2 = "falsefindid";
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			str2 = rs.getString("id");
			return str2;
		} catch (SQLException e) {
			return str2;
		}
	}

	public String FindPW(String id, String phone) {
		String str = "Select password from heart_pumping where id='" + id
				+ "' and phone = '" + phone + "';";
		String str2 = "falsefindpw";
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			str2 = rs.getString("password");
			return str2;
		} catch (SQLException e) {
			return str2;
		}
	}

	public String FindFriend(String nicname, String id, String sex,
			String univ_1) {
		if (!sex.equals("-1")) {
			if (!univ_1.equals("-1")) {
				String str = "Select * from heart_pumping where nicname like '%"
						+ nicname
						+ "%' and univ_1 like '%"
						+ univ_1
						+ "%' and sex = "
						+ Integer.parseInt(sex)
						+ " and searchnic = 1;";
				String str2 = "";
				String str3 = "falsefriend";
				try {
					rs = stmt.executeQuery(str);
					rs.first();
					if (rs.getRow() == 0)
						return str3;
					for (int i = 0; i < rs.getRow(); i++) {
						if (!id.equals(rs.getString("id"))) {

							str = "Select * from heart_friends where send ='"
									+ id + "' and receiver ='"
									+ rs.getString("id") + "';";

							rs2 = stmt2.executeQuery(str);
							rs2.first();
							if (rs2.getRow() == 0) {
								str2 += rs.getString("nicname") + "/";
							}
						}
						rs.next();
					}
					if (str2.equals("")) {
						str2 = "falsefriend";
					}
					return str2;
				} catch (SQLException e) {
					return str3;
				}
			} else {
				String str = "Select * from heart_pumping where nicname like '%"
						+ nicname
						+ "%' and sex = "
						+ Integer.parseInt(sex)
						+ " and searchnic = 1;";
				String str2 = "";
				String str3 = "falsefriend";
				try {
					rs = stmt.executeQuery(str);
					rs.first();
					if (rs.getRow() == 0)
						return str3;
					for (int i = 0; i < rs.getRow(); i++) {
						if (!id.equals(rs.getString("id"))) {

							str = "Select * from heart_friends where send ='"
									+ id + "' and receiver ='"
									+ rs.getString("id") + "';";

							rs2 = stmt2.executeQuery(str);
							rs2.first();
							if (rs2.getRow() == 0) {
								str2 += rs.getString("nicname") + "/";
							}
						}
						rs.next();
					}
					if (str2.equals("")) {
						str2 = "falsefriend";
					}
					return str2;
				} catch (SQLException e) {
					return str3;
				}
			}
		} else {
			if (!univ_1.equals("-1")) {
				String str = "Select * from heart_pumping where nicname like '%"
						+ nicname
						+ "%' and univ_1 like '%"
						+ univ_1
						+ "%' and searchnic = 1;";
				String str2 = "";
				String str3 = "falsefriend";
				try {
					rs = stmt.executeQuery(str);
					rs.first();
					if (rs.getRow() == 0)
						return str3;
					for (int i = 0; i < rs.getRow(); i++) {
						if (!id.equals(rs.getString("id"))) {

							str = "Select * from heart_friends where send ='"
									+ id + "' and receiver ='"
									+ rs.getString("id") + "';";

							rs2 = stmt2.executeQuery(str);
							rs2.first();
							if (rs2.getRow() == 0) {
								str2 += rs.getString("nicname") + "/";
							}
						}
						rs.next();
					}
					if (str2.equals("")) {
						str2 = "falsefriend";
					}
					return str2;
				} catch (SQLException e) {
					return str3;
				}
			} else {
				String str = "Select * from heart_pumping where nicname like '%"
						+ nicname + "%' and searchnic = 1;";
				String str2 = "";
				String str3 = "falsefriend";
				try {
					rs = stmt.executeQuery(str);
					rs.first();
					if (rs.getRow() == 0)
						return str3;
					for (int i = 0; i < rs.getRow(); i++) {
						if (!id.equals(rs.getString("id"))) {

							str = "Select * from heart_friends where send ='"
									+ id + "' and receiver ='"
									+ rs.getString("id") + "';";

							rs2 = stmt2.executeQuery(str);
							rs2.first();
							if (rs2.getRow() == 0) {
								str2 += rs.getString("nicname") + "/";
							}
						}
						rs.next();
					}
					if (str2.equals("")) {
						str2 = "falsefriend";
					}
					return str2;
				} catch (SQLException e) {
					return str3;
				}
			}
		}

	}

	public String FindData(String FindData, String InputType, String InputData,
			String id, String sex, String univ_1) {
		if (!sex.equals("-1")) {
			if (!univ_1.equals("-1")) {
				String str = "Select * from heart_pumping where " + InputType
						+ " like '%" + InputData + "%' and univ_1 like '%"
						+ univ_1 + "%' and sex = " + Integer.parseInt(sex)
						+ " and searchnic = 1;";
				String str2 = "-1";
				try {
					rs = stmt.executeQuery(str);
					rs.first();
					if (rs.getRow() == 0)
						return str2;
					str2 = "";
					for (int i = 0; i < rs.getRow(); i++) {
						if (!id.equals(rs.getString("id"))) {
							str = "Select * from heart_friends where send ='"
									+ id + "' and receiver ='"
									+ rs.getString("id") + "';";

							rs2 = stmt2.executeQuery(str);
							rs2.first();
							if (rs2.getRow() == 0) {
								str2 += rs.getString(FindData) + "/";
							}
						}
						rs.next();
					}
					if (str2.equals("")) {
						str2 = "-1";
					}
					return str2;
				} catch (SQLException e) {
					return str2;
				}
			} else {
				String str = "Select * from heart_pumping where " + InputType
						+ " like '%" + InputData + "%' and sex = "
						+ Integer.parseInt(sex) + " and searchnic = 1;";
				String str2 = "-1";
				try {
					rs = stmt.executeQuery(str);
					rs.first();
					if (rs.getRow() == 0)
						return str2;
					str2 = "";
					for (int i = 0; i < rs.getRow(); i++) {
						if (!id.equals(rs.getString("id"))) {
							str = "Select * from heart_friends where send ='"
									+ id + "' and receiver ='"
									+ rs.getString("id") + "';";

							rs2 = stmt2.executeQuery(str);
							rs2.first();
							if (rs2.getRow() == 0) {
								str2 += rs.getString(FindData) + "/";
							}
						}
						rs.next();
					}
					if (str2.equals("")) {
						str2 = "-1";
					}
					return str2;
				} catch (SQLException e) {
					return str2;
				}
			}

		} else {
			if (!univ_1.equals("-1")) {
				String str = "Select * from heart_pumping where " + InputType
						+ " like '%" + InputData + "%' and univ_1 like '%"
						+ univ_1 + "%' and searchnic = 1;";
				String str2 = "-1";
				try {
					rs = stmt.executeQuery(str);
					rs.first();
					if (rs.getRow() == 0)
						return str2;
					str2 = "";
					for (int i = 0; i < rs.getRow(); i++) {
						if (!id.equals(rs.getString("id"))) {
							str = "Select * from heart_friends where send ='"
									+ id + "' and receiver ='"
									+ rs.getString("id") + "';";

							rs2 = stmt2.executeQuery(str);
							rs2.first();
							if (rs2.getRow() == 0) {
								str2 += rs.getString(FindData) + "/";
							}
						}
						rs.next();
					}
					if (str2.equals("")) {
						str2 = "-1";
					}
					return str2;
				} catch (SQLException e) {
					return str2;
				}
			} else {
				String str = "Select * from heart_pumping where " + InputType
						+ " like '%" + InputData + "%' and searchnic = 1;";
				String str2 = "-1";
				try {
					rs = stmt.executeQuery(str);
					rs.first();
					if (rs.getRow() == 0)
						return str2;
					str2 = "";
					for (int i = 0; i < rs.getRow(); i++) {
						if (!id.equals(rs.getString("id"))) {
							str = "Select * from heart_friends where send ='"
									+ id + "' and receiver ='"
									+ rs.getString("id") + "';";

							rs2 = stmt2.executeQuery(str);
							rs2.first();
							if (rs2.getRow() == 0) {
								str2 += rs.getString(FindData) + "/";
							}
						}
						rs.next();
					}
					if (str2.equals("")) {
						str2 = "-1";
					}
					return str2;
				} catch (SQLException e) {
					return str2;
				}
			}
		}
	}

	public String SetFriend(String Send, String Receiver, String State) {
		if (State.equals("요청")) {
			String str = "Select state from heart_friends where send = '"
					+ Send + "' and receiver = '" + Receiver + "';";
			String str2 = "";
			String str3 = "";
			try {
				rs = stmt.executeQuery(str);
				rs.first();
				str2 = rs.getString("state");
				if (str2.equals("0")) {
					// 이미 요청한거
					str3 = "frienderror-1";
					return str3;
				} else if (str2.equals("1")) {
					str3 = "frienderror-2";
					return str3;
					// 친구상태인거
				} else if (str2.equals("2")) {
					str3 = "frienderror-3";
					return str3;
					// 내가 차단한거
				}
			} catch (SQLException e) {
				System.out.print("내가안했다.");
				str = "Select state from heart_friends where send = '"
						+ Receiver + "' and receiver = '" + Send + "';";
				try {
					rs2 = stmt2.executeQuery(str);
					rs2.first();
					str2 = rs2.getString("state");
					if (str2.equals("0")) {
						// 상대방은 요청 나는 아직 없음.
						str = "update heart_friends set state = '1' where send ='"
								+ Receiver + "' and receiver ='" + Send + "';";
						try {
							stmt.executeUpdate(str);
							str = "Insert into heart_friends values('" + Send
									+ "', '" + Receiver + "', '1');";
							stmt.executeUpdate(str);
							str3 = "frienderror-4";
							return str3;
						} catch (SQLException e4) {
							str3 = "frienderror-5";
							return str3;
						}
					} else if (str2.equals("1")) {
						// 일어날수 없는 일
						System.out.println("일어날 수 없는 일");
						str3 = "frienderror-6";
						return str3;
					} else if (str2.equals("2")) {
						// 내가 요청했지만 상대방이 이미 차단한 상태
						// 그러면 나는 요청한 상태로 유지 상대방은 안보임.
						str = "Insert into heart_friends values('" + Send
								+ "', '" + Receiver + "', '0');";
						try {
							stmt.execute(str);
							str3 = "frienderror-7";
						} catch (SQLException e2) {
							// TODO 자동 생성된 catch 블록
							// insert 오류
							str3 = "frienderror-8";
						}
						return str3;
						// 내가 차단한거
					}
				} catch (SQLException e1) {
					System.out.print("상대방도안했다.");
					str = "Insert into heart_friends values('" + Send + "', '"
							+ Receiver + "', '0');";
					try {
						stmt.execute(str);
						str3 = "frienderror-9";
					} catch (SQLException e2) {
						// TODO 자동 생성된 catch 블록
						// insert 오류
						str3 = "frienderror-10";
					}
					return str3;
				}
			}
		} else if (State.equals("친구")) {
			// 친구요청을 승낙했을때 들어옴
			String str = "Select state from heart_friends where send = '"
					+ Receiver + "' and receiver + '" + Send + "';";
			String str2 = "";
			String str3 = "";
			String str4 = "";
			try {
				rs = stmt.executeQuery(str);
				rs.first();
				str2 = rs.getString("state");
				if (str2.equals("0")) {
					str = "Select state from heart_friends where send = '"
							+ Send + "' and receiver + '" + Receiver + "';";

					try {
						rs2 = stmt2.executeQuery(str);
						rs2.first();
						str4 = rs2.getString("state");
						if (str4.equals("0")) {
							str = "update heart_friends set state = '1' where (send ='"
									+ Send
									+ "' and receiver ='"
									+ Receiver
									+ "') or (send ='"
									+ Receiver
									+ "' and receiver ='" + Send + "');";
							stmt.executeUpdate(str);
							str3 = "frienderror-11";
						} else if (str4.equals("1")) {
							str = "update heart_friends set state = '1' where (send ='"
									+ Send
									+ "' and receiver ='"
									+ Receiver
									+ "') or (send ='"
									+ Receiver
									+ "' and receiver ='" + Send + "');";
							stmt.executeUpdate(str);
							str3 = "frienderror-12";
						} else if (str4.equals("2")) {
							// 차단한 상태에서 요청을 받아주는 거니까 그거에 맞는 메시지 띄우는거 구현해야함.
							str = "update heart_friends set state = '1' where (send ='"
									+ Send
									+ "' and receiver ='"
									+ Receiver
									+ "') or (send ='"
									+ Receiver
									+ "' and receiver ='" + Send + "');";
							stmt.executeUpdate(str);
							str3 = "frienderror-13";
						}
						return str3;
					} catch (SQLException e2) {
						// TODO 자동 생성된 catch 블록
						// 내가 없을 경우 인설트 시키고 업데이트해야함.
						str = "Insert into heart_friends values('" + Send
								+ "', '" + Receiver + "', '1');";
						stmt.execute(str);
						str = "update heart_friends set state = '1' where (send ='"
								+ Send
								+ "' and receiver ='"
								+ Receiver
								+ "') or (send ='"
								+ Receiver
								+ "' and receiver ='" + Send + "');";
						stmt.executeUpdate(str);
						str3 = "frienderror-14";
						return str3;
					}
				} else {
					// 상대방이 나를 차단했거나 이미 친구인 경우
					str3 = "frienderror-15";
					return str3;
				}
			} catch (SQLException e) {
				// 상대방이 요청했다가 삭제한 경우
				str3 = "frienderror-16";
				return str3;
			}
		} else if (State.equals("차단")) {
			String str = "Select state from heart_friends where send = '"
					+ Send + "' and receiver = '" + Receiver + "';";
			String str2 = "";
			String str3 = "";
			String str4 = "";
			try {
				rs = stmt.executeQuery(str);
				rs.first();
				str2 = rs.getString("state");
				if (str2.equals("0")) {
					// 내 상태가 대기중
					str = "Select state from heart_friends where send = '"
							+ Receiver + "' and receiver = '" + Send + "';";
					try {
						rs2 = stmt2.executeQuery(str);
						rs2.first();
						str3 = rs2.getString("state");
						if (str3.equals("0")) {
							str4 = "frienderror-17";
						} else if (str3.equals("1")) {
							str = "delete from heart_friends where send = '"
									+ Receiver + "' and receiver = '" + Send
									+ "';";
							stmt.execute(str);
							str4 = "frienderror-18";
						} else if (str3.equals("2")) {
							str4 = "frienderror-19";
						}
					} catch (SQLException e1) {
						str4 = "frienderror-20";
					}
				} else if (str2.equals("1")) {
					// 내 상태가 친구
					str = "delete from heart_friends where send = '" + Receiver
							+ "' and receiver = '" + Send + "';";
					stmt.execute(str);
					str4 = "frienderror-21";
				} else if (str2.equals("2")) {
					// 내 상태가 이미 차단
					str = "Select state from heart_friends where send = '"
							+ Receiver + "' and receiver = '" + Send + "';";
					try {
						rs2 = stmt2.executeQuery(str);
						rs2.first();
						str3 = rs2.getString("state");
						if (str3.equals("1")) {
							str = "delete from heart_friends where send = '"
									+ Receiver + "' and receiver = '" + Send
									+ "';";
							stmt.execute(str);
							str4 = "frienderror-22";
						} else {
							str4 = "frienderror-23";
						}
					} catch (SQLException e1) {
						str4 = "frienderror-24";
					}
				}
				str = "update heart_friends set state = '2' where send ='"
						+ Send + "' and receiver ='" + Receiver + "';";
				stmt.executeUpdate(str);
				return str4;
			} catch (SQLException e) {
				// 처음으로 차단하는 경우 그니까 테이블이 안만들어져 있는 경우 인설트 해야함.
				str = "Insert into heart_friends values('" + Send + "', '"
						+ Receiver + "', '2');";
				try {
					stmt.execute(str);
					str3 = "frienderror-25";
				} catch (SQLException e1) {
					// TODO 자동 생성된 catch 블록
					e1.printStackTrace();
					str3 = "frienderror-26";
				}
			}
			return str3;
		} else if (State.equals("삭제")) {
			String str = "Select state from heart_friends where send = '"
					+ Receiver + "' and receiver = '" + Send + "';";
			String str2 = "";
			String str3 = "";
			try {
				rs = stmt.executeQuery(str);
				rs.first();
				str2 = rs.getString("state");
				if (str2.equals("0")) {
					str = "delete from heart_friends where send = '" + Send
							+ "' and receiver = '" + Receiver + "';";
					try {
						stmt2.execute(str);
						str3 = "frienderror-27";
					} catch (SQLException e) {
						str3 = "frienderror-28";
					}
					return str3;
				} else if (str2.equals("1")) {
					str = "delete from heart_friends where (send = '"
							+ Receiver + "' and receiver = '" + Send
							+ "') or (send = '" + Send + "' and receiver = '"
							+ Receiver + "');";
					try {
						stmt2.execute(str);
						str3 = "frienderror-29";
					} catch (SQLException e) {
						str3 = "frienderror-30";
					}
					return str3;
				} else if (str2.equals("2")) {
					str = "delete from heart_friends where send = '" + Send
							+ "' and receiver = '" + Receiver + "';";
					try {
						stmt2.execute(str);
						str3 = "frienderror-31";
					} catch (SQLException e) {
						str3 = "frienderror-32";
					}
					return str3;
				}
			} catch (SQLException e1) {
				// TODO 자동 생성된 catch 블록
				str = "delete from heart_friends where send = '" + Send
						+ "' and receiver = '" + Receiver + "';";
				try {
					stmt2.execute(str);
					str3 = "frienderror-33";
				} catch (SQLException e) {
					str3 = "frienderror-34";
				}
				return str3;
			}
		} else if (State.equals("삭제2")) {
			String str = "Select state from heart_friends where send = '"
					+ Send + "' and receiver = '" + Receiver + "';";
			String str2 = "";
			String str3 = "";
			try {
				rs = stmt.executeQuery(str);
				rs.first();
				str2 = rs.getString("state");
				if (str2.equals("0")) {
					str = "delete from heart_friends where send = '" + Receiver
							+ "' and receiver = '" + Send + "';";
					try {
						stmt2.execute(str);
						str3 = "frienderror-27";
					} catch (SQLException e) {
						str3 = "frienderror-28";
					}
					return str3;
				} else if (str2.equals("1")) {
					str = "delete from heart_friends where (send = '" + Send
							+ "' and receiver = '" + Receiver
							+ "') or (send = '" + Receiver
							+ "' and receiver = '" + Send + "');";
					try {
						stmt2.execute(str);
						str3 = "frienderror-29";
					} catch (SQLException e) {
						str3 = "frienderror-30";
					}
					return str3;
				} else if (str2.equals("2")) {
					str = "delete from heart_friends where send = '" + Receiver
							+ "' and receiver = '" + Send + "';";
					try {
						stmt2.execute(str);
						str3 = "frienderror-31";
					} catch (SQLException e) {
						str3 = "frienderror-32";
					}
					return str3;
				}
			} catch (SQLException e1) {
				// TODO 자동 생성된 catch 블록
				str = "delete from heart_friends where send = '" + Receiver
						+ "' and receiver = '" + Send + "';";
				try {
					stmt2.execute(str);
					str3 = "frienderror-33";
				} catch (SQLException e) {
					str3 = "frienderror-34";
				}
				return str3;
			}
		}
		return "frienderror-35";
	}

	public String FindResultMy(String id, String Input) {
		String str = "select " + Input + " from heart_pumping where id = '"
				+ id + "';";
		String str2 = "falseresultmy";

		try {
			rs = stmt.executeQuery(str);
			rs.first();
			if (rs.getRow() == 0) {
				return str2;
			}
			str2 = rs.getString(Input);

		} catch (SQLException e) {
			return str2;
		}
		return str2;
	}

	public String FindResult(String Send, String State, String Input) {
		String str = "select receiver from heart_friends where send = '" + Send
				+ "' and state ='" + State + "';";
		String str2 = "falseresult";

		try {
			rs = stmt.executeQuery(str);
			rs.first();
			if (rs.getRow() == 0) {
				return str2;
			}
			str2 = "";

			for (int i = 0; i < rs.getRow(); i++) {
				str = "select " + Input + " from heart_pumping where id='"
						+ rs.getString("receiver") + "';";
				rs2 = stmt2.executeQuery(str);
				rs2.first();
				if (rs2.getString(Input).equals("")) {
					str2 += " " + "/";
					rs.next();
					continue;
				}
				str2 += rs2.getString(Input) + "/";
				rs.next();
			}
		} catch (SQLException e) {
			return str2;
		}

		return str2;
	}

	public String FindReverseResult(String Send, String State, String Input) {
		String str = "select send from heart_friends where receiver = '" + Send
				+ "' and state ='" + State + "';";
		String str2 = "falseresult";

		try {
			rs = stmt.executeQuery(str);
			rs.first();
			if (rs.getRow() == 0) {
				return str2;
			}
			str2 = "";

			for (int i = 0; i < rs.getRow(); i++) {
				str = "select * from heart_friends where send = '" + Send
						+ "' and receiver = '" + rs.getString("send") + "';";

				rs2 = stmt2.executeQuery(str);
				rs2.first();

				if (rs2.getRow() == 0) {
					str = "select " + Input + " from heart_pumping where id='"
							+ rs.getString("send") + "';";
					rs3 = stmt3.executeQuery(str);
					rs3.first();
					if (rs3.getString(Input).equals("")) {
						str2 += " " + "/";
						rs.next();
						continue;
					}
					str2 += rs3.getString(Input) + "/";

				}
				rs.next();
			}
			if (str2.equals("")) {
				str2 = "falseresult";
			}
		} catch (SQLException e) {
			return str2;
		}

		return str2;
	}

	public String MakeBoard(String Written, String Subject, String Content,
			String MeetZone, String PeoPleCount, String PeoPle,
			String OkPeople, String NoPeople, String avrpoint) {
		String str = "select * from heart_pumping where id= '" + Written + "';";
		String str2 = "falsemakeboard";
		String str3 = "";
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			switch (rs.getInt("boardstate")) {
			case 0:
				str = "Insert into heart_board(written, writtensex,subject, content, meetzone, peoplecount, people,okpeople,nopeople,avrpoint) values('"
						+ Written
						+ "', "
						+ rs.getInt("sex")
						+ ", '"
						+ Subject
						+ "', '"
						+ Content
						+ "', '"
						+ MeetZone
						+ "', "
						+ Integer.parseInt(PeoPleCount)
						+ ", '"
						+ PeoPle
						+ "', '"
						+ OkPeople
						+ "', '"
						+ NoPeople
						+ "', "
						+ Integer.parseInt(avrpoint) + ");";
				stmt2.execute(str);
				str = "update heart_pumping set boardstate = 1 where id='"
						+ Written + "';";
				stmt2.executeUpdate(str);
				str2 = "truemakeboard";
				break;
			case 1:
				return str2;
			case 2:
				return str2;
			}
			if (PeoPleCount.equals("1")) {
				str = "update heart_board set view_able=1 where written='"
						+ Written + "';";
				stmt2.executeUpdate(str);
				str2 = "truemakeboard";
			}
		} catch (Exception e) {
			return str2;
		}

		return str2;
	}

	public String ModifyBoard(String Written, String Subject, String Content,
			String MeetZone) {
		String str = "Update heart_board set subject = '" + Subject
				+ "', content ='" + Content + "', meetzone ='" + MeetZone
				+ "' where written ='" + Written + "';";
		String str2 = "falsemakeboard";
		try {
			stmt.executeUpdate(str);
			str2 = "truemakeboard";
		} catch (Exception e) {
			return str2;
		}

		return str2;
	}

	public String ReqBoard(String ChkStr, String ReqStr, String state) {
		String str = "select * from heart_board where view_able = 0";
		String str2[];
		String str3 = "";
		String str4[];
		String str5[];
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			str = "select nicname from heart_pumping where id='"
					+ rs.getString("written") + "'";
			rs2 = stmt2.executeQuery(str);
			rs2.first();
			for (int i = 0; i < rs.getRow(); i++) {
				str2 = rs.getString("people").split("/");
				str4 = rs.getString("okpeople").split("/");
				str5 = rs.getString("nopeople").split("/");

				for (int j = 0; j < str2.length; j++) {
					if (str2[j].equals(ChkStr)
							&& !str2[j].equals(rs.getString("written"))) {
						for (int k = 0; k < str4.length; k++) {
							if (str4.length == 1) {
								for (int m = 0; m < str5.length; m++) {
									if (!str5[m].equals(ChkStr)) {
										if (m == str5.length - 1) {
											if (state.equals("1")) {
												str3 += rs2
														.getString("nicname")
														+ "/";
											} else if (state.equals("2")) {

											} else {
												str3 += rs.getString(ReqStr)
														+ "/";
											}
										}
									} else {
										break;
									}
								}

								if (str5.length == 0) {
									if (state.equals("1")) {
										str3 += rs2.getString("nicname") + "/";
									} else if (state.equals("2")) {

									} else {
										str3 += rs.getString(ReqStr) + "/";
									}
								}

								break;
							}

							if (!str4[k].equals(ChkStr)) {
								if (!str4[k].equals(rs.getString("written"))) {

									if (str4.length - 1 == k) {

										for (int m = 0; m < str5.length; m++) {
											if (!str5[m].equals(ChkStr)) {
												if (m == str5.length - 1) {
													if (state.equals("1")) {
														str3 += rs2
																.getString("nicname")
																+ "/";
													} else if (state
															.equals("2")) {

													} else {
														str3 += rs
																.getString(ReqStr)
																+ "/";
													}
												}
											} else {
												break;
											}
										}

										if (str5.length == 0) {
											if (state.equals("1")) {
												str3 += rs2
														.getString("nicname")
														+ "/";
											} else if (state.equals("2")) {

											} else {
												str3 += rs.getString(ReqStr)
														+ "/";
											}

										}

										break;
									}
								}
							} else {
								break;
							}

						}
					}

				}
				rs.next();
			}
			if (str3.equals("")) {
				str3 = "falsereqboard";
			}
		} catch (Exception e) {
			if (str3.equals("")) {
				str3 = "falsereqboard";
			}
			return str3;
		}
		return str3;
	}

	public String OkNoReqBoard(String id, String boardid, String state) {
		String str = "Select * from heart_pumping where id = '" + id + "';";
		String str2 = "";
		String str3[];
		String str4 = "";
		String str5 = "";
		try {
			if (state.equals("ok")) {
				rs = stmt.executeQuery(str);
				rs.first();
				if (rs.getString("boardstate").equals("0")) {
					str = " select * from heart_board where id = "
							+ Integer.parseInt(boardid) + ";";
					rs2 = stmt2.executeQuery(str);
					rs2.first();

					str2 = rs2.getString("okpeople");
					str2 += id + "/";

					str = "update heart_board set okpeople = '" + str2
							+ "' where id =" + Integer.parseInt(boardid) + ";";
					stmt.executeUpdate(str);

					str = "update heart_pumping set boardstate = 1 where id='"
							+ id + "';";
					stmt.executeUpdate(str);

					str3 = rs2.getString("nopeople").split("/");

					if (str3[0].equals("")) {
						if (str2.split("/").length == rs2.getString("people")
								.split("/").length) {
							str = "update heart_board set view_able =1 where id = "
									+ Integer.parseInt(boardid) + ";";
							stmt.executeUpdate(str);
						}
					}

					str = "Select * from heart_pumping where id = '" + id
							+ "';";
					rs3 = stmt3.executeQuery(str);
					rs3.first();
					str4 = rs3.getString("heartpoint");

					str = "update heart_board set avrpoint = "
							+ (Integer.parseInt(str4) + Integer.parseInt(rs2
									.getString("avrpoint")))
							/ str2.split("/").length + " where id = "
							+ Integer.parseInt(boardid) + ";";

					stmt.executeUpdate(str);

					str5 = "truereqboard";

				} else {
					str5 = "falsereqboard";
				}
			} else if (state.equals("cancel")) {
				str = "select * from heart_board where id ="
						+ Integer.parseInt(boardid) + ";";
				rs2 = stmt2.executeQuery(str);
				rs2.first();
				str3 = rs2.getString("okpeople").split("/");
				for (int i = 0; i < str3.length; i++) {
					if (!id.equals(str3[i])) {
						str2 += str3[i] + "/";
					}

				}

				str = "update heart_board set okpeople = '" + str2
						+ "' where id =" + Integer.parseInt(boardid) + ";";
				stmt.executeUpdate(str);

				str3 = rs2.getString("nopeople").split("/");
				str4 = id + "/";
				for (int i = 0; i < str3.length; i++) {
					if (str3[i].equals("")) {
						break;
					}
					str4 += str3[i] + "/";
				}
				System.out.println("1");
				str = "update heart_board set nopeople = '" + str4
						+ "' where id =" + Integer.parseInt(boardid) + ";";
				stmt.executeUpdate(str);
				System.out.println("2");
				str = "update heart_board set view_able = 0 where id="
						+ Integer.parseInt(boardid) + ";";
				stmt.executeUpdate(str);
				System.out.println("3");
				str = "update heart_pumping set boardstate = 0 where id='" + id
						+ "';";
				stmt.executeUpdate(str);
				System.out.println("4");
				str5 = "truereqboard";

			} else if (state.equals("delete")) {
				str = "select * from heart_board where id="
						+ Integer.parseInt(boardid) + ";";
				rs = stmt.executeQuery(str);
				rs.first();
				str3 = rs.getString("people").split("/");

				for (int i = 0; i < str3.length; i++) {
					str = "update heart_pumping set boardstate = 0 where id='"
							+ str3[i] + "';";
					stmt.executeUpdate(str);
				}

				str = "delete from heart_board where id= "
						+ Integer.parseInt(boardid) + ";";
				stmt.executeUpdate(str);
				str5 = "truereqboard";

			} else {

				str = "select * from heart_board where id ="
						+ Integer.parseInt(boardid) + ";";
				rs = stmt.executeQuery(str);
				rs.first();
				str3 = rs.getString("nopeople").split("/");
				str4 = id + "/";

				for (int i = 0; i < str3.length; i++) {
					if (str3[i].equals("")) {
						break;
					}
					str4 += str3[i] + "/";
				}

				str = "update heart_board set nopeople = '" + str4
						+ "' where id =" + Integer.parseInt(boardid) + ";";
				stmt.executeUpdate(str);
				str5 = "truereqboard";
			}
		} catch (Exception e) {
			str5 = "falsereqboard";
		}
		return str5;
	}

	public String BoardInfo(String id, String boardid, String Require1,
			String Require2, int state) {
		String str = "";
		String str2 = "falseboardinfo";
		String str3[];
		String str4[];
		String str5[];
		try {
			switch (state) {
			case 0:// 게시판정보
				str = "select * from heart_board where id="
						+ Integer.parseInt(boardid) + ";";
				rs = stmt.executeQuery(str);
				rs.first();
				str2 = rs.getString(Require1);
				break;
			case 1:// 게시판에 속한 사람들의 정보
				str = "select * from heart_board where id="
						+ Integer.parseInt(boardid) + ";";
				rs = stmt.executeQuery(str);
				rs.first();
				str3 = rs.getString(Require1).split("/");
				str2 = "";
				for (int i = 0; i < str3.length; i++) {
					str = "select * from heart_pumping where id='" + str3[i]
							+ "';";
					rs2 = stmt2.executeQuery(str);
					rs2.first();
					str2 += rs2.getString(Require2) + "/";
				}
				break;
			case 2:
				str = "select * from heart_board where id="
						+ Integer.parseInt(boardid) + ";";
				rs = stmt.executeQuery(str);
				rs.first();
				str3 = rs.getString(Require1).split("/");
				str4 = rs.getString(Require2).split("/");
				str5 = rs.getString("people").split("/");
				str2 = "";
				for (int i = 0; i < str5.length; i++) {
					for (int j = 0; j < str3.length; j++) {
						if (str5[i].equals(str3[j])) {
							break;
						} else {
							if (j == str3.length - 1) {
								for (int k = 0; k < str4.length; k++) {
									if (!str4[k].equals("")) {
										if (!str5[i].equals(str4[k])) {
											str = "select * from heart_pumping where id='"
													+ str5[i] + "';";
											rs2 = stmt2.executeQuery(str);
											rs2.first();
											str2 += rs2.getString("nicname")
													+ "/";
										}
									} else {
										str = "select * from heart_pumping where id='"
												+ str5[i] + "';";
										rs2 = stmt2.executeQuery(str);
										rs2.first();
										str2 += rs2.getString("nicname") + "/";
									}
								}
							}
						}
					}
				}
				if (str2.equals("")) {
					str2 = "falseboardinfo";
				}

				break;
			}
		} catch (Exception e) {
			str2 = "falseboardinfo";
		}
		return str2;
	}

	public String ReqMyBoard(String ChkStr, String ReqStr, String state) {
		String str = "select * from heart_board where written = '" + ChkStr
				+ "';";
		String str2 = "";
		String str3 = "";
		String str4[];
		String str5 = "";
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			if (rs.getRow() != 0) {
				if (state.equals("1")) {
					str = "select nicname from heart_pumping where id='"
							+ rs.getString("written") + "';";
					rs2 = stmt2.executeQuery(str);
					rs2.first();
					str2 = rs2.getString("nicname");
					return str2;
				} else if (state.equals("2")) {

				} else {
					str2 = rs.getString(ReqStr);
					return str2;
				}

			} else if (rs.getRow() == 0) {
				str = "select * from heart_pumping where id='" + ChkStr + "';";
				rs2 = stmt2.executeQuery(str);
				rs2.first();
				if (rs2.getString("boardstate").equals("0")) {
					str2 = "falsereqboard";
					return str2;
				} else {
					str = "select * from heart_board;";
					rs3 = stmt3.executeQuery(str);
					rs3.first();
					for (int i = 0; i < rs3.getRow(); i++) {
						str4 = rs3.getString("okpeople").split("/");
						for (int j = 0; j < str4.length; j++) {
							if (str4[j].equals(ChkStr)) {
								str5 = rs3.getString("id");
								break;
							}
						}
						if (str5.equals("")) {
							rs3.next();
						} else {
							if (state.equals("1")) {
								str = "select nicname from heart_pumping where id='"
										+ rs3.getString("written") + "';";
								rs4 = stmt4.executeQuery(str);
								rs4.first();
								str2 = rs4.getString("nicname");
								return str2;
							} else if (state.equals("2")) {

							} else {
								str2 = rs3.getString(ReqStr);
								return str2;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			str2 = "falsereqboard";
		}
		return str2;

	}

	public String PushView(String boardid) {
		String str = "select * from heart_board where id="
				+ Integer.parseInt(boardid) + ";";
		String str2 = "falsepush";
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			str2 = rs.getString("view_able");
			if (str2.equals("0")) {
				str = "update heart_board set view_able=1 where id="
						+ Integer.parseInt(boardid) + ";";
				stmt2.executeUpdate(str);
				str = "update heart_board set people ='"
						+ rs.getString("okpeople") + "' where id="
						+ Integer.parseInt(boardid) + ";";
				stmt2.executeUpdate(str);
				str = "update heart_board set nopeople = '' where id="
						+ Integer.parseInt(boardid) + ";";
				stmt2.executeUpdate(str);
				str = "update heart_board set peoplecount ="
						+ rs.getString("okpeople").split("/").length
						+ " where id=" + Integer.parseInt(boardid) + ";";
				stmt2.executeUpdate(str);
				str2 = "공개로 전환됨";
			} else {
				str = "update heart_board set view_able=0 where id="
						+ Integer.parseInt(boardid) + ";";
				stmt.executeUpdate(str);
				str2 = "비공개로 전환됨";
			}
		} catch (Exception e) {
			str2 = "falsepush";
		}
		return str2;
	}

	public String FindUserBoard(String Subject, String iWorld, String iSex,
			String iPeople, String iPumping, String ReqStr1, int State) {
		int pcount = 0;
		int scount = 0;
		String str = "";
		String str2 = "";
		String str3[];
		if (iPeople.equals("1 & 1")) {
			pcount = 1;
		} else if (iPeople.equals("2 & 2")) {
			pcount = 2;
		} else if (iPeople.equals("3 & 3")) {
			pcount = 3;
		} else if (iPeople.equals("4 & 4")) {
			pcount = 4;
		}

		if (iSex.equals("남자")) {
			scount = 0;
		} else if (iSex.equals("여자")) {
			scount = 1;
		}

		if (iWorld.equals("지역(전체)")) {
			if (iSex.equals("성별(전체)")) {
				if (iPeople.equals("인원(전체)")) {
					if (iPumping.equals("두근(전체)")) {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%';";
					} else {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and avrpoint >="
								+ Integer.parseInt(iPumping) + ";";
					}
				} else {
					if (iPumping.equals("두근(전체)")) {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and peoplecount =" + pcount
								+ ";";
					} else {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and peoplecount =" + pcount
								+ " and avrpoint >="
								+ Integer.parseInt(iPumping) + ";";
					}
				}
			} else {
				if (iPeople.equals("인원(전체)")) {
					if (iPumping.equals("두근(전체)")) {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and writtensex =" + scount
								+ ";";
					} else {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and writtensex =" + scount
								+ " and avrpoint >="
								+ Integer.parseInt(iPumping) + ";";
					}
				} else {
					if (iPumping.equals("두근(전체)")) {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and writtensex =" + scount
								+ " and peoplecount =" + pcount + ";";
					} else {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and writtensex =" + scount
								+ " and peoplecount =" + pcount
								+ " and avrpoint >="
								+ Integer.parseInt(iPumping) + ";";
					}
				}
			}
		} else {
			if (iSex.equals("성별(전체)")) {
				if (iPeople.equals("인원(전체)")) {
					if (iPumping.equals("두근(전체)")) {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and (meetzone ='" + iWorld
								+ "' or meetzone ='지역(전체)');";
					} else {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and (meetzone ='" + iWorld
								+ "' or meetzone ='지역(전체)') and avrpoint >="
								+ Integer.parseInt(iPumping) + ";";
					}
				} else {
					if (iPumping.equals("두근(전체)")) {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and (meetzone ='" + iWorld
								+ "' or meetzone ='지역(전체)') and peoplecount ="
								+ pcount + ";";
					} else {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and (meetzone ='" + iWorld
								+ "' or meetzone ='지역(전체)') and peoplecount ="
								+ pcount + " and avrpoint >="
								+ Integer.parseInt(iPumping) + ";";
					}
				}
			} else {
				if (iPeople.equals("인원(전체)")) {
					if (iPumping.equals("두근(전체)")) {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and (meetzone ='" + iWorld
								+ "' or meetzone ='지역(전체)') and writtensex ="
								+ scount + ";";
					} else {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and (meetzone ='" + iWorld
								+ "' or meetzone ='지역(전체)') and writtensex ="
								+ scount + " and avrpoint >="
								+ Integer.parseInt(iPumping) + ";";
					}
				} else {
					if (iPumping.equals("두근(전체)")) {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and (meetzone ='" + iWorld
								+ "' or meetzone ='지역(전체)') and writtensex ="
								+ scount + " and peoplecount =" + pcount + ";";
					} else {
						str = "select * from heart_board where subject like '%"
								+ Subject + "%' and (meetzone ='" + iWorld
								+ "' or meetzone ='지역(전체)') and writtensex ="
								+ scount + " and peoplecount =" + pcount
								+ " and avrpoint >="
								+ Integer.parseInt(iPumping) + ";";
					}
				}
			}
		}

		try {
			rs = stmt.executeQuery(str);
			rs.first();
			if (rs.getRow() == 0) {
				str2 = "falseuserboard";
				return str2;
			}

			if (rs.getString("view_able").equals("1")) {
				switch (State) {
				case 0:
					for (int i = 0; i < rs.getRow(); i++) {
						str2 += rs.getString(ReqStr1) + "/";
						rs.next();
					}
					break;
				case 1:
					for (int i = 0; i < rs.getRow(); i++) {
						str3 = rs.getString("okpeople").split("/");
						for (int j = 0; j < str3.length; j++) {
							str = "select * from heart_pumping where id='"
									+ str3[j] + "';";
							rs2 = stmt2.executeQuery(str);
							rs2.first();
							str2 += rs2.getString(ReqStr1) + "/";
						}
						str2 += "/";
						rs.next();
					}
					break;
				case 2:
					for (int i = 0; i < rs.getRow(); i++) {
						str2 += rs.getString(ReqStr1) + "//";
						rs.next();
					}
					break;
				}
			}

		} catch (Exception e) {
			str2 = "falseuserboard";
		}
		if (str2.equals("")) {
			str2 = "falseuserboard";
		}
		return str2;
	}

	public String Changeflag(String id, String flag, int state) {
		String str = "select * from heart_pumping where id='" + id + "';";
		String str2 = "";
		try {
			if (state == 1) {
				rs = stmt.executeQuery(str);
				rs.first();
				str2 = rs.getString("searchnic");
				return str2;
			}
			stmt.executeUpdate("update heart_pumping set searchnic="
					+ Integer.parseInt(flag) + " where id='" + id + "';");
			str2 = "truesearchflag";
		} catch (Exception e) {
			str2 = "falsesearchflag";
		}
		return str2;
	}

	public String RemoveMember(String id) {
		String str = "select * from heart_board;";
		String str2 = "";
		String str3[];
		String str4 = "";
		try {
			stmt.execute("delete from heart_board where written='" + id + "';");

			rs2 = stmt2.executeQuery(str);
			rs2.first();
			for (int i = 0; i < rs2.getRow(); i++) {
				str3 = rs2.getString("people").split("/");
				for (int j = 0; j < str3.length; j++) {
					if (!str3[j].equals(id)) {
						str4 += str3[j] + "/";
					}
				}
				stmt.executeUpdate("update heart_board set people='" + str4
						+ "', peoplecount = " + str4.split("/").length
						+ " where id=" + rs2.getInt("id") + ";");

				str4="";
				str3 = rs2.getString("okpeople").split("/");
				for (int j = 0; j < str3.length; j++) {
					if (!str3[j].equals(id)) {
						str4 += str3[j] + "/";
					}
				}
				
				stmt.executeUpdate("update heart_board set okpeople='" + str4
						+ "' where id=" + rs2.getInt("id") + ";");
				str4="";
				str3 = rs2.getString("nopeople").split("/");
				if (!str3[0].equals("")) {
					for (int j = 0; j < str3.length; j++) {
						if (!str3[j].equals(id)) {
							str4 += str3[j] + "/";
						}
					}
					stmt.executeUpdate("update heart_board set nopeople='" + str4
							+ "' where id=" + rs2.getInt("id") + ";");
				}
				
				//avrpoint도 다시 계산해야됨.
				
				rs2.next();
			}

			stmt.execute("delete from heart_friends where send='" + id
					+ "' or receiver ='" + id + "';");
			stmt.execute("delete from heart_pumping where id='" + id + "';");
			str2 = "trueremove";
		} catch (Exception e) {
			str2 = "falseremove";
		}
		return str2;
	}

	public String FindReqMyBoard(String boardid, String id) {
		String str = "select * from heart_board where id="
				+ Integer.parseInt(boardid) + ";";
		String str2 = "";
		String str3[];
		try {
			rs = stmt.executeQuery(str);
			rs.first();
			str3 = rs.getString("request").split("/");

			if (str3[0].equals("")) {
				str2 = "falsemyreqboard";
				return str2;
			}

			for (int i = 0; i < str3.length; i++) {
				str = "select * from heart_pumping where id='" + str3[i] + "';";
				rs2 = stmt2.executeQuery(str);
				rs2.first();
				str2 += rs2.getString("nicname") + "/";
			}
		} catch (Exception e) {
			str2 = "falsemyreqboard";
		}
		return str2;
	}

	public String ReqMeetBoard(String BoardId, String ReqId, String State,
			String count) {
		String str = "select * from heart_board where id = "
				+ Integer.parseInt(BoardId) + ";";
		String str2 = "";
		String str3[];
		String str4 = "";
		try {
			if (State.equals("request")) {
				rs = stmt.executeQuery(str);
				rs.first();
				if (rs.getInt("requestcount") < 3) {
					if (rs.getInt("peoplecount") == Integer.parseInt(count)) {
						str3 = rs.getString("request").split("/");
						if (!str3[0].equals("")) {
							for (int i = 0; i < str3.length; i++) {
								if (str3[i].equals(ReqId)) {
									str2 = "falsereqerror2";
									return str2;
								}
							}
							str4 = rs.getString("request");
							int count2 = str3.length + 1;
							str = "update heart_board set request='" + str4
									+ ReqId + "/" + "', requestcount ="
									+ count2 + " where id="
									+ Integer.parseInt(BoardId) + ";";
							stmt2.executeUpdate(str);
							str2 = "truereqmeetboard";
						} else {
							str4 = rs.getString("request");
							int count2 = 1;
							str = "update heart_board set request='" + str4
									+ ReqId + "/" + "', requestcount ="
									+ count2 + " where id="
									+ Integer.parseInt(BoardId) + ";";
							stmt2.executeUpdate(str);
							str2 = "truereqmeetboard";
						}

					} else {
						str2 = "falseovercount2";
						return str2;
					}
				} else {
					str2 = "falseovercount1";
					return str2;
				}
			}
		} catch (Exception e) {
			str2 = "falsereqerror1";
		}
		return str2;
	}
}
