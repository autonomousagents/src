import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class PredatorPolicyIteration implements Agent {
	
	 private double VMatrix[][];
	 private double policyMatrix[][][]; // predatorstates,preystates,probabilities per actie 
	 
	 private PredatorPolicyEvaluation policyEvaluator; 
	 private PredatorPolicyImprovement policyImprover;
	 
	 public PredatorPolicyIteration() {		
		 
		 policyMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH]
		                                                                   [Direction.nrMoves];
		 VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];
		 
		 initMatricesRandom();
		 
		 policyEvaluator = new PredatorPolicyEvaluation(VMatrix);
		 policyImprover = new PredatorPolicyImprovement(policyMatrix);

         start();
	 }
	 
	 public void start() {

         int iter=0;

        do {
            iter++;
            policyEvaluator.setPolicy(policyMatrix);
            policyEvaluator.start();

            // we got the values, now give them to improver
            setVMatrix(policyEvaluator.getVMatrix());
            policyImprover.setVMatrix(VMatrix);

            // start improving
            policyImprover.start();
            setPolicy( policyImprover.getPolicy());

            System.out.println(iter);
         } while (!policyImprover.isFinished());
	 }
	 
	 private void initMatricesRandom() {
		 
		 Random rand = new Random();
		 
		 for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++)  {
	        	for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
	        	
	        			// policy
	        			double randomArray[]=new double[Direction.nrMoves];
	        			randomArray[0] = rand.nextDouble();
	        			randomArray[1]= (1-randomArray[0])*rand.nextDouble();
	        			randomArray[2] = (1-randomArray[0]-randomArray[1])*rand.nextDouble();
	        			randomArray[3] = (1-randomArray[0]-randomArray[1]-randomArray[2])*rand.nextDouble();
	        			randomArray[4] = (1-randomArray[0]-randomArray[1]-randomArray[2]-randomArray[3]);
	        			
	        			Collections.shuffle(Arrays.asList(randomArray));
	        			policyMatrix[i][j] = randomArray;
	        			
	        			// VMatrix   
	        			VMatrix[i][j] = Environment.minimumReward + rand.nextDouble()*Environment.maximumReward;
	        		
	        	}
		 }
		 
		 
	 }

     public void setPolicy(double p[][][]) {
    	for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
                for (int k=0; k < Direction.nrMoves; k++)
                    policyMatrix[i][j][k] = p[i][j][k];
            }
        }
    }

     public void setVMatrix(double[][] v) {
    	for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++)
                VMatrix[i][j]=v[i][j];
        }
    }

    public void doMove(Position other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Position getPos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	 
	 
	

    
    
	 
	 
	 
}
	 

