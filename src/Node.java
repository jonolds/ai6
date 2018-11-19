public abstract class Node {
	abstract boolean isLeaf();
}
class Data {
	Boolean real;
	int at;
	double p;
	public void set(int a, boolean b) {
		at = a;
		real = b;
	}
	public Data(){
	}
	Data(int a, boolean b) {
		this.real = b;
		this.at = a;
	}
	
}
class Inter extends Node {
	public int att; // which attribute to divide on
	public double p; // which value to divide on
	boolean real;
	public Node a = null, b = null;
	
	Inter(Data data, Node a1, Node b1) { 
		this.real = data.real;
		this.att = data.at; 
		this.p = data.p;
		this.a = a1;
		this.b = b1;
	}
	boolean isLeaf() { return false; }
}
// CLASS LeafNode
class Leaf extends Node {
	double[] label = new double[] {1.0};
	
	Leaf(double d) { this.label[0] = d; }
	boolean isLeaf() { return true; }
}