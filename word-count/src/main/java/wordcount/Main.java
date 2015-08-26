package wordcount;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

/**
 * Created by abhijith.nagarajan on 8/26/15.
 */
public class Main {

	public static void main(String[] args) throws IOException {

		String path = Main.class.getClassLoader().getResource("spark_example.txt").getPath();

		System.out.println(Files.size(Paths.get(path)));

		System.out.println(System.nanoTime());

		wordCount(path);

		System.out.println(System.nanoTime());

	}

	private static void wordCount(String fileName) {

		SparkConf conf = new SparkConf().setMaster("local").setAppName("Wordcount");

		JavaSparkContext sc = new JavaSparkContext(conf);

		JavaRDD<String> input = sc.textFile(fileName);

		JavaPairRDD<String, Integer> counts = input.flatMap(s -> Arrays.asList(s.split(" "))).mapToPair( t -> new
				Tuple2(t,	1)).reduceByKey((x,y) ->
				(int)x
				+ (int) y);

		counts.saveAsTextFile("output");
	}
}
