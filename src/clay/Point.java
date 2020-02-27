package clay;

import java.awt.Color;


public class Point {
	
	private double x,y,z,w;
	private int[] prevEnd= new int[200];
	
	/**Define a new point in 2D
	 * 
	 * @param x position in the x-axis
	 * @param y position in the y-axis
	 */
	public Point(double x, double y) {
		
		this.x=x;
		this.y=y;
		this.z=0;
	}
	
	/**Define a new point in 3D
	 * 
	 * @param x position in the x-axis
	 * @param y position in the y-axis
	 * @param z position in the z-axis
	 */
	public Point(double x, double y, double z) {
		
		this.x=x;
		this.y=y;
		this.z=z;
		
	}
	
	/**Define a new point in 3D with an associated array
	 * 
	 * @param x position in the x-axis
	 * @param y position in the y-axis
	 * @param z position in the z-axis
	 * @param prevEnd the array of location of the last nonnull Point in an array of point
	 */
	public Point(double x, double y, double z, int[] prevEnd) {
		
		this.x=x;
		this.y=y;
		this.z=z;
		this.prevEnd=prevEnd;
		
	}
	

	/**Define a new point in RGB space from a color
	 * 
	 * @param clr a color to be turned into a 3D point
	 */	
	public Point(Color clr) {
		
		this.x=clr.getRed();
		this.y=clr.getGreen();
		this.z=clr.getBlue();
		
	}
	
	/**Define a new point in RGB space from a color with an associated array
	 * 
	 * @param clr a color to be turned into a 3D point
	 * @param prevEnd the array of location of the last nonnull Point in an array of point
	 */	
	public Point(Color clr , int[] prevEnd) {
		
		this.x=clr.getRed();
		this.y=clr.getGreen();
		this.z=clr.getBlue();
		this.prevEnd=prevEnd;
	}
	
	
	//************************************SETTERS*************************************SETTERS*******************************
	
	/**Set the x-axis
	 * 
	 * @param x a location in the x-axis
	 */
	public void setX(double x){
		this.x=x;
	}
	
	/**Set the y-axis
	 * 
	 * @param y a location in the y-axis
	 */
	public void setY(double y){
		this.y=y;
	}
	
	/**Set the z-axis
	 * 
	 * @param z a location in the z-axis
	 */
	public void setZ(double z) {
		this.z=z;
	}
	
	/**Set an additional value W to the point, it will not be used when calculating the distane between 2 points
	 * 
	 * @param w a free variable for any uses
	 */
	public void setW(double w) {
		this.w=w;
	}
	
	
	public void setPrevEnd(int index , int end) {
		if(index<prevEnd.length) {
			this.prevEnd[index]=end;
		}
	}
	public void setPrevEnd(int[] prevEndArr) {
		this.prevEnd=prevEndArr;
	}
	
	//************************************GETTERS***************************GETTERS********************************
	
	public double getX(){
		return this.x;
	}
	public double getY(){
		return this.y;
	}
	public double getZ() {
		return this.z;
	}
	public double getW() {
		return this.w;
	}
	public int getPrevEnd( int index ) {
		return this.prevEnd[index];
	}
	public int[] getPrevEnd() {
		return this.prevEnd;
	}
	
	//   *************************************  ADDITIONAL METHOD  ************************************************************
	
	/**Define the distance in a 3D
	 * 	
	 * @param A Point A
	 * @param B Point B
	 * @return the distance between point A and B
	 */
	public static double dist(Point A, Point B) {
		return Math.sqrt(Math.pow((A.getX()-B.getX()),2)+Math.pow((A.getY()-B.getY()), 2)+Math.pow((A.getZ()-B.getZ()),2));
	}
		
		
	/**Define the distance in a 2D plane between points that could be in 3D
	 * 	
	 * @param A Point A
	 * @param B Point B
	 * @return the distance between point A and B in 2d
	 */
	public static double distXY(Point A, Point B) {
		return Math.sqrt( Math.pow((A.getX()-B.getX()),2)+Math.pow((A.getY()-B.getY()), 2) );
	}
		
		
		
	/**Define the angle with the x-axis of the vector defined by point AB
	 * 
	 * @param A
	 * @param B
	 * @return the angle between two points with the x-axis 
	 */
	public static double angle(Point A, Point B) {
		
		Point pt = new Point( B.getX()-A.getX() , B.getY()-A.getY() );
		double theta = Math.atan( pt.getY()/pt.getX() );
		
		return theta*180/Math.PI;
	}
		
		
		
		
	
	
	
	
	
	
	
	/**The distance of the this point from the 0-coordinate ( Think of it as the norm of a vector)
	 * 
	 * @return the norm of a vector
	 */
	public double length() {
		return Math.sqrt(this.x*this.x+this.y+this.y);
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * @return a string made of the location of the points in 3D
	 */
	public String show(){
		return ""+this.x+" "+this.y+" "+this.z+"";	
	}

}
