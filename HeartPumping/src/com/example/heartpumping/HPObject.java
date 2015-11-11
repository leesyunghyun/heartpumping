package com.example.heartpumping;

import java.io.Serializable;

public class HPObject implements Serializable {

	public String ID;
	public String PassWord;
	public String NicName;
	public String Sex;
	public String Univ_1;
	public String Univ_2;
	public String Phone;
	public String message;
	public String SchoolNum;
	public String Profile;
	public String HeartPoint;
	public HPBoardObject board;
	
	public HPObject(String ID, String PassWord, String Nicname, String Sex, String Univ_1, String Univ_2, String Phone,String SchoolNum,String Profile, String HeartPoint,HPBoardObject board, String message)
	{
		this.ID = ID; 
		this.PassWord = PassWord;
		this.NicName = Nicname;
		this.Sex = Sex;
		this.Univ_1 = Univ_1;
		this.Univ_2 = Univ_2;
		this.Phone = Phone;
		this.SchoolNum = SchoolNum;
		this.Profile = Profile;
		this.HeartPoint = HeartPoint;
		this.board = board;
		this.message = message;
	}
}
