abstract class SupervisedLearner {
	String name;
	/// Train this supervised learner
	abstract void train(Mat features, Mat labels);
	/// Make a prediction
	abstract double[] predict(double[] in, double[] out);
	/// Measures the misclassifications with the provided test data
	int countMisses(Mat features, Mat labels) throws Exception {
		if(features.rows() != labels.rows()) throw new Exception("Mismatching number of rows");
		double[] pred = new double[labels.cols()];
		int miss = 0;
		for(int i = 0; i < features.rows(); i++) {
			double[] feat = features.row(i);
			double[] lab = labels.row(i);
			predict(feat, pred);
			for(int j = 0; j < lab.length; j++)
				if(pred[j] != lab[j])
					miss++;
		}
		return miss;
	}
}

class BaselineLearner extends SupervisedLearner {
	BaselineLearner() {name = "BaselineLearner";}
	double[] mode;
	
	void train(Mat features, Mat labels) {
		mode = new double[labels.cols()];
		for(int i = 0; i < labels.cols(); i++) {
			if(labels.valueCount(i) == 0)
				mode[i] = labels.colMean(i);
			else
				mode[i] = labels.colMCV(i);
		}
	}
	
	int countMisses(Mat features, Mat labels) throws Exception {
		if(features.rows() != labels.rows()) throw new Exception("Mismatching number of rows");
		double[] pred = new double[labels.cols()];
		int miss = 0;
		for(int i = 0; i < features.rows(); i++) {
			double[] feat = features.row(i);
			double[] lab = labels.row(i);
			predict(feat, pred);
			for(int j = 0; j < lab.length; j++)
				if(pred[j] != lab[j])
					miss++;
		}
		return miss;
	}

	double[] predict(double[] in, double[] out) { 
		Vec.copy(out, mode); 
		return out;
	}
}