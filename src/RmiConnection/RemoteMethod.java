package RmiConnection;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.List;

import API_Weather.Weather;
import Server.Server;

public class RemoteMethod extends UnicastRemoteObject implements RemoteMethodInf{

	Server server; //서버 객체
	
	public RemoteMethod(Server server) throws RemoteException { //서버 객체 주소 설정
		this.server = server; 
	}
	
	@Override
	public List<LinkedHashMap<String, String>> getWeatherData(String ip, String cityName, String townName) { //기상 정보 얻기
		server.printLog(ip+" >> 기상 정보 요청");
		return Weather.getWeather(cityName, townName);
	}
	
	@Override
	public boolean checkConnect(String ip) { //RMI 동작 잘되나 확인
		server.printLog(ip+" >> 접속확인 요청");
		return true;
	}

	@Override
	public LinkedHashMap<String, List<String>> getAreaInfo(String ip) throws RemoteException { //전체 지역목록 얻기
		server.printLog(ip+" >> 지역정보 요청");
		return Server.database.getAreaInfo();
	}
	
}
