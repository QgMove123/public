package com.qg;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*
 * ����TCPЭ���Socketͨ��
 * ��������
 */
public class Server {
	
	static List<Socket> socketList = new ArrayList<Socket>();
	
	public static void main(String[] args) {
		try {
			//1.����һ����������Socket����ServerSocket��ָ���󶨵Ķ˿�
			ServerSocket serverSocket=new ServerSocket(8888);
			Socket socket=null;	

			System.out.println("***�������������ȴ��ͻ��˵�����***");
			//2.ѭ�������ȴ��ͻ��˵�����
			while(true){
				//����accept()������ʼ�������ȴ��ͻ��˵�����
				socket=serverSocket.accept();			
				socketList.add(socket);
				//����һ���µ��߳�
				ServerThread serverThread=new ServerThread(socket);
				//�����߳�
				serverThread.start();
				
				//ͳ�ƿͻ��˵�����
				System.out.println("�ͻ��˵�������"+socketList.size());
				System.out.println("��ǰ�ͻ��˵�IP��"+socket.getInetAddress().getHostAddress());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public static void sendMessage(String str){	
		try {
			PrintWriter pw=null;
			for (Socket socket : socketList) {	
				pw = new PrintWriter(socket.getOutputStream(),true);
				pw.println(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public static void removeSoc(Socket socket){
		socketList.remove(socket);
		System.out.println("�ͻ��˵�������"+socketList.size());
		System.out.println("�˳��ͻ��˵�IP��"+socket.getInetAddress().getHostAddress());
	}
}
