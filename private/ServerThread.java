package com.qg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/*
 * �������̴߳�����
 */
public class ServerThread extends Thread {
	
	Socket socket;// �ͱ��߳���ص�Socket
	
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	//�߳�ִ�еĲ�������Ӧ�ͻ��˵�����
	public void run(){
		try {	
			//��ʾ��Ϣ		
			Server.sendMessage("***"+socket.getInetAddress().getHostName()+"����QG Moblie������***");
			BufferedReader br = null;
			while (true) {
				// ��ȡ�ͻ���д�������			
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));			
				String str = null;
				str = br.readLine();
				//֪ͨ����������Ϣ���������пͻ���
				if(!str.equals("") || str!=null){
					Server.sendMessage(str);
				}			
			}
		} catch (IOException e) {		
			//���û�ǿ���˳�ʱ
			//�ر���Դ				
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			//��socket��List��ɾȥ
			Server.removeSoc(socket);					
			//֪ͨ����ͻ����пͻ��˳�����
			Server.sendMessage("***"+socket.getInetAddress().getHostName()+"�˳�QG Moblie������***");
			//�������߳�
			return;
		}	
	}
}
