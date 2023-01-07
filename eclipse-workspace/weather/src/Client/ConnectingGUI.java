package Client;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Addon.MyColor;
import RmiConnection.RemoteMethodInf;

public class ConnectingGUI extends JFrame { //������ ������

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel contentPane;
	private int frameWidth = 400;
	private int frameHeight = 300;
	private JLabel lblConnecting;
	
	public ConnectingGUI() {
		frame = this;
		
		Toolkit tk = Toolkit.getDefaultToolkit(); //������� ȭ�� ũ�Ⱚ�� ������� ��Ŷ Ŭ���� 
		
		URL src = ConnectingGUI.class.getResource("/resources/baricon.png");
		ImageIcon icon = new ImageIcon(src);
		this.setIconImage(icon.getImage());
		this.setTitle("Weather Schedule");
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth /2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(MyColor.lightBlack);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitleChat = new JLabel("Weather Schedule"); //����
		lblTitleChat.setFont(new Font("���� ���", Font.BOLD, 34));
		lblTitleChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitleChat.setBounds(25, 25, 350, 40);
		lblTitleChat.setForeground(MyColor.white);
		contentPane.add(lblTitleChat);
		
		ImageIcon mainImg = new ImageIcon(Client.class.getResource("/resources/connect.png"));  //�ε� �̹���
		JLabel lblNewLabel = new JLabel(mainImg);
		lblNewLabel.setBounds(80, 105, 120, 120);
		contentPane.add(lblNewLabel);
		
		lblConnecting = new JLabel("������..."); //�޽���
		lblConnecting.setSize(100, 50);
		lblConnecting.setLocation(220, 150);
		lblConnecting.setForeground(MyColor.white);
		lblConnecting.setFont(new Font("���� ���", Font.BOLD, 16));
		lblConnecting.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblConnecting);
		
		this.setVisible(true);
		
		connect(); //���� ���� �õ� 
		
	}
		
	public void connect() { // ���� ���� �õ�
		Registry reg;
		boolean reponse = false;
		try {
			reg = LocateRegistry.getRegistry("127.0.0.1", 3099);
			Client.server = (RemoteMethodInf) reg.lookup("stub");
			reponse = Client.server.checkConnect(Client.ip);
		} catch (Exception e) {
			e.printStackTrace();
			CheckGUI checkgui = new CheckGUI((JFrame) this, "��������  ����", true, true);
		}
		if (reponse) {
			boolean skip = false;//��ŵ ����
			this.dispose();
			try {
				// ������ ������ �ҷ���
				File dataFile = new File("./area.txt"); // ���� �ε� �غ�
				if(dataFile.exists()) {//���� �����ϸ�(���α׷� ó�� �����) �� ����
					String readData;
					StringTokenizer st;
					BufferedReader br = new BufferedReader(new FileReader(dataFile));
					while ((readData = br.readLine()) != null) {
						st = new StringTokenizer(readData, ","); // ������ ,�� ����
						String city = st.nextToken(); //city��
						String town = st.nextToken(); //town��
						if(city == null || town == null) { //�� ���� �ϳ��� �̻��ϸ�
							skip = false; //�ٽ� �����ϵ��� skip ����
							break; 
						}
						Client.cityName = city; //���� ����
						Client.townName = town;
					}
					br.close();
					skip = true; //�� �ƴٸ� �ٽ� ������ �ʿ� ���� �ٷ� ���θ޴� ȭ�� ǥ��
				} else { //���� ������
					skip = false; //�ٽ� �����ؾ���
				}
			} catch (Exception e) { //�������� �ٽ� ��������
				skip = false;
			}
			if(skip) { //�ٽ� �������� ����
				MainMenuGUI main = new MainMenuGUI(); //������ �ʿ������ �ٷ� ���θ޴�
			} else {//������ �ʿ������� ����â ǥ��
				AreaSelectGUI asGui = new AreaSelectGUI(null); //null�� �Ѱ��ָ� ���� �����Ϸ�� ���θ޴� �������� ���
			}
		} else {
			CheckGUI checkgui = new CheckGUI((JFrame) this, "��������  ����", true, true); //RMI��� �ȵǸ� ������� ���� ���α׷� ����
		}
		
	}
	
	
}
