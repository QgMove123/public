package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private ServerSocket ssocket = null;
	private boolean bStart = false; //标记是否连接服务器
	
	List<Client> clients = new ArrayList<Client>();//建立Client类数组
	
	public void MainServer() {
		try {
			ssocket = new ServerSocket(8888);//设置端口值
			bStart = true;//连接成功
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while(bStart) {
				System.out.println("等待新用户用户连接...");
				Socket socket = ssocket.accept();//等待客户端连接
				System.out.println("服务器套接字创建成功！");
				Client client = new Client(socket);
				clients.add(client);//添加客户进入Client类数组
				
				System.out.println(socket.getInetAddress().getHostAddress()
						+ " connected " + clients.size() + " clients\n");
				new Thread(client).start();//开启此线程
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ssocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		Server server = new Server();
		server.MainServer();
	}
	
	private class Client implements Runnable {
		DataInputStream myin = null;//定义输入流
		DataOutputStream myout = null;//定义输出流
		
		Socket socket = null;
		boolean bStart = false;
		
		Client(Socket socket) {
			this.socket = socket;
			try {
				myin = new DataInputStream(socket.getInputStream());//获取客户端输入流
				myout = new DataOutputStream(socket.getOutputStream());//获取客户端输出流
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			bStart = true;
		}
		
		//发送给每一个客户端
		public void sentToEveryClient(String str) {
			try {
				myout.writeUTF(str);
				myout.flush();
			} catch (IOException e) {
				// TODO: handle exception
				clients.remove(this);
				System.out.println(socket.getInetAddress().getHostAddress()
						+ " exited " + clients.size() + " clients\n");
				System.out.println("对方退出了！我从List里面去掉了！");
			}
		}
		
		public void run() {
			try {
				while (bStart) {
					String str = myin.readUTF();//读取输入流信息
					/*System.out.println("客户端：" + str);*/
					for(int i=0; i<clients.size(); i++) {
						Client client = clients.get(i);
					/* 当当前客户段对象不为发送信息自己者时才发送信息*/
						if(client.socket != this.socket){
							client.sentToEveryClient("我的好友 :" + str);
						}
					}
				}
			} catch (EOFException e) {
				clients.remove(this);
				System.out.println(socket.getInetAddress().getHostAddress()
						+ " exited " + clients.size() + " clients\n");
				System.out.println("client closed");
			} catch (SocketException e) {
				clients.remove(this);
				System.out.println(socket.getInetAddress().getHostAddress()
						+ " exited " + clients.size() + " clients\n");
				System.out.println("client closed(unNormal)");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (socket != null)
						socket.close();
					if (myin != null)
						myin.close();
					if (myout != null)
						myout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}