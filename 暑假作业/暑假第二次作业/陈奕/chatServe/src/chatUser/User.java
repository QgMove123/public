package chatUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
	public static void main(String[] args) throws Exception {
		//连接服务器
		Socket user = new Socket("127.0.0.1", 5000);
		//创建线程输入信息
		new MyThread(user).start();
		//连接串流
		InputStream is = user.getInputStream();
		BufferedReader read = new BufferedReader(new InputStreamReader(is));
		
		while (true) {
			//输出内容
			String message = read.readLine();
			System.out.println(message);
		}
	}
}

class MyThread extends Thread {
	Socket user;
	
	public MyThread(Socket user) {
		super();
		//存取socket信息
        this.user = user;
	}
	
	public void run() {
		try {
			//连接串流并输出
			BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter write = new PrintWriter(user.getOutputStream(), true);
			
			while (true) {
	        	String message = read.readLine();
	        	write.println(message);
	        }
		} catch(IOException ex) {
			//处理IO异常
			ex.printStackTrace();
		}
	}
}