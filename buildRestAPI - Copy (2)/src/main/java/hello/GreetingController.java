package hello;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "%s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/runtest")
    public Greeting greeting() {

        File file = new File("C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master\\file.xml");//file.xml is the testng xml file to be kept in project directory
        try {

            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n" +
                    "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >\n" +
                    "\n" +
                    "<suite name = \"Suite1\">\n" +
                    " <test name = \"test1\">\n" +

                    "\n" +
                    "<classes>\n" );//credential exporting script to be added mandatory

            File file1 = new File("E:\\Niranjana\\New Folder\\append.txt");

            BufferedReader br = new BufferedReader(new FileReader(file1));

            String st;
            while ((st = br.readLine()) != null)//all class from append.txt written to testng(ie file.xml) xml file
            {
               // System.out.println(st);
                writer.write(st);
            writer.write("\n");
            }

            br.close();

            writer.write("</classes>\n" +
                    " </test>\n" +
                    "</suite>\n" +
                    "\n" +
                    "\n" +
                    "\n");
            writer.flush();
            writer.close();
        } catch (Exception e2) {

        }

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("E:\\Niranjana\\New Folder\\append.txt"));
            writer.write("");
            writer.flush();//erased append.txt


        }catch(Exception e){}

        try
        {
            Runtime.getRuntime().exec("cmd.exe /c start mvn clean test -Dsurefire.suiteXmlFiles=C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master\\file.xml", null, new File("C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master"));//run testng xml file
        }
        catch (Exception ex)
        {
            System.out.println("ERROR");
            ex.printStackTrace();
        }

        String y = "C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master\\file.xml";
        return new Greeting(counter.incrementAndGet(),
                String.format(template, y));
    }
}