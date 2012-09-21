/**
 * Master AI UvA 2012/2013
 * Autonomous Agents
 * Assignment 1
 *
 * @authors Group 7: Agnes van Belle, Maaike Fleuren, Norbert Heijne, Lydia Mennes
 */

import java.util.Arrays;

public class PredatorValueIterationNewRep implements Agent{

    //variables new state space repr
    private StateRepresentation v;
    private StateRepresentation vNew;
    private final static double WAIT = 0.2;
    Position myPos;
    Position startPosition;
    double theta, gamma;

    /**
     * Constructor initializes all variables
     * @param startPos = start position of the agent
     */
    public PredatorValueIterationNewRep(Position startPos){
        myPos = startPos;
        startPosition=startPos;
        setParams();
        v = new StateRepresentation();
        calcV();
        // print table
        v.printLatexTable();
    }

    /**
     * Calculates the V-values for all states
     */
    private void calcV() {

        double delta ;
        double[] vPerAction= new double [StateRepresentation.nrActions];
        int nrIterations = 0;
        do {
            delta = 0.0;
            vNew = new StateRepresentation();
            //for each state
            for(int i = 0; i<StateRepresentation.nrStates;i++){
                if(i != 0) {
                    Arrays.fill(vPerAction,0);
                    //calculate v-value for each action
                    vPerAction = calcVPerAction(i);
                    //determine maximal action and replace v
                    vNew.setVvalue(i, getMaximum(vPerAction));
                    double difference = Math.abs(vNew.getVvalue(i)-v.getVvalue(i));
                    if(difference>delta){
                        delta = difference;
                    }
                } else {
                	vNew.setVvalue(i, 0.0);
                }
            }
            v = vNew;
            nrIterations++;
        } while(delta>theta);
        System.out.println("nr iterations until convergence = "+nrIterations);
    }

    /**
     * Calculates the v-value for each reachable state s' from state s
     * @param state = index of state s
     * @return v-value for each reachable state s'
     */
    private double[] calcVPerAction(int state) {
        double [] vPerAction = new double[StateRepresentation.nrActions];
        int [] actionsPred = getPostions(state);
        int [] actionsPrey;
        double [] pResultingStates;
        //v k+1 per action
        double vkP1;
        // for each resulting position of possible actions of the predator
        for(int i = 0; i<StateRepresentation.nrActions;i++){
            vkP1 = 0.0;
            //for each possible action of the prey determining s'
            actionsPrey = getPostions(actionsPred[i]);
            pResultingStates = probabilities(actionsPred[i],actionsPrey);
            for(int j = 0; j<StateRepresentation.nrActions;j++){
                //V k(s')
                double vk = v.getVvalue(actionsPrey[j]);
                // calculate v k+1 (s) per action
                vkP1+= pResultingStates[j]*(v.getReward(actionsPrey[j])+ gamma*vk);
            }
            vPerAction[i] = vkP1;
        }
        return vPerAction;
    }

    /**
     * Calculates the probability of ending up in state s'(depending on prey) 
     * given a resulting state from an action
     * @param predator = index of state in which the predator is 
     * @param prey = possible states in which the prey is
     * @return list of probabilities for each state s'
     */
    private double [] probabilities (int predator, int [] prey){
        double [] probabilities = {0.05,0.05,0.05,0.05,0.8};
        int onPredator = -1;
        //check for each action if the prey steps on the predator
        for(int i = 0;i<StateRepresentation.nrActions-1;i++){
            //if the prey steps on the predator set the probability of that state to zero
            if(prey[i]==0){
                onPredator = i;
                probabilities[i]=0.0;
            }
        }
        //If necessary adjust the resulting probabilities
        if(onPredator != -1){
            for(int i = 0;i<StateRepresentation.nrActions-1;i++){
                if(i!=onPredator)
                    probabilities[i] = 0.2/3.0;
            }
        }
        if(predator == 0){
            double[]pr = {0.0,0.0,0.0,0.0,1.0};
            probabilities = pr;
        }
        return probabilities;
    }

    /**
     * Results in linear indexes for the states which are reachable from state s
     * @param state = linear index of state s
     * @return list of linear indexes of possible states s'
     */
    private int [] getPostions(int state){
    	int[] pos = new int [StateRepresentation.nrActions];
    	
        //Vertical approach
        pos[0] =  v.getLinearIndexForAction(state, StateRepresentation.Action.HorizontalApproach);
        //Horizontal approach
        pos[1] =  v.getLinearIndexForAction(state, StateRepresentation.Action.HorizontalRetreat);
        //Vertical retreat
        pos[2] =  v.getLinearIndexForAction(state, StateRepresentation.Action.VerticalApproach);
        //Horizontal retreat
        pos[3] =  v.getLinearIndexForAction(state, StateRepresentation.Action.VerticalRetreat);
        //Wait
        pos[4] =  v.getLinearIndexForAction(state, StateRepresentation.Action.Wait);
        
        return pos;
    }
    
    

