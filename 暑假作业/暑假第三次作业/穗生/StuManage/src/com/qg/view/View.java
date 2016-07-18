package com.qg.view;

import java.util.List;
import java.util.Scanner;

import com.qg.control.StuControl;
import com.qg.model.Stu;

public class View {

	private static final String CONTEXT = "欢迎进入学生成绩管理系统：\n" + "下面是系统的功能列表：\n"
			+ "[MAIN/M]:主菜单\n" + "[QUERY/Q]:查看全部学生的信息\n"
			+ "[GET/G]:查看某同学的全部信息\n" + "[ADD/A]:增加某一同学信息\n"
			+ "[UPDATE/U]:修改同学信息\n" + "[SEARCH/S]:按学科成绩进行范围查找\n"
			+ "[DELETE/D]:删除某一同学信息\n" + "[EXIT/E]:退出系统\n";

	private static final String OPERATION_MAIN = "MAIN";
	private static final String OPERATION_QUERY = "QUERY";
	private static final String OPERATION_GET = "GET";
	private static final String OPERATION_ADD = "ADD";
	private static final String OPERATION_UPDATE = "UPDATE";
	private static final String OPERATION_SEARCH = "SEARCH";
	private static final String OPERATION_DELETE = "DELETE";
	private static final String OPERATION_EXIT = "EXIT";

