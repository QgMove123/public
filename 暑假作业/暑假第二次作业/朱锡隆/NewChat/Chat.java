package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Chat { // 创建类MyTcp
	private BufferedReader reader; // 创建BufferedReader对象
	private ServerSocket server; // 创建ServerSocket对象
	private Socket socket; // 创建Socket对象socket
	public List<Socket> list = new ArrayList<>();
	InetAddress ip;
	
	class ThreadTest extends Thread{
		private BufferedReader rd; // 创建BufferedReader对象
		private Socket st; // 创建Socket对象socket
		private PrintWriter wt;
		public String message;
		ThreadTest(BufferedReader reader,Socket socket){
			rd = reader;
			st = socket;
		}
		public void run(){
			try {
				ip = InetAddress.getLocalHost();
				while (true) { // 如果套接字是连接状态
					rd = new BufferedReader(new InputStreamReader(st
							.getInputStream())); // 实例化BufferedReader对象
					getClientMessage(); // 调用getClientMessage()方法
				}
			} catch (Exception k) {
				try{
					rd.close();
					}catch(Exception e){
						e.printStackTrace();
					}
			}
		}
		private void getClientMessage() {
			try {
				while (true) { // 如果套接字是连接状态
					try{
						if ((message=rd.readLine())!=null) {
							// 获得客户端信息
							for(int i=0;i<list.size();i++){
								try{
									if(!list.get(i).equals(st)){
										wt = new PrintWriter(list.get(i).getOutputStream(),true);
										wt.println(ip.getHostName()+" : "+message);
									}
								}catch(Exception e){
									list.remove(i);
								}
							}
							System.out.println(ip.getHostName()+" : "+ message);
						}else break;
					}catch(Exception e){
						rd.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace(); // 输出异常信息
			}
			try {
				if (rd != null) {
					rd.close(); // 关闭流
				}
				if (st != null) {
					st.close(); // 关闭套接字
				}
				if (wt != null){
					wt.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	Chat(){
		try{
			server = new ServerSocket(8998); // 实例化Socket对象
			System.out.println("服务器套接字已经创建成功"); // 输出信息
			System.out.println("等待客户机的连接"); // 输出信息
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	void getserver() {
		try {
			while (true) { // 如果套接字是连接状态
				socket = server.accept(); // 实例化Socket对象
				list.add(socket);
				new ThreadTest(reader,socket).start();
			}
		} catch (Exception e) {
			e.printStackTrace(); // 输出异常信息
		}
	}
		
		
	public static void main(String[] args) { // 主方法
		
		Chat tcp = new Chat(); // 创建本类对象
		tcp.getserver(); // 调用方法
	}
}

