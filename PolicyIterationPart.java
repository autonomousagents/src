/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public  class PolicyIterationPart {

    private double VMatrix[][];

     public final static double chancePreyMoves = 0.2;
    public final static double nrMovesPrey = 4;
    public final static double discountFactor = 0.1;

    public double[] chancesPositionsPrey(int posPredator, int posPrey, int[] positionsPrey) {

    	if(posPrey == posPredator) // then chance of standing still = 1
    		return new double[]{0,0,0,0,1}; // up, right, down, left, still

    	else {
	    	//init to default (not next to predator)
	        double[] chanceArray = {chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, // up, right, down, left
                                    1 - chancePreyMoves};// still
	        // find in what direction prey will stand on  predator, if there is one
	        int predatorEqualsIndex = -1;
	        for (int i = 0; i < Direction.nrMoves && predatorEqualsIndex == -1; i++) {
	            if (positionsPrey[i] == posPredator)
	                predatorEqualsIndex = i;
	        }
	        // if prey was next to predator at that direction-index
	        if (predatorEqualsIndex != -1) {
	            for (int i = 0; i < Direction.nrMoves - 1; i++)
	                chanceArray[i] = chancePreyMoves / (nrMovesPrey - 1);
	            chanceArray[predatorEqualsIndex] = 0;
	        }
	        return chanceArray;
    	}
    }

    public int[] newPositionsNumbers(int currentPosNr) {

    	Position currentPos = Position.getPosition(currentPosNr); // convert position-number to Position object
    	int[] positionNumbersArray = new int[Direction.nrMoves];
    	Position[] positionsArray = new Position[Direction.nrMoves];
    	for (int i=0; i < positionsArray.length; i++)
    		positionsArray[i]= new Position(currentPos);

    	// up
    	positionsArray[0].setY(currentPos.getY() == 0 ? Environment.HEIGHT-1 : currentPos.getY()-1);
    	//right
    	positionsArray[1].setX(currentPos.getX() == Environment.WIDTH-1 ? 0 : currentPos.getX()+1);
    	// down
    	positionsArray[2].setY(currentPos.getY() == Environment.HEIGHT-1 ? 0 : currentPos.getY()+1);
    	//left
    	positionsArray[3].setX(currentPos.getX() == 0? Environment.WIDTH-1 : currentPos.getX()-1);

		for (int i=0; i< positionsArray.length; i++) // re-convert found Position objects to position-numbers
			positionNumbersArray[i]=positionsArray[i].getPosNr();

		return positionNumbersArray;
    }

    public double getPositionValue(int posNrPredator, int posNrPrey) {

        int[] newPreyPositions = newPositionsNumbers(posNrPrey);
        double[] chancesPreyPositions = chancesPositionsPrey(posNrPredator, posNrPrey, newPreyPositions);

        double totalValue = 0;

        
        for (int i = 0; i < chancesPreyPositions.length; i++) {

            totalValue +=   chancesPreyPositions[i] * // P^{a}_{s s'}
                            (	Environment.reward(Position.getPosition(newPreyPositions[i]), Position.getPosition(posNrPredator))  // R^{a}_{s s'}
                                + (discountFactor * VMatrix[posNrPredator][newPreyPositions[i]]) // gamma * V_k{s')
                            );
        }
        return totalValue;
    }

    public void setVMatrix(double v[][]) {
        VMatrix=v;
    }
}
