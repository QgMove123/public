package com.qg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/*
 * 服务器线程处理类
 */
public class ServerThread extends Thread {
	
	Socket socket;// 和本线程相关的Socket
	
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	//线程执行的操作，响应客户端的请求
	public void run(){
		try {	
			//提示信息		
			Server.sendMessage("***"+socket.getInetAddress().getHostName()+"进入QG Moblie聊天室***");
			BufferedReader br = null;
			while (true) {
				// 读取客户端写入的数据			
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));			
				String str = null;
				str = br.readLine();
				//通知服务器将消息发布给所有客户端
				if(!str.equals("") || str!=null){
					Server.sendMessage(str);
				}			
			}
		} catch (IOException e) {		
			//当用户强制退出时
			//关闭资源				
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			//将socket从List中删去
			Server.removeSoc(socket);					
			//通知其余客户端有客户退出聊天
			Server.sendMessage("***"+socket.getInetAddress().getHostName()+"退出QG Moblie聊天室***");
			//结束该线程
			return;
		}	
	}
}
