/**
 * Master AI UvA 2012/2013
 * Autonomous Agents
 * Assignment 1
 *
 * @authors Group 7: Agnes van Belle, Maaike Fleuren, Norbert Heijne, Lydia Mennes
 */

import java.util.Arrays;

public class StateRepresentationOld {
    private double [] horizontal;
    private double [] vertical;
    private double [] diagonal;

    
    public StateRepresentationOld (){
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

//<<<<<<< HEAD
//   
//    
//=======
    public enum Type {
	     Horizontal(0), 
	     Vertical(1), 
	     Diagonal(2);
	     
	     private final int index;   

	     Type(int index) {
	         this.index = index;
	     }

	     public int index() { 
	         return index; 
	     }
    }
//>>>>>>> Verder gewerkt aan State Representation

    public void setVvalue(Type type, int distance, double value){
        switch (type){
            case Horizontal: horizontal[distance] = value; break;
            case Vertical:  vertical[distance] = value; break;
            case Diagonal:  diagonal[distance-2] = value; break;
            default: System.out.println("something weird happened"); break;
        }
    }

    public static int stateToLinearIndex(Type type, int distance){
        switch (type){
            case Horizontal: return distance;
            case Vertical:  return distance + 6;
            case Diagonal:  return distance + 10;
            default: System.out.println("something weird happened"); break;
        }
        return -1;
    }

    public static int[] linearIndexToState(int index){
        if(index < 6) {
        	int[] state = {Type.Horizontal.index(), index};
            return state;
        }
        else if(index <12) {
        	int[] state = {Type.Vertical.index(), index-6};
            return state;
        }
        else if(index < 21){
        	int[] state = {Type.Diagonal.index(), index-10};
            return state;
        }
        return null;
    }

}
