
public class Position {
	private int x;
	private int y;
	
	public Position(){
		this.x = 0;
		this.y = 0;
	}
	
	public Position(Position other){
		this.x = other.x;
		this.y = other.y;
	}
	
	public Position(int x, int y){
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
	
	public boolean equals(Position other){
		return (this.x == other.getX() && this.y == other.getY()) ;
	}
        
        public int relativeDistance (Position other){            
            int temp = Math.abs(other.getX()-this.x);
            int horizontal = Math.min(temp, Environment.WIDTH-temp);
            temp = Math.abs(other.getY()-this.y);
            int vertical = Math.min(temp, Environment.HEIGHT-temp);
            return horizontal+vertical;
        }

        


	
}
