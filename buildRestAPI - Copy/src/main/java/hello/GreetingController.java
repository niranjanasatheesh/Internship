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

    @RequestMapping("/mapping")
    public Greeting greeting(@RequestParam(value="name", defaultValue="") String name) {

try {
    File file = new File("E:\\Niranjana\\New Folder\\mtc.txt");
    FileWriter fr = new FileWriter(file, true);
    BufferedWriter br = new BufferedWriter(fr);
    br.write(name+"\n");

    br.close();
    fr.close();
}catch (Exception exc){}
    String csvFile1 = "E:\\Niranjana\\New Folder\\mapping.csv";
        BufferedReader br1 = null;
        String line1 = "";
        String cvsSplitBy1 = ",";
        String h1 = " ";
        int i = 0;

            try {
                br1 = new BufferedReader(new FileReader(csvFile1));


                String n[] = name.split(",");

                name = n[n.length - 1];

                while ((line1 = br1.readLine()) != null) {
                    String[] country = line1.split(cvsSplitBy1);
                    String[] wr = country[1].split("#");
                    if (country[0].equals(name)) {
                        h1 = h1 + "<class name = " + "\"" + wr[0] + "\"" + ">\n" + "<methods><include name=" + "\"" + wr[1] + "\"" + "/></methods></class>";


                    }
                }

                File file = new File("E:\\Niranjana\\New Folder\\append.txt");
                FileWriter fr = new FileWriter(file, true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(h1);

                br.close();
                fr.close();
            } catch (Exception e) {

            }


        return new Greeting(counter.incrementAndGet(),
                String.format(template, h1));
    }
}