    /**
     * Determines best action in the state space and
     * Translates this action from State space representation to 
     * a move in the actual world.
     */
    public void doMove(Position other) {
    	//Determine best move
        int [] relDistance = v.getRelDistance(myPos, other);
        int linIndex = v.relDistanceToLinearIndex(relDistance[0], relDistance[1]);
        double [] vValues = new double[StateRepresentation.nrActions];
        int [] newStates = new int [StateRepresentation.nrActions];
        newStates[0] = v.getLinearIndexForAction(linIndex, StateRepresentation.Action.HorizontalApproach);
        newStates[1] = v.getLinearIndexForAction(linIndex, StateRepresentation.Action.HorizontalRetreat);
        newStates[2] = v.getLinearIndexForAction(linIndex, StateRepresentation.Action.VerticalApproach);
        newStates[3] = v.getLinearIndexForAction(linIndex, StateRepresentation.Action.VerticalRetreat);
        newStates[4] = v.getLinearIndexForAction(linIndex, StateRepresentation.Action.Wait);
        for(int i = 0;i<StateRepresentation.nrActions;i++){
        	vValues[i] = v.getVvalue(newStates[i]);
        }
        int maxIndex = -1;
        double maxV = -1.0;
        for(int i = 0; i<StateRepresentation.nrActions;i++){
        	if (maxV <= vValues[i]){
        		maxIndex=i;
        	}
        }
        //determine realworldmoves
        Position [] moves = getPostions(myPos);
        if(maxIndex != 4){
        	//Translate to realwordlmove
        	myPos=moves[getMove(other, maxIndex)];
        }        
    }
    
    

    //Determines positions corresponding to real world moves 
	private Position [] getPostions(Position p){
        Position[] pos  = new Position[5];
        //Up
        pos[0] = new Position(p.getX(), (p.getY()+Environment.HEIGHT-1)% Environment.HEIGHT);
        //Right
        pos[1] = new Position((p.getX() +Environment.WIDTH+1) % Environment.WIDTH, p.getY());
        //Down
        pos[2] = new Position(p.getX(), (p.getY()+Environment.HEIGHT+1)% Environment.HEIGHT);
        //Left
        pos[3] = new Position((p.getX() +Environment.WIDTH-1) % Environment.WIDTH, p.getY());
        //Wait
        pos[4] = new Position(p.getX(),p.getY());
        return pos;
    }

    @Override
    public Position getPos() {
        return myPos;
    }

    public void restart(){
        myPos = startPosition;
    }

    public void setParams(double theta, double gamma){
            this.theta=theta;
            this.gamma = gamma;
    }

    public void setParams(){
            this.theta= 0.0;
            this.gamma = 0.8;
    }

    private double getMaximum(double [] values){
        double max = values[0];
        for(int i = 1; i<values.length;i++){
            if (values[i]>max){
                max = values[i];
            }
        }
        return max;
    } 
    
    //Determines in which wind direction the prey is located
    private Direction getDirection(Position other){
    	if(other.getX()>=myPos.getX()){
    		if(other.getY()>=myPos.getY()){
    			if(myPos.getX()>=myPos.getY()){
    				return Direction.NNW;
    			}
    			else{
    				return Direction.WNW;
    			}
    		}
    		else{
    			if(Environment.WIDTH-myPos.getX()>myPos.getY()){
    				return Direction.WZW;
    			}
    			else{
    				return Direction.ZZW;
    			}
    		}
    	}
    	else{
    		if(other.getY()>=myPos.getY()){
    			if(Environment.HEIGHT-myPos.getY()<myPos.getX()){
    				return Direction.ONO;
    			}
    			else{
    				return Direction.NNO;
    			}
    		}
    		else{
    			if(myPos.getY()>myPos.getX()){
    				return Direction.ZZO;
    			}
    			else{
    				return Direction.OZO;
    			}
    		}
    	}
    }

    /**
     * Provides real world move based on action in state representation and position of the prey
     * @param other = position of prey
     * @param stateRepMove = action in state space
     * @return action in real world
     */
    private int getMove(Position other, int stateRepMove) {
		Direction direction = getDirection(other);
		switch(direction){
		case NNW:
			
			switch(stateRepMove){
			case 0:return 1;
			case 1:return 3;
			case 2:return 2; 
			case 3:return 0;
			}
		case NNO:
			switch(stateRepMove){
			case 0:return 3;
			case 1:return 1;
			case 2:return 2;
			case 3:return 0;
			}
		case ONO:
			switch(stateRepMove){
			case 0:return 2;
			case 1:return 0;
			case 2:return 3;
			case 3:return 1;
			}
		case OZO:
			switch(stateRepMove){
			case 0:return 0;
			case 1:return 2;
			case 2:return 3;
			case 3:return 1;
			}
		case ZZO:
			switch(stateRepMove){
			case 0:return 3;
			case 1:return 1;
			case 2:return 0;
			case 3:return 2;
			}
		case ZZW:
			switch(stateRepMove){
			case 0:return 1;
			case 1:return 3;
			case 2:return 0;
			case 3:return 2;
			}
		case WZW:
			switch(stateRepMove){
			case 0:return 0;
			case 1:return 2;
			case 2:return 1;
			case 3:return 3;
			}
		case WNW:
			switch(stateRepMove){
			case 0:return 2;
			case 1:return 0;
			case 2:return 1;
			case 3:return 3;
			}
		}
		return -1;
	}



}
