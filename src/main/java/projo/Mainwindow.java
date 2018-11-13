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

	void go() {

		database.load("C:\\dev\\Maple_Oak.txt");

		System.out.println("Enter number");
		int dimension;
		Scanner sc = new Scanner(System.in);
		while (true) {
			dimension = sc.nextInt();

			if (dimension > 64 || dimension < 1) {

				System.out.println("Enter number");
				dimension = sc.nextInt();
			} else {
				break;
			}
		}

		Scanner sc1 = new Scanner(System.in);

		System.out.println("0=discriminant; 1=sequential");
		int variant = sc1.nextInt();
		sc1.close();

		System.out.println(dimension + " dim");
		System.out.println(database.getNoClass() + " noclass");

		firstClassArray = database.getFirstClassArray();
		secondClassArray = database.getSecondClassArray();

		getClassAverages(featureAvgFirstClass, featureAvgSecondClass);
		getClassStds(featureStdsFirstClass, featureStdsSecondClass);
		if (variant == 0) {
			discriminantFisher(dimension);
		} else if (variant == 1) {
			sequentialFisher(dimension);
		} else {
			System.out.println("end");
		}

	}

	private int[] discriminantFisher(int dimension) {
		if (dimension == 1 && database.getNoClass() == 2) {
			//
			float FLD = 0;
			float temp;
			int max_ind = -1;

			for (int i = 0; i < database.getNoFeatures(); i++) {
				temp = Math.abs((featureAvgFirstClass.get(i) - featureAvgSecondClass.get(i))
						/ (featureStdsFirstClass.get(i) + featureStdsSecondClass.get(i)));

				if (temp > FLD) {
					FLD = temp;
					max_ind = i;
				}
			}
			System.out.println(FLD + " fld, ind: " + max_ind);
			return null;

		} else {

			Combinations comb = new Combinations(database.getNoFeatures(), dimension);
			Map<int[], Double> fishers = new ConcurrentHashMap<int[], Double>();

			for (int[] features : comb) {
				double fisher = getFisher(features);
				fishers.put(features, fisher);
			}

			int[] result = Collections.max(fishers.entrySet(), Map.Entry.comparingByValue()).getKey();
			System.out.println(Arrays.toString(result) + " max");
			return result;
		}
	}

	private void sequentialFisher(int dimension) {

		List<Integer> features = new ArrayList<>();
		// Set<Integer> features = new HashSet<>();

		for (int i = 0; i < database.getNoFeatures(); i++) {
			int[] firstClassFisher = new int[firstClassArray[0].length];
			double[] secondClassFisher = new double[secondClassArray[0].length];

			for (int j = 0; j < firstClassArray[0].length; j++) {
				if (i == j || features.contains(j)) {
					firstClassFisher[j] = Integer.MIN_VALUE;
				}
				List<Integer> featureList = new ArrayList<>(features);
				featureList.add(j);
				
				int[] featuresArray = featureList.stream().mapToInt(x -> x).toArray();
				firstClassFisher = discriminantFisher(featuresArray);
			}
		}
	}

	private double getFisher(int[] comb) {

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

		Matrix sSecondClassMtx = new Matrix(sSecondClassArray);
		Matrix sSecondClassMulti = sSecondClassMtx.times(sSecondClassMtx.transpose());

		Matrix subtracted = sFirstClassMulti.minus(sSecondClassMulti);

		double det = subtracted.det();

		double numerator = 0;

		for (Double d : vector) {
			numerator += Math.pow(d, 2);
		}
		return Math.sqrt(numerator) / det;
		// System.out.println(FLD);
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

}
