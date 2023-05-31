package sorters;

import java.util.Comparator;

import structure.memoryBlock;

public class SortByUsageCount implements Comparator<memoryBlock> {
    
    public int compare(memoryBlock a, memoryBlock b)
    {
        return a.getUsedCount() - b.getUsedCount();
    }
}
