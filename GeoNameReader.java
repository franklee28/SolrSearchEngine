import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.Map.Entry;

public class GeoNameReader {
	
	Map<String, Location> geoInfo;
	Comparator<Entry<String, Integer>> countOrder;

	public static void main(String[] args) {
		GeoNameReader gnr = new GeoNameReader();
		gnr.specificOutput("Kainamanu");
		//gnr.output();
	}
	
	public GeoNameReader() {
		geoInfo = new HashMap<String, Location>();
		String filename = "US.txt";
		int count = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
			String str;
			String name;
			String latitude;
			String longitude;
			while ((str = reader.readLine()) != null) {
				count++;
				String[] words = str.split("\\t");
				name = words[1];
				latitude = words[4];
				longitude = words[5];
				/*if (count == 1791455) {
					System.out.println(count + ". " + name + ": " + latitude + "," + longitude);
				}*/
				Location location = new Location(Double.parseDouble(latitude), Double.parseDouble(longitude));
				geoInfo.put(name, location);
			}
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		countOrder = new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
				int count1 = e1.getValue();
				int count2 = e2.getValue();
				if (count1 < count2) {
					return 1;
				}
				else if (count1 > count2) {
					return -1;
				}
				else {
					return 0;
				}
			}
		};
	}
	
	public void specificOutput(String name) {
		try {
			System.out.println("Name:" + name);
			System.out.println("Location:" + geoInfo.get(name).getLatitude() + "," + geoInfo.get(name).getLongitude());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void output() {
		int count = 0;
		try {
			for (Entry<String, Location> e : geoInfo.entrySet()) {
				count++; 
				System.out.println(count + ". " + e.getKey() + ": " + e.getValue().getLatitude() + "," + e.getValue().getLongitude());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void wordCountsOutput(HashMap<String, Integer> map) {
		int count = 0;
		try {
			for (Entry<String, Integer> e : map.entrySet()) {
				count++; 
				System.out.println(count + ". " + e.getKey() + ": " + e.getValue());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void wordCountSpecOutput(String name, HashMap<String, Integer> map) {
		try {
			System.out.println(name + ": " + map.get(name));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Location getGeoName(String content) {
		Location location = null;
		HashMap<String, Integer> uniWordCounts = new HashMap<String, Integer>();
		HashMap<String, Integer> biWordCounts = new HashMap<String, Integer>();
		HashMap<String, Integer> triWordCounts = new HashMap<String, Integer>();

		Pattern p = Pattern.compile("\\w+('|-)?\\w*",Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		
		int count = 0;
		String biOne = null;
		String biTwo = null;
		String biGram = null;
		
		String triOne = null;
		String triTwo = null;
		String triThree = null;
		String triGram = null;
		
		while (m.find()) {
			String str = m.group();
			//System.out.println(str);
			
			if (isStopWord(str) == true) {
				continue;
			}
			
			//Uni-Gram
			if (uniWordCounts.containsKey(str)) {
				int num = uniWordCounts.get(str);
				num++;
				uniWordCounts.put(str, num);
			}
			else {
				uniWordCounts.put(str, 1);
			}
			
			//Bi-Gram
			if (count == 0) {
				biOne = str;
			}
			else {
				biTwo = str;
				biGram = biOne + " " + biTwo;
				if (biWordCounts.containsKey(biGram)) {
					int num = biWordCounts.get(biGram);
					num++;
					biWordCounts.put(biGram, num);
				}
				else {
					biWordCounts.put(biGram, 1);
				}
				biOne = biTwo;
			}
			
			//Tri-Gram
			if (count == 0) {
				triOne = str;
			}
			else if (count == 1) {
				triTwo = str;
			}
			else {
				triThree = str;
				triGram = triOne + " " + triTwo + " " + triThree;
				//System.out.println(triGram);
				if (triWordCounts.containsKey(triGram)) {
					int num = triWordCounts.get(triGram);
					num++;
					triWordCounts.put(triGram, num);
				}
				else {
					triWordCounts.put(triGram, 1);
				}
				triOne = triTwo;
				triTwo = triThree;
			}
			count++;
		}
		
		/*wordCountsOutput(wordCounts);
		wordCountSpecOutput("saucer", wordCounts);
		wordCountSpecOutput("los", wordCounts);
		wordCountSpecOutput("angeles", wordCounts);*/
		
		PriorityQueue<Entry<String, Integer>> uniWordFrequency = new PriorityQueue<Entry<String, Integer>>(10, countOrder);
		PriorityQueue<Entry<String, Integer>> biWordFrequency = new PriorityQueue<Entry<String, Integer>>(10, countOrder);
		PriorityQueue<Entry<String, Integer>> triWordFrequency = new PriorityQueue<Entry<String, Integer>>(10, countOrder);
		
		try {
			for (Entry<String, Integer> e : triWordCounts.entrySet()) {
				triWordFrequency.add(e);
			}
			while (triWordFrequency.peek() != null) {
				Entry<String, Integer> e = triWordFrequency.poll();
				if (geoInfo.containsKey(e.getKey())) {
					location = geoInfo.get(e.getKey());
					//System.out.println("Result: " + e.getKey() + "," + e.getValue() + ": " + location.getLatitude() + "," + location.getLongitude());
					return location;
				}
			}
			for (Entry<String, Integer> e : biWordCounts.entrySet()) {
				biWordFrequency.add(e);
			}
			while (biWordFrequency.peek() != null) {
				Entry<String, Integer> e = biWordFrequency.poll();
				if (geoInfo.containsKey(e.getKey())) {
					location = geoInfo.get(e.getKey());
					//System.out.println("Result: " + e.getKey() + "," + e.getValue() + ": " + location.getLatitude() + "," + location.getLongitude());
					return location;
				}
			}
			for (Entry<String, Integer> e : uniWordCounts.entrySet()) {
				uniWordFrequency.add(e);
			}
			while (uniWordFrequency.peek() != null) {
				Entry<String, Integer> e = uniWordFrequency.poll();
				if (geoInfo.containsKey(e.getKey())) {
					location = geoInfo.get(e.getKey());
					//System.out.println("Result: " + e.getKey() + "," + e.getValue() + ": " + location.getLatitude() + "," + location.getLongitude());
					return location;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return location;
	}
	
	private boolean isStopWord(String word) {
		String reg = "_+";
		
		if (word.matches(reg)) {
			return true;
		}
		else {
			return false;
		}
	}
}