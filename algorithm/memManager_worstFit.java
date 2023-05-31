package algorithm;

import java.util.Collections;
import java.util.List;

import sorters.SortBySizeDesc;
import structure.job;
import structure.memoryBlock;

public class memManager_worstFit extends memManager_firstFit {


    public memManager_worstFit(List<memoryBlock> memoryList, List<job> storageLocations) {
        super(memoryList, storageLocations);
        name = "Worst Fit";
    }

    @Override
    protected void sortMemory(){
        Collections.sort(memoryList, new SortBySizeDesc());
    }
    
}
