package com.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.client.Client;
import com.model.*;

public class View {
	static BufferedReader bReader = new BufferedReader(
			new InputStreamReader(System.in));
	static String line = null;
	//主菜单页面
	public static int showMenu() throws IOException {
		System.out.println("|*丑团外卖*|");
		System.out.println("[1] 用户注册");
		System.out.println("[2] 商户注册");
		System.out.println("[3] 用户登录");
		System.out.println("[4] 商户登陆");
		System.out.print("请输入你的选择：");
		
		line = bReader.readLine();
		return Integer.valueOf(line);
	}
	
	//结束语
	public static void showEnd() {
		System.out.println("欢迎下次再次使用");
		System.exit(0);
	}
	
	//注册 ，flag： 0 表示用户， 1表示商户
	public static void register(int flag, Common com) throws IOException {
		com.setStatus(1);//status 2  代表信息包状态为注册
		com.setFlag(flag);
		System.out.println("|***注册***|");
		if (flag == 0) {
			com.setObject(new Usercustom());
			Usercustom uc = (Usercustom)com.getObject();
			System.out.print("请输入账号：");
			uc.setAccount(bReader.readLine());
			System.out.print("请输入密码：");
			uc.setPassword(bReader.readLine());
			System.out.print("请输入您的名字：");
			uc.setName(bReader.readLine());
		} else {
			com.setObject(new Userbusiness());
			Userbusiness uc = (Userbusiness)com.getObject();
			System.out.print("请输入账号：");
			uc.setAccount(bReader.readLine());
			System.out.print("请输入密码：");
			uc.setPassword(bReader.readLine());
			System.out.print("请输入您的名字：");
			uc.setName(bReader.readLine());
		}
	}
	
	//登陆 ，flag： 0 表示用户， 1表示商户
	public static void login(int flag, Common com) throws IOException {
		com.setStatus(2);//status 2  代表信息包状态为登陆
		com.setFlag(flag);
		System.out.println("|***登陆***|");
		if (flag == 0) {
			com.setObject(new Usercustom());
			Usercustom uc = (Usercustom)com.getObject();
			System.out.print("请输入账号：");
			uc.setAccount(bReader.readLine());
			System.out.print("请输入密码：");
			uc.setPassword(bReader.readLine());
		} else {
			com.setObject(new Userbusiness());
			Userbusiness ub = (Userbusiness)com.getObject();
			System.out.print("请输入账号：");
			ub.setAccount(bReader.readLine());
			System.out.print("请输入密码：");
			ub.setPassword(bReader.readLine());
		}
	}
	
	//判断登陆的信息
	public static void enterCT(Client client) throws IOException, ClassNotFoundException {
		client.myout.writeObject(client.com);
		while (true) {//当没接收到特定的返回的时候，一直循环
			Common com = (Common)client.myin.readObject();
			if (com.getStatus() == 1 || com.getStatus() == 2) {//状态1/2 表示登陆与注册
				if (com.getFlag() == 0) { //用户
					Usercustom uc = (Usercustom) com.getObject();
					if (uc.getId_custom() != -1) {
						client.com = com;
					}
					break;
				} else {
					Userbusiness ub = (Userbusiness) com.getObject();
					if (ub.getId_business() != -1) {
						client.com = com;
					}
					break;
				}
			}
		}
	}
	
