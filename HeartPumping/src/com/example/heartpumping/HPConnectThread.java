package com.example.heartpumping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HPConnectThread extends Thread {
	Context context;
	Handler handler;
	public static SocketChannel channel;
	public static Selector selector;
	Socket s;
	BufferedReader br;
	BufferedWriter bw;
	ByteArrayOutputStream baos;
	ObjectOutputStream out;
	int message = 0;

	public HPConnectThread(Context context) {
		this.context = context;
		try {
			selector = Selector.open();
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
		} catch (Exception e) {

		}
	}

	@Override
	public void run() {
		try {
			String str = "192.168.0.4";
			if (!channel.isConnected()) {
				channel = SocketChannel.open(new InetSocketAddress(InetAddress
						.getByName(str), 1525));
			}
			this.interrupt();
		} catch (Exception e) {

		}
		super.run();
	}

}
