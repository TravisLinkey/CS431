public class TranslationLookasideBuffer {
    page[] tlb = new page[8];
    int count = 0;

    public String fetchAddress(String page) {
        return tlb[getindex(page)].pageFrame;
    }
    private int getindex(String page) {
        for (int i = 0; i < tlb.length; i++) {
            if(tlb[i].vPage.equals(page))
                return i;
        }
        return 0;
    }
    public boolean checkAddress(String page) {
        for (int i = 0; i < count; i++) {
                if(tlb[i].vPage.equals(page))
                    return true;
        }
        return false;
    }
    public void add(String page, String VPage) {

            count++;
            this.addNew(page, VPage);
    }
    void Fifo(String page) {

        if(!arrayFull())
        {
            for (int i = 0; i < tlb.length; i++) {
                if(tlb[i] == null)
                    tlb[i].vPage.equals(page);
            }
        }

        System.out.println("Evicting " + tlb[0].pageFrame + " from the TLB. Adding " + page);

        // shift every element down
        for (int i = 0; i < tlb.length-1; i++) {
           tlb[i] = tlb[i+1];
        }

        tlb[tlb.length-1] = new page(page,page);
    }
    boolean arrayFull() {
        if(count == 16)
                return true;
        return false;
    }
    private void addNew(String page, String VPage) {
        for (int i = 0; i < tlb.length; i++) {
            if(tlb[i] == null)
            {
                tlb[i] = new page(VPage,page);
                break;
            }
        }
    }
    public boolean isfull() {
        if(count > 7)
                return true;
        return false;

    }
    public boolean contains(String address) {
        for (int i = 0; i < count; i++) {
            if(tlb[i].vPage.equals(address))
                return true;
        }
        return false;
    }

    class page{
        String vPage;
        boolean valid;
        boolean recent;
        boolean dirty;
        String pageFrame;

        page(String virtualPage, String pageFrame)
        {
           this.vPage = virtualPage;
           this.valid = true;
           this.recent = true;
           this.dirty = false;
           this.pageFrame = pageFrame;
        }
    }
}
