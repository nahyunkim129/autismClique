package wgcna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LoadData {
	
	HashMap<String, Integer> aut_index_map = new HashMap<String, Integer>();
	ArrayList<Integer> aut_index_AL = new ArrayList<Integer>();

	public double[][] Load(String coex_path) throws IOException {

		//String total_gpath = "gene_name.csv";
		String total_gpath = "src/resources/gene_name2.csv";
		String aut84_gpath = "src/resources/AutRef84.csv";
		//String aut26_gpath = "aut26.csv";
		String aut26_gpath = "src/resources/Aut3.csv";

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

		// Get 84 total autism genes
		ArrayList<String> aut_gname = new ArrayList<String>();
		Scanner aut_gscan = new Scanner(new File(aut84_gpath));
		int no_aut = 0;
		while (aut_gscan.hasNextLine()) {
			String line = aut_gscan.nextLine();
			String[] lineArray = line.split(" ");
			aut_gname.add(new String(lineArray[0]));
			no_aut++;
		}
		//System.out.println(no_aut + " Autism Genes : " + aut_gname);
		aut_gscan.close();
		
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
		//System.out.println(no_aut26 + " Autism Genes : " + aut_26);
		aut26_gscan.close();
		
		// Get 26 autism gene index		
		for(int i=0; i<aut_26.size(); i++)
		{			
			aut_index_map.put(aut_26.get(i), total_gname.indexOf(aut_26.get(i)));
			aut_index_AL.add(total_gname.indexOf(aut_26.get(i)));
						
		}
		//System.out.println("Autism gene name = index : ");
		System.out.println(no_aut26+" Autism Genes : "+Arrays.asList(aut_index_map));		

		// Get coex matrix
		// 1. Create coex matrix first by getting its size
		String delimiter = ",";
		int size = 0;
		Scanner coex_scan = new Scanner(new File(coex_path));
		while (coex_scan.hasNextLine()) {
			String line = coex_scan.nextLine();
			String[] fxRatesAsString = line.split(delimiter);
			size=fxRatesAsString.length;
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
		System.out.println("\nLoaded Coexpression matrix : ");
		for (double[] coex_row : coex) {
            System.out.println(Arrays.toString(coex_row));
        }
        */		
		return coex;
	}	
	
}
