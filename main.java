import structure.*;
import algorithm.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;  

public class main {
    public static void main(String[] args){
    
        List<job> jList = makeJobs("csvFiles/jobList.csv");
        List<memoryBlock> mList = makeMemoryBlocks("csvFiles/memoryList.csv");

        memManager_firstFit ffManager = new memManager_firstFit(mList, jList);
        // memManager_bestFit bfManager = new memManager_bestFit(mList, jList);
        // memManager_worstFit wfManager = new memManager_worstFit(mList, jList);

        ffManager.thread();
        // bfManager.thread();
        // wfManager.thread();
        
    }

    public static List<job> makeJobs(String path){
        String line = "";  
        String splitBy = ",";
        List<job> lis = new ArrayList<job>();
        try{  
            //parsing a CSV file into BufferedReader class constructor  
            BufferedReader br = new BufferedReader(new FileReader(path));
            br.readLine();
            while ((line = br.readLine()) != null)   //returns a Boolean value  
            {  
                line = line.replaceAll("\\s+", "");
                String[] job = line.split(splitBy);    // use comma as separator  
                //System.out.println("JOB: [stream: " + job[0] + ", time: " + job[1] + ", size:" + job[2] + "]");
                int[] nlist = {(int) Integer.valueOf(job[0]), (int) Integer.valueOf(job[1]), (int) Integer.valueOf(job[2])};
                job jobline = createStorage(nlist);

                lis.add(jobline);
            }
            br.close();
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        return lis;
    }

    public static List<memoryBlock> makeMemoryBlocks(String path){
        String line = "";  
        String splitBy = ",";
        LinkedList<memoryBlock> lis = new LinkedList<memoryBlock>();
        try{  
            //parsing a CSV file into BufferedReader class constructor  
            BufferedReader br = new BufferedReader(new FileReader(path));
            br.readLine();
            while ((line = br.readLine()) != null)   //returns a Boolean value  
            {  
                line = line.replaceAll("\\s+", "");
                String[] mem = line.split(splitBy);    // use comma as separator  
                int[] nlist = {(int) Integer.valueOf(mem[0]), (int) Integer.valueOf(mem[1])};
                memoryBlock jobline = createOldMemoryBlock(nlist);
                // memoryBlock jobline = createMemoryBlock(nlist);
                lis.add(jobline);
            }
            br.close();
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }  
        return lis;
    }

    public static job createStorage(int[] arr){
        return new job(arr[0] ,arr[1], arr[2]);
    }
    public static memoryBlock createMemoryBlock(int[] arr){
        return new memoryBlock(arr[0] ,arr[1]);
    }
    public static oldMemoryBlock createOldMemoryBlock(int[] arr){
        return new oldMemoryBlock(arr[0] ,arr[1]);
    }
}
