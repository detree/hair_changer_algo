package face_detect_test;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class ColorRecg_YCrCb {
	private Mat my_image_rgb;
	private Mat my_image_ycrcb;
	private Mat my_skin_color_mask;
	public ColorRecg_YCrCb(Mat pic_in) {
		my_image_rgb = pic_in;
		my_image_ycrcb = new Mat();
		my_skin_color_mask = null;
	}
	public ColorRecg_YCrCb(String file_in){
		my_image_rgb = Imgcodecs.imread(file_in);
		my_image_ycrcb = new Mat();
		my_skin_color_mask = null;
	}
	//TODO: change the return value to make it useful
	public Mat get_skin_mask(){
		if(my_skin_color_mask != null)
			return my_skin_color_mask;
		String outpic = "other_color_recg.png";
		Imgproc.cvtColor(my_image_rgb, my_image_ycrcb, Imgproc.COLOR_BGR2YCrCb);
		Scalar skin_threshod_min = new Scalar(0, 133, 77);
		Scalar skin_threshod_max = new Scalar(255, 173, 127);
		Mat skin_color = new Mat(my_image_rgb.height(), my_image_rgb.width(), CvType.CV_8U);
		Core.inRange(my_image_ycrcb, skin_threshod_min, skin_threshod_max, skin_color);
		Imgcodecs.imwrite(outpic, skin_color);
		//the color matching is done by now.
		//=================================
		//below we are trying to find a rather coarse contour so to match the general shape and fill it.
		/*List<MatOfPoint> contour_pnts = new ArrayList<MatOfPoint>();
		MatOfPoint biggest_contour = null;
		Mat dependency = new Mat(); MatOfInt hull;
		Imgproc.findContours(skin_color, contour_pnts, dependency, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_TC89_L1);
		//Imgproc.convexHull(contour_pnts, hull);
		Imgproc.drawContours(skin_color, contour_pnts, -1, new Scalar(255,0,0), -1);
		Imgcodecs.imwrite("o"+outpic, skin_color);*/
		//=================================
		my_skin_color_mask = skin_color;
		return my_skin_color_mask;
	}
}
