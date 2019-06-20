package hello;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.GET;

@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();


public static String getFileName() {
    BufferedReader reader;
    String filename = null;
    try {
        reader = new BufferedReader(new FileReader("E:\\Niranjana\\New Folder\\filename.txt"));
        String line = reader.readLine();

        int i = 0;
        while (i == 0) {
            filename = line;
            System.out.println(filename);
            i++;
            line = reader.readLine();
        }
        reader.close();
    } catch (IOException e) {
        e.printStackTrace();
    }

    try {
        File file1 = new File("E:\\Niranjana\\New Folder\\mtc.txt");
        PrintWriter writer = new PrintWriter(file1);
        writer.print("");
        writer.close();

    }catch(Exception e){}
 return filename;
}

    static String filename = getFileName();
    private static final String template = "http://localhost:63342/attr-qa-api-ui-auto/reports/"+filename+"?_ijt=8ssl79a5opuqj1ndtr0915l8cc";
    @GET
    @RequestMapping("/greeting")
    public Greeting greeting (){
        return new Greeting(counter.incrementAndGet(),String.format(template));
    }
}
