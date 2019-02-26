import java.io.*;

public class CentralProcessUnit {
    MemoryManagementUnit mmu = new MemoryManagementUnit();
    int softMiss = 0;
    int hardMiss = 0;
    int hits = 0;
    int diskReads = 0;
    int diskWrites = 0;
    public String currentline;
    InputStream fileInputStream;
    BufferedReader bufferedReader;
    private String address;

    public CentralProcessUnit() throws IOException {
    }
    public void readFile(sBuilder sb) throws IOException {

        int operation = Integer.parseInt(currentline);
                currentline = bufferedReader.readLine();
                address = currentline;
                sb.writeToFile2(0, address);
                System.out.println();
                System.out.println(operation + " " + address);

                if(operation == 0)
                {
                    mmu.fetchAddress(address, sb);
                    sb.writeToFile2(6, String.valueOf(mmu.getpageInfo(address,mmu.Offset)));
                    sb.writeToFile2(1, "0");
                }
                else if(operation == 1)
                {
                    diskWrites++;
                    sb.writeToFile2(1, "1");
                    // fetch the address
                    mmu.fetchAddress(address, sb);

                    currentline = bufferedReader.readLine();
                    mmu.writeToMemory(address,mmu.Offset,currentline, sb);

                }
        boolean hit = true;
        // check if miss occured
        // if so, record it and reset mmu
        if (mmu.softMiss)
        {
            sb.writeToFile2(2, "1");
            hit = false;
            this.softMiss++;
            mmu.softMiss = false;
        }
        else{
            sb.writeToFile2(2, "0");
        }
        if (mmu.hardMiss)
        {
            diskReads++;
            hit = false;
            sb.writeToFile2(3, "1");
            this.hardMiss++;

            mmu.hardMiss = false;
        }
        else{
            sb.writeToFile2(3, "0");
        }
        if(hit == true){
            this.hits++;
            sb.writeToFile2(4, "1");
        }
        else{sb.writeToFile2(4, "0");
        }
                currentline = bufferedReader.readLine();

    }

}
