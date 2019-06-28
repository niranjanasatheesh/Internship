package hello;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;

@RestController
public class GreetingController {

    private static final String template = "%s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/export")
    public Greeting greeting() {

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");  //taking details from jira

        JsonObject bodyjson = new JsonObject();
        bodyjson.addProperty("exportType", "csv");  //to get the output in a csv file

        request.body(bodyjson.toString());  //  all the requirec details are kept in request body

        request.auth().preemptive().basic("sani9002","Password4$");
        Response response = request.get("https://adlm.nielsen.com/jira/rest/api/2/search?jql=filter=61563&maxResults=1000");
        //System.out.println(response.getStatusCode());
        //System.out.println(response.body().prettyPrint());

        String y=response.body().asString();
        try {//always use a try during file creation
            String sJSON=response.body().asString();  //cconverting json response to a string
            DocumentContext documentContext = JsonPath.parse(sJSON);  //always use jsonpath for parsing a json string
//            String p = documentContext.read("issues[0].key");
//


            File file = new File("E:\\Niranjana\\New Folder\\map.txt");  //always flush and close..always use this code for file creation
            file.createNewFile();
            FileWriter fw = new FileWriter(file);      //creating a mapping file
            BufferedWriter bw = new BufferedWriter(fw);

            File file1 = new File("E:\\Niranjana\\New Folder\\testcases1.txt");
            file1.createNewFile();
            FileWriter fw1 = new FileWriter(file1);  //creating a file with jira details
            BufferedWriter bw1 = new BufferedWriter(fw1);

            try {
//
                int i = 0;
                while (i < 1000) {  //max 1000   fields must be selected by user
                    String p = documentContext.read("issues[" + i + "].key");
                    String q = documentContext.read("issues[" + i + "].fields.customfield_10113");
                    String r = documentContext.read("issues[" + i + "].fields.summary");
                    System.out.println(p +  ","  + q +  "\n");
                    String t = p +  ","  + q +  "\n";
                    String u = p +  "," +r + "," + q +  "\n";         //looping through the issues array to get the details and create the required files

                    bw.write(t);
                    bw1.write(u);
                    i++;
                }


                bw.flush();
                bw.close();
                bw1.flush();
                bw1.close();

            }catch (Exception e){  //if no of testcases less than 1000 exception is caught and bw is closed
                System.out.println("catch1");
                bw1.flush();
                bw1.close();
                bw.flush();
                bw.close();
            }

        }
        catch (Exception ew)
        {
            System.out.println("catch1");
        }

        return new Greeting(counter.incrementAndGet(),
                String.format(template, y));
    }
}