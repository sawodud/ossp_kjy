package Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.StringTokenizer;

import RmiConnection.RemoteMethodInf;

public class Client {
	
	public static RemoteMethodInf server; //������ RMI����� ���� ��ü �ּ� ��
	public static String cityName = "��⵵"; //���ø� �������
	public static String townName = "�Ⱦ�õ��ȱ�"; //���׸� �������
	public static String ip = ""; //IP�������

	public static void main(String args[]) {
		try {
			ip = InetAddress.getLocalHost().getHostAddress().toString();//Ŭ���̾�Ʈ ������� IP ����
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ConnectingGUI connectingGUI = new  ConnectingGUI(); //���� ���� ����
		MainMenuGUI mainMenuGUI = new MainMenuGUI();
	}
	
}
