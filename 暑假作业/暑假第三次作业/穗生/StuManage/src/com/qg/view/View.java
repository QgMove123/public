package com.qg.view;

import java.util.List;
import java.util.Scanner;

import com.qg.control.StuControl;
import com.qg.model.Stu;

public class View {

	private static final String CONTEXT = "��ӭ����ѧ���ɼ�����ϵͳ��\n" + "������ϵͳ�Ĺ����б�\n"
			+ "[MAIN/M]:���˵�\n" + "[QUERY/Q]:�鿴ȫ��ѧ������Ϣ\n"
			+ "[GET/G]:�鿴ĳͬѧ��ȫ����Ϣ\n" + "[ADD/A]:����ĳһͬѧ��Ϣ\n"
			+ "[UPDATE/U]:�޸�ͬѧ��Ϣ\n" + "[SEARCH/S]:��ѧ�Ƴɼ����з�Χ����\n"
			+ "[DELETE/D]:ɾ��ĳһͬѧ��Ϣ\n" + "[EXIT/E]:�˳�ϵͳ\n";

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
		String c = null;// ��Ŀ;

		while (true) {
			String in = scan.next();
			if (OPERATION_EXIT.equals(in.toUpperCase())
					|| OPERATION_EXIT.substring(0, 1).equals(in.toUpperCase())) {
				System.out.println("���ѳɹ��˳�ϵͳ");
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
						System.out.println("���:" + stu.getId() + " , " + "����:"
								+ stu.getName());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("û���κ�ѧ����Ϣ");
					pervious = null;
				}
			} else if (OPERATION_GET.equals(in.toUpperCase())
					|| OPERATION_GET.substring(0, 1).equals(in.toUpperCase())
					|| OPERATION_GET.equals(pervious)) {
				pervious = OPERATION_GET;
				if (1 == step) {
					System.out.println("�������ѯ��ѧ����ţ�");
				} else if (step > 1) {
					try {
						g = control.get((int) Integer.valueOf(in));
						if (g == null) {
							System.out.println("��ѯѧ����Ϣʧ��");
							pervious = null;
							step = 0;
						} else {
							System.out.println(g.toString());
							pervious = null;
							step = 1;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("��������ȷ��ѧ�����");
						step = 2;
					}
				}
				step++;

			} else if (OPERATION_ADD.equals(in.toUpperCase())
					|| OPERATION_ADD.substring(0, 1).equals(in.toUpperCase())
					|| OPERATION_ADD.equals(pervious)) {
				pervious = OPERATION_ADD;
				if (1 == step) {
					System.out.println("������ѧ������Ϣ[רҵ]��");
				} else if (2 == step) {
					g = new Stu();
					g.setMajor(in);
					System.out.println("������ѧ������Ϣ[�༶]��");
				} else if (3 == step) {
					g.setGrade(in);
					System.out.println("������ѧ������Ϣ[����]��");
				} else if (4 == step) {
					g.setName(in);
					System.out.println("������ѧ������Ϣ[�Ա�]��");
				} else if (5 == step) {
					g.setSex(in);
					System.out.println("������ѧ������Ϣ[ѧ��]��");
				} else if (6 == step) {
					g.setNumber(in);
					System.out.println("������ѧ������Ϣ[����]��");
				} else if (7 == step) {
					try {
						g.setChinese(Double.valueOf(in));
						System.out.println("������ѧ������Ϣ[��ѧ]��");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("��������ȷ��ѧ���ɼ�");
						step = 6;
					}
				} else if (8 == step) {
					try {
						g.setMaths(Double.valueOf(in));
						System.out.println("������ѧ������Ϣ[Ӣ��]��");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("��������ȷ��ѧ���ɼ�");
						step = 7;
					}

				} else if (9 == step) {
					try {
						g.setEnglish(Double.valueOf(in));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						System.out.println("��������ȷ��ѧ���ɼ�");
						step = 9;
						continue;
					}
					try {
						control.add(g);
						System.out.println("����ѧ����Ϣ�ɹ�");
						step = 0;
						pervious = null;
					} catch (Exception e) {
						System.out.println("����ѧ����Ϣʧ��");
					}
				}
				step++;

			} else if (OPERATION_UPDATE.equals(in.toUpperCase())
					|| OPERATION_UPDATE.substring(0, 1)
							.equals(in.toUpperCase())
					|| OPERATION_UPDATE.equals(pervious)) {
				pervious = OPERATION_UPDATE;
				if (1 == step) {
					System.out.println("������Ҫ�޸ĵ�ѧ����ţ�");
				} else if (2 == step) {
					try {
						g = control.get((int) Integer.valueOf(in));
						if (g == null) {
							System.out.println("�޸�ѧ����Ϣʧ��");
							step = 0;
							pervious = null;
						}
					} catch (Exception e) {
						System.out.println("��������ȷ��ѧ����ţ�");
						step = 1;
					}
					System.out.println("�������µ�ѧ����Ϣ[רҵ]��������޸ĸ�ֵ��������0��");
				} else if (3 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setMajor(in);
					}
					System.out.println("�������µ�ѧ����Ϣ[�༶]��������޸ĸ�ֵ��������0��");
				} else if (4 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setGrade(in);
					}
					System.out.println("�������µ�ѧ����Ϣ[����]��������޸ĸ�ֵ��������0��");
				} else if (5 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setName(in);
					}
					System.out.println("�������µ�ѧ����Ϣ[�Ա�]��������޸ĸ�ֵ��������0��");
				} else if (6 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setSex(in);
					}
					System.out.println("�������µ�ѧ����Ϣ[ѧ��]��������޸ĸ�ֵ��������0��");
				} else if (7 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						g.setNumber(in);
					}
					System.out.println("�������µ�ѧ����Ϣ[����]��������޸ĸ�ֵ��������0��");
				} else if (8 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						try {
							g.setChinese(Double.valueOf(in));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("��������ȷ��ѧ���ɼ���");
							step = 7;
						}
					}
					System.out.println("�������µ�ѧ����Ϣ[��ѧ]��������޸ĸ�ֵ��������0��");
				} else if (9 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						try {
							g.setMaths(Double.valueOf(in));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("��������ȷ��ѧ���ɼ���");
							step = 8;
						}
					}
					System.out.println("�������µ�ѧ����Ϣ[Ӣ��]��������޸ĸ�ֵ��������0��");
				} else if (10 == step) {
					if (!"0".equals(in.substring(0, 1))) {
						try {
							g.setEnglish(Double.valueOf(in));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("��������ȷ��ѧ���ɼ���");
							step = 10;
							continue;
						}
					}
					try {
						control.update(g);
						System.out.println("�޸�ѧ����Ϣ�ɹ�");
						step = 0;
						pervious = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("�޸�ѧ����Ϣʧ��");
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
					System.out.println("������Ҫɾ����ѧ����ţ�");
				} else if (2 == step) {
					try {
						control.del((int) Integer.valueOf(in));
						System.out.println("ɾ��ѧ����Ϣ�ɹ�");
						step = 0;
						pervious = null;
					} catch (Exception e) {
						System.out.println("��������ȷ��ѧ����ţ�");
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
					System.out.println("������Ҫ��ѯ�Ŀ�Ŀ,ѡ�C/M/E");
				} else if (2 == step) {
					c = in + "";
					System.out.println("������Ҫ��ѯ�ķ�Χ,��ʽ�磺min,max");
				} else if (3 == step) {
					double min = 0;
					double max = 0;
					try {
						min = Double.valueOf(in.substring(0, in.indexOf(',')));
						max = Double.valueOf(in.substring(in.indexOf(',') + 1));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						System.out.println("��������ȷ����Ϣ��ʽ");
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
							System.out.println("û���κ�ѧ����Ϣ");
						}
						step = 0;
						pervious = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("��ѯѧ����Ϣʧ��");
						pervious = null;
					}
				}
				step++;
			}
		}
	}
}
