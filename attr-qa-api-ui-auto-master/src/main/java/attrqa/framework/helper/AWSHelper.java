package attrqa.framework.helper;

import attrqa.framework.reporting.ExtentTestManager;
import com.amazonaws.services.athena.AmazonAthena;
import com.amazonaws.services.athena.AmazonAthenaAsyncClientBuilder;
import com.amazonaws.services.athena.model.ColumnInfo;
import com.amazonaws.services.athena.model.Datum;
import com.amazonaws.services.athena.model.GetQueryExecutionRequest;
import com.amazonaws.services.athena.model.GetQueryExecutionResult;
import com.amazonaws.services.athena.model.GetQueryResultsRequest;
import com.amazonaws.services.athena.model.GetQueryResultsResult;
import com.amazonaws.services.athena.model.QueryExecutionContext;
import com.amazonaws.services.athena.model.QueryExecutionState;
import com.amazonaws.services.athena.model.ResultConfiguration;
import com.amazonaws.services.athena.model.Row;
import com.amazonaws.services.athena.model.StartQueryExecutionRequest;
import com.amazonaws.services.athena.model.StartQueryExecutionResult;
import com.amazonaws.services.athena.model.StopQueryExecutionRequest;
import com.amazonaws.services.athena.model.StopQueryExecutionResult;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * utility class to cater to AWS operations
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class AWSHelper {


  public static List<String> getResultsFromDynamoDB(String sRegion,String sTableName,String sKeyConditionExpression, String sKey, String sValue){
    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(sRegion).build();
    DynamoDB dynamoDB = new DynamoDB(client);
    Table table = dynamoDB.getTable(sTableName);
    QuerySpec querySpec = new QuerySpec().withKeyConditionExpression(sKeyConditionExpression)
        .withValueMap(new ValueMap().withString(sKey, sValue));
    ItemCollection<QueryOutcome> items = table.query(querySpec);
    Iterator<Item> iterator = items.iterator();
    List<String> listOfJSONString = new ArrayList<>();
    while (iterator.hasNext()) {
      Item item = iterator.next();
      listOfJSONString.add(item.toJSON());
    }
    return listOfJSONString;
  }

  public static void deleteObjectsFromS3(String sRegion, String sBucketName, String sKey){
    AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(sRegion).build();
    amazonS3.deleteObject(new DeleteObjectRequest(sBucketName,sKey));
    ExtentTestManager.getExtentTest().info("deleting "+sKey+" from S3 bucket "+sBucketName);
  }

  public static void deleteAllObjectsFromS3(String sRegion, String sBucketName, String sFolderName){
    AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(sRegion).build();
    ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(sBucketName);
    ListObjectsV2Result listObjectsV2Result;
    do {
      listObjectsV2Result = amazonS3.listObjectsV2(listObjectsV2Request);
      for (S3ObjectSummary objectSummary : listObjectsV2Result.getObjectSummaries()) {
        if(objectSummary.getKey().startsWith(sFolderName+"/")){
          amazonS3.deleteObject(new DeleteObjectRequest(sBucketName,objectSummary.getKey()));
          ExtentTestManager.getExtentTest().info("deleting "+objectSummary.getKey()+" from S3 bucket "+sBucketName);
        }
      }
    }
    while (listObjectsV2Result.isTruncated());
  }

  public static void uploadFilesToS3(String sRegion, String sBucketName, String sFolderName, String... filePaths){
    AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(sRegion).build();

    for(String sFilePath : filePaths){
      File file = new File(sFilePath);
      String sKeyName = sFolderName + "/" + file.getName();
      amazonS3.putObject(sBucketName,sKeyName,file);
      ExtentTestManager.getExtentTest().info("uploading "+sKeyName+" into "+sBucketName);
    }
  }

  public static List<String> queryAthena(String sRegion, String sDatabase, String sOutputLocation, String sQuery) throws Exception{
    AmazonAthena amazonAthena = AmazonAthenaAsyncClientBuilder.standard().withRegion(sRegion).build();
    QueryExecutionContext queryExecutionContext = new QueryExecutionContext().withDatabase(sDatabase);
    ResultConfiguration resultConfiguration = new ResultConfiguration().withOutputLocation(sOutputLocation);
    StartQueryExecutionRequest startQueryExecutionRequest = new StartQueryExecutionRequest().withQueryString(sQuery).withQueryExecutionContext(queryExecutionContext).withResultConfiguration(resultConfiguration);
    StartQueryExecutionResult startQueryExecutionResult = amazonAthena.startQueryExecution(startQueryExecutionRequest);
    String sQueryExecutionId = startQueryExecutionResult.getQueryExecutionId();
    GetQueryExecutionRequest queryExecutionRequest = new GetQueryExecutionRequest().withQueryExecutionId(sQueryExecutionId);
    GetQueryExecutionResult queryExecutionResult = null;
    boolean isQueryStillRunning = true;
    while(isQueryStillRunning){
      queryExecutionResult = amazonAthena.getQueryExecution(queryExecutionRequest);
      String queryState = queryExecutionResult.getQueryExecution().getStatus().getState();
      if (queryState.equals(QueryExecutionState.FAILED.toString())) {
        throw new RuntimeException("Query Failed to run with Error Message: " + queryExecutionResult.getQueryExecution().getStatus().getStateChangeReason());
      }
      else if (queryState.equals(QueryExecutionState.CANCELLED.toString())) {
        throw new RuntimeException("Query was cancelled.");
      }
      else if (queryState.equals(QueryExecutionState.SUCCEEDED.toString())) {
        isQueryStillRunning = false;
      }
      else {
        Thread.sleep(3000);
      }
    }
    GetQueryResultsRequest queryResultsRequest = new GetQueryResultsRequest().withQueryExecutionId(sQueryExecutionId);
    GetQueryResultsResult queryResultsResult = amazonAthena.getQueryResults(queryResultsRequest);
    List<ColumnInfo> columnInfoList = queryResultsResult.getResultSet().getResultSetMetadata().getColumnInfo();
    List<Row> rows = queryResultsResult.getResultSet().getRows();
    List<String> listOfValues = new ArrayList<>();
    for(Row row : rows){
      List<Datum> datumList = row.getData();
      for(Datum datum : datumList){
        listOfValues.add(datum.getVarCharValue());
      }
    }

    StopQueryExecutionRequest stopQueryExecutionRequest = new StopQueryExecutionRequest().withQueryExecutionId(sQueryExecutionId);
    StopQueryExecutionResult stopQueryExecutionResult = amazonAthena.stopQueryExecution(stopQueryExecutionRequest);

    return listOfValues;
  }

  private static void processRow(Row row, List<ColumnInfo> columnInfoList) {
    for (int i = 0; i < columnInfoList.size(); ++i) {
      switch (columnInfoList.get(i).getType()) {
        case "varchar":
          // Convert and Process as String
          System.out.println(row);
          break;
        case "tinyint":
          // Convert and Process as tinyint
          break;
        case "smallint":
          // Convert and Process as smallint
          break;
        case "integer":
          // Convert and Process as integer
          break;
        case "bigint":
          // Convert and Process as bigint
          break;
        case "double":
          // Convert and Process as double
          break;
        case "boolean":
          // Convert and Process as boolean
          break;
        case "date":
          // Convert and Process as date
          break;
        case "timestamp":
          // Convert and Process as timestamp
          break;
        default:
          throw new RuntimeException(
              "Unexpected Type is not expected" + columnInfoList.get(i).getType());
      }
    }
  }

//  public static void printS3Data (String sRegion, String sBucketName){
//    AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(sRegion).build();
////    ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(sBucketName).withMaxKeys(2);
//    ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request()
//        .withBucketName(sBucketName);
//    ListObjectsV2Result listObjectsV2Result;
//    do {
//      listObjectsV2Result = amazonS3.listObjectsV2(listObjectsV2Request);
//      for (S3ObjectSummary objectSummary : listObjectsV2Result.getObjectSummaries()) {
//        System.out.println(objectSummary.getKey() + " " + objectSummary.getSize());
//      }
//      // If there are more than maxKeys keys in the bucket, get a continuation token
//      // and list the next objects.
//      String token = listObjectsV2Result.getNextContinuationToken();
//      System.out.println("Next Continuation Token: " + token);
//      listObjectsV2Request.setContinuationToken(token);
//    } while (listObjectsV2Result.isTruncated());
//  }
}