	//查看菜单
	public static int showCuisine(Client client, int status2) throws IOException, ClassNotFoundException {
		Common common = new Common();
		common.setObject(new ArrayList<Cuisine>());
		common.setStatus(3);
		if (client.com.getFlag() == 0) {
			System.out.println("|****菜单****|");
			System.out.println("[1]  默认排序");
			System.out.println("[2]  销量降序");
			System.out.println("[3]  价格降序");
			System.out.println("[4]  评分降序");
			line = bReader.readLine();
			common.setFlag(Integer.valueOf(line));
		} else {
			common.setFlag(1);
		}
		//发送
		client.myout.writeObject(common);
		
		while (true) {//当没接收到特定的返回的时候，一直循环
			common = (Common)client.myin.readObject();
			if (common.getStatus() == 3) {//状态3 表示菜单信息
				@SuppressWarnings("unchecked")
				List<Cuisine> list = (List<Cuisine>) common.getObject();
				if (list.size() == 0) {
					System.out.println("无菜单信息");
					return 0;
				}
				int num = 0;//用来进行计数
				for (int i=0; i<list.size(); i++) {
					Cuisine cs = list.get(i);
					if (status2 == 0) {
						num++;
						System.out.println(
								"菜单id：" + cs.getId_cuisine() + " 菜名：" + cs.getName() + " 文字描述：" + cs.getDescribe()
										+ " 价格：" + cs.getPrice() + " 销量：" + cs.getSale() + " 评分：" + cs.getStar());
						List<String> list2 = cs.getFeedback();
						int count = 0;
						for (int j = 0; j < list2.size(); j++) { // 显示评价
							count = 0;
							if (list2.get(j) != null) { // 当评价不为空
								count++;
								System.out.println("评价 " + count + "：" + list2.get(j));
							}
						}
					} else {//商家特殊处理
						Userbusiness ub = (Userbusiness)client.com.getObject();
						if (cs.getId_business() == ub.getId_business()) {
							num++;
							System.out.println(
									"菜单id：" + cs.getId_cuisine() + " 菜名：" + cs.getName() + " 文字描述：" + cs.getDescribe()
											+ " 价格：" + cs.getPrice() + " 销量：" + cs.getSale() + " 评分：" + cs.getStar());
							List<String> list2 = cs.getFeedback();
							int count = 0;
							for (int j = 0; j < list2.size(); j++) { // 显示评价
								count = 0;
								if (list2.get(j) != null) { // 当评价不为空
									count++;
									System.out.println("评价 " + count + "：" + list2.get(j));
								}
							}
						}
					}
				}
				return num;//退出
			}
		}
	}
	
	//上架菜式
	public static void addCuisine(Client client) throws IOException, ClassNotFoundException {
		Common common = new Common();
		common.setObject(new Cuisine());
		common.setStatus(7);//status 7：表示上架菜式
		Cuisine cs = (Cuisine) common.getObject();
		Userbusiness ub = (Userbusiness) client.com.getObject();
		//菜式属性设置
		cs.setId_business(ub.getId_business());
		System.out.print("请输入菜名：");
		cs.setName(bReader.readLine());
		System.out.print("请简单描述菜式：");
		cs.setDescribe(bReader.readLine());
		System.out.print("请设置价格：");
		cs.setPrice(Float.valueOf(bReader.readLine()));
		//传输数据
		client.myout.writeObject(common);
		System.out.println("添加成功!\n");
	}
	
	//下架菜式
	public static void deleteCuisine(Client client) throws IOException, ClassNotFoundException {
		if (View.showCuisine(client, 1) > 0 ) {
			Common common = new Common();
			common.setStatus(8);
			System.out.print("请输入要下架菜式的id：");
			common.setFlag(Integer.valueOf(bReader.readLine()));
			client.myout.writeObject(common);
			
			View.showCuisine(client, 1);
		} else {
			System.out.println("无可下架菜式");
		}
	}
	
	//修改菜式
	public static void modefyCuisine(Client client) throws NumberFormatException, ClassNotFoundException, IOException {
		if (View.showCuisine(client, 1) > 0 ) {
			Common common = new Common();
			common.setStatus(9);
			common.setObject(new Cuisine());
			Cuisine cs = (Cuisine) common.getObject();
			System.out.print("请输入要修改菜式的id：");
			cs.setId_cuisine(Integer.valueOf(bReader.readLine()));
			System.out.print("请输入新的菜名：");
			cs.setName(bReader.readLine());
			System.out.print("请输入新的描述：");
			cs.setDescribe(bReader.readLine());
			System.out.print("请输入新的价格：");
			cs.setPrice(Float.valueOf(bReader.readLine()));
			
			client.myout.writeObject(common);
			View.showCuisine(client, 1);
		} else {
			System.out.println("无可修改菜式信息");
		}
	}
	
