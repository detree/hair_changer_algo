package face_detect_test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainFunction {

	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	    System.out.println( "mat = " + mat.dump() );
	   //new RawClassifier().face_range();
	    String addr = "test7.jpg";
	    RawClassifier faceDetect = new RawClassifier(addr);
	    int[] dataOfRec;
	    dataOfRec = faceDetect.getloc();   // 1st
	   // System.out.println( dataOfRec[0] + " " + dataOfRec[1]+ " " + dataOfRec[2] + " " + dataOfRec[3] );
	    colorRecognize testCase = new colorRecognize();
		testCase.readImg(addr);
		testCase.readXY(dataOfRec);
	//	testCase.getFacialColor(120, 110, 100);
		testCase.getFacialAuto();
		//System.out.println(testCase.facial[0] + " " + testCase.facial[1]);
		testCase.solve();
		testCase.getHairline();
		testCase.drawHairline();
		short[][] matrix = testCase.solveForMatrix();
	    faceDetect.face_contour_bymask(matrix);
		//testCase.writeImgOriginal("output.jpg");
		testCase.writeImg("output.jpg");
		System.out.println("finished");
		//System.out.println(Imgproc.GC_BGD);
	}

}