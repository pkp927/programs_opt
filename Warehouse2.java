import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to
 * solve the knapsack problem.
 * 
 */
public class Warehouse2 {

	/**
	 * The main class
	 */
	private static double cost;

	public static void main(String[] args) {
		try {
			// solve(args);
			solve("-file=wl_500_1");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void solve(String args) throws IOException {
		String fileName = null;

		// get the temp file name
		// for(String arg : args){
		if (args.startsWith("-file=")) {
			fileName = args.substring(6);
		}
		// }
		if (fileName == null)
			return;

		// read the lines out of the file
		List<String> lines = new ArrayList<String>();

		BufferedReader input = new BufferedReader(new FileReader(fileName));
		try {
			String line = null;
			while ((line = input.readLine()) != null) {
				lines.add(line);
			}
		} finally {
			input.close();
		}

		// parse the data in the file
		cost=0;
		String[] firstLine = lines.get(0).split("\\s+");
		int noWarehouses = Integer.parseInt(firstLine[0]);
		int noCustomers = Integer.parseInt(firstLine[1]);
		
		int warehouseCap[]=new int[noWarehouses];
		double warehouseCost[]=new double[noWarehouses];
		
		for (int i = 0; i < noWarehouses; i++) {
			String line = lines.get(i+1);
			String[] parts = line.split("\\s+");
			warehouseCap[i] = Integer.parseInt(parts[0]);
			warehouseCost[i] = Double.parseDouble(parts[1]);
		}
		
		int customerDemand[] =new int[noCustomers];
		double distance[][] =new double[noCustomers][noWarehouses];
		
		int k=0;
		for (int i = 0; i < 2*noCustomers; i++) {
			String line = lines.get(i+1+noWarehouses);
			String[] parts = line.split("\\s+");
			if(i%2 != 0){
				for(int j=0;j<noWarehouses;j++){
					distance[k][j]=Double.parseDouble(parts[j]);
				}
				k++;
			}else{
				customerDemand[k] = Integer.parseInt(parts[0]);
			}
		}
		
		int warehouseAssigned[]=assignCustomers(noWarehouses, noCustomers, warehouseCost, warehouseCap, customerDemand, distance);
		// prepare the solution in the specified output format
		System.out.println(cost+" 0");
		for (int i = 0; i < noCustomers; i++) {
			System.out.print(warehouseAssigned[i]+" ");
		}
		System.out.println("");
	}
	
	public static int[] assignCustomers(int noWarehouses,int noCustomers,double warehouseCost[],int warehouseCap[],int customerDemand[],double distance[][]){
		int warehouseAssigned[]=new int[noCustomers];  //end result
		int custAssigned[]=new int[noCustomers];       //whether customer is given a warehouse or not
		for(int i=0;i<noWarehouses;i++){
			custAssigned[i]=0;
		}
		int warehouseCapFill[]=new int[noWarehouses];  //capacity of warehouse filled
		for(int i=0;i<noWarehouses;i++){
			warehouseCapFill[i]=0;
		}
		double sortedCost[]=getSortedCost(warehouseCost, noWarehouses);   //warehouses in sorted cost order
		
		for(int i=0;i<noWarehouses;i++){
			double[] dist = new double[noCustomers];    //distance of all customers from particular warehouse 
			for(int j=0;j<noCustomers;j++) dist[j]=distance[j][(int) sortedCost[i]];
			double sortedDist[]=getSortedDistance(dist, noCustomers);    //customers in sorted distance order
			int j=0;
			while(true){
				if(j==noCustomers) break;
				else if(custAssigned[(int) sortedDist[j]]==0 && (warehouseCapFill[(int) sortedCost[i]]+customerDemand[(int) sortedDist[j]] )<=warehouseCap[(int) sortedCost[i]]){
					warehouseCapFill[(int) sortedCost[i]]+=customerDemand[(int) sortedDist[j]]; 
					custAssigned[(int) sortedDist[j]]=1;
					warehouseAssigned[(int) sortedDist[j]]=(int) sortedCost[i];
					cost+=distance[(int) sortedDist[j]][(int) sortedCost[i]];
				}else{
					j++;
				}
			}
		}
		for(int j=0;j<noWarehouses;j++){
			if(warehouseCapFill[j]>0)
				cost+=warehouseCost[j];
		}
		return warehouseAssigned;
	}
	
	public static double[] getSortedCost(double[] cost,int noWarehouses){
		double sorted[]=new double[noWarehouses];
		ArrayList<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<noWarehouses;i++){
			sorted[i]=cost[i];
		}
		mergeSort(sorted ,0 ,noWarehouses-1);
		for(int i=0;i<noWarehouses;i++){
			int j=0;
			while(sorted[i]!=cost[j] || l.contains(j)){
				j++;
			}
			sorted[i]=j;
			l.add(j);
		}
		return sorted; 
	}
	
	public static int[] getSortedDemand(int[] demand,int noCustomers){
		int sorted[]=new int[noCustomers];
		ArrayList<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<noCustomers;i++){
			sorted[i]=demand[i];
		}
		intmergeSort(sorted ,0 ,noCustomers-1);
		for(int i=0;i<noCustomers;i++){
			int j=0;
			while(sorted[i]!=demand[j] || l.contains(j)){
				j++;
			}
			sorted[i]=j;
			l.add(j);
		}
		return sorted; 
	}
	
