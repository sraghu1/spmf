package ca.pfv.spmf.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceEuclidian;
import ca.pfv.spmf.algorithms.clustering.distanceFunctions.DistanceFunction;
import ca.pfv.spmf.algorithms.clustering.optics.AlgoOPTICS;
import ca.pfv.spmf.algorithms.clustering.optics.DoubleArrayOPTICS;
import ca.pfv.spmf.patterns.cluster.Cluster;
import ca.pfv.spmf.patterns.cluster.DoubleArray;

/**
 *  Example of how to use the OPTICS algorithm to extract 
 *  DBScan-style clusters, in the source code and keep the
 *  result in memory.
 */
public class MainTestOPTICS_extractDBScan_saveToMemory {
	
	public static void main(String []args) throws NumberFormatException, IOException{
		
		String input = fileToPath("inputDBScan.txt");
		
		// we set the parameters of DBScan:
		int minPts=2;
		double epsilon = 20d;
		double epsilonPrime = 5d;
		
		// We specify that in the input file, double values on each line are separated by spaces
		String separator = " ";
		
		// Apply the algorithm to compute a cluster ordering
		AlgoOPTICS algo = new AlgoOPTICS();  
		
		/* Set the distance function to be used for clustering  
		 * Others are available (subclasses of DistanceFunction) */
		DistanceFunction distanceFunction = new DistanceEuclidian(); 
		algo.setDistanceFunction(distanceFunction);
		
		List<DoubleArrayOPTICS> clusterOrdering = algo.computerClusterOrdering(input, minPts, epsilon, separator);
		
		// Print the cluster-ordering of points to the console (for debugging)
		System.out.println("THE CLUSTER ORDERING:");
		System.out.println(" [data point] - reachability distance");
		for(DoubleArrayOPTICS arrayOP : clusterOrdering) {
			System.out.println(" " + Arrays.toString(arrayOP.data) + " -  " + arrayOP.reachabilityDistance);
		}
		
		//  generate dbscan clusters from the cluster ordering:
		List<Cluster> dbScanClusters = algo.extractDBScan(minPts,epsilonPrime);
		
		// Print the clusters found by the algorithm
		// For each cluster:
		System.out.println("CLUSTER(S) FOUND:");
		int i=0;
		for(Cluster cluster : dbScanClusters) {
			System.out.println("Cluster " + i++);
			// For each data point:
			for(DoubleArray dataPoint : cluster.getVectors()) {
				System.out.println("   " + dataPoint);
			}
		}
		
		algo.printStatistics();
//		algo.saveToFile(output);
	}
	
	public static String fileToPath(String filename) throws UnsupportedEncodingException{
		URL url = MainTestOPTICS_extractDBScan_saveToMemory.class.getResource(filename);
		 return java.net.URLDecoder.decode(url.getPath(),"UTF-8");
	}
	
	
}
