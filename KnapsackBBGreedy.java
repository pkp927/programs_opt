import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.*;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to solve the knapsack problem.
 *
 */
public class KnapsackBBGreedy {

    /**
     * The main class
     */
	
	/*
    public static void main(String[] args) {
        try {
        	//solve(args);
        	solve("-file=ks_1000_0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	*/
    static void solve(String args) throws IOException {
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
        long room = capacity;
        boolean[] taken = new boolean[items];
        boolean[] best_taken=new boolean[items];
        long estimate=0;
        long best_estimate=0;

        for(int i=0; i < items; i++){
           estimate+=values[i];
           taken[i]=false;
           best_taken[i]=false;
        }
        
        SortKnap sort = new SortKnap(values,weights);
        int[] sorted= sort.getSorted(); 
        
        int item=sorted.length-1;
        boolean loop=true;
        while(loop){

           taken[sorted[item]]=true;
           value+=values[sorted[item]];
           room-=weights[sorted[item]];

           if(room<0){
               taken[sorted[item]]=false;
               value-=values[sorted[item]];
               room+=weights[sorted[item]];
               estimate-=values[sorted[item]];
           }

           if(estimate==value && estimate > best_estimate){
              best_estimate=estimate;
              for(int i=0; i < items; i++){
                best_taken[i]=taken[i];
              }
           }

           item--;

           if(item<0){
              int j=item+1;

              if(taken[sorted[j]]==true){
                 taken[sorted[j]]=false;
                 room+=weights[sorted[j]];
                 value-=values[sorted[j]];
              }else{
                 estimate+=values[sorted[j]];
              }

              j++;

              while(j>0){
                 if(taken[sorted[j]]==true){
                     taken[sorted[j]]=false;
                     room+=weights[sorted[j]];
                     value-=values[sorted[j]];
                     j++;
                     break;
                 }else{
                    estimate+=values[sorted[j]];
                    j++;
                 }
              }

              if(j==items-1){
                 if(taken[sorted[j]]==true){
                     taken[sorted[j]]=false;
                     room+=weights[sorted[j]];
                     value-=values[sorted[j]];
                     estimate-=values[sorted[j]];
                     j--;
                 }else{
                    loop=false;
                 }
              }

              item=j;
              //System.out.println(item);
              //System.out.println(taken[item]);
           }
           if(estimate<best_estimate){
              loop=false;
           }

        }
        // prepare the solution in the specified output format
        System.out.println(best_estimate+" 0");
        for(int i=0; i < items; i++){
            if(best_taken[i]==true)
             System.out.print(1+" ");
            else
             System.out.print(0+" ");
        }
        System.out.println("");
    }

}
