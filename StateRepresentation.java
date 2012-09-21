
import java.util.Arrays;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * 
 */
public class StateRepresentation {
    private double stateRep[][];
    public static final int stateRepWidth = Math.round(Environment.WIDTH/2)+1;
    public static final int stateRepHeight = Math.round(Environment.HEIGHT/2)+1;
    
    public StateRepresentation ( ){
        stateRep = new double[stateRepHeight][stateRepWidth];
        Arrays.fill(stateRep, 0.0);
        fillUnused();
    }
    
    public enum Action {
    	HorizontalApproach,
    	HorizontalRetreat,
    	VerticalApproach,
    	VerticalRetreat;
    }
    
    public double getVvalue(int linearIndex){
    	Position pos = linearIndexToPosition(linearIndex);
    	return stateRep[pos.getY()][pos.getX()];
    }

    public int getLinearIndexForAction(int linearIndex, Action action){
    	Position pos = linearIndexToPosition(linearIndex);
    	if(linearIndex == 0)return 0;
    	switch(action){
    	case HorizontalApproach: 
    		if(pos.getY() == 0) return linearIndex +1;
    		else return linearIndex -1;
    	case HorizontalRetreat: 
    		Position tempPos = linearIndexToPosition(linearIndex+1);
    		if(tempPos.getY() == pos.getY()) return linearIndex+1;
    		else return linearIndex+pos.getX()+1;
    	VerticalApproach,
//    	VerticalRetreat;
    	}
    	return 0.0;
    }
    
    public double getReward(int linearIndex){
    	if(linearIndex == 0)return Environment.maximumReward;
    	else return Environment.minimumReward;
    }
    
    public int[] getRelDistance(Position predator, Position prey){
    	int [] relativeDistance = new int[2];
    	//horizontal Distance
    	relativeDistance[0] = Math.abs(prey.getX()-predator.getX());
    	if(relativeDistance[0] > (Environment.WIDTH)/2) relativeDistance[0] = Environment.WIDTH - relativeDistance[0];
    	//vertical Distance
    	relativeDistance[1] = Math.abs(prey.getY()-predator.getY());
    	if(relativeDistance[1] > (Environment.HEIGHT)/2) relativeDistance[1] = Environment.HEIGHT - relativeDistance[1];
    	return relativeDistance;
    }

    public static int relDistanceToLinearIndex(int x, int y){
    	if(x > y){ 
    		int i = x;
    		x = y;
    		y = i;
    	}
    	int sumOfY = 0;
    	for(int j = 1;j<=y;j++){
    		sumOfY += j;
    	}
    	int linearIndex = sumOfY + x;
    	return linearIndex;
    }
    
    public static Position linearIndexToPosition(int linearIndex){
    	int y = 0;
    	int oldSumY = 0;
    	int sumY = 0;
    	for(int j = 0;sumY<=linearIndex;j++){
    		y = j;
    		oldSumY = sumY;
    		sumY += j+1;
    	}
    	int x = linearIndex - oldSumY;
    	
    	return new Position(x, y);
    }
    
    private void fillUnused(){
    	for(int i = 0;i<stateRepHeight;i++){
    		for(int j = i+1;j<stateRepWidth;j++){
    			stateRep[i][j] =  -1.0;
    		}
    	}
    }

}