	//查看订单
	public static int showOrder(Client client, int status2) throws IOException, ClassNotFoundException {
		Common com = new Common();
		com.setObject(new ArrayList<Order>());
		com.setStatus(10);
		
		client.myout.writeObject(com);
		
		while (true) {//当没接收到特定的返回的时候，一直循环
			com = (Common)client.myin.readObject();
			if (com.getStatus() == 10) {
				@SuppressWarnings("unchecked")
				List<Order> list = (ArrayList<Order>) com.getObject();
				if (list.size() == 0) {
					System.out.println("无订单信息");
					return 0;
				}
				if (client.com.getFlag() == 0) {
					Usercustom uc = (Usercustom) client.com.getObject();
					int id = uc.getId_custom();
					int count = 0;//用来进行计数
					for (int i=0; i<list.size(); i++) {
						Order od = list.get(i);
						if (od.getId_custom() == id && status2 == 0) {
							count++;
							System.out.print("订单：" + od.getId_order() + " 商家名：" + od.getBusinessname() 
									+ " 菜名：" + od.getName() + " 订餐数量：" + od.getNum() + " 送达时间：" + od.getTime() 
									+ " 送达地点：" + od.getSpot() + " 订单状态：");
							switch (od.getStatus()) {
							case 0:
								System.out.println("待处理");
								break;
							case 1:
								System.out.println("待评价");
								break;
							case 2:
								System.out.println("被拒单");
								break;
							case 3:
								System.out.println("已处理");
								break;
							}
						} else if (od.getStatus() == 1){//专门用于待评价的状态
							count++;
							System.out.println("订单：" + od.getId_order() + " 商家名：" + od.getBusinessname() 
									+ " 菜名：" + od.getName() + " 订餐数量：" + od.getNum() + " 送达时间：" + od.getTime() 
									+ " 送达地点：" + od.getSpot() + " 订单状态：待评价");
						}
					}
					return count;//退出
				} else {
					Userbusiness uc = (Userbusiness) client.com.getObject();
					String name = uc.getName();
					int count = 0;//用来进行计数
					for (int i=0; i<list.size(); i++) {
						Order od = list.get(i);
						if ((od.getBusinessname()).equals(name) && od.getStatus() == 0) {
							count++;
							System.out.println("订单：" + od.getId_order() + " 商家名：" + od.getBusinessname() 
									+ " 菜名：" + od.getName() + " 订餐数量：" + od.getNum() + " 送达时间：" + od.getTime() 
									+ " 送达地点：" + od.getSpot() + " 订单状态：未处理");
						}
					}
					return count;
				}
			}
		}
	}
	
	//添加订单
	public static void addOrder(Client client) throws IOException, ClassNotFoundException {
		if (View.showCuisine(client, 0) > 0 ) {
			Common common = new Common();
			common.setStatus(5);
			common.setObject(new Order());
			Order od = (Order) common.getObject();
			Usercustom uc = (Usercustom) client.com.getObject();
			od.setId_custom(uc.getId_custom());//获得客户id
			od.setStatus(0);//设置订单状态
			System.out.print("请输入要添加菜式的id：");
			od.setId_cuisine(Integer.valueOf(bReader.readLine()));
			System.out.print("请输入购买数量：");
			od.setNum(Integer.valueOf(bReader.readLine()));
			System.out.print("请输入送达地点：");
			od.setSpot(bReader.readLine());
			System.out.print("请输入送达时间（HH:MM）：");
			od.setTime(bReader.readLine());
			
			client.myout.writeObject(common);
			System.out.println("添加订单成功！");
		} else {
			System.out.println("无菜式信息");
		}
	}
	
