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
// ä��â ������ ���⿡ �ִ°ɷ� �սô�. *******
public class MainMenuGUI extends JFrame {
	private int frameWidth = 500; //����
	private int frameHeight = 750; //����
	private JPanel mainPanel; 
	private CardLayout card; //������, ������ �� ȭ���� 2���̹Ƿ� ī�� ���̾ƿ� ����Ͽ� ȭ�� ��ȯ
	private DayPanel todayPanel; //������ �г�
	private DayPanel tomorPanel; //������ �г�
	//private DayPanel chatPanel; // ä��â �г�
	private JButton btn_tomor; //���� ��ư
	private JButton btn_today; //���� ��ư
	//private JButton btn_chatting; // ä�� ��ư
	private MyAction myAction; //�̺�Ʈ
	private String selectedDay = "today"; //���� ������ �г�(������? ������?)
	private MainMenuGUI mainFrame; //�� ������
	
	public MainMenuGUI() { //���θ޴� ������
		mainFrame = this; //������ �ּ� ����
		
		Toolkit tk = Toolkit.getDefaultToolkit(); //������� ȭ�� ũ�Ⱚ�� ������� ��Ŷ Ŭ���� 
		
		//GUI ����
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		URL src = Client.class.getResource("/resources/baricon.png");
		ImageIcon icon = new ImageIcon(src);
		this.setIconImage(icon.getImage());
		this.setTitle("Weather Schedule");
		setResizable(false); // â���� �Ұ���
		setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth /2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		getContentPane().setLayout(null);
		getContentPane().setBackground(MyColor.darkAqua);
		
		myAction = new MyAction();
		todayPanel = new TodayPanel(); //������ ������
		tomorPanel = new TomorPanel(); //������ ������
		
		JPanel pane_DaySelect = new JPanel();
		pane_DaySelect.setBounds(0, 0, 494, 50);
		getContentPane().add(pane_DaySelect);
		pane_DaySelect.setBackground(MyColor.black);
		pane_DaySelect.setLayout(new GridLayout(0, 2, 0, 0));
		pane_DaySelect.setBorder(new LineBorder(MyColor.black, 1));
		
		btn_today = new JButton("����"); //���� ��ư
		btn_today.setFont(new Font("���� ���", Font.BOLD, 24));
		btn_today.setBackground(MyColor.black);
		btn_today.setForeground(MyColor.white);
		btn_today.setFocusPainted(false);
		btn_today.setBorderPainted(false);
		btn_today.addActionListener(myAction);
		//btn_today.setBorder(new LineBorder(MyColor.white, 1));
		pane_DaySelect.add(btn_today);
		
		btn_tomor = new JButton("����"); //���� ��ư
		btn_tomor.setFont(new Font("���� ���", Font.PLAIN, 24));
		btn_tomor.setBackground(MyColor.lightBlack);
		btn_tomor.setForeground(MyColor.white);
		btn_tomor.setFocusPainted(false);
		btn_tomor.setBorderPainted(false);
		btn_tomor.addActionListener(myAction);
		//btn_tomor.setBorder(new LineBorder(MyColor.white, 1));
		pane_DaySelect.add(btn_tomor);
		
		mainPanel = new JPanel(); //ȭ�� ��ȯ�� �г�
		mainPanel.setBounds(0,50,494,700);
		mainPanel.setBackground(MyColor.darkAqua);
		card = new CardLayout(); //ī�巹�̾ƿ� �ּ�
		mainPanel.setLayout(card);
		mainPanel.add("today", todayPanel);
		mainPanel.add("tomor", tomorPanel);
		getContentPane().add(mainPanel);
		card.show(mainPanel, "today"); //ó������ ������ �г� ǥ��
		
		//setInfo(); //������, ������ �гο� ��������� GUI ����
		
		this.setVisible(true);
		
	}
	
