import java.util.*;


public class Graph {

	private int nodes;
	private int edges;
	private int matrix[][];
	
	public Graph(){
		nodes=0; edges=0;
	}
	public Graph(int n,int e){
		nodes=n; edges=e;
	}
	
	public void initialiseGraph(){
		matrix=new int[nodes][nodes];
		for(int i=0;i<nodes;i++){
			for(int j=0;j<nodes;j++){
				matrix[i][j]=0;
			}
		}
	}

	public void setEdges(int n1,int n2) {
		matrix[n1][n2]=1;
		matrix[n2][n1]=1;
	}
	
	public int getMaxDeg(){
		int max=0;
		for(int i=0;i<nodes;i++){
			int m=0;
			for(int j=0;j<nodes;j++){
				if(matrix[i][j]==1){
					m+=1;
				}
			}
			if(m>max){
				max=m;
			}
		}
		return max; 
	}
	
	public int getDeg(int n){
		int m=0;
		for(int j=0;j<nodes;j++){
			if(matrix[n][j]==1){
				m+=1;
			}
		}
		return m;
	}
	
	public int[] getSortedDeg(){
		int arr[]=new int[nodes];
		int sorted[]=new int[nodes];
		ArrayList<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<nodes;i++){
			arr[i]=getDeg(i);
			sorted[i]=arr[i];
		}
		mergeSort(sorted,0,nodes-1);
		for(int i=0;i<nodes;i++){
			int j=0;
			while(sorted[i]!=arr[j] || l.contains(j)){
				j++;
			}
			sorted[i]=j;
			l.add(j);
			//System.out.println(sorted[i]);
		}
		return sorted;
	}
	
	public int[] getNeighbours(int n){
		int neighbours[]=new int[nodes];
		for(int i=0;i<nodes;i++){
			neighbours[i]=matrix[n][i];
		}
		return neighbours;
	}
	
	private void  mergeSort(int a[],int l,int r){
		if(l<r){
			int m=(l+r)/2;
			mergeSort(a,l,m);
			mergeSort(a,m+1,r);
			merge(a,l,m,r);
		}
	}
	private void merge(int[] a, int l, int m, int r) {
		int i=l;
		int j=m+1;
		int k=0;
		int[] arr=new int[r-l+1];
		while(true){
			if((i>m)||(j>r)) break;
			if(a[i]<a[j]) {
				arr[k]=a[i];
				i++;
			}
			else {
				arr[k]=a[j];
				j++;
			}
			k++;
		}
		while(i<=m){
			arr[k]=a[i];
			k++; i++;
		}
		while(j<=r){
			arr[k]=a[j];
			k++; j++;
		}
		for(i=0,k=l;k<=r;k++,i++){
			a[k]=arr[i];
		}
	}

}
