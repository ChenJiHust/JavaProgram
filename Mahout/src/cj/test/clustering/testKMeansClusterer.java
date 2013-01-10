package cj.test.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.mahout.clustering.dirichlet.UncommonDistributions;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;


/*
 * 测试K-Means算法。
 * 
 * 使用KMeansClusterer.clusterPoints()
 * 
 * */
public class testKMeansClusterer {
	private static void generateSamples(List<Vector> vectors,int num,
			double mx,double my,double sd) {
		for(int i = 0 ;i<num;i++){
			vectors.add(new DenseVector(new double[]{
					UncommonDistributions.rNorm(mx, sd),
					UncommonDistributions.rNorm(my, sd)
			}));
		}		
	}
	private static List<Vector> chooseRandomPoints(List<Vector> vectors,int k){
		List<Vector> seedData = new ArrayList<Vector>();
		for(int i=0;i<k;i++){
			Random random = new Random();
			Vector vec = vectors.get(random.nextInt(vectors.size())).clone();
			seedData.add(vec);
		}
		return seedData;
	}
	public static void main(String[] args){
		List<Vector> sampleData = new ArrayList<Vector>();
		
		generateSamples(sampleData, 400, 1, 1, 3);
		generateSamples(sampleData, 300, 1, 0, 0.5);
		generateSamples(sampleData, 300, 0, 2, 0.1);
		
		int k = 3;
		
		List<Vector> randomPoints = chooseRandomPoints(sampleData,k);
		
		List<Kluster> clusters = new ArrayList<Kluster>();
		int clusterID = 0;
		for (Vector v : randomPoints) {
			clusters.add(new Kluster(v,clusterID++,new EuclideanDistanceMeasure()));
		}

	}

}
