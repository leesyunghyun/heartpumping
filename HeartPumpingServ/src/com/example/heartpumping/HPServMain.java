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

	
	
	//�������Ӱ��� ���� ���� ����
	private Selector hpSelector = null;
	private ServerSocketChannel hpserversocketchannel = null;
	private ServerSocket hpserversocket = null;
	private SocketAddress hpsocketaddress = null;
	private SocketChannel hpsocketchannel = null;
	//�������Ӱ��� ���� ���� ��
	
	//���� ���� ������ ���� ���� (����� �� ���� �� ���)
	private HPServTh hpservth = null;
	//���� ���� ������ ���� �� (����� �� ���� �� ���)

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
			System.out.println("��������");
			while(true)
			{
				int num;
				num = hpSelector.select();
				if(num > 0)
				{
					Iterator it = hpSelector.selectedKeys().iterator();
					System.out.println("������ iterator");
					while(it.hasNext())
					{
						SelectionKey key = (SelectionKey) it.next();
						it.remove();
						hpservth = new HPServTh(key);
						if(key.isAcceptable())
						{
							System.out.println("������ ����������");
							hpservth.start();
							Thread.sleep(1000);
						}
					}
				}
				
			}
			
		} catch (Exception e) {
			// TODO �ڵ� ������ catch ���
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
