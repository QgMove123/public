package com.qg;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/*
 * �ͻ����̴߳�����
 */
public class ClientThread extends Thread{
		// �ͱ��߳���ص�Socket
		Socket socket;
		
		public ClientThread(Socket socket) {
			this.socket = socket;
		}
		
	public void run() {
		try {
			//��ȡ������������ȡ�������˵���Ӧ��Ϣ
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
