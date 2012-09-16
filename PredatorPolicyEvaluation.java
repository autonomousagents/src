
public class PredatorPolicyEvaluation implements Agent {
													
	private final static double chancesPositions[] = {0.2 , 0.2, 0.2, 0.2, 0.2}; // up, right, down left, still
        private final static double chancePreyMoves= 0.2;
        private final static int nrMovesPrey =4;

	private double VMatrix[][];

        private double discountFactor=0.9;
	
	/**
	* @param index of state in VMatrix
	* @return Position with correct x and y coordinates (fields)
	*/
	Position getPosition(int nr) {

	    int x = nr % Environment.WIDTH;
	    int y = (nr - x) /  Environment.WIDTH;
	    return new Position(x, y);
	}


	/**
	* @param Position with correct x and y coordinates (fields)
	* @return index of state in VMatrix
	*/
	int getStateNr(Position state) { 

	    return state.getY() * Environment.WIDTH + state.getX();
	}

	
	// return array always length of chancesPositions.length
	// ORDER IS: // up, right, down left, still
	private double[] chancesPositionsPrey(int posPredator, int[] positionsPrey) {
                //init to default (not next to predator)
                double chanceArray[] = {chancePreyMoves/nrMovesPrey,chancePreyMoves/nrMovesPrey,chancePreyMoves/nrMovesPrey,chancePreyMoves/nrMovesPrey, // up, right, down, left
                                        1-chancePreyMoves/nrMovesPrey};// still

                // find in what direction prey will stand on  predator, if there is one
		int predatorEqualsIndex = -1;
		for(int i = 0;  i < chancesPositions.length && predatorEqualsIndex == -1 ; i++)
			if(positionsPrey[i] == posPredator) 
				predatorEqualsIndex = i;
                    
		 // prey was next to predator at that direction-index
		if (predatorEqualsIndex != -1) {  
                    for(int i = 0; i< chancesPositions.length-1; i++)
                         chanceArray[i] = chancePreyMoves / (nrMovesPrey-1);

                    chanceArray[predatorEqualsIndex]=0;
                }

		return chanceArray;
	}
	
	
	// --> deze is voor zowel prey als predator goed toch? - Agnes
	private int[] newPositions(int currentPos) {
		int[] positionArray = new int[chancesPositions.length];

		//up
		positionArray[0] = currentPos-Environment.WIDTH < 0?
								currentPos+(Environment.HEIGHT*Environment.WIDTH)-Environment.WIDTH:
								currentPos-Environment.WIDTH;
		//right
		positionArray[1] = (currentPos+1)%Environment.WIDTH == 0?
								currentPos - Environment.WIDTH:
								currentPos +1;
		//left
		positionArray[2] = currentPos == 0? 
								Environment.WIDTH : 
								(currentPos -1)% Environment.WIDTH == Environment.WIDTH-1? 
										currentPos + Environment.WIDTH -1: currentPos -1;
		//down
		positionArray[3] = currentPos+Environment.WIDTH>Environment.HEIGHT*Environment.WIDTH?
								currentPos-(Environment.HEIGHT*Environment.WIDTH)+Environment.WIDTH:
								currentPos+Environment.WIDTH;
		//still
		positionArray[4] = currentPos;
		
		return positionArray;
	}

        
	private double getPositionValue(int posPredator, int posPrey) {
		
		//int[] newPreyPositions = newPositionsPrey(posPredator, posPrey);
		int[] newPreyPositions = newPositions(posPrey);
		double[] chancesPreyPositions = chancesPositionsPrey(posPredator, newPreyPositions);
		
		double totalValue=0;
		for (int i=0; i < chancesPreyPositions.length; i++) {
			
			totalValue += chancesPreyPositions[i]  * // pi(s,a)
                                                // P^{a}_{s s'}  ontbreekt in deze setting
                                                (   Environment.reward(getPosition(newPreyPositions[i]), getPosition(posPredator)) + // R^{a}_{s s'}
                                                    discountFactor * VMatrix[ posPredator ] [ newPreyPositions[i] ] // gamma * V_k{s')
                                                );
			
		}
		return totalValue;
		
	}
	
	
	
	public double calculateValue(int posPredator, int posPrey) {
		
		double total =0;
		
		int[] possiblePositions = newPositions(posPredator);
		
		for (int i=0; i < chancesPositions.length; i++) {
			
			double TEMP  = chancesPositions[i] * getPositionValue( possiblePositions[i] , posPrey);
			
		}
		
		return (Double) null;
	}
	
	/**
	 * Start van policy evaluation
	 */
	public void start() {
		
		double maxDiff = 0.01;
		
		// zolang de grootste updatewaarden groter is dan maxDiff
			// voor elke positie in VMatrix
				// bereken state-waarde:
				// Vmatrix[i][j] = calculateValue(i,j);
		
	}
	
	public PredatorPolicyEvaluation(Position startPos) {
		 VMatrix = new double[Environment.HEIGHT*Environment.WIDTH][Environment.HEIGHT*Environment.WIDTH];
	}
	
	@Override
	public void doMove(Position other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Position getPos() {
		// TODO Auto-generated method stub
		return null;
	}

}
