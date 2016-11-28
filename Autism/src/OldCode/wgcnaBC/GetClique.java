package wgcnaBC;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GetClique {

	static LoadData loadCoex = new LoadData();

	public ArrayList<Integer> FilterByAutism(ArrayList<Integer> Aut_index_al, ArrayList<ArrayList<Integer>> connected,
			int NumAutThrsh) throws IOException {
		ArrayList<Integer> FilteredIndex = new ArrayList<Integer>();
		for (int i = 0; i < connected.size(); i++) {
			ArrayList<Integer> sub_connected = connected.get(i);
			int count = 0;
			if (Aut_index_al.contains(i))
				count++;
			for (int j = 0; j < sub_connected.size(); j++) {
				if (Aut_index_al.contains(sub_connected.get(j)))
					count++;
			}

			if (count < NumAutThrsh) {
			} else {
				System.out.println("count for " + i + " : " + count);
				FilteredIndex.add(i);
			}
		}
		return FilteredIndex;
	}

	public double[][] Filter(double[][] matrix, double threshold) {
		// System.out.println("\nFiltered coexpression matrix : ");
		for (double[] matrix_row : matrix) {
			for (int i = 0; i < matrix_row.length; i++) {
				if (matrix_row[i] < threshold)
					matrix_row[i] = 0.0;
			}
			// System.out.println(Arrays.toString(matrix_row));
		}
		return matrix;
	}

	public List<boolean[]> FindCliques(double[][] matrix) {

		List<boolean[]> cliques = new ArrayList<boolean[]>();
		int size = matrix.length;
		/*
		 * cliques.add(new boolean[matrix.length]); boolean[] firstClique =
		 * cliques.get(0); Arrays.fill(firstClique, Boolean.FALSE);
		 * firstClique[0]=false;
		 */

		for (double[] matrix_row : matrix) {
			boolean[] temp = new boolean[size];
			Arrays.fill(temp, Boolean.FALSE);
			for (int j = 0; j < size; j++) { // column
				if (matrix_row[j] != 0.0)
					temp[j] = true;
			}
			cliques.add(temp);
		}
		for (int i = 0; i < cliques.size(); i++) {
			System.out.println(Arrays.toString(cliques.get(i)));
		}
		System.out.println("\nBefore : ");
		for (int i = 0; i < cliques.size(); i++) {
			System.out.println(Arrays.toString(cliques.get(i)));
		}

		for (int i = 0; i < size; i++) {
			boolean[] this_clique = cliques.get(i);
			for (int j = 0; j < size; j++) { // column
				if (this_clique[j]) {
					boolean[] check = cliques.get(j);
					if (!check[i])
						this_clique[j] = false;
				} else {
				}
			}
		}
		System.out.println("\nAfter : ");
		for (int i = 0; i < cliques.size(); i++) {
			System.out.println(Arrays.toString(cliques.get(i)));
		}

		return cliques;
	}

	public List<int[]> Integerize(double[][] matrix) {

		List<int[]> cliques = new ArrayList<int[]>();
		int size = matrix.length;
		/*
		 * cliques.add(new boolean[matrix.length]); boolean[] firstClique =
		 * cliques.get(0); Arrays.fill(firstClique, Boolean.FALSE);
		 * firstClique[0]=false;
		 */

		for (double[] matrix_row : matrix) {
			int[] temp = new int[size];
			Arrays.fill(temp, 0);
			for (int j = 0; j < size; j++) { // column
				if (matrix_row[j] != 0.0)
					temp[j] = 1;
			}
			cliques.add(temp);
		}

		/*
		 * for (int i = 0; i < cliques.size(); i++) { System.out.println(i+" : "
		 * +Arrays.toString(cliques.get(i))); }
		 */

		/*
		 * for (int i=0; i<size; i++) { int[] this_clique = cliques.get(i); for
		 * (int j = 0; j < size; j++) { // column if (this_clique[j]==1) { int[]
		 * check = cliques.get(j); if(check[i]==0) this_clique[j]=0; } else{} }
		 * } System.out.println("\nAfter : "); for (int i = 0; i <
		 * cliques.size(); i++) {
		 * System.out.println(Arrays.toString(cliques.get(i))); }
		 */
		return cliques;
	}

	// public List<int[]> findClique(List<int[]> Lists){
	public ArrayList<ArrayList<Integer>> FindConnected(List<double[]> Lists, double threshold)
			throws FileNotFoundException, UnsupportedEncodingException {

		ArrayList<ArrayList<Integer>> IndexLists = new ArrayList<ArrayList<Integer>>();
		for (int k = 0; k < Lists.size(); k++) {
			double[] List = Lists.get(k);
			double min_threshold = 1;
			ArrayList<Integer> index = new ArrayList<Integer>();
			for (int i = k; i < List.length; i++) {
				if (List[i] > threshold && i != k) {
					if (List[i] < min_threshold)
						min_threshold = List[i];
					index.add(i);
				}
			}
			IndexLists.add(index);
		}
/*
		System.out.println("\nConnected components : ");
		for (int i = 0; i < IndexLists.size(); i++) {
			System.out.println(i + " : " + (IndexLists.get(i)));
		}
*/
		System.out.println("Finished finding connection");
		return IndexLists;
	}

	public void createInput(ArrayList<ArrayList<Integer>> connected, String filename, int NumNode)
			throws FileNotFoundException, UnsupportedEncodingException {

		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		int edges = 0;
		writer.println(NumNode);		
		for (int i = 0; i < connected.size(); i++) {
			ArrayList<Integer> sub = connected.get(i);
			for (int j = 0; j < sub.size(); j++) {
				writer.println(i + " " + sub.get(j)); // 3rd line
				writer.println(sub.get(j) + " " + i); // 3rd line
			}
		}
		writer.close();
	}

	public List<double[]> putInList(double[][] coex, double threshold) {
		// TODO Auto-generated method stub

		List<double[]> inList = new ArrayList<double[]>();
		int size = coex.length;

		for (double[] matrix_row : coex) {
			double[] temp = new double[size];
			Arrays.fill(temp, 0);
			for (int j = 0; j < size; j++) { // column
				if (matrix_row[j] > threshold)
					temp[j] = matrix_row[j];
			}
			inList.add(temp);
		}

		return inList;
	}

}
