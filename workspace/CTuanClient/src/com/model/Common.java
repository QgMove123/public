package com.model;
//������
/*
 * status ״̬
 * 		 1 �û�ע��
 * 		 2 �û���½
 * 		 3 ��ò˵�
 * 		 4 ��������
 * 		 5 ���Ӷ���
 * 		 6 ������־
 * 		 7 �ϼܲ�ʽ
 * 		 8 �¼ܲ�ʽ
 * 		 9 �޸Ĳ�ʽ
 * 		10 �鿴����
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