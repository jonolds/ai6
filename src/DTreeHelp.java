import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.*;

abstract class DTreeHelp extends SupervisedLearner {
	
	void printTree(Node node) {
		if(node.isLeaf())
			System.out.println(((Leaf)node).label[0]);
		else {
			printTree(((Inter)node).a);
			printTree(((Inter)node).b);
		}
	}
	static void print(List<Integer> list) {
		System.out.print(list.get(0));
		for(int i = 1; i < list.size(); i++)
			System.out.print(", " + list.get(i));
		System.out.println();
	}
	
	static String toString(List<Integer> list) {
		return list.stream().map(x->x.toString()).collect(Collectors.joining(", "));
	}
	
	static String toString(int[] list) {
		return Arrays.stream(list).mapToObj(x->String.valueOf(x)).collect(Collectors.joining(", "));
	}
	
	static void appendToFile(String s, String filename) throws FileNotFoundException {
		PrintWriter br = new PrintWriter(new FileOutputStream(new File(filename), true));
		br.append(s + "\n");
		br.close();
	}
	
	static <T>void println(T t) {
		System.out.println(t);
	}
	
	static <T>void print(T[] arr) {
		if(arr.length > 0)
			System.out.print(arr[0]);
		if(arr.length > 1)
			for(int i = 1; i < arr.length; i++)
				System.out.print(", " + arr[i]);
		System.out.println();
	}
	
	static void print(int[] arr) {
		if(arr.length > 0)
			System.out.print(arr[0]);
		if(arr.length > 1)
			for(int i = 1; i < arr.length; i++)
				System.out.print(", " + arr[i]);
		System.out.println();
	}
	
	static void print(double[] arr) {
		if(arr.length > 0)
			System.out.print(arr[0]);
		if(arr.length > 1)
			for(int i = 1; i < arr.length; i++)
				System.out.print(", " + arr[i]);
		System.out.println();
	}
	
	static int[] randoHomoCols(Matrix m, Random r) {
		return r.ints(0, m.cols()).distinct().limit(m.cols()).filter(x->!m.isColHomo(x)).toArray();
	}
	
	
	void printTree(Node n, int count) {
		if(n.isLeaf()) System.out.println(count + ") LEAF label:" + ((Leaf)n).label[0]);
		else {
			System.out.println(count + ") INTER att:" + ((Inter)n).att + " p:" + ((Inter)n).p + " real:" + ((Inter)n).real);
			count++;
			printTree(((Inter)n).a, count);
			printTree(((Inter)n).b, count);
		}
	}
}

class Set {
	Matrix trainF, trainL, testF, testL;
	Set(String s) {
		this.trainF = new Matrix("data/" + s + "_train_feat.arff");
		this.trainL = new Matrix("data/" + s + "_train_lab.arff");
		this.testF = new Matrix("data/" + s + "_test_feat.arff");
		this.testL = new Matrix("data/" + s + "_test_lab.arff");
//		this.testF = new Mat("data/" + s + "_train_feat.arff");
//		this.testL = new Mat("data/" + s + "_train_lab.arff");
	}
}

//mode = IntStream.range(0, l.cols()).mapToDouble(x->l.isReal(x) ? l.colMean(x):l.colMCV(x)).toArray();
