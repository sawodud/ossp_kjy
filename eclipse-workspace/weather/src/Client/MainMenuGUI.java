package Client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Addon.MyColor;
// 채팅창 정보를 여기에 넣는걸로 합시다. *******
public class MainMenuGUI extends JFrame {
	private int frameWidth = 500; //가로
	private int frameHeight = 750; //세로
	private JPanel mainPanel; 
	private CardLayout card; //오늘자, 내일자 로 화면이 2개이므로 카드 레이아웃 사용하여 화면 전환
	private DayPanel todayPanel; //오늘자 패널
	private DayPanel tomorPanel; //내일자 패널
	//private DayPanel chatPanel; // 채팅창 패널
	private JButton btn_tomor; //오늘 버튼
	private JButton btn_today; //내일 버튼
	//private JButton btn_chatting; // 채팅 버튼
	private MyAction myAction; //이벤트
	private String selectedDay = "today"; //현재 선택한 패널(오늘자? 내일자?)
	private MainMenuGUI mainFrame; //이 프레임
	
	public MainMenuGUI() { //메인메뉴 프레임
		mainFrame = this; //프레임 주소 저장
		
		Toolkit tk = Toolkit.getDefaultToolkit(); //사용자의 화면 크기값을 얻기위한 툴킷 클래스 
		
		//GUI 설정
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		URL src = Client.class.getResource("/resources/baricon.png");
		ImageIcon icon = new ImageIcon(src);
		this.setIconImage(icon.getImage());
		this.setTitle("Weather Schedule");
		setResizable(false); // 창조절 불가능
		setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth /2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		getContentPane().setLayout(null);
		getContentPane().setBackground(MyColor.darkAqua);
		
		myAction = new MyAction();
		todayPanel = new TodayPanel(); //오늘자 프레임
		tomorPanel = new TomorPanel(); //내일자 프레임
		
		JPanel pane_DaySelect = new JPanel();
		pane_DaySelect.setBounds(0, 0, 494, 50);
		getContentPane().add(pane_DaySelect);
		pane_DaySelect.setBackground(MyColor.black);
		pane_DaySelect.setLayout(new GridLayout(0, 2, 0, 0));
		pane_DaySelect.setBorder(new LineBorder(MyColor.black, 1));
		
		btn_today = new JButton("오늘"); //오늘 버튼
		btn_today.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		btn_today.setBackground(MyColor.black);
		btn_today.setForeground(MyColor.white);
		btn_today.setFocusPainted(false);
		btn_today.setBorderPainted(false);
		btn_today.addActionListener(myAction);
		//btn_today.setBorder(new LineBorder(MyColor.white, 1));
		pane_DaySelect.add(btn_today);
		
		btn_tomor = new JButton("내일"); //내일 버튼
		btn_tomor.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
		btn_tomor.setBackground(MyColor.lightBlack);
		btn_tomor.setForeground(MyColor.white);
		btn_tomor.setFocusPainted(false);
		btn_tomor.setBorderPainted(false);
		btn_tomor.addActionListener(myAction);
		//btn_tomor.setBorder(new LineBorder(MyColor.white, 1));
		pane_DaySelect.add(btn_tomor);
		
		mainPanel = new JPanel(); //화면 전환용 패널
		mainPanel.setBounds(0,50,494,700);
		mainPanel.setBackground(MyColor.darkAqua);
		card = new CardLayout(); //카드레이아웃 주소
		mainPanel.setLayout(card);
		mainPanel.add("today", todayPanel);
		mainPanel.add("tomor", tomorPanel);
		getContentPane().add(mainPanel);
		card.show(mainPanel, "today"); //처음에는 오늘자 패널 표시
		
		//setInfo(); //오늘자, 내일자 패널에 기상정보를 GUI 세팅
		
		this.setVisible(true);
		
	}
	
