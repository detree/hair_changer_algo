package face_detect_test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainFunction {

	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	    System.out.println( "mat = " + mat.dump() );
	   //new RawClassifier().face_range();
	    String addr = "test.jpg";
	    RawClassifier faceDetect = new RawClassifier(addr);
	    int[] dataOfRec;
	    dataOfRec = faceDetect.getloc();
	    System.out.println( dataOfRec[0] + " " + dataOfRec[1]+ " " + dataOfRec[2] + " " + dataOfRec[3] );
	    colorRecognize testCase = new colorRecognize();
		testCase.readImg(addr);
		testCase.readXY(dataOfRec);
	//	testCase.getFacialColor(120, 110, 100);
		testCase.getFacialAuto();
		//System.out.println(testCase.facial[0] + " " + testCase.facial[1]);
		testCase.solve();
		testCase.getHairline();
		testCase.drawHairline();
	    faceDetect.face_contour_bymask(testCase.solveForMatrix());
		//testCase.writeImgOriginal("output.jpg");

		testCase.writeImg("output.jpg");
		faceDetect.face_contour_bymask(testCase.solveForMatrix());
		System.out.println("finished");
	}

}

/*public class testing {

	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	    System.out.println( "mat = " + mat.dump() );
	//    new RawClassifier().face_range();
	}

}

/*import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class testing {
	public static void main(String[] args){
		imagerecognize testCase = new imagerecognize();
		testCase.readImg("WIN_20160220_03_09_08_Pro.jpg");
		int[] facial = new int[]{110 , 60 , 55};
		testCase.solve(facial, 150);
		testCase.writeImg("output.jpg");
		//System.out.print(testCase.getXY(500 , 500));
	/*	try {
		    // retrieve image
		    BufferedImage bi = new BufferedImage(800 , 600 , BufferedImage.TYPE_INT_RGB);
		    File outputfile = new File("saved.png");
		    ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
		}
*/
	/*}
}	
*/