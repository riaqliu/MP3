package sorters;

import java.util.Comparator;

import structure.memoryBlock;

public class SortBySizeAsc implements Comparator<memoryBlock> {
    
    public int compare(memoryBlock a, memoryBlock b)
    {
        return a.getSize() - b.getSize();
    }
}
