public class VirtualPageTable {
    page[] virtualpagetable;

    VirtualPageTable()
    {
        virtualpagetable = new page[256];
        initializeTable();
    }

    public void initializeTable() {

        for (int i = 0; i < virtualpagetable.length; i++) {
            if(i < 16)
                virtualpagetable[i] = new page("0"+Integer.toHexString(i));
            else
                virtualpagetable[i] = new page(Integer.toHexString(i));

        }

//            pg.valid = true;
//            pg.recent = true;
//            pg.dirty = false;
    }

    public String fetchAddress(String page) {
        return virtualpagetable[Integer.parseInt(page,16)].frameNumber;
    }

    /**
     * Class to represent a single page
     */
    class page {
        String frameNumber;
        boolean valid, recent, dirty;

        page(String frameNumber)
        {
            this.frameNumber = frameNumber.toUpperCase();
            this.valid = true;
            this.recent = true;
            this.dirty = false;
        }
    }
}
