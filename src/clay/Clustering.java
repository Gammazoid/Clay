package clay;




import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Clustering {
	
	
	
	
	//the set always has for element zero a point with value: P = (end, threshold );
	
	/**Add points to the array of point uder the condition that they have closestneighbor and their color distance is smaller than the threshold
	 * 
	 * @param left the writable image of the leftview (the original image)
	 * @param right the writable image of the rightview (the image of all the set)
	 * @param set the targetted set to be generated
	 * @param threshold the value defining the recursive definition defining the set
	 */
	public static void expand(WritableImage left , WritableImage right , Point[] set , double threshold) {
		
		double distanceXY=1;
		int start = 1;
		int end = (int)set[0].getX();
		set[0].setPrevEnd( (int)(threshold-1) , end);   

				
		PixelReader leftReader = left.getPixelReader();
		PixelWriter leftWriter = left.getPixelWriter();
		PixelWriter rightWriter = right.getPixelWriter();
		
		//generate the set recursively
		while(start<end) {
			
			int x=(int)set[start].getX();
			int y=(int)set[start].getY();										
		
			
			
			for(double i=-distanceXY;i<=distanceXY;i++) {
				for(double j=-distanceXY;j<=distanceXY;j++) {
																											// The condition for adding a point: closest neighbor AND distance color< threshold
					if( ( (i+x)>=0  &&  (i+x)<left.getWidth()  &&  j+y>=0  &&  j+y<left.getHeight())  &&  !(i==0  && j==0)  &&  leftReader.getColor((int)(x+i),(int)(y+j))!=Color.WHITE  &&  Point.dist(  new Point(new java.awt.Color((int)set[start].getZ())) , new Point(new java.awt.Color( leftReader.getColor((int)(x+i), (int)(y+j)).hashCode() ))  )<Math.sqrt(threshold)) {
						set[end]=new Point( x+i , y+j , leftReader.getColor( (int)(x+i) , (int)(y+j) ).hashCode() );   //put the color on the left into the point[] array
						rightWriter.setColor( (int)(x+i),(int)(y+j), leftReader.getColor((int)(x+i),(int)(y+j)) );		// add it to the image right
						leftWriter.setColor((int)(x+i),(int)(y+j), Color.WHITE );			// remove it from the left
						end++; 														
					}

				}
			}
			
			start++;
			
			
		}
		
	
		set[0].setX(end);
		set[0].setY(threshold);						

		
	}//END  expand
	
	
	
	
	
	
	
	
	
	/*Instead of computing the smaller set, the end point of different value of the threshold is saved in an array, when needed, the array is consulted and points removed accordingly
	 * 
	 */

	
	/**Remove points from the set to get the set defined by the previous threshold
	 * 
	 * @param left the writable image of the leftview (the original image)
	 * @param right the writable image of the rightview (the image of all the set)
	 * @param set the targetted set to be degenerated
	 * @param threshold the value defining the recursive definition defining the set
	 */
	public static void contract(WritableImage left , WritableImage right , Point[] set , double threshold) {
		
		
		int newEnd = set[0].getPrevEnd( (int)threshold );   
		int currentEnd = (int)set[0].getX();										
		
		PixelReader rightReader = right.getPixelReader();
		PixelWriter leftWriter = left.getPixelWriter();
		PixelWriter rightWriter = right.getPixelWriter();
		
		
		
		
		for(int i=newEnd;i<currentEnd;i++) {
			
		
			Color c = rightReader.getColor( (int)set[i].getX() , (int)set[i].getY() );
			leftWriter.setColor( (int)set[i].getX() , (int)set[i].getY() , c );
			rightWriter.setColor( (int)set[i].getX() , (int)set[i].getY() , Color.WHITE );
			set[i]=null;
			
	
		}

		set[0].setX(newEnd);
	}//END contract
	
	
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}