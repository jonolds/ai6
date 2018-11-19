import java.util.Random;

class DecisionTree extends DTreeHelp {
	static Random r;
	boolean debug = false;
	Node root;
	double[] mode, predics;
	public static final int k = 5;
	
	DecisionTree() {r = new Random(55);} 
	DecisionTree(Random rando) {r = rando; }

	
	void train(Matrix f, Matrix l) {
		root = resolveNode(f, l);
	}
	Node resolveNode(Matrix f, Matrix l) {
		
		if(l.isColHomo(0) || f.rows() < k)
			return new Leaf(l.row(0)[0]);
		else {
			Matrix f1 = new Matrix(f), f2 = f1.struct(f), l1 = new Matrix(l), l2 = f.struct(l);
			Data data = new Data();
			if(canSplit(f1, l1, f2, l2, data))
				return new Inter(data, resolveNode(f1, l1), resolveNode(f2, l2));
			return new Leaf((l.isReal(0)) ? l.columnMean(0) : l.mostCommonValue(0));
		}
	}
	boolean canSplit(Matrix f1, Matrix l1, Matrix f2, Matrix l2, Data data) {
		boolean foundSplit = false;
		int[] order = randoHomoCols(f1, r);
		for(int i = 0; i < order.length && !foundSplit; i++) {
			data.set(order[i], f1.isReal(order[i]));
			if(!f1.isColHomo(data.at)) {
				if(f1.isReal(data.at))
					for(int try10 = 0; try10 < 10 && !foundSplit; try10++) {
						data.p = f1.columnMean(data.at);
						foundSplit = slice(f1, l1, f2, l2, data);
					}
				else {
					data.p = f1.mostCommonValue(data.at);
					foundSplit = slice(f1, l1, f2, l2, data);
				}
		}}
		return foundSplit;
	}
	boolean slice(Matrix f1, Matrix l1, Matrix f2, Matrix l2, Data data) {
		for(int i = f1.rows()-1; i > -1; i--)
			if((data.real && f1.row(i)[data.at] >= data.p) || (!data.real && f1.row(i)[data.at] != data.p)) {
				f2.pushRow(f1.popRow(i));  
				l2.pushRow(l1.popRow(i)); 
			}
		return (f1.rows() != 0 && f2.rows() != 0) ? true : false;
	}
	
	int countMisclassifications(Matrix features, Matrix labels) {
		if(features.rows() != labels.rows())
			throw new IllegalArgumentException("Mismatching number of rows");
		double[] pred = new double[labels.cols()], mode = new double[features.rows()];
		int miss = 0;
		for(int i = 0; i < features.rows(); i++) {
			double[] feat = features.row(i);
			predict(feat, pred);
			mode[i] = pred[0];
			double[] lab = labels.row(i);
			for(int j = 0; j < lab.length; j++) {
				if(pred[j] != lab[j])
					miss++;
			}
		}
		return miss;
	}

	void predict(double[] in, double[] out) { 
		System.arraycopy(getLab(in, root), 0, out, 0, 1);
	}
	
	double[] getLab(double[] f, Node n) {
		if(n.isLeaf()) 
			return ((Leaf)n).label;
		Inter in = (Inter)n;
		if((in.real && (f[in.att] < in.p)) || (!in.real && (f[in.att] == in.p)))
			return getLab(f, in.a);
		return getLab(f, in.b);
	}
	String name() {return "DecisionTree"; }
}