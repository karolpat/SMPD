package projo;

public class Main {
	public static void main(String[] args) {
		
		Database db = new Database(0, 0, 0);
		
		System.out.println(db.load("C:\\Users\\Karol Patecki\\Downloads\\SMPD\\SMPD\\Maple_Oak.txt"));
		System.out.println(db.getNoObjects()+" objs");
		System.out.println(db.getNoFeatures()+" feas");
		System.out.println(db.getNoClass()+" classs");

	}
}
