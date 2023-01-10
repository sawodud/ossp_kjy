package RmiConnection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;

/*
 - ������ ���� - 
 RMI �������̽��� ��ġ�� ������ RMI�������̽� ��ġ�� Ŭ���̾�Ʈ�� RMI��ġ�� �����ؾ���
 ��Ű���� ������ ��� ��ΰ� �����ؾ��Ѵٴ� ���� 
 */

public interface RemoteMethodInf extends Remote{
	
	public List<LinkedHashMap<String, String>> getWeatherData(String ip, String cityName, String townName) throws RemoteException;
	public boolean checkConnect(String ip) throws RemoteException;
	public LinkedHashMap<String, List<String>> getAreaInfo(String ip) throws RemoteException;
	
}
