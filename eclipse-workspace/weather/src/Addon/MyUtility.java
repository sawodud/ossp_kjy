package Addon;

import java.util.ArrayList;
import java.util.List;

public class MyUtility {

	public static String lineSpacing(String text, int stand) { //stand �� �������� ���ڿ� ����
		String str = text; //���� �۾��� ���� �ӽ÷� str�� text ����
		if(text.length() > stand) { //���ڿ��� ���̰� 1�� ���ѹ��ڼ��� �ʰ��ߴٸ�
			List<String> tmpStr = new ArrayList<>(); //���� �۾��� ���� ���ڿ� list ����
			int i = 0; //���ڿ� ���� �۾��� ���� ����
			for(; i < text.length() - stand; i += stand) { //���� ��ȣ�� ���� ���ڿ� ������ŭ �ݺ�
				int subStart = i; //���� ��������
				int subEnd = subStart + stand; //���� ������
				tmpStr.add(text.substring(subStart, subEnd)); //������ ���ڿ��� tmpStr�� �߰�
			}
			tmpStr.add(text.substring(i, text.length())); //���� ���ڿ� ����
			str = ""; //str �ʱ�ȭ
			for(i = 0; i < tmpStr.size() - 1; i++) //tmpStr�� ����ִ� ���ڿ��� ������ ���ڿ��� ������ ���� ���� ó��
				str += tmpStr.get(i) + "<br>"; //���� ��ȣ �߰�
			str += tmpStr.get(i); //������ ���ڿ��� �߰��� �ʿ����
		}
		str = str.replaceAll("(\r|\n|\r\n|\n\r)","<br>");//�����ȣ��ȯ
		return str = "<html><body>" + str + "</body></html>"; //�� ��ȯ
	}
	
}
