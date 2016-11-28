

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetClique {

	public double[][] Filter(double[][] matrix, double threshold) {
		System.out.println("\nFiltered coexpression matrix : ");
		for (double[] matrix_row : matrix) {
			for (int i = 0; i < matrix_row.length; i++) {
				if (matrix_row[i] < threshold)
					matrix_row[i] = 0.0;
			}
			System.out.println(Arrays.toString(matrix_row));
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
				if (matrix_row[j] != 0.0) temp[j] = true;				
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
		
		for (int i=0; i<size; i++) {
			boolean[] this_clique = cliques.get(i);
			for (int j = 0; j < size; j++) { // column			
				if (this_clique[j]) 
				{
					boolean[] check = cliques.get(j);
					if(!check[i]) this_clique[j]=false;					
				}
				else{}
			}			
		}		
	    System.out.println("\nAfter : ");
		for (int i = 0; i < cliques.size(); i++) {
		     System.out.println(Arrays.toString(cliques.get(i)));
		}		
		
		return cliques;		
	}
	
	public List<int[]> FindClique(double[][] matrix) {

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
				if (matrix_row[j] != 0.0) temp[j] = 1;				
			}
			cliques.add(temp);
		}
		System.out.println("\nBefore : ");
		for (int i = 0; i < cliques.size(); i++) {
		     System.out.println(i+" : "+Arrays.toString(cliques.get(i)));
		}
		
		/*
		for (int i=0; i<size; i++) {
			int[] this_clique = cliques.get(i);
			for (int j = 0; j < size; j++) { // column			
				if (this_clique[j]==1) 
				{
					int[] check = cliques.get(j);
					if(check[i]==0) this_clique[j]=0;					
				}
				else{}
			}			
		}		
	    System.out.println("\nAfter : ");
		for (int i = 0; i < cliques.size(); i++) {
		     System.out.println(Arrays.toString(cliques.get(i)));
		}		
		*/
		return cliques;		
	}
	
	//public List<int[]> findClique(List<int[]> Lists){
	public ArrayList<ArrayList<Integer>> FindConnected(List<int[]> Lists){
		
		ArrayList<ArrayList<Integer>> IndexLists = new ArrayList<ArrayList<Integer>>();		
		for(int k=0; k<Lists.size(); k++){
			int[] List = Lists.get(k);
			ArrayList<Integer> index = new ArrayList<Integer>();
			
			for(int i=k; i<List.length; i++){
				if(List[i]==1 && i!=k) index.add(i);
			}
			IndexLists.add(index);			
		}
		
		System.out.println("\nConnected components : ");
		for (int i = 0; i < IndexLists.size(); i++) {
		     System.out.println(i+" : "+(IndexLists.get(i)));
		}				
		
		return IndexLists;
		/*
		List<int[]> allCliques = new ArrayList<int[]>();		
		for(int i=0; i<Lists.size(); i++){
			int[] list = Lists.get(i);
			for (int j=i; j<list.length; j++)
			{
				if(list[j]==1)
				{
					
				}
				
			}
		}
		*/		
	}
	
	
}
