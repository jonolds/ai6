class Main {
	static void test(SupervisedLearner learner, String s) throws Exception {
		Set set = new Set(s);
		// Train the model
		learner.train(set.trainF, set.trainL);
		// Measure and report accuracy
		int miss = learner.countMisses(set.testF, set.testL);
		System.out.println("Miss# by " + learner.name + ": " + s + " = " + miss + "/" + set.testF.rows());
	}

	public static void testLearner(SupervisedLearner learner) throws Exception {
		test(learner, "hep");
		test(learner, "vow");
		test(learner, "soy");
		test(learner, "small");
	}

	public static void main(String[] args) throws Exception {
		testLearner(new BaselineLearner());
		testLearner(new DecisionTree());
		//testLearner(new RandomForest(50));
	}
}