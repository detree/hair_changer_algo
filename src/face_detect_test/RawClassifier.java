package face_detect_test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class RawClassifier {
	private Mat my_image = null;
	private String filename = null;
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
		//start the detecting
		System.out.println("\nRunning DetectFaceDemo");
		CascadeClassifier face_rect_dtect = new CascadeClassifier(classifier_path);
		MatOfRect face_rect_out = new MatOfRect();
		face_rect_dtect.detectMultiScale(my_image, face_rect_out);
		System.out.println(String.format("Detected %s faces", face_rect_out.toArray().length));
		//we have a matrix of rects representing the position of faces
		//choose the most obvious one
		Rect final_face = null;
	    for (Rect rect: face_rect_out.toArray()){
	    	if(final_face == null || final_face.width < rect.width)
	    		final_face = rect;
	    }
	    Mat image = my_image.clone();
	    Imgproc.rectangle(image, new Point(final_face.x, final_face.y), 
	    		new Point(final_face.x + final_face.width, final_face.y + final_face.height),
	    		new Scalar(0, 255, 0));
	    String filename = "faceDetection_rect.png";
	    System.out.println(String.format("Writing %s", filename));
	    Imgcodecs.imwrite(filename, image);
		return final_face;
	}
	//TODO: change the return type of this function
	public void face_contour_byrect(){
		//first create helper vars and get the mask
		Rect face_rawrange = face_rect();
		face_rawrange.height*=1.1;
		//creating the mask which are all background first. Then using the rect to define the foreground.
		Mat mask = new Mat();
    	new Mat();
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
	public void face_contour_bymask(){
		//creating the mask which are all background first. Then using the rect to define the foreground.
		Rect face_rawrange = face_rect();
		Mat mask = Mat.ones(my_image.rows(), my_image.cols(), CvType.CV_8U).clone();
    	Mat tmp_mask_frt = Mat.zeros(face_rawrange.height, face_rawrange.width, CvType.CV_8U).clone();
    	Mat mask_sub_ptr = mask.colRange(face_rawrange.x, face_rawrange.x+face_rawrange.width).
    							rowRange(face_rawrange.y, face_rawrange.y+face_rawrange.height);
    	tmp_mask_frt.copyTo(mask_sub_ptr);
	}
}
