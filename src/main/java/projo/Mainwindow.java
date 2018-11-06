package projo;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Mainwindow {

	Database database = new Database(0, 0, 0);

	void go() {

		database.load("C:\\Users\\Karol Patecki\\Downloads\\SMPD\\SMPD\\Maple_Oak.txt");

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

		if (dimension == 1 && database.getNoClass() == 2) {

			double FLD = 0;
			double temp;
			int max_ind = -1;
			

			Map<String, Double> classAverages = new HashMap<String, Double>();
			classAverages.put(database.getClassNames().get(0), 0.0);
			classAverages.put(database.getClassNames().get(1), 0.0);
			Map<String, Double> classStds = new HashMap<String, Double>();
			classStds.put(database.getClassNames().get(0), 0.0);
			classStds.put(database.getClassNames().get(1), 0.0);

			for (int i = 0; i < database.getNoFeatures(); i++) {
				System.out.println("+++++++++++++++++++++++++++++++");
				System.out.println(i);
				System.out.println("+++++++++++++++++++++++++++++++");


				for (Object ob : database.getObjects()) {

//					if (!classAverages.containsKey(ob.getClassName())) {
//						classAverages.put(ob.getClassName(), 0F);
//					}
//
//					if (!classStds.containsKey(ob.getClassName())) {
//						classStds.put(ob.getClassName(), 0F);
//					}

					classAverages.put(ob.getClassName(), classAverages.get(ob.getClassName()) + ob.getFetures().get(i));
					classStds.put(ob.getClassName(),
							(classStds.get(ob.getClassName()) + Math.pow(ob.getFetures().get(i), 2)));
					System.out.println(Math.pow(ob.getFetures().get(i), 2) + " pow");
					System.out.println(ob.getClassName() + " name, stds " + classStds.get(ob.getClassName()));
				}

				for (Map.Entry<String, Integer> entry : database.getClassCounter().entrySet()) {
					classAverages.put(entry.getKey(), classAverages.get(entry.getKey()) / entry.getValue());
					classStds.put(entry.getKey(), Math.sqrt(classStds.get(entry.getKey()) / entry.getValue()
							- Math.pow(classAverages.get(entry.getKey()), 2)));
//					System.out.println(classAverages.get(entry.getKey()) + " v,avg,k: " + entry.getKey());
//					System.out.println(classStds.get(entry.getKey()) + " v,stds,k: " + entry.getKey());
				}
//				System.out.println(classAverages.get(database.getClassNames().get(0))+" avg, 0");
//				System.out.println(classAverages.get(database.getClassNames().get(1))+" avg, 1");
//				System.out.println(classStds.get(database.getClassNames().get(0))+" stds, 0");
//				System.out.println(classStds.get(database.getClassNames().get(1))+" stds, 1");
				temp = Math.abs(classAverages.get(database.getClassNames().get(0))
						- classAverages.get(database.getClassNames().get(1))
								/ (classStds.get(database.getClassNames().get(0))
										+ classStds.get(database.getClassNames().get(1))));
				if (temp > FLD) {
					FLD = temp;
					max_ind = i;
				}
			}

			System.out.println("max ind= " + max_ind);
			System.out.println("FLD= " + FLD);
		}

	}

}
