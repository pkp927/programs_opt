import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.*;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to solve the knapsack problem.
 *
 */
public class KnapsackDynamic {

    /**
     * The main class
     */
	/*
    public static void main(String[] args) {
        try {
            //solve(args);
        	solve("-file=ks_60_0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	*/
    public static void solve(String args) throws IOException {
        String fileName = null;

        // get the temp file name
        //for(String arg : args){
            if(args.startsWith("-file=")){
                fileName = args.substring(6);
            }
        //}
        if(fileName == null)
            return;

        // read the lines out of the file
        List<String> lines = new ArrayList<String>();

        BufferedReader input =  new BufferedReader(new FileReader(fileName));
        try {
            String line = null;
            while (( line = input.readLine()) != null){
                lines.add(line);
            }
        }
        finally {
            input.close();
        }


        // parse the data in the file
        String[] firstLine = lines.get(0).split("\\s+");
        int items = Integer.parseInt(firstLine[0]);
        int capacity = Integer.parseInt(firstLine[1]);

        long[] values = new long[items];
        long[] weights = new long[items];

        for(int i=1; i < items+1; i++){
          String line = lines.get(i);
          String[] parts = line.split("\\s+");

          values[i-1] = Long.parseLong(parts[0]);
          weights[i-1] = Long.parseLong(parts[1]);
        }


        long value = 0;
        long weight = 0;
        long[] taken = new long[items];

        long arr[][]=new long[capacity+1][items+1];
        for(int i=0;i<=capacity;i++){
           arr[i][0]=0;
        }
        for(int i=1;i<items+1;i++){
           fun(arr,capacity,items,i,values[i-1],weights[i-1]);
        }
        value=arr[capacity][items];
        int j=capacity;
        for(int i=items;i>0;i--){
           if(arr[j][i]==arr[j][i-1]){
              taken[i-1]=0;
           }else{
              taken[i-1]=1;
              j=j-(int)weights[i-1];
           }
        }
        // prepare the solution in the specified output format
        System.out.println(value+" 0");
        for(int i=0; i < items; i++){
            System.out.print(taken[i]+" ");
        }
        System.out.println("");
    }

    public static void fun(long arr[][],long capacity,long items,int i,long v,long w){
        for(int j=0;j<=capacity;j++){
           if(j==0){
              arr[j][i]=0;
           }else{
              if((j-w)>=0){
                 arr[j][i]=max(arr[j][i-1],v+arr[j-(int)w][i-1]);
              } else{
                 arr[j][i]=arr[j][i-1];
              }
           }
        }
    }

    public static long max(long a,long b){
      if(a>=b)  return a;
      else return b;
    }
}
