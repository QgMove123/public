package clien;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class ThreadTest extends Thread{
	public BufferedReader reader;
	Socket st;
	String ss;
	ThreadTest(Socket socket){
		st = socket;
	}
	public void run(){
		try{
			while(true){
				reader = new BufferedReader(new InputStreamReader(st
						.getInputStream())); // ʵ����BufferedReader����
				if((ss=reader.readLine()) != null) System.out.println(ss);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
public class Clien {
	String str;
	public PrintWriter writer;
	Socket socket;
	public void connect(){
		try{
			if(writer!=null) writer.close();
			if(socket!=null)socket.close();
			socket = new Socket("127.0.0.1",8998);
			Scanner scan = new Scanner(System.in);
			new ThreadTest(socket).start();
			while(true){
				str = scan.nextLine();
				writer = new PrintWriter(socket.getOutputStream(),true);
				writer.println(str);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		Clien clien = new Clien();
		clien.connect();
	}
}
