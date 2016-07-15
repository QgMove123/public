package com.qg;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
/*
 * 客户端
 */
public class Client {		
	public static void main(String[] args)  {	
		try {
			//1.创建客户端Socket，指定服务器地址和端口
			Socket socket = new Socket("localhost", 8888);
			//2.创建客户端线程，实时获取服务器信息
			ClientThread clientThread=new ClientThread(socket);
			clientThread.start();
			//3.不断向服务器发送信息	
			Scanner input = new Scanner(System.in);
			String str = null;
			PrintWriter pw = null;
			while(true){
				str = input.next();
				pw=new PrintWriter(socket.getOutputStream(),true);//将输出流包装为打印流									
				pw.println(InetAddress.getLocalHost().getHostName()+"说："+str+"\n");						
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
