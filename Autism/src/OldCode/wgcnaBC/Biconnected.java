package wgcnaBC;

//A Java program to find biconnected components in a given
//undirected graph
import java.io.*;
import java.util.*;

//This class represents a directed graph using adjacency
//list representation
public class Biconnected {
	private int V, E; // No. of vertices & Edges respectively
	private LinkedList<Integer> adj[]; // Adjacency List

	int nodesCount;
	static int NumClique;
	static int NumGoodClique = 0;
	static int loopcount;
	static int ng;

	static ArrayList<ArrayList<Integer>> FoundClique = new ArrayList<ArrayList<Integer>>();
	static ArrayList<Integer> Aut_index = new ArrayList<Integer>();
	static HashMap<String, Integer> Aut_map = new HashMap<String, Integer>();

	static ArrayList<String> selectedGenes = new ArrayList<String>();
	static ArrayList<ArrayList<Integer>> validCliques = new ArrayList<ArrayList<Integer>>();
	

	static LoadData loadCoex = new LoadData();
	static GetClique getClique = new GetClique();

	// Count is number of biconnected components. time is
	// used to find discovery times
	int count = 0;
	static int time = 0;

	class Edge {
		int u;
		int v;

		Edge(int u, int v) {
			this.u = u;
			this.v = v;
		}
	};

	// Constructor
	Biconnected(int v) {
		V = v;
		E = 0;
		adj = new LinkedList[v];
		for (int i = 0; i < v; ++i)
			adj[i] = new LinkedList();
	}

	// Function to add an edge into the graph
	void addEdge(int v, int w) {
		adj[v].add(w);
		E++;
	}

	// A recursive function that finds and prints strongly connected
	// components using DFS traversal
	// u --> The vertex to be visited next
	// disc[] --> Stores discovery times of visited vertices
	// low[] -- >> earliest visited vertex (the vertex with minimum
	// discovery time) that can be reached from subtree
	// rooted with current vertex
	// *st -- >> To store visited edges
	void BCCUtil(int u, int disc[], int low[], LinkedList<Edge> st, int parent[]) {

		// Initialize discovery time and low value
		disc[u] = low[u] = ++time;
		int children = 0;

		// Go through all vertices adjacent to this
		Iterator<Integer> it = adj[u].iterator();
		while (it.hasNext()) {
			int v = it.next(); // v is current adjacent of 'u'

			// If v is not visited yet, then recur for it
			if (disc[v] == -1) {
				children++;
				parent[v] = u;

				// store the edge in stack
				st.add(new Edge(u, v));
				BCCUtil(v, disc, low, st, parent);

				// Check if the subtree rooted with 'v' has a
				// connection to one of the ancestors of 'u'
				// Case 1 -- per Strongly Connected Components Article
				if (low[u] > low[v])
					low[u] = low[v];

				// If u is an articulation point,
				// pop all edges from stack till u -- v
				if ((disc[u] == 1 && children > 1) || (disc[u] > 1 && low[v] >= disc[u])) {
					ArrayList<Integer> tempFoundClique = new ArrayList<Integer>();
					while (st.getLast().u != u || st.getLast().v != v) {
						int u_value = st.getLast().u;
						int v_value = st.getLast().v;
						System.out.print(u_value + " + " + v_value+ " ");
						if(!tempFoundClique.contains(u_value)) tempFoundClique.add(u_value);
						if(!tempFoundClique.contains(v_value)) tempFoundClique.add(v_value);
						st.removeLast();
												
					}
					if(!tempFoundClique.isEmpty()) validCliques.add(tempFoundClique);
					System.out.println(st.getLast().u + " ++ " + st.getLast().v + "  ");
					st.removeLast();

					count++;
				}
			}

			// Update low value of 'u' only of 'v' is still in stack
			// (i.e. it's a back edge, not cross edge).
			// Case 2 -- per Strongly Connected Components Article
			else if (v != parent[u] && disc[v] < low[u]) {
				if (low[u] > disc[v])
					low[u] = disc[v];
				st.add(new Edge(u, v));
			}
		}
	}

