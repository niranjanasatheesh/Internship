package hello;

import java.io.*;
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

        File file = new File("C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master\\file.xml");
        try {
            if (file.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }


            FileWriter writer = new FileWriter(file);
            writer.write("<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n" +
                    "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >\n" +
                    "\n" +
                    "<suite name = \"Suite1\">\n" +
                    " <test name = \"test1\">\n" +

                    "\n" +
                    "<classes>\n" );

            File file1 = new File("E:\\Niranjana\\New Folder\\append.txt");

            BufferedReader br = new BufferedReader(new FileReader(file1));

            String st;
            while ((st = br.readLine()) != null)
                writer.write(st);

            writer.write("</classes>\n" +
                    " </test>\n" +
                    "</suite>\n" +
                    "\n" +
                    "\n" +
                    "\n");
            writer.close();
        } catch (Exception e2) {

        }

        try {
            File file1 = new File("E:\\Niranjana\\New Folder\\append.txt");
            PrintWriter writer = new PrintWriter(file1);
            writer.print("");
            writer.close();

        }catch(Exception e){}

        try
        {
            Runtime.getRuntime().exec("cmd.exe /c start mvn clean test -Dsurefire.suiteXmlFiles=C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master\\file.xml", null, new File("C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master"));
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