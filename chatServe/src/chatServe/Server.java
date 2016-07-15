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
		//�ȴ��û�����
		serverSocket = new ServerSocket(5000);
		System.out.println("��������");
		
		while (true) {
					Socket socket = serverSocket.accept();
					System.out.println("��������");
		            //�������ӵĿͻ��˴����б�
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
		//����socket��Ϣ
		this.user = user;
		this.users = users;
		
		//���Ӵ���
		InputStream is = this.user.getInputStream();
		reader = new BufferedReader(new InputStreamReader(is));
	}
	
	public void run(){
		try{
			String message = null;
			while(true){
				//�ͻ�������
				if((message = reader.readLine()) != null){
					for(Socket socket:users){
						//����������ݴ�����ͬ�Ŀͻ���
						if(user != socket){
							PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);  
	                        writer.println(message);
						}
					}
					//��������������
					System.out.println(message);
				}
			}
		} catch(SocketException ex){
			//���������쳣
			users.remove(user);
		} catch(IOException ex){
			//���������쳣
			ex.printStackTrace();
		} 
	}
}


