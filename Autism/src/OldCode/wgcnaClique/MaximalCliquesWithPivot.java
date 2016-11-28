package wgcnaClique;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//pivot
public class MaximalCliquesWithPivot {

	int nodesCount;
	static int NumClique;
	static int NumGoodClique = 0;
	static int loopcount;
	static int ng;
	ArrayList<Vertex> graph = new ArrayList<Vertex>();
	ArrayList<ArrayList<Integer>> FoundClique = new ArrayList<ArrayList<Integer>>();
	static ArrayList<Integer> Aut_index = new ArrayList<Integer>();
	static LoadData loadCoex = new LoadData();
	static GetClique getClique = new GetClique();

	class Vertex implements Comparable<Vertex> {
		int x;

		int degree;
		ArrayList<Vertex> nbrs = new ArrayList<Vertex>();

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getDegree() {
			return degree;
		}

		public void setDegree(int degree) {
			this.degree = degree;
		}

		public ArrayList<Vertex> getNbrs() {
			return nbrs;
		}

		public void setNbrs(ArrayList<Vertex> nbrs) {
			this.nbrs = nbrs;
		}

		public void addNbr(Vertex y) {
			this.nbrs.add(y);
			if (!y.getNbrs().contains(y)) {
				y.getNbrs().add(this);
				y.degree++;
			}
			this.degree++;

		}

		public void removeNbr(Vertex y) {
			this.nbrs.remove(y);
			if (y.getNbrs().contains(y)) {
				y.getNbrs().remove(this);
				y.degree--;
			}
			this.degree--;

		}

		@Override
		public int compareTo(Vertex o) {
			if (this.degree < o.degree) {
				return -1;
			}
			if (this.degree > o.degree) {
				return 1;
			}
			return 0;
		}

		public String toString() {
			return "" + x;
		}
	}

	void initGraph() {
		graph.clear();
		for (int i = 0; i < nodesCount; i++) {
			Vertex V = new Vertex();
			V.setX(i);
			graph.add(V);
		}
	}

	int readTotalGraphCount(BufferedReader bufReader) throws Exception {

		return Integer.parseInt(bufReader.readLine());
	}

