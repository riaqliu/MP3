package structure;

import java.util.*;


public class memoryBlock {
    protected int blockNumber;
    protected int size;
    protected int usedCount;

    // private int startAddress;
    protected Vector<job> jobList = new Vector<job>();

    public memoryBlock(int blockNumber, int size){
        this.blockNumber = blockNumber;
        this.size = size;
        this.usedCount = 0;
        nullJob empty = new nullJob();
        empty.setSize(size);

        jobList.add(empty);
    }
    
    public Vector<job> getJobList(){
        return jobList;
    }

    public int getNumber(){
        return blockNumber;
    }

    public int getSize(){
        return size;
    }


    public void mergeFreeMemory(){
        boolean changed = true;

        while(changed){
            changed = false;
            for(int i = 0; i < jobList.size(); i++){
                job memory = jobList.elementAt(i);
                if(memory instanceof nullJob){
                    if (i + 1 < jobList.size()){
                        job otherMemory = jobList.elementAt(i+1);
                        if(otherMemory instanceof nullJob){
                            jobList.add(i, new nullJob(memory.getStart(), memory.getSize() + otherMemory.getSize()));
                            jobList.remove(memory);
                            jobList.remove(otherMemory);
                            changed = true;
                        }
                    }
                }
            }
        }

    }

    public Integer countFreeMemory(){
        Integer unusedMemory = 0;
        for(job j : jobList){
            if(j instanceof nullJob){
                unusedMemory += j.getSize();
            }
        }

        return unusedMemory;
    }

    public boolean isFree(){
        for(job j : jobList){
            if(!(j instanceof nullJob)){
                return false;
            }
        }
        return true;
    }

    public boolean isUsed(){
        return (usedCount>0)?true:false;
    }

    public boolean fillJob(job j){
        for(int i = 0; i < jobList.size(); i++ ){
            job address = jobList.elementAt(i);
            int endpoint = address.getStart() + j.getSize();
            if(
                address instanceof nullJob &&
                endpoint <= this.size
            ){
                if (i + 1 < jobList.size())
                    if(endpoint >= jobList.elementAt(i+1).start){
                        return false;
                    }
                    
                j.setMemoryBlock(this);
                jobList.add(i, j);
                jobList.add(jobList.indexOf(address), new nullJob(endpoint, address.getSize()-j.getSize()));
                jobList.remove(address);
                this.usedCount++;
                return true;

            }
        }
        return false;
    }

    public void finishJob(job j){
        j.setMemoryBlock(null);
        jobList.add(jobList.indexOf(j), new nullJob(j.getStart(), j.getSize()));
        jobList.remove(j);
    }


    @Override
    public String toString(){
        return "B" + String.valueOf(blockNumber);
    }


    public String print(){
        return this + " [" + getJobOccupied() + "]";
    }

    public String getJobOccupied(){
        StringBuffer str = new StringBuffer("");
        for (job m : jobList){
            if(m instanceof nullJob){
                // str.append("(NULL[" + m.getSize() + " " + m.getTime() + "s <" + m.getStart() + ", " + m.getEnd() + ">]) ");
                str.append("(empty[" + m.getSize() + "]) ");
            }
            else{
                // str.append("(" + m + "[" + m.getSize() + " " + m.getTime() + "s <" + m.getStart() + ", " + m.getEnd() + ">]) ");
                str.append("("+ m + "[" + m.getSize() + " " + m.getTime() + "s]) ");
            }
        }
        return str.toString();

    }
}
