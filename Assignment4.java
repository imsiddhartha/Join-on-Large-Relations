

import java.io.File;
import java.io.IOException;

public class Assignment4 {
    
    public static void main(String[] args) throws IOException {
        int tuples = 100;
        String R=args[0],S=args[1],out;
        String table1=args[0];
        String table2=args[1];
        String type=args[2];
        int m=Integer.parseInt(args[3]);
        
        if(args[0].contains("\\")){
            String str[] = args[0].split("\\\\");
            R = str[str.length-1];
        }
        
        if(args[1].contains("\\")){
            String str[] = args[1].split("\\\\");
            S = str[str.length-1];
        }
        //out = R+"_"+S+args[2]+"_join";
        out = R+"_"+S+"_join";
        if(type.equalsIgnoreCase("sort")){
            SortMergeJoin s = new SortMergeJoin();
            s.sort(table1, table2, m,out,tuples);
        }
        
        if(type.equalsIgnoreCase("hash")){
            HashJoin h = new HashJoin();
            h.sort(table1, table2, m, out,tuples);
        }
        deletefiles(Integer.parseInt(args[3]));
    }

    private static void deletefiles(int m) {
		// TODO Auto-generated method stub
		try {
			
			for(int i=0;i<m;i++)
			{
				String s="tempS"+i;
				String r="tempR"+i;
				boolean success = (new File(s)).delete();
				success = (new File(r)).delete();
			}
		} catch (Exception e) {
			System.out.println("In deleeting fiels");
			// TODO: handle exception
		}
	}

}

