import java.io.IOException;
import java.util.ArrayList;


public class SortKnap {
	
	private double[] arr; 
	
	public SortKnap(long[] v,long[] w){
		arr=new double[w.length];
		for(int i=0;i<w.length;i++){
			arr[i]=(double) ((double)v[i]/w[i]);
		}
	}

	public int[] getSorted(){
		double sorted[]=new double[arr.length];
		ArrayList<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<arr.length;i++){
			sorted[i]=arr[i];
		}
		quickSort(sorted ,0 ,arr.length-1);
		for(int i=0;i<arr.length;i++){
			int j=0;
			while(sorted[i]!=arr[j] || l.contains(j)){
				j++;
			}
			sorted[i]=j;
			//System.out.println(j);
			l.add(j);
		}
		int s[]=new int[arr.length];
		for(int i=0;i<arr.length;i++) s[i]=(int) sorted[i];
		return s; //return numbers
	}
	
	static void swap(double[] x,int y,int z){
		double temp=x[y];
		x[y]=x[z];
		x[z]=temp;
	}

	static int partitionAroundPivot(double a[],int start,int end){

		//int r=rand()%17;
		//int p;
		//if(r>=start&&r<=end) p=r;
		//else p=r%(end-start)+start;
		//cout<<p<<endl;
		int m=start+(end-start)/2;
		int p;
		if((a[start]>=a[m] && a[start]<=a[end] )|| (a[start]<=a[m] && a[start]>=a[end]) ){
	        p=start;
		}else if((a[m]>=a[start] && a[m]<=a[end]) || (a[m]<=a[start] && a[m]>=a[end])){
		    p=m;
		}else {
		    p=end;
		}

		swap(a,start,p);
		int i=start+1;
		int size=end-start+1;
		for(int j=start+1;j<=end;j++){
			if(a[j]<a[start]){
				swap(a,i,j);
				i++;
			}
		}
		swap(a,start,i-1);
		return i;
	}
	
	static void quickSort(double a[],int start,int end){
		int index=partitionAroundPivot(a,start,end);
		
		if((index-1-start)>1){
			quickSort(a,start,index-2);
		}

		if((end+1-index)>1){
			quickSort(a,index,end);
		}

	}

}
