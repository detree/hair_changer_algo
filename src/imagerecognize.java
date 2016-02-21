import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class imagerecognize {
	public BufferedImage img = null;
	public BufferedImage out = null;
	public int startX;
	public int startY;
	public int widthRec;
	public int heightRec;
	public int[] facial = new int[3];
	public int hairlineY;
	private static double coe = Math.pow(10 , 6) * Math.pow(2, -15);
	//public int[][] 
	public void getFacialColor( int r , int g , int b ){
		facial[0] = r;
		facial[1] = g;
		facial[2] = b;
	}
	public void drawHairline(){
		for ( int x = startX ; x < startX + widthRec ; x ++ ){
			out.setRGB(x, hairlineY, 0xFFFFFF);
		}
	}
	public void getFacialAuto(){
		int rSum = 0 , gSum = 0 , bSum = 0;
		int[][] samples = new int[7][3];
		samples[0] = get4th(1 , 1);
		samples[1] = get4th(1 , 2);
		samples[2] = get4th(1 , 3);
		samples[3] = get4th(2 , 1);
		samples[4] = get4th(3 , 1);
		samples[5] = get4th(3 , 2);
		samples[6] = get4th(3 , 3);
		for( int i = 0 ; i < 7 ; i ++ ){
			rSum += samples[i][0];
			gSum += samples[i][1];
			bSum += samples[i][2];
		}
		rSum /= 7;
		gSum /= 7;
		bSum /= 7;
		facial[0] = rSum;
		facial[1] = gSum;
		facial[2] = bSum;
		
	}
	public void getHairline(){
		int maximumPixels = widthRec;
		int count = 0;
		for ( int y = startY ; y < startY + heightRec ; y ++){
			count = 0;
			for ( int x = startX ; x < startX + widthRec ; x ++ ){
				if ( similar(out.getRGB(x, y) , 0xFFFFFF , 50 ))
					count ++;
			}
			if ( count >= 20 ){
				hairlineY = y;
				return;
			}
		}
		hairlineY = startY + heightRec / 4;
	}
	private int[] get4th( int a , int b ){
		int thresX , thresY;
		thresX = widthRec / 4;
		thresY = heightRec / 4;
		return getXY( startX + thresX * a , startY + thresY * b );
	}
	public void readImg(String addr){
		try {
			img = ImageIO.read(new File(addr));
		} catch (IOException e) {
		}
	}
	public void readXY( int[] data ){
		startX = data[0];
		startY = data[1];
		widthRec = data[2];
		heightRec = data[3];
	}
	public void writeImg(String addr){
		try {
		    // retrieve image
		    File outputfile = new File(addr);
		    ImageIO.write(out, "jpg", outputfile);
		} catch (IOException e) {
		    
		}
	}
	public void writeImgOriginal(String addr){
		try {
		    // retrieve image
		    File outputfile = new File("originalModified" + addr);
		    ImageIO.write(img, "jpg", outputfile);
		} catch (IOException e) {
		    
		}
	}
	public void solve( int differential ){	
		int height = img.getHeight();
		int width = img.getWidth();
		out = new BufferedImage( width , height , BufferedImage.TYPE_INT_RGB );
		for ( int x = startX ; x < startX + widthRec - 1 ; x ++ ){
			for ( int y = startY ; y < startY + heightRec - 1 ; y ++ ){
				int[] pix = getXY(x , y);
				if ( facial(pix , facial ) ){
					int[] pix1 = getXY(x - 1 , y - 1);
					int[] pix2 = getXY(x - 1 , y + 1);
					if ( /*differentiate( pix , pix1 , differential ) || differentiate( pix , pix2 , differential )*/true ){
						int temp = 0xFFFFFF;
						out.setRGB(x, y, temp);
					}
				}
			}
		}
	}
	public void solve1( int differential ){	
		int height = img.getHeight();
		int width = img.getWidth();
		int temp = 0xFFFFFF;
		out = new BufferedImage( width , height , BufferedImage.TYPE_INT_RGB );
		for ( int x = startX ; x < startX + widthRec - 1 ; x ++ ){
			for ( int y = startY ; y < startY + heightRec - 1 ; y ++ ){
				int[] pix = getXY(x , y);
				if ( facial1(pix) ){
					out.setRGB(x, y, temp);
				}
			}
		}
	}
	private boolean facial1(int[] pix){
		int r = pix[0] * 4;
		int g = pix[1] * 4;
		int b = pix[2] * 4;
		int maxRGB = Math.max( Math.max( r , g ) , b );
		double L1 = LAlgorithm(g) + LAlgorithm(maxRGB);
		double L2 = LAlgorithm(r) - LAlgorithm(g) + LAlgorithm(maxRGB);
		return ((int)L1 <= 150 && (int)L1 >= 100 )|| ((int)L2 >= 85 && (int)L2 <= 115);
	}
	private double LAlgorithm( int x ){
		double temp = Math.log10(x + 1);
		return temp * coe;
	}
	private int[] getXY(int x , int y){
		int pixel = img.getRGB(x, y);
		int[] rgb = new int[3];
		rgb[0] = pixel & 0xFF;
		rgb[1] = (pixel >> 8) & 0xFF;
		rgb[2] = (pixel >> 16) & 0xFF;
		return rgb;
	}
	private static boolean facial( int[] rgb , int[] facial ){
		return ( Math.abs(rgb[0] - facial[0]) + Math.abs(rgb[1] - facial[1]) + Math.abs(rgb[2] - facial[2])) <= 30;
	}
	private static boolean similar( int rgb , int rgbCompare , int differential ){
		
		return ( Math.abs(((rgb >> 16 ) & 0xFF) - ((rgbCompare >> 16 ) & 0xFF)) + Math.abs(((rgb >> 8 ) & 0xFF) - ((rgbCompare >> 8 ) & 0xFF)) + Math.abs((rgb & 0xFF) - (rgbCompare & 0xFF))) <= differential;
	}
	//public 
}
