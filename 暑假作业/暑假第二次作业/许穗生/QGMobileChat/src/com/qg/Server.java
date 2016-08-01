package com.qg;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
 * 基于TCP协议的Socket通信
 * 服务器端
 */
public class Server {
	
	static List<Socket> socketList = new ArrayList<Socket>();
	
	public static void main(String[] args) {
		try {
			//1.创建一个服务器端Socket，即ServerSocket，指定绑定的端口
			ServerSocket serverSocket=new ServerSocket(8888);
			Socket socket=null;	

			System.out.println("***服务器启动，等待客户端的连接***");
			//2.循环监听等待客户端的连接
			while(true){
				//调用accept()方法开始监听，等待客户端的连接
				socket=serverSocket.accept();			
				socketList.add(socket);
				//创建一个新的线程
				ServerThread serverThread=new ServerThread(socket);
				//启动线程
				serverThread.start();
				
				//统计客户端的数量
				System.out.println("客户端的数量："+socketList.size());
				System.out.println("当前客户端的IP："+socket.getInetAddress().getHostAddress());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public static void sendMessage(String str){	
		try {
			PrintWriter pw=null;
			for (Socket socket : socketList) {	
				pw = new PrintWriter(socket.getOutputStream(),true);
				pw.println(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public static void removeSoc(Socket socket){
		socketList.remove(socket);
		System.out.println("客户端的数量："+socketList.size());
		System.out.println("退出客户端的IP："+socket.getInetAddress().getHostAddress());
	}
}