	public void setInfo() {
		todayPanel.btn_AreaName.setText(Client.cityName+" "+Client.townName); //������ �г� ��ȸ ���� �̸� ����
		tomorPanel.btn_AreaName.setText(Client.cityName+" "+Client.townName); //������ �г� ��ȸ ���� �̸� ����
		
		
		List<LinkedHashMap<String, String>> weatherList = null;
		try {
			weatherList = Client.server.getWeatherData(Client.ip, Client.cityName, Client.townName); //�������� ������� �޾ƿ�
		} catch (RemoteException e) { //RMI ����̻��
			CheckGUI checkgui = new CheckGUI((JFrame) this, "���� ��� ����", true, true);
			e.printStackTrace();
		}
		if(weatherList == null) { //�������� �ƹ����� ������ ��
			CheckGUI checkgui = new CheckGUI((JFrame) this, "���� ��� ����", true, true);
			return;
		}
		/*for (LinkedHashMap<String, String> map : weatherList) { //�� Ȯ�ο�
			System.out.println("------------------");
			for (String key : map.keySet()) {
				System.out.println(key + " : " + map.get(key));
			}
		}*/
		LinkedHashMap<String, String> now_weatherMap = weatherList.get(0); //���� ���� ����
		LinkedHashMap<String, String> today_weatherMap = weatherList.get(1); //������ ���� ������ ���� ����
		LinkedHashMap<String, String> tomor_weatherMap = weatherList.get(2); //������ ���� ����
		LinkedHashMap<String, String> tomor_tm_Map = weatherList.get(3); //������ ����, �ְ��� ����
		
		String dayValue = now_weatherMap.get("day"); //��ȸ ���� �Ľ��ؼ� ǥ��
		String year = dayValue.substring(2,4)+"��"; 
		String mon = dayValue.substring(4,6)+"��";
		String day = dayValue.substring(6,8)+"��";
		todayPanel.btn_day.setText(year+" "+mon+" "+day);
		todayPanel.lbl_nowTempe.setText(now_weatherMap.get("T3H")+"��"); //���� �µ� ����
		todayPanel.lbl_POP.setText(now_weatherMap.get("POP")+"%"); //���� ����Ȯ�� ����
		todayPanel.lbl_REH.setText(now_weatherMap.get("REH")+"��"); //���� ���� ����
		todayPanel.lbl_WSD.setText(now_weatherMap.get("WSD")+"m/s"); //���� ǳ�� ����
		
		String weatherState = getWeatherState(now_weatherMap.get("PTY"), now_weatherMap.get("SKY"));  //�������� �����ͼ� ���¿�  ���� icon ����
		URL src = Client.class.getResource("/resources/"+weatherState+".png");
		ImageIcon img = new ImageIcon(src);
		todayPanel.lbl_weatherIcon.setIcon(img);
		todayPanel.lbl_weatherState.setText(weatherToKor(weatherState));
			
		List<WeatherInfo> todayWeatherList = new ArrayList<WeatherInfo>(); //������ ���� ���� ����Ʈ
		List<String> keyList = new ArrayList<String>(today_weatherMap.keySet()); //Ű ����Ʈ
		LinkedHashMap<String, String> parseMap = new LinkedHashMap<String, String>(); //�Ľ����� �� �����
		String parseDay = today_weatherMap.get(keyList.get(0)); //�Ľ��� ����
		String parseTime = ""; //�Ľ����� �ð���
		for(int i = 1; i < keyList.size(); i++) { //0���� day ���̴� �Ľ��� �ʿ� ����
			String key = keyList.get(i);
			String time = key.substring(0,4); //���������� �ð��� �Ľ�
			if(parseTime.equalsIgnoreCase("")) parseTime = time;
			if(!parseTime.equalsIgnoreCase(time)) { //���� �Ľ��ϰ� �ִ� �ð����ϰ� ��ġ���������� �ش� �ð���� �Ľ� ��
				//�׵��� ���� ������ ���� ������ �����ؼ� ��������
				WeatherInfo wi = createWeatherInfo(parseDay, parseTime, parseMap); //���� ���� ����
				if(wi != null) todayWeatherList.add(wi); //����
				parseMap.clear(); //�ʱ�ȭ
				parseTime = time; //�ð��� ���ο� �ð����
			}
			String category = key.substring(4,7); //���� ī�װ�
			parseMap.put(category, today_weatherMap.get(keyList.get(i))); //�ش� ī�װ��� ���� ��
			System.out.println(category+" : "+today_weatherMap.get(keyList.get(i)));
		}
		//���� ������ �ֱ�
				WeatherInfo wi = createWeatherInfo(parseDay, parseTime, parseMap); //���� ���� ����
				if(wi != null) todayWeatherList.add(wi); //����

		
		////////////������
		List<WeatherInfo> tomorWeatherList = new ArrayList<WeatherInfo>(); //������ ���� ����Ʈ
		keyList = new ArrayList<String>(tomor_weatherMap.keySet()); //Ű ����Ʈ
		parseMap.clear(); //�Ľ����� �� �����
		parseDay = tomor_weatherMap.get(keyList.get(0)); //�Ľ��� ����
		dayValue = parseDay;
		year = dayValue.substring(2,4)+"��";
		mon = dayValue.substring(4,6)+"��";
		day = dayValue.substring(6,8)+"��";
		tomorPanel.btn_day.setText(year+" "+mon+" "+day);
		parseTime = ""; //�Ľ����� �ð���
		for(int i = 1; i < keyList.size(); i++) { //0���� day ���̴� �Ľ��� �ʿ� ����
			String key = keyList.get(i);
			String time = key.substring(0,4); //���������� �ð��� �Ľ�
			if(parseTime.equalsIgnoreCase("")) parseTime = time;
			if(!parseTime.equalsIgnoreCase(time)) { //���� �Ľ��ϰ� �ִ� �ð����ϰ� ��ġ���������� �ش� �ð���� �Ľ� ��
				//�׵��� ���� ������ ���� ������ �����ؼ� ��������
				wi = createWeatherInfo(parseDay, parseTime, parseMap); //���� ���� ����
				if(wi != null) tomorWeatherList.add(wi); //����
				parseMap.clear(); //�ʱ�ȭ
				parseTime = time; //�ð��� ���ο� �ð����
			}
			String category = key.substring(4,7); //���� ī�װ�
			parseMap.put(category, tomor_weatherMap.get(keyList.get(i))); //�ش� ī�װ��� ���� ��
			//System.out.println(category+" : "+tomor_weatherMap.get(keyList.get(i))); �� Ȯ�ο�
		}
		//���� ������ �ֱ�
		wi = createWeatherInfo(parseDay, parseTime, parseMap); //���� ���� ����
		if(wi != null) tomorWeatherList.add(wi); //����
		
		for(Component c : todayPanel.listPanel.getComponents()) { //������ ���� �гο� �ִ� ������Ʈ ��� ����
			todayPanel.listPanel.remove(c);
		}	
		todayPanel.setWeatherGui(todayWeatherList);
		
		for(Component c : tomorPanel.listPanel.getComponents()) { //������ ���� �гο� �ִ� ������Ʈ ��� ����
			tomorPanel.listPanel.remove(c);
		}	
		tomorPanel.setWeatherGui(tomorWeatherList);
		
		String tomor_TMN = tomor_tm_Map.get("TMN"); //���� �������
		String tomor_TMX = tomor_tm_Map.get("TMX"); //���� �ְ���
		tomorPanel.lbl_TMX.setText(tomor_TMX+"��"); //ǥ��
		tomorPanel.lbl_TMN.setText(tomor_TMN+"��");
	}
	
