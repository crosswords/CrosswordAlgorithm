package models;
import java.awt.*;

/**
 * Created by Tomek on 2014-12-25.
 */
public class Word {
    public Word(){}
    public Point start;
    public Point end;
    public char[] word;
    public int length;
    public boolean isFilled;
    public int possibilities;
    public java.util.List<String> entries;
    //horizontal??
    public boolean hasPoint(Point p){
        if(p.getX()==start.getX()&&p.getX()==end.getX()){
            if(p.getY()>=start.getY()&&p.getY()<=end.getY()){
                return true;
            }
        }
        if(p.getY()==start.getY()&&p.getY()==end.getY()){
            if(p.getX()>=start.getX()&&p.getX()<=end.getX()){
                return true;
            }
        }
        return false;
    }
}
