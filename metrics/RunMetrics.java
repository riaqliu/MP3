package metrics;

import java.util.Vector;

public class RunMetrics {
    public Vector<InstanceMetrics> Metrics;

    public RunMetrics(){
        Metrics = new Vector<InstanceMetrics>();
    }

    public double averageThroughput(){
        Integer sum = 0;
        
        for(InstanceMetrics m : Metrics){
            sum += m.Throughput;
        }
        Double average = Double.valueOf(sum)/Metrics.size();

        return average;
    }

    
    public double averageUtilization(){
        double sum = 0;
        
        for(InstanceMetrics m : Metrics){
            sum += m.Utilization;
        }
        Double average = Double.valueOf(sum)/Metrics.size();

        return average;
    }
    
    public double averageQueueLength(){
        double sum = 0;
        
        for(InstanceMetrics m : Metrics){
            sum += m.QueueLength;
        }
        Double average = Double.valueOf(sum)/Metrics.size();

        return average;
    }

    public double averageInternalFragmentation(){
        double sum = 0;
        
        for(InstanceMetrics m : Metrics){
            sum += m.InternalFragmentation;
        }
        Double average = Double.valueOf(sum)/Double.valueOf(Metrics.size());

        return average;
    }

    public double averageInternalFragmentationValue(){
        double sum = 0;
        
        for(InstanceMetrics m : Metrics){
            sum += m.InternalFragmentationValue;
        }
        Double average = Double.valueOf(sum)/Double.valueOf(Metrics.size());

        return average;
    }

    public double averageUnusedMemory(){
        double sum = 0;
        
        for(InstanceMetrics m : Metrics){
            sum += m.UnusedMemory;
        }
        Double average = Double.valueOf(sum)/Double.valueOf(Metrics.size());

        return average;
    }
}
