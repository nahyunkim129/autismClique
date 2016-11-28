package filterFoundCliques;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class FilterFoundClique {
	static HashMap<String, Integer> aut_index_map = new HashMap<String, Integer>();
	static ArrayList<Integer> aut_index_AL = new ArrayList<Integer>();
	static ArrayList<String> total_gname = new ArrayList<String>();
	static ArrayList<ArrayList<String>> maxClique = new ArrayList<ArrayList<String>>();
	static Integer ng = 2;

	public static void filterByThreshold(String clique_path) throws IOException {

		String total_gpath = "src/resources/gene_name.csv";
		String aut26_gpath = "src/resources/aut26.csv";

		// Get 3041 total genes
		// ArrayList<String> total_gname = new ArrayList<String>();
		Scanner all_gscan = new Scanner(new File(total_gpath));
		int no_total = 0;
		while (all_gscan.hasNextLine()) {
			String line = all_gscan.nextLine();
			String[] lineArray = line.split(" ");
			total_gname.add(new String(lineArray[0]));
			no_total++;
		}
		System.out.println(no_total + " total genes : " + total_gname);
		all_gscan.close();

		// Get 26 selected autism genes
		ArrayList<String> aut_26 = new ArrayList<String>();
		Scanner aut26_gscan = new Scanner(new File(aut26_gpath));
		int no_aut26 = 0;
		while (aut26_gscan.hasNextLine()) {
			String line = aut26_gscan.nextLine();
			String[] lineArray = line.split(" ");
			aut_26.add(new String(lineArray[0]));
			no_aut26++;
		}
		// System.out.println(no_aut26 + " Autism Genes : " + aut_26);
		aut26_gscan.close();

		// Get 26 autism gene index
		for (int i = 0; i < aut_26.size(); i++) {
			aut_index_map.put(aut_26.get(i), total_gname.indexOf(aut_26.get(i)));
			aut_index_AL.add(total_gname.indexOf(aut_26.get(i)));

		}
		System.out.println("Autism gene name = index : " + aut_index_AL);
		System.out.println(no_aut26 + " Autism Genes : " + Arrays.asList(aut_index_map));

		// Get found cliques.
		String delimiter = ",";
		int rowNum = 0;
		int columnNum = 0;
		Scanner clique_scan = new Scanner(new File(clique_path));
		while (clique_scan.hasNextLine()) {
			rowNum++;
			String line = clique_scan.nextLine();
			String[] AsString = line.split(delimiter);
			columnNum = AsString.length;

		}
		clique_scan.close();
		double final_th = (double) (101 - columnNum) / 100;
		System.out.println("Loaded number of genes : " + rowNum);
		System.out.println("Loaded number of threshold: " + columnNum + " from 1 to " + final_th);
		System.out.println("");
		clique_scan = new Scanner(new File(clique_path));
		double[][] cliques = new double[rowNum][columnNum];
		int row = 0;
		while (clique_scan.hasNextLine()) {
			String line = clique_scan.nextLine();
			String[] ToString = line.split(delimiter);
			for (int i = 0; i < ToString.length; i++) {
				cliques[row][i] = Double.parseDouble(ToString[i]);
			}
			row++;
		}
		clique_scan.close();

		/*
		 * This is to print the double[][] matrix for(double[] subMAT : cliques)
		 * { for(int i=0; i<subMAT.length; i++) {
		 * 
		 * System.out.print(subMAT[i]+" "); } System.out.println(); }
		 */

		double threshold;
		for (threshold = 0.47; threshold < 0.99; threshold = threshold + 0.01) {
			int thToIndex = (int) (101 - (threshold * 100)) - 1;
			HashMap<Integer, ArrayList<Integer>> groupCliques = new HashMap<Integer, ArrayList<Integer>>();
			int cliqueIndex;
			for (int gene = 0; gene < rowNum; gene++) {
				// System.out.println(gene);
				ArrayList<Integer> temp = new ArrayList<Integer>();
				cliqueIndex = (int) (cliques[gene][thToIndex]);
				if (groupCliques.get(cliqueIndex) == null) {
					temp.add(gene);
					groupCliques.put(cliqueIndex, temp);
				} else {
					groupCliques.get(cliqueIndex).add(gene);
				}
			}
			System.out.println("Loaded threshold " + threshold);
			System.out.println("Loaded column " + thToIndex);
			System.out.println("Total number of cliques : " + groupCliques.size());

			for (HashMap.Entry<Integer, ArrayList<Integer>> entry : groupCliques.entrySet()) {
				if (entry.getValue().size() >= ng) {

					if (NumAut(entry.getValue()))
						System.out.println(
								entry.getKey() + " of size " + entry.getValue().size() + " : " + entry.getValue());
				}

			}
			System.out.println("");
		}

	}

	public static void MaxClique(String clique_path) throws IOException {

		String total_gpath = "src/resources/gene_name.csv";
		String aut26_gpath = "src/resources/aut26.csv";

		// Get 3041 total genes
		// ArrayList<String> total_gname = new ArrayList<String>();
		Scanner all_gscan = new Scanner(new File(total_gpath));
		int no_total = 0;
		while (all_gscan.hasNextLine()) {
			String line = all_gscan.nextLine();
			String[] lineArray = line.split(" ");
			total_gname.add(new String(lineArray[0]));
			no_total++;
		}
		System.out.println(no_total + " total genes : " + total_gname);
		all_gscan.close();

		// Get 26 selected autism genes
		ArrayList<String> aut_26 = new ArrayList<String>();
		Scanner aut26_gscan = new Scanner(new File(aut26_gpath));
		int no_aut26 = 0;
		while (aut26_gscan.hasNextLine()) {
			String line = aut26_gscan.nextLine();
			String[] lineArray = line.split(" ");
			aut_26.add(new String(lineArray[0]));
			no_aut26++;
		}
		// System.out.println(no_aut26 + " Autism Genes : " + aut_26);
		aut26_gscan.close();

		// Get 26 autism gene index
		for (int i = 0; i < aut_26.size(); i++) {
			aut_index_map.put(aut_26.get(i), total_gname.indexOf(aut_26.get(i)));
			aut_index_AL.add(total_gname.indexOf(aut_26.get(i)));

		}
		System.out.println("Autism gene name = index : " + aut_index_AL);
		System.out.println(no_aut26 + " Autism Genes : " + Arrays.asList(aut_index_map));

		// Get found cliques.
		String delimiter = ",";
		int rowNum = 0;
		int columnNum = 0;
		Scanner clique_scan = new Scanner(new File(clique_path));
		while (clique_scan.hasNextLine()) {
			rowNum++;
			String line = clique_scan.nextLine();
			String[] AsString = line.split(delimiter);
			columnNum = AsString.length;

		}
		clique_scan.close();
		System.out.println("Loaded number of genes : " + rowNum);
		System.out.println("");
		clique_scan = new Scanner(new File(clique_path));
		int[][] cliques = new int[rowNum][columnNum];
		int row = 0;
		while (clique_scan.hasNextLine()) {
			String line = clique_scan.nextLine();
			String[] ToString = line.split(delimiter);
			for (int i = 0; i < ToString.length; i++) {

				cliques[row][i] = Integer.parseInt(ToString[i]);
			}
			row++;
		}
		clique_scan.close();

		System.out.println("Total " + columnNum + " cliques have been found");
		for (int j = 0; j < columnNum; j++) {
			
			ArrayList<String> temp_name = new ArrayList<String>();
			ArrayList<Integer> temp = new ArrayList<Integer>();
			//System.out.print("Checking "+j+" clique of size ");
			
			for (int i = 0; i < rowNum; i++) {
				
				if (cliques[i][j] == 1) {
					temp_name.add(total_gname.get(cliques[i][j]));
					temp.add(i);
				}
			}
			//System.out.println(temp.size());
			if ((temp.size() >= 2) && NumAut(temp)) {				
				maxClique.add(temp_name);
				System.out.print("Clique " + j+" ");
				System.out.println(temp);
			}
		}
		
		System.out.println("Number of good cliques : "+maxClique.size());
		
	}

	private static boolean NumAut(ArrayList<Integer> f_clique) {

		ArrayList<Integer> temp_aut_gene = new ArrayList<Integer>();
		ArrayList<String> temp_aut_gene_name = new ArrayList<String>();
		int count = 0;
		String aut_name;

		for (Integer gene : f_clique) {
			if (aut_index_AL.contains(gene)) {
				count++;
				temp_aut_gene.add(gene);
				aut_name = total_gname.get(gene);
				temp_aut_gene_name.add(aut_name);
				//System.out.print("		" + aut_name);
			}
		}

		if (count >= ng) {
			//System.out.println("	Found " + count + " Autism gene " + temp_aut_gene);
			System.out.println("	Found " + count + " Autism gene " + temp_aut_gene_name);
			return true;
		} else
			return false;
	}
	
	private static void deleteDuplicates(ArrayList<ArrayList<String>> maxClique2) {
		// ArrayList<ArrayList<Integer>> finalCliques = new
		// ArrayList<ArrayList<Integer>>();
		int size = maxClique2.size();
		boolean Break = false;
		int jump;
		for (int i = size - 1; i >= 0; i--) {
			jump = 0;
			ArrayList<String> L1 = maxClique2.get(i); // last row
			Break = false;
			// System.out.println("for i "+i);
			for (int j = i - 1; j >= 0 && !Break; j--) {
				// System.out.println("for j "+j);
				ArrayList<String> L2 = maxClique2.get(j);
				if (L2.containsAll(L1)) {
					// System.out.println(L1+" removed.");
					maxClique2.remove(i);
					jump++;
					Break = true;
				}
				j = j - jump;
			}
		}

		System.out.println("Duplicates cleared");
	}


	public static void main(String args[]) throws IOException {
		//filterByThreshold("src/resources/elsclique_4735.csv");
		MaxClique("src/resources/elsclique_4735.csv");
		deleteDuplicates(maxClique);
		for(ArrayList<String> eachMax : maxClique)
		{
			System.out.println("size "+eachMax.size()+" : "+eachMax);
		}
	}
}
