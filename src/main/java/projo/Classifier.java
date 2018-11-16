package projo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Classifier {

	private final String[] CLASSIFIERS = { "NN", "kNN", "NM", "kNM" };

	private List<Object> trainingListFirstClass = new ArrayList<>();
	private List<Object> trainingListSecondClass = new ArrayList<>();

	private List<Object> testListFirstClass = new ArrayList<>();
	private List<Object> testListSecondClass = new ArrayList<>();

	private Database database = new Database(0, 0, 0);

	private List<Object> firstClassObjects;
	private List<Object> secondClassObjects;

	public void classificate(int percentage, int k, int classifier) {

		// Map<Object, List<Double>> lengthMapFirstClass = new HashMap<>();
		// Map<Object, List<Double>> lengthMapSecondClass = new HashMap<>();
		System.out.println(database.getNoObjects());
		int correct = 0;
		if (classifier == 0 || classifier == 1) {

			correct = populateLengthListNeighbour(testListFirstClass, trainingListFirstClass, trainingListSecondClass,
					correct, k);

			correct = populateLengthListNeighbour(testListSecondClass, trainingListFirstClass, trainingListSecondClass,
					correct, k);
		} else if (classifier == 2 || classifier == 3) {
			correct = populateLengthListAverage(testListFirstClass, trainingListFirstClass, trainingListSecondClass,
					correct, k);

			correct = populateLengthListAverage(testListSecondClass, trainingListFirstClass, trainingListSecondClass,
					correct, k);
		}

		double correctness = (double) correct / (double) (testListFirstClass.size() + testListSecondClass.size()) * 100;
		DecimalFormat df = new DecimalFormat("##.00");

		System.out.println(correct + " <- Number of correct matches.");
		System.out.println(k + " <- k.");
		System.out.println(CLASSIFIERS[classifier] + " <- Classifier selected.");
		System.out.println(df.format(correctness) + "% <- of correctness.");
		System.out.println("=============================================");

	}

	public void splitObjects(int percentage) {
		database.load("C:\\dev\\Maple_Oak.txt");

		firstClassObjects = database.getFirstClassObjectList();
		secondClassObjects = database.getSecondClassObjectList();

		firstClassObjects = shuffleList(firstClassObjects);
		secondClassObjects = shuffleList(secondClassObjects);

		populateTrainingList(percentage, firstClassObjects, secondClassObjects);
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

	private int populateLengthListNeighbour(List<Object> testList, List<Object> trainingListFirst,
			List<Object> trainingListSecond, int correct, int k) {

		List<Double> distancesFirst;
		List<Double> distancesSecond;

		double first;
		double second;

		List<Double> kNNFirst;
		List<Double> kNNSecond;

		for (Object testObject : testList) {
			distancesFirst = new ArrayList<>();
			distancesSecond = new ArrayList<>();

			kNNFirst = new ArrayList<>();
			kNNSecond = new ArrayList<>();

			first = 0;
			second = 0;

			for (int i = 0; i < database.getNoFeatures(); i++) {
				distancesFirst.add(0d);
				distancesSecond.add(0d);

				for (Object trainingObject : trainingListFirst) {
					distancesFirst.add(Math
							.pow((double) (trainingObject.getFetures().get(i) - testObject.getFetures().get(i)), 2));
				}
				for (Object trainingObject : trainingListSecond) {
					distancesSecond.add(Math
							.pow((double) (trainingObject.getFetures().get(i) - testObject.getFetures().get(i)), 2));

				}

				first = Math.sqrt(distancesFirst.stream().mapToDouble(x -> x.doubleValue()).sum());
				second = Math.sqrt(distancesSecond.stream().mapToDouble(x -> x.doubleValue()).sum());

				kNNFirst = manageDistList(kNNFirst, first, k);
				kNNSecond = manageDistList(kNNSecond, second, k);

			}

			double sumFirst = kNNFirst.stream().mapToDouble(x -> x.doubleValue()).sum();
			double sumSecond = kNNSecond.stream().mapToDouble(x -> x.doubleValue()).sum();

			if (sumFirst < sumSecond && testObject.getClassName().equals("Acer")) {
				correct++;
			} else if (sumFirst > sumSecond && testObject.getClassName().equals("Quercus")) {
				correct++;
			}
		}
		return correct;
	}

	private int populateLengthListAverage(List<Object> testList, List<Object> trainingListFirst,
			List<Object> trainingListSecond, int correct, int k) {

		List<Double> distancesFirst;
		List<Double> distancesSecond;
		Map<Object, Float> averageMap = new HashMap<>();

		double first;
		double second;

		List<Double> kNNFirst;
		List<Double> kNNSecond;

		for (Object testObject : testList) {
			distancesFirst = new ArrayList<>();
			distancesSecond = new ArrayList<>();

			kNNFirst = new ArrayList<>();
			kNNSecond = new ArrayList<>();

			first = 0;
			second = 0;

			for (int i = 0; i < database.getNoFeatures(); i++) {
				distancesFirst.add(0d);
				distancesSecond.add(0d);

				for (Object trainingObject : trainingListFirst) {
					if (!averageMap.containsKey(trainingObject)) {
						averageMap.put(trainingObject, trainingObject.getFetures().get(i));
					} else {
						averageMap.put(trainingObject, averageMap.get(trainingObject) + i);
					}
				}
				for (Object trainingObject : trainingListSecond) {
					if (!averageMap.containsKey(trainingObject)) {
						averageMap.put(trainingObject, trainingObject.getFetures().get(i));
					} else {
						averageMap.put(trainingObject, averageMap.get(trainingObject) + i);
					}
				}

				for (Object trainingObject : trainingListFirst) {
					double objectAverage = averageMap.get(trainingObject) / database.getNoFeatures();
					distancesFirst.add(Math.pow((double) (objectAverage - testObject.getFetures().get(i)), 2));
				}
				for (Object trainingObject : trainingListSecond) {
					double objectAverage = averageMap.get(trainingObject) / database.getNoFeatures();
					distancesSecond.add(Math.pow((double) (objectAverage - testObject.getFetures().get(i)), 2));
				}

				first = Math.sqrt(distancesFirst.stream().mapToDouble(x -> x.doubleValue()).sum());
				second = Math.sqrt(distancesSecond.stream().mapToDouble(x -> x.doubleValue()).sum());

				kNNFirst = manageDistList(kNNFirst, first, k);
				kNNSecond = manageDistList(kNNSecond, second, k);

			}

			double sumFirst = kNNFirst.stream().mapToDouble(x -> x.doubleValue()).sum();
			double sumSecond = kNNSecond.stream().mapToDouble(x -> x.doubleValue()).sum();

			if (sumFirst < sumSecond && testObject.getClassName().equals("Acer")) {
				correct++;
			} else if (sumFirst > sumSecond && testObject.getClassName().equals("Quercus")) {
				correct++;
			}
		}
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
