package projo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Classifier {

	private List<Object> trainingList = new ArrayList<>();
	private List<Object> testList = new ArrayList<>();

	private Database database = new Database(0, 0, 0);
	
	private List<Object> firstClassObjects;
	private List<Object> secondClassObjects;
	

	public void splitObjects(int percentage) {
		database.load("C:\\dev\\Maple_Oak.txt");
		
		firstClassObjects=database.getFirstClassObjectList();
		secondClassObjects=database.getSecondClassObjectList();
		
		System.out.println("fc: "+firstClassObjects.size()+" , sc: "+secondClassObjects.size());
	}
}
