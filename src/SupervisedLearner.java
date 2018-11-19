abstract class SupervisedLearner {
	/// Return the name of this learner
	abstract String name();
	/// Train this supervised learner
	abstract void train(Matrix features, Matrix labels);
	/// Make a prediction
	abstract void predict(double[] in, double[] out);
	/// Measures the misclassifications with the provided test data
	int countMisclassifications(Matrix features, Matrix labels) throws Exception {
		if(features.rows() != labels.rows())
			throw new IllegalArgumentException("Mismatching number of rows");
		double[] pred = new double[labels.cols()];
		int mis = 0;
		for(int i = 0; i < features.rows(); i++) {
			double[] feat = features.row(i);
			predict(feat, pred);
			double[] lab = labels.row(i);
			for(int j = 0; j < lab.length; j++) {
				if(pred[j] != lab[j])
					mis++;
			}
		}
		return mis;
	}
}

class BaselineLearner extends SupervisedLearner {
	double[] mode;

	String name() { return "Baseline"; }

	void train(Matrix features, Matrix labels) {
		mode = new double[labels.cols()];
		for(int i = 0; i < labels.cols(); i++) {
			if(labels.valueCount(i) == 0)
				mode[i] = labels.columnMean(i);
			else
				mode[i] = labels.mostCommonValue(i);
		}
	}

	void predict(double[] in, double[] out) {
		Vec.copy(out, mode);
	}
}