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

//	private Map<Integer, Object>

	private Database database = new Database(0, 0, 0);

	private List<Object> firstClassObjects;
	private List<Object> secondClassObjects;

	public void classificate(int percentage) {
		splitObjects(percentage);
		Map<Object, Map<Integer, Double>> lengthMapFirstClass = new HashMap<>();
		Map<Object, Map<Integer, Double>> lengthMapSecondClass = new HashMap<>();

		lengthMapFirstClass = populateLengthMap(lengthMapFirstClass, testListFirstClass, trainingListFirstClass);
		lengthMapSecondClass = populateLengthMap(lengthMapSecondClass, testListSecondClass, trainingListSecondClass);

//		for (int i = 0; i < database.getNoFeatures(); i++) {
//			double firstClassDistance = 0;
//			double secondClassDistance = 0;
//
//			firstClassDistance = lengthMapFirstClass.get(i);
//			secondClassDistance = lengthMapSecondClass.get(i);
//
//		}
//		double minFirstClass = lengthMapFirstClass

		for (Object o : testListFirstClass) {

		}

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

	private Map<Object, List<Double>>> populateLengthMap(Map<Object, List<Double>> lengthMap, List<Object> testList,
			List<Object> trainingList) {
		
//		Map<Integer, Double> featureDistanceMap = new HashMap<>();
		List<Double> distances;

		for (Object testObject : testList) {
			distances=new ArrayList<>();
			lengthMap.put(testObject, distances);
			
			for (int i = 0; i < database.getNoFeatures(); i++) {
				distances.add(0d);
				
				for (Object trainingObject : trainingList) {
					distances.add()
					featureDistanceMap.put(i, featureDistanceMap.get(i)
							+ Math.pow(testObject.getFetures().get(i) - trainingObject.getFetures().get(i), 2));
				}
				featureDistanceMap.put(i, Math.sqrt(lengthMap.get(testObject).get(i)));
			}

			lengthMap.put(testObject,featureDistanceMap );
			System.out.println(testObject.getClassName() + " i, length: " + lengthMap.get(testObject).get(1));
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
