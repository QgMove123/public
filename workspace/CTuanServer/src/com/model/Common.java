package com.model;
//������
/*
 * status ״̬
 * 		 1 �û�ע��
 * 		 2 �û���½
 * 		 3 ��ò˵�
 * 		 4 ��ö���
 * 		 5 ���Ӷ���
 * 		 6 ɾ������
 * 		 7 ���ܶ���
 * 		 8 �ܾ�����
 * 		 9 �ϼܲ�ʽ
 * 		10 �¼ܲ�ʽ
 * 		11 �޸Ĳ�ʽ
 */
public class Common implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int status;
	private int flag;//Ĭ��0�����û���1�����̻�����������ʾ������Ϣ��
	private Object object;

	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}