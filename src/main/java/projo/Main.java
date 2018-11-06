package projo;

public class Main {
	public static void main(String[] args) {

		Database db = new Database(0, 0, 0);
		Mainwindow mainwindow = new Mainwindow();
//		db.load("C:\\Users\\Karol Patecki\\Downloads\\SMPD\\SMPD\\Maple_Oak.txt");
		mainwindow.go();
//		for (int i = 0; i < db.getNoFeatures(); i++) {
//
//			for (Object ob : db.getObjects()) {
//				System.out.println(ob.getClassName() + " name, v: " + ob.getFetures().size());
//			}
//		}
//		System.out.println(ob.getClassName()+" name, v: "+ob.getFetures().get(i));
	}
}