	private WeatherInfo createWeatherInfo(String parseDay, String parseTime, LinkedHashMap<String, String> parseMap) { //������� Ŭ���� ����
		WeatherInfo wi = null; 
		String T3H = parseMap.get("T3H"); //�Ľ��� ����
		String POP = parseMap.get("POP");
		String REH = parseMap.get("REH");
		String SKY = parseMap.get("SKY");
		String PTY = parseMap.get("PTY");
		String WSD = parseMap.get("WSD");
		
		if(T3H == null || POP == null || REH == null ||SKY == null ||PTY == null ||WSD == null) {
			//�ʿ��� ī�װ��� �ϳ��� �����ִٸ� �ش� ���� �н���
			System.out.println(parseDay+" - "+parseTime+" ���� �Ľ̹��� �߻�");
		} else {
			wi = new WeatherInfo(parseDay, parseTime, T3H, POP, REH, SKY, PTY, WSD); //���� ���� ����
		}
		return wi;
	}
	
	private String getWeatherState(String pty, String sky) {
		//pty //�׽�Ʈ��
		//sky = "2"; //�׽�Ʈ��
		String weatherState = "clear";
		if (!pty.equalsIgnoreCase("0")) { //�ϴÿ��� ���� ���� �ִٸ�
			switch (pty) {
			case "1":
				weatherState = "rain";
				break; // ���
			case "2":
				weatherState = "snowrain";
				break; // �����
			case "3":
				weatherState = "snow";
				break; // ����
			case "4":
				weatherState = "rain";
				break; // �ҳ���
			default:
				pty = "0";
			}
		}
		if (pty.equalsIgnoreCase("0")) { //�� ���� ���� ������
			switch (sky) {
			case "1":
				weatherState = "clear";
				break; // ����
			case "2":
				weatherState = "cloudbig";
				break; // ��������
			case "3":
				weatherState = "cloudbig";
				break; // ��������
			case "4":
				weatherState = "cloudy";
				break; // �帲
			default:
				weatherState = "clear";
			}
		}
		return weatherState;
	}
	
	
	private String weatherToKor(String weatherState) { //���� ���� ������ �ѱ۷� �ٲ���
		if(weatherState.equalsIgnoreCase("cloudbig")) return "��������";
		else if(weatherState.equalsIgnoreCase("cloudy")) return "�帲";
		else if(weatherState.equalsIgnoreCase("rain")) return "��";
		else if(weatherState.equalsIgnoreCase("snow")) return "��";
		else if(weatherState.equalsIgnoreCase("snowrain")) return "��/��";
		else return "����";
	}
	
