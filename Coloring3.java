import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.*;


public class Coloring3 {

	private static Graph g;
	private static int noOfColors;
	private static int[] prevColorOfNode;
	private static int[] colorOfNode;
	private static int[][] domainOfNode;
	private static int[][] prevDomainOfNode;
	private static boolean further;
	/*
	public static void main(String[] args) {
		try {
			// solve(args);
			solve("-file=gc_70_5");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	public static void solve(String args) throws IOException {
		String fileName = null;

		// get the temp file name
		// for (String arg : args) {
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
		String[] firstLine = lines.get(0).split("\\s+");
		int nodes = Integer.parseInt(firstLine[0]);
		int edges = Integer.parseInt(firstLine[1]);

		g = new Graph(nodes, edges);
		g.initialiseGraph();

		for (int i = 1; i < edges + 1; i++) {
			String line = lines.get(i);
			String[] parts = line.split("\\s+");

			g.setEdges(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		}

		noOfColors = g.getMaxDeg();
		//System.out.println(noOfColors);
		colorOfNode = new int[nodes];
		prevColorOfNode = new int[nodes];

		domainOfNode = new int[nodes][noOfColors];
		colorTheGraph(nodes);

		int colorsUsed = 0;
		for (int i = 0; i < nodes; i++) {
			int c = colorOfNode[i];
			if (c > colorsUsed)
				colorsUsed = c;
		}

		// System.out.println(noOfColors);
		 //System.out.println(checkEverything(nodes));
		// prepare the solution in the specified output format

		System.out.println(colorsUsed + " " + 0);
		for (int i = 0; i < nodes; i++) {
			System.out.print(colorOfNode[i] + " ");
		}
		System.out.println("");

	}


	private static void colorTheGraph(int nodes) {
		initialiseDomain(nodes);
		int arr[]=g.getSortedDeg();
		ArrayList<Integer> l = new ArrayList<Integer>();
		int n;
		for (int i = nodes-1; i >2*nodes/3; i--) {
			n=arr[i];
			n = colorNodes(nodes, n);
			boolean check = checkConstraints(nodes, n);
			if (check == false)
				n = backtrack(nodes, n);
			else l.add(n);
		}
		
		for(int i =2*nodes/3; i >=0; i--){
			n = smallByDomain(nodes, l);
			n = colorNodes(nodes, n);
			boolean check = checkConstraints(nodes, n);
			if (check == false){
				n = backtrack(nodes, n);
			}else{
				l.add(n);
			}
		}
	}
	
	private static int smallByDomain(int nodes,ArrayList l){
		int arr[]=g.getSortedDeg();
		int n,nod=0;
		int m,min=10000;
		for (int i = 2*nodes/3; i >=0; i--) {
			if(!l.contains(arr[i])){
				n=arr[i];
				m=0;
				for(int j=0;j<noOfColors;j++){
					if(domainOfNode[i][j]!=0) m++; 
				}
				if(m<min){ 
					min=m; nod=n;
				}	
			}
		}
		return nod;
	}

	private static int backtrack(int nodes, int n) {
		// System.out.println("backtrack");
		int work = n;
		while (work >= 0) {
			int c = 0;
			while (domainOfNode[work][c] != colorOfNode[work])
				c++;
			if (c < (noOfColors - 1)) {
				colorOfNode[work] = domainOfNode[work][c + 1];
				pruneTheDomain(nodes);
				break;
			} else {
				// System.out.println("back");
				work--;
			}
		}
		return work;
	}

	private static int colorNodes(int nodes, int n) {
		int c = 0;
		while (c < noOfColors && domainOfNode[n][c] == 0)
			c++;
		
		if (c < noOfColors)
			colorOfNode[n] = domainOfNode[n][c];
		else
			n = backtrack(nodes, n);
		return n;
	}

	private static void initialiseDomain(int nodes) {
		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < noOfColors; j++) {
				domainOfNode[i][j] = j + 1;
			}
		}
	}

	private static boolean checkConstraints(int nodes, int n) {
		int neighbours[] = g.getNeighbours(n);
		for (int j = 0; j < neighbours.length; j++) {
			if (neighbours[j] == 1) {
				if (colorOfNode[n] == colorOfNode[j])
					return false;
			}
		}
		pruneTheDomain(nodes);
		return true;
	}

	private static void pruneTheDomain(int nodes) {
		for (int i = 0; i < nodes; i++) {
			if (colorOfNode[i] != 0) {
				int neighbours[] = g.getNeighbours(i);
				for (int j = 0; j < neighbours.length; j++) {
					if (neighbours[j] == 1) {
						for (int k = 0; k < noOfColors; k++) {
							if (domainOfNode[j][k] == colorOfNode[i]) {
								domainOfNode[j][k] = 0;
							}
						}
					}
				}
			}
		}
	}

	private static boolean checkEverything(int nodes) {
		for (int i = 0; i < nodes; i++) {
			if (colorOfNode[i] != 0) {
				int neighbours[] = g.getNeighbours(i);
				for (int j = 0; j < neighbours.length; j++) {
					if (neighbours[j] == 1) {
						if (colorOfNode[i] == colorOfNode[j]) {
							//System.out.println("color is same " + i + " " + j);
							return false;
						}
					}
				}
			} else {
				//System.out.println("no color");
				return false;
			}
		}
		return true;
	}

}
