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
	private JComboBox<String> cb_sel2; // ����â
	private JComboBox<String> cb_sel1;
	private String[] cityNames;
	private LinkedHashMap<String, String[]> cityInfo;
	private MyAction myAction;
	private JButton btn_submit;
	private JFrame frame;
	private MainMenuGUI mainMenu;
	
	public AreaSelectGUI(MainMenuGUI mainMenu) { //���θ޴� ������
		
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
		this.addWindowListener(new JFrameWindowClosingEventHandler()); //â �ݱ� �̺�Ʈ
		
		Toolkit tk = Toolkit.getDefaultToolkit(); //������� ȭ�� ũ�Ⱚ�� ������� ��Ŷ Ŭ���� 
		
		//GUI����
		URL src = Client.class.getResource("/resources/baricon.png");  //������
		ImageIcon icon = new ImageIcon(src);
		this.setIconImage(icon.getImage());
		this.setTitle("Weather Schedule");
		setResizable(false);
		setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth /2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		getContentPane().setLayout(null);
		getContentPane().setBackground(MyColor.lightBlack);
		
		JLabel lbl_Title = new JLabel("���� ����"); //��ܿ� �������� ǥ��
		lbl_Title.setFont(new Font("���� ���", Font.BOLD, 20));
		lbl_Title.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Title.setBounds(72, 10, 100, 50);
		lbl_Title.setForeground(MyColor.white);
		getContentPane().add(lbl_Title);
		
		JLabel lbl_Select1 = new JLabel("��/��"); //�޺� �ڽ�1 ����  ��/�� ���� ǥ��
		lbl_Select1.setFont(new Font("���� ���", Font.PLAIN, 14));
		lbl_Select1.setBounds(12, 102, 50, 30);
		lbl_Select1.setForeground(MyColor.white);
		getContentPane().add(lbl_Select1);
		
		cb_sel1 = new JComboBox(cityNames); //�޺��ڽ�1 -> ���� ���
		cb_sel1.setBounds(82, 106, 150, 25);
		cb_sel1.setBackground(MyColor.darkAqua);
		cb_sel1.setForeground(MyColor.black);
		cb_sel1.setFont(new Font("���� ���", Font.PLAIN, 12));
		cb_sel1.addActionListener(myAction);
		getContentPane().add(cb_sel1);
		
		cb_sel2 = new JComboBox(); //�޺��ڽ�2 -> ���� ���
		cb_sel2.setBounds(82, 158, 150, 25);
		cb_sel2.setBackground(MyColor.darkAqua);
		cb_sel2.setForeground(MyColor.black);
		cb_sel2.setFont(new Font("���� ���", Font.PLAIN, 12));
		cb_sel2.addActionListener(myAction);
		getContentPane().add(cb_sel2);
		
		JLabel lbl_Select2 = new JLabel("���μ���");  //�޺� �ڽ�2 ���� ���μ��� ǥ��
		lbl_Select2.setFont(new Font("���� ���", Font.PLAIN, 14));
		lbl_Select2.setForeground(MyColor.white);
		lbl_Select2.setBounds(12, 154, 67, 30);
		getContentPane().add(lbl_Select2);
		
		btn_submit = new JButton("����"); //���� �Ϸ� ��ư
		btn_submit.setFont(new Font("���� ���", Font.PLAIN, 14));
		btn_submit.setBounds(72, 220, 100, 25);
		btn_submit.setBorder(new LineBorder(MyColor.white, 1));
		btn_submit.setFocusPainted(false);
		//btn_submit.setBorderPainted(false);
		btn_submit.setBackground(MyColor.white);
		btn_submit.setForeground(MyColor.black);
		btn_submit.addActionListener(myAction);
		getContentPane().add(btn_submit);

		updateTown(); //�޺��ڽ�1�� �ִ� ���ð��� ���� �޺��ڽ�2 ��� ����
		
		this.setVisible(true);
	}
	
	private void updateTown() { //�޺��ڽ�1�� �ִ� ���ð��� ���� �޺��ڽ�2 ��� ����
		String cityName = cityNames[cb_sel1.getSelectedIndex()]; //������ ���ø�
		String[] townList = cityInfo.get(cityName); //�ش� ���ø��� Key������ String[] �迭�� HashMap���� ������
		cb_sel2.removeAllItems();  //�޺��ڽ�2 �ʱ�ȭ
		for(String s : townList) { //�޺��ڽ�2�� ���׸�� �ֱ�
			cb_sel2.addItem(s);
		}
	}
	
	private boolean saveAreaData(String cityName, String townName) { //������ ����, Ÿ����� ������
		
		try {
			File file = new File("./area.txt");  //���
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(cityName+","+townName); //�������� ����
			bw.close();
			Client.cityName = cityName; //���ø�
			Client.townName = townName; //���׸�
		} catch (Exception e) {
			return false;
		}
		return true;
		
	}
	
	private class MyAction implements ActionListener{ //Ŭ�� �̺�Ʈ
		
		@Override
		public void actionPerformed(ActionEvent e) {	
			if(e.getSource() == cb_sel1) { //���� �޺��ڽ� ���ý�
				updateTown(); //���� �޺��ڽ� �缳��
			} else if(e.getSource() == btn_submit) {
				String cityName = cityNames[cb_sel1.getSelectedIndex()];
				String[] townNames = cityInfo.get(cityName); 
				String townName = townNames[cb_sel2.getSelectedIndex()];
				saveAreaData(cityName, townName);
				frame.dispose();
				if(mainMenu == null) { //���θ޴� ���� �� â�� ȣ��������
					new MainMenuGUI(); //â ��������
				} else { //���θ޴� �ִٸ�
					CheckGUI checkgui = new CheckGUI(frame, "���� �Ϸ�", false, true);
					mainMenu.setInfo(); //���� ���� ���ΰ�ħ
				}
			}
		}
	}
	
	private class JFrameWindowClosingEventHandler extends WindowAdapter { //â �ݱ��
		public void windowClosing(WindowEvent e) {
			if(mainMenu == null) { //���θ޴�â ���� ȣ���ߴٸ� (�� �� â�ۿ�����)
				System.exit(0); //���α׷� ����
			} else { //���θ޴�â�� �ִٸ� 
				frame.dispose(); //�� â ����
			}
		}
	}

}
