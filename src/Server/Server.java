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
	public static MyDatabase database = new MyDatabase(); //�����ͺ��̽� ��ſ� Ŭ����
	
	public Server server; //����
	
	RemoteMethodInf rm; //���� RMI ���ٿ�
	//
	////GUI����
	JFrame frame;
	JScrollPane sc;
	JTextArea logArea;
	int frameWidth = 500;
	int frameHeight = 700;
	
	public static void main(String args[]) {
		Server server = new Server();
	}

	public Server() {
		
		//InputCmd inputCmd = new InputCmd(); ������
		//inputCmd.start();
		
		server = this;
		
		//////////������
		Toolkit tk = Toolkit.getDefaultToolkit(); //������� ȭ�� ũ�Ⱚ�� ������� ��Ŷ Ŭ���� 
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(5,5));
		frame.setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth/2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		frame.setTitle("Title Chat Log Manager"); //������ ���� ����
		frame.setResizable(true); //������ ũ�� ���� ����
		frame.setBackground(MyColor.black); //���� ����
		frame.add(new JPanel(), BorderLayout.NORTH); //����� GUI�� ���� �߰�
		frame.add(new JPanel(), BorderLayout.SOUTH); //����� GUI�� ���� �߰�
		frame.add(new JPanel(), BorderLayout.EAST); //����� GUI�� ���� �߰�
		frame.add(new JPanel(), BorderLayout.WEST); //����� GUI�� ���� �߰�
		//this.setUndecorated(true); ������ Ʋ ���ֱ��, ������
	
		logArea = new JTextArea();
		logArea.setFont(logArea.getFont().deriveFont(15f));
		logArea.setAutoscrolls(true);
		logArea.setEditable(false);
		logArea.setForeground(MyColor.white); //���ڻ� ����
		logArea.setBackground(MyColor.lightBlack);
		logArea.setLineWrap(true);   //������ �����ٷ� ���� ����.
		sc = new JScrollPane(logArea);
		sc.setAutoscrolls(true);
		frame.add(sc, BorderLayout.CENTER);
		
		URL src = Server.class.getResource("/resources/baricon.png"); //������ ����
		ImageIcon icon = new ImageIcon(src);
		frame.setIconImage(icon.getImage());
		
		frame.setVisible(true); //������ ǥ��
		
		openServer(); //���� ����
		
		/*try { //�׽�Ʈ��
			rm.getWeatherData("", "��⵵", "�Ⱦ�õ��ȱ�");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		printLog("Server> ���� ���� �Ϸ�");
		
		
	}
	
	public void printLog(String str) { //�α� GUI�� �α����
		SimpleDateFormat format1 = new SimpleDateFormat ( "[yy��MM��dd�� HH:mm:ss] "); //�ð�
		String format_time1 = format1.format(System.currentTimeMillis());
		logArea.append(format_time1+str+"\n"); //���
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
			System.out.println("���� ���࿡ �����Ͽ����ϴ�.");
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
