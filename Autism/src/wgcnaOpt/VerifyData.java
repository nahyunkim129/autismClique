package wgcnaOpt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

//degen
public class VerifyData {

	int nodesCount;
	static int NumClique;
	static int NumGoodClique = 0;
	static int loopcount;
	static int ng;
	ArrayList<Gene> graph = new ArrayList<Gene>();
	static ArrayList<ArrayList<Integer>> FoundClique = new ArrayList<ArrayList<Integer>>();
	static ArrayList<Integer> Aut_index = new ArrayList<Integer>();	
	static HashMap<String, Integer> Aut_map = new HashMap<String, Integer>();
	ArrayList<Gene> graphOpt = new ArrayList<Gene>();
	static ArrayList<String> selectedGenes = new ArrayList<String>();
	
	static LoadData loadCoex = new LoadData();
	static GetClique getClique = new GetClique();
	static ArrayList<String> total_gname;
	

	class Gene implements Comparable<Gene> {
		int x;
		int degree;
		ArrayList<Gene> nbrs = new ArrayList<Gene>();

		Gene(int x) {
			this.x = x;
		}

		Gene() {}

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

		public ArrayList<Gene> getNbrs() {
			return nbrs;
		}

		public void setNbrs(ArrayList<Gene> nbrs) {
			this.nbrs = nbrs;
		}

		public void addNbr(Gene y) {
			this.nbrs.add(y);
			if (!y.getNbrs().contains(y)) {
				y.getNbrs().add(this);
				y.degree++;
			}
			this.degree++;

		}

		public void removeNbr(Gene y) {
			this.nbrs.remove(y);
			if (y.getNbrs().contains(y)) {
				y.getNbrs().remove(this);
				y.degree--;
			}
			this.degree--;

		}

		@Override
		public int compareTo(Gene o) {
			if (this.degree < o.degree) {
				return -1;
			}
			if (this.degree > o.degree) {
				return 1;
			}
			return 0;
		}

		public boolean equals(Object v) {
			Gene v1 = (Gene) v;
			if (v1.x == this.x) {
				return true;
			}
			return false;
		}

	}

	void initGraph() {
		graph.clear();
		for (int i = 0; i < nodesCount; i++) {
			Gene V = new Gene();
			V.setX(i);
			graph.add(V);
		}
	}

	void initOptGraph() {
		graphOpt.clear();
		for (int i = 0; i < nodesCount; i++) {
			Gene V = new Gene();
			V.setX(i);
			graphOpt.add(V);
		}
	}

