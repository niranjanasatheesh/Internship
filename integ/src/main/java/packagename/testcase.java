package packagename;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import net.miginfocom.swing.MigLayout;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class testcase {

    public static void main(String[] args) {
        MigLayout layout = new MigLayout("fillx", "[][grow][]");
        JPanel content = new JPanel(layout);

        JLabel l1 = new JLabel("XML file to be run:");
        content.add(l1, " spanx 2,growx");
        final JTextField t1 = new JTextField("C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master\\file.xml");
        content.add(t1, "spanx 2,wrap,growx");

        content.add(new JLabel("\t"), " wrap");

        JLabel l2 = new JLabel("Project to be run:");
        content.add(l2, " spanx 2,growx");
        final JTextField t2 = new JTextField("C:\\Users\\Niranjana\\IdeaProjects\\attr-qa-api-ui-auto-master");
        content.add(t2, "spanx 2,growx,wrap");

        content.add(new JLabel("\t"), " wrap");

        JLabel l3 = new JLabel("CSV file with test cases");
        content.add(l3, " spanx 2,growx");
        final JTextField t3 = new JTextField("E:\\Niranjana\\New Folder\\jira.csv");
        content.add(t3, "spanx 2,wrap,growx");

        content.add(new JLabel("\t"), " wrap");

        JLabel l4 = new JLabel("CSV file with mappings");
        content.add(l4, " spanx 2,growx");
        final JTextField t4 = new JTextField("E:\\Niranjana\\New Folder\\mapping.csv");
        content.add(t4, "spanx 2,wrap,growx");

        content.add(new JLabel("\t"), " wrap");

        content.add(new JLabel("Test cases File:"), " spanx 2,growx");
        content.add(new JLabel("Mapping File:"), " spanx 2,growx,wrap");

        content.add(new JLabel("\t"), " wrap");

        content.add(new JLabel("Column containing Test Executions:"));
        final JTextField t5 = new JTextField("0");
        content.add(t5,"growx");

        content.add(new JLabel("Column containing Manual Test Cases:"));
        final JTextField t6 = new JTextField("0");
        content.add(t6, "wrap,growx");

        content.add(new JLabel("\t"), " wrap");

        content.add(new JLabel("Column containing Test Step:"));
        final JTextField t7 = new JTextField("1");
        content.add(t7,"growx");

        content.add(new JLabel("Column containing Automated Test Cases:"));
        final JTextField t8 = new JTextField("1");
        content.add(t8, "wrap,growx");

        content.add(new JLabel("\t"), " wrap");

        JLabel l9 = new JLabel("Path of Surefire report:");
        content.add(l9, " spanx 2,growx");
        final JTextField t9 = new JTextField("C:/Users/Niranjana/IdeaProjects/mvntesting/target/surefire-reports");
        content.add(t9, "spanx 2,growx,wrap");

        content.add(new JLabel("\t"), " wrap");

        JLabel l10 = new JLabel("Location of chromedriver.exe:");
        content.add(l10, " spanx 2,growx");
        final JTextField t10 = new JTextField("E:\\Niranjana\\chrome74\\chromedriver.exe");
        content.add(t10, "spanx 2,growx,wrap");

        content.add(new JLabel("\t"), " wrap");

        JButton b1 = new JButton("Submit");
        content.add(b1);
        content.setBounds(20,20,700,500);
        final JFrame frame = new JFrame("Configure Settings");
        frame.add(content);

        frame.setSize(800,550);
        frame.setLayout(null);
        frame.setVisible(true);

        b1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                final String propath = t2.getText();
                final String xmlpath = t1.getText();
                String testpath = t3.getText();
                final String mappath = t4.getText();
                final String surefirepath = t9.getText();
                final String driverpath = t10.getText();
                int c1 = Integer.parseInt(t5.getText());
                int c2 = Integer.parseInt(t7.getText());
                final int c3 = Integer.parseInt(t6.getText());
                final int c4 = Integer.parseInt(t8.getText());

                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

                final JFrame f = new JFrame("Manual Test Cases");
                final JButton b = new JButton("Submit");
                final JCheckBox[] cb= new JCheckBox[1000];

                UIManager.put("TabbedPane.selected", Color.TRANSLUCENT);
                final JTabbedPane tp = new JTabbedPane();
                tp.setBounds(0,-25,800,450);
                final JPanel[] pan = new JPanel[100];
                int y = 20;

                String csvFile = testpath;
                BufferedReader br = null;
                String line = "";
                String csvSplitBy = ",";

                BufferedReader br2 = null;
                int q=0;
        try {
            br2 = new BufferedReader(new FileReader(csvFile));

            while ((line = br2.readLine()) != null) {
                q++;
            }
            final int max;
            if((q)%10 == 0)
                max = (q)/10;
            else
                max = q/10 + 1;


            br = new BufferedReader(new FileReader(csvFile));
            int i = 0;
            int j = 0;
            int p = 0;
            final String autotc[] = new String[1000];
            pan[p] = new JPanel();
            pan[p].setLayout(null);
            int flag=0;

            while ((line = br.readLine()) != null) {
                String open = null;
                String[] data = line.split(csvSplitBy);
                try {
                    open = data[c1];
                }
                catch (Exception exp){}
                if(i!=0) {
                    int h = j-1;
                    cb[h] = new JCheckBox(open);
                    cb[h].setBounds(20, y, 100, 30);
                    y += 40;

                    pan[p].add(cb[h]);
                    final int w = p;

                    if (i == 1) {
                        tp.add("Page " + (p + 1), pan[p]);
                        if (flag == 0) {
                            JButton next = new JButton("next");
                            next.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent e) {
                                    tp.setSelectedIndex(1);
                                }
                            });
                            next.setBounds(650, 400, 100, 20);
                            pan[p].add(next);
                            JLabel pgno = new JLabel(Integer.toString(w + 1));
                            pgno.setBounds(625, 400, 25, 20);
                            pan[p].add(pgno);
                            flag++;
                        } else {

                            if (p == max - 1) {
                                JButton prev = new JButton("prev");
                                prev.addActionListener(new ActionListener() {

                                    public void actionPerformed(ActionEvent e) {
                                        tp.setSelectedIndex(w - 1);
                                    }
                                });
                                prev.setBounds(650, 400, 100, 20);
                                pan[p].add(prev);
                                JLabel pgno = new JLabel(Integer.toString(w + 1));
                                pgno.setBounds(625, 400, 25, 20);
                                pan[p].add(pgno);
                            } else {
                                JButton next = new JButton("next");
                                next.addActionListener(new ActionListener() {

                                    public void actionPerformed(ActionEvent e) {
                                        tp.setSelectedIndex(w + 1);
                                    }
                                });
                                JButton prev = new JButton("prev");
                                prev.addActionListener(new ActionListener() {

                                    public void actionPerformed(ActionEvent e) {
                                        tp.setSelectedIndex(w - 1);
                                    }
                                });

                                JLabel pgno = new JLabel(Integer.toString(w + 1));
                                next.setBounds(650, 400, 100, 20);
                                pgno.setBounds(625, 400, 25, 20);
                                prev.setBounds(500, 400, 100, 20);
                                pan[p].add(next);
                                pan[p].add(prev);
                                pan[p].add(pgno);
                            }
                        }
                    }
                }
                    if(i>9) {
                        i = 0;
                        y = 20;
                        p = p+1;
                        pan[p] = new JPanel();
                        pan[p].setLayout(null);
                    }
                i++;
                j++;
            }
            f.add(tp);
            final int g = j - 1;
            b.setBounds(20,450,100,30);
            b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        File file = new File(xmlpath);
                        file.createNewFile();
                        FileWriter fw = new FileWriter(file);
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write("<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n" +
                                "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >\n" +
                                "\n" +
                                "<suite name=\"hellotest\">\n" +
                                "    <test name = \"test1\">\n" +
                                "\n" +

                                "        <classes>\n" );


                        File file1 = new File("E:\\Niranjana\\New Folder\\mtc.txt");
                        file1.createNewFile();
                        FileWriter fw1 = new FileWriter(file1);
                        BufferedWriter bw1 = new BufferedWriter(fw1);

                        for(int k=0;k<g;k++){
                            if(cb[k].isSelected()){
                                String csvFile1 = mappath;
                                BufferedReader br1 = null;
                                String line = "";
                                String csvSplitBy = ",";

                                bw1.write(cb[k].getText()+"\n");
                                System.out.println(cb[k].getText());

                                br1 = new BufferedReader(new FileReader(csvFile1));
                                while ((line = br1.readLine()) != null) {

                                    String[] data = line.split(csvSplitBy);
                                    String open1 = data[0];
                                    if (open1.equals(cb[k].getText())) {
                                        autotc[k] = data[1];

                                    }
                                }

                                String[] classmethod = autotc[k].split("#");
                                bw.write("            <class name = \""+classmethod[0]+"\">\n" +
                                        "              <methods>\n" +
                                        "                   <include name = \"" + classmethod[1]+"\"/>\n"+
                                        "              </methods>\n" +
                                        "           </class>\n");
                            }
                        }
                        bw1.flush();
                        bw1.close();
                        bw.write("        </classes>\n" +
                                "    </test>\n" +
                                "</suite>");
                        bw.flush();
                        bw.close();
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                    try
                    {
                        Runtime.getRuntime().exec("cmd.exe /c start mvn clean test -Dsurefire.suiteXmlFiles="+xmlpath, null, new File(propath));
                    }
                    catch (Exception ex)
                    {
                        System.out.println("ERROR");
                        ex.printStackTrace();
                    }
                    f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                    JFrame f2 = new JFrame("Test result");
                    JButton l2 = new JButton("Link to test results");
                    l2.setBounds(20,20,200,40);
                    l2.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            System.setProperty("webdriver.chrome.driver",driverpath);
                            WebDriver driver = new ChromeDriver();
                            driver.navigate().to("file:///C:/Users/Niranjana/IdeaProjects/attr-qa-api-ui-auto-master/reports/30-05-2019_hellotest_05-13-39_run_report.html");
                        }
                    });
                    f2.add(l2);
                    f2.setSize(800,550);
                    f2.setLayout(null);
                    f2.setVisible(true);
                }
            });
            f.add(b);

            JLabel pagelabel = new JLabel(" Page no: ");
            pagelabel.setBounds(580,450,70,20);
            final JTextField pageno = new JTextField("");
            pageno.setBounds(650,450,25,20);
            JLabel totpage = new JLabel(" of "+max);
            totpage.setBounds(675,450,25,20);
            JButton go = new JButton("Go");
            go.setBounds(700,450,50,20);
            go.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    tp.setSelectedIndex(Integer.parseInt(pageno.getText())-1);
                }
            });
            f.add(go);
            f.add(pageno);
            f.add(totpage);
            f.add(pagelabel);
            f.setSize(800,550);
            f.setLayout(null);
            f.setVisible(true);


        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
            }
        });
    }
}
