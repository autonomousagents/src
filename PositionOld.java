
public class PositionOld {
	private int x;
	private int y;
	
	public PositionOld(){
		this.x = 0;
		this.y = 0;
	}
	
	public PositionOld(PositionOld other){
		this.x = other.x;
		this.y = other.y;
	}
	
	public PositionOld(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public boolean equals(PositionOld other){
		return (this.x == other.getX() && this.y == other.getY()) ;
	}
        
    public int relativeDistance (PositionOld other){
        int temp = Math.abs(other.getX()-this.x);
        int horizontal = Math.min(temp, Environment.WIDTH-temp);
        temp = Math.abs(other.getY()-this.y);
        int vertical = Math.min(temp, Environment.HEIGHT-temp);
        return horizontal+vertical;
    }

//    public StateRepresentation.Type relativeAlignment(PositionOld other) {
//
//        if (other.x == this.x)
//            return StateRepresentation.Type.Horizontal;
//        else if (other.y == this.y)
//            return StateRepresentation.Type.Vertical;
//        else
//            return StateRepresentation.Type.Diagonal;
//    }

    /**
     * @param number of a position (index of row/column in VMatrix) in the non-efficient state representation
     * @return a Position object with correct x and y coordinates (fields), denoting the corresponding Cartesian position
     */
    public static PositionOld getPosition(int posNr) {

        int x = posNr % Environment.WIDTH;
        int y = (int)(  (posNr - x) / Environment.WIDTH);
        return new PositionOld(x, y);
    }

    /**
     * @param Position object with correct x and y coordinates (fields), denoting a Cartesian position
     * @return the corresponding number of that position (index of row/column in VMatrix) in the non-efficient state representation
     */
    public int getPosNr() {

        return this.getY() * Environment.WIDTH + this.getX();
    }



        


	
}
