package it.sunnyvale.academy.sparksqlbasics;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;
/*
Run me:

$ mvn exec:exec -Dspark.master=local -P DataFrameOperations

$ spark-submit \
  --master yarn \
  --driver-memory 512m  \
  --executor-memory 512m \
  --class it.sunnyvale.academy.sparksqlbasics.DataFrameOperations \
  --deploy-mode cluster \
  target/spark-sql-basics-1.0-SNAPSHOT.jar

*/

public class DataFrameOperations {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();

        Dataset<Row> df = spark
                .read()
                .option("multiline", "true")
                .json("lab12_input"+System.getProperty("file.separator")+"users.json");

        // print the JSON schema
        df.printSchema();

        // Show all
        df.show();

        // Select only the "firstName" column
        df
                .select("firstName")
                .show();

        // Select everybody (firstName and age)
        df
                .select(col("firstName"), col("age"))
                .show();

        // Select people older than 21
        df
                .filter(col("age").gt(21))
                .show();

        // filtering using Expressions
        df
                .filter("age < 21")
                .show();

        // filtering with Lambdas
        df
                .filter((FilterFunction<Row>) row -> row
                                .getAs("firstName")
                                .toString().startsWith("L"))
                .show();

        // columns
        Column eyeColourColumn = df.col("eyeColor");
        df
                .filter(eyeColourColumn.geq("green"))
                .show();

    }
}
