import java.util.Scanner;
import java.util.ArrayList;

class Gossip{

	static final int CREATENUM=1;	
	static final int SIMULATE_COUNT=1;
	static final int[] NODE_LIST={100,200,300,400,500,600,700,800,900,1000};
	static final int[] ROUND={10,50,100};
	static final int ALGORITHM = 0;

	Gossip(){
		for(int i=0;i<CREATENUM;i++){	
			int count=0;	
			Data data = new Data();
			while(true){
				Scheduler schedule = new Scheduler(NODE_LIST[count],ALGORITHM,ROUND[0]+"_2_",data);
				if(schedule.debug()){

					System.out.println("start");
					//描画が必要な場合はコメントアウトを外す。
					GossipView view = new GossipView(schedule); 

					Eff eff = new Eff(schedule);
					schedule.setProbability(eff.getProbability(),eff.getFixedFanout(),eff.getProbability());

					schedule.start(ROUND[2]);

					if(count>=SIMULATE_COUNT){//NODE_LIST.length-1){
						break;
					}
					count++;
				}
			}

			Scheduler.write(data);
		}
		System.out.println("ProgramEnd");
	}

	public static void main(String[] args){

		Gossip test = new Gossip();
	}

}
