import java.io.*;

public class OperatingSystem {
    CentralProcessUnit cpu;
    private String filepath;
    private int instruction = 0;


    OperatingSystem(String filepath, sBuilder sb) throws IOException {
        this.filepath = filepath;
        cpu = new CentralProcessUnit();
        this.run(sb);
    }

    /**
     * This function runs the program, opening the file
     * and passing it to the cpu
     */
    public void run(sBuilder sb) throws FileNotFoundException {

        filepath = "test_files/" + filepath + ".txt";
        cpu.fileInputStream = getClass().getResourceAsStream(filepath);
        cpu.bufferedReader = new BufferedReader(new InputStreamReader(cpu.fileInputStream));

        try {
            cpu.currentline = cpu.bufferedReader.readLine();

            while(cpu.currentline != null)
            {
                cpu.readFile(sb);
                sb.writeToFile("\n");
                instruction++;

                // if 5 instructions have passed,
                // reset r bits to 0
                if(instruction > 4)
                {
                    resetRbits();
                    //checkDbits();
                    instruction = 0;
                }
                sb.writeToSB();
                sb.clearSBArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Total soft miss:" + cpu.softMiss);
        System.out.println("Total hard miss:" + cpu.hardMiss);

        sb.writeToFile("\nTotal Disk Access:,"  +(cpu.diskReads+cpu.diskWrites)+ "\n");
        sb.writeToFile("Total Soft Miss:," + cpu.softMiss + "\n");
        sb.writeToFile("Total Hard Miss:," + cpu.hardMiss + "\n");
        sb.writeToFile("Total Hits:," + cpu.hits + "\n");
        sb.writeToFile("Total Disk Reads:," + cpu.diskReads + "\n");
        sb.writeToFile("Total Disk Writes:," + cpu.diskWrites +  "\n");


    }

    public void resetRbits() {
        System.out.println("Reset all r bits!");
        cpu.mmu.resetRbits();
        }
    }