	public void setInfo() {
		todayPanel.btn_AreaName.setText(Client.cityName+" "+Client.townName); //오늘자 패널 조회 지역 이름 설정
		tomorPanel.btn_AreaName.setText(Client.cityName+" "+Client.townName); //내일자 패널 조회 지역 이름 설정
		
		
		List<LinkedHashMap<String, String>> weatherList = null;
		try {
			weatherList = Client.server.getWeatherData(Client.ip, Client.cityName, Client.townName); //서버에서 기상정보 받아옴
		} catch (RemoteException e) { //RMI 통신이상시
			CheckGUI checkgui = new CheckGUI((JFrame) this, "정보 얻기 실패", true, true);
			e.printStackTrace();
		}
		if(weatherList == null) { //서버에서 아무값도 못받을 시
			CheckGUI checkgui = new CheckGUI((JFrame) this, "정보 얻기 실패", true, true);
			return;
		}
		/*for (LinkedHashMap<String, String> map : weatherList) { //값 확인용
			System.out.println("------------------");
			for (String key : map.keySet()) {
				System.out.println(key + " : " + map.get(key));
			}
		}*/
		LinkedHashMap<String, String> now_weatherMap = weatherList.get(0); //현재 날씨 정보
		LinkedHashMap<String, String> today_weatherMap = weatherList.get(1); //앞으로 남은 오늘자 날씨 정보
		LinkedHashMap<String, String> tomor_weatherMap = weatherList.get(2); //내일자 날씨 정보
		LinkedHashMap<String, String> tomor_tm_Map = weatherList.get(3); //내일자 최저, 최고기온 정보
		
		String dayValue = now_weatherMap.get("day"); //조회 일자 파싱해서 표시
		String year = dayValue.substring(2,4)+"년"; 
		String mon = dayValue.substring(4,6)+"월";
		String day = dayValue.substring(6,8)+"일";
		todayPanel.btn_day.setText(year+" "+mon+" "+day);
		todayPanel.lbl_nowTempe.setText(now_weatherMap.get("T3H")+"º"); //현재 온도 설정
		todayPanel.lbl_POP.setText(now_weatherMap.get("POP")+"%"); //현재 강수확률 설정
		todayPanel.lbl_REH.setText(now_weatherMap.get("REH")+"º"); //현재 습도 설정
		todayPanel.lbl_WSD.setText(now_weatherMap.get("WSD")+"m/s"); //현재 풍속 설정
		
		String weatherState = getWeatherState(now_weatherMap.get("PTY"), now_weatherMap.get("SKY"));  //날씨상태 가져와서 상태에  따라 icon 설정
		URL src = Client.class.getResource("/resources/"+weatherState+".png");
		ImageIcon img = new ImageIcon(src);
		todayPanel.lbl_weatherIcon.setIcon(img);
		todayPanel.lbl_weatherState.setText(weatherToKor(weatherState));
			
		List<WeatherInfo> todayWeatherList = new ArrayList<WeatherInfo>(); //오늘자 날씨 정보 리스트
		List<String> keyList = new ArrayList<String>(today_weatherMap.keySet()); //키 리스트
		LinkedHashMap<String, String> parseMap = new LinkedHashMap<String, String>(); //파싱중인 값 저장용
		String parseDay = today_weatherMap.get(keyList.get(0)); //파싱한 일자
		String parseTime = ""; //파싱중인 시간대
		for(int i = 1; i < keyList.size(); i++) { //0번은 day 값이니 파싱할 필요 없음
			String key = keyList.get(i);
			String time = key.substring(0,4); //날씨정보의 시간값 파싱
			if(parseTime.equalsIgnoreCase("")) parseTime = time;
			if(!parseTime.equalsIgnoreCase(time)) { //현재 파싱하고 있는 시간값하고 일치하지않으면 해당 시간대는 파싱 끝
				//그동안 모은 정보로 날씨 정보를 형성해서 저장해줌
				WeatherInfo wi = createWeatherInfo(parseDay, parseTime, parseMap); //날씨 정보 만들어서
				if(wi != null) todayWeatherList.add(wi); //넣음
				parseMap.clear(); //초기화
				parseTime = time; //시간을 새로운 시간대로
			}
			String category = key.substring(4,7); //날씨 카테고리
			parseMap.put(category, today_weatherMap.get(keyList.get(i))); //해당 카테고리에 대한 값
			System.out.println(category+" : "+today_weatherMap.get(keyList.get(i)));
		}
		//남은 데이터 넣기
				WeatherInfo wi = createWeatherInfo(parseDay, parseTime, parseMap); //날씨 정보 만들어서
				if(wi != null) todayWeatherList.add(wi); //넣음

		
		////////////내일자
		List<WeatherInfo> tomorWeatherList = new ArrayList<WeatherInfo>(); //내일자 정보 리스트
		keyList = new ArrayList<String>(tomor_weatherMap.keySet()); //키 리스트
		parseMap.clear(); //파싱중인 값 저장용
		parseDay = tomor_weatherMap.get(keyList.get(0)); //파싱한 일자
		dayValue = parseDay;
		year = dayValue.substring(2,4)+"년";
		mon = dayValue.substring(4,6)+"월";
		day = dayValue.substring(6,8)+"일";
		tomorPanel.btn_day.setText(year+" "+mon+" "+day);
		parseTime = ""; //파싱중인 시간대
		for(int i = 1; i < keyList.size(); i++) { //0번은 day 값이니 파싱할 필요 없음
			String key = keyList.get(i);
			String time = key.substring(0,4); //날씨정보의 시간값 파싱
			if(parseTime.equalsIgnoreCase("")) parseTime = time;
			if(!parseTime.equalsIgnoreCase(time)) { //현재 파싱하고 있는 시간값하고 일치하지않으면 해당 시간대는 파싱 끝
				//그동안 모은 정보로 날씨 정보를 형성해서 저장해줌
				wi = createWeatherInfo(parseDay, parseTime, parseMap); //날씨 정보 만들어서
				if(wi != null) tomorWeatherList.add(wi); //넣음
				parseMap.clear(); //초기화
				parseTime = time; //시간을 새로운 시간대로
			}
			String category = key.substring(4,7); //날씨 카테고리
			parseMap.put(category, tomor_weatherMap.get(keyList.get(i))); //해당 카테고리에 대한 값
			//System.out.println(category+" : "+tomor_weatherMap.get(keyList.get(i))); 값 확인용
		}
		//남은 데이터 넣기
		wi = createWeatherInfo(parseDay, parseTime, parseMap); //날씨 정보 만들어서
		if(wi != null) tomorWeatherList.add(wi); //넣음
		
		for(Component c : todayPanel.listPanel.getComponents()) { //오늘자 날씨 패널에 있는 컴포넌트 모두 삭제
			todayPanel.listPanel.remove(c);
		}	
		todayPanel.setWeatherGui(todayWeatherList);
		
		for(Component c : tomorPanel.listPanel.getComponents()) { //내일자 날씨 패널에 있는 컴포넌트 모두 삭제
			tomorPanel.listPanel.remove(c);
		}	
		tomorPanel.setWeatherGui(tomorWeatherList);
		
		String tomor_TMN = tomor_tm_Map.get("TMN"); //내일 최저기온
		String tomor_TMX = tomor_tm_Map.get("TMX"); //내일 최고기온
		tomorPanel.lbl_TMX.setText(tomor_TMX+"º"); //표시
		tomorPanel.lbl_TMN.setText(tomor_TMN+"º");
	}
	
