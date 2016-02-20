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
		filename = filename;
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		my_image = Imgcodecs.imread(filename);
	}
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
		return final_face;
	}
	//TODO: change the return type of this function
	public void 
	public void face_range() {
	    System.out.println("\nRunning DetectFaceDemo");
	    // Create a face detector from the cascade file in the resources
	    // directory.
	    CascadeClassifier faceDetector = new CascadeClassifier("./classifier_data/haarcascade_frontalface_default.xml");
	    Mat image = Imgcodecs.imread("./test.jpg");
	    // Detect faces in the image.
	    // MatOfRect is a special container class for Rect.
	    MatOfRect faceDetections = new MatOfRect();
	    faceDetector.detectMultiScale(image, faceDetections);
	    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
	    //==================creating the mat for the grabCut=========================
	    Rect final_face = null;
	    for (Rect rect: faceDetections.toArray()){
	    	if(final_face == null || final_face.width < rect.width)
	    		final_face = rect;
	    }
	    Mat mask = Mat.ones(image.rows(), image.cols(), CvType.CV_8U).clone();
//	    byte data[] = {2};
//	    mask.get(0, 0, data);
//	    System.out.println("value"+data[0]);
//    	mask.dump();
//    	System.out.println("here");
    	Mat tmp_mask_frt = Mat.zeros(final_face.height, final_face.width, CvType.CV_8U).clone();
    	Mat mask_sub_ptr = mask.colRange(final_face.x, final_face.x+final_face.width).
    							rowRange(final_face.y, final_face.y+final_face.height);
    	tmp_mask_frt.copyTo(mask_sub_ptr);
    	Mat result = new Mat();
    	Mat bgdModel = new Mat();
    	Mat fgdModel = new Mat();
    	Mat source = new Mat(image.height(), image.width(), CvType.CV_8U, new Scalar(3));
    	Imgproc.grabCut(image, result, final_face, bgdModel, fgdModel, 8, Imgproc.GC_INIT_WITH_RECT);

    	Core.compare(result, source, result, Core.CMP_EQ);
        Mat foreground = new Mat(image.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        image.copyTo(foreground, result);
    	
	    String filename = "faceDetection.png";
	    System.out.println(String.format("Writing %s", filename));
	    Imgcodecs.imwrite(filename, foreground);
	  }
}
