import java.text.DecimalFormat;
import java.util.Locale;

import weka.classifiers.Evaluation;

public class Classifier {
	
	public static void main(String[] args){
		Locale.setDefault(new Locale("en", "US"));
		DecimalFormat df = new DecimalFormat("#.####");
		String folder = "Gray";
		
		String trainPath = folder + "/train.arff";
		String testPath = folder + "/test.arff";
		double kappa1 = computeKappa(trainPath, testPath);
		System.err.println(df.format(kappa1));
	}
	
	private static double computeKappa(String trainPath, String testPath) {		
		// 1NN
		String[] options = {"-t", trainPath, "-T", testPath, "-o", "-v"};				// 1NN
		
		// Random Forest
		// String[] options = {"-t", trainPath, "-T", testPath, "-o", "-v", "-i", "-I", "100", "-K", "AAAA", "-S", "1"};
		
		try{
			//String results = Evaluation.evaluateModel(new weka.classifiers.lazy.IBk(), options);							// 1NN
			String results = Evaluation.evaluateModel(new weka.classifiers.trees.RandomForest(), options);				// RF
			
			String[] lines = results.split("\n");
			double kappa = 0.0;
			
			for(int j = 0; j < lines.length; j++){
				
				if(lines[j].startsWith("Kappa")){
					String[] tokens = lines[j].split("\\s+");
					kappa = Double.parseDouble(tokens[2]);
					//System.err.println();
					return kappa;
				}					
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return 0;
	}
}
