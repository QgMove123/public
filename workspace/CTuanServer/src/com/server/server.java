package com.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.control.Control;
import com.model.Common;

public class server {
	private ServerSocket ssocket = null;
	private boolean bStart = false; //����Ƿ����ӷ�����
	
	List<Client> clients = new ArrayList<Client>();//����Client������
	
	public void MainServer() {
		try {
			ssocket = new ServerSocket(50000);//���ö˿�ֵ
			bStart = true;//���ӳɹ�
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while(bStart) {
				System.out.println("�ȴ����û��û�����...");
				Socket socket = ssocket.accept();//�ȴ��ͻ�������
				System.out.println("�������׽��ִ����ɹ���");
				Client client = new Client(socket);
				clients.add(client);//���ӿͻ�����Client������
				
				System.out.println(socket.getInetAddress().getHostAddress()
						+ " connected " + clients.size() + " clients\n");
				new Thread(client).start();//�������߳�
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
	
	private class Client implements Runnable {
		ObjectInputStream myin = null;//����������
		ObjectOutput myout = null;//���������
		
		Socket socket = null;
		boolean bStart = false;
		
		Client(Socket socket) {//�вι��췽��
			this.socket = socket;
			try {
				myout = new ObjectOutputStream(socket.getOutputStream());//��ȡ�ͻ��������
				myin = new ObjectInputStream(socket.getInputStream());//��ȡ�ͻ���������
				bStart = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//���͸�ÿһ���ͻ���
		public void sentToClient(Common com) {
			try {
				myout.writeObject(com);
				myout.flush();
			} catch (IOException e) {
				// TODO: handle exception
				clients.remove(this);
				System.out.println(socket.getInetAddress().getHostAddress()
						+ " connected " + clients.size() + " clients\n");
			}
		}
		
		public void run() {
			try {
				Control control = new Control();
				boolean send;
				int choice;
				while (bStart) {
					Common com = (Common) myin.readObject();//��ȡ��Ϣ��commom����Ϣ
					send = true;//Ĭ���ǻ�ش���Ϣ��
					choice = com.getStatus();
					switch (choice) {
					case 1:
						com = control.register(com);
						break;
					case 2:
						com = control.login(com);
						break;
					case 3:
						com = control.showCuisine(com);
						break;
					case 4:
						com = control.modefyOrder(com);
						break;
					case 5:
						control.addOrder(com);
						send = false;
						break;
					case 6:
						com = control.downloadLog(com);
						break;
					case 7:
						control.addCuisine(com);
						send = false;
						break;
					case 8:
						control.deleteCuisine(com);
						send = false;
						break;
					case 9:
						control.modefyCuisine(com);
						send = false;
						break;
					case 10:
						com = control.showOrder(com);
						break;
					default:
						break;
					}
					control.recordDaily(choice);
					if (send == true) {
						this.sentToClient(com);
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
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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
	
	public static void main(String args[]) {
		server server = new server();
		server.MainServer();
	}
}