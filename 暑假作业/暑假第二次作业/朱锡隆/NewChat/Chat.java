package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class Chat { // ������MyTcp
	private BufferedReader reader; // ����BufferedReader����
	private ServerSocket server; // ����ServerSocket����
	private Socket socket; // ����Socket����socket
	public List<Socket> list = new ArrayList<>();
	InetAddress ip;
	
	class ThreadTest extends Thread{
		private BufferedReader rd; // ����BufferedReader����
		private Socket st; // ����Socket����socket
		private PrintWriter wt;
		public String message;
		ThreadTest(BufferedReader reader,Socket socket){
			rd = reader;
			st = socket;
		}
		public void run(){
			try {
				ip = InetAddress.getLocalHost();
				while (true) { // ����׽���������״̬
					rd = new BufferedReader(new InputStreamReader(st
							.getInputStream())); // ʵ����BufferedReader����
					getClientMessage(); // ����getClientMessage()����
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
				while (true) { // ����׽���������״̬
					try{
						if ((message=rd.readLine())!=null) {
							// ��ÿͻ�����Ϣ
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
				e.printStackTrace(); // ����쳣��Ϣ
			}
			try {
				if (rd != null) {
					rd.close(); // �ر���
				}
				if (st != null) {
					st.close(); // �ر��׽���
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
			server = new ServerSocket(8998); // ʵ����Socket����
			System.out.println("�������׽����Ѿ������ɹ�"); // �����Ϣ
			System.out.println("�ȴ��ͻ���������"); // �����Ϣ
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	void getserver() {
		try {
			while (true) { // ����׽���������״̬
				socket = server.accept(); // ʵ����Socket����
				list.add(socket);
				new ThreadTest(reader,socket).start();
			}
		} catch (Exception e) {
			e.printStackTrace(); // ����쳣��Ϣ
		}
	}
		
		
	public static void main(String[] args) { // ������
		
		Chat tcp = new Chat(); // �����������
		tcp.getserver(); // ���÷���
	}
}

