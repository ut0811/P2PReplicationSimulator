import java.util.ArrayList;

class Data{

	private int dataCount=0;
	private ArrayList<ArrayList<Double>> data;
	
	Data(){
		data = new ArrayList<ArrayList<Double>>();	
	}

	ArrayList<ArrayList<Double>> getData(){
		return data;
	}

}
