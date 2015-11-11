package com.example.heartpumping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class HPServTh extends Thread {

	// �������� �� ä�ΰ��� ���� ���� ����
	private ServerSocketChannel hpthserversocketchannel = null;
	private SelectionKey hpservthkey = null;
	private SocketChannel hpservthsocketchannel = null;
	// �������� �� ä�ΰ��� ���� ���� ��

	// ���� ��ſ� �ʿ��� ��Ʈ�� ���� ���� ����
	private ObjectInputStream hpinputstream;
	private ObjectOutputStream hpoutputstream;
	private ByteBuffer data;// = ByteBuffer.allocate(1024);
	private ByteArrayInputStream bais = null;
	private ByteArrayOutputStream baos = null;
	// ���� ��ſ� �ʿ��� ��Ʈ�� ���� ���� ��

	// �����ͺ��̽� ���� �������
	private HPData hpdata;

	// �����ͺ��̽� ���� ����
	public HPServTh(SelectionKey key) {
		hpservthkey = key;
	}

	@Override
	public void run() {
		// TODO �ڵ� ������ �޼ҵ� ����
		hpthserversocketchannel = (ServerSocketChannel) hpservthkey.channel();
		data = ByteBuffer.allocate(1024);
		hpdata = new HPData();
		try {
			hpservthsocketchannel = hpthserversocketchannel.accept();
			System.out.println(hpservthsocketchannel.socket().getInetAddress()
					.getHostAddress().toString()
					+ " //accept����");
			while (true) {
				readhpobject(hpinputstream, hpoutputstream,
						hpservthsocketchannel);
			}
		} catch (Exception e) {
			// TODO �ڵ� ������ catch ���
			e.printStackTrace();
		}
	}

	public void readhpobject(ObjectInputStream ois, ObjectOutputStream out,
			SocketChannel sc) {
		try {

			System.out.println("�б� �����");

			hpservthsocketchannel.read(data);

			System.out.println("�ϱ��û ����");

			bais = new ByteArrayInputStream(data.array());

			ois = new ObjectInputStream(bais);

			HPObject hp = (HPObject) ois.readObject();

			switch (hp.message) {
			case "login":
				System.out.println("�α��νõ���  : " + hp.ID + " // " + hp.PassWord);
				if (hpdata.LoginCheck(hp.ID, hp.PassWord)) {
					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject outhp = new HPObject(null, null, null, null, null,
							null, null, null, null, null, null, "truelogin");

					Object obc = outhp;

					out.writeObject(obc);
					if (hpservthsocketchannel.isConnected()) {
						hpservthsocketchannel.write(ByteBuffer.wrap(baos
								.toByteArray()));
					}
					System.out.println("�α��μ���  : " + hp.ID + " // "
							+ hp.PassWord);
				} else {

					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject outhp = new HPObject(null, null, null, null, null,
							null, null, null, null, null, null, "falselogin");

					Object obc = outhp;

					out.writeObject(obc);
					if (hpservthsocketchannel.isConnected()) {
						hpservthsocketchannel.write(ByteBuffer.wrap(baos
								.toByteArray()));
					}
					System.out.println("�α��ν���  : " + hp.ID + " // "
							+ hp.PassWord);
				}
				HPServMain.initbuffer(data);
				break;
			case "newmember":
				System.out.println("�������� : " + hp.ID + " // " + hp.PassWord
						+ " // " + hp.NicName + " // " + hp.Phone + " // ");
				hpdata.InsertMember(hp.ID, hp.PassWord, hp.NicName, hp.Sex,
						hp.Univ_1, hp.Univ_2, hp.Phone, hp.SchoolNum);
				HPServMain.initbuffer(data);
				break;
			case "idcheck":
				System.out.println("���̵� üũ�� : " + hp.ID);
				if (hpdata.IDCheck(hp.ID)) {

					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject outhp = new HPObject(null, null, null, null, null,
							null, null, null, null, null, null, "trueid");

					Object obc = outhp;

					out.writeObject(obc);
					if (hpservthsocketchannel.isConnected()) {
						hpservthsocketchannel.write(ByteBuffer.wrap(baos
								.toByteArray()));
					}
					System.out.println("��밡���� ���̵� : " + hp.ID);
				} else {
					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject outhp = new HPObject(null, null, null, null, null,
							null, null, null, null, null, null, "falseid");

					Object obc = outhp;

					out.writeObject(obc);
					if (hpservthsocketchannel.isConnected()) {
						hpservthsocketchannel.write(ByteBuffer.wrap(baos
								.toByteArray()));
					}
					System.out.println("��� �Ұ����� ���̵� : " + hp.ID);
				}
				HPServMain.initbuffer(data);
				break;
			case "phonecheck":
				System.out.println("����ó üũ�� : " + hp.Phone);
				if (hpdata.PhoneCheck(hp.Phone)) {

					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject outhp = new HPObject(null, null, null, null, null,
							null, null, null, null, null, null, "truephone");

					Object obc = outhp;

					out.writeObject(obc);
					if (hpservthsocketchannel.isConnected()) {
						hpservthsocketchannel.write(ByteBuffer.wrap(baos
								.toByteArray()));
					}
					System.out.println("��밡���� ����ó : " + hp.Phone);
				} else {
					baos = new ByteArrayOutputStream();
					out = new ObjectOutputStream(baos);

					HPObject outhp = new HPObject(null, null, null, null, null,
							null, null, null, null, null, null, "falsephone");

					Object obc = outhp;

					out.writeObject(obc);
					if (hpservthsocketchannel.isConnected()) {
						hpservthsocketchannel.write(ByteBuffer.wrap(baos
								.toByteArray()));
					}
					System.out.println("��� �Ұ����� ����ó : " + hp.Phone);
				}
				HPServMain.initbuffer(data);
				break;
			case "findid":
				System.out.println("���̵� ã���� : " + hp.NicName + " // "
						+ hp.Phone);

				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject outid = new HPObject(null, null, null, null, null,
						null, null, null, null, null, null, hpdata.FindID(
								hp.NicName, hp.Phone));

				Object obcid = outid;

				out.writeObject(obcid);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("���̵� ã�� ��� : " + outid.message);
				HPServMain.initbuffer(data);
				break;
			case "findpw":
				System.out.println("��� ã���� : " + hp.ID + " // " + hp.Phone);

				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject outpw = new HPObject(null, null, null, null, null,
						null, null, null, null, null, null, hpdata.FindPW(
								hp.ID, hp.Phone));

				Object obcpw = outpw;

				out.writeObject(obcpw);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("��� ã�� ��� : " + outpw.message);
				HPServMain.initbuffer(data);
				break;
			case "findfriend":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject findfri = new HPObject(hpdata.FindData("id",
						"nicname", hp.NicName, hp.ID, hp.Sex, hp.Univ_1), null,
						null, hpdata.FindData("sex", "nicname", hp.NicName,
								hp.ID, hp.Sex, hp.Univ_1), hpdata.FindData(
								"univ_1", "nicname", hp.NicName, hp.ID, hp.Sex,
								hp.Univ_1), null,
						hpdata.FindData("phone", "nicname", hp.NicName, hp.ID,
								hp.Sex, hp.Univ_1), null, null,
						hpdata.FindData("heartpoint", "nicname", hp.NicName,
								hp.ID, hp.Sex, hp.Univ_1), null,
						hpdata.FindFriend(hp.NicName, hp.ID, hp.Sex, hp.Univ_1));
				Object friendobc = findfri;

				out.writeObject(friendobc);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("ģ�� ã�� ��� : " + findfri.message);
				HPServMain.initbuffer(data);
				break;
			case "setfriend":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject setfriend = new HPObject(null, null, null, null, null,
						null, null, null, null, null, null, hpdata.SetFriend(
								hp.ID, hp.PassWord, hp.NicName));
				Object setfriob = setfriend;

				out.writeObject(setfriob);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("ģ���߰� ��� : " + setfriend.message);
				HPServMain.initbuffer(data);
				break;
			case "setfriendban":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);
				HPObject setfriendban = new HPObject(null, null, null, null,
						null, null, null, null, null, null, null,
						hpdata.SetFriend(hp.ID, hp.PassWord, hp.NicName));
				Object setfriobb = setfriendban;

				out.writeObject(setfriobb);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("ģ���۾� ��� : " + setfriendban.message);
				HPServMain.initbuffer(data);
				break;
			case "findhomefri":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);
				HPObject findhome = new HPObject(hpdata.FindResult(hp.ID, "1",
						"id"), null, null,
						hpdata.FindResult(hp.ID, "1", "sex"),
						hpdata.FindResult(hp.ID, "1", "univ_1"), null,
						hpdata.FindResult(hp.ID, "1", "phone"), null,
						hpdata.FindResult(hp.ID, "1", "profile"),
						hpdata.FindResult(hp.ID, "1", "heartpoint"), null,
						hpdata.FindResult(hp.ID, "1", "nicname"));

				Object find = findhome;

				out.writeObject(find);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("�α���ģ�� ��� : " + findhome.message);
				System.out.println("�α���ģ�� ��ȭ�� ��� : " + findhome.Profile);
				HPServMain.initbuffer(data);
				break;
			case "findhomemy":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);
				HPObject findhomemy = new HPObject(null, hpdata.FindResultMy(
						hp.ID, "password"), null, hpdata.FindResultMy(hp.ID,
						"sex"), hpdata.FindResultMy(hp.ID, "univ_1"),
						hpdata.FindResultMy(hp.ID, "univ_2"),
						hpdata.FindResultMy(hp.ID, "phone"),
						hpdata.FindResultMy(hp.ID, "student"),
						hpdata.FindResultMy(hp.ID, "profile"),
						hpdata.FindResultMy(hp.ID, "heartpoint"), null,
						hpdata.FindResultMy(hp.ID, "nicname"));

				Object findmy = findhomemy;

				out.writeObject(findmy);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("�� ���� : " + findhomemy.message);
				HPServMain.initbuffer(data);
				break;
			case "membermodify":
				hpdata.UpdateMember(hp.ID, hp.PassWord, hp.NicName, hp.Sex,
						hp.Univ_1, hp.Univ_2, hp.Phone, hp.SchoolNum);
				System.out.println("ȸ������ : " + hp.ID + " // " + hp.PassWord
						+ " // " + hp.NicName + " // " + hp.Phone + " // ");
				HPServMain.initbuffer(data);
				break;
			case "findban":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);
				HPObject findban = new HPObject(hpdata.FindResult(hp.ID, "2",
						"id"), null, null, null, null, null, null, null, null,
						null, null, hpdata.FindResult(hp.ID, "2", "nicname"));

				Object findb = findban;

				out.writeObject(findb);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("��ģ�� ��� : " + findban.message);
				System.out.println("��ģ�� ���̵� ��� : " + findban.ID);
				HPServMain.initbuffer(data);
				break;
			case "messagemodify":
				hpdata.UpdateProfile(hp.ID, hp.NicName);
				System.out.println("��ȭ����� : " + hp.ID + " // " + hp.Profile
						+ " // ");
				HPServMain.initbuffer(data);
				break;
			case "cancelban":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);
				HPObject cancelban = new HPObject(null, null, null, null, null,
						null, null, null, null, null, null, hpdata.SetFriend(
								hp.ID, hp.PassWord, hp.NicName));
				Object cancel = cancelban;

				out.writeObject(cancel);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("ģ���۾� ��� : " + cancelban.message);
				HPServMain.initbuffer(data);
				break;
			case "findfndin":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);
				HPObject findfndin = new HPObject(hpdata.FindReverseResult(
						hp.ID, "0", "id"), null, null,
						hpdata.FindReverseResult(hp.ID, "0", "sex"),
						hpdata.FindReverseResult(hp.ID, "0", "univ_1"), null,
						hpdata.FindReverseResult(hp.ID, "0", "phone"), null,
						null,
						hpdata.FindReverseResult(hp.ID, "0", "heartpoint"),
						null, hpdata.FindReverseResult(hp.ID, "0", "nicname"));

				Object findin = findfndin;

				out.writeObject(findin);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("�� ��û ���̵� ��� : " + findfndin.ID);
				HPServMain.initbuffer(data);
				break;
			case "findfndout":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);
				HPObject findfndout = new HPObject(hpdata.FindResult(hp.ID,
						"0", "id"), null, null, hpdata.FindResult(hp.ID, "0",
						"sex"), hpdata.FindResult(hp.ID, "0", "univ_1"), null,
						hpdata.FindResult(hp.ID, "0", "phone"), null, null,
						hpdata.FindResult(hp.ID, "0", "heartpoint"), null,
						hpdata.FindResult(hp.ID, "0", "nicname"));

				Object findout = findfndout;

				out.writeObject(findout);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("���� ��û ���̵� ��� : " + findfndout.ID);
				HPServMain.initbuffer(data);
				break;
			case "makeboard":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject makeboard = new HPObject(null, null, null, null, null,
						null, null, null, null, null, null, hpdata.MakeBoard(
								hp.board.Written, hp.board.Subject,
								hp.board.Content, hp.board.MeetZone,
								hp.board.PeoPleCount, hp.board.PeoPle,
								hp.board.Written + "/", "", hp.board.AvrPoint));

				
				System.out.println("���� : " + hp.board.AvrPoint);
				Object boardobject = makeboard;

				out.writeObject(boardobject);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("�Խ��ǵ�� ��� : " + makeboard.message);
				HPServMain.initbuffer(data);
				break;
			case "findreqboard":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPBoardObject boardreq = new HPBoardObject(hpdata.ReqBoard(
						hp.ID, "written", "0"), hpdata.ReqBoard(hp.ID,
						"subject", "0"),
						hpdata.ReqBoard(hp.ID, "content", "0"),
						hpdata.ReqBoard(hp.ID, "meetzone", "0"),
						hpdata.ReqBoard(hp.ID, "peoplecount", "0"),
						hpdata.ReqBoard(hp.ID, "people", "0"), hpdata.ReqBoard(
								hp.ID, "okpeople", "0"), hpdata.ReqBoard(hp.ID,
								"nopeople", "0"), hpdata.ReqBoard(hp.ID,
								"avrpoint", "0"), hpdata.ReqBoard(hp.ID,
								"requestcount", "0"), hpdata.ReqBoard(hp.ID,
								"request", "0"), hpdata.ReqBoard(hp.ID,
								"request_avrpoint", "0"), hpdata.ReqBoard(
								hp.ID, "view_able", "0"), hpdata.ReqBoard(
								hp.ID, "id", "0"));

				HPObject reqboard = new HPObject(null, null, hpdata.ReqBoard(
						hp.ID, "nicname", "1"), null, null, null, null, null,
						null, null, boardreq, hpdata.ReqBoard(hp.ID, "id", "0"));

				Object reqobject = reqboard;
				out.writeObject(reqobject);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("��û�Խ��� ��� : " + reqboard.message);
				HPServMain.initbuffer(data);
				break;
			case "oknoreqboard":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject okreqboard = new HPObject(null, null, null, null,
						null, null, null, null, null, null, null,
						hpdata.OkNoReqBoard(hp.ID, hp.PassWord, hp.NicName));

				Object okreq = okreqboard;
				out.writeObject(okreq);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("�Խ��� ��� : " + okreqboard.message);
				HPServMain.initbuffer(data);
				break;
			case "findmyboard":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPBoardObject boardmy = new HPBoardObject(hpdata.ReqMyBoard(
						hp.ID, "written", "0"), hpdata.ReqMyBoard(hp.ID,
						"subject", "0"), hpdata.ReqMyBoard(hp.ID, "content",
						"0"), hpdata.ReqMyBoard(hp.ID, "meetzone", "0"),
						hpdata.ReqMyBoard(hp.ID, "peoplecount", "0"),
						hpdata.ReqMyBoard(hp.ID, "people", "0"),
						hpdata.ReqMyBoard(hp.ID, "okpeople", "0"),
						hpdata.ReqMyBoard(hp.ID, "nopeople", "0"),
						hpdata.ReqMyBoard(hp.ID, "avrpoint", "0"),
						hpdata.ReqMyBoard(hp.ID, "requestcount", "0"),
						hpdata.ReqMyBoard(hp.ID, "request", "0"),
						hpdata.ReqMyBoard(hp.ID, "request_avrpoint", "0"),
						hpdata.ReqMyBoard(hp.ID, "view_able", "0"),
						hpdata.ReqMyBoard(hp.ID, "id", "0"));

				HPObject findmyboard = new HPObject(null, null,
						hpdata.ReqMyBoard(hp.ID, "nicname", "1"), null, null,
						null, null, null, null, null, boardmy,
						hpdata.ReqMyBoard(hp.ID, "id", "0"));

				Object findmyb = findmyboard;
				out.writeObject(findmyb);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("�� �Խ��� ��� : " + findmyboard.message);
				System.out.println("�� �Խ��� �г� : " + findmyboard.NicName);
				HPServMain.initbuffer(data);
				break;
			case "modifyboard":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject modifyboard = new HPObject(null, null, null, null,
						null, null, null, null, null, null, null,
						hpdata.ModifyBoard(hp.board.Written, hp.board.Subject,
								hp.board.Content, hp.board.MeetZone));

				Object modifyob = modifyboard;

				out.writeObject(modifyob);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("�Խ��Ǽ��� ��� : " + modifyboard.message);
				HPServMain.initbuffer(data);
				break;
			case "boardinfo":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPBoardObject boardinfo = new HPBoardObject(hpdata.BoardInfo(
						hp.ID, hp.PassWord, "written", null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord,"subject",null,0),
						hpdata.BoardInfo(hp.ID,hp.PassWord,"content",null,0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "meetzone",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "peoplecount",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "people",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "okpeople",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "nopeople",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "avrpoint",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "requestcount",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "request",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "request_avrpoint",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "view_able",null, 0),
						hpdata.BoardInfo(hp.ID,hp.PassWord, "id",null, 0));

				HPObject boardinfoob = new HPObject(hpdata.BoardInfo(hp.ID,hp.PassWord,"okpeople","nopeople",2), null,
						hpdata.BoardInfo(hp.ID,hp.PassWord,"okpeople","nicname",1), hpdata.BoardInfo(hp.ID,hp.PassWord,"nopeople","nicname",1), null,
						null, null, null, null, null, boardinfo,
						hpdata.BoardInfo(hp.ID,hp.PassWord,"okpeople","nicname",1));

				Object boardob = boardinfoob;
				out.writeObject(boardob);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				System.out.println("�Խ������� : " + hpdata.BoardInfo(hp.ID,hp.PassWord,"okpeople","nicname",1));
				HPServMain.initbuffer(data);
				break;
			case "pushview":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject pushview = new HPObject(null, null,
						null, null, null,
						null, null, null, null, null, null,hpdata.PushView(hp.ID));

				Object push = pushview;
				out.writeObject(push);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				
				System.out.println("�Խ��� ���� : " + pushview.message );
				HPServMain.initbuffer(data);
				break;
			case "finduserboard":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPBoardObject userboard = new HPBoardObject(hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "written", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "subject", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "content", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "meetzone", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "peoplecount", 0),
						null,
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "okpeople", 2),
						null,
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "avrpoint", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "requestcount", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "request", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "request_avrpoint", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "view_able", 0),
						hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "id", 0));
				
				HPObject finduserboard = new HPObject(null, null,
						null, null, null,
						null, null, null, null, null, userboard,hpdata.FindUserBoard(hp.board.Subject, hp.board.MeetZone, hp.Sex, hp.board.PeoPleCount, hp.board.AvrPoint, "nicname", 1));

				Object finduser = finduserboard;
				out.writeObject(finduser);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				
				System.out.println("�Խ��� �˻� : " + finduserboard.message );
				HPServMain.initbuffer(data);
				break;
			case "searchflag":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject searchnic = new HPObject(null, null,
						null, null, null,
						null, null, null, null, null, null,hpdata.Changeflag(hp.ID, hp.PassWord,0));

				Object searchflag = searchnic;
				out.writeObject(searchflag);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				
				System.out.println("�г��Ӱ˻� : " + searchnic.message );
				HPServMain.initbuffer(data);
				break;
			case "removemember":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject remove = new HPObject(null, null,
						null, null, null,
						null, null, null, null, null, null,hpdata.RemoveMember(hp.ID));

				Object removeob = remove;
				out.writeObject(removeob);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				
				System.out.println("ȸ������ : " + remove.message );
				HPServMain.initbuffer(data);
				
				break;
			case "joinsearchnic":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject joinsearchnic = new HPObject(null, null,
						null, null, null,
						null, null, null, null, null, null,hpdata.Changeflag(hp.ID, hp.PassWord,1));

				Object joinsearchnicob = joinsearchnic;
				out.writeObject(joinsearchnicob);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				
				System.out.println("�г��Ӱ˻���ȸ : " + joinsearchnic.message );
				HPServMain.initbuffer(data);
				break;
			case "reqmeetboard":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject reqmeetboard = new HPObject(null, null,
						null, null, null,
						null, null, null, null, null, null,hpdata.ReqMeetBoard(hp.PassWord,hp.ID,hp.NicName,hp.Sex));

				Object reqmeetboardob = reqmeetboard;
				out.writeObject(reqmeetboardob);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				
				System.out.println("�Խ��ǽ�û : " + reqmeetboard.message );
				HPServMain.initbuffer(data);
				break;
			case "findmyreqboard":
				baos = new ByteArrayOutputStream();
				out = new ObjectOutputStream(baos);

				HPObject findmyreqboard = new HPObject(null, null,
						null, null, null,
						null, null, null, null, null, null,hpdata.FindReqMyBoard(hp.PassWord, hp.ID));

				Object findmyreqboardob = findmyreqboard;
				out.writeObject(findmyreqboardob);
				if (hpservthsocketchannel.isConnected()) {
					hpservthsocketchannel.write(ByteBuffer.wrap(baos
							.toByteArray()));
				}
				
				System.out.println("��û�Խ��Ǹ�� : " + findmyreqboard.message );
				HPServMain.initbuffer(data);
				break;
			case "loginbye":
				System.out.println(hpservthsocketchannel.socket()
						.getInetAddress().getHostAddress().toString()
						+ " �α׾ƿ� ��û��");
				HPServMain.initbuffer(data);
				sc.close();
				this.destroy();
				break;
			}
		} catch (ClassNotFoundException e) {
			// TODO �ڵ� ������ catch ���
		} catch (IOException e) {
			// TODO �ڵ� ������ catch ���
			System.out.println(hpservthsocketchannel.socket().getInetAddress()
					.getHostAddress().toString()
					+ " ������ ��Ŵ");
			HPServMain.initbuffer(data);
			try {
				sc.close();
			} catch (IOException e1) {
				// TODO �ڵ� ������ catch ���
				e1.printStackTrace();
			}
			this.destroy();
		}
	}
}
