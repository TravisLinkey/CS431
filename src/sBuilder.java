public class sBuilder {
    StringBuilder sb = new StringBuilder();
    String[] fileInfo = new String[7];


    public StringBuilder returnSB(){
        return sb;
    }
    public void writeToFile(String data){
        sb.append(data);
    }
    public void writeToFile2(int index, String data){fileInfo[index] = data;}
    public void writeToSB(){
        for(int x=0; x<7; x++){
            sb.append(fileInfo[x] + ',');
        }
        //sb.append("\n");
    }
    public void clearSBArray(){
        for(int x=0; x<7; x++){
           fileInfo[x] = " ";
        }
    }
}

