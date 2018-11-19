
class Main {
	static void test(SupervisedLearner learner, String challenge) throws Exception {
		// Load the training data
		String fn = "data/" + challenge;
		Matrix trainFeatures = new Matrix();
		trainFeatures.loadARFF(fn + "_train_feat.arff");
		Matrix trainLabels = new Matrix();
		trainLabels.loadARFF(fn + "_train_lab.arff");

		// Train the model
		learner.train(trainFeatures, trainLabels);

		// Load the test data
		Matrix testFeatures = new Matrix();
		testFeatures.loadARFF(fn + "_test_feat.arff");
		Matrix testLabels = new Matrix();
		testLabels.loadARFF(fn + "_test_lab.arff");

		// Measure and report accuracy
		int misclassifications = learner.countMisclassifications(testFeatures, testLabels);
		System.out.println("Misclassifications by " + learner.name() + " at " + challenge + " = " + Integer.toString(misclassifications) + "/" + Integer.toString(testFeatures.rows()));

	}

	public static void testLearner(SupervisedLearner learner) throws Exception {
		test(learner, "hep");
		test(learner, "vow");
		test(learner, "soy");
//		test(learner, "small");
	}

	public static void main(String[] args) throws Exception {
		testLearner(new BaselineLearner());
		testLearner(new DecisionTree());
		testLearner(new RandomForest(30));
	}
}