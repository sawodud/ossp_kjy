package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.mysql.cj.xdevapi.Result;

public class MyDatabase { //데이터베이스 통신용
	
	String url = "jdbc:mysql://127.0.0.1:3306/weatherinfo_db?serverTimezone=Asia/Seoul"; //데이터 베이스 주소
	String rootId = "weatherinfo_manager"; //루트 id
	String rootPw = "1234"; //루트 pw
	Connection con; //데이터베이스 통신용
	Statement stmt; //데이터베이스 명령 실해용ㅇ
	
	public MyDatabase() { //생성 및 초기화
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(java.lang.ClassNotFoundException e) {
			e.printStackTrace();
			return;
		} 
		try {
			con = DriverManager.getConnection(url, rootId, rootPw); //데이터 베이스 로그인
			stmt = con.createStatement();
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	public LinkedHashMap<String, List<String>> getAreaInfo() { //전체 지역목록 얻기
		LinkedHashMap<String, List<String>> areaList = new LinkedHashMap<String, List<String>>(); //hashmap은 도시, list는 이 도시의 동네들
		try {
			ResultSet result = stmt.executeQuery("SELECT distinct city FROM areainfo"); //도시목록 얻기
			if(!result.next()) return null; 
			do { //각각의 도시 이름을 areaList에 키로서 넣음
				String cityName = result.getString("city");
				areaList.put(cityName, new ArrayList<String>()); //아직 동네 리스트는 비어있음
			}while(result.next());
			
			for(String cityName : areaList.keySet()) { //각 도시이름마다 동네를 조회할 거임
				List<String> townList = areaList.get(cityName); //도시이름
				ResultSet rs = stmt.executeQuery("SELECT town FROM areainfo where city = '"+cityName+"'"); //동네목록 얻기 
				while(rs.next()) {
					townList.add(rs.getString("town")); //해당 도시의 동네 리스트에 넣기
				}
			}
			
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		
		return areaList.size() == 0 ? null : areaList; //areaList가 비어있으면 null 반환
	}
	
	public String[] getPos(String cityName, String townName) { //특정 지역명을 이용하여 해당 지역의 지점좌표 얻기
		String pos[] = null; //반환값이 2개가 필요하므로 배열로 선언
		try {
			ResultSet result = stmt.executeQuery("SELECT nx, ny FROM areainfo where city = '"+cityName + "' and town = '"+townName+"'");
			//도시명과 동네명으로 지점좌표 얻기
			if(!result.next()) return null; 
			pos = new String[2]; //x,y 좌표 설정
			pos[0] = result.getString("nx");
			pos[1] = result.getString("ny");
			if(pos[0] == null || pos[1] == null) return null; //둘중 하나라도 값 이상하면 null
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return pos; //좌표 반환
	}
	
}
