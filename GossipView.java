import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Graphics2D;


class GossipView extends JPanel{

  private Scheduler admin;

  GossipView(Scheduler admin){
    this.admin = admin;

    JFrame window = new JFrame("Gossip");	
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
    window.getContentPane().add(this);
    window.setSize(800, 800);
    this.setSize(800,800);
    window.setVisible(true);
    this.setVisible(true);

  }

  public void paintComponent(Graphics aGraphics){	
    ArrayList<Node> list = admin.getList();
    Graphics2D strGraphics = (Graphics2D)aGraphics;		

    for(Node node:list){	
      aGraphics.setColor(Color.red);
      aGraphics.fillArc((int)node.getPoint().getX(),(int)node.getPoint().getY(), 10, 10,0,360);


      //System.out.println((int)node.getPoint().getX()+","+(int)node.getPoint().getY());
      aGraphics.setColor(Color.black);
      strGraphics.drawString(((Integer)node.getId()).toString(),(int)node.getPoint().getX(),(int)node.getPoint().getY());
      ArrayList<Node> link = node.getLink();
      for(Node nexthop:link){
        aGraphics.drawLine((int)node.getPoint().getX()+5,(int)node.getPoint().getY()+5,(int)nexthop.getPoint().getX()+5,(int)nexthop.getPoint().getY()+5);
      }
    }
  }

}
