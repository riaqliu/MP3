package structure;

public class job {
    protected int jobStream = 0;
    protected int time = 0;
    protected int size = 0;
    protected int start = 0;
    protected int waitingTime;
    protected memoryBlock mem = null;

    public job(int JobStream, int Time, int size){
        this.jobStream = JobStream;
        this.time = Time;
        this.size = size;
        this.waitingTime = 0;
    }

    public void setMemoryBlock(memoryBlock mem){
        this.mem = mem;
    }

    public int getNumber(){
        return jobStream;
    }
    public int getSize(){
        return size;
    }

    public int getStart(){
        return start;
    }

    public int getEnd(){
        return size + start;
    }

    public int getTime(){
        return time;
    }

    public int getWaitingTime(){
        return waitingTime;
    }
    
    public void setStart(int start){
        this.start =  start;
    }

    public boolean use(){
        time -= 1;
        return true;
    }

    public void blocked(){
        waitingTime += 1;
    }

    

    @Override
    public String toString(){
        return "J" + String.valueOf(jobStream);
    }
}


