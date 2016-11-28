package wgcnaBF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BF {

	static HashMap<String, Integer> aut_index_map = new HashMap<String, Integer>();
	static ArrayList<Integer> aut_index_AL = new ArrayList<Integer>();
	static ArrayList<ArrayList<Integer>> FinalCliques = new ArrayList<ArrayList<Integer>>();

	static double[][] coex;
	static int ng;
	static double threshold;
	static int NumberOfClique;
	static String total_gpath = "src/resources/gene_name2.csv";
	// static String total_gpath = "gene_name.csv";
	// static String aut26_gpath = "aut26.csv";
	static String aut26_gpath = "src/resources/Aut3.csv";

	public static double[][] Load(String coex_path) throws IOException {

		// String total_gpath = "gene_name.csv";

		// String aut26_gpath = "aut26.csv";

		// Get 3041 total genes
		ArrayList<String> total_gname = new ArrayList<String>();
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
		// System.out.println("Autism gene name = index : ");
		System.out.println(no_aut26 + " Autism Genes : " + Arrays.asList(aut_index_map));

		// Get coex matrix
		// 1. Create coex matrix first by getting its size
		String delimiter = ",";
		int size = 0;
		Scanner coex_scan = new Scanner(new File(coex_path));
		while (coex_scan.hasNextLine()) {
			String line = coex_scan.nextLine();
			String[] fxRatesAsString = line.split(delimiter);
			size = fxRatesAsString.length;
		}
		coex_scan.close();

		int row = 0;
		coex_scan = new Scanner(new File(coex_path));
		double[][] coex = new double[size][size];
		while (coex_scan.hasNextLine()) {
			String line = coex_scan.nextLine();
			String[] fxRatesAsString = line.split(delimiter);
			for (int i = 0; i < fxRatesAsString.length; i++) {
				coex[row][i] = Double.parseDouble(fxRatesAsString[i]);
			}
			row++;
		}
		coex_scan.close();
		/*
		 * System.out.println("\nLoaded Coexpression matrix : "); for (double[]
		 * coex_row : coex) { System.out.println(Arrays.toString(coex_row)); }
		 */
		return coex;
	}

	public static ArrayList<ArrayList<Integer>> FindCliques(double[][] coex) {

		ArrayList<ArrayList<Integer>> cliques = new ArrayList<ArrayList<Integer>>();
		// ArrayList<ArrayList<Integer>> GoodCliques = new
		// ArrayList<ArrayList<Integer>>();
		int coex_size = coex.length;
		boolean isGoodClique;
		for (int i = 0; i < coex_size; i++) // Checking a row
		{
			// For each low, create a tempBigclique
			ArrayList<ArrayList<Integer>> tempBigClique = new ArrayList<ArrayList<Integer>>();
			System.out.println("i=" + i);
			for (int j = i + 1; j < coex_size; j++) { // Start checking a column
				System.out.println("	j=" + j);
				if ((coex[i][j]) > threshold) {
					// Create an arraylist to store the each list for this j
					// value
					ArrayList<Integer> tempClique = new ArrayList<Integer>();
					tempClique.add(i);
					tempClique.add(j);

					for (int k = j + 1; k < coex_size; k++) {
						System.out.println("		k=" + k);
						if ((coex[i][k]) > threshold && (coex[j][k]) > threshold) {
							//tempClique.add(k);
							if (inClique(tempClique, k))
								tempClique.add(k);
							/*
							for (int m = k + 1; m < coex_size;) {
								if (inClique(tempClique, m))
									tempClique.add(m);
							}
							*/
						}
					} // End of 3rd for loop

					System.out.println("		Found Clique (i,j) = (" + i + "," + j + ") : " + tempClique);
					if (tempBigClique.isEmpty()) // is empty if it's the first
													// clique for i and j
					{
						isGoodClique = NumberOfAutism(tempClique);
						if (isGoodClique) {
							tempBigClique.add((ArrayList<Integer>) tempClique.clone());
							/*
							 * for (ArrayList<Integer> each_list :
							 * tempBigClique) { System.out.println(
							 * "each clique in tempBigClique : " + each_list); }
							 */
							cliques.add((ArrayList<Integer>) tempClique.clone());
							System.out
									.println("			GooD Clique of size " + tempClique.size() + " : " + tempClique);
							System.out.println("		" + cliques.size() + " Good cliques found");
						}
					} else { // if there is already a clique saved in
								// tempBigClique
						/*
						 * for (ArrayList<Integer> each_list : tempBigClique) {
						 * System.out.println(
						 * "each clique in tempBigClique (else) : " +
						 * each_list); }
						 */
						boolean isPart = isSubClique(tempBigClique, tempClique);
						System.out.println("Is it Part of it? " + isPart);
						isGoodClique = NumberOfAutism(tempClique);
						System.out.println("Is it a good clique of it? " + isGoodClique);
						if (isPart == true || isGoodClique == false) {
							System.out.println("Not a good clique. Do not add");
						} else {
							System.out
									.println("			Good Clique of size " + tempClique.size() + " : " + tempClique);
							cliques.add((ArrayList<Integer>) tempClique.clone());
							tempBigClique.add((ArrayList<Integer>) tempClique.clone());
							System.out.println("		" + cliques.size() + " Good cliques found");
						}
					} // end of else, there is already a clique.
					tempClique.clear();
					tempClique.add(i);
					/*
					 * if (NumberOfAutism(tempClique)) { NumberOfClique++;
					 * cliques.add(tempClique); System.out.println(
					 * "			Good Clique : " + tempClique);
					 * tempClique.clear(); tempClique.add(i); }
					 */
					// }
				} // End of if
			} // End of 2nd for loop
				// System.out.println("Finished " + i);
			tempBigClique.clear();
		}
		/*
		 * System.out.println("erica testing "+cliques.size());
		 * for(ArrayList<Integer> each_clique : cliques) {
		 * System.out.println(each_clique); }
		 */
		return cliques;
	}

	private static boolean isSubClique(ArrayList<ArrayList<Integer>> tempBigClique, ArrayList<Integer> tempClique) {
		// System.out.println("check this clique : " + tempClique);
		for (ArrayList<Integer> each_list : tempBigClique) {
			// System.out.println("each clique in tempBigClique : " +
			// each_list);
			if (each_list.containsAll((tempClique)))
				return true;
		}
		return false;
	}

	private static boolean NumberOfAutism(ArrayList<Integer> tempClique) {

		ArrayList<Integer> foundAut = new ArrayList<Integer>();
		int count = 0;
		for (Integer gene : tempClique) {
			if (aut_index_AL.contains(gene)) {
				count++;
				foundAut.add(gene);
			}

			// if(count>ng)
			if (count >= ng) {
				// System.out.println("Found "+count+" Autism gene of index " +
				// foundAut);
				return true;
			}
		}
		return false;

	}

	private static boolean inClique(ArrayList<Integer> tempClique, int k) {
		for (int i = 0; i < tempClique.size(); i++) {
			int check_w_this_gene = tempClique.get(i);
			if (coex[check_w_this_gene][k] < threshold) {
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) throws IOException {

		threshold = 0.4785;
		// threshold = 0.70;
		ng = -1;
		String filepath;
		// filepath = "5x5coex.csv";
		filepath = "src/resources/10x10coex.csv";
		// filepath = "coex_noHeader.csv";
		// filepath = "src/resources/coex_noHeader2.csv";
		ArrayList<ArrayList<Integer>> cliques = new ArrayList<ArrayList<Integer>>();
		NumberOfClique = 0;
		coex = Load(filepath);
		System.out.println("Size of coex : " + coex.length);

		cliques = FindCliques(coex);
		System.out.println("\nFound " + cliques.size() + " GOOD cliques");
		/*
		 * for (ArrayList<Integer> each_clique : cliques) {
		 * System.out.println(each_clique); }
		 */

		deleteDuplicates(cliques);
		System.out.println("\nFound " + cliques.size() + " GOOD cliques");
		for (ArrayList<Integer> each_clique : cliques) {
			System.out.println("size : " + each_clique.size() + " : " + each_clique);
		}

		File out = new File("result/found_cliques_BF.txt");
		PrintWriter printWriter = new PrintWriter(out);
		printWriter.println("Total Cliques : " + cliques.size());

		for (ArrayList<Integer> clique : cliques) {
			printWriter.println("size " + clique.size() + " : " + clique);
		}
		printWriter.close();
	}

	private static void deleteDuplicates(ArrayList<ArrayList<Integer>> cliques) {
		// ArrayList<ArrayList<Integer>> finalCliques = new
		// ArrayList<ArrayList<Integer>>();
		int size = cliques.size();
		boolean Break = false;
		int jump;
		for (int i = size - 1; i >= 0; i--) {
			jump = 0;
			ArrayList<Integer> L1 = cliques.get(i); // last row
			Break = false;
			// System.out.println("for i "+i);
			for (int j = i - 1; j >= 0 && !Break; j--) {
				// System.out.println("for j "+j);
				ArrayList<Integer> L2 = cliques.get(j);
				if (L2.containsAll(L1)) {
					// System.out.println(L1+" removed.");
					cliques.remove(i);
					jump++;
					Break = true;
				}
				j = j - jump;
			}
		}

		System.out.println("Duplicates cleared");
	}
}