	//评价订单（客户）
	public static void dealtCustomOrder(Client client) throws ClassNotFoundException, IOException {
		if (View.showOrder(client, 1) > 0 ) {
			Common com = new Common();
			com.setObject(new Order());
			com.setStatus(4);
			Order od = (Order) com.getObject();
			od.setStatus(3);

			System.out.print("请输入你要评价的订单号：");
			od.setId_order(Integer.valueOf(bReader.readLine()));
			System.out.print("请输入评价：");
			od.setFeedback(bReader.readLine());
			System.out.print("请输入评分：");
			bReader.readLine();
			System.out.println("处理成功！");
			
			client.myout.writeObject(com);
		} else {
			System.out.println("无可评价订单");
		}
	}
	
	//下载日志
	public static void downloadLog(Client client) throws IOException, ClassNotFoundException {
		Common com = new Common();
		com.setObject(new ArrayList<String>());
		com.setStatus(6);
		//发送
		client.myout.writeObject(com);
		//接收
		com = null;
		com = (Common) client.myin.readObject();
		@SuppressWarnings("unchecked")
		List<String> list = (ArrayList<String>) com.getObject();
		for (int i=0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
	//处理订单（商家）
	public static void dealtBusinessOrder(Client client) throws ClassNotFoundException, IOException, Exception {
		if (View.showOrder(client, 0) > 0) {
			Common com = new Common();
			com.setObject(new Order());
			com.setStatus(4);
			Order od = (Order) com.getObject();
			System.out.print("请输入要处理的订单：");
			od.setId_order(Integer.valueOf(bReader.readLine()));
			System.out.print("请问你是接受还是拒绝？回复1或者2");
			if (Integer.valueOf(bReader.readLine()) == 1) {
				od.setStatus(1);
			} else {
				od.setStatus(2);
			}
			
			client.myout.writeObject(com);
			
			System.out.print("正在送餐->");
			for(int i=0; i<5; i++) {
				System.out.print(".");
				Thread.sleep(500);
			}
			System.out.println("->送餐成功");
		} else {
			System.out.println("无可处理订单");
		}
	}
	
	//用户主页
	public static void customMenu(Client client) throws IOException, ClassNotFoundException {
		Usercustom uc = (Usercustom) client.com.getObject();
		//检查登陆是否成功
		if (uc.getId_custom() == -1)
			return;
		while (true) {
			System.out.println("|**丑团外卖**|");
			System.out.println("[1]  查看菜单");
			System.out.println("[2]  添加订单");
			System.out.println("[3]  查看订单");
			System.out.println("[4]  评价订单");
			System.out.println("[5]  退出丑团");
			System.out.print("请输入你的选择：");
			line = bReader.readLine();
			
			switch (Integer.valueOf(line)) {
			case 1:
				View.showCuisine(client, 0);
				break;
			case 2:
				View.addOrder(client);
				break;
			case 3:
				View.showOrder(client, 0);
				break;
			case 4:
				View.dealtCustomOrder(client);
				break;
			case 5:
				System.exit(0);
			default:
				break;
			}
		}
	}
	
	//商户主页
	public static void businessMenu(Client client) throws Exception {
		Userbusiness ub = (Userbusiness) client.com.getObject();
		//检查登陆是否成功
		if (ub.getId_business() == -1) {
			return;
		}
		while (true) {
			System.out.println("|**丑团外卖**|");
			System.out.println("[1]  查看菜单");
			System.out.println("[2]  处理订单");
			System.out.println("[3]  上架菜式");
			System.out.println("[4]  下架菜式");
			System.out.println("[5]  修改菜式");
			System.out.println("[6]  下载日志");
			System.out.println("[7]  退出丑团");
			System.out.print("请输入你的选择：");
			line = bReader.readLine();
			
			switch (Integer.valueOf(line)) {
			case 1:
				View.showCuisine(client, 0);
				break;
			case 2:
				View.dealtBusinessOrder(client);
				break;
			case 3:
				View.addCuisine(client);
				break;
			case 4:
				View.deleteCuisine(client);
				break;
			case 5:
				View.modefyCuisine(client);
				break;
			case 6:
				View.downloadLog(client);
				break;
			case 7:
				System.exit(0);
				break;
			default:
				break;
			}
		}
	}
}