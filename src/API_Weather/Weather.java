package API_Weather;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Server.Server;

/*
 ���� ���� ��ȸ����API ���
 https://www.data.go.kr/dataset/15000099/openapi.do
 ��ó
*/

public class Weather {
	//API Ű ����
	public static List<LinkedHashMap<String, String>> getWeather(String cityName, String townName) {
	
		String key = "nLQ8uGmYQaDFqw5g8Bgck8kLFuxGP%2FnzgzMPT9x76gbIF5GJEuwrkWFtrHNmF9wkXPuizJwzkOECL1l3sELYGg%3D%3D"; // ��ûŰ
		
		try {
			String[] pos = Server.database.getPos(cityName, townName);
			
			String posX = null;
			String posY = null;
			if(pos == null) return null;
			else {
				posX = pos[0];
				posY = pos[1];
			}
			//�����ð� �������� ���� ����
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //������ ��¥ ����
			SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm"); //������ �ð� ����
			String day = dateFormat.format(System.currentTimeMillis()); //������
			String time = timeFormat.format(System.currentTimeMillis()); //���� �ð�
			
			//day = "20191213";
			//time = "0710"; //�׽�Ʈ�� ���� �ð� ����
			
			double tempTime = Double.valueOf(time); //���� �ֱٿ� ��ǥ�� ������ ������ ����
			double c = tempTime / 300; //3�ð����� ��ǥ�ǹǷ� 300���� ����
			int d = 0; //������
			c -= 2; //�� -2
			if(c < 0) { //���� 06�� ������ ��ȸ�Ѵٸ�
				int tmp = Integer.valueOf(day);
				tmp -= 1; //��ǥ���ڸ� �����ڷ�
				day = String.valueOf(tmp);
				d = (int)c; //�Ҽ��� ����
				d = 2400 + (d * 300) - 100;
			} else {
				d = (int)c; //�Ҽ��� ����
				d = (d * 300) + 200; //0200 ���� 3�ð����� �߷ɵǹǷ� �������� 200 ���ؼ� �ð� ����
			}
			//if(d >= 2000) d = 1700; //2000(20��)�� �Ѿ ��ǥ�� ���� ������ �������� ������ ��� ������ �ȳ���
			time = (d < 1000 ? "0": "") + String.valueOf(d); //���ڿ� �������� ��ȯ
			
			d += 400; //������ ��� �������� 4�ð� �� ���������� �����ð���
			
			String baseDate = day; //������ ������ ��ǥ ��¥ ����
			if(d > 2400) { //��ȸ �ð��� ������ �Ѿ��
				int tmp = Integer.valueOf(day);
				tmp += 1; //��ȸ���ڸ� ��������
				day = String.valueOf(tmp);
				d = d - 2400;
			}
			String time_proximate = (d < 1000 ? "0": "") + String.valueOf(d); //���ڿ� �������� ��ȯ
			
			String baseTime = time;	//������ ������ ��ǥ �ð�
		
			//��û�� UTF-8�� ���ڵ��ؾ��� 
	        StringBuilder requestUrl = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst");
	        requestUrl.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+key);
	        requestUrl.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(posX, "UTF-8")); //�浵 ��ǥ
	        requestUrl.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(posY, "UTF-8")); //���� ��ǥ
	        requestUrl.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); // ��ȸ�� ��¥
	        requestUrl.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); // ��ȸ�� �ð� [AM 02�ú��� 3�ð� ����] 
	        requestUrl.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("999", "UTF-8"));	// ���� �޼��� Ÿ��
	        requestUrl.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));	// ���� �޼����� Json ���·�

	        
	        //String requestUrl = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?"
	        //		+ "ServiceKey="+key+"nx="+posX+"ny="+posY+"base_date="+baseDate+"base_time="+baseTime+"_type="+type; //�׽�Ʈ�� UTF-8 ���ڵ� ���� ����
	        
	        
	        /*
	        	����API Ȩ�������� ���׿�����ȸ����API���� �⺻������ �����ϴ� JAVA �����ڵ� �ο�
	         */
	        System.out.println("��û��: \n"+requestUrl);
	        URL url = new URL(requestUrl.toString()); //��û Url ���ڿ��� URL����
	        //System.out.println(url); //��û�� �޼��� Ȯ�ο�
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //���� ���� �����͸� �ְ�ޱ� ���� ���
	        conn.setRequestMethod("GET"); //get��� ���
	        conn.setRequestProperty("Content-type", "application/json"); //���� �����͸� Json ������ Ÿ������ ��û
	        //System.out.println("Response code: " + conn.getResponseCode()); //���� �ڵ� Ȯ�ο�
	        
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { //�����ڵ尡 200~300������ �����̶��
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); //���������� �� �б�
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream())); //������ �б�
	        }
	        StringBuilder sb = new StringBuilder(); //��� ������ ��
	        String line; 
	        while ((line = rd.readLine()) != null) { //������ ������
	            sb.append(line);
	        }
	        rd.close(); 
	        conn.disconnect(); //�� ���� ����
	        String result= sb.toString(); //����� �ϳ��� ���ڿ��� ��ȯ
	        System.out.println("����Ȯ��:"); //���� �� Ȯ��
	        System.out.println(result); //���� �� Ȯ��
	        
			JSONParser parser = new JSONParser(); //Json ���� �����͸� �Ľ��ϱ����� �ν��Ͻ�  
			JSONObject obj = (JSONObject) parser.parse(result); //���ڿ� ������ ���� ���� ��üȭ
			// response Ű�� ������ �����͸� �Ľ� 
			JSONObject response = (JSONObject) obj.get("response"); //���� �� �κ�(reponse�κ�) �ϴ��� ���׿�����ȸ���� Ȱ�밡�̵忡�� �����ϴ� ���� ������ ���� 
			JSONObject body = (JSONObject) response.get("body"); //���� �� �κп��� body �κ� ã��
			JSONObject items = (JSONObject) body.get("items"); //body�κп��� items(�������� ��� �����͵�) ã��

			JSONArray itemList = (JSONArray) items.get("item"); //items���� �迭�� ������ ������͵� ��������
			List<String> parseCatecory = new ArrayList<String>(); //��� ������ �ϴ� ����, �� ����Ʈ�� �ִ� ī�װ��� �Ľ��Ұ���

			parseCatecory.add("POP"); //����Ȯ��, �����Ͻ� ���� Ȯ��
			parseCatecory.add("REH"); //����
			parseCatecory.add("R06"); //������
			parseCatecory.add("S06"); //������
			parseCatecory.add("T3H"); //���� ���
			parseCatecory.add("TMN"); //�������
			parseCatecory.add("TMX"); //�ְ���
			parseCatecory.add("WSD"); //ǳ��
			parseCatecory.add("PTY"); //��������
			parseCatecory.add("SKY"); //�ϴû���
			
			JSONObject weather; // parse_item�� �迭�����̱� ������ �ϳ��� �����͸� �ϳ��� �����ö� ���
			// ī�װ��� ���� �޾ƿ���
			String getDay= day; //������ ���� ����
			String getTime= time_proximate; //������ ���� �ð�
			
			LinkedHashMap<String, String> today_tm_Map = null; //������ ����, �ְ���
			LinkedHashMap<String, String> now_WeatherMap = new LinkedHashMap<String, String>(12); //���� ���
			LinkedHashMap<String, String> today_WeatherMap = new LinkedHashMap<String, String>(12); //������ �������� ���
			LinkedHashMap<String, String> tomor_WeatherMap = new LinkedHashMap<String, String>(12); //�������� ���
			LinkedHashMap<String, String> tomor_tm_Map = new LinkedHashMap<String, String>(12); //������ ����, �ְ���
			
			now_WeatherMap.put("day", getDay);
			today_WeatherMap.put("day", getDay);
			for(int i = 0 ; i < itemList.size(); i++) { //������ ���� ��������
				weather = (JSONObject) itemList.get(i);
				Object fcstValue = weather.get("fcstValue");
				Object fcstDate = weather.get("fcstDate");
				Object fcstTime = weather.get("fcstTime");
				String category = (String) weather.get("category");
				
				if(fcstDate.toString().equals(getDay) && parseCatecory.contains(category)) {
					if(fcstTime.toString().equals(getTime)) { //���� ����� ���ϱ� ���� getTime�� ��ġ�Ѵٸ�
						now_WeatherMap.put(category.toString(), fcstValue.toString()); //���� ���ʿ� �߰�
					} else {
						if(category != "TMX" && category != "TMN") {
							today_WeatherMap.put(fcstTime+category.toString(), fcstValue.toString());
						}			
					}
					//System.out.print("\tcategory : "+ category); //�� Ȯ�ο�
					//System.out.println(", fcst_Value : "+ fcstValue); //�� Ȯ�ο�
					//System.out.print(", fcstDate : "+ fcstDate); //�� Ȯ�ο�
					//System.out.println(", fcstTime : "+ fcstTime); //�� Ȯ�ο�
				}
			}
			
			getDay = String.valueOf(Integer.valueOf(getDay)+1); //��¥�� ���Ϸ�
			tomor_WeatherMap.put("day", getDay);
			for(int i = 0 ; i < itemList.size(); i++) { //������ ���� ��������
				weather = (JSONObject) itemList.get(i);
				Object fcstValue = weather.get("fcstValue");
				Object fcstDate = weather.get("fcstDate");
				Object fcstTime = weather.get("fcstTime");
				String category = (String) weather.get("category");
				
				if(fcstDate.toString().equals(getDay) && parseCatecory.contains(category)) {
					if(category.equalsIgnoreCase("TMX")) { 
						tomor_tm_Map.put("TMX", String.valueOf(fcstValue));
					} else if(category.equalsIgnoreCase("TMN")){
						tomor_tm_Map.put("TMN", String.valueOf(fcstValue));
					} else {
						tomor_WeatherMap.put(fcstTime+category.toString(), fcstValue.toString());
					}
					//System.out.print(", fcstDate : "+ fcstDate); //�� Ȯ�ο�
					//System.out.println(", fcstTime : "+ fcstTime); //�� Ȯ�ο�
				}
			}
			
			//Server.database.insertDayTemperature(getDay, baseTime, tmn, tmx);
			
			List<LinkedHashMap<String, String>> returnList = new ArrayList<LinkedHashMap<String, String>>();
			returnList.add(now_WeatherMap);
			returnList.add(today_WeatherMap);
			returnList.add(tomor_WeatherMap);
			returnList.add(tomor_tm_Map);
			
			return returnList;

	        /* ī�װ� ���
	          POP	����Ȯ��	 %                         - �ʿ�
	          PTY	��������	�ڵ尪
	          R06	6�ð� ������	���� (1 mm)			   - �ʿ�
	          REH	����	 %                             - �ʿ�
	          S06	6�ð� ������	����(1 cm)              - �ʿ�
	          SKY	�ϴû���	�ڵ尪
	          T3H	3�ð� ���	 ��                                                        - �ʿ�
	          TMN	��ħ �������	 ��                                               - �ʿ�
	          TMX	�� �ְ���	 ��                                                        - �ʿ�
	          UUU	ǳ��(��������)	 m/s                   
	          VVV	ǳ��(���ϼ���)	 m/s                             
	          WSD	ǳ��	 m/s                  		 - �ʿ�
	         */
			
			/*
			 {"response":{"header":{"resultCode":"0000","resultMsg":"OK"},"body":{"items":{"item":[
				{"baseDate":20151021,"baseTime":"0500","category":"T3H","fcstDate":20151021,"fcstTime":"0900","fcstValue":-50,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"UUU","fcstDate":20151021,"fcstTime":"0900","fcstValue":-5,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"VVV","fcstDate":20151021,"fcstTime":"0900","fcstValue":-1,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"POP","fcstDate":20151021,"fcstTime":"0900","fcstValue":-1,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"REH","fcstDate":20151021,"fcstTime":"0900","fcstValue":-1,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"PTY","fcstDate":20151021,"fcstTime":"0900","fcstValue":0,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"R06","fcstDate":20151021,"fcstTime":"0900","fcstValue":0,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"S06","fcstDate":20151021,"fcstTime":"0900","fcstValue":0,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"TMN","fcstDate":20151021,"fcstTime":"0900","fcstValue":0,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"TMX","fcstDate":20151021,"fcstTime":"0900","fcstValue":0,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"SKY","fcstDate":20151021,"fcstTime":"0900","fcstValue":1,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"WAV","fcstDate":20151021,"fcstTime":"0900","fcstValue":1,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"WSD","fcstDate":20151021,"fcstTime":"0900","fcstValue":5,"nx":1,"ny":1},
				{"baseDate":20151021,"baseTime":"0500","category":"VEC","fcstDate":20151021,"fcstTime":"0900","fcstValue":74,"nx":1,"ny":1},
				,"numOfRows":308,"pageNo":1,"totalCount":308}}}
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;

    }
}

