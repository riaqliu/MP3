package sorters;

import java.util.Comparator;

import structure.memoryBlock;

public class SortbyNo implements Comparator<memoryBlock> {
    public int compare(memoryBlock a, memoryBlock b)
    {
        return a.getNumber() - b.getNumber();
    }
}
