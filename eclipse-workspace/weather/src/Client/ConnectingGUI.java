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

public class ConnectingGUI extends JFrame { //접속중 프레임

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel contentPane;
	private int frameWidth = 400;
	private int frameHeight = 300;
	private JLabel lblConnecting;
	
	public ConnectingGUI() {
		frame = this;
		
		Toolkit tk = Toolkit.getDefaultToolkit(); //사용자의 화면 크기값을 얻기위한 툴킷 클래스 
		
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
		
		JLabel lblTitleChat = new JLabel("Weather Schedule"); //제목
		lblTitleChat.setFont(new Font("맑은 고딕", Font.BOLD, 34));
		lblTitleChat.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitleChat.setBounds(25, 25, 350, 40);
		lblTitleChat.setForeground(MyColor.white);
		contentPane.add(lblTitleChat);
		
		ImageIcon mainImg = new ImageIcon(Client.class.getResource("/resources/connect.png"));  //로딩 이미지
		JLabel lblNewLabel = new JLabel(mainImg);
		lblNewLabel.setBounds(80, 105, 120, 120);
		contentPane.add(lblNewLabel);
		
		lblConnecting = new JLabel("접속중..."); //메시지
		lblConnecting.setSize(100, 50);
		lblConnecting.setLocation(220, 150);
		lblConnecting.setForeground(MyColor.white);
		lblConnecting.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		lblConnecting.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblConnecting);
		
		this.setVisible(true);
		
		connect(); //서버 접속 시도 
		
	}
		
	public void connect() { // 서버 접속 시도
		Registry reg;
		boolean reponse = false;
		try {
			reg = LocateRegistry.getRegistry("127.0.0.1", 3099);
			Client.server = (RemoteMethodInf) reg.lookup("stub");
			reponse = Client.server.checkConnect(Client.ip);
		} catch (Exception e) {
			e.printStackTrace();
			CheckGUI checkgui = new CheckGUI((JFrame) this, "서버연결  실패", true, true);
		}
		if (reponse) {
			boolean skip = false;//스킵 여부
			this.dispose();
			try {
				// 설정한 지역값 불러옴
				File dataFile = new File("./area.txt"); // 파일 로드 준비
				if(dataFile.exists()) {//파일 존재하면(프로그램 처음 실행시) 값 설정
					String readData;
					StringTokenizer st;
					BufferedReader br = new BufferedReader(new FileReader(dataFile));
					while ((readData = br.readLine()) != null) {
						st = new StringTokenizer(readData, ","); // 지역은 ,로 구분
						String city = st.nextToken(); //city값
						String town = st.nextToken(); //town값
						if(city == null || town == null) { //두 값중 하나라도 이상하면
							skip = false; //다시 세팅하도록 skip 막음
							break; 
						}
						Client.cityName = city; //정보 저장
						Client.townName = town;
					}
					br.close();
					skip = true; //잘 됐다면 다시 세팅할 필요 없음 바로 메인메뉴 화면 표시
				} else { //파일 없으면
					skip = false; //다시 세팅해야함
				}
			} catch (Exception e) { //오류나면 다시 세팅으로
				skip = false;
			}
			if(skip) { //다시 세팅할지 여부
				MainMenuGUI main = new MainMenuGUI(); //세팅할 필요없으면 바로 메인메뉴
			} else {//세팅할 필요있으면 세팅창 표시
				AreaSelectGUI asGui = new AreaSelectGUI(null); //null값 넘겨주면 지역 설정완료시 메인메뉴 프레임을 띄움
			}
		} else {
			CheckGUI checkgui = new CheckGUI((JFrame) this, "서버연결  실패", true, true); //RMI통신 안되면 연결실패 띄우고 프로그램 종료
		}
		
	}
	
	
}