	private class WeatherInfo{
		String day; //���� ���� ��¥
		String time; //���� ���� �ð�
		String T3H; //���
		String POP; //����Ȯ��
		String REH; //����
		String SKY; //�ϴû���
		String PTY; //��������
		String WSD; //��������
		
		public WeatherInfo(String day, String time, String T3H, String POP, String REH, String SKY, String PTY, String WSD) { //ä�ù� ���� ����
			this.day = day; //�ʱ�ȭ
			this.time = time;
			this.T3H = T3H;
			this.POP = POP;
			this.REH = REH;
			this.PTY = PTY;
			this.SKY = SKY;
			this.WSD = WSD;
		}
		
		public JPanel getGUI() { //�ش� ���� ������ ���� gui�г��� �������
				
			JPanel pane = new JPanel(); //��ȯ�� �г�
			pane.setLayout(null); //���� ��ġ

			setGuiComponents(pane); //������Ʈ ����
			
			pane.setBorder(new LineBorder(MyColor.lightBlack, 1));
			pane.setBackground(MyColor.darkAqua);
			
			return pane;
		}
		
		public void setGuiComponents(JPanel pane) { //���� ������ ���� ������Ʈ ����
			
			String weatherState = getWeatherState(PTY, SKY); //���� ���� ���� ������
			
			ImageIcon icon = new ImageIcon(MainMenuGUI.class.getResource("/resources/"+weatherState+".png"));  //������ ���� �̹��� ����
			Image resizeImg = icon.getImage();  //ImageIcon�� Image�� ��ȯ.
			Image resizedImg = resizeImg.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
			ImageIcon resizedicon = new ImageIcon(resizedImg); //Image�� ImageIcon ����
			JLabel titleImg = new JLabel(resizedicon);
			titleImg.setBounds(5, 10, 50, 50);
			pane.add(titleImg);
			
			String timeText = time.substring(0,2) + "��";
			JLabel lblTime = new JLabel(timeText); //�ð� ǥ�� ��
			lblTime.setForeground(MyColor.white);
			lblTime.setHorizontalAlignment(JLabel.CENTER);
			lblTime.setBounds(5, 65, 50, 25);
			lblTime.setForeground(MyColor.black);
			lblTime.setFont(new Font("���� ���", Font.BOLD, 12));
			pane.add(lblTime);
			
			JLabel lblT3H = new JLabel(T3H+"��"); //��� ǥ�� ��
			lblT3H.setForeground(MyColor.white);
			lblT3H.setBounds(70, 37, 100, 35);
			lblT3H.setFont(new Font("���� ���", Font.BOLD, 35));
			lblT3H.setForeground(MyColor.lightBlack);
			lblT3H.setVerticalAlignment(JLabel.CENTER);
			pane.add(lblT3H);
			
			
			JLabel lblState = new JLabel(weatherToKor(weatherState)); //��� ǥ�� ��
			lblState.setForeground(MyColor.white);
			lblState.setBounds(70, 10, 100, 25);
			lblState.setFont(new Font("���� ���", Font.BOLD, 12));
			lblState.setForeground(MyColor.lightBlack);
			lblState.setVerticalAlignment(JLabel.CENTER);
			pane.add(lblState);
			
			JLabel lblPOP_Text = new JLabel("����Ȯ��"); //����Ȯ�� �ؽ�Ʈ ǥ�� ��
			lblPOP_Text.setForeground(MyColor.white);
			lblPOP_Text.setBounds(290, 8, 80, 25);
			lblPOP_Text.setVerticalAlignment(JLabel.TOP);
			lblPOP_Text.setHorizontalAlignment(JLabel.LEFT);
			lblPOP_Text.setForeground(MyColor.black);
			lblPOP_Text.setFont(new Font("���� ���", Font.PLAIN, 18));
			pane.add(lblPOP_Text);
			
			JLabel lblPOP = new JLabel(POP+"%"); //����Ȯ�� �� ǥ�� ��
			lblPOP.setForeground(MyColor.white);
			lblPOP.setBounds(350, 8, 100, 25);
			lblPOP.setVerticalAlignment(JLabel.TOP);
			lblPOP.setHorizontalAlignment(JLabel.RIGHT);
			lblPOP.setForeground(MyColor.lightBlack);
			lblPOP.setFont(new Font("���� ���", Font.BOLD, 18));
			pane.add(lblPOP);
			
			JLabel lblREH_Text = new JLabel("����"); //����Ȯ�� �ؽ�Ʈ ǥ�� ��
			lblREH_Text.setForeground(MyColor.white);
			lblREH_Text.setBounds(290, 38, 80, 25);
			lblREH_Text.setVerticalAlignment(JLabel.TOP);
			lblREH_Text.setHorizontalAlignment(JLabel.LEFT);
			lblREH_Text.setForeground(MyColor.black);
			lblREH_Text.setFont(new Font("���� ���", Font.PLAIN, 18));
			pane.add(lblREH_Text);
			
			JLabel lblREH = new JLabel(REH+"%"); //����Ȯ�� �� ǥ�� ��
			lblREH.setForeground(MyColor.white);
			lblREH.setBounds(350, 38, 100, 25);
			lblREH.setVerticalAlignment(JLabel.TOP);
			lblREH.setHorizontalAlignment(JLabel.RIGHT);
			lblREH.setForeground(MyColor.lightBlack);
			lblREH.setFont(new Font("���� ���", Font.BOLD, 18));
			pane.add(lblREH);
			
			JLabel lblWSD_Text = new JLabel("ǳ��"); //����Ȯ�� �ؽ�Ʈ ǥ�� ��
			lblWSD_Text.setForeground(MyColor.white);
			lblWSD_Text.setBounds(290, 68, 80, 25);
			lblWSD_Text.setVerticalAlignment(JLabel.TOP);
			lblWSD_Text.setHorizontalAlignment(JLabel.LEFT);
			lblWSD_Text.setForeground(MyColor.black);
			lblWSD_Text.setFont(new Font("���� ���", Font.PLAIN, 18));
			pane.add(lblWSD_Text);
			
			JLabel lblWSD = new JLabel(WSD+"m/s"); //����Ȯ�� �� ǥ�� ��
			lblWSD.setForeground(MyColor.white);
			lblWSD.setBounds(350, 68, 100, 25);
			lblWSD.setVerticalAlignment(JLabel.TOP);
			lblWSD.setHorizontalAlignment(JLabel.RIGHT);
			lblWSD.setForeground(MyColor.lightBlack);
			lblWSD.setFont(new Font("���� ���", Font.BOLD, 18));
			pane.add(lblWSD);
				
		}
	}
	
