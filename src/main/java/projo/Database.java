package projo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Database {

	private ArrayList<Object> objects = new ArrayList<Object>();
	private Map<String, Integer> classCounter = new HashMap<String, Integer>();
	private ArrayList<String> classNamesList = new ArrayList<String>();
	private ArrayList<Integer> featuresIDs = new ArrayList<Integer>();

	private List<Object> firstClassObjects = new ArrayList<Object>();
	private List<Object> secondClassObjects = new ArrayList<Object>();

	private Float[][] firstClassArray;
	private Float[][] secondClassArray;

	private int noClass = 0;
	private int noObjects = 0;
	private int noFeatures = 0;

	public Database(int noClass, int noObjects, int noFeatures) {
		this.noClass = noClass;
		this.noObjects = noObjects;
		this.noFeatures = noFeatures;
	}

	void getArrays() {

		int index = 0;
		String firstClass = null;
		String secondClass = null;
		for (Map.Entry<String, Integer> entry : classCounter.entrySet()) {
			if (index == 0) {
				firstClassArray = new Float[featuresIDs.size()][entry.getValue()];
				index++;
				firstClass = entry.getKey();
			} else {
				secondClassArray = new Float[featuresIDs.size()][entry.getValue()];
				secondClass = entry.getKey();
			}
		}

		int firstIndex = 0;
		int secondIndex = 0;
		// boolean secondArr=false;

		for (int i = 0; i < featuresIDs.size(); i++) {
			firstIndex = 0;
			secondIndex = 0;
			// if(secondArr) secondIndex++;
			for (Object ob : objects) {
				if (ob.getClassName().equals(firstClass)) {
					firstClassArray[i][firstIndex++] = ob.getFetures().get(i);
				} else if (ob.getClassName().equals(secondClass)) {
					secondClassArray[i][secondIndex++] = ob.getFetures().get(i);
					// secondArr=true;
				}
			}
		}
	}

	boolean addObject(Object object) {

		int counter = 0;
		String className = object.getClassName();

		if (noFeatures == 0) {
			noFeatures = object.getFeaturesNumber();
		} else if (noFeatures != object.getFeaturesNumber()) {
			return false;
		} else if (object.getClassName() == null) {
			return false;
		}
		// System.out.println(object.getClassName()+" name");
		// for(Float f:object.getFetures()) {
		// System.out.print(f+" ");
		// }
		objects.add(object);
		++noObjects;

		if (classCounter.get(className) == null) {
			counter = 1;
			classCounter.put(className, counter);
			classNamesList.add(className);
		} else {
			classCounter.put(className, classCounter.get(className) + 1);
		}
		// System.out.println(classCounter.get("Acer")+" acer");
		// System.out.println(classCounter.get("Quercus")+" quer");
		// if (classCounter.get(object.getClassName()+1 == 0) {
		// classNamesList.add(object.getClassName());
		// }

		return true;
	}

	boolean load(String fileName) {

		clear();

		File file = new File(fileName);
		String[] pos;
		List<String> nextLine;
		String line;
		int classFeaturesNo;

		try {
			Scanner sc = new Scanner(file);
			// Open file and read the first line to get number of features.
			if (sc.hasNextLine()) {
				line = sc.nextLine();
				pos = line.split(", ");

				classFeaturesNo = Integer.parseInt(pos[0]);
			} else {
				System.out.println("File empty");
				sc.close();
				return false;
			}

			for (int i = 1; i < pos.length; i++) {
				featuresIDs.add(Integer.parseInt(pos[i]));
			}

			if (!checkFeaturesNumber(featuresIDs.size(), classFeaturesNo)) {
				sc.close();
				return false;
			}

			ArrayList<Float> featuresValues = new ArrayList<Float>();
			String className = null;

			while (sc.hasNextLine()) {
				String currentLine = sc.nextLine();
				nextLine = Arrays.asList(currentLine.split(" "));

				className = nextLine.get(0);

				featuresValues = new ArrayList<Float>();
				nextLine = Arrays.asList(currentLine.split(","));

				for (int i = 1; i < nextLine.size(); i++) {
					featuresValues.add(Float.parseFloat(nextLine.get(i)));
				}

				// for (int i = 0; i < featuresValues.size(); i++) {
				// System.out.println(featuresValues.get(i));
				// }

				if (checkFeaturesNumber(featuresValues.size(), classFeaturesNo)) {
					if (!addObject(new Object(className, featuresValues))) {
						System.out.println("Error log");
					}
				}

			}

			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		getArrays();
		return true;
	}

	void clear() {
		objects.clear();
		classNamesList.clear();
		classCounter.clear();
		featuresIDs.clear();
		noClass = 0;
		noObjects = 0;
		noFeatures = 0;
	}

	boolean checkFeaturesNumber(int featuresIDs, int classFeaturesNo) {
		if (featuresIDs != classFeaturesNo) {
			System.out.println("Wrong number of features " + featuresIDs + " " + classFeaturesNo);
			return false;
		}
		return true;
	}
	
	public void splitObjectList() {
		
		List<String> classNames = new ArrayList<>();
		
		firstClassObjects.clear();
		secondClassObjects.clear();
		
		for(Map.Entry<String, Integer> entry : classCounter.entrySet()) {
			classNames.add(entry.getKey());
		}
		
		for(Object o:objects) {
			if(o.getClassName().equals(classNames.get(0))) {
				firstClassObjects.add(o);
			}else if(o.getClassName().equals(classNames.get(1))) {
				secondClassObjects.add(o);
			}
		}
	}

	public ArrayList<Object> getObjects() {
		return objects;
	}

	public int getNoClass() {
		return classNamesList.size();
	}

	public int getNoObjects() {
		return noObjects;
	}

	public int getNoFeatures() {
		return noFeatures;
	}

	public Map<String, Integer> getClassCounter() {
		return classCounter;
	}

	public ArrayList<String> getClassNames() {
		return classNamesList;
	}

	public Float[][] getFirstClassArray() {
		return firstClassArray;
	}

	public Float[][] getSecondClassArray() {
		return secondClassArray;
	}

	public List<Object> getFirstClassObjectList() {
		splitObjectList();
		return firstClassObjects;
	}

	public List<Object> getSecondClassObjectList() {
		splitObjectList();
		return secondClassObjects;
	}

}
