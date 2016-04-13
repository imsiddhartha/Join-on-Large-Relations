

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Relation{
    String str;
    String x,y;
    int index;
}

class queueComparator implements Comparator <Relation>{
    @Override
    public int compare(Relation l1, Relation l2) {
            return l1.y.compareTo(l2.y);
    }
}

public class SortMergeJoin {
    
    void sort(String inputR, String inputS, int m, String output, int tuples) throws FileNotFoundException, IOException {
        //long start = System.currentTimeMillis();
        open(m,inputR,inputS,tuples);
        
        BufferedReader brR = new BufferedReader(new FileReader("SortedR.txt"));
        BufferedReader brS = new BufferedReader(new FileReader("SortedS.txt"));

        FileWriter fw = new FileWriter(output);
        StringBuilder sb = new StringBuilder();
        String sr,ss;
        int flagR=0,flagS=0;

        ArrayList<String> listR = new ArrayList<>();
        ArrayList<String> listS = new ArrayList<>();

        sr = getnext(brR);
        ss = getnext(brS);

        String strR[] = sr.split(" ");
        String strS[] = ss.split(" ");
        
        while(true){
            if(flagR==1 && flagS==1)
                break;
            if(flagR==0 && strR[1].compareTo(strS[0])<0)		//R(x,y)  with S(y,z) so r[1] and s[0] comparing
            {
                if((sr = getnext(brR))!=null)
                    strR = sr.split(" ");					//reading next of R
                else
                    flagR=1;
            }
            else if(flagS==0 && strR[1].compareTo(strS[0])>0){
                if((ss = getnext(brS))!=null)
                    strS = ss.split(" ");
                else
                    flagS=1;
            }
            else{
                String keyR[] = null,keyS[] = null;
                if(sr!=null){
                    listR.add(sr);
                    keyR = sr.split(" ");
                }
                else
                    flagR=1;
                    
                if(ss!=null){
                    listS.add(ss);
                    keyS= ss.split(" ");
                }
                else
                    flagS=1;
                while(true){
                    if((sr = getnext(brR))==null)
                        break;
                    strR = sr.split(" ");
                    if(strR[1].equals(keyR[1])){
                        listR.add(strR[0]+" "+strR[1]);
                    }
                    else
                        break;
                }
                
                while(true){
                    if((ss = getnext(brS))==null)
                        break;
                    strS = ss.split(" ");
                    if(strS[0].equals(keyS[0])){
                        listS.add(strS[0]+" "+strS[1]);
                    }
                    else
                        break;
                }
                
                for(int x = 0;x<listR.size();x++){
                    String tempR[] = listR.get(x).split(" ");
                    //System.out.println(tempR[0]);
                    for(int y=0;y<listS.size();y++){
                        String tempS[] = listS.get(y).split(" ");
                        //System.out.println(tempS.length+" "+tempR.length);
                        if(tempR[1].equals(tempS[0])){
                            sb.append(tempR[0]);
                            sb.append(" ");
                            sb.append(tempR[1]);
                            sb.append(" ");
                            sb.append(tempS[1]);
                            sb.append("\n");
                            fw.write(sb.toString());
                            sb.setLength(0);
                        }
                    }
                }
                listR.clear();
                listS.clear();
            }
        }
        close(fw,brR,brS);
        //System.out.println(System.currentTimeMillis() - start);
    }
    