	private WeatherInfo createWeatherInfo(String parseDay, String parseTime, LinkedHashMap<String, String> parseMap) { //기상정보 클래스 생성
		WeatherInfo wi = null; 
		String T3H = parseMap.get("T3H"); //파싱한 값들
		String POP = parseMap.get("POP");
		String REH = parseMap.get("REH");
		String SKY = parseMap.get("SKY");
		String PTY = parseMap.get("PTY");
		String WSD = parseMap.get("WSD");
		
		if(T3H == null || POP == null || REH == null ||SKY == null ||PTY == null ||WSD == null) {
			//필요한 카테고리중 하나라도 빠져있다면 해당 정보 패스함
			System.out.println(parseDay+" - "+parseTime+" 에서 파싱문제 발생");
		} else {
			wi = new WeatherInfo(parseDay, parseTime, T3H, POP, REH, SKY, PTY, WSD); //날씨 정보 만들어서
		}
		return wi;
	}
	
	private String getWeatherState(String pty, String sky) {
		//pty //테스트용
		//sky = "2"; //테스트용
		String weatherState = "clear";
		if (!pty.equalsIgnoreCase("0")) { //하늘에서 뭔가 오고 있다면
			switch (pty) {
			case "1":
				weatherState = "rain";
				break; // 비옴
			case "2":
				weatherState = "snowrain";
				break; // 눈비옴
			case "3":
				weatherState = "snow";
				break; // 눈옴
			case "4":
				weatherState = "rain";
				break; // 소나기
			default:
				pty = "0";
			}
		}
		if (pty.equalsIgnoreCase("0")) { //비나 눈이 오지 않으면
			switch (sky) {
			case "1":
				weatherState = "clear";
				break; // 맑음
			case "2":
				weatherState = "cloudbig";
				break; // 구름많음
			case "3":
				weatherState = "cloudbig";
				break; // 구름많음
			case "4":
				weatherState = "cloudy";
				break; // 흐림
			default:
				weatherState = "clear";
			}
		}
		return weatherState;
	}
	
	
	private String weatherToKor(String weatherState) { //그저 영어 날씨를 한글로 바꿔줌
		if(weatherState.equalsIgnoreCase("cloudbig")) return "구름많음";
		else if(weatherState.equalsIgnoreCase("cloudy")) return "흐림";
		else if(weatherState.equalsIgnoreCase("rain")) return "비";
		else if(weatherState.equalsIgnoreCase("snow")) return "눈";
		else if(weatherState.equalsIgnoreCase("snowrain")) return "비/눈";
		else return "맑음";
	}
	
