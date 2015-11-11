package com.example.heartpumping;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


public class HPServMain {

	
	
	//서버접속관련 변수 선언 시작
	private Selector hpSelector = null;
	private ServerSocketChannel hpserversocketchannel = null;
	private ServerSocket hpserversocket = null;
	private SocketAddress hpsocketaddress = null;
	private SocketChannel hpsocketchannel = null;
	//서버접속관련 변수 선언 끝
	
	//서버 접속 스레드 선언 시작 (입출렷 및 접속 등 담당)
	private HPServTh hpservth = null;
	//서버 접속 스레드 선언 끝 (입출렷 및 접속 등 담당)

	public HPServMain()
	{
		try {
			hpSelector = Selector.open();
			hpserversocketchannel = ServerSocketChannel.open();
			hpserversocket = hpserversocketchannel.socket();
			
			hpsocketaddress = new InetSocketAddress(1525);
			
			hpserversocket.bind(hpsocketaddress);
			
			hpserversocketchannel.configureBlocking(false);
			hpserversocketchannel.register(hpSelector, SelectionKey.OP_ACCEPT);
			System.out.println("서버오픈");
			while(true)
			{
				int num;
				num = hpSelector.select();
				if(num > 0)
				{
					Iterator it = hpSelector.selectedKeys().iterator();
					System.out.println("접속중 iterator");
					while(it.hasNext())
					{
						SelectionKey key = (SelectionKey) it.next();
						it.remove();
						hpservth = new HPServTh(key);
						if(key.isAcceptable())
						{
							System.out.println("접속중 스레드대기중");
							hpservth.start();
							Thread.sleep(1000);
						}
					}
				}
				
			}
			
		} catch (Exception e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
	}
	
	public static void initbuffer(ByteBuffer bf)
	{
		bf.clear();
		bf.put(new byte[1024]);
		bf.flip();
		bf.clear();
	}
	
	public static void main(String args[])
	{
		HPServMain hpservmain = new HPServMain();
		
	}
}
