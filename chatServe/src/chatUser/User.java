package chatUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class User {
	public static void main(String[] args) throws Exception {
		//���ӷ�����
		Socket user = new Socket("127.0.0.1", 5000);
		//�����߳�������Ϣ
		new MyThread(user).start();
		//���Ӵ���
		InputStream is = user.getInputStream();
		BufferedReader read = new BufferedReader(new InputStreamReader(is));
		
		while (true) {
			//�������
			String message = read.readLine();
			System.out.println(message);
		}
	}
}

class MyThread extends Thread {
	Socket user;
	
	public MyThread(Socket user) {
		super();
		//��ȡsocket��Ϣ
        this.user = user;
	}
	
	public void run() {
		try {
			//���Ӵ��������
			BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter write = new PrintWriter(user.getOutputStream(), true);
			
			while (true) {
	        	String message = read.readLine();
	        	write.println(message);
	        }
		} catch(IOException ex) {
			//����IO�쳣
			ex.printStackTrace();
		}
	}
}