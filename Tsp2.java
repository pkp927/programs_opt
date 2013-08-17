import java.awt.Point;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.*;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to
 * solve the knapsack problem.
 * 
 */
public class Tsp2 {

	/**
	 * The main class
	 */

	private static TspGraph g;
	/*
	public static void main(String[] args) {
		try {
			// solve(args);
			solve("-file=tsp_100_2");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
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
		int nodes = Integer.parseInt(lines.get(0));

		double[] x = new double[nodes];
		double[] y = new double[nodes];

		for (int i = 1; i < nodes + 1; i++) {
			String line = lines.get(i);
			String[] parts = line.split("\\s+");

			x[i - 1] = Double.parseDouble(parts[0]);
			y[i - 1] = Double.parseDouble(parts[1]);
		}

		g = new TspGraph(nodes);
		g.initialiseTspGraph(nodes,x, y);

		travel(nodes);

		int[] arr = g.getFullPath();
		//boolean b= isPermutation(arr);
		// prepare the solution in the specified output format
		System.out.println(arr[nodes]+" 0");
		for (int i = 0; i < nodes; i++) {
			 System.out.print(arr[i]+" ");
		}
		System.out.println("");
	}

	private static boolean isPermutation(int[] arr) {
		ArrayList<Integer> l=new ArrayList<Integer>();
		for(int i=0;i<arr.length;i++){
			if(l.contains(arr[i])){
				return false;
			}
			l.add(arr[i]);
		}
		return true;
	}

	private static void travel(int nodes) {
		initialTravel(nodes);
	}

	private static void initialTravel(int nodes) {
		g.makePath(0, 1);
		g.makePath(1, 0);
		for (int i = 2; i < nodes; i++) {
			double min=0;
			int pt1=0;
			int pt2=0;
			Point[] path=new Point[g.getPno()];
			path = g.getPaths();
			double d= g.getPathDist();
			for (int j = 0; j < i-1; j++) {
				PointTsp p1 = g.getPt(path[j].x);
				PointTsp p2 = g.getPt(path[j].y);
				PointTsp p = g.getPt(i);
				double d1 = g.getDistance(p1, p) + g.getDistance(p, p2);
				double d2 = g.getDistance(p1, p2);
				d1 = d - d2 + d1;
				if(j==0){ 
					min = d1;
					pt1=path[j].x;
					pt2=path[j].y;
				}
				else{
					if(min>d1){
						min=d1;
						pt1=path[j].x;
						pt2=path[j].y;
					}
				}
			}
			if(pt1!=0 || pt2!=0)
				g.insertNode(i, pt1, pt2);
			else
				g.makePath(i, 0);
		}
	}
}

