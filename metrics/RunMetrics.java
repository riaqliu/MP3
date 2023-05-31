package metrics;

import java.util.ArrayList;
import java.util.Vector;

import structure.memoryBlock;

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

    public double getHeavilyUsedPercentage(ArrayList<memoryBlock> validList){
        double total = 0d, count = 0d;


        for(InstanceMetrics m : Metrics){

            total++;
            for(memoryBlock m2 : validList){
                if(m.usedMemory.contains(m2)){
                    count++;
                    break;
                }
            }
            // System.out.println(m.usedMemory +" " +String.valueOf(validList) + count + "/" + total);
        }


        return count/total * 100d;
    }
}
