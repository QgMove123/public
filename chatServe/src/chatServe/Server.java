package chatServe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
	public static ArrayList<Socket> socketList = new ArrayList<Socket>();
	private static ServerSocket serverSocket;
	static Socket socket;
	
	public static void main(String[] args) throws Exception{
		//等待用户请求
		serverSocket = new ServerSocket(5000);
		System.out.println("服务启动");
		
		while (true) {
					Socket socket = serverSocket.accept();
					System.out.println("服务连接");
		            //将已连接的客户端存入列表
		            socketList.add(socket);
		        	new MyThread(socket, socketList).start();
		}
	}
}

class MyThread extends Thread {
	Socket user;
	ArrayList<Socket> users;
	
	BufferedReader reader;
	
	public MyThread(Socket user, ArrayList<Socket> users)throws Exception {
		super();
		//复制socket信息
		this.user = user;
		this.users = users;
		
		//连接串流
		InputStream is = this.user.getInputStream();
		reader = new BufferedReader(new InputStreamReader(is));
	}
	
	public void run(){
		try{
			String message = null;
			while(true){
				//客户端输入
				if((message = reader.readLine()) != null){
					for(Socket socket:users){
						//将输入的内容传给不同的客户端
						if(user != socket){
							PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);  
	                        writer.println(message);
						}
					}
					//将输入的内容输出
					System.out.println(message);
				}
			}
		} catch(SocketException ex){
			//出现输入异常
			users.remove(user);
		} catch(IOException ex){
			//出现输入异常
			ex.printStackTrace();
		} 
	}
}


