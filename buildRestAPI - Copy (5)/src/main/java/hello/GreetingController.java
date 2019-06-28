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
        request.header("Content-Type", "application/json");

        JsonObject bodyjson = new JsonObject();
        bodyjson.addProperty("exportType", "csv");

        request.body(bodyjson.toString());

        request.auth().preemptive().basic("sani9002","Password4$");
        Response response = request.get("https://adlm.nielsen.com/jira/rest/api/2/search?jql=filter=61563&maxResults=1000");
        //System.out.println(response.getStatusCode());
        //System.out.println(response.body().prettyPrint());
        String y = response.body().prettyPrint();

        try {
            String sJSON=response.body().asString();
            DocumentContext documentContext = JsonPath.parse(sJSON);
//            String p = documentContext.read("issues[0].key");
//


                File file = new File("E:\\Niranjana\\New Folder\\JIRA.txt");
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);

        try {
//
            int i = 0;
            while (i < 1000) {
                String p = documentContext.read("issues[" + i + "].key");
                String q = documentContext.read("issues[" + i + "].fields.customfield_10113");
                System.out.println("\"" + p + "\"" + "," + "\"" + q + "\"" + ";");
                String t = "\"" + p + "\"" + "," + "\"" + q + "\"" + ";\n";
                bw.write(t);
                i++;
            }


            bw.flush();
            bw.close();

        }catch (Exception e){
            System.out.println("catch1");
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