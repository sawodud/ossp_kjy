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
 동네 예보 조회서비스API 사용
 https://www.data.go.kr/dataset/15000099/openapi.do
 출처
*/

public class Weather {
	//API 키 세팅
	public static List<LinkedHashMap<String, String>> getWeather(String cityName, String townName) {
	
		String key = "nLQ8uGmYQaDFqw5g8Bgck8kLFuxGP%2FnzgzMPT9x76gbIF5GJEuwrkWFtrHNmF9wkXPuizJwzkOECL1l3sELYGg%3D%3D"; // 요청키
		
		try {
			String[] pos = Server.database.getPos(cityName, townName);
			
			String posX = null;
			String posY = null;
			if(pos == null) return null;
			else {
				posX = pos[0];
				posY = pos[1];
			}
			//서버시간 기준으로 날씨 구함
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); //추출할 날짜 형식
			SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm"); //추출할 시간 형식
			String day = dateFormat.format(System.currentTimeMillis()); //오늘자
			String time = timeFormat.format(System.currentTimeMillis()); //현재 시각
			
			//day = "20191213";
			//time = "0710"; //테스트용 강제 시간 설정
			
			double tempTime = Double.valueOf(time); //가장 최근에 발표된 예보를 가져올 것임
			double c = tempTime / 300; //3시간마다 발표되므로 300으로 나눔
			int d = 0; //최종값
			c -= 2; //몫 -2
			if(c < 0) { //만약 06시 이전에 조회한다면
				int tmp = Integer.valueOf(day);
				tmp -= 1; //발표일자를 어제자로
				day = String.valueOf(tmp);
				d = (int)c; //소수점 버림
				d = 2400 + (d * 300) - 100;
			} else {
				d = (int)c; //소수점 버림
				d = (d * 300) + 200; //0200 부터 3시간마다 발령되므로 마지막에 200 더해서 시간 설정
			}
			//if(d >= 2000) d = 1700; //2000(20시)를 넘어서 발표된 예보 정보를 가져오면 오늘자 기상 정보는 안나옴
			time = (d < 1000 ? "0": "") + String.valueOf(d); //문자열 형식으로 전환
			
			d += 400; //가져온 기상 정보에서 4시간 후 기후정보를 가져올거임
			
			String baseDate = day; //가져올 정보의 발표 날짜 설정
			if(d > 2400) { //조회 시간이 자정을 넘어서면
				int tmp = Integer.valueOf(day);
				tmp += 1; //조회일자를 다음날로
				day = String.valueOf(tmp);
				d = d - 2400;
			}
			String time_proximate = (d < 1000 ? "0": "") + String.valueOf(d); //문자열 형식으로 전환
			
			String baseTime = time;	//가져올 정보의 발표 시간
		
			//요청시 UTF-8로 인코딩해야함 
	        StringBuilder requestUrl = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst");
	        requestUrl.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+key);
	        requestUrl.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(posX, "UTF-8")); //경도 좌표
	        requestUrl.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(posY, "UTF-8")); //위도 좌표
	        requestUrl.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); // 조회할 날짜
	        requestUrl.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); // 조회할 시간 [AM 02시부터 3시간 단위] 
	        requestUrl.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("999", "UTF-8"));	// 응답 메세지 타입
	        requestUrl.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));	// 응답 메세지는 Json 형태로

	        
	        //String requestUrl = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?"
	        //		+ "ServiceKey="+key+"nx="+posX+"ny="+posY+"base_date="+baseDate+"base_time="+baseTime+"_type="+type; //테스트로 UTF-8 인코딩 없이 실행
	        
	        
	        /*
	        	공공API 홈페이지의 동네예보조회서비스API에서 기본적으로 제공하는 JAVA 샘플코드 인용
	         */
	        System.out.println("요청값: \n"+requestUrl);
	        URL url = new URL(requestUrl.toString()); //요청 Url 문자열로 URL생성
	        //System.out.println(url); //요청한 메세지 확인용
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //웹을 통해 데이터를 주고받기 위해 사용
	        conn.setRequestMethod("GET"); //get방식 통신
	        conn.setRequestProperty("Content-type", "application/json"); //응답 데이터를 Json 형식의 타입으로 요청
	        //System.out.println("Response code: " + conn.getResponseCode()); //응답 코드 확인용
	        
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) { //응답코드가 200~300사이의 정상값이라면
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); //정상적으로 값 읽기
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream())); //에러값 읽기
	        }
	        StringBuilder sb = new StringBuilder(); //결과 저장할 곳
	        String line; 
	        while ((line = rd.readLine()) != null) { //데이터 가져옴
	            sb.append(line);
	        }
	        rd.close(); 
	        conn.disconnect(); //웹 연결 끊기
	        String result= sb.toString(); //결과를 하나의 문자열로 변환
	        System.out.println("응답확인:"); //응답 값 확인
	        System.out.println(result); //응답 값 확인
	        
			JSONParser parser = new JSONParser(); //Json 형의 데이터를 파싱하기위한 인스턴스  
			JSONObject obj = (JSONObject) parser.parse(result); //문자열 형태의 응답 값을 객체화
			// response 키를 가지고 데이터를 파싱 
			JSONObject response = (JSONObject) obj.get("response"); //응답 값 부분(reponse부분) 하단의 동네예보조회서비스 활용가이드에서 제공하는 샘플 데이터 참고 
			JSONObject body = (JSONObject) response.get("body"); //응답 값 부분에서 body 부분 찾기
			JSONObject items = (JSONObject) body.get("items"); //body부분에서 items(실질적인 기상 데이터들) 찾기

			JSONArray itemList = (JSONArray) items.get("item"); //items에서 배열을 형태의 기상데이터들 가져오기
			List<String> parseCatecory = new ArrayList<String>(); //기상 데이터 하단 참고, 이 리스트에 있는 카테고리만 파싱할거임

			parseCatecory.add("POP"); //강수확률, 영하일시 눈올 확률
			parseCatecory.add("REH"); //습도
			parseCatecory.add("R06"); //강수량
			parseCatecory.add("S06"); //적설량
			parseCatecory.add("T3H"); //현재 기온
			parseCatecory.add("TMN"); //최저기온
			parseCatecory.add("TMX"); //최고기온
			parseCatecory.add("WSD"); //풍속
			parseCatecory.add("PTY"); //강수형태
			parseCatecory.add("SKY"); //하늘상태
			
			JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사용
			// 카테고리와 값만 받아오기
			String getDay= day; //가져올 값의 일자
			String getTime= time_proximate; //가져올 값의 시간
			
			LinkedHashMap<String, String> today_tm_Map = null; //오늘자 최저, 최고기온
			LinkedHashMap<String, String> now_WeatherMap = new LinkedHashMap<String, String>(12); //현재 기상
			LinkedHashMap<String, String> today_WeatherMap = new LinkedHashMap<String, String>(12); //오늘자 앞으로의 기상
			LinkedHashMap<String, String> tomor_WeatherMap = new LinkedHashMap<String, String>(12); //내일자의 기상
			LinkedHashMap<String, String> tomor_tm_Map = new LinkedHashMap<String, String>(12); //내일자 최저, 최고기온
			
			now_WeatherMap.put("day", getDay);
			today_WeatherMap.put("day", getDay);
			for(int i = 0 ; i < itemList.size(); i++) { //오늘자 내용 가져오기
				weather = (JSONObject) itemList.get(i);
				Object fcstValue = weather.get("fcstValue");
				Object fcstDate = weather.get("fcstDate");
				Object fcstTime = weather.get("fcstTime");
				String category = (String) weather.get("category");
				
				if(fcstDate.toString().equals(getDay) && parseCatecory.contains(category)) {
					if(fcstTime.toString().equals(getTime)) { //현재 기상을 구하기 위한 getTime과 일치한다면
						now_WeatherMap.put(category.toString(), fcstValue.toString()); //현재 기상맵에 추가
					} else {
						if(category != "TMX" && category != "TMN") {
							today_WeatherMap.put(fcstTime+category.toString(), fcstValue.toString());
						}			
					}
					//System.out.print("\tcategory : "+ category); //값 확인용
					//System.out.println(", fcst_Value : "+ fcstValue); //값 확인용
					//System.out.print(", fcstDate : "+ fcstDate); //값 확인용
					//System.out.println(", fcstTime : "+ fcstTime); //값 확인용
				}
			}
			
			getDay = String.valueOf(Integer.valueOf(getDay)+1); //날짜를 내일로
			tomor_WeatherMap.put("day", getDay);
			for(int i = 0 ; i < itemList.size(); i++) { //내일자 내용 가져오기
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
					//System.out.print(", fcstDate : "+ fcstDate); //값 확인용
					//System.out.println(", fcstTime : "+ fcstTime); //값 확인용
				}
			}
			
			//Server.database.insertDayTemperature(getDay, baseTime, tmn, tmx);
			
			List<LinkedHashMap<String, String>> returnList = new ArrayList<LinkedHashMap<String, String>>();
			returnList.add(now_WeatherMap);
			returnList.add(today_WeatherMap);
			returnList.add(tomor_WeatherMap);
			returnList.add(tomor_tm_Map);
			
			return returnList;

	        /* 카테고리 목록
	          POP	강수확률	 %                         - 필요
	          PTY	강수형태	코드값
	          R06	6시간 강수량	범주 (1 mm)			   - 필요
	          REH	습도	 %                             - 필요
	          S06	6시간 신적설	범주(1 cm)              - 필요
	          SKY	하늘상태	코드값
	          T3H	3시간 기온	 ℃                                                        - 필요
	          TMN	아침 최저기온	 ℃                                               - 필요
	          TMX	낮 최고기온	 ℃                                                        - 필요
	          UUU	풍속(동서성분)	 m/s                   
	          VVV	풍속(남북성분)	 m/s                             
	          WSD	풍속	 m/s                  		 - 필요
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