	public static double[][] extractGenes(String PathSelected, double[][] totalCoex) throws FileNotFoundException {

		// Read selected gene names and save in selectedGenes

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
		for (int i = 0; i < size; i++) {
			// aut_index_map.put(aut_26.get(i),
			// total_gname.indexOf(aut_26.get(i)));
			selectedGenesInt.add(loadCoex.total_gname.indexOf(selectedGenes.get(i)));

		}
		// Collections.sort(selectedGenesInt);
		System.out.println(size + " selected genes : " + selectedGenesInt);

		int[] printThis = { 2, 30, 33, 34, 35, 36 };
		for (Integer index : printThis) {
			System.out.println(selectedGenesInt.get(index) + " : " + selectedGenes.get(index));
		}

		double[][] matrix = new double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// System.out.println("("+i+","+j+") =
				// "+totalCoex[selectedGenesInt.get(i)][selectedGenesInt.get(j)]);
				matrix[i][j] = totalCoex[selectedGenesInt.get(i)][selectedGenesInt.get(j)];
			}
		}

		for (double[] subMAT : matrix) {
			for (int i = 0; i < subMAT.length; i++) {

				System.out.print(subMAT[i] + " ");
			}
			System.out.println();
		}

		return matrix;
	}

	// The function to do DFS traversal. It uses BCCUtil()
	void BCC() throws FileNotFoundException {
		int disc[] = new int[V];
		int low[] = new int[V];
		int parent[] = new int[V];
		LinkedList<Edge> st = new LinkedList<Edge>();

		// Initialize disc and low, and parent arrays
		for (int i = 0; i < V; i++) {
			disc[i] = -1;
			low[i] = -1;
			parent[i] = -1;
		}

		for (int i = 0; i < V; i++) {
			if (disc[i] == -1)
				BCCUtil(i, disc, low, st, parent);

			int j = 0;

			/*
			 * File out = new File
			 * ("src/resources/result/found_cliques_bi_strongly.txt");
			 * PrintWriter printWriter = new PrintWriter (out);
			 * printWriter.println ("Total Cliques : "+FoundClique.size());
			 * for(ArrayList<Integer> clique : FoundClique) {
			 * printWriter.println("size "+clique.size()+ " : "+clique); }
			 * printWriter.close ();
			 */
			// If stack is not empty, pop all edges from stack
			ArrayList<Integer> strong_clique = new ArrayList<Integer>();

			while (st.size() > 0) {
				j = 1;
				int u_val = st.getLast().u;
				int v_val = st.getLast().v;
				System.out.print(u_val + "-" + v_val + " ");
				st.removeLast();
				// if(!strong_clique.contains(u_val))
				strong_clique.add(u_val);
			}

			//System.out.println("Found Strong clique : "+strong_clique);

			if (j == 1) {
				System.out.println();
				count++;
			}
		}
	}

	public static void main(String args[]) throws IOException {

		double[][] coex_total;
		double[][] coex;
		double threshold;
		ArrayList<ArrayList<Integer>> foundConnected;

		String inputpath;
		String filepath;
		List<double[]> coex_list;

		// filepath = "src/resources/10x10coex.csv";
		filepath = "src/resources/coex_noHeader.csv";
		//filepath = "src/resources/coex_noHeader2.csv";
		//inputpath = "src/resources/input_test_Bi.txt";
		inputpath = "src/resources/input_test_Bi.txt";
		
		threshold = 0.00;
		// threshold=0.795;
		// threshold=0.999;
		// threshold = 0.704;
		// threshold = 0.8;
		ng = 2;
		NumClique = 0;

		coex = loadCoex.Load(filepath);
		// extractGenes("C:/Erica/Research/Autism/JavaAutism/Autism/two_cliques_gene_text.txt",coex_total);
		System.out.println("Size of coex : " + coex.length);
		Aut_index = loadCoex.aut_index_AL;
		Aut_map = loadCoex.aut_index_map;

		for (threshold = 0.704; threshold < 1.01; threshold = threshold + 0.01) {
			validCliques.clear();
			System.out.println();
			System.out.println("Threshold : " + threshold);
			coex_list = getClique.putInList(coex, threshold);
			foundConnected = getClique.FindConnected(coex_list, threshold);
			getClique.createInput(foundConnected, inputpath, coex.length);

			BufferedReader bufReader = null;

			File file = new File(inputpath);
			try {
				bufReader = new BufferedReader(new FileReader(file));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			int edgeCount = Integer.parseInt(bufReader.readLine());
			Biconnected g = new Biconnected(edgeCount);
			// constructGraph(bufReader, edgeCou nt);
			for (String line = bufReader.readLine(); line != null; line = bufReader.readLine()) {
				String[] strArr = line.split(" ");
				int u = Integer.parseInt(strArr[0]);
				int v = Integer.parseInt(strArr[1]);
				g.addEdge(u, v);
			}

			/*
			 * File out = new
			 * File("src/resources/result/found_cliques_opt.txt"); PrintWriter
			 * printWriter = new PrintWriter(out); printWriter.println(
			 * "Total Cliques : " + FoundClique.size()); System.out.println(
			 * "Total Cliques : " + FoundClique.size()); for (ArrayList<Integer>
			 * clique : FoundClique) { printWriter.println("size " +
			 * clique.size() + " : " + clique); } printWriter.close();
			 */
			bufReader.close();
			g.BCC();

			System.out.println("Above are " + g.count + " biconnected components in graph");			
			System.out.println("Valid clique for threshold: "+threshold);
			for(ArrayList<Integer> validClique : validCliques)
			{
				Collections.sort(validClique);
				System.out.println("Size of "+validClique.size()+" : "+ validClique);	
			}
			System.out.println();
			System.out.println();
		}
	}

	private static void constructGraph(BufferedReader bufReader, int edgesCount) {

		// initGraph();
		// initOptGraph();

	}
}