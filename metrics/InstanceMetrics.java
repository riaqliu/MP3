package metrics;

import java.util.ArrayList;

import structure.memoryBlock;

public class InstanceMetrics {
    public Integer Throughput;
    public Double Utilization;
    public Integer QueueLength;
    public Double InternalFragmentation;
    public Integer InternalFragmentationValue;
    public Double UnusedMemory;
    public ArrayList<memoryBlock> usedMemory;

    public InstanceMetrics(){
        Throughput = 0;
        Utilization = 0d;
        QueueLength = 0;
        InternalFragmentation = 0d;
        InternalFragmentationValue = 0;
        UnusedMemory = 0d;
        usedMemory = new ArrayList<memoryBlock>();
    }


}
