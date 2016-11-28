package wgcna;

import java.io.BufferedReader; 
import java.io.File; 
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List; 

public class FindCliques {
	
    int nodesCount; 
    static int NumClique;
    static int NumGoodClique=0;
    static int loopcount;
    static int ng;
    ArrayList<Node> graph = new ArrayList<Node>(); 	
	ArrayList<ArrayList<Integer>> FoundClique = new ArrayList<ArrayList<Integer>>();
	static ArrayList<Integer> Aut_index = new ArrayList<Integer>();
	static LoadData loadCoex = new LoadData();
	static GetClique getClique = new GetClique();		
	    
    class Node implements Comparable<Node> {
        int x; 
        int degree; 
        ArrayList<Node> nbrs = new ArrayList<Node>(); 

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

        public ArrayList<Node> getNbrs() { 
            return nbrs; 
        } 

        public void setNbrs(ArrayList<Node> nbrs) {
            this.nbrs = nbrs; 
        } 

        public void addNbr(Node y) {
            this.nbrs.add(y); 
            if (!y.getNbrs().contains(y)) { 
                y.getNbrs().add(this); 
                y.degree++; 
            } 
            this.degree++; 

        } 

        public void removeNbr(Node y) {
            this.nbrs.remove(y); 
            if (y.getNbrs().contains(y)) { 
                y.getNbrs().remove(this); 
                y.degree--; 
            } 
            this.degree--; 

        } 

        @Override 
        public int compareTo(Node o) {
            if (this.degree < o.degree) {
                return -1; 
            } 
            if (this.degree > o.degree) {
                return 1;
            } 
            return 0; 
        } 
    } 

    void initGraph() { 
        graph.clear(); 
        for (int i = 0; i < nodesCount; i++) {
            Node V = new Node(); 
            V.setX(i); 
            graph.add(V); 
        } 
    } 

    int readTotalGraphCount(BufferedReader bufReader) throws Exception {

        return Integer.parseInt(bufReader.readLine()); 
    } 

    // Reads Input 
    void readGraph(BufferedReader bufReader) throws Exception {
        try { 
            nodesCount = Integer.parseInt(bufReader.readLine()); 
            int edgesCount = Integer.parseInt(bufReader.readLine());
            initGraph(); 

            for (int k = 0; k < edgesCount; k++) {
                String[] strArr = bufReader.readLine().split(" "); 
                int u = Integer.parseInt(strArr[0]); //First node
                int v = Integer.parseInt(strArr[1]); //Second node
                Node vertU = graph.get(u); 
                Node vertV = graph.get(v); 
                vertU.addNbr(vertV); 

            } 

        } catch (Exception e) { 
            e.printStackTrace(); 
            throw e; 
        } 
    } 

    // Finds nbr of Node i 
    ArrayList<Node> getNbrs(Node v) { 
        int i = v.getX(); 
        return graph.get(i).nbrs; 
    } 

    // Intersection of two sets 
    ArrayList<Node> intersect(ArrayList<Node> arlFirst, 
            ArrayList<Node> arlSecond) { 
        ArrayList<Node> arlHold = new ArrayList<Node>(arlFirst); 
        arlHold.retainAll(arlSecond); 
        return arlHold; 
    } 

    // Union of two sets 
    ArrayList<Node> union(ArrayList<Node> arlFirst, 
            ArrayList<Node> arlSecond) { 
        ArrayList<Node> arlHold = new ArrayList<Node>(arlFirst); 
        arlHold.addAll(arlSecond); 
        return arlHold; 
    } 

    // Removes the neighbors 
    ArrayList<Node> removeNbrs(ArrayList<Node> arlFirst, Node v) { 
        ArrayList<Node> arlHold = new ArrayList<Node>(arlFirst); 
        arlHold.removeAll(v.getNbrs()); 
        return arlHold; 
    } 

