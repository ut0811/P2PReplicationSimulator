import java.util.ArrayList;
import java.util.Arrays;

class Eff{

  private final double EFF = 5;
  private Scheduler schedule;
  private int algorithm;
  private double ave; 

  private int[] count;

  Eff(Scheduler schedule){
    this.schedule = schedule;
    this.ave = averageLink(schedule.getNodeList()); 
  	count = new int[100];
  	Arrays.fill(count,0);
	linkCount();
  }

  double averageLink(ArrayList<Node> nodeList){

	int N=schedule.getMaxNode();
	double p=100,a=750,b=750;

    double v = (N*(3.14*p*p))/(a*b);
	
		return v;
    }

	void linkCount(){
		ArrayList<Node> nodeList = schedule.getNodeList();	
		for(Node node :nodeList){
			count[node.getLink().size()]++;	
		}
	}
	

  double getProbability(){
    double pb =  EFF/ave; 
    System.out.println("pb:"+pb);
    return pb;
  }
  
  int getFixedFanout(){

    int tmp=0;
    int fanout=0;
    for(int f=1;f<100;f++){
      for(int i=1;i<f;i++){
        tmp+=count[i]*i;
      }
      for(int i=f;i<100;i++){
        tmp+=count[i]*f; 
      }
    
      if(tmp/schedule.getMaxNode()>=EFF){
        fanout=f;
        break;
      }
    
    }
    
    System.out.println("fixed:"+fanout);
    return fanout;
  }

}
