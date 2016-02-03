import java.util.concurrent.LinkedBlockingQueue;
import java.util.ArrayList;
import java.awt.Point;
import java.util.NoSuchElementException;
import java.util.Random;

class Node extends LinkedBlockingQueue{

  static final int BUFFER_STORAGE=50;
  static double FIXEDPROBABILITY=0.5;
  static int FIXEDFUNOUT=5;
  static double PROBABILITYBLOADCAST=0.5;
  private int id;
  private ArrayList<Node> link;
  private int eria;
  private Point point;
  private int algorithmType;
  private ArrayList<Message> buffer;
  private ArrayList<Message> overflow;
  private boolean startFlag;
  private boolean messageFlag;
  private boolean firstFlag;

  Node(int id,int eria,int algorithmType){
    this.id = id;
    this.link = new ArrayList<Node>();
    this.buffer = new ArrayList<Message>();
    this.overflow = new ArrayList<Message>();
    this.point = null;
    this.eria = eria; 
    this.startFlag = false;
    this.messageFlag = false;
    this.firstFlag = false;
    this.algorithmType = algorithmType;
  }

  public void setId(int id){
    this.id = id;
  }

  public int getId(){
    return this.id; 
  }

  public void setLink(Node node){
    this.link.add(node);
  }

  public ArrayList<Node> getLink(){
    return this.link;
  }

  public void setEria(int eria){
    this.eria = eria; 
  }

  public int getEria(){
    return this.eria;
  }

  public void setPoint(Point point){
    this.point = point;
  }

  public Point getPoint(){
    return this.point; 
  }

  public boolean getMessageFlag(){
    return messageFlag;
  }

  public ArrayList<Message> getBuffer(){
    return buffer;
  }

  public ArrayList<Message> getOverflow(){
    return overflow;
  }

  public void createMessage(int round,int ttl){
    Message message = new Message(id,round,ttl);			
    this.firstFlag=true;
    this.receive(message);
    this.gossip();
  }


  /**
   *メッセージがキャッシュにあるかを確かめる。
   *(現在作成されたラウンドが違う同じメッセージはキャッシュへ)
   **/

  public boolean notCache(Message message){
    for(Message tmp:buffer){
      if((message.getId() == tmp.getId() )){   //&&(message.getRound() == tmp.getRound()))
        return false;	
      }
    }
    return true;
  }

  public void receive(Message message){
    if(message.getTTL() > 0 && notCache(message)){
      this.add(message);	
      if(this.buffer.size() >= BUFFER_STORAGE && message.getId() != this.id){
        //this.overflow.add(this.buffer.get(0));
        this.buffer.remove(0);
        this.buffer.add(message);	

      }
      else if(message.getId() != this.id){
        this.buffer.add(message);

      }

      //System.out.println("send:"+id+" -> receive:"+message.getId());
    }
    return;
  }

  /**
   *Gossipでメッセージを送るノードを選択
   **/

  public ArrayList<Node> chooseNode(){

    Random rand = new Random();
    ArrayList<Node> chooseList = new ArrayList<Node>();

    if(algorithmType==0){	

      for(Node node:link){
        if(rand.nextDouble() < FIXEDPROBABILITY){
          chooseList.add(node);
        }
      }
    }
    else if(algorithmType==1){
      if(link.size()<=FIXEDFUNOUT){
        chooseList = link;
      }
      else{
        int count=0;
        while(true){
          Node tmp = link.get(rand.nextInt(link.size()));
          boolean flug = true;
          for(Node node:chooseList){
            if(tmp == node){
              flug = false;
            }
          }

          if(flug){
            chooseList.add(tmp);
            count++;
          }

          if(count<=FIXEDFUNOUT){
            break;
          }
        }
      }

    }
    else if(algorithmType==2){

      if(firstFlag || rand.nextDouble() < PROBABILITYBLOADCAST){
        firstFlag=false;
        for(Node node:link){
          chooseList.add(node);
        }

      }	
    }

    return chooseList;
  }

  public void gossip(){

    ArrayList<Node> chooseList = chooseNode();


    try{
      while(true){
        Message message = (Message)this.remove();				
        for(Node node:chooseList){

          node.receive(new Message(message.getId(),message.getRound(),message.getTTL()-1));
        }
      }	
    }
    catch(NoSuchElementException e){
      //System.out.println("round"+id+":"+"Gossip通過");
      return;	
    }
  }

  public void algorithm(Message message){

    if(algorithmType==0){

    }
    else if(algorithmType==1){

    }
    else if(algorithmType==2){

    }

  }

  public void setMessageFlag(boolean flag){
    messageFlag = flag; 	
  }

  public boolean getStartFlag(){
    return startFlag;
  }

  public void broadCast(){

    startFlag=true;
    for(Node node :link){

      if(!node.getStartFlag()){
        node.broadCast();  
      }
    }
  }

  public void setProbability(double one,int two,double three){
    this.FIXEDPROBABILITY=one;
    this.FIXEDFUNOUT=two;
    this.PROBABILITYBLOADCAST=three;
  }

}
