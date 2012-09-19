import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class PredatorPolicyImprovement {
	
	private double VMatrix[][];
    private double policyMatrix[][][];
    private boolean policyStable ;
    
    public PredatorPolicyImprovement(double p[][][]) {
    	policyMatrix = p;
    }
    
//    private Direction bestActionFromRow(double chancesActions[]) {
//    	
//    	double bestValue=Environment.minimumReward;
//    	Direction bestAction = Direction.enumValues[4];
//    	
//    	for(int i=0; i < Direction.nrMoves; i++) {
//    		if (chancesActions[i] > bestValue) {
//    			bestValue = chancesActions[i];
//    			bestAction = Direction.enumValues[i];
//    		}
//    	}
//    	return bestAction;	
//    }
    
  
    
    public void start() {
    	policyStable = false;
    	
    	for (int posNrPredator = 0; posNrPredator < Environment.HEIGHT *  Environment.WIDTH ; posNrPredator++) {
            for (int posNrPrey = 0; posNrPrey < Environment.HEIGHT *  Environment.WIDTH ; posNrPrey++) {
            	
            	double policyActions[] = policyMatrix[posNrPredator][posNrPrey];
            	Direction b = bestActionFromRow(policyActions);
            	
            }
    	}
    	
    }
    
    public boolean isFinished() {
    	return policyStable;    	
    }
    
    public void setVMatrix(double[][] v) {
    	VMatrix=v;
    }
    
    public double[][][] getPolicy() {
    	return policyMatrix;    	
    }
    
    public void writePolicyMatrix(String filename)  {
    	/*try
    	{
            FileWriter fstream = new FileWriter(filename,false);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("clear;clc;");
            out.newLine();
            for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
                    out.write(String.format("C(%d,:)=[",i+1));
                for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
                    if (j != 0)
                            out.write(",");

                    String number;
                    if (i == j) // Set reward at the goal states in the "VMatrix" in the script. Makes a more intuitive colormap.
                        number = String.format("%.3f", Environment.reward(getPosition(i), getPosition(j))); 
                    else
                        number = String.format("%.3f", VMatrix[i][j]);
                   
                    out.write(number.replaceAll(",", "."));
                }
                out.write("];");
                out.newLine();
                out.flush();
            }
            out.write("imagesc(C, [ min(min(C)), max(max(C)) ] );");
            out.write("colormap(gray);");
            out.write("axis image");
            out.flush();
            fstream.close();
            out.close();
    	}
    	catch(IOException e)
    	{
    		System.out.println("Error in writeVMatrix(): " + e);
    	}*/
    }

}
