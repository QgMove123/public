package com.qg;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
/*
 * �ͻ���
 */
public class Client {		
	public static void main(String[] args)  {	
		try {
			//1.�����ͻ���Socket��ָ����������ַ�Ͷ˿�
			Socket socket = new Socket("localhost", 8888);
			//2.�����ͻ����̣߳�ʵʱ��ȡ��������Ϣ
			ClientThread clientThread=new ClientThread(socket);
			clientThread.start();
			//3.�����������������Ϣ	
			Scanner input = new Scanner(System.in);
			String str = null;
			PrintWriter pw = null;
			while(true){
				str = input.next();
				pw=new PrintWriter(socket.getOutputStream(),true);//���������װΪ��ӡ��									
				pw.println(InetAddress.getLocalHost().getHostName()+"˵��"+str+"\n");						
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