    void isClique(ArrayList<Node> Clique, ArrayList<Node> AllGeneArray,
            ArrayList<Node> ExcludedGeneArray, String pre) { 
    	loopcount++;
        //System.out.print(pre + " " + printSet(R) + ", " + printSet(P) + ", "+ printSet(X)); 
    	if(loopcount==10000000||loopcount==20000000) System.out.println("Still finding cliques...");
        if ((AllGeneArray.size() == 0) && (ExcludedGeneArray.size() == 0)) {
        	//System.out.println("Number of loops taken : "+loopcount);        	
        	
        	NumClique++;
        	if(checkNumAut(Clique,ng)) 
        	{        		        		
        		printClique(Clique);
        	}
        	else{
        		System.out.println(" Skipping this clique. ");
        	}
        	
        	//printClique(R);
            return; 
        }  

        ArrayList<Node> EachGeneArray = new ArrayList<Node>(AllGeneArray); 

        for (Node v : AllGeneArray) { 
        	Clique.add(v); 
            isClique(Clique, intersect(EachGeneArray, getNbrs(v)), 
                    intersect(ExcludedGeneArray, getNbrs(v)), pre + "\t"); 
            Clique.remove(v); 
            EachGeneArray.remove(v); 
            ExcludedGeneArray.add(v); 
        } 
    } 

    private boolean checkNumAut(ArrayList<Node> clique, int ng) {
    	//System.out.println(Aut_index+" and ng is "+ng);
    	System.out.print(NumClique+". Clique found. Autism Genes >= "+ng+"? : ");
    	int count = 0;  
        for (Node v : clique) { 
        	if(Aut_index.contains(v.getX())) 
        	{
        		count++;
        		System.out.println("\n Found "+count+" Autism gene of index " + v.getX());
        	}
            
            //if(count>ng)
            if(count>=0)
            {            	
            	return true;
            }
        } 
        
        System.out.println("False.");
		return false;
	}

    void printClique(ArrayList<Node> R) { 
    	NumGoodClique++;
    	ArrayList<Integer> temp = new ArrayList<Integer>();
    	
        System.out.print("	"+NumGoodClique+" Good Clique : "); 
        for (Node v : R) { 
            System.out.print(" " + (v.getX())); 
            temp.add(v.getX());
        } 
        FoundClique.add(temp);
        System.out.println();                 
        
    }     

	void findClique() { 

        ArrayList<Node> X = new ArrayList<Node>(); 
        ArrayList<Node> R = new ArrayList<Node>(); 
        ArrayList<Node> P = new ArrayList<Node>(graph); 
        isClique(R, P, X, ""); 
    } 

    String printSet(ArrayList<Node> Y) { 
        StringBuilder strBuild = new StringBuilder(); 

        strBuild.append("{"); 
        for (Node v : Y) { 
            strBuild.append("" + (v.getX()) + ","); 
        } 
        if (strBuild.length() != 1) {
            strBuild.setLength(strBuild.length() - 1); 
        } 
        strBuild.append("}"); 
        return strBuild.toString(); 
    } 

    public static void main(String[] args) throws IOException {
    	    	
    	double[][] coex;
    	double threshold;
    	ArrayList<ArrayList<Integer>> foundConnected;
    	
    	String inputpath;	
    	String filepath;
    	List<double[]> coex_list;
    	
		//filepath = "src/resources/10x10coex.csv";
    	//filepath = "src/resources/6x6coex.csv";
    	filepath = "src/resources/7x7coex.csv";
		//filepath = "coex_noHeader.csv";
    	//filepath = "src/resources/coex_noHeader2.csv";
		inputpath = "src/resources/input_test_nop.txt";
		threshold = 0.4875;
		ng=0;
		NumClique=0;
		
		coex = loadCoex.Load(filepath);
		System.out.println("Size of coex : "+coex.length);
		Aut_index = loadCoex.aut_index_AL;
		//System.out.println("Index of Autism Genes : "+Aut_index); //only the index	
		coex_list = getClique.putInList(coex, threshold);
		foundConnected = getClique.FindConnected(coex_list, threshold);
		getClique.createInput(foundConnected, inputpath, coex.length);
		
        BufferedReader bufReader = null; 
        File file = new File(inputpath); //read input data
        try { 
            bufReader = new BufferedReader(new FileReader(file));
        } catch (Exception e) { 
            e.printStackTrace(); 
            return; 
        } ]
        
        FindCliques ff = new FindCliques();
        System.out.println("\n Find Cliques : "); 
        try { 
            int totalGraphs = ff.readTotalGraphCount(bufReader);
            for (int i = 0; i < totalGraphs; i++) {                
                ff.readGraph(bufReader); 
                ff.findClique(); 
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
