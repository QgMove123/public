package user;

import java.io.*;
import java.net.*;

public class User {
	
	Socket socket = null;
	DataOutputStream myout = null;
	DataInputStream myin = null;

	Thread t = new Thread(new RecToServer());
	
	//连接服务器
	private void Connect() {
		try {
			
			socket = new Socket("127.0.0.1", 8888);
			
			/*绑定socket的输入与输出流*/
			myout = new DataOutputStream(socket.getOutputStream());
			myin = new DataInputStream(socket.getInputStream());
			
		} catch(BindException e) {
			System.out.println("找不到指定的服务器");
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//断开连接
	public void disConnect() {
		try {
			if(socket != null) {
				socket.close();
			}
			
			if(myout != null) {
				myout.close();
			}
			
			if(myin != null) {
				myin.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		User user = new User();
		String line = null; 
		//绑定输入流
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		user.Connect();
		user.t.start();//启动线程
         
        try {
			while ((line = bufferedReader.readLine()) != null) {//当字符串不为空时
				if(line.equals("I leave!")){//当字符串为 I leave时，退出！
					break;
				}
			    user.myout.writeUTF(line);
			}
		} catch (SocketException e) {
			System.out.println("服务器已断开连接！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        user.disConnect();//断开连接
	}
	
	private class RecToServer implements Runnable {
		public void run() {
			try {
				while(true){
					String str = myin.readUTF();//读取服务器传来的信息
					System.out.println(str);
				}
			} catch (SocketException e) {
				System.out.println("服务器拒绝访问");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