	// Reads Input
	void readNextGraph(BufferedReader bufReader) throws Exception {
		try {
			nodesCount = Integer.parseInt(bufReader.readLine());
			int edgesCount = Integer.parseInt(bufReader.readLine());
			initGraph();

			for (int k = 0; k < edgesCount; k++) {
				String[] strArr = bufReader.readLine().split(" ");
				int u = Integer.parseInt(strArr[0]);
				int v = Integer.parseInt(strArr[1]);
				Vertex vertU = graph.get(u);
				Vertex vertV = graph.get(v);
				vertU.addNbr(vertV);

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	// Finds nbrs of vertex i
	ArrayList<Vertex> getNbrs(Vertex v) {
		int i = v.getX();
		return graph.get(i).nbrs;
	}

	// Intersection of two sets
	ArrayList<Vertex> intersect(ArrayList<Vertex> arlFirst, ArrayList<Vertex> arlSecond) {
		ArrayList<Vertex> arlHold = new ArrayList<Vertex>(arlFirst);
		arlHold.retainAll(arlSecond);
		return arlHold;
	}

	// Union of two sets
	ArrayList<Vertex> union(ArrayList<Vertex> arlFirst, ArrayList<Vertex> arlSecond) {
		ArrayList<Vertex> arlHold = new ArrayList<Vertex>(arlFirst);
		arlHold.addAll(arlSecond);
		return arlHold;
	}

	// Removes the neigbours
	ArrayList<Vertex> removeNbrs(ArrayList<Vertex> arlFirst, Vertex v) {
		ArrayList<Vertex> arlHold = new ArrayList<Vertex>(arlFirst);
		arlHold.removeAll(v.getNbrs());
		return arlHold;
	}

	// Version with a Pivot
	void Bron_KerboschWithPivot(ArrayList<Vertex> R, ArrayList<Vertex> P, ArrayList<Vertex> X, String pre) {

		// System.out.print(pre + " " + printSet(R) + ", " + printSet(P) + ", "
		// + printSet(X));
		if ((P.size() == 0) && (X.size() == 0)) {
			// System.out.println("Number of loops taken : "+loopcount);

			NumClique++;
			if (checkNumAut(R, ng)) {
				printClique(R);
			} else {
				//System.out.println(" Skipping this clique. ");
			}

			// printClique(R);
			return;
		}
		// System.out.println();
		ArrayList<Vertex> P1 = new ArrayList<Vertex>(P);
		// Find Pivot
		Vertex u = getMaxDegreeVertex(union(P, X));

		// System.out.println("" + pre + " Pivot is " + (u.x));
		// P = P / Nbrs(u)
		P = removeNbrs(P, u);

		for (Vertex v : P) {
			R.add(v);
			Bron_KerboschWithPivot(R, intersect(P1, getNbrs(v)), intersect(X, getNbrs(v)), pre + "\t");
			R.remove(v);
			P1.remove(v);
			X.add(v);
		}
	}

	Vertex getMaxDegreeVertex(ArrayList<Vertex> g) {
		Collections.sort(g);
		return g.get(g.size() - 1);
	}

	void Bron_KerboschPivotExecute() {

		ArrayList<Vertex> X = new ArrayList<Vertex>();
		ArrayList<Vertex> R = new ArrayList<Vertex>();
		ArrayList<Vertex> P = new ArrayList<Vertex>(graph);
		Bron_KerboschWithPivot(R, P, X, "");
	}

	 void printClique(ArrayList<Vertex> R) { 
	    	NumGoodClique++;
	    	ArrayList<Integer> temp = new ArrayList<Integer>();
	    	
	        System.out.print(""+NumGoodClique+" Good Clique : "); 
	        for (Vertex v : R) { 
	            System.out.print(" " + (v.getX())); 
	            temp.add(v.getX());
	        } 
	        FoundClique.add(temp);
	        System.out.println();                 
	        
	    }     

	String printSet(ArrayList<Vertex> Y) {
		StringBuilder strBuild = new StringBuilder();

		strBuild.append("{");
		for (Vertex v : Y) {
			strBuild.append("" + (v.getX()) + ",");
		}
		if (strBuild.length() != 1) {
			strBuild.setLength(strBuild.length() - 1);
		}
		strBuild.append("}");
		return strBuild.toString();
	}

	private boolean checkNumAut(ArrayList<Vertex> clique, int ng) {
		// System.out.println(Aut_index+" and ng is "+ng);
		//System.out.print(NumClique + ". Clique found. Autism Genes > 2? : ");
		ArrayList<Integer> temp_aut_gene = new ArrayList<Integer>();
		int count = 0;
		for (Vertex v : clique) {
			if (Aut_index.contains(v.getX())) {
				count++;
				temp_aut_gene.add(v.getX());				
			}

			// if(count>ng)
			if (count >= ng) {
				System.out.println("	Found " + count + " Autism gene of index " + temp_aut_gene);
				return true;
			}
		}

		//System.out.println("False.");
		return false;
	}

	public static void main(String[] args) throws IOException {

		double[][] coex;
		double threshold;
		ArrayList<ArrayList<Integer>> foundConnected;

		String inputpath;
		String filepath;
		List<double[]> coex_list;

		// filepath = "10x10coex.csv";
		filepath = "coex_noHeader.csv";
		//filepath = "coex_noHeader2.csv";
		inputpath = "input_test.txt";
		threshold = 0.4875;
		ng = 2;
		NumClique = 0;

		coex = loadCoex.Load(filepath);
		System.out.println("Size of coex : " + coex.length);
		Aut_index = loadCoex.aut_index_AL;
		// System.out.println("Index of Autism Genes : "+Aut_index); //only the
		// index
		coex_list = getClique.putInList(coex, threshold);
		foundConnected = getClique.FindConnected(coex_list, threshold);
		getClique.createInput(foundConnected, inputpath, coex.length);

		BufferedReader bufReader = null;
		if (args.length > 0) {
			// Unit Test Mode
			bufReader = new BufferedReader(new StringReader("1\n5\n7\n0 1\n0 2\n0 3\n0 4\n1 2\n2 3\n3 4\n"));
		} else {
			File file = new File("input_test.txt");
			try {
				bufReader = new BufferedReader(new FileReader(file));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		MaximalCliquesWithPivot ff = new MaximalCliquesWithPivot();
		try {
			int totalGraphs = ff.readTotalGraphCount(bufReader);
			System.out.println("Max Cliques with Pivot");
			for (int i = 0; i < totalGraphs; i++) {
				ff.readNextGraph(bufReader);
				ff.Bron_KerboschPivotExecute();

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exiting : " + e);
		} finally {
			try {
				bufReader.close();
			} catch (Exception f) {

			}
		}
	}
}