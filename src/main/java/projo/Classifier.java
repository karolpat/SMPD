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

		firstClassObjects = database.getFirstClassObjectList();
		secondClassObjects = database.getSecondClassObjectList();

		firstClassObjects = shuffleList(firstClassObjects);
		secondClassObjects = shuffleList(secondClassObjects);

		System.out.println(
				"fc: " + firstClassObjects.size() + " , sc: " + secondClassObjects.size() + " per: " + percentage);
		populateTrainingList(percentage, firstClassObjects, secondClassObjects);
		System.out.println(trainingList.size() + " sizetraining");
		System.out.println(testList.size() + " size");
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
		
		int temp = newSize/2;
		trainingList = firstClassObjects.subList(0, newSize / 2);
		trainingList.addAll(secondClassObjects.subList(0, newSize / 2));
		
//		testList.addAll(firstClassObjects.subList(newSize/2, firstClassObjects.size()));
		testList.addAll(firstClassObjects.subList(temp, firstClassObjects.size()));
//		testList.addAll(secondClassObjects.subList(temp, secondClassObjects.size()));
	}

	public List<Object> getFirstClassObject() {
		return firstClassObjects;
	}

	public List<Object> getSecondClassObject() {
		return secondClassObjects;
	}
}
