package RmiConnection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;

/*
 - 나중을 위해 - 
 RMI 인터페이스의 위치는 서버의 RMI인터페이스 위치와 클라이언트의 RMI위치가 동일해야함
 패키지를 포함한 모든 경로가 동일해야한다는 뜻임 
 */

public interface RemoteMethodInf extends Remote{
	
	public List<LinkedHashMap<String, String>> getWeatherData(String ip, String cityName, String townName) throws RemoteException;
	public boolean checkConnect(String ip) throws RemoteException;
	public LinkedHashMap<String, List<String>> getAreaInfo(String ip) throws RemoteException;
	
}