	private class WeatherInfo{
		String day; //날씨 정보 날짜
		String time; //날씨 정보 시간
		String T3H; //기온
		String POP; //강수확률
		String REH; //습도
		String SKY; //하늘상태
		String PTY; //강수형태
		String WSD; //강수형태
		
		public WeatherInfo(String day, String time, String T3H, String POP, String REH, String SKY, String PTY, String WSD) { //채팅방 정보 생성
			this.day = day; //초기화
			this.time = time;
			this.T3H = T3H;
			this.POP = POP;
			this.REH = REH;
			this.PTY = PTY;
			this.SKY = SKY;
			this.WSD = WSD;
		}
		
		public JPanel getGUI() { //해당 날씨 정보를 토대로 gui패널을 만들어줌
				
			JPanel pane = new JPanel(); //반환할 패널
			pane.setLayout(null); //절대 배치

			setGuiComponents(pane); //컴포넌트 설정
			
			pane.setBorder(new LineBorder(MyColor.lightBlack, 1));
			pane.setBackground(MyColor.darkAqua);
			
			return pane;
		}
		
		public void setGuiComponents(JPanel pane) { //날씨 정보를 토대로 컴포넌트 설정
			
			String weatherState = getWeatherState(PTY, SKY); //현재 날씨 상태 가져옴
			
			ImageIcon icon = new ImageIcon(MainMenuGUI.class.getResource("/resources/"+weatherState+".png"));  //주제에 맞춰 이미지 설정
			Image resizeImg = icon.getImage();  //ImageIcon을 Image로 변환.
			Image resizedImg = resizeImg.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
			ImageIcon resizedicon = new ImageIcon(resizedImg); //Image로 ImageIcon 생성
			JLabel titleImg = new JLabel(resizedicon);
			titleImg.setBounds(5, 10, 50, 50);
			pane.add(titleImg);
			
			String timeText = time.substring(0,2) + "시";
			JLabel lblTime = new JLabel(timeText); //시간 표시 라벨
			lblTime.setForeground(MyColor.white);
			lblTime.setHorizontalAlignment(JLabel.CENTER);
			lblTime.setBounds(5, 65, 50, 25);
			lblTime.setForeground(MyColor.black);
			lblTime.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			pane.add(lblTime);
			
			JLabel lblT3H = new JLabel(T3H+"º"); //기온 표시 라벨
			lblT3H.setForeground(MyColor.white);
			lblT3H.setBounds(70, 37, 100, 35);
			lblT3H.setFont(new Font("맑은 고딕", Font.BOLD, 35));
			lblT3H.setForeground(MyColor.lightBlack);
			lblT3H.setVerticalAlignment(JLabel.CENTER);
			pane.add(lblT3H);
			
			
			JLabel lblState = new JLabel(weatherToKor(weatherState)); //기온 표시 라벨
			lblState.setForeground(MyColor.white);
			lblState.setBounds(70, 10, 100, 25);
			lblState.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			lblState.setForeground(MyColor.lightBlack);
			lblState.setVerticalAlignment(JLabel.CENTER);
			pane.add(lblState);
			
			JLabel lblPOP_Text = new JLabel("강수확률"); //강수확률 텍스트 표시 라벨
			lblPOP_Text.setForeground(MyColor.white);
			lblPOP_Text.setBounds(290, 8, 80, 25);
			lblPOP_Text.setVerticalAlignment(JLabel.TOP);
			lblPOP_Text.setHorizontalAlignment(JLabel.LEFT);
			lblPOP_Text.setForeground(MyColor.black);
			lblPOP_Text.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
			pane.add(lblPOP_Text);
			
			JLabel lblPOP = new JLabel(POP+"%"); //강수확률 값 표시 라벨
			lblPOP.setForeground(MyColor.white);
			lblPOP.setBounds(350, 8, 100, 25);
			lblPOP.setVerticalAlignment(JLabel.TOP);
			lblPOP.setHorizontalAlignment(JLabel.RIGHT);
			lblPOP.setForeground(MyColor.lightBlack);
			lblPOP.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			pane.add(lblPOP);
			
			JLabel lblREH_Text = new JLabel("습도"); //강수확률 텍스트 표시 라벨
			lblREH_Text.setForeground(MyColor.white);
			lblREH_Text.setBounds(290, 38, 80, 25);
			lblREH_Text.setVerticalAlignment(JLabel.TOP);
			lblREH_Text.setHorizontalAlignment(JLabel.LEFT);
			lblREH_Text.setForeground(MyColor.black);
			lblREH_Text.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
			pane.add(lblREH_Text);
			
			JLabel lblREH = new JLabel(REH+"%"); //강수확률 값 표시 라벨
			lblREH.setForeground(MyColor.white);
			lblREH.setBounds(350, 38, 100, 25);
			lblREH.setVerticalAlignment(JLabel.TOP);
			lblREH.setHorizontalAlignment(JLabel.RIGHT);
			lblREH.setForeground(MyColor.lightBlack);
			lblREH.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			pane.add(lblREH);
			
			JLabel lblWSD_Text = new JLabel("풍속"); //강수확률 텍스트 표시 라벨
			lblWSD_Text.setForeground(MyColor.white);
			lblWSD_Text.setBounds(290, 68, 80, 25);
			lblWSD_Text.setVerticalAlignment(JLabel.TOP);
			lblWSD_Text.setHorizontalAlignment(JLabel.LEFT);
			lblWSD_Text.setForeground(MyColor.black);
			lblWSD_Text.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
			pane.add(lblWSD_Text);
			
			JLabel lblWSD = new JLabel(WSD+"m/s"); //강수확률 값 표시 라벨
			lblWSD.setForeground(MyColor.white);
			lblWSD.setBounds(350, 68, 100, 25);
			lblWSD.setVerticalAlignment(JLabel.TOP);
			lblWSD.setHorizontalAlignment(JLabel.RIGHT);
			lblWSD.setForeground(MyColor.lightBlack);
			lblWSD.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			pane.add(lblWSD);
				
		}
	}
	
