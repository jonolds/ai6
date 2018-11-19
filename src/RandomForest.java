import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class RandomForest extends SupervisedLearner{
	ArrayList<DecisionTree> forest;
	Random rando = new Random(645);
	double[] mode;
	int num;
	
	RandomForest(int n) {
		num = n;
		forest = new ArrayList<>(IntStream.range(0, n).mapToObj(x->new DecisionTree(rando)).collect(Collectors.toList()));
	}
	
	void train(Matrix features, Matrix labels) {
		int minSample = 10*features.rows()/num ;
		System.out.println(5*features.rows()/num);
		for(int i = 0; i < num; i++) {
			int[] randomRowNums = rando.ints(0, features.rows()).distinct().limit(minSample).toArray();
			Matrix fCopy = features.struct(features), lCopy = labels.struct(labels);
			for(int k = 0; k < randomRowNums.length; k++) {
				fCopy.pushRow(features.copyRow(randomRowNums[k]));
				lCopy.pushRow(labels.copyRow(randomRowNums[k]));
			}
			forest.get(i).train(fCopy, lCopy);
		}
	}

	int countMisclassifications(Matrix features, Matrix labels) throws Exception {
		double[] pred = new double[labels.rows()];
		for(int row = 0; row < features.rows(); row++) {
			Matrix rowCandidates = new Matrix(0, labels.cols());
			
			for(int treeNum = 0; treeNum < forest.size(); treeNum++) {
				double[] tmp = new double[labels.cols()];
				forest.get(treeNum).predict(features.row(row), tmp);
				rowCandidates.pushRow(tmp);
			}
			pred[row] = rowCandidates.mostCommonValue(0);
		}
		
		return IntStream.range(0, pred.length).map(x->(pred[x] != labels.row(x)[0]) ? 1 : 0).sum();
	}
	void predict(double[] in, double[] out) {
	}

	String name() { return "RandomForest"; }
}
