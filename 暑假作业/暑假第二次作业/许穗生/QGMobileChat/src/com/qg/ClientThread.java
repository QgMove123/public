package com.qg;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/*
 * 客户端线程处理类
 */
public class ClientThread extends Thread{
		// 和本线程相关的Socket
		Socket socket;
		
		public ClientThread(Socket socket) {
			this.socket = socket;
		}
		
	public void run() {
		try {
			//获取输入流，并读取服务器端的响应信息
			BufferedReader br = null;
			while(true){
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String str = br.readLine();
				System.out.println(str);			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