	public static void main(String[] args) throws Exception {
		System.out.println(CONTEXT);

		Scanner scan = new Scanner(System.in);
		StuControl control = new StuControl();
		Stu g = null;
		String pervious = null;
		int step = 1;
		String c = null;// 科目;

		while (true) {
			String in = scan.next();
			if (OPERATION_EXIT.equals(in.toUpperCase())
					|| OPERATION_EXIT.substring(0, 1).equals(in.toUpperCase())) {
				System.out.println("您已成功退出系统");
				break;
			} else if (pervious == null
					&& (OPERATION_MAIN.equals(in.toUpperCase()) || OPERATION_MAIN
							.substring(0, 1).equals(in.toUpperCase()))) {
				pervious = null;
				step = 1;
				g = null;
				System.out.println(CONTEXT);
			} else if (OPERATION_QUERY.equals(in.toUpperCase())
					|| OPERATION_QUERY.substring(0, 1).equals(in.toUpperCase())) {
				try {
					List<Stu> list = control.query();
					for (Stu stu : list) {
						System.out.println("编号:" + stu.getId() + " , " + "姓名:"
								+ stu.getName());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("没有任何学生信息");
					pervious = null;
				}
			} else if (OPERATION_GET.equals(in.toUpperCase())
					|| OPERATION_GET.substring(0, 1).equals(in.toUpperCase())
					|| OPERATION_GET.equals(pervious)) {
				pervious = OPERATION_GET;
				if (1 == step) {
					System.out.println("请输入查询的学生编号：");
				} else if (step > 1) {
					try {
						g = control.get((int) Integer.valueOf(in));
						if (g == null) {
							System.out.println("查询学生信息失败");
							pervious = null;
							step = 0;
						} else {
							System.out.println(g.toString());
							pervious = null;
							step = 1;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("请输入正确的学生编号");
						step = 2;
					}
				}
				step++;

			} else if (OPERATION_ADD.equals(in.toUpperCase())
					|| OPERATION_ADD.substring(0, 1).equals(in.toUpperCase())
					|| OPERATION_ADD.equals(pervious)) {
				pervious = OPERATION_ADD;
				if (1 == step) {
					System.out.println("请输入学生的信息[专业]：");
				} else if (2 == step) {
					g = new Stu();
					g.setMajor(in);
					System.out.println("请输入学生的信息[班级]：");
				} else if (3 == step) {
					g.setGrade(in);
					System.out.println("请输入学生的信息[姓名]：");
				} else if (4 == step) {
					g.setName(in);
					System.out.println("请输入学生的信息[性别]：");
				} else if (5 == step) {
					g.setSex(in);
					System.out.println("请输入学生的信息[学号]：");
				} else if (6 == step) {
					g.setNumber(in);
					System.out.println("请输入学生的信息[语文]：");
				} else if (7 == step) {
					try {
						g.setChinese(Double.valueOf(in));
						System.out.println("请输入学生的信息[数学]：");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("请输入正确的学生成绩");
						step = 6;
					}
				} else if (8 == step) {
					try {
						g.setMaths(Double.valueOf(in));
						System.out.println("请输入学生的信息[英语]：");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("请输入正确的学生成绩");
						step = 7;
					}

				} else if (9 == step) {
					try {
						g.setEnglish(Double.valueOf(in));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						System.out.println("请输入正确的学生成绩");
						step = 9;
						continue;
					}
					try {
						control.add(g);
						System.out.println("新增学生信息成功");
						step = 0;
						pervious = null;
					} catch (Exception e) {
						System.out.println("新增学生信息失败");
					}
				}
				step++;

			} else if (OPERATION_UPDATE.equals(in.toUpperCase())
					|| OPERATION_UPDATE.substring(0, 1)
							.equals(in.toUpperCase())
					|| OPERATION_UPDATE.equals(pervious)) {
				pervious = OPERATION_UPDATE;
				if (1 == step) {
					System.out.println("请输入要修改的学生编号：");
				} else if (2 == step) {
					try {
						g = control.get((int) Integer.valueOf(in));
						if (g == null) {
							System.out.println("修改学生信息失败");
							step = 0;
							pervious = null;
						}
					} catch (Exception e) {
						System.out.println("请输入正确的学生编号：");
						step = 1;
					}
					System.out.println("请输入新的学生信息[专业]，如果不修改该值，请输入0：");
				} else if (3 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setMajor(in);
					}
					System.out.println("请输入新的学生信息[班级]，如果不修改该值，请输入0：");
				} else if (4 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setGrade(in);
					}
					System.out.println("请输入新的学生信息[姓名]，如果不修改该值，请输入0：");
				} else if (5 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setName(in);
					}
					System.out.println("请输入新的学生信息[性别]，如果不修改该值，请输入0：");
				} else if (6 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setSex(in);
					}
					System.out.println("请输入新的学生信息[学号]，如果不修改该值，请输入0：");
				} else if (7 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setNumber(in);
					}
					System.out.println("请输入新的学生信息[语文]，如果不修改该值，请输入0：");
				} else if (8 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						try {
							g.setChinese(Double.valueOf(in));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("请输入正确的学生成绩：");
							step = 7;
						}
					}
					System.out.println("请输入新的学生信息[数学]，如果不修改该值，请输入0：");
				} else if (9 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						try {
							g.setMaths(Double.valueOf(in));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("请输入正确的学生成绩：");
							step = 8;
						}
					}
					System.out.println("请输入新的学生信息[英语]，如果不修改该值，请输入0：");
				} else if (10 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						try {
							g.setEnglish(Double.valueOf(in));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("请输入正确的学生成绩：");
							step = 10;
							continue;
						}
					}
					try {
						control.update(g);
						System.out.println("修改学生信息成功");
						step = 0;
						pervious = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("修改学生信息失败");
						step = 0;
						pervious = null;
					}

				}
				step++;
			} else if (OPERATION_DELETE.equals(in.toUpperCase())
					|| OPERATION_DELETE.substring(0, 1)
							.equals(in.toUpperCase())
					|| OPERATION_DELETE.equals(pervious)) {
				pervious = OPERATION_DELETE;
				if (1 == step) {
					System.out.println("请输入要删除的学生编号：");
				} else if (2 == step) {
					try {
						control.del((int) Integer.valueOf(in));
						System.out.println("删除学生信息成功");
						step = 0;
						pervious = null;
					} catch (Exception e) {
						System.out.println("请输入正确的学生编号：");
						step = 1;
					}
				}
				step++;
			} else if (OPERATION_SEARCH.equals(in.toUpperCase())
					|| OPERATION_SEARCH.substring(0, 1)
							.equals(in.toUpperCase())
					|| OPERATION_SEARCH.equals(pervious)) {
				pervious = OPERATION_SEARCH;
				if (1 == step) {
					System.out.println("请输入要查询的科目,选填：C/M/E");
				} else if (2 == step) {
					c = in + "";
					System.out.println("请输入要查询的范围,格式如：min,max");
				} else if (3 == step) {
					double min = 0;
					double max = 0;
					try {
						min = Double.valueOf(in.substring(0, in.indexOf(',')));
						max = Double.valueOf(in.substring(in.indexOf(',') + 1));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						System.out.println("请输入正确的信息格式");
						step = 3;
						continue;
					}
					try {
						List<Stu> list = control.query();
						int flag = 0;
						for (Stu stu : list) {
							if ("C".equals(c.toUpperCase())
									&& stu.getChinese() >= min
									&& stu.getChinese() <= max) {
								System.out.println(stu.toString());
								flag = 1;
							} else if ("M".equals(c.toUpperCase())
									&& stu.getMaths() >= min
									&& stu.getMaths() <= max) {
								System.out.println(stu.toString());
								flag = 1;
							} else if ("E".equals(c.toUpperCase())
									&& stu.getEnglish() >= min
									&& stu.getEnglish() <= max) {
								System.out.println(stu.toString());
								flag = 1;
							}
						}
						if (flag == 0) {
							System.out.println("没有任何学生信息");
						}
						step = 0;
						pervious = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("查询学生信息失败");
						pervious = null;
					}
				}
				step++;
			}
		}
	}
}