	private class MyAction implements ActionListener{ //�̺�Ʈ

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == btn_today) { //���� ��ư Ŭ����
				if(!selectedDay.equalsIgnoreCase("today")) { //���� ǥ������ �г��� ������ �ƴ϶��
					card.show(mainPanel, "today"); //�����ڤ� �г� ǥ��
					selectedDay = "today";  //���� ǥ���г� ����
					btn_today.setBackground(MyColor.black); //��ư GUI����
					btn_today.setFont(new Font("���� ���", Font.BOLD, 24));
					btn_tomor.setBackground(MyColor.lightBlack);
					btn_tomor.setFont(new Font("���� ���", Font.PLAIN, 24));
				}
			} else if (e.getSource() == btn_tomor){ //���� ��ư Ŭ����
				if(!selectedDay.equalsIgnoreCase("tomor")) { //���� ǥ������ �г��� ������ �ƴ϶��
					selectedDay = "tomor"; //���� ǥ���г� ����
					card.show(mainPanel, "tomor"); //������ �г� ǥ��
					btn_tomor.setBackground(MyColor.black); //��ư GUI ����
					btn_tomor.setFont(new Font("���� ���", Font.BOLD, 24));
					btn_today.setBackground(MyColor.lightBlack);
					btn_today.setFont(new Font("���� ���", Font.PLAIN, 24));
				}
			}else if (e.getSource() == todayPanel.btn_day || e.getSource() == tomorPanel.btn_day){ //��¥ Ŭ���� ���ΰ�ħ
				CheckGUI checkgui = new CheckGUI(mainFrame, "���� ������ �����Ͽ����ϴ�.", false, false);
				setInfo();
			} else if (e.getSource() == todayPanel.btn_AreaName|| e.getSource() == tomorPanel.btn_AreaName){ //���� ��ư Ŭ���� ���� �缳�� ����
				AreaSelectGUI asgui = new AreaSelectGUI(mainFrame);
			}
		}
	}
	
	private class DayPanel extends JPanel{ //������, ������ �г� ���� �ϱ����� DayPanel
		
		JPanel listPanel; //�ϴ� ��ũ�ѹٿ� ǥ�õ� ���� �ð�ǥ�� �ö�  ��
		JButton btn_AreaName; //������
		JButton btn_day; //����
		JLabel lbl_weatherIcon; //���� ������
		JLabel lbl_weatherState; //���� ����
		JLabel lbl_nowTempe; //���� ���
		JLabel lbl_TMX; //�ְ���
		JLabel lbl_TMN; //�������
		JLabel lbl_POP; //������
		JLabel lbl_REH; //����
		JLabel lbl_WSD;//ǳ��
		JScrollPane pane_scorll; //��ũ�� 
		
		public void setWeatherGui(List<WeatherInfo> wiList) { //�ش� ��ũ���гο� ���� GUI ����
			int yPos = 0; //y��ġ��
			int showCnt = 0; //�������� �� ����
			for(WeatherInfo wi : wiList) {//���� ������ ""��� ��ü ǥ����
				JPanel gui = wi.getGUI(); //GUIȭ �ؼ� ǥ��
				gui.setBounds(0, yPos, pane_scorll.getSize().width-20, 100);
				listPanel.add(gui);
				yPos += 100;
				showCnt += 1;
			}
			
			if(showCnt >= 5) { //�������� GUI�� �� ������ 5�� �̻��̸� ���� �г��� ���� �ø�
				listPanel.setPreferredSize(new Dimension(pane_scorll.getSize().width,pane_scorll.getSize().height+((showCnt-4)*100)));
			} else {
				listPanel.setPreferredSize(new Dimension(pane_scorll.getSize().width,pane_scorll.getSize().height-20));
			}
			listPanel.revalidate(); //�����г� ������Ʈ
			listPanel.repaint();
		}
		
	}
	
	private class TodayPanel extends DayPanel { //������ �г� ����
		
		public TodayPanel() {
			this.setLayout(null);
			
			JPanel pane_center = new JPanel();
			pane_center.setBounds(0, 0, 494, 50);
			this.add(pane_center);
			pane_center.setBackground(MyColor.darkAqua);
			pane_center.setLayout(new GridLayout(0, 2, 0, 0));
			pane_center.setBorder(new LineBorder(MyColor.black, 1));
			
			/////////////���� GUI ��ġ
			btn_AreaName = new JButton("������");
			btn_AreaName.setForeground(MyColor.black);
			btn_AreaName.setFont(new Font("���� ���", Font.PLAIN, 16));
			btn_AreaName.setHorizontalAlignment(JLabel.LEFT);
			btn_AreaName.setBorderPainted(false);
			btn_AreaName.setContentAreaFilled(false);
			btn_AreaName.setFocusPainted(false);
			btn_AreaName.addActionListener(myAction);
			pane_center.add(btn_AreaName);
			
			btn_day = new JButton("��¥");
			btn_day.setForeground(MyColor.black);
			btn_day.setFont(new Font("���� ���", Font.PLAIN, 16));
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
			lbl_weatherIcon.setOpaque(false); //���� ������
			pane_Bottom.add(lbl_weatherIcon);
			
			lbl_weatherState = new JLabel("");
			lbl_weatherState.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_weatherState.setBounds(125, 25, 100, 25);
			lbl_weatherState.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_weatherState);
			
			lbl_nowTempe = new JLabel("4��");
			lbl_nowTempe.setFont(new Font("���� ���", Font.BOLD, 50));
			lbl_nowTempe.setBounds(125, 47, 100, 100);
			lbl_nowTempe.setForeground(MyColor.black);
			pane_Bottom.add(lbl_nowTempe);
			
			JLabel lbl_POP_Text = new JLabel("����Ȯ��");
			lbl_POP_Text.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_POP_Text.setFont(new Font("���� ���", Font.BOLD, 16));
			lbl_POP_Text.setBounds(270, 40, 100, 25);
			lbl_POP_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_POP_Text);
			
			lbl_POP = new JLabel("");
			lbl_POP.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_POP.setFont(new Font("���� ���", Font.BOLD, 16));
			lbl_POP.setBounds(380, 40, 100, 25);
			lbl_POP.setForeground(MyColor.black);
			pane_Bottom.add(lbl_POP);
			
			JLabel lbl_REH_Text = new JLabel("����");
			lbl_REH_Text.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_REH_Text.setFont(new Font("���� ���", Font.BOLD, 16));
			lbl_REH_Text.setBounds(270, 75, 100, 25);
			lbl_REH_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_REH_Text);
			
			lbl_REH = new JLabel("");
			lbl_REH.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_REH.setFont(new Font("���� ���", Font.BOLD, 16));
			lbl_REH.setBounds(380, 75, 100, 25);
			lbl_REH.setForeground(MyColor.black);
			pane_Bottom.add(lbl_REH);
			
			JLabel lbl_WSD_Text = new JLabel("ǳ��");
			lbl_WSD_Text.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_WSD_Text.setFont(new Font("���� ���", Font.BOLD, 16));
			lbl_WSD_Text.setBounds(270, 110, 100, 25);
			lbl_WSD_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_WSD_Text);
			
			lbl_WSD = new JLabel("");
			lbl_WSD.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_WSD.setFont(new Font("���� ���", Font.BOLD, 16));
			lbl_WSD.setBounds(380, 110, 100, 25);
			lbl_WSD.setForeground(MyColor.black);
			pane_Bottom.add(lbl_WSD);
			
			listPanel = new JPanel(); //���� �г�
			listPanel.setBackground(MyColor.darkAqua);
			listPanel.setLayout(null);
			
			pane_scorll = new JScrollPane(listPanel);
			pane_scorll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			pane_scorll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			pane_scorll.setBounds(0, 250, 494, 422);
			pane_scorll.getViewport().setBackground(MyColor.darkAqua);
			pane_scorll.setBorder(new LineBorder(MyColor.black, 1));
			pane_scorll.getVerticalScrollBar().setUI(new BasicScrollBarUI() { //��ũ�ѹ� �� ����
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
			/////////////���� GUI ��ġ	
			btn_AreaName = new JButton("������");
			btn_AreaName.setForeground(MyColor.black);
			btn_AreaName.setFont(new Font("���� ���", Font.PLAIN, 16));
			btn_AreaName.setHorizontalAlignment(JLabel.LEFT);
			btn_AreaName.setBorderPainted(false);
			btn_AreaName.setContentAreaFilled(false);
			btn_AreaName.setFocusPainted(false);
			btn_AreaName.addActionListener(myAction);
			pane_center.add(btn_AreaName);
			
			btn_day = new JButton("��¥");
			btn_day.setForeground(MyColor.black);
			btn_day.setFont(new Font("���� ���", Font.PLAIN, 16));
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
			
			JLabel lbl_TMN_Text = new JLabel("��ħ �������");
			lbl_TMN_Text.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_TMN_Text.setBounds(120, 10, 130, 35);
			lbl_TMN_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_TMN_Text);
			
			JLabel lbl_TMX_Text = new JLabel("�� �ְ���");
			lbl_TMX_Text.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_TMX_Text.setBounds(264, 10, 130, 35);
			lbl_TMX_Text.setForeground(MyColor.lightBlack);
			pane_Bottom.add(lbl_TMX_Text);
			
			lbl_TMN = new JLabel("");
			lbl_TMN.setFont(new Font("���� ���", Font.BOLD, 24));
			lbl_TMN.setBounds(120, 50, 130, 35);
			lbl_TMN.setForeground(MyColor.black);
			lbl_TMN.setHorizontalAlignment(JLabel.CENTER);
			pane_Bottom.add(lbl_TMN);
			
			lbl_TMX = new JLabel("");
			lbl_TMX.setFont(new Font("���� ���", Font.BOLD, 24));
			lbl_TMX.setBounds(264, 50, 130, 35);
			lbl_TMX.setForeground(MyColor.black);
			lbl_TMX.setHorizontalAlignment(JLabel.CENTER);
			pane_Bottom.add(lbl_TMX);
			
			listPanel = new JPanel(); //���� �г�
			listPanel.setBackground(MyColor.darkAqua);
			listPanel.setLayout(null);
			
			pane_scorll = new JScrollPane(listPanel);
			pane_scorll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			pane_scorll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			pane_scorll.setBounds(0, 150, 494, 522);
			pane_scorll.getViewport().setBackground(MyColor.darkAqua);
			pane_scorll.setBorder(new LineBorder(MyColor.black, 1));
			pane_scorll.getVerticalScrollBar().setUI(new BasicScrollBarUI() { //��ũ�ѹ� �� ����
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


