package projo;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		Database db = new Database(0, 0, 0);
		Mainwindow mainwindow = new Mainwindow();
		Classifier classifier = new Classifier();
		// db.load("C:\\Users\\Karol Patecki\\Downloads\\SMPD\\SMPD\\Maple_Oak.txt");
		
		System.out.println("What you want?");
		System.out.println("1=fisher, 2=NNClasifier");
		
		Scanner sc = new Scanner(System.in);
		int listening = sc.nextInt();
		System.out.println(db.getObjects().size());
		System.out.println("Give me %");
		int percentage = sc.nextInt();
		System.out.println("Give me k, max 10");
		int k=sc.nextInt();
		sc.close();
		if(listening==1) {
//			mainwindow.go();
		}else {
//			classifier.classificate(percentage,k);
		}
		
		// for (int i = 0; i < db.getNoFeatures(); i++) {
		//
		// for (Object ob : db.getObjects()) {
		// System.out.println(ob.getClassName() + " name, v: " +
		// ob.getFetures().size());
		// }
		// }
		// System.out.println(ob.getClassName()+" name, v: "+ob.getFetures().get(i));

	}
}
