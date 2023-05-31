package sorters;

import java.util.Comparator;

import structure.memoryBlock;

public class SortBySizeDesc implements Comparator<memoryBlock> {
    
    public int compare(memoryBlock a, memoryBlock b)
    {
        return b.getSize() - a.getSize();
    }
}
