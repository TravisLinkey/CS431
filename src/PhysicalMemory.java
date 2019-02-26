import java.io.*;
import java.nio.channels.FileChannel;

public class PhysicalMemory {
    int pages = 0xFF;
    int pageframes = 0xFF;
    page[][] HardDrive = new page[pageframes][pages];
    private File originalFile;
    private File newFile;
    linkedList RAM;
    int clockptr = 0;

    /**
     * Constructor initializes Hard Drive array
     * and the RAM Linked List
     * @throws IOException
     */
    PhysicalMemory() throws IOException {
        initializeHD();
        initializeList();
    }

    /**
     * Function to create the Linked List
     */
    private void initializeList() {
        RAM = new linkedList(16);
    }

    /**
     * Function to add a page to memory.
     * If memory is not full, add the entry.
     * If memory is full, evict a page with
     * Clock Replacement
     * @param page
     * @param pagedata
     */
    public void add(String page, int[] pagedata, sBuilder sb) {
        if (!RAM.isFull())
        {
            RAM.addNew(page,pagedata);
            RAM.listCount++;
            sb.writeToFile2(5, "No Eviction");
            //sb.writeToFile("No Eviction" + ',');
        }
        else
        {
            System.out.println("RAM is full. ");
            ClockAlgorithm(page,pagedata, sb);
        }
    }

    /**
     * Clock Page Replacement Algorithm
     * @param address
     * @param pagedata
     */
    public void ClockAlgorithm(String address, int[] pagedata, sBuilder sb) {
        boolean found0 = false;
        Node newNode = new Node(address, pagedata);

        Node currentNode = RAM.getNode(clockptr);


        while (found0 == false && currentNode.getNext() != null)
        {
            int i = RAM.getNodeIndex((String) currentNode.getData());

            if (currentNode.recent == false) {

                System.out.println("EVICTING " + currentNode.getData() + " from RAM. At index: " + i);
                System.out.println("Dirty bit is: " + currentNode.dirty);

                if(currentNode.dirty)
                {
                    System.out.println("*********************************************************");
                    System.out.println("*********************************************************");
                }

                // If dirty bit is set, write to disk before eviction
                if(currentNode.dirty == true){
                    sb.writeToFile2(5, "1");
                    //sb.writeToFile("1" + ',');
                    writeToDisk(currentNode.getData(), currentNode.page);
                }
                else{
                    sb.writeToFile2(5, "0");
                    //sb.writeToFile("0" + ',');
                }
                // insert new page at index i
                System.out.println("Adding: " + newNode.getData());
                RAM.addNode(newNode, i);
                found0 = true;
            }

            // if the recent bit = 1 set it to 0
            // get the next node
            if (currentNode.recent == true) {
                System.out.println("Resetting recent bit. ");
                currentNode.recent = false;
                currentNode = currentNode.getNext();
            }

            i++;
            clockptr++;

            if(clockptr > 15)
                clockptr = 0;
        }
        printMemory();
    }

    private void printMemory() {
        for (int i = 0; i < this.RAM.listCount; i++) {
            System.out.println(i + " :" + this.RAM.getNode(i).getData());
        }
    }

    private void writeToDisk(Object data, int[] page) {

        String file = data.toString() + ".pg";
        System.out.println(file);
        this.originalFile = new File("src/page_files/WorkingSet/" + file);

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(originalFile), "UTF-16"));

            for (int line: page) {
                writer.write(String.valueOf(line) + '\n');
            }
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Address functions
    public String fetchAddress(String physicalAddress) {
        String data = null;

        Node currentNode = RAM.head;

        while (currentNode != null)
        {
            if(currentNode.getData().equals(physicalAddress))
                return (String) currentNode.getData();
            else
                currentNode = currentNode.getNext();

        }

        return data;
    }
    private int[] getpage(String pageframe, String page) throws IOException {
        int[] pageinfo = new int[256];
        int count = 0;

        String filepath = "src/page_files/WorkingSet/"+ pageframe + page + ".pg";

        FileReader fileReader = new FileReader(filepath);
        InputStream inputStream = getClass().getResourceAsStream(filepath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String currentline = bufferedReader.readLine();

        while(currentline != null)
        {
          
            try{
                pageinfo[count++] = Integer.parseInt(String.valueOf(currentline));
            }catch(NumberFormatException e)
            {
                System.out.println("Not a number!");
                System.out.println(e.getMessage());

            }
            currentline = bufferedReader.readLine();
        }

        return pageinfo;
    }
    public boolean checkAddress(String VPage) {
        if(memoryContains(VPage))
            return true;
        else
            return false;
    }

    /**
     * Function to Copy Original Files manually and
     * populate the Hard Drive array
     * @throws IOException
     */
    private void initializeHD() throws IOException {
        CopyOriginalToWorkingSet();
        copyHardDrive();
    }
    private void copyHardDrive() throws IOException {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                HardDrive[i][j] = new page(toHex(i), toHex(j));
            }
        }
    }
    void CopyOriginalToWorkingSet() throws IOException {
        String pageFrame1;
        String pageFrame2;

        for (int i = 0; i <= 0xf; i++) {
            {
                for (int j = 0x0; j <= 0xf; j++) {
                    pageFrame1 = Integer.toHexString(i).toUpperCase();
                    pageFrame2 = Integer.toHexString(j).toUpperCase();

                    String page = pageFrame1 + pageFrame2 + ".pg";
                    this.originalFile = new File("src/page_files/Original/" + page);
                    this.newFile = new File("src/page_files/WorkingSet/" + page);
                    FileChannel src = new FileInputStream(originalFile.getAbsolutePath()).getChannel();
                    FileChannel workingset = new FileOutputStream(newFile.getAbsolutePath()).getChannel();
                    workingset.transferFrom(src, 0, src.size());
                }
            }
        }
    }
    public boolean memoryContains(String vPage) {
        Node currentNode = RAM.head;

        for(int i = 0; i< RAM.listCount; i++)
        {
            if(currentNode == null)
                return false;

            if(currentNode.getData().equals(vPage))
            {
                return true;
            }
            currentNode = currentNode.getNext();
        }

        Node nextNode = currentNode;

        while (currentNode != null && nextNode != RAM.head && nextNode != null)
        {
            nextNode = currentNode;
            if(currentNode.getData().equals(vPage))
                return true;
            currentNode = currentNode.getNext();
        }
        return false;
    }
    public void setRbit(String address) {
        this.RAM.getNodeWithAddress(address).setRBit(true);
    }

    // Misc Functions
    private String toHex(int i) {
        String hex = Integer.toHexString(i);

        return hex.toUpperCase();
    }

    /**
     * {@link PhysicalMemory.page} class
     */
    class page {
       int[] lines = new int[256];

       page(String pageframe, String page) throws IOException {
          lines = getpage(pageframe, page);

       }

       void printpages()
       {
           for (int i = 0; this.lines.length > i; i++) {
              System.out.println(lines[i]);
           }
       }
    }
    }
