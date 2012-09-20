
import java.util.Arrays;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 10350470
 */
public class StateRepresentation {
    private double  [] horizontal;
    private double [] vertical;
    private double [] diagonal;

    public static enum Type {
     Horizontal , Vertical, Diagonal
    }
    
    public StateRepresentation (){
        horizontal = new double [6];
        vertical = new double[6];
        diagonal = new double [9];
        Arrays.fill(horizontal, 0.0);
        Arrays.fill(vertical, 0.0);
        Arrays.fill(diagonal, 0.0);
    }

    public double getVvalue(Type type, int distance){
        switch (type){
            case Horizontal: return horizontal[distance];
            case Vertical: return vertical[distance];
            case Diagonal: return diagonal[distance-2];
        }
        return -1.0;
    }

    public double getVvalue(int linearIndex){
        if(linearIndex < 6){
            return horizontal[linearIndex];
        }
        else if (linearIndex < 12){
            return vertical [linearIndex-6];
        }
        else{
            return diagonal[linearIndex-12];
        }
    }

    public void setVvalue(int linearIndex, double value){
        if(linearIndex < 6){
            horizontal[linearIndex]=value;
        }
        else if (linearIndex < 12){
            vertical [linearIndex-6]=value;
        }
        else{
            diagonal[linearIndex-12]=value;
        }
    }

   
    

    public void setVvalue(Type type, int distance, double value){
        switch (type){
            case Horizontal: horizontal[distance] = value; break;
            case Vertical:  vertical[distance] = value; break;
            case Diagonal:  diagonal[distance-2] = value; break;
            default: System.out.println("something weird happened"); break;
        }
    }

    public int stateToLinearIndex(Type type, int distance){
        switch (type){
            case Horizontal: return distance;
            case Vertical:  return distance + 6;
            case Diagonal:  return distance + 10;
            default: System.out.println("something weird happened"); break;
        }
        return -1;
    }

    public int linearIndexToState(int index){
        if(index < 6){
            return 10+index;
        }
        else if(index <12){
            return 20+index-6;
        }
        else if(index < 21){
            return 30+index-10;
        }
        return -1;
    }

}
