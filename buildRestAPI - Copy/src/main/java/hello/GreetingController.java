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

    @RequestMapping("/mapping")        //2nd api 1st part
    public Greeting greeting(@RequestParam(value="name", defaultValue="") String name) {//the ticked test cases from angular ui is passed to name

try {
    File file = new File("E:\\Niranjana\\New Folder\\mtc.txt");//each time api is called name is appended to this file
    FileWriter fr = new FileWriter(file, true);
    BufferedWriter br = new BufferedWriter(fr);
    br.write(name+"\n");
    br.flush();
    br.close();

}catch (Exception exc){}
    String csvFile1 = "E:\\Niranjana\\New Folder\\JIRA.txt";
        BufferedReader br1 = null;
        String line1 = "";
        String cvsSplitBy1 = ",";
        String h1 = " ";
        int i = 0;

            try {
                br1 = new BufferedReader(new FileReader(csvFile1));


                String n[] = name.split(",");//bufferread reads all the test cases we only require the last one in the string name hence that is passed to name

                name = n[n.length - 1];//ie string is of form attr1attr2 only attr2 selected

                while ((line1 = br1.readLine()) != null) {//Jira.txt file is read line by line check whethernthe first col and name is same
                    String[] country = line1.split(cvsSplitBy1);//if same (the script class is in second col)

                    if (country[0].equals(name)) {//script class apeended to append.txt
                        h1 = "<class name = " + "\"" + country[1] + "\"" + ">\n" +"</class>";


                    }
                }

                br1.close();

                File file = new File("E:\\Niranjana\\New Folder\\append.txt");
                FileWriter fr = new FileWriter(file, true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(h1);
                br.flush();
                br.close();

            } catch (Exception e) {
            }


        return new Greeting(counter.incrementAndGet(),
                String.format(template, h1));
    }
}