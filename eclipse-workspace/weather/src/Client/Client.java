package Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.StringTokenizer;

import RmiConnection.RemoteMethodInf;

public class Client {
	
	public static RemoteMethodInf server; //서버와 RMI통신을 위한 객체 주소 저
	public static String cityName = "경기도"; //도시명 저장공간
	public static String townName = "안양시동안구"; //동네명 저장공간
	public static String ip = ""; //IP저장공간

	public static void main(String args[]) {
		try {
			ip = InetAddress.getLocalHost().getHostAddress().toString();//클라이언트 사용자의 IP 저장
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ConnectingGUI connectingGUI = new  ConnectingGUI(); //서버 접속 시작
		MainMenuGUI mainMenuGUI = new MainMenuGUI();
	}
	
}
