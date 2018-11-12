package projo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Mainwindow {

	Database database = new Database(0, 0, 0);

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
		System.out.println(dimension + " dim");
		System.out.println(database.getNoClass() + " noclass");

		Float[][] firstClassArray = database.getFirstClassArray();
		Float[][] secondClassArray = database.getSecondClassArray();

		if (dimension == 1 && database.getNoClass() == 2) {
			//
			float FLD = 0;
			float temp;
			int max_ind = -1;

			Map<Integer, Float> featureAvgFirstClass = new HashMap<Integer, Float>();
			Map<Integer, Float> featureAvgSecondClass = new HashMap<Integer, Float>();

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

			Map<Integer, Float> featureStdsFirstClass = new HashMap<Integer, Float>();
			Map<Integer, Float> featureStdsSecondClass = new HashMap<Integer, Float>();

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
				System.out.println(featureStdsSecondClass.get(i));
			}

			for (int i = 0; i < database.getNoFeatures(); i++) {
				temp = Math.abs((featureAvgFirstClass.get(i) - featureAvgSecondClass.get(i))
						/ (featureStdsFirstClass.get(i) + featureStdsSecondClass.get(i)));

				if (temp > FLD) {
					FLD = temp;
					max_ind = i;
				}
			}

			System.out.println(FLD + " fld, ind: " + max_ind);
		}
	}

}
