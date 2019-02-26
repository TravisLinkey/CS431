import java.io.IOException;

public class MemoryManagementUnit {
    VirtualPageTable virtualPageTable = new VirtualPageTable();
    PhysicalMemory physicalMemory = new PhysicalMemory();
    TranslationLookasideBuffer tlb = new TranslationLookasideBuffer();
    boolean softMiss = false;
    boolean hardMiss = false;
    String VPage;
    String Offset;

    public MemoryManagementUnit() throws IOException {
    }

    /**
     * Function to check the TLB and Page Table for
     * an address and data. If it exists, output the
     * data. If it does not exist, fetch the data
     * from physical memory.
     * @return
     * @throws IOException
     */
    public String fetchAddress(String page, sBuilder sb) throws IOException {
        VPage = page.substring(0,2);
        Offset = page.substring(2);
        String address;

        // check tlb
        if(tlb.checkAddress(VPage))
        {
            address = tlb.fetchAddress(VPage);
        }
        //check physical memory
        else if(physicalMemory.checkAddress(VPage))
        {
            System.out.println("soft miss. . .");
            softMiss = true;
            address = physicalMemory.fetchAddress(VPage);
        }
        else
        {
            System.out.println("hard miss. . .");
            hardMiss = true;
            address = virtualPageTable.fetchAddress(VPage);
        }

        if(!tlb.contains(address)) {
            if (!tlb.isfull()) {
                System.out.println("Adding " + address + " to the TLB.");

                // bring in page to the TLB
                tlb.add(address, VPage);
            } else {
                // evict page to replace
                System.out.println("TLB is full. ");
                tlb.Fifo(address);
            }
        }
        else
                System.out.println("Page already in TLB.");

        // bring in page to the Memory
        System.out.println("Adding " + address + " to RAM.");
        int[] pagedata = copypageInfo(address);
        physicalMemory.add(address, pagedata, sb);
        physicalMemory.setRbit(address);

        return address;
    }

    /**
     * Function to copy the contents into a
     * one dimensional array
     * @param address
     * @return
     */
    private int[] copypageInfo(String address) {
        int add;
        int off;

        char[] digits = address.toCharArray();
        char pageframe = digits[0];
        char page = digits[1];
        add = Integer.parseInt(String.valueOf(pageframe), 16);
        off = Integer.parseInt(String.valueOf(page), 16);


        int[] newpage = physicalMemory.HardDrive[add][off].lines;

        return newpage;
    }

    /**
     * This function returns the contents of the page
     * at the passed offset value
     * @param address
     * @param offset
     * @return
     */
    public int getpageInfo(String address, String offset) {

        int offs;
        String vpage = address.substring(0,2);
        System.out.println(offset);
        offs = Integer.parseInt(String.valueOf(offset),16);

        Node pagedata = physicalMemory.RAM.getNodeWithAddress(vpage);

        System.out.println(pagedata.page[offs]);
       return pagedata.page[offs];
    }

    /**
     *
     * @param address
     * @param offset
     * @param currentline
     */
    public void writeToMemory(String address, String offset, String currentline, sBuilder sb) {
        int offs;

        String vpage = address.substring(0,2);
        offs = Integer.parseInt(String.valueOf(offset),16);

        Node pagedata = physicalMemory.RAM.getNodeWithAddress(vpage);
        pagedata.page[offs] = Integer.parseInt(currentline);
        sb.writeToFile2(6, String.valueOf(pagedata.page[offs]));
        System.out.print("writing: ");
        System.out.println(pagedata.page[offs]);
        setDbit(address);
    }

    // Bit manipulations
    public void setDbit(String address) {
        physicalMemory.RAM.getNodeWithAddress(address.substring(0,2)).dirty = true;
    }

    /**
     * This function resets the recent bit of
     * all the pages in RAM
     */
    public void resetRbits() {
        for (int i = 0; i < physicalMemory.RAM.listCount; i++) {
            physicalMemory.RAM.getNode(i).recent = false;
        }
    }
}
