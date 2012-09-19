import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class PredatorPolicyIteration {
	
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
	 }
	 
	 public void start() {
		 
		 
		 // while (!policyImprover.isFinished())
		 //		policyEvaluator.setPolicy(policyMatrix)
		//		policyEvaluator.start();
		 //		VMatrix = policyEvaluator.getvMatrix();
		 //		policyImprover.setVMatrix(VMatrix);
		 // 	policyImprover.start();
		 // 	policyMatrix = policyImprover.getPolicy();
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
	 
	 
	

    
    
	 
	 
	 
}
	 

