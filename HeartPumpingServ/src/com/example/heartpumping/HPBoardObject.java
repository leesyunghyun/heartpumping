package com.example.heartpumping;

import java.io.Serializable;

public class HPBoardObject implements Serializable {

	public String Written;
	public String Subject;
	public String Content;
	public String MeetZone;
	public String PeoPleCount;
	public String PeoPle;
	public String OkPeoPle;
	public String NoPeoPle;
	public String AvrPoint;
	public String RequestCount;
	public String Request;
	public String RequestAvrPoint;
	public String Viewable;
	public String boardid;

	public HPBoardObject(String Written, String Subject, String Content,
			String MeetZone, String PeoPleCount, String PeoPle,String OkPeoPle, String NoPeoPle, String AvrPoint,
			String RequestCount, String Request, String RequestAvrPoint, String Viewable, String boardid) {
		this.Written = Written;
		this.Subject = Subject;
		this.Content = Content;
		this.MeetZone = MeetZone;
		this.PeoPleCount = PeoPleCount;
		this.PeoPle = PeoPle;
		this.OkPeoPle = OkPeoPle;
		this.NoPeoPle = NoPeoPle;
		this.RequestAvrPoint = RequestAvrPoint;
		this.AvrPoint = AvrPoint;
		this.RequestCount = RequestCount;
		this.Request = Request;
		this.Viewable = Viewable;
		this.boardid = boardid;
	}
}
