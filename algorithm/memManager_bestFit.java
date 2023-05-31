package algorithm;

import java.util.Collections;
import java.util.List;

import sorters.SortBySizeAsc;
import structure.job;
import structure.memoryBlock;

public class memManager_bestFit extends memManager_firstFit {


    public memManager_bestFit(List<memoryBlock> memoryList, List<job> storageLocations) {
        super(memoryList, storageLocations);
        name = "Best Fit";
    }

    @Override
    protected void sortMemory(){
        Collections.sort(memoryList, new SortBySizeAsc());
    }
    
}
