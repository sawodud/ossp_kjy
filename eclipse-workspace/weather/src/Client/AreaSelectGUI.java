package Client;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import Addon.MyColor;

public class AreaSelectGUI extends JFrame {

	private int frameWidth = 250;
	private int frameHeight = 300;
	private JComboBox<String> cb_sel2; // 선택창
	private JComboBox<String> cb_sel1;
	private String[] cityNames;
	private LinkedHashMap<String, String[]> cityInfo;
	private MyAction myAction;
	private JButton btn_submit;
	private JFrame frame;
	private MainMenuGUI mainMenu;
	
	public AreaSelectGUI(MainMenuGUI mainMenu) { //메인메뉴 프레임
		
		this.frame = this;
		this.mainMenu = mainMenu;
		
		try {
			LinkedHashMap<String, List<String>> areaInfo;
			areaInfo = Client.server.getAreaInfo(Client.ip);
			cityNames = new String[areaInfo.size()];
			cityInfo = new LinkedHashMap<String, String[]>();
			int index = 0;
			for(String city : areaInfo.keySet()) {
				cityNames[index++] = city;
				List<String> townList = areaInfo.get(city);
				String[] townNames = townList.toArray(new String[townList.size()]);
				cityInfo.put(city, townNames);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		myAction = new MyAction();
		this.addWindowListener(new JFrameWindowClosingEventHandler()); //창 닫기 이벤트
		
		Toolkit tk = Toolkit.getDefaultToolkit(); //사용자의 화면 크기값을 얻기위한 툴킷 클래스 
		
		//GUI설정
		URL src = Client.class.getResource("/resources/baricon.png");  //아이콘
		ImageIcon icon = new ImageIcon(src);
		this.setIconImage(icon.getImage());
		this.setTitle("Weather Schedule");
		setResizable(false);
		setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth /2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		getContentPane().setLayout(null);
		getContentPane().setBackground(MyColor.lightBlack);
		
		JLabel lbl_Title = new JLabel("지역 선택"); //상단에 지역선택 표시
		lbl_Title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		lbl_Title.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Title.setBounds(72, 10, 100, 50);
		lbl_Title.setForeground(MyColor.white);
		getContentPane().add(lbl_Title);
		
		JLabel lbl_Select1 = new JLabel("도/시"); //콤보 박스1 옆에  도/시 선택 표시
		lbl_Select1.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lbl_Select1.setBounds(12, 102, 50, 30);
		lbl_Select1.setForeground(MyColor.white);
		getContentPane().add(lbl_Select1);
		
		cb_sel1 = new JComboBox(cityNames); //콤보박스1 -> 도시 목록
		cb_sel1.setBounds(82, 106, 150, 25);
		cb_sel1.setBackground(MyColor.darkAqua);
		cb_sel1.setForeground(MyColor.black);
		cb_sel1.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		cb_sel1.addActionListener(myAction);
		getContentPane().add(cb_sel1);
		
		cb_sel2 = new JComboBox(); //콤보박스2 -> 동네 목록
		cb_sel2.setBounds(82, 158, 150, 25);
		cb_sel2.setBackground(MyColor.darkAqua);
		cb_sel2.setForeground(MyColor.black);
		cb_sel2.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		cb_sel2.addActionListener(myAction);
		getContentPane().add(cb_sel2);
		
		JLabel lbl_Select2 = new JLabel("세부선택");  //콤보 박스2 옆에 세부선택 표시
		lbl_Select2.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lbl_Select2.setForeground(MyColor.white);
		lbl_Select2.setBounds(12, 154, 67, 30);
		getContentPane().add(lbl_Select2);
		
		btn_submit = new JButton("설정"); //설정 완료 버튼
		btn_submit.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		btn_submit.setBounds(72, 220, 100, 25);
		btn_submit.setBorder(new LineBorder(MyColor.white, 1));
		btn_submit.setFocusPainted(false);
		//btn_submit.setBorderPainted(false);
		btn_submit.setBackground(MyColor.white);
		btn_submit.setForeground(MyColor.black);
		btn_submit.addActionListener(myAction);
		getContentPane().add(btn_submit);

		updateTown(); //콤보박스1에 있는 도시값에 따라 콤보박스2 목록 수정
		
		this.setVisible(true);
	}
	
	private void updateTown() { //콤보박스1에 있는 도시값에 따라 콤보박스2 목록 수정
		String cityName = cityNames[cb_sel1.getSelectedIndex()]; //선택한 도시명
		String[] townList = cityInfo.get(cityName); //해당 도시명을 Key값으로 String[] 배열을 HashMap에서 가져옴
		cb_sel2.removeAllItems();  //콤보박스2 초기화
		for(String s : townList) { //콤보박스2에 동네목록 넣기
			cb_sel2.addItem(s);
		}
	}
	
	private boolean saveAreaData(String cityName, String townName) { //설정한 도시, 타운값으로 재저장
		
		try {
			File file = new File("./area.txt");  //경로
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(cityName+","+townName); //지역정보 저장
			bw.close();
			Client.cityName = cityName; //도시명
			Client.townName = townName; //동네명
		} catch (Exception e) {
			return false;
		}
		return true;
		
	}
	
	private class MyAction implements ActionListener{ //클릭 이벤트
		
		@Override
		public void actionPerformed(ActionEvent e) {	
			if(e.getSource() == cb_sel1) { //도시 콤보박스 선택시
				updateTown(); //동네 콤보박스 재설정
			} else if(e.getSource() == btn_submit) {
				String cityName = cityNames[cb_sel1.getSelectedIndex()];
				String[] townNames = cityInfo.get(cityName); 
				String townName = townNames[cb_sel2.getSelectedIndex()];
				saveAreaData(cityName, townName);
				frame.dispose();
				if(mainMenu == null) { //메인메뉴 없이 이 창을 호출했으면
					new MainMenuGUI(); //창 생성해줌
				} else { //메인메뉴 있다면
					CheckGUI checkgui = new CheckGUI(frame, "설정 완료", false, true);
					mainMenu.setInfo(); //날씨 정보 새로고침
				}
			}
		}
	}
	
	private class JFrameWindowClosingEventHandler extends WindowAdapter { //창 닫기시
		public void windowClosing(WindowEvent e) {
			if(mainMenu == null) { //메인메뉴창 없이 호출했다면 (즉 이 창밖에없음)
				System.exit(0); //프로그램 종료
			} else { //메인메뉴창이 있다면 
				frame.dispose(); //이 창 종료
			}
		}
	}

}
