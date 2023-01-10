package Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import API_Weather.Weather;
import Addon.MyColor;
import RmiConnection.RemoteMethod;
import RmiConnection.RemoteMethodInf;

public class Server {
	public static MyDatabase database = new MyDatabase(); //데이터베이스 통신용 클래스
	
	public Server server; //서버
	
	RemoteMethodInf rm; //서버 RMI 접근용
	//
	////GUI관련
	JFrame frame;
	JScrollPane sc;
	JTextArea logArea;
	int frameWidth = 500;
	int frameHeight = 700;
	
	public static void main(String args[]) {
		Server server = new Server();
	}

	public Server() {
		
		//InputCmd inputCmd = new InputCmd(); 디버깅용
		//inputCmd.start();
		
		server = this;
		
		//////////프레임
		Toolkit tk = Toolkit.getDefaultToolkit(); //사용자의 화면 크기값을 얻기위한 툴킷 클래스 
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(5,5));
		frame.setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth/2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		frame.setTitle("Title Chat Log Manager"); //프레임 제목 설정
		frame.setResizable(true); //프레임 크기 조정 가능
		frame.setBackground(MyColor.black); //배경색 설정
		frame.add(new JPanel(), BorderLayout.NORTH); //깔끔한 GUI를 위해 추가
		frame.add(new JPanel(), BorderLayout.SOUTH); //깔끔한 GUI를 위해 추가
		frame.add(new JPanel(), BorderLayout.EAST); //깔끔한 GUI를 위해 추가
		frame.add(new JPanel(), BorderLayout.WEST); //깔끔한 GUI를 위해 추가
		//this.setUndecorated(true); 윈도우 틀 없애기용, 사용안함
	
		logArea = new JTextArea();
		logArea.setFont(logArea.getFont().deriveFont(15f));
		logArea.setAutoscrolls(true);
		logArea.setEditable(false);
		logArea.setForeground(MyColor.white); //글자색 설정
		logArea.setBackground(MyColor.lightBlack);
		logArea.setLineWrap(true);   //꽉차면 다음줄로 가게 해줌.
		sc = new JScrollPane(logArea);
		sc.setAutoscrolls(true);
		frame.add(sc, BorderLayout.CENTER);
		
		URL src = Server.class.getResource("/resources/baricon.png"); //아이콘 설정
		ImageIcon icon = new ImageIcon(src);
		frame.setIconImage(icon.getImage());
		
		frame.setVisible(true); //프레임 표시
		
		openServer(); //서버 열기
		
		/*try { //테스트용
			rm.getWeatherData("", "경기도", "안양시동안구");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		printLog("Server> 서버 가동 완료");
		
		
	}
	
	public void printLog(String str) { //로그 GUI에 로그출력
		SimpleDateFormat format1 = new SimpleDateFormat ( "[yy년MM월dd일 HH:mm:ss] "); //시간
		String format_time1 = format1.format(System.currentTimeMillis());
		logArea.append(format_time1+str+"\n"); //출력
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}
	
	public boolean openServer() {
		int rmiPort = 3099;
		try {
			rm = new RemoteMethod(server);
			UnicastRemoteObject.unexportObject(rm, true);
			RemoteMethodInf myStub = (RemoteMethodInf) UnicastRemoteObject.exportObject(rm, 0);
			Registry myReg = LocateRegistry.createRegistry(rmiPort);
			myReg.rebind("stub", myStub);	
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("서버 실행에 실패하였습니다.");
			System.exit(0);
			return false;
		}
	}
	
	public boolean closeServer() {
		try {
			UnicastRemoteObject.unexportObject(rm, true);
			Registry myReg = LocateRegistry.getRegistry(3099);
			myReg.unbind("stub");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
