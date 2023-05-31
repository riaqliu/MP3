package sorters;

import java.util.Comparator;

import structure.job;

public class SortBySize_job implements Comparator<job> {
    
    public int compare(job a, job b)
    {
        return a.getSize() - b.getSize();
    }
}
