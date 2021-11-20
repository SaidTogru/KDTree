

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Object.*;

public class KDTree {

	class KDNode {
		int dim;
		double t;
		int point;

		// inner node
		public KDNode(int dim, double t) {
			this.dim = dim;
			this.t = t;
			this.point = -1;
		}

		// leaf
		public KDNode(int point) {
			this.dim = -1;
			this.t = 0;
			this.point = point;
		}

		public KDNode() {
			this.dim = (Integer) null;
			this.t = (Double) null;
			this.point = (Integer) null;

		}

		public String toString() {
			return dim + " " + t + " " + point;
		}
	}

	KDNode[] nodes;
	double[][] data;
	int help = 1;

	public KDTree(double[][] data) {
		this.data = data;
		build(data);
	}

	static int NextPowerOfTwo(int n) {
		for (int i = 0; n >= Math.pow(2, i - 1); i++) {
			if (Math.pow(2, i) >= n) {

				return (int) Math.pow(2, i);
			}
		}
		return 9;

	}

	static double median(double[] data) {
		double temp[] = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			temp[i] = data[i];
		}
		Arrays.sort(temp);
		double median;
		if (temp.length % 2 == 0)
			median = ((double) temp[temp.length / 2] + (double) temp[temp.length / 2 - 1]) / 2;
		else
			median = (double) temp[temp.length / 2];
		return median;
	}

	public void build(double[][] data) {

		nodes = new KDNode[2 * NextPowerOfTwo(data[0].length)];
		int[] points = new int[data[0].length];
		for (int i = 0; i < data[0].length; i++) {
			points[i] = i;

		}
		build(data, points, 1, 0);

	}

	public void build(double[][] data, int[] points, int index, int dim) {

		System.out.println("Die points am Anfang: " + Arrays.toString(points));
		double[][] datahelp = new double[data.length][points.length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < points.length; j++) {
				datahelp[i][j] = data[i][points[j]];

			}
		}
		System.out.println(Arrays.deepToString(datahelp));
		double cut = median(datahelp[dim]);
		System.out.println("cut: " + cut);
		if (points.length > 1) {

			nodes[index] = new KDNode(dim, cut);
		}

		List<Integer> pr = new ArrayList<Integer>();
		List<Integer> pl = new ArrayList<Integer>();
		for (int i = 0; i < points.length; i++) {
			if ((datahelp[dim][i]) >= cut) {
				pr.add(points[i]);
			}
			if (datahelp[dim][i] < cut) {
				pl.add(points[i]);
			}
		} 
		int[] pointsRight = new int[pr.size()];
		for (int i = 0; i < pr.size(); i++) {
			pointsRight[i] = pr.get(i);
		}
		int[] pointsLeft = new int[pl.size()];
		for (int i = 0; i < pl.size(); i++) {
			pointsLeft[i] = pl.get(i);
		}

		if (pointsLeft.length > 1) {
			build(data, pointsLeft, index * 2, (dim < data.length - 1 ? dim + 1 : 0));
		} else {
			System.out.println("Blatt " + Arrays.toString(pointsLeft));
			nodes[index * 2] = new KDNode(pl.get(0));

		}

		if (pointsRight.length > 1) {
			build(data, pointsRight, index * 2 + 1, (dim < data.length - 1 ? dim + 1 : 0));
		} else {
			System.out.println("Blatt " + Arrays.toString(pointsRight));
			nodes[index * 2 + 1] = new KDNode(pr.get(0));
		}

	}

	static double dist(double[] p1, double[] p2) {
		double x = 0;
		for (int i = 0; i < p1.length; i++) {
			x += Math.pow(p1[i] - p2[i], 2);
		}
		return Math.sqrt(x);

	}

	double searchGreedy(double[] query) {
		int i = 1;
		double t = nodes[1].t; // Feldzugriff
		KDNode nearestNode = null;

		double[] temp = new double[data.length];
		int dimension = 0;
		while (t != 0) {
			if (query[dimension] >= t) {
				t = nodes[i * 2 + 1].t; // Feldzugriff
				nearestNode = nodes[i * 2 + 1]; // Feldzugriff
				i = i * 2 + 1;
				dimension = (dimension < data.length - 1 ? dimension + 1 : 0);

			} else {
				t = nodes[i * 2].t; // Feldzugriff
				nearestNode = nodes[i * 2]; // Feldzugriff
				i = i * 2;
				dimension = (dimension < data.length - 1 ? dimension + 1 : 0);
			}

		}
		for (int j = 0; j < data.length; j++) {
			temp[j] = data[j][nearestNode.point];
		} // Feldzugriff

		System.out.println(Arrays.toString(temp));
		return dist(temp, query);

	}

	public void removefromNode(int p, KDNode[] nodesss) {
		for (int i = 1; i < nodesss.length; i++) {
			if (p == nodesss[i].point) {
				nodesss[i] = nodes[i + 1];
			}
			p = nodes[i].point;
		}
	}

	public static void main(String[] args) {
		double data[][] = new double[][] { { 4, 7, 2, 5, 1 }, { 3, 5, 7, 1, 2 } };
		KDTree kdt = new KDTree(data);
		double result = kdt.searchGreedy(new double[] { 5, 5 });
		System.out.println("Search Greedy: " + result);
	}
}