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

	public void classificate(int percentage, int k) {
		splitObjects(percentage);
		Map<Object, List<Double>> lengthMapFirstClass = new HashMap<>();
		Map<Object, List<Double>> lengthMapSecondClass = new HashMap<>();
		System.out.println(database.getNoObjects());
		int correct = 0;

		correct = populateLengthMap(lengthMapFirstClass, testListFirstClass, trainingListFirstClass,
				trainingListSecondClass, correct, k);
		correct = populateLengthMap(lengthMapSecondClass, testListSecondClass, trainingListFirstClass,
				trainingListSecondClass, correct, k);

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

	private int populateLengthMap(Map<Object, List<Double>> lengthMap, List<Object> testList,
			List<Object> trainingListFirst, List<Object> trainingListSecond, int correct, int k) {

//		Map<Integer, Double> featureDistanceMap = new HashMap<>();
		List<Double> distancesFirst;
		List<Double> distancesSecond;

//		int correct= 0;

		double first;
		double second;

		List<Double> kNNFirst = new ArrayList<>();
		List<Double> kNNSecond = new ArrayList<>();

		for (Object testObject : testList) {
			distancesFirst = new ArrayList<>();
			distancesSecond = new ArrayList<>();

			first = 0;
			second = 0;
//			lengthMap.put(testObject, distances);

			for (int i = 0; i < database.getNoFeatures(); i++) {
				distancesFirst.add(0d);
				distancesSecond.add(0d);

				for (Object trainingObject : trainingListFirst) {
					distancesFirst.add((double) (trainingObject.getFetures().get(i) - testObject.getFetures().get(i)));
				}
				for (Object trainingObject : trainingListSecond) {
					distancesSecond.add((double) (trainingObject.getFetures().get(i) - testObject.getFetures().get(i)));

				}

				first = Math.sqrt(distancesFirst.stream().mapToDouble(x -> x.doubleValue()).sum());
				second = Math.sqrt(distancesSecond.stream().mapToDouble(x -> x.doubleValue()).sum());

				kNNFirst = manageDistList(kNNFirst, first, k);
				kNNSecond = manageDistList(kNNSecond, second, k);

			}
			if (first < second && testObject.getClassName().equals("Acer")) {
				correct++;
			} else if (first > second && testObject.getClassName().equals("Quercus")) {
				correct++;
			}
			System.out.println(correct);
		}
		System.out.println("=============================================");
		System.out.println(testList.size() + " test lsit");
		return correct;
	}

	private List<Double> manageDistList(List<Double> list, double element, int k) {

		if (list.size() == k) {
			if (Collections.min(list) > element) {
				list.remove(list.indexOf(Collections.min(list)));
				list.add(element);
			}
		} else {
			list.add(element);
		}

		return list;
	}

	public List<Object> getFirstClassObject() {
		return firstClassObjects;
	}

	public List<Object> getSecondClassObject() {
		return secondClassObjects;
	}
}
