
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Arrays;
import java.lang.Math;
class Scheduler{

	//グラフに関係する定数
	static final int MAPWIDTH = 750;
	static final int MAPHEIGHT = 750;
	static final int PARCENT = 5;
	static final double[] BALANCE = {0.11,0.11,0.11,0.11,0.12,0.11,0.11,0.11,0.11};
	

	private Data data;
	static String prefixNum;

	private ArrayList<Node> nodeList;//全てのノードリスト
	private ArrayList<Node> sampleList;//はじめのラウンドでメッセージを送るノードリスト
	private int maxNodes;
		
	static int algorithmType;
	static int round;
	private int ttl;

	Scheduler(int maxNodes,int algorithmType,String prefixNum,Data data){
		this.data = data;
		this.prefixNum = prefixNum;
		this.maxNodes = maxNodes;//100の倍数
		this.nodeList = new ArrayList<Node>();
		this.sampleList = new ArrayList<Node>();
		this.round = 0;
		this.ttl=8;
		this.algorithmType = algorithmType;

		this.createNodes(maxNodes);
		this.createLink();
	}

	public ArrayList<Node> createNodes(int roopNum){ 
		int count1=0,count2=0;
		for(int j=0;j<9;j++){
			count1 +=(int)roopNum*BALANCE[j];	
			for(int i=count2;i<count1;i++){
				Random rand = new Random(); 
				Node node = new Node(i,j,algorithmType);
				nodeList.add(node);  
				Point a = createPoint(node.getEria());
				node.setPoint(a);
			}
			count2=count1;
		}
		return nodeList;
	}

	public Point createPoint(int eria){
		Random rand = new Random();

		int xSize=MAPWIDTH/3;
		int ySize=MAPHEIGHT/3;
		int x=0,y=0;
		
		/*if(eria == 0){
			x = rand.nextInt(xSize);
			y = rand.nextInt(ySize);
		}
		else if(eria ==1){
			x = rand.nextInt(xSize)+250;
			y = rand.nextInt(ySize);
		}
		else if(eria ==2){
			x = rand.nextInt(xSize)+500;
			y = rand.nextInt(ySize);
		}
		else if(eria ==3){
			x = rand.nextInt(xSize);
			y = rand.nextInt(ySize)+250;
		}
		else if(eria ==4){
			x = rand.nextInt(xSize)+250;
			y = rand.nextInt(ySize)+250;
		}
		else if(eria ==5){
			x = rand.nextInt(xSize)+500;
			y = rand.nextInt(ySize)+250;
		}
		else if(eria ==6){
			x = rand.nextInt(xSize);
			y = rand.nextInt(ySize)+500;
		}
		else if(eria ==7){
			x = rand.nextInt(xSize)+250;
			y = rand.nextInt(ySize)+500;
		}
		else if(eria ==8){
			x = rand.nextInt(xSize)+500;
			y = rand.nextInt(ySize)+500;
		}*/

		x=rand.nextInt(MAPWIDTH);
		y=rand.nextInt(MAPHEIGHT);

		Point point = new Point(x,y);
		return point;
	}

  public boolean searchPath(Node a,Node b){

    ArrayList<Node> link = a.getLink();
    boolean flag = true;

    for(Node node:link){
      if(node.getId()==b.getId()) flag=false; 
    }

    return flag;
  }


	public void createLink(){

		Random rand = new Random();

		for(Node a :nodeList){
			for(Node b :nodeList){
				if(a!=b && searchPath(a,b)){
					Point pointA =a.getPoint();
					Point pointB =b.getPoint();
					double c = pointB.getX()-pointA.getX();
					double d = pointB.getY()-pointA.getY();


					if(Math.sqrt((c*c)+(d*d))<100){	
						a.setLink(b);
						b.setLink(a);
					}

				}
			}
		}
    //リンクサイズ削減
		/*for(Node a:nodeList){
			ArrayList<Node> link = a.getLink();
			int tmp = rand.nextInt(5)+4;
			while(link.size() >= tmp){
				int random = rand.nextInt(link.size());			
				link.remove(random);
			}
		}*/
	}