	void constructGraph(BufferedReader bufReader) throws Exception {
		try {
			nodesCount = Integer.parseInt(bufReader.readLine());
			int edgesCount = Integer.parseInt(bufReader.readLine());
			initGraph();
			initOptGraph();

			for (int k = 0; k < edgesCount; k++) {
				String[] strArr = bufReader.readLine().split(" ");
				int u = Integer.parseInt(strArr[0]);
				int v = Integer.parseInt(strArr[1]);
				Gene vertU = graph.get(u);
				Gene vertV = graph.get(v);
				vertU.addNbr(vertV);

				addGeneToGraphOpt(u, v);

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	void addGeneToGraphOpt(int u, int v) {

		Gene vertU = graphOpt.get(u);
		Gene vertV = graphOpt.get(v);
		vertU.addNbr(vertV);
	}

	// Finds nbr of Gene i
	ArrayList<Gene> getNbrs(Gene v) {
		int i = v.getX();
		return graph.get(i).nbrs;
	}

	ArrayList<Gene> intersect(ArrayList<Gene> arlFirst, ArrayList<Gene> arlSecond) {
		ArrayList<Gene> arlHold = new ArrayList<Gene>(arlFirst);
		arlHold.retainAll(arlSecond);
		return arlHold;
	}

	ArrayList<Gene> union(ArrayList<Gene> arlFirst, ArrayList<Gene> arlSecond) {
		ArrayList<Gene> arlHold = new ArrayList<Gene>(arlFirst);
		arlHold.addAll(arlSecond);
		return arlHold;
	}

	ArrayList<Gene> removeNbrs(ArrayList<Gene> arlFirst, Gene v) {
		ArrayList<Gene> arlHold = new ArrayList<Gene>(arlFirst);
		arlHold.removeAll(v.getNbrs());
		return arlHold;
	}

	void FindCliqueRec(ArrayList<Gene> R, ArrayList<Gene> P, ArrayList<Gene> X, String pre) throws IOException {
		
		  
	    
		if ((P.size() == 0) && (X.size() == 0)) {
			NumClique++;
			//if (checkNumAut(R, ng)) {
			if (checkNumAutExtract(R, ng)) {
				printClique(R);
			} else {
				// System.out.println(" Skipping this clique. ");
			}

			// printClique(R);
			return;
		}
		// System.out.println();

		ArrayList<Gene> P1 = new ArrayList<Gene>(P);

		// Find Pivot
		Gene u = getMaxDegreeGene(union(P, X));

		// P = P / Nbrs(u)
		P = removeNbrs(P, u);

		for (Gene v : P) {
			R.add(v);
			FindCliqueRec(R, intersect(P1, getNbrs(v)), intersect(X, getNbrs(v)), pre + "\t");
			R.remove(v);
			P1.remove(v);
			X.add(v);
		}
		
		
	}

	Gene getMaxDegreeGene(ArrayList<Gene> g) {
		Collections.sort(g);
		return g.get(g.size() - 1);
	}

	ArrayList<Integer> orderGene() {

		ArrayList<Integer> graphSorted = new ArrayList<Integer>();

		while (graphOpt.size() > 0) {
			Collections.sort(graphOpt);
			Gene v = removeGene(graphOpt);
			graphSorted.add(v.x);
		}

		return graphSorted;
	}

	Gene removeGene(ArrayList<Gene> g) {
		Gene v = g.get(0);
		for (Gene nbr : v.getNbrs()) {
			nbr.removeNbr(v);
		}
		g.remove(0);
		return v;
	}

	void FindClique() throws IOException {

		ArrayList<Gene> X = new ArrayList<Gene>();
		ArrayList<Gene> R = new ArrayList<Gene>();
		ArrayList<Gene> P = new ArrayList<Gene>();
		ArrayList<Integer> geneSorted = orderGene();

		for (int i : geneSorted) {		
			P.add(graph.get(i));
		}
		
		ArrayList<Gene> P1 = new ArrayList<Gene>(P);

		for (Gene v : P) {
			R.add(v);
			FindCliqueRec(R, intersect(P1, getNbrs(v)), intersect(X, getNbrs(v)), "");
			R.remove(v);
			P1.remove(v);
			X.add(v);

		}
	}

	void printClique(ArrayList<Gene> R) {
		NumGoodClique++;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<String> temp_name = new ArrayList<String>();

		System.out.print("" + NumGoodClique + " Good Clique of size " + R.size() + " : ");
		for (Gene v : R) {
			//System.out.print(" " + (v.getX()));
			temp.add(v.getX());
			temp_name.add(total_gname.get(v.getX()));
		}
		Collections.sort(temp);
		FoundClique.add(temp);
		//System.out.println(temp);
		
		ArrayList<String> temp_name_selected = new ArrayList<String>();
		for(int i=0; i<temp.size(); i++)
		{
			temp_name_selected.add(selectedGenes.get(temp.get(i)));
		}
		System.out.println(temp_name_selected);
		//System.out.println(temp_name);

	}

	private boolean checkNumAut(ArrayList<Gene> clique, int ng) {
		// System.out.println(Aut_index+" and ng is "+ng);
		// System.out.print(NumClique + ". Clique found. Autism Genes > 2? : ");
		ArrayList<String> temp_aut_gene = new ArrayList<String>();
		int count = 0;
		for (Gene v : clique) {
			if (Aut_index.contains(v.getX())) {
				count++;
				temp_aut_gene.add(total_gname.get(v.getX()));
			}
		}

		if (count >= ng) {
			System.out.println("	Found " + count + " Autism gene " + temp_aut_gene);
			return true;
		}
		else
			return false;
	}
	
	private boolean checkNumAutExtract(ArrayList<Gene> clique, int ng) {
		
		HashMap<String, Integer> temp_aut_gene = new HashMap<String, Integer>();
		int count = 0;
		for (Gene v : clique) {
			int convertedIndex = v.getX();
			String nameInExtracted = selectedGenes.get(convertedIndex);
			//System.out.println("converted index : "+convertedIndex+" "+nameInExtracted);
			if (Aut_map.containsKey(nameInExtracted)) {
				count++;
				temp_aut_gene.put(nameInExtracted, Aut_map.get(nameInExtracted));
			}
		}

		if (count >= ng) {
			System.out.println("	Found " + count + " Autism gene of index " + temp_aut_gene);
			return true;
		}
		else
			return false;
	}

	int readTotalGraphCount(BufferedReader bufReader) throws Exception {
		return Integer.parseInt(bufReader.readLine());
	}

	String printSet(ArrayList<Gene> geneSet) {
		StringBuilder strBuild = new StringBuilder();

		strBuild.append("{");
		for (Gene g : geneSet) {
			strBuild.append("" + (g.getX()) + ",");
		}
		if (strBuild.length() != 1) {
			strBuild.setLength(strBuild.length() - 1);
		}
		strBuild.append("}");
		return strBuild.toString();
	}
	
	public static double[][] extractGenes(String PathSelected, double[][] totalCoex ) throws FileNotFoundException
	{
				
		//Read selected gene names and save in selectedGenes
		
		Scanner all_gscan = new Scanner(new File(PathSelected));
		int no_total = 0;
		while (all_gscan.hasNextLine()) {
			String line = all_gscan.nextLine();
			String[] lineArray = line.split(" ");
			selectedGenes.add(new String(lineArray[0]));
			no_total++;
		}
		System.out.println(no_total + " total genes : " + selectedGenes);
		all_gscan.close();
		
				
		ArrayList<Integer> selectedGenesInt = new ArrayList<Integer>();
		// Get 26 autism gene index		
		int size = selectedGenes.size();
		for(int i=0; i<size; i++)
		{						
			//aut_index_map.put(aut_26.get(i), total_gname.indexOf(aut_26.get(i)));
			selectedGenesInt.add(loadCoex.total_gname.indexOf(selectedGenes.get(i)));
						
		}
		//Collections.sort(selectedGenesInt);
		System.out.println(size+" selected genes : "+selectedGenesInt);
		
		/*
		int[] printThis = {2, 30, 33, 34, 35, 36};		
		for(Integer index : printThis)
		{
			System.out.println(selectedGenesInt.get(index)+" : "+selectedGenes.get(index));	
		}
		*/
		
		double[][] matrix = new double[size][size];
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{				
				//System.out.println("("+i+","+j+") = "+totalCoex[selectedGenesInt.get(i)][selectedGenesInt.get(j)]);				
				matrix[i][j]=totalCoex[selectedGenesInt.get(i)][selectedGenesInt.get(j)];				
			}
		}
		
		for(double[] subMAT : matrix)
		{
			for(int i=0; i<subMAT.length; i++)
			{
				
				System.out.print(subMAT[i]+" ");
			}
			System.out.println();
		}
		
		return matrix;
	}

	public static void main(String[] args) throws IOException {

		double[][] coex_total;
		double[][] coex;
		double threshold;
		ArrayList<ArrayList<Integer>> foundConnected;

		String inputpath;
		String filepath;
		List<double[]> coex_list;

		//filepath = "src/resources/10x10coex.csv";
		filepath = "src/resources/coex_noHeader.csv";
		//filepath = "src/resources/coex_noHeader2.csv";
		inputpath = "src/resources/input_test.txt";
		
		//threshold = 0.4753;
		//threshold=0.78;
		threshold=0.795;
		//threshold = 0.69;
		//threshold = 0.8;
		ng = 2;
		NumClique = 0;

		coex_total = loadCoex.Load(filepath);
		//coex = extractGenes("C:/Erica/Research/Autism/JavaAutism/Autism/clique_I_test.txt",coex_total);
		coex = extractGenes("C:/Erica/Research/Autism/JavaAutism/Autism/two_cliques_gene_text.txt",coex_total);
		//Change printClique
		//Change checkNumAutExtract
		
		System.out.println("Size of coex : " + coex.length);
		//System.out.println("Size of coex : " + coex_total.length);
		Aut_index = loadCoex.aut_index_AL;
		Aut_map = loadCoex.aut_index_map;
		total_gname = loadCoex.total_gname;

		coex_list = getClique.putInList(coex, threshold);
		//coex_list = getClique.putInList(coex_total, threshold);
		foundConnected = getClique.FindConnected(coex_list, threshold);
		getClique.createInput(foundConnected, inputpath, coex.length);
		//getClique.createInput(foundConnected, inputpath, coex_total.length);

		BufferedReader bufReader = null;

		File file = new File(inputpath);
		try {
			bufReader = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		VerifyData maxCliqueOpt = new VerifyData();

		try {
			int totalGraphs = maxCliqueOpt.readTotalGraphCount(bufReader);
			for (int i = 0; i < totalGraphs; i++) {
				maxCliqueOpt.constructGraph(bufReader);
				maxCliqueOpt.FindClique();
			}
			System.out.println ("Total Cliques : "+FoundClique.size());
			
			File out = new File ("src/resources/result/found_cliques_opt.txt");
		    PrintWriter printWriter = new PrintWriter (out);
		    printWriter.println ("Total Cliques : "+FoundClique.size());		    
		    for(ArrayList<Integer> clique : FoundClique)
		    {
		    	printWriter.println("size "+clique.size()+ " : "+clique);
		    }
		    printWriter.close (); 

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
