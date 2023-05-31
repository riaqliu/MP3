package structure;

public class oldMemoryBlock extends memoryBlock{

    public oldMemoryBlock(int blockNumber, int size) {
        super(blockNumber, size);
    }

    @Override
    public boolean fillJob(job j){
        for(int i = 0; i < 1; i++ ){
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
    
}
