import java.awt.Point;
import java.util.*;


public class TspGraph {
	
	private int nodes;
	private PointTsp[] pt;
	private int[] in;
	private int[] out;
	private int pno;
	
	public TspGraph(){
		
	}
	
	public TspGraph(int n){
		pt =new PointTsp[n];
		in = new int[n];
		out = new int[n];
		pno=0;
	}
	
	public void initialiseTspGraph( int nodes,double[] x, double[] y){
		this.nodes = nodes;
		for(int i=0; i<nodes; i++){
			pt[i] = new PointTsp(x[i],y[i]);
		}
	}

	public int getPno() {
		return pno;
	}

	public Point[] getPaths() {
		Point[] arr = new Point[pno]; 
		for(int i=0;i<pno;i++){
			arr[i]=new Point(i,out[i]);
		}
		return arr;
	}

	public PointTsp getPt(int i) {
		return pt[i];
	}

	public void makePath(int n1, int n2){  // n1->n2
		out[n1] = n2;
		in[n2] = n1;
		pno++;
	}
	
	public void insertNode(int n, int n1, int n2){
		if(out[n1] == n2){  // if n1->n2
			pno--;
			makePath(n1,n);
			makePath(n,n2);
		}else{				// if n2->n1
			pno--;
			makePath(n2,n);
			makePath(n,n1);
		}
	}

	public int  getPathDist(){
		int[] arr = new int[pno]; 
		arr[0] = 0;
		int d = 0;
		for(int i=1;i<pno;i++){
			arr[i]=out[arr[i-1]];
			d+=getDistance(pt[arr[i-1]], pt[arr[i]]); 
		}
		return d;
	}
	
	public double getDistance(PointTsp p1, PointTsp p2){
		double d = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
		return d;
	}
	
	public int[] getSortedInX(int[] arr){
		int sorted[]=new int[nodes];
		ArrayList<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<nodes;i++){
			sorted[i]=arr[i];
		}
		mergeSort(sorted ,0 ,nodes-1);
		for(int i=0;i<nodes;i++){
			int j=0;
			while(sorted[i]!=arr[j] || l.contains(j)){
				j++;
			}
			sorted[i]=j+1;
			l.add(j);
		}
		return sorted; //return node numbers
	}

	public int[] getSortedInY(int[] arr){
		int sorted[]=new int[nodes];
		ArrayList<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<nodes;i++){
			sorted[i]=arr[i];
		}
		mergeSort(sorted ,0 ,nodes-1);
		for(int i=0;i<nodes;i++){
			int j=0;
			while(sorted[i]!=arr[j] || l.contains(j)){
				j++;
			}
			sorted[i]=j+1;
			l.add(j);
		}
		return sorted; //return node numbers
	}
	
	public int[]  getFullPath(){
		int[] arr = new int[nodes+1]; // arr[nodes] to calculate total distance
		arr[0] = 0;
		arr[nodes] = 0;
		for(int i=1;i<nodes;i++){
			arr[i]=out[arr[i-1]];
			arr[nodes]+=getDistance(pt[arr[i-1]], pt[arr[i]]); 
		}
		return arr;
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

