package projo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classifier {

	private List<Object> trainingListFirstClass = new ArrayList<>();
	private List<Object> trainingListSecondClass = new ArrayList<>();

	private List<Object> testListFirstClass = new ArrayList<>();
	private List<Object> testListSecondClass = new ArrayList<>();

	private Database database = new Database(0, 0, 0);

	private List<Object> firstClassObjects;
	private List<Object> secondClassObjects;

	public void classificate(int percentage) {
		splitObjects(percentage);
		Map<Integer, Double> lengthMapFirstClass = new HashMap<>();
		Map<Integer, Double> lengthMapSecondClass = new HashMap<>();

//		for (int i = 0; i < database.getNoFeatures(); i++) {
//			lengthMapFirstClass.put(i, 0d);
//			lengthMapSecondClass.put(i, 0d);
//			for (Object testObject : testListFirstClass) {
//
//				for (Object trainingObject : trainingListFirstClass) {
//					lengthMapFirstClass.put(i, lengthMapFirstClass.get(i)
//							+ Math.pow(testObject.getFetures().get(i) - trainingObject.getFetures().get(i), 2));
//				}
//			}
//		}

		lengthMapFirstClass = populateLengthMap(lengthMapFirstClass, testListFirstClass, trainingListFirstClass);
		lengthMapSecondClass = populateLengthMap(lengthMapSecondClass, testListSecondClass, trainingListSecondClass);

	}

	public void splitObjects(int percentage) {
		database.load("C:\\dev\\Maple_Oak.txt");

		firstClassObjects = database.getFirstClassObjectList();
		secondClassObjects = database.getSecondClassObjectList();

		firstClassObjects = shuffleList(firstClassObjects);
		secondClassObjects = shuffleList(secondClassObjects);

		System.out.println(
				"fc: " + firstClassObjects.size() + " , sc: " + secondClassObjects.size() + " per: " + percentage);
		populateTrainingList(percentage, firstClassObjects, secondClassObjects);
		System.out.println(trainingListFirstClass.size() + " sizetrainingFC");
		System.out.println(trainingListSecondClass.size() + " sizetrainingSC");
		System.out.println(testListFirstClass.size() + " sizeTestFC");
		System.out.println(testListSecondClass.size() + " sizeTestSC");
	}

	private List<Object> shuffleList(List<Object> listToShuffle) {
		Collections.shuffle(listToShuffle);
		return listToShuffle;
	}

	private void populateTrainingList(int percentage, List<Object> firstClassObjects, List<Object> secondClassObjects) {
		int newSize;

		if (firstClassObjects.size() < secondClassObjects.size()) {
			newSize = (firstClassObjects.size() * percentage / 100);
		} else {
			newSize = (secondClassObjects.size() * percentage / 100);
		}

		int temp = newSize / 2;
		trainingListFirstClass = firstClassObjects.subList(0, newSize / 2);
		trainingListSecondClass = secondClassObjects.subList(0, newSize / 2);

		testListFirstClass = firstClassObjects.subList(newSize / 2, firstClassObjects.size());
		testListSecondClass = secondClassObjects.subList(newSize / 2, secondClassObjects.size());
	}

	private Map<Integer, Double> populateLengthMap(Map<Integer, Double> lengthMap, List<Object> testList,
			List<Object> trainingList) {

		for (int i = 0; i < database.getNoFeatures(); i++) {
			lengthMap.put(i, 0d);
			for (Object testObject : testList) {

				for (Object trainingObject : trainingList) {
					lengthMap.put(i, lengthMap.get(i)
							+ Math.pow(testObject.getFetures().get(i) - trainingObject.getFetures().get(i), 2));
				}
			}
			
			lengthMap.put(i, Math.sqrt(lengthMap.get(i)));
			System.out.println(i+" i, length: "+lengthMap.get(i));
		}

		return lengthMap;
	}

	public List<Object> getFirstClassObject() {
		return firstClassObjects;
	}

	public List<Object> getSecondClassObject() {
		return secondClassObjects;
	}
}
