
import java.util.ArrayList;
import java.util.Arrays;


public class PredatorPolicyImprovement extends PolicyIterationPart {
	
	private double VMatrix[][];
    private double policyMatrix[][][];
    private boolean policyStable ;


    public static final double doubleEpsilon = 0.00001; // to compare double values with some tolerance



    public PredatorPolicyImprovement(double p[][][]) {
    	policyMatrix = p;
        VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];
    }


    private double[] bestActionRow(int posNrPredator, int posNrPrey) {
    	
    	double bestValue = Environment.minimumReward;
    	ArrayList<Direction> bestActions = new ArrayList<Direction>();
        double  bestActionRow[] = new double[Direction.nrMoves] ;
        Arrays.fill(bestActionRow,0);
        int[] possiblePositionsNumbers = newPositionsNumbers(posNrPredator);

    	for(int i=0; i < Direction.nrMoves; i++) {

            super.setVMatrix(VMatrix);
            
    		double thisActionValue = getPositionValue(possiblePositionsNumbers[i],posNrPrey);

            if (thisActionValue > bestValue) {
                bestValue = thisActionValue;
                bestActions.clear();
                bestActions.add(Direction.enumValues[i]);
            }
            else if (Math.abs(thisActionValue - bestValue) < doubleEpsilon) {
               bestActions.add(Direction.enumValues[i]);
            }
    	}
        int nrBestActions = bestActions.size();
        for (int i=0; i < nrBestActions; i++) { // fill with probabilities
            bestActionRow[bestActions.get(i).intValue] = 1.0 / nrBestActions;
        }
    	return bestActionRow;
    }

    public boolean samePolicies(double policyOne[], double policyTwo[]) {

        for (int i=0; i < Direction.nrMoves; i++) {
            
            if (Math.abs(policyOne[i] - policyTwo[i]) > doubleEpsilon) { // check if they have not same probabilities
                //System.out.println(policyOne[i] +  "  vs " + policyTwo[i]);
              
                return false;
            }
        }
        return true;
    }
  
    
    public void start() {
    	policyStable = true;
    	

    	for (int posNrPredator = 0; posNrPredator < Environment.HEIGHT *  Environment.WIDTH ; posNrPredator++) {
            for (int posNrPrey = 0; posNrPrey < Environment.HEIGHT *  Environment.WIDTH ; posNrPrey++) {

                if (posNrPredator != posNrPrey) {
                    double givenPolicyActions[] =  Arrays.copyOf(policyMatrix[posNrPredator][posNrPrey], Direction.nrMoves);

                    double currentBestActions[] = bestActionRow(posNrPredator, posNrPrey);
                     for (int k=0; k < Direction.nrMoves; k++)
                            policyMatrix[posNrPredator][posNrPrey][k] = currentBestActions[k];


                    if (!samePolicies(givenPolicyActions, currentBestActions)) {
                        policyStable = false;
                       // System.out.println("state for positions [" + getPosition(posNrPredator).getX()+","+ getPosition(posNrPredator).getY() +
                         //           "] and [" + getPosition(posNrPrey).getX()+","+ getPosition(posNrPrey).getY() + "]differ in policy");

                    }
                }
            }
    	}
        if (!policyStable)
            System.out.println("Policy improver says: Policy not stable yet");
    	
    }
    
    public boolean isFinished() {
    	return policyStable;    	
    }
    
    @Override
    public void setVMatrix(double[][] v) {
    	for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++)
                VMatrix[i][j]=v[i][j];
        }
    }
    
    public double[][][] getPolicy() {
    	return policyMatrix;    	
    }
    
//    public void writePolicyMatrix(String filename)  {
//    	try
//    	{
//            FileWriter fstream = new FileWriter(filename,false);
//            BufferedWriter out = new BufferedWriter(fstream);
//
//            out.write("clear;clc;");
//            out.newLine();
//            for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
//                for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
//                    for (int k=0; k < Direction.nrMoves; k++) {
//                         out.write(String.format("P(%d,%d,:)=[",i+1,j+1));
//                         if (k != 0)
//                            out.write(",");
//
//                        String number;
//                        number = String.format("%.3f", policyMatrix[i][j][k]);
//                        out.write(number.replaceAll(",", "."));
//                    }
//                }
//                out.write("];");
//                out.newLine();
//                out.flush();
//            }
//            out.write("imagesc(C, [ min(min(C)), max(max(C)) ] );");
//            out.write("colormap(gray);");
//            out.write("axis image");
//            out.flush();
//            fstream.close();
//            out.close();
//    	}
//    	catch(IOException e)
//    	{
//    		System.out.println("Error in writeVMatrix(): " + e);
//    	}
//    }

}
