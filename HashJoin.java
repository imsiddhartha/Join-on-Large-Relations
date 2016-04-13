
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HashJoin {
    
    void sort(String inputR, String inputS, int m, String output, int tuples) throws FileNotFoundException, IOException {
        //long start = System.currentTimeMillis();
        open(m,inputR,inputS,tuples);
        String s;
        FileWriter fw = new FileWriter(output);
        StringBuilder sb = new StringBuilder();
        HashMap<String,ArrayList<String>> map = new HashMap<>();
        for(int i=0;i<m-1;i++){
            
            BufferedReader brR = new BufferedReader(new FileReader("tempR"+i+".txt"));
            BufferedReader brS = new BufferedReader(new FileReader("tempS"+i+".txt"));
           
            while((s=getnext(brR))!=null)
            {
                String str[] = s.split(" ");			//adding R's tupple in hashtablewith key R(y)
                if(map.containsKey(str[1])){
                    map.get(str[1]).add(str[0]);
                }
                else{
                    ArrayList<String> list = new ArrayList<>();
                    list.add(str[0]);
                    map.put(str[1], list);
                }
            }
                       
            while((s=getnext(brS))!=null){
                String str[] = s.split(" ");
                if(map.containsKey(str[0]))			//now finding S(y) in map 
                {
                    for(int j=0;j<map.get(str[0]).size();j++){
                        sb.append(map.get(str[0]).get(j));
                        sb.append(" ");
                        sb.append(str[0]);
                        sb.append(" ");
                        sb.append(str[1]);
                        sb.append("\n");
                        fw.write(sb.toString());
                        sb.setLength(0);
                    }
                }
            }
            brR.close();
            brS.close();
            map.clear();
        }
        for(int i=0;i<m-1;i++)				//m files for m buufers
        {
            File fR = new File("tempR"+i+".txt");
            File fS = new File("tempS"+i+".txt");
            fR.delete();
            fS.delete();
        }
        close(fw);
        //System.out.println(System.currentTimeMillis()-start);
    }

    private void open(int m, String inputR, String inputS, int tuples) throws FileNotFoundException, IOException {
        BufferedReader brR = new BufferedReader(new FileReader(inputR));
        BufferedReader brS = new BufferedReader(new FileReader(inputS));
        String s;
        FileWriter fwR[] = new FileWriter[m-1];
        FileWriter fwS[] = new FileWriter[m-1];
        for(int i=0;i<m-1;i++){
            fwR[i] = new FileWriter("tempR"+i+".txt");
            fwS[i] = new FileWriter("tempS"+i+".txt");
        }
        
        while((s=getnext(brR))!=null){
            String str[] = s.split(" ");
            int hashvalue = (971 + str[1].hashCode())%(m-1);
            fwR[hashvalue].write(s+"\n");
        }
        
        while((s=getnext(brS))!=null){
            String str[] = s.split(" ");
            int hashvalue = (971 + str[0].hashCode())%(m-1);
            fwS[hashvalue].write(s+"\n");
        }
        int c=0;
        for(int i=0;i<m-1;i++){
            close(fwR[i]);
            close(fwS[i]);
        }
    }
    
    private String getnext(BufferedReader br) throws IOException {
        return br.readLine();
    }

    private void close(FileWriter fw) throws IOException {
        fw.close();
    }
    
}
