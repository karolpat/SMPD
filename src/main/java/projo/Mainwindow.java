package projo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import org.apache.commons.math3.util.Combinations;

import Jama.Matrix;

public class Mainwindow {

	Database database = new Database(0, 0, 0);
	Map<Integer, Float> featureAvgFirstClass = new HashMap<Integer, Float>();
	Map<Integer, Float> featureAvgSecondClass = new HashMap<Integer, Float>();

	Map<Integer, Float> featureStdsFirstClass = new HashMap<Integer, Float>();
	Map<Integer, Float> featureStdsSecondClass = new HashMap<Integer, Float>();

	Float[][] firstClassArray;
	Float[][] secondClassArray;

	int[] go(int dimension, int variant, String filePath) {

		database.load(filePath);

		Scanner sc1 = new Scanner(System.in);

		System.out.println(dimension + " dim");
		System.out.println(database.getNoClass() + " noclass");

		firstClassArray = database.getFirstClassArray();
		secondClassArray = database.getSecondClassArray();

		getClassAverages(featureAvgFirstClass, featureAvgSecondClass);
		getClassStds(featureStdsFirstClass, featureStdsSecondClass);

		if (variant == 0) {
			if (dimension == 1) {
				int[] temp = getFisher();
				System.out.println(Arrays.toString(temp) + " <- Set of best features.");
				return temp;
			} else {
				int[] temp = discriminantFisher(dimension);
				System.out.println(Arrays.toString(temp) + " <- Set of best features.");
				return temp;
			}
		} else if (variant == 1) {
			int[] temp = sequentialFisher(dimension);
			System.out.println(Arrays.toString(temp) + " <- Set of best features.");
			return temp;
		} else {
			System.out.println("end");
		}
		return null;
	}

	private int[] getFisher() {
		int[] bestFeature = new int[1];
		double FLD = 0;
		double temp;
		int max_ind = -1;

		for (int i = 0; i < database.getNoFeatures(); i++) {
			temp = Math.abs((featureAvgFirstClass.get(i) - featureAvgSecondClass.get(i))
					/ (featureStdsFirstClass.get(i) + featureStdsSecondClass.get(i)));

			if (temp > FLD) {
				FLD = temp;
				max_ind = i;
			}
		}
		bestFeature[0] = max_ind;
		return bestFeature;
	}

	private int[] discriminantFisher(int dimension) {

		Combinations comb = new Combinations(database.getNoFeatures(), dimension);
		Map<int[], Double> fishers = new HashMap<int[], Double>();

		for (int[] features : comb) {
			double fisher = calculateFisher(features);
			fishers.put(features, fisher);
		}

		int[] result = Collections.max(fishers.entrySet(), Map.Entry.comparingByValue()).getKey();
		return result;
	}

	private int[] sequentialFisher(int dimension) {

		List<Integer> features = new ArrayList<>();
		List<Integer> featureList = new ArrayList<>();

		features.add(getFisher()[0]);

		for (int i = 0; i < dimension - 1; i++) {
			double[] consecutiveFeatures = new double[database.getNoFeatures()];

			for (int j = 0; j < database.getNoFeatures(); j++) {
				if (!features.contains(j)) {

					featureList = new ArrayList<>(features);
					featureList.add(j);
				}

				int[] featuresArray = featureList.stream().mapToInt(x -> x).toArray();
//				System.out.println(Arrays.toString(featuresArray));
				consecutiveFeatures[j] = calculateFisher(featuresArray);
			}
			int bestFeatureIndex = getIndexOfLargest(consecutiveFeatures);
			features.add(bestFeatureIndex);
		}
		int[] result = features.stream().mapToInt(x -> x).toArray();
		return result;
	}

	private double calculateFisher(int[] comb) {

		double[][] sFirstClassArray = new double[comb.length][firstClassArray[0].length];
		double[][] sSecondClassArray = new double[comb.length][secondClassArray[0].length];
		List<Double> vector = new ArrayList<Double>();
		double vectorSum = 0;
		List<Integer> featuresList = new ArrayList<Integer>();

		int n = 0;
		for (int i = 0; i < database.getNoFeatures(); i++) {
			final int index = i;

			if (IntStream.of(comb).anyMatch(x -> x == index)) {

				for (int j = 0; j < firstClassArray[0].length; j++) {

					sFirstClassArray[n][j] = firstClassArray[i][j] - featureAvgFirstClass.get(i);
				}
				for (int j = 0; j < secondClassArray[0].length; j++) {

					sSecondClassArray[n][j] = secondClassArray[i][j] - featureAvgSecondClass.get(i);
				}
				vectorSum += featureAvgFirstClass.get(i) - featureAvgSecondClass.get(i);
				vector.add(vectorSum);
				featuresList.add(i);
				n++;
			}
		}

		Matrix sFirstClassMtx = new Matrix(sFirstClassArray);
		Matrix sFirstClassMulti = sFirstClassMtx.times(sFirstClassMtx.transpose());
		sFirstClassMulti.timesEquals(1.0 / (firstClassArray[0].length * 1.0));

		Matrix sSecondClassMtx = new Matrix(sSecondClassArray);
		Matrix sSecondClassMulti = sSecondClassMtx.times(sSecondClassMtx.transpose());
		sSecondClassMulti.timesEquals(1.0 / (secondClassArray[0].length * 1.0));

		Matrix subtracted = sFirstClassMulti.minus(sSecondClassMulti);

		double det = subtracted.det();
		double numerator = 0;

		for (Double d : vector) {
			numerator += Math.pow(d, 2);
		}
		return Math.sqrt(numerator) / det;
	}

	private void getClassAverages(Map<Integer, Float> featureAvgFirstClass, Map<Integer, Float> featureAvgSecondClass) {

		float firstAverage = 0;
		float secondAverage = 0;

		for (int i = 0; i < firstClassArray.length; i++) {
			firstAverage = 0;
			secondAverage = 0;
			for (Float f : firstClassArray[i]) {
				firstAverage += f;
			}
			firstAverage = firstAverage / firstClassArray[i].length;
			featureAvgFirstClass.put(i, firstAverage);

			for (Float f : secondClassArray[i]) {
				secondAverage += f;
			}
			secondAverage = secondAverage / secondClassArray[i].length;
			featureAvgSecondClass.put(i, secondAverage);
		}
	}

	private void getClassStds(Map<Integer, Float> featureStdsFirstClass, Map<Integer, Float> featureStdsSecondClass) {

		float firstStds = 0;
		float secondStds = 0;

		for (int i = 0; i < firstClassArray.length; i++) {
			firstStds = 0;
			secondStds = 0;
			float temporary = 0;
			for (Float f : firstClassArray[i]) {
				temporary += Math.pow(f - featureAvgFirstClass.get(i), 2);
			}
			firstStds = (float) Math.sqrt(temporary / firstClassArray[i].length);
			featureStdsFirstClass.put(i, firstStds);
			temporary = 0;
			for (Float f : secondClassArray[i]) {
				temporary += Math.pow(f - featureAvgSecondClass.get(i), 2);
			}
			secondStds = (float) Math.sqrt(temporary / secondClassArray[i].length);
			featureStdsSecondClass.put(i, secondStds);
		}
	}

	public int getIndexOfLargest(double[] array) {
		if (array == null || array.length == 0)
			return -1; // null or empty

		int largest = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > array[largest])
				largest = i;
		}
		return largest; // position of the first largest found
	}

}
