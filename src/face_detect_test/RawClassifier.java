package face_detect_test;
import org.opencv.core.*;
import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class RawClassifier {
	private Mat my_image = null;
	private String filename = null;
	private Rect my_facerect = null;
	private String classifier_path = "./classifier_data/haarcascade_frontalface_default.xml";
	public RawClassifier(String in_file){
		filename = in_file;
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		my_image = Imgcodecs.imread(filename);
	}
	
	//This function is responsible for detecting the rough range of the face.
	public Rect face_rect(){
		if(my_image == null){
			System.out.println("not initialized");
			return null;
		}
		if(my_facerect != null)
			return my_facerect;
		//start the detecting
		System.out.println("\nRunning DetectFaceDemo");
		CascadeClassifier face_rect_dtect = new CascadeClassifier(classifier_path);
		MatOfRect face_rect_out = new MatOfRect();
		face_rect_dtect.detectMultiScale(my_image, face_rect_out);
		System.out.println(String.format("Detected %s faces", face_rect_out.toArray().length));
		//we have a matrix of rects representing the position of faces
		//choose the most obvious one
		Rect final_face = null;
		Mat image = my_image.clone();
	    for (Rect rect: face_rect_out.toArray()){
	    	if(final_face == null || final_face.width < rect.width)
	    		final_face = rect;
	    	Imgproc.rectangle(image, new Point(rect.x, rect.y), 
		    		new Point(rect.x + rect.width, rect.y + rect.height),
		    		new Scalar(0, 255, 0));
	    }
	    
	    String filename = "faceDetection_rect.png";
	    System.out.println(String.format("Writing %s", filename));
	    Imgcodecs.imwrite(filename, image);
	    my_facerect = final_face;
		return final_face;
	}
	
	//wrapper for the face_rect to accommodate code in imagerecognize.java
	public int[] getloc(){
		Rect rect = face_rect();
		System.out.println( "processing");
		return new int[]{rect.x , rect.y , rect.width , rect.height};
	}
	
	//TODO: change the return type of this function
	public void face_contour_byrect(){
		//first create helper vars and get the mask
		Rect face_rawrange = face_rect();
		//face_rawrange.height*=1.1;
		//creating the mask which are all background first. Then using the rect to define the foreground.
		Mat mask = new Mat();
    	Mat bgdModel = new Mat();
    	Mat fgdModel = new Mat();
    	//create matrix full of 3, which indicate the foreground.
    	Mat source = new Mat(my_image.height(), my_image.width(), CvType.CV_8U, new Scalar(3));
    	Imgproc.grabCut(my_image, mask, face_rawrange, bgdModel, fgdModel, 8, Imgproc.GC_INIT_WITH_RECT);

    	Core.compare(mask, source, mask, Core.CMP_EQ);
        Mat foreground = new Mat(my_image.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        my_image.copyTo(foreground, mask);
    	
	    String filename = "faceDetection_contour.png";
	    System.out.println(String.format("Writing %s", filename));
	    Imgcodecs.imwrite(filename, foreground);
	}
	
	//TODO: if we can get some input.....
	public void face_contour_bymask(short[][] matrix){
		//creating the mask which are all background first. Then using the rect to define the foreground.
		Rect face_rawrange = face_rect();
		Mat mask = new Mat(my_image.rows(), my_image.cols(), CvType.CV_8U);
		mask.setTo(new Scalar(Imgproc.GC_PR_BGD));
    	Mat tmp_mask_frt = new Mat(face_rawrange.height, face_rawrange.width, CvType.CV_8U);
    	tmp_mask_frt.setTo(new Scalar(Imgproc.GC_PR_BGD));
    	for(int i=0; i<matrix.length; i++){
    		for(int j=0; j<matrix[0].length; j++){
    			tmp_mask_frt.put(i, j, matrix[i][j]);
    		}
    	}
    	Mat mask_sub_ptr = mask.colRange(face_rawrange.x, face_rawrange.x+face_rawrange.width).
    							rowRange(face_rawrange.y, face_rawrange.y+face_rawrange.height);
    	tmp_mask_frt.copyTo(mask_sub_ptr);
		
		/*Imgproc.ellipse(mask, new Point(face_rawrange.x+face_rawrange.width/2, face_rawrange.y+face_rawrange.height/2),
				new Size(face_rawrange.y, face_rawrange.y/1.3), 43.0, 0.0, 360.0, new Scalar(Imgproc.GC_PR_FGD), -1);*/
		
    	Mat bgdModel = new Mat();
    	Mat fgdModel = new Mat();
    	double[] old_val = {Imgproc.GC_FGD};
		double new_val = Imgproc.GC_PR_FGD;
    	//create matrix full of 3, which indicate the foreground.
    	Imgproc.grabCut(my_image, mask, face_rawrange, bgdModel, fgdModel, 8, Imgproc.GC_INIT_WITH_MASK);
    	Mat source = new Mat(my_image.height(), my_image.width(), CvType.CV_8U, new Scalar(Imgproc.GC_PR_FGD));
    	for(int i=face_rawrange.x; i<face_rawrange.x+face_rawrange.width; i++)
    		for(int j=face_rawrange.y; j<face_rawrange.y + face_rawrange.height; j++)
    			if(mask.get(j, i)[0] == old_val[0])
    				mask.put(j, i, new_val);
    	
  
    	Core.compare(mask, source, mask, Core.CMP_EQ);
        Mat foreground = new Mat(my_image.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        my_image.copyTo(foreground, mask);
    	
	    String filename = "faceDetection_contour2.png";
	    System.out.println(String.format("Writing %s", filename));
	    Imgcodecs.imwrite(filename, foreground);
	}
}