    private void open(int m,String inputR, String inputS, int tuples) throws FileNotFoundException, IOException{
        String s;
        int TupleCounter = tuples * m;
        int c=0,i=0;
        /*For sorting and making sublist of relation R*/
        
        ArrayList<Relation> r = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(inputR));
        FileWriter f;
        FileWriter outR = new FileWriter("SortedR.txt");
        while((s=getnext(br))!=null){
            if(c == TupleCounter){
                f = new FileWriter("temp"+i+".txt");
                i++;
                Comparator<Relation> localeComparator = new Comparator<Relation>() {
                    public int compare(Relation o1, Relation o2) {
                        return o1.y.compareTo(o2.y);
                    }
		};
                Collections.sort(r, localeComparator);
                for(int j=0;j<c;j++){
                    f.write(r.get(j).str+"\n");
                }
                r.clear();
                f.close();
                c=0;
                Relation temp = new Relation();
                temp.str = s;
                String str[] = s.split(" ");
                temp.x = str[0];
                temp.y = str[1];
                r.add(temp);
                c++;
            }
            else{
               Relation temp = new Relation();
                temp.str = s;
                String str[] = s.split(" ");
                temp.x = str[0];
                temp.y = str[1];
                r.add(temp);
                c++;
            }
        }
        if(c!=0){
            f = new FileWriter("temp"+i+".txt");
            i++;
            Comparator<Relation> localeComparator = new Comparator<Relation>() {
                public int compare(Relation o1, Relation o2) {
                    return o1.y.compareTo(o2.y);
                }
            };
            Collections.sort(r, localeComparator);
            for(int j=0;j<c;j++){
                f.write(r.get(j).str+"\n");
            }
            r.clear();
            f.close();
        }
        BufferedReader in[] = new BufferedReader[i];
        for(int j=0;j<i;j++){
            in[j] = new BufferedReader(new FileReader("temp"+j+".txt"));
        }
        Relation r1;
        PriorityQueue<Relation> queue = queue=new PriorityQueue<Relation>(i,new queueComparator()); 
        for(int j=0;j<i;j++){
            s=getnext(in[j]);
            if(s!=null){
                r1 = new Relation(); 
                r1.str = s;
                String st[] = s.split(" ");
                r1.x = st[0];
                r1.y = st[1];
                r1.index = j;           
                queue.add(r1);
            }
        }
        String min;
        int index;
        while(!queue.isEmpty()){
            min = queue.peek().str;
            index=queue.peek().index;
            queue.remove();
            outR.write(min+"\n");
            if((s=getnext(in[index]))!=null){
                r1 = new Relation();
                r1.str = s;
                String st[] = s.split(" ");
                r1.x = st[0];
                r1.y = st[1];
                r1.index = index;     
                queue.add(r1);
            }
        }
        outR.close();
        for(int j=0;j<i;j++){
            in[j].close();
            File file = new File("temp"+j+".txt");
            file.delete();
        }
        
         /*For sorting and making sublist of relation S*/
        c=0;i=0;
        ArrayList<Relation> S = new ArrayList<>();
        br = new BufferedReader(new FileReader(inputS));
        FileWriter outS = new FileWriter("SortedS.txt");
        while((s=getnext(br))!=null){
            if(c == TupleCounter){
                f = new FileWriter("temp"+i+".txt");
                i++;
                Comparator<Relation> localeComparator = new Comparator<Relation>() {
                    public int compare(Relation o1, Relation o2) {
                        return o1.y.compareTo(o2.y);
                    }
		};
                Collections.sort(S, localeComparator);
                for(int j=0;j<c;j++){
                    f.write(S.get(j).str+"\n");
                }
                S.clear();
                f.close();
                c=0;
                Relation temp = new Relation();
                temp.str = s;
                String str[] = s.split(" ");
                temp.x = str[1];
                temp.y = str[0];
                S.add(temp);
                c++;
            }
            else{
               Relation temp = new Relation();
                temp.str = s;
                String str[] = s.split(" ");
                temp.x = str[1];
                temp.y = str[0];
                S.add(temp);
                c++;
            }
        }
        if(c!=0){
            f = new FileWriter("temp"+i+".txt");
            i++;
            Comparator<Relation> localeComparator = new Comparator<Relation>() {
                public int compare(Relation o1, Relation o2) {
                    return o1.y.compareTo(o2.y);
                }
            };
            Collections.sort(S, localeComparator);
            for(int j=0;j<c;j++){
                f.write(S.get(j).str+"\n");
            }
            S.clear();
            f.close();
        }
        in = new BufferedReader[i];
        for(int j=0;j<i;j++){
            in[j] = new BufferedReader(new FileReader("temp"+j+".txt"));
        }
        queue = queue=new PriorityQueue<Relation>(i,new queueComparator()); 
        for(int j=0;j<i;j++){
            s=getnext(in[j]);
            if(s!=null){
                r1 = new Relation(); 
                r1.str = s;
                String st[] = s.split(" ");
                r1.x = st[1];
                r1.y = st[0];
                r1.index = j;           
                queue.add(r1);
            }
        }
        while(!queue.isEmpty()){
            min = queue.peek().str;
            index=queue.peek().index;
            queue.remove();
            outS.write(min+"\n");
            if((s=getnext(in[index]))!=null){
                r1 = new Relation();
                r1.str = s;
                String st[] = s.split(" ");
                r1.x = st[1];
                r1.y = st[0];
                r1.index = index;     
                queue.add(r1);
            }
        }
        outS.close();
        for(int j=0;j<i;j++){
            in[j].close();
            File file = new File("temp"+j+".txt");
            file.delete();
        }
    }
    
    private String getnext(BufferedReader br) throws IOException {
        return br.readLine();
        
    }
    
    private void close(FileWriter fw,BufferedReader brR,BufferedReader brS) throws IOException{
        fw.close();
        brR.close();
        brS.close();
        File fileR = new File("SortedR.txt");
        File fileS = new File("SortedS.txt");
        fileR.delete();
        fileS.delete();
    }

    
    
    
    
}
