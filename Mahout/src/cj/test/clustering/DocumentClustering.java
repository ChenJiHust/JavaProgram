package cj.test.clustering;

import java.io.File;

import org.apache.lucene.benchmark.utils.ExtractReuters;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.vectorizer.SparseVectorsFromSequenceFiles;

public class DocumentClustering {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DocumentClustering.documentVectorize(args);
	}

	public static void documentVectorize(String[] args) throws Exception {
		DocumentClustering.extractReuters();

		DocumentClustering.transformToSequenceFile();

		DocumentClustering.transformToVector();
	}

	public static void extractReuters() {
		File inputFolder = new File("clustering/reuters");
		File outputFolder = new File("clustering/reuters-extracted");
		ExtractReuters extractor = new ExtractReuters(inputFolder, outputFolder);
		extractor.extract();
	}

	public static void transformToSequenceFile() {

		String[] args = { "-c", "UTF-8", "-i", "reuters-extracted", "-o",
				"reuters-seqfiles" };
		try {
			SequenceFilesFromDirectory.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void transformToVector() {

		String[] args = { "-i", "reuters-seqfiles/", "-o",
				"reuters-vectors-bigram", "-a",
				"org.apache.lucene.analysis.WhitespaceAnalyzer", "-chunk",
				"200", "-wt", "tfidf", "-s", "5", "-md", "3", "-x", "90",
				"-ng", "2", "-ml", "50", "-seq" };
		try {
			SparseVectorsFromSequenceFiles.main(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
