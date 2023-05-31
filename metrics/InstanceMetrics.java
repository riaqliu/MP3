package metrics;

public class InstanceMetrics {
    public Integer Throughput;
    public Double Utilization;
    public Integer QueueLength;
    public Double InternalFragmentation;
    public Integer InternalFragmentationValue;
    public Double UnusedMemory;

    public InstanceMetrics(){
        Throughput = 0;
        Utilization = 0d;
        QueueLength = 0;
        InternalFragmentation = 0d;
        InternalFragmentationValue = 0;
        UnusedMemory = 0d;
    }
}