	private class MyAction implements ActionListener{ //이벤트

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btn_today) { //오늘 버튼 클릭시
				if(!selectedDay.equalsIgnoreCase("today")) { //현재 표시중인 패널이 오늘이 아니라면
					card.show(mainPanel, "today"); //오늘자ㅣ 패널 표시
					selectedDay = "today";  //현재 표시패널 수정
					btn_today.setBackground(MyColor.black); //버튼 GUI수정
					btn_today.setFont(new Font("맑은 고딕", Font.BOLD, 24));
					btn_tomor.setBackground(MyColor.lightBlack);
					btn_tomor.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
				}
			} else if (e.getSource() == btn_tomor){ //내일 버튼 클릭시
				if(!selectedDay.equalsIgnoreCase("tomor")) { //현재 표시중인 패널이 내일이 아니라면
					selectedDay = "tomor"; //현재 표시패널 수정
					card.show(mainPanel, "tomor"); //내일자 패널 표시
					btn_tomor.setBackground(MyColor.black); //버튼 GUI 수정
					btn_tomor.setFont(new Font("맑은 고딕", Font.BOLD, 24));
					btn_today.setBackground(MyColor.lightBlack);
					btn_today.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
				}
			}else if (e.getSource() == todayPanel.btn_day || e.getSource() == tomorPanel.btn_day){ //날짜 클릭시 새로고침
				CheckGUI checkgui = new CheckGUI(mainFrame, "날씨 정보를 갱신하였습니다.", false, false);
				setInfo();
			} else if (e.getSource() == todayPanel.btn_AreaName|| e.getSource() == tomorPanel.btn_AreaName){ //지역 버튼 클릭시 지역 재설정 가능
				AreaSelectGUI asgui = new AreaSelectGUI(mainFrame);
			}
		}
	}
	
	private class DayPanel extends JPanel{ //오늘자, 내일자 패널 관리 하기위한 DayPanel
		
		JPanel listPanel; //하단 스크롤바에 표시될 날씨 시간표들 올라갈  곳
		JButton btn_AreaName; //지역명
		JButton btn_day; //일자
		JLabel lbl_weatherIcon; //날씨 아이콘
		JLabel lbl_weatherState; //날씨 상태
		JLabel lbl_nowTempe; //현재 기온
		JLabel lbl_TMX; //최고기온
		JLabel lbl_TMN; //최저기온
		JLabel lbl_POP; //강수량
		JLabel lbl_REH; //습도
		JLabel lbl_WSD;//풍속
		JScrollPane pane_scorll; //스크롤 
		
		public void setWeatherGui(List<WeatherInfo> wiList) { //해당 스크롤패널에 날씨 GUI 설정
			int yPos = 0; //y위치값
			int showCnt = 0; //보여지는 총 개수
			for(WeatherInfo wi : wiList) {//선택 주제가 ""라면 전체 표시임
				JPanel gui = wi.getGUI(); //GUI화 해서 표시
				gui.setBounds(0, yPos, pane_scorll.getSize().width-20, 100);
				listPanel.add(gui);
				yPos += 100;
				showCnt += 1;
			}
			
			if(showCnt >= 5) { //보여지는 GUI의 총 개수가 5개 이상이면 센터 패널의 높이 늘림
				listPanel.setPreferredSize(new Dimension(pane_scorll.getSize().width,pane_scorll.getSize().height+((showCnt-4)*100)));
			} else {
				listPanel.setPreferredSize(new Dimension(pane_scorll.getSize().width,pane_scorll.getSize().height-20));
			}
			listPanel.revalidate(); //센터패널 업데이트
			listPanel.repaint();
		}
		
	}
	
	private class TodayPanel extends DayPanel { //오늘자 패널 설정
		
		public TodayPanel() {
			this.setLayout(null);
			
			JPanel pane_center = new JPanel();
			pane_center.setBounds(0, 0, 494, 50);
			this.add(pane_center);
			pane_center.setBackground(MyColor.darkAqua);
			pane_center.setLayout(new GridLayout(0, 2, 0, 0));
			pane_center.setBorder(new LineBorder(MyColor.black, 1));
			
			/////////////각종 GUI 배치
			btn_AreaName = new JButton("지역명");
			btn_AreaName.setForeground(MyColor.black);
			btn_AreaName.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
			btn_AreaName.setHorizontalAlignment(JLabel.LEFT);
			btn_AreaName.setBorderPainted(false);
			btn_AreaName.setContentAreaFilled(false);
			btn_AreaName.setFocusPainted(false);
			btn_AreaName.addActionListener(myAction);
			pane_center.add(btn_AreaName);
			
			btn_day = new JButton("날짜");
			btn_day.setForeground(MyColor.black);
			btn_day.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
			btn_day.setHorizontalAlignment(JLabel.RIGHT);
			btn_day.setBorderPainted(false);
			btn_day.setContentAreaFilled(false);
			btn_day.setFocusPainted(false);
			btn_day.addActionListener(myAction);
			pane_center.add(btn_day);
			
			JPanel pane_Bottom = new JPanel();
			pane_Bottom.setBackground(MyColor.darkAqua);
			pane_Bottom.setBounds(0, 50, 494, 200);
			pane_Bottom.setBorder(new LineBorder(MyColor.black, 1));
			pane_Bottom.setLayout(null);
			this.add(pane_Bottom);
			
			lbl_weatherIcon = new JLabel("icon");
			lbl_weatherIcon.setBackground(Color.DARK_GRAY);
			lbl_weatherIcon.setBounds(12, 25, 100, 100);
			lbl_weatherIcon.setOpaque(false); //배경색 불투명
			pane_Bottom.add(lbl_weatherIcon);
			
			lbl_weatherState = new JLabel("");
			lbl_weatherState.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_weatherState.setBounds(125, 25, 100, 25);
			lbl_weatherState.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_weatherState);
			
			lbl_nowTempe = new JLabel("4º");
			lbl_nowTempe.setFont(new Font("맑은 고딕", Font.BOLD, 50));
			lbl_nowTempe.setBounds(125, 47, 100, 100);
			lbl_nowTempe.setForeground(MyColor.black);
			pane_Bottom.add(lbl_nowTempe);
			
			JLabel lbl_POP_Text = new JLabel("강수확률");
			lbl_POP_Text.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_POP_Text.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			lbl_POP_Text.setBounds(270, 40, 100, 25);
			lbl_POP_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_POP_Text);
			
			lbl_POP = new JLabel("");
			lbl_POP.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_POP.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			lbl_POP.setBounds(380, 40, 100, 25);
			lbl_POP.setForeground(MyColor.black);
			pane_Bottom.add(lbl_POP);
			
			JLabel lbl_REH_Text = new JLabel("습도");
			lbl_REH_Text.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_REH_Text.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			lbl_REH_Text.setBounds(270, 75, 100, 25);
			lbl_REH_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_REH_Text);
			
			lbl_REH = new JLabel("");
			lbl_REH.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_REH.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			lbl_REH.setBounds(380, 75, 100, 25);
			lbl_REH.setForeground(MyColor.black);
			pane_Bottom.add(lbl_REH);
			
			JLabel lbl_WSD_Text = new JLabel("풍속");
			lbl_WSD_Text.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_WSD_Text.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			lbl_WSD_Text.setBounds(270, 110, 100, 25);
			lbl_WSD_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_WSD_Text);
			
			lbl_WSD = new JLabel("");
			lbl_WSD.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_WSD.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			lbl_WSD.setBounds(380, 110, 100, 25);
			lbl_WSD.setForeground(MyColor.black);
			pane_Bottom.add(lbl_WSD);
			
			listPanel = new JPanel(); //센터 패널
			listPanel.setBackground(MyColor.darkAqua);
			listPanel.setLayout(null);
			
			pane_scorll = new JScrollPane(listPanel);
			pane_scorll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			pane_scorll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			pane_scorll.setBounds(0, 250, 494, 422);
			pane_scorll.getViewport().setBackground(MyColor.darkAqua);
			pane_scorll.setBorder(new LineBorder(MyColor.black, 1));
			pane_scorll.getVerticalScrollBar().setUI(new BasicScrollBarUI() { //스크롤바 색 설정
	            @Override
	            protected void configureScrollBarColors()
	            {
	                this.thumbColor = MyColor.white;
	            }
	        });

			this.add(pane_scorll);

		}
	}
	
	private class TomorPanel extends DayPanel {
		
		public TomorPanel() {
			this.setLayout(null);
			
			JPanel pane_center = new JPanel();
			pane_center.setBounds(0, 0, 494, 50);
			this.add(pane_center);
			pane_center.setBackground(MyColor.darkAqua);
			pane_center.setLayout(new GridLayout(0, 2, 0, 0));
			pane_center.setBorder(new LineBorder(MyColor.black, 1));
			/////////////각종 GUI 배치	
			btn_AreaName = new JButton("지역명");
			btn_AreaName.setForeground(MyColor.black);
			btn_AreaName.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
			btn_AreaName.setHorizontalAlignment(JLabel.LEFT);
			btn_AreaName.setBorderPainted(false);
			btn_AreaName.setContentAreaFilled(false);
			btn_AreaName.setFocusPainted(false);
			btn_AreaName.addActionListener(myAction);
			pane_center.add(btn_AreaName);
			
			btn_day = new JButton("날짜");
			btn_day.setForeground(MyColor.black);
			btn_day.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
			btn_day.setHorizontalAlignment(JLabel.RIGHT);
			btn_day.setBorderPainted(false);
			btn_day.setContentAreaFilled(false);
			btn_day.setFocusPainted(false);
			btn_day.addActionListener(myAction);
			pane_center.add(btn_day);
			
			JPanel pane_Bottom = new JPanel();
			pane_Bottom.setBackground(MyColor.darkAqua);
			pane_Bottom.setBounds(0, 50, 494, 100);
			pane_Bottom.setBorder(new LineBorder(MyColor.black, 1));
			pane_Bottom.setLayout(null);
			this.add(pane_Bottom);
			
			JLabel lbl_TMN_Text = new JLabel("아침 최저기온");
			lbl_TMN_Text.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_TMN_Text.setBounds(120, 10, 130, 35);
			lbl_TMN_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_TMN_Text);
			
			JLabel lbl_TMX_Text = new JLabel("낮 최고기온");
			lbl_TMX_Text.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_TMX_Text.setBounds(264, 10, 130, 35);
			lbl_TMX_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_TMX_Text);
			
			lbl_TMN = new JLabel("");
			lbl_TMN.setFont(new Font("맑은 고딕", Font.BOLD, 24));
			lbl_TMN.setBounds(120, 50, 130, 35);
			lbl_TMN.setForeground(MyColor.black);
			lbl_TMN.setHorizontalAlignment(JLabel.CENTER);
			pane_Bottom.add(lbl_TMN);
			
			lbl_TMX = new JLabel("");
			lbl_TMX.setFont(new Font("맑은 고딕", Font.BOLD, 24));
			lbl_TMX.setBounds(264, 50, 130, 35);
			lbl_TMX.setForeground(MyColor.black);
			lbl_TMX.setHorizontalAlignment(JLabel.CENTER);
			pane_Bottom.add(lbl_TMX);
			
			listPanel = new JPanel(); //센터 패널
			listPanel.setBackground(MyColor.darkAqua);
			listPanel.setLayout(null);
			
			pane_scorll = new JScrollPane(listPanel);
			pane_scorll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			pane_scorll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			pane_scorll.setBounds(0, 150, 494, 522);
			pane_scorll.getViewport().setBackground(MyColor.darkAqua);
			pane_scorll.setBorder(new LineBorder(MyColor.black, 1));
			pane_scorll.getVerticalScrollBar().setUI(new BasicScrollBarUI() { //스크롤바 색 설정
	            @Override
	            protected void configureScrollBarColors()
	            {
	                this.thumbColor = MyColor.white;
	            }
	        });

			this.add(pane_scorll);
		}
	}

}


