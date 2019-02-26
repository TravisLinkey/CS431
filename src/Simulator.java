import java.io.*;
import java.util.Scanner;

public class Simulator {
    Scanner input = new Scanner(System.in);

    Simulator() throws IOException { run(); }
   // public StringBuilder sb = new StringBuilder();
    public void run() throws IOException {
        sBuilder sb = new sBuilder();
        System.out.println("Please type the name of the file or 'Q' to quit. ");

        // User input section
        String filepath = input.next();
        if(filepath.equals("Q"))
            return;

        String outputFile = "src/outputFiles/" + filepath + ".csv";
        PrintWriter pw = new PrintWriter(new File(outputFile));

        sb.writeToFile(filepath+" output" + ','+'\n');

        sb.writeToFile("Address" + ',' + "Read/Write" + ',' + "Soft Miss" + ','+ "Hard Miss"+ ',' + "A hit" + ',' + "Dirty Bit on Evict" +','+ "Value Read/Written" + ',' + '\n');

        OperatingSystem os = new OperatingSystem(filepath, sb);


        StringBuilder file = sb.returnSB();

        pw.write(file.toString());
        pw.close();
        }
    }