	public ArrayList<Node> getList(){
		return nodeList;
	}

	/**
	 *シミュレートを開始するメソッド
	 *typeは0~2
	 **/

	public void start(int roundCount){

		for(int i=0;i<roundCount;i++){
			this.round=i;
			pickOutBuff();
			decide();	
		}

		output(roundCount);
	}
	/**
	 *新しいメッセージ送信を行うノードの割合を決めるメソッド
	 **/

	public void decide(){
		Random rand = new Random();
		int count=0;
		ArrayList<Node> flagList = new ArrayList<Node>();
		while(count<(maxNodes/100)*PARCENT){
			int number = rand.nextInt(maxNodes);
			Node node = nodeList.get(number);
			if(!node.getMessageFlag()){
				node.createMessage(this.round,this.ttl);	
				node.setMessageFlag(true);
				flagList.add(node);
				if(this.round==0){
					sampleList.add(node);
				}
				count++;
			}
		}
		//フラグを初期化
		for(Node node :flagList){
			node.setMessageFlag(false);	
		}
	}

	public void pickOutBuff(){
		for(Node node :nodeList){
			node.gossip();
		}
	}

	public void output(int round){
		int addCount=0;
		//int firstCount=addCount;
		for(Node node :sampleList){
			int count=0;
			for(Node tmp :nodeList){
				ArrayList<Message> messageList = tmp.getBuffer();
				for(Message message : messageList){
					if(message.getId() == node.getId()){	
					//	if(message.getRound() == 0){
							count++;
					//	}
					}
				}
			}

			System.out.println(count+":"+maxNodes);

			double ans = (((float)count)/((float)maxNodes)); 
			
			
			ArrayList<ArrayList<Double>> list = data.getData();
			if(maxNodes==100){
				ArrayList<Double> dataList = new ArrayList<Double>();
				dataList.add(ans);
				list.add(dataList);
				addCount++;
			}
			else{
				list.get(addCount).add(ans);
				addCount++;
			}

			if(addCount>=5){
				break;
			}

		}
	}

	static void write(Data data){
		try {
			File csv = new File(prefixNum+"data.csv"); // CSVデータファイル
			// 追記モード
			BufferedWriter bw 
				= new BufferedWriter(new FileWriter(csv, true)); 
			// 新たなデータ行の追加

			ArrayList<ArrayList<Double>> dataSet =  data.getData();
			for(ArrayList<Double> tmp1 :dataSet){
				String str="";
				for(double tmp2 :tmp1){
					str=str+tmp2*100+",";
				}

				bw.write(str);
				bw.newLine();
			}
			bw.close();

		} catch (FileNotFoundException e) {
			// Fileオブジェクト生成時の例外捕捉
			e.printStackTrace();
		} catch (IOException e) {
			// BufferedWriterオブジェクトのクローズ時の例外捕捉
			e.printStackTrace();
		}
	}

	public boolean debug(){
		int[] eriaCount = new int[9];

		Arrays.fill(eriaCount,0);

		nodeList.get(0).broadCast();

		for(Node node : nodeList){
			if(!node.getStartFlag()){
				System.out.println(node.getId()+":"+node.getStartFlag());
				return false;
			}
    }

    System.out.println("Graph Create");

		for(Node node : nodeList){
			eriaCount[node.getEria()]++;
			ArrayList<Node> list = node.getLink();	
		  System.out.print(list.size() + " ");	
    }
		System.out.println("");

		System.out.println("各エリアのNode数");
		for(int a:eriaCount){
			System.out.print(a+" ");	
		}
		System.out.println("");

		return true;

	}

  ArrayList<Node> getNodeList(){
    return nodeList; 
  }

  int getMaxNode(){
    return maxNodes;
  }

  void setProbability(double one,int two,double three){
    for(Node node:nodeList){
      node.setProbability(one,two,three); 
    }
    System.out.println("set compleat");
  }

}
