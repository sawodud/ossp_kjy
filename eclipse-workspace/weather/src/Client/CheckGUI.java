package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Addon.MyColor;
import Addon.MyUtility;

public class CheckGUI extends JFrame{ //����ڿ��� �˸��� ���� ���� ������
	
	static CheckGUI lastFrame = null; //CheckGUI�� 2���� ������ ���� �װ� ����
	boolean programExit = false; //�� �������� ������ ���α׷����� ������ ���ΰ�?
	boolean disposeCaller = false; //�� �����ָ� ������ �� �������� ȣ���� ������ ���� ���� ���ΰ�?
	JFrame callerFrame; //ȣ���� ������
	JButton okButton; //Ȯ�ι�ư
	
	//ȣ���� ������, �˸� �޽���, ���α׷� ���� ����, ȣ�� ������ ���� ����
	public CheckGUI(JFrame callerFrame, String msg, boolean programExit,boolean disposeCaller) { 
		if(lastFrame != null) { //������ 2�� ���� ����
			lastFrame.dispose();
		}
		lastFrame = this;
		this.callerFrame = callerFrame; //�ʱ�ȭ
		this.programExit = programExit;
		this.disposeCaller = disposeCaller;
		
		msg = MyUtility.lineSpacing(msg, 16); //�˸� �޽��� 16���� �������� �� ����
		createFrame(msg, 14); //������ ����
	}
	
	private void createFrame(String msg, float fontSize) { //�˸� �޽���, ��Ʈ ������
		Toolkit tk = Toolkit.getDefaultToolkit(); //������ ����� ���� ����
		Dimension screenSize = tk.getScreenSize();
		
		MyEvent myActionEvent = new MyEvent(this); //Ȯ�ι�ư Ŭ�� �̺�Ʈ��
		
		this.addWindowListener(new JFrameWindowClosingEventHandler()); //������ â �ݱ� ��
		
		this.setResizable(false);
		this.setBounds(screenSize.width / 2 - 125, screenSize.height / 2 - 75, 250, 150);
		this.setTitle("Ȯ��");
		URL src = CheckGUI.class.getResource("/resources/baricon.png"); //icon �̹���
		ImageIcon icon = new ImageIcon(src);
		this.setIconImage(icon.getImage());
		
		JPanel pane = new JPanel(); //���� �г�
		pane.setBackground(MyColor.lightBlack);
		pane.setLayout(new BorderLayout());	
		
		this.setContentPane(pane);
		
		JLabel msgLabel = new JLabel(msg); //�˸� �޽��� ��
		msgLabel.setFont(new Font("���� ���", Font.PLAIN, (int)fontSize));
		msgLabel.setForeground(MyColor.white);
		msgLabel.setHorizontalAlignment(JLabel.CENTER);
		
		okButton = new JButton("Ȯ��"); //Ȯ�� ��ư
		okButton.addActionListener(myActionEvent);
		okButton.setBackground(MyColor.white);
		okButton.setForeground(MyColor.black);
		
		pane.add(msgLabel, BorderLayout.CENTER);
		pane.add(okButton, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	private class MyEvent implements ActionListener{ //��ư Ŭ���̺�Ʈ

		private JFrame frame;
		
		public MyEvent(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton src = (JButton) e.getSource();
			if(src.equals(okButton)) { //Ȯ�� ��ư Ŭ����
				if(!programExit) { //���α׷� ���ᰡ �ƴ϶��
					frame.dispose(); //������ �ݱ�
					if(disposeCaller) { //ȣ�� ������ ������
						if(callerFrame != null) { 
							callerFrame.dispose(); //ȣ�� ������ �ݱ�
						}			
					}
				} else { //���α׷� ���� ����   true��
					System.exit(0);  //���α׷� ����
				}
			}
		}	
	}
	
	private class JFrameWindowClosingEventHandler extends WindowAdapter { //������ ������
		public void windowClosing(WindowEvent e) {
			if(e.getWindow() instanceof CheckGUI) {
				CheckGUI eFrame = (CheckGUI) e.getWindow();
				if(eFrame.equals(lastFrame)) 
					lastFrame.dispose(); //������ ����
					if(programExit) { //���α׷� ���� ���� true��
						System.exit(0); //���α׷� ����
					} else if(disposeCaller) { //ȣ�� ������ ������
						callerFrame.dispose(); //ȣ�� ������ �ݱ�
					}	
			}	
		}
	}

	
}
