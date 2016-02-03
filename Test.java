import java.util.Map;
import java.util.HashMap;


class Test{

  public static void main(String[] args){
    HashMap<Integer,String> map = new HashMap<Integer,String>();
    
    for(int i=0;i<10;i++){ 
      map.put(i,"a");
    }
    
    map.put(10,"b");

    for(Map.Entry<Integer,String> entry : map.entrySet()){ 
      System.out.println(entry.getValue());
    }
  }

}
