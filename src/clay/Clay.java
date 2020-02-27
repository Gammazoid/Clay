package clay;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Clay extends Application implements EventHandler<MouseEvent>{

	
	//Declare those variable that needs global access
	private WritableImage imgOrigin = null;
	private WritableImage imgRight = null;
	private WritableImage imgLeft = null;
	private ArrayList listSet = new ArrayList();
	private Point[] currentSet;
	//private Point[] glowingEdge;
	private ToggleButton cut;
	private ToggleButton selectPoint;
	private Label thresholdLabel;
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
	
	

	@Override
	public void start(Stage primary) throws Exception {
		// TODO Auto-generated method stub
		primary.setWidth(1100); primary.setHeight(600); //primary.setFullScreen(true);  
			
		
		
		
	// A. Initialize principal components
		
		VBox root = new VBox();
			MenuBar menu=new MenuBar();
			HBox tripartie=new HBox();
				HBox right = new HBox();	
				HBox left = new HBox();
				VBox middle = new VBox();	
			GridPane toolGrid = new GridPane();
		
		
	
		
	// B. Define the tripartie : where the images are displayed + and the button for set transferal
		
		// B1. Define left view
			
			
		ImageView currentLeftView = new ImageView();
		left.getChildren().add(currentLeftView);
		currentLeftView.setOnMouseClicked(this);
		//currentLeftView.setOnMousePressed(this);
		left.setPrefWidth( primary.getWidth()/2-50 );
			
		
		// B2. Define middle
			
		
		FontAwesomeIconView faLeft = new FontAwesomeIconView(FontAwesomeIcon.ARROW_LEFT);
		FontAwesomeIconView faRight = new FontAwesomeIconView(FontAwesomeIcon.ARROW_RIGHT);
		Button moveLeft = new Button("" , faLeft );
		middle.setAlignment(Pos.CENTER);
		moveLeft.setOnAction( e->removeSet() );
		Button moveRight = new Button("" , faRight );
		middle.getChildren().addAll( moveLeft /*, moveRight*/);
		middle.setPrefWidth(50);
			
		
		// B3. Define right view
			
		
		ImageView currentRightView = new ImageView();
		currentRightView.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        currentSet = findClosestSet(new Point(event.getX() , event.getY() ));
		    }
		});
		right.getChildren().add(currentRightView);
		right.setPrefWidth( primary.getWidth()/2-50 );
		
		
		// B4. add separator
		
		Separator sep1 = new Separator(Orientation.VERTICAL);
		Separator sep2 = new Separator(Orientation.VERTICAL);
		tripartie.setPrefHeight(primary.getHeight());
		tripartie.getChildren().addAll (left , sep1 , middle , sep2 ,right );
		
		
		
	// C. Define the menubar
		
		Menu prjct=new Menu("Project");			//a menu is a single vertical selector, each option in the bar requires a menu object
		menu.getMenus().add(prjct);
		
		
		// C1. add menu option
		MenuItem load=new MenuItem("Load Image");
		MenuItem save=new MenuItem("Save Image");
		MenuItem reset = new MenuItem("Reset");
		load.setOnAction( e -> loadingAction(currentLeftView, currentRightView , primary));//add a menu chooser
		save.setOnAction( e -> saveProject( primary ) );
		reset.setOnAction(e-> resetProject() );
		prjct.getItems().add(load);
		prjct.getItems().add(save);
		prjct.getItems().add(reset);
		
		
		
	// D. Define the toolbar
	

		FontAwesomeIconView faCut = new FontAwesomeIconView(FontAwesomeIcon.CUT);
		FontAwesomeIconView faSelect = new FontAwesomeIconView(FontAwesomeIcon.MOUSE_POINTER);
		FontAwesomeIconView faPlus = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
		FontAwesomeIconView faMinus = new FontAwesomeIconView(FontAwesomeIcon.MINUS);


		cut = new ToggleButton("" , faCut);
		selectPoint = new ToggleButton("" , faSelect);
		selectPoint.setSelected(true);
		
		ToggleGroup toolGroup = new ToggleGroup();
		cut.setToggleGroup(toolGroup);
		selectPoint.setToggleGroup(toolGroup);
		
		Button minus = new Button("", faMinus);	
		thresholdLabel = new Label("1");
		thresholdLabel.setAlignment(Pos.BOTTOM_CENTER);
		thresholdLabel.setPrefWidth(15);
		Button plus = new Button("", faPlus);
				
		minus.setOnAction(e -> actionMinus() );
		plus.setOnAction( e-> actionPlus() );
				

		HBox thresholdSelection = new HBox(2, selectPoint, minus , thresholdLabel , plus);
		//toolGrid.add(cut , 0 , 0 , 1 , 1);
		toolGrid.add(thresholdSelection , 0 , 1 , 3 , 1 );
		toolGrid.setHgap(5);
		toolGrid.setVgap(5);	
		
		
		
	// E. Bring all layout together in the root
		
		
		
		Separator sep3 = new Separator(Orientation.HORIZONTAL);
		root.getChildren().addAll(menu, tripartie , sep3 , toolGrid);
		Scene scene=new Scene( root );
		scene.setCursor(Cursor.DEFAULT);	
		primary.setTitle("Clay");
		primary.setScene(scene);
		primary.initStyle(StageStyle.DECORATED);
		primary.show();
		
		
		
	}//END start()
	
	
	
	
	
	
	
	// **************************************************  Event Handler *****************************************************
	
	
	
	
	
	
	
	
	/**Define the behavior to load an image from the computer
	 * 	
	 * @param left the imageview containing the original image
	 * @param right the imageview containing the image of the sets
	 * @param primary the current stage
	 */
	public void loadingAction( ImageView left , ImageView right , Stage primary ) {
		

		// A. Have the user select of an image
		
		
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("C:"));
			fileChooser.getExtensionFilters().addAll(
			     new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
			    ,new FileChooser.ExtensionFilter("PNG Files", "*.png")
			);
		
			File selectedFile = fileChooser.showOpenDialog(primary);
			FileInputStream input = null;
			FileInputStream input2 = null;
			try {
				input = new FileInputStream(selectedFile);
				input2 = new FileInputStream(selectedFile); 
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
	  // B. Resize the image to 500 if necessary	 	    
	  	    
	  	    Image image = new Image(input);
			Image image2 = new Image(input2 , 500, 500, true , true);
	     
		    if(image.getWidth()>500  ||  image.getHeight()>500) {
		    	imgLeft = ImgtoWritable(image2);
		    	imgOrigin = ImgtoWritable(image2);
			}
			else {
				imgLeft = ImgtoWritable(image);
				imgOrigin = ImgtoWritable(image);
			}
			imgRight = makeWritableWhite((int)imgLeft.getWidth() , (int)imgLeft.getHeight()); //new WritableImage((int)image.getWidth() , (int)image.getHeight());

		    
	// C. set each imageviews to the new image	    
			
			
			left.setImage(imgLeft);
			right.setImage(imgRight);
		
			
	}//END loadingAction
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

