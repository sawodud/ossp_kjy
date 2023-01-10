package RmiConnection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.List;

import API_Weather.Weather;
import Server.Server;

public class RemoteMethod extends UnicastRemoteObject implements RemoteMethodInf{

	Server server; //���� ��ü
	
	public RemoteMethod(Server server) throws RemoteException { //���� ��ü �ּ� ����
		this.server = server; 
	}
	
	@Override
	public List<LinkedHashMap<String, String>> getWeatherData(String ip, String cityName, String townName) { //��� ���� ���
		server.printLog(ip+" >> ��� ���� ��û");
		return Weather.getWeather(cityName, townName);
	}
	
	@Override
	public boolean checkConnect(String ip) { //RMI ���� �ߵǳ� Ȯ��
		server.printLog(ip+" >> ����Ȯ�� ��û");
		return true;
	}

	@Override
	public LinkedHashMap<String, List<String>> getAreaInfo(String ip) throws RemoteException { //��ü ������� ���
		server.printLog(ip+" >> �������� ��û");
		return Server.database.getAreaInfo();
	}
	
}
