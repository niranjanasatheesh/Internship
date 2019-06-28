package hello;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;

@RestController
public class GreetingController {

    private static final String template = "%s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/gettestcases")
    public Greeting greeting() {

                RequestSpecification request = RestAssured.given();
                request.header("Content-Type", "application/json");

                JsonObject bodyjson = new JsonObject();
                bodyjson.addProperty("exportType", "csv");
                bodyjson.addProperty("maxAllowedResult", "true");
                bodyjson.addProperty("expand", "teststeps");
                bodyjson.addProperty("startIndex", "0");
                bodyjson.addProperty("zqlQuery", "executionStatus != UNEXECUTED AND executedBy != sani9002 AND project = attribution");

                request.body(bodyjson.toString());

                request.auth().preemptive().basic("sani9002","Password4$");
                Response response = request.post("https://adlm.nielsen.com/jira/rest/zapi/latest/execution/export");
                //System.out.println(response.getStatusCode());
                //   System.out.println(response.body().prettyPrint());


                String[] url1 = response.body().prettyPrint().split(":",2);
                // System.out.println(url1[1]);

                String[] url2 = url1[1].split("\"",3);
                //System.out.println(url2[1]);
/*
        String[] url3 = url2[0].split("\"",3);
        System.out.println(url3[1]);*/
                String url = url2[1];

                RequestSpecification request1 = RestAssured.given();
                request1.header("Content-Type", "application/json");
                //request1.param("fileName","ZFJ-Executions-05-28-2019.csv");
                request1.auth().preemptive().basic("sani9002","Password4$");
                Response response1 = request.get(url);
                //System.out.println(response1.getStatusCode());
                //System.out.println(response1.body().prettyPrint());
                try {
                    File file = new File("E:\\ExportedTestCases.csv");
                    file.createNewFile();
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(response1.body().prettyPrint());
                    bw.flush();
                    bw.close();
                }
                catch (Exception e){}




        String y = "E:\\JiraTestCases.csv";
        return new Greeting(counter.incrementAndGet(),
                String.format(template, y));
    }
}