/**Method called when the user clicks anywhere on the original image
 * 
 */
	public void handle(MouseEvent e) {
		// TODO Auto-generated method stub

		if(  cut.isSelected() ) {
			System.out.println("selected an area");
		}
		else if( selectPoint.isSelected() ) {
			double x = e.getX();
			double y = e.getY();
			Point[] set = generateNewSet( new Point( x , y ) );
			listSet.add(set);
			currentSet = set;
		}
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
	

	
	
/**Method called when the user chooses to increase the threshold 
 * 
 */
	public void actionPlus() {
		
		if(currentSet!=null) {
			int threshold = Integer.parseInt(thresholdLabel.getText())+1;
			thresholdLabel.setText(""+(threshold)+"");
			Clustering.expand( imgLeft , imgRight , currentSet , threshold );
		}
		
	}//END actionPlus
	
	
	
	
	
	
	
	
	/**Method called when the user chooses to decrease the threshold 
	 * 
	 */	
	public void actionMinus() {
		
		if(currentSet!=null) {
		
			int threshold = Integer.parseInt(thresholdLabel.getText());
			
			if(threshold>1) {
				thresholdLabel.setText(""+(threshold-1)+"");
				Clustering.contract( imgLeft , imgRight , currentSet , threshold-1 );
			}
			
		}
		
	}//END actionMinus
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**Method called when the user chooses to remove a set
	 * 
	 */
	public void removeSet() {
		
		if(currentSet!=null) {
			
			PixelReader rightReader = imgRight.getPixelReader();
			PixelWriter rightWriter = imgRight.getPixelWriter();
			PixelWriter leftWriter = imgLeft.getPixelWriter();
			
			for(int i=0;i<listSet.size();i++) {
					
					if( listSet.get(i) == currentSet ) {
						
						for(int b=1;b<currentSet[0].getX();b++) {
							
							Point pt = currentSet[b];
							Color c = rightReader.getColor( (int)pt.getX() , (int)pt.getY() );
							leftWriter.setColor( (int)pt.getX() , (int)pt.getY() , c );
							rightWriter.setColor( (int)pt.getX() , (int)pt.getY() , Color.WHITE );
							
						}//for(b)
						
						listSet.remove(i);
						currentSet = null;
	
					}
					
			}//for(i)
		

		}//(!null)
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**Method called when the user chooses to reset the whole project: the left image is reseted to the original image untampered and the right image is made blank
	 * 
	 */
	public void resetProject() {
		
		
		PixelWriter rightWriter = imgRight.getPixelWriter();
		PixelWriter leftWriter = imgLeft.getPixelWriter();
		PixelReader originReader  = imgOrigin.getPixelReader();
		
		
		for(int i=0;i<imgLeft.getWidth();i++) {
			for(int j=0;j<imgLeft.getHeight();j++) {
				
				Color c = originReader.getColor(i,j);

				leftWriter.setColor(i,j,c);
				rightWriter.setColor(i,j, Color.WHITE);
					
				
				
			}//for(i)
		}//for(j)
		

		
		listSet = new ArrayList();
		
	}
	
	
	
	
	
	
	
	
	/**Method called when the user chooses to save the current project
	 * 
	 */
	public void saveProject(Stage primary) {
		
		
		// A. Have the user choose a name and location for the image
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("C:\\Users\\Dr.Manhattan\\eclipse-workspace\\Darwinin\\test"));
		fileChooser.getExtensionFilters().addAll(
		     new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
		    ,new FileChooser.ExtensionFilter("PNG Files", "*.png")
		);
	
	
		File selectedFile = fileChooser.showSaveDialog(primary);
		
		// B. Save the image to the computer 
		
		try {
	        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imgRight, null);
	        ImageIO.write(bufferedImage, "png", selectedFile);
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
				//  ********************************************  Secondary method   ************************************
	
	
	
	// A set of Point is a "data struture" where the first Point has value P(end, threshold), that is:
	// end is the last point in the set and threshold is the current threshold of the set
	
	/**Initialized a new array of point from the point pt passed as argument
	 * 
	 * @param pt the initial point
	 * @return an empty array of point except for the point pt passed as argument located at array[1]
	 */
	public Point[] generateNewSet(Point pt) {
		
		Point[] set=null;
		int x = (int)pt.getX();
		int y = (int)pt.getY();
		
		
		if(imgLeft!=null) {

			Color c = imgLeft.getPixelReader().getColor( x , y );
			imgLeft.getPixelWriter().setColor(x,y, Color.WHITE );
			imgRight.getPixelWriter().setColor(x,y,c);
			set = new Point[(int) (imgLeft.getWidth()*imgLeft.getHeight())];
			set[0]=new Point( 2 , 1 );
			set[1]=new Point( pt.getX() , pt.getY() , c.hashCode() );
			thresholdLabel.setText(""+1+"");	
		}
		else {
			System.out.println("imgLeft is null");
		}
		
		
		return set;
	}
	
	
	
	
	
	
	
	
	
	
	
	// from the right image, a user may not have selected exactly a set, so we accommodate by finding the closest set
	// to the point selected within a radius of 10 pixel
	
	/**Find the set closest to the point, with condition that the distance must be less than 10
	 * 
	 * @param pt
	 * @return the closest set to the point 
	 */
	public Point[] findClosestSet(Point pt) {
		
		int indexClosest=5000;
		double closestDistanceOfSet=5000;
		
		// A. Find the set with the closest distance to the point pt
		
		
		for(int a=0;a<listSet.size();a++) {
			
			Point[] set = (Point[]) listSet.get(a);
			double closestDistance=5000;
			
			for(int b=1;b<set[0].getX();b++) {
				
				double dist = Point.distXY( pt , set[b] );		
				
				if(dist<closestDistance) {
					closestDistance=dist;
				}
				
				
				if(dist==0) {
					closestDistanceOfSet=0;
					break;
				}
				
			}//for(b)
			
			if( closestDistanceOfSet > closestDistance ) {
				closestDistanceOfSet=closestDistance;
				indexClosest=a;											
			}
			
			if(closestDistanceOfSet==0){
				indexClosest=a;
				break;
			}
			
			
		}//for(a)
		
		
		// B. Return the closest set to the point pt if the distance is less than 10
		
		
		if(closestDistanceOfSet<10) {
			Point[] closestSet = (Point[]) listSet.get(indexClosest);
			thresholdLabel.setText(""+(int)closestSet[0].getY()+"");			
			return closestSet;
		}
		else {
			return currentSet;
		}
	}
	
	
	
	
	
	
	
	
				// ******************************************************  Other Methods  ******************************************************
	
	
	
	
	
	
	
	
	
	
	
	
	public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
	    BufferedImage dbi = null;
	    if(sbi != null) {
	        dbi = new BufferedImage(dWidth, dHeight, imageType);
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}
	
	
	
	
	
	/**
	 * 
	 * @param img the image to be turned into a writable image
	 * @return a writabel image of the image
	 */
	public WritableImage ImgtoWritable(Image img) {
		
		WritableImage wrimg = new WritableImage( (int)img.getWidth() , (int)img.getHeight() );
		PixelReader pxlReader = img.getPixelReader();
		PixelWriter pxlWriter = wrimg.getPixelWriter();
		
		for(int i=0;i<img.getWidth();i++) {
			for(int j=0;j<img.getHeight();j++) {
				
				pxlWriter.setColor(i,j, pxlReader.getColor(i,j));
				
			}
		}
		
		
		return wrimg;
	}
	
	
	
	
	/**Make a copy of a writable image
	 * 
	 * @param wrimg1 the writable image to be copied
	 * @return a copy of the writable image
	 */
	public WritableImage copyWritable(WritableImage wrimg1) {
		
		WritableImage wrimg2 = new WritableImage( (int)wrimg1.getWidth() , (int)wrimg1.getHeight() );
		PixelReader pxlReader = wrimg1.getPixelReader();
		PixelWriter pxlWriter = wrimg2.getPixelWriter();
		
		for(int i=0;i<wrimg1.getWidth();i++) {
			for(int j=0;j<wrimg1.getHeight();j++) {
				
				pxlWriter.setColor(i,j, pxlReader.getColor(i,j));
				
			}
		}
		
		
		return wrimg2;
	}
	
	
	
	
	
	
	
	
	/**Generate a blank image of size width * height
	 * 
	 * @param width the width of the blank image wanted
	 * @param height the height of the blank image wanted
	 * @return a blank bufferedimage
	 */
	public static BufferedImage makeWhite(int width, int height) {
		
		BufferedImage toRet=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				toRet.setRGB(i, j, -1);
			}
		}
		
		return toRet;
	}//END makeWhite()








	
	
	
	
	
	/**Generate a blank image of size width * height
	 * 
	 * @param width the width of the blank image wanted
	 * @param height the height of the blank image wanted
	 * @return a blank writable image
	 */
	public WritableImage makeWritableWhite(int width, int height) {
		
		WritableImage wimg = new WritableImage(width, height);
		PixelWriter pxlwtr = wimg.getPixelWriter();
		
		for(int i=0;i<width;i++) {
			for(int j=0;j<height;j++) {
				pxlwtr.setColor(i,j, Color.WHITE);
			}
		}
		
		return wimg;
	}
	
	
	
	
	
	
	
}//END class{}