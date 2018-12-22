import java.io.*;
import java.text.DecimalFormat;
import java.util.Locale;

import vpt.Image;
import vpt.algorithms.conversion.RGB2Gray;
import vpt.algorithms.histogram.Histogram;
import vpt.algorithms.io.Load;
@SuppressWarnings("Duplicates")

/**
 * Outex14 icin deneme alani.
 * 
 * @author yoktish
 * 
 * Test suite ID:	Outex_TC_00014
 * Textures:		68 textures
 * Window size:		128x128
 * Illuminants:		Horizon, Inca, TL84
 * Rotations:		00
 * Resolutions:		100dpi
 *
 * http://www.outex.oulu.fi/index.php?page=classification
 * 
 */
public class Test14{
	
	/**
	 * 
	 * A general purpose descriptor for an Image object
	 * 
	 * @param img The image to be described
	 * @return the feature vector of the image to be described
	 */
	private static double[] describea(Image img) {
		return Histogram.invoke(img, true);
	}

	private static double[] describe(Image img,int col,int row) {
		int feature_vector_size = 1;
		int feature_vector_index = 0;
		double [] feature_vector = new double [feature_vector_size];

		// 8-bit grayscale value
		int pixel = img.getXYByte(col,row);
		feature_vector[feature_vector_index++] = pixel;

		/*// Histogram
		double [] histogram = new double [256];
		double total = 0;
		for(int i = col-4;i<=col+4;i++)
		{
			for(int j=row-4; j<=row+4; j++)
			{
				if(i<0 || j<0 || i>=img.getXDim() || j>=img.getYDim())
					continue;
				int pixelValue = img.getXYByte(i,j);
				histogram[pixelValue]+= 1;
				total += pixelValue;
			}
		}

		for(int i = 0; i<histogram.length; i++)
			feature_vector[feature_vector_index++] = histogram[i];

		// Mean
		double mean = total / 81;
		feature_vector[feature_vector_index++] = mean;

		// Variance
		double sum = 0;
		for(int i = col-4;i<=col+4;i++)
		{
			for(int j=row-4; j<=row+4; j++)
			{
				if(i<0 || j<0 || i>=img.getXDim() || j>=img.getYDim())
					continue;
				int pixelValue = img.getXYByte(i,j);
				sum += Math.pow(pixelValue - mean,2);
			}
		}
		double std_deviation = Math.sqrt(sum);
		double variance = std_deviation / 81;

		feature_vector[feature_vector_index++] = variance;*/

		return feature_vector;
	}

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "US"));
		DecimalFormat df = new DecimalFormat("#.######");


		String folderName = "Gray";
		new File(folderName).mkdir();
		boolean first = true;


		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(folderName + "/train.arff")));
			PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(folderName + "/test.arff")));

			// Train
			Image img = Load.invoke("pan.png");
			Image train = Load.invoke("train.png");
			double[] feature = null;
			for(int row=0; row<img.getYDim(); row++)
			{
				for(int col=0; col<img.getXDim(); col++)
				{
					if(train.getXYByte(col,row) == 0)
						continue;
					feature = describe(img,col,row);
					if(first == true) {
						printHeader(pw, feature.length);
						printHeader(pw2, feature.length);
						first = false;
					}

					for(int j = 0; j < feature.length; j++)
						pw.print(df.format(feature[j])+",");

					pw.println(train.getXYByte(col,row));

					System.err.println(train.getXYByte(col,row) + " train");
				}
			}
			pw.close();

			// Pan
			Image pan = Load.invoke("pan.png");
			Image gt = Load.invoke("GT.png");
			feature = null;
			for(int row=0; row<pan.getYDim(); row++)
			{
				for(int col=0; col<pan.getXDim(); col++)
				{
					if(gt.getXYByte(col,row) == 0)
						continue;
					feature = describe(pan,col,row);
					for(int j = 0; j < feature.length; j++)
						pw2.print(df.format(feature[j])+",");

					pw2.println(gt.getXYByte(col,row));

					System.err.println(gt.getXYByte(col,row) + " test");
				}
			}
			pw2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	// *** There is nothing to modify beyond this line *** //
	public static void maina(String[] args){
		String path = "Outex_TC_00014/images/";
		Locale.setDefault(new Locale("en", "US"));
		DecimalFormat df = new DecimalFormat("#.######");
		
		String folderName = "test1";
		
		try{
			String s = null;
			new File(folderName).mkdir();
			boolean first = true;
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(folderName + "/train.arff")));
			PrintWriter pw2 = new PrintWriter(new BufferedWriter(new FileWriter(folderName + "/test1a.arff")));
			PrintWriter pw3 = new PrintWriter(new BufferedWriter(new FileWriter(folderName + "/test1b.arff")));
			
			for(int i = 0; i < 1360; i++){
				if(i < 10) s = "00000";
				else if(i < 100) s = "0000";
				else if(i < 1000) s = "000";
				else s = "00";
				
				Image img = Load.invoke(path + s + i +".png");
				double[] feature = describea(img);
	
				if(first == true) {
					printHeader(pw, feature.length);
					printHeader(pw2, feature.length);
					printHeader(pw3, feature.length);
					first = false;
				}
				
				for(int j = 0; j < feature.length; j++)
					pw.print(df.format(feature[j])+",");
				
				pw.println(i/20);
				
				System.err.println(i + " train");
			}
			pw.close();
			
			
			for(int i = 1360; i < 2720; i++){
				if(i < 10) s = "00000";
				else if(i < 100) s = "0000";
				else if(i < 1000) s = "000";
				else s = "00";
				
				Image img = Load.invoke(path + s + i +".png");			
				double[] feature = describea(img);
	
				for(int j = 0; j < feature.length; j++)
					pw2.print(df.format(feature[j])+",");
				
				pw2.println((i-1360)/20);
				
				System.err.println(i + " test");
			}
			pw2.close();
			
			for(int i = 2720; i < 4080; i++){
				if(i < 10) s = "00000";
				else if(i < 100) s = "0000";
				else if(i < 1000) s = "000";
				else s = "00";
				
				Image img = Load.invoke(path + s + i +".png");
				
				double[] feature = describea(img);
	
				for(int j = 0; j < feature.length; j++)
					pw3.print(df.format(feature[j])+",");
				
				pw3.println((i-2720)/20);
				
				System.err.println(i + " test");
			}
			pw3.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	private static void printHeader(PrintWriter pw, int flength){
		pw.println("@RELATION deneme");
		for(int i = 1; i <= flength; i++)
			pw.println("@ATTRIBUTE o" + i +"	REAL");
		pw.println("@ATTRIBUTE o 	{1,2,3,4,5,6}");
		pw.println("@DATA");
	}

	private static void printHeadera(PrintWriter pw, int flength){
		pw.println("@RELATION deneme");
		for(int i = 1; i <= flength; i++)
			pw.println("@ATTRIBUTE o" + i +"	REAL");
		pw.println("@ATTRIBUTE o 	{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67}");
		pw.println("@DATA");
	}
}
