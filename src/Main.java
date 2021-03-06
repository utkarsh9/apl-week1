import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class Main {
	public static final String EMPTY_STRING = "";
	public static final String CHARACTER_S = "s";

	public static void main(String[] args) {
		List<String> bookWordsList = new ArrayList<String>();
		List<String> stopWordsList = new ArrayList<String>();
		Map<String, Integer> wordFrequencyMap = new HashMap<String, Integer>();
		try {
			bookWordsList = readTextFile(args[0]);
			stopWordsList = readTextFile("stop-words.txt");
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unknown error encountered");
			e.printStackTrace();
		}
		wordFrequencyMap = createWordFrequencyMap(bookWordsList, stopWordsList);
		Map<String, Integer> mapSortedByCount = getMapSortedByWordCount(wordFrequencyMap);
		displaySortedMap(mapSortedByCount);
	}

	private static Map<String, Integer> createWordFrequencyMap(List<String> bookWordsList, List<String> stopWordsList) {
		Map<String, Integer> termFreqMap=null;
		if (bookWordsList!=null && stopWordsList!=null) {
			termFreqMap = new HashMap<String, Integer>();
			for (int k = 0; k < bookWordsList.size(); k++) {
				if (!CHARACTER_S.equals(bookWordsList.get(k))) {
					int count = 1;
					if (stopWordsList.contains(bookWordsList.get(k))) {
						continue;
					} else {
						if (termFreqMap.containsKey(bookWordsList.get(k))) {
							count = termFreqMap.get(bookWordsList.get(k));
							count++;
						}
						termFreqMap.put(bookWordsList.get(k), count);
					}
				}
			} 
		}else{
			System.out.println("Bad file read");
			System.exit(0);
		}
		return termFreqMap;
	}

	private static void displaySortedMap(Map<String, Integer> mapToDisplay) {
		Set<Entry<String, Integer>> treeMapSet = mapToDisplay.entrySet();
		Iterator<Entry<String, Integer>> it = treeMapSet.iterator();
		int entryCount = 0;
		while (it.hasNext() && entryCount < 25) {
			entryCount++;
			Map.Entry mapEntry = (Map.Entry) it.next();
			System.out.println(mapEntry.getKey() + " - " + mapEntry.getValue());
		}
	}

	private static Map<String, Integer> getMapSortedByWordCount(Map<String, Integer> unsortedMap) {
		Map<String, Integer> result=unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
							Map.Entry::getKey, Map.Entry::getValue, (value1, value2) -> value1, LinkedHashMap::new));
		return result;
	}

	private static List<String> readTextFile(String fileName) throws Exception {
		List<String> wordList = new ArrayList<String>();
		String st;
		BufferedReader br=null;
		if(!EMPTY_STRING.equals(fileName)){
			File file = new File(fileName);
			br = new BufferedReader(new FileReader(file));
			if (fileName.contains("stop")) {
				while ((st = br.readLine()) != null) {
					String[] tempArr = st.split(",");
					for (int i = 0; i < tempArr.length; i++) {
						wordList.add(tempArr[i]);
					}
				}
			} else {
				while ((st = br.readLine()) != null) {
					String[] tempStr = st.split("[\\s,;:?._!--]+");
					for (int j = 0; j < tempStr.length; j++) {
						if (!EMPTY_STRING.equals(tempStr[j]))
							wordList.add(tempStr[j].toLowerCase());
					}
				}
			} 
		}else{
			System.out.println("Empty filename");
		}
		br.close();
		return wordList;
	}
}