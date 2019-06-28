package hello;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.GET;

@RestController
public class GreetingController {

    private final AtomicLong counter = new AtomicLong();

   @RequestMapping("/getresult")
    public Greeting greeting (){
        BufferedReader reader;
        String filename = null;
        try {//api to display latest extend report
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
            File file1 = new File("E:\\Niranjana\\New Folder\\mtc.txt");//mtc.txt created with 2nd api erased
            PrintWriter writer = new PrintWriter(file1);//mtc is used pass data 2nd api to mongodb
            writer.print("");
            writer.close();

        }catch(Exception e){}
        final String template = "http://localhost:63342/attr-qa-api-ui-auto/reports/"+filename+"?_ijt=8ssl79a5opuqj1ndtr0915l8cc";//link to get extend report (this is given in angular ui)

        return new Greeting(counter.incrementAndGet(),String.format(template));
    }
}