	public static double[] getSortedDistance(double[] distance,int noWarehouses){
		double sorted[]=new double[noWarehouses];
		ArrayList<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<noWarehouses;i++){
			sorted[i]=distance[i];
		}
		mergeSort(sorted ,0 ,noWarehouses-1);
		for(int i=0;i<noWarehouses;i++){
			int j=0;
			while(sorted[i]!=distance[j] || l.contains(j)){
				j++;
			}
			sorted[i]=j;
			l.add(j);
		}
		return sorted; 
	}
	
	private static void  mergeSort(double[] sorted,int l,int r){
		if(l<r){
			int m=(l+r)/2;
			mergeSort(sorted,l,m);
			mergeSort(sorted,m+1,r);
			merge(sorted,l,m,r);
		}
	}
	
	private static void merge(double[] sorted, int l, int m, int r) {
		int i=l;
		int j=m+1;
		int k=0;
		double[] arr=new double[r-l+1];
		while(true){
			if((i>m)||(j>r)) break;
			if(sorted[i]<sorted[j]) {
				arr[k]=sorted[i];
				i++;
			}
			else {
				arr[k]=sorted[j];
				j++;
			}
			k++;
		}
		while(i<=m){
			arr[k]=sorted[i];
			k++; i++;
		}
		while(j<=r){
			arr[k]=sorted[j];
			k++; j++;
		}
		for(i=0,k=l;k<=r;k++,i++){
			sorted[k]=arr[i];
		}
	}
	private static void  intmergeSort(int[] sorted,int l,int r){
		if(l<r){
			int m=(l+r)/2;
			intmergeSort(sorted,l,m);
			intmergeSort(sorted,m+1,r);
			intmerge(sorted,l,m,r);
		}
	}
	
	private static void intmerge(int[] sorted, int l, int m, int r) {
		int i=l;
		int j=m+1;
		int k=0;
		int[] arr=new int[r-l+1];
		while(true){
			if((i>m)||(j>r)) break;
			if(sorted[i]<sorted[j]) {
				arr[k]=sorted[i];
				i++;
			}
			else {
				arr[k]=sorted[j];
				j++;
			}
			k++;
		}
		while(i<=m){
			arr[k]=sorted[i];
			k++; i++;
		}
		while(j<=r){
			arr[k]=sorted[j];
			k++; j++;
		}
		for(i=0,k=l;k<=r;k++,i++){
			sorted[k]=arr[i];
		}
	}
}

