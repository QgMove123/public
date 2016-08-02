package com.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.model.Common;
import com.view.View;

public class Client extends Thread{
	private Socket socket;
	public ObjectInputStream myin;
	public ObjectOutputStream myout;
	public Common com;
	
	//�޲ι��췽������ʼ��������������
	public Client() {
		try {
			socket = new Socket("127.0.0.1", 50000);
			
			/*��socket�������������*/
			myin = new ObjectInputStream(socket.getInputStream());
			myout = new ObjectOutputStream(socket.getOutputStream());
			
		} catch(BindException e) {
			System.out.println("�Ҳ���ָ���ķ�����");
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	/*@Override
	//�������ת��
	public void run() {
		try {
			while(true){
				Common com = (Common)myin.readObject();
			}
		} catch (SocketException e) {
			System.out.println("�������ܾ�����");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	//�Ͽ�����
	private void disConnect() {
		try {
			if (socket != null) {
				socket.close();
			}

			if (myout != null) {
				myout.close();
			}

			if (myin != null) {
				myin.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		Client client = new Client();
		client.com = new Common();
		boolean iscustomo = true;// true Ϊ�û�
		try {
			switch (View.showMenu()) {
			case 1:
				View.register(0, client.com);
				break;
			case 2:
				View.register(1, client.com);
				iscustomo = false;
				break;
			case 3:
				View.login(0, client.com);
				break;
			case 4:
				View.login(1, client.com);
				iscustomo = false;
				break;
			default:
				View.showEnd();
			}
			View.enterCT(client);//�ḻ��½����ע�����Ϣ
			if (iscustomo) {
				View.customMenu(client);
			} else {
				View.businessMenu(client);
			}
			client.disConnect();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}