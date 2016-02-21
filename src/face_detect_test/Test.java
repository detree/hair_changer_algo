package face_detect_test;

import org.opencv.core.Core;

public class Test {

	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    //Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	    //System.out.println( "mat = " + mat.dump() );
		RawClassifier test = new RawClassifier("test.jpg");
		test.face_rect();
	    test.face_contour_byrect();
	}

}
