import java.util.Random;

class DecisionTree extends DTreeHelp {
	static Random r;
	boolean debug = false;
	Node root;
	
	DecisionTree() { name = "DecisionTree"; r = new Random();} 
	DecisionTree(int seed) { name = "DecisionTree"; r = new Random(seed); }

	
	void train(Mat f, Mat l) {
		root = resolveNode(f, l);
	}
	Node resolveNode(Mat f, Mat l) {
		
		if(l.isColHomo(0))
			return new Leaf(l.row(0)[0]);
		else {
			Mat f1 = new Mat(f), f2 = f1.struct(f), l1 = new Mat(l), l2 = f.struct(l);
			Data data = new Data();
			if(canSplit(f1, l1, f2, l2, data))
				return new Inter(data, resolveNode(f1, l1), resolveNode(f2, l2));
			return new Leaf((l.isReal(0)) ? l.colMean(0) : l.colMCV(0));
		}
	}
	boolean canSplit(Mat f1, Mat l1, Mat f2, Mat l2, Data data) {
		boolean foundSplit = false;
		int[] order = randoHomoCols(f1, r);
		for(int i = 0; i < order.length && !foundSplit; i++) {
			data.set(order[i], f1.isReal(order[i]));
			if(!f1.isColHomo(data.at)) {
				if(f1.isReal(data.at))
					for(int try10 = 0; try10 < 10 && !foundSplit; try10++) {
						data.p = f1.colMean(data.at);
						foundSplit = slice(f1, l1, f2, l2, data);
					}
				else {
					data.p = f1.colMCV(data.at);
					foundSplit = slice(f1, l1, f2, l2, data);
				}
		}}
		return foundSplit;
	}
	boolean slice(Mat f1, Mat l1, Mat f2, Mat l2, Data data) {
		for(int i = f1.rows()-1; i > -1; i--)
			if((data.real && f1.row(i)[data.at] >= data.p) || (!data.real && f1.row(i)[data.at] != data.p)) {
				f2.pushRow(f1.popRow(i));  
				l2.pushRow(l1.popRow(i)); 
			}
		return (f1.rows() != 0 && f2.rows() != 0) ? true : false;
	}
	int countMisses(Mat feats, Mat labs) throws Exception {
		if(feats.rows() != labs.rows()) throw new Exception("Mismatching number of rows");
		double[] pred = new double[labs.cols()];
		int miss = 0;
		for(int i = 0; i < feats.rows(); i++)
			if(predict(feats.row(i), pred)[0] != labs.row(i)[0])
				miss++;
		return miss;
	}
	double[] predict(double[] in, double[] out) { 
		return getLab(in, root); 
	}
	
	double[] getLab(double[] f, Node n) {
		if(n.isLeaf()) 
			return ((Leaf)n).label;
		Inter in = (Inter)n;
		if((in.real && (f[in.att] < in.p)) || (!in.real && (f[in.att] == in.p)))
			return getLab(f, in.a);
		return getLab(f, in.b);
	}
}