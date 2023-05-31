package algorithm;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import metrics.InstanceMetrics;
import metrics.RunMetrics;
import sorters.SortByUsageCount;
import sorters.SortbyNo;
import structure.*;


public class memManager_firstFit {
    protected Vector<memoryBlock> memoryList;
    protected Vector<job> jobList;
    protected Vector<job> finishedJobs;
    protected int time = 0;
    protected boolean run = true;
    protected job waitingJob = null;

    protected Integer totalMemory = 0;

    protected static String name;

    
    protected RunMetrics rm = new RunMetrics();
    protected InstanceMetrics im = new InstanceMetrics();

    public memManager_firstFit(List<memoryBlock> memoryList, List<job> joblist){
        this.memoryList = new Vector<memoryBlock>(memoryList);
        this.jobList = new Vector<job>(joblist);
        this.finishedJobs = new Vector<job>(joblist);
        name = "First Fit";
    }

    public void thread(){
        printJobs();

        // Before load
        printStorage();
        sortMemory();
        totalMemory = partition_countFreeMemory();

        // First time load
        time += 1;
        // fit();   // loads one job at t=0
        while(fit()); // loads everything at t=0
        printStorage();

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                im = new InstanceMetrics();

                run = false;
                time += 1;
                partition_mergeFreeMemory();
                run = runJobs()? true : run;
                while(fit());
                
                printStorage();

                // get instance metrics
                im.Utilization = (1d - (Double.valueOf(partition_countEmptyPartitions())/Double.valueOf(memoryList.size())))*100d;
                im.QueueLength = queue_countLength();
                im.InternalFragmentation = partition_getInternalFragmentation();
                im.UnusedMemory = partition_getUnused();
                printMetrics();

                rm.Metrics.add(im);
                
                if(!run){
                    // END
                    printAverageMetrics();
                    executorService.shutdown();
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    protected void sortMemory(){
        Collections.sort(memoryList, new SortbyNo());
    }

    protected void partition_mergeFreeMemory(){
        for(memoryBlock m : memoryList){
            m.mergeFreeMemory();
        }
    }

    protected Integer partition_countFreeMemory(){
        Integer freeMemory = 0;
        for(memoryBlock m : memoryList){
            freeMemory += m.countFreeMemory();
        }
        return freeMemory;
    }

    protected Integer partition_getTotalMemory(){
        Integer memory = 0;
        for(memoryBlock m : memoryList){
            memory += m.getSize();
        }
        return memory;

    }

    
    protected Integer partition_countEmptyPartitions(){
        Integer freePartitions = 0;
        for(memoryBlock m : memoryList){
            if(m.isFree()){
                freePartitions++;
            }
        }
        return freePartitions;
    }

    protected Double partition_getUnused(){
        Double sum = 0d;

        for(memoryBlock m : memoryList){
            if(!m.isUsed()){
                sum += 1d;
            }
        }

        return sum/Double.valueOf(memoryList.size()) * 100d;
    }

    protected String partition_getUsedCountList(){
        ArrayList<String> mList = new ArrayList<String>();

        for(memoryBlock m : partition_getSortedMemoryList_byUsageCount()){
            mList.add(m + " (" + String.valueOf(m.getUsedCount()) + ")");
        }
        StringBuilder str = new StringBuilder("");
        for (String eachstring : mList){
            str.append(eachstring).append(", ");
        }
        String commaseparatedlist = str.toString();

        if(commaseparatedlist.length()>0)
            commaseparatedlist = commaseparatedlist.substring(0, commaseparatedlist.length()-2);
        
        return commaseparatedlist;
    }
    protected String partition_getPartitionsHeavilyUsedPercentage(){

        HashMap<String,Integer> Histogram = new HashMap<>();

        for(memoryBlock m : memoryList){
            if(Histogram.containsKey(String.valueOf(m.getUsedCount()))){
                Integer count = Histogram.get(String.valueOf(m.getUsedCount()));
                // System.out.println(m.getUsedCount()+"//"+count);
                Histogram.put(String.valueOf(m.getUsedCount()), count + 1);
                // System.out.println(Histogram.get(String.valueOf(m.getUsedCount())));
            } else{
                Histogram.put(String.valueOf(m.getUsedCount()), 1);
            }
            // System.out.println("wtf"+Histogram);
        }
        Vector<Integer> keyList = new Vector<Integer>(Histogram.values());
        ArrayList<memoryBlock> memoryList2 = new ArrayList<memoryBlock>();

        for(memoryBlock m : memoryList){
            if(m.getUsedCount() > get3rdQuartile(keyList)){
                memoryList2.add(m);
            }
        }

        return String.valueOf(roundDecimals(Double.valueOf(memoryList2.size())/Double.valueOf(memoryList.size()), 5)*100d) 
            + "% (Condition: greater than "+ get3rdQuartile(keyList)+")";

    }

    protected String old_partition_getPartitionsHeavilyUsedPercentage(){

        HashMap<String,Integer> Histogram = new HashMap<>();

        for(memoryBlock m : memoryList){
            if(Histogram.containsKey(String.valueOf(m.getUsedCount()))){
                Integer count = Histogram.get(String.valueOf(m.getUsedCount()));
                // System.out.println(m.getUsedCount()+"//"+count);
                Histogram.put(String.valueOf(m.getUsedCount()), count + 1);
                // System.out.println(Histogram.get(String.valueOf(m.getUsedCount())));
            } else{
                Histogram.put(String.valueOf(m.getUsedCount()), 1);
            }
            // System.out.println("wtf"+Histogram);
        }
        Vector<Integer> keyList = new Vector<Integer>(Histogram.values());
        ArrayList<memoryBlock> memoryList2 = new ArrayList<memoryBlock>();

        for(memoryBlock m : memoryList){
            if(m.getUsedCount() > get3rdQuartile(keyList)){
                memoryList2.add(m);
            }
        }

        return String.valueOf(roundDecimals(rm.getHeavilyUsedPercentage(memoryList2), 5));

    }

    protected Integer get3rdQuartile(Vector<Integer> v){

        return (int)Math.floor(v.size()*(3d/4d));
    }

    protected Vector<memoryBlock> partition_getSortedMemoryList_byUsageCount(){
        Vector<memoryBlock> sortedMemoryList = new Vector<memoryBlock>(memoryList);
        Collections.sort(sortedMemoryList, new SortByUsageCount());
        return sortedMemoryList;
    }

    protected Double partition_getInternalFragmentation(){
        Double internalFragmentation = 0d;
        Vector<Double> record = new Vector<Double>();

        for(memoryBlock m : memoryList){
            if(!m.isFree()){
                Double mif = Double.valueOf(m.countFreeMemory())/Double.valueOf(m.getSize());
                record.add(mif);
            }
        }

        for(Double d : record){
            internalFragmentation += d;
        }
        if(record.size() > 0)
            internalFragmentation = internalFragmentation / Double.valueOf(memoryList.size()) * 100d;

        return internalFragmentation;
    }

    protected Integer queue_countLength(){
        return jobList.size();
    }

    protected Double partition_getInternalFragmentationValue(){
        Double internalFragmentation = 0d;
        Vector<Double> record = new Vector<Double>();

        for(memoryBlock m : memoryList){
            if(!m.isFree()){
                Double mif = Double.valueOf(m.countFreeMemory())/Double.valueOf(m.getSize());
                record.add(mif);
            }
        }

        for(Double d : record){
            internalFragmentation += d;
        }
        if(record.size() > 0)
            internalFragmentation = internalFragmentation / Double.valueOf(memoryList.size()) * 100d;

        return internalFragmentation;
    }



    protected Double jobs_getAverageWaitingTime(){
        double sum = 0;
        
        for(job j : finishedJobs){
            sum += j.getWaitingTime();
        }
        Double average = Double.valueOf(sum)/finishedJobs.size();

        return average;
    }


    protected boolean fit(){
        boolean changed = false, possibleFit = false;
        if(jobList.size() > 0){
            if(waitingJob == null){
                waitingJob = jobList.remove(0);
                changed = true;
            }
        }
        if(waitingJob != null){
            waitingJob.blocked();
            for(memoryBlock m : memoryList){
                if(waitingJob.getSize() <= m.getSize()) possibleFit = true;
                if(m.fillJob(waitingJob)){
                    changed = true;
                    waitingJob = null;
                    break;
                }
            }

        }
        if(!possibleFit) waitingJob = null; // discard job if cannot place it anywhere

        return changed;
    }

    protected boolean runJobs(){
        boolean changed = false;
        for(memoryBlock m : memoryList){
            Vector<job> doneJobs = new Vector<job>();
            for(job j : m.getJobList()){
                if(j.use()){
                    changed = true;
                    im.Throughput += 1;
                    im.usedMemory.add(m);
                }
                if(!(j instanceof nullJob) && j.getTime() <= 0){
                    doneJobs.add(j);
                    changed = true;
                }
            }
            for(job j : doneJobs){
                m.finishJob(j);
                finishedJobs.add(j);
            }
            
        }
        return changed;
    }
    protected void printStorage(){
        System.out.println("\nMemory ("+ time +"s)");
        System.out.println("[next memory:"+waitingJob+"]");
        Vector<memoryBlock> sortedMemoryList = new Vector<memoryBlock>(memoryList);
        Collections.sort(sortedMemoryList, new SortbyNo());
        for(memoryBlock mb : sortedMemoryList){
            System.out.println(mb + " (uses:" + mb.getUsedCount() + ")" + "["+ mb.getJobOccupied()+"]");
        }
    }

    protected void printJobs(){
        System.out.println("Jobs:");

        for(job j : jobList){
            System.out.print("\t" + j + " t:" + j.getTime() + " s:" + j.getSize() + "\n");
        }
    }

    protected void printMetrics(){
        String waitingTime = "None";
        if(waitingJob != null) waitingTime = String.valueOf(waitingJob.getWaitingTime()) + "s";

        System.out.println("\nMetrics:" + 
            "\n\tThroughput: " + im.Throughput +  
            "\n\tUtilization: " + roundDecimals(im.Utilization,2) + 
                "%\n\t\tUnused Partitions: " + im.UnusedMemory +"%" +
            "\n\tQueue Length: " + im.QueueLength + 
            "\n\tQueue Waiting Time: " + waitingTime + 
            "\n\tInternal Fragmentation: " + roundDecimals(im.InternalFragmentation, 5) + "%"
            );
    }

    protected void printAverageMetrics(){
        System.out.println("\n "+ name + " Average Metrics: (Total Time Steps = " + time + ")" + 
            "\n\tThroughput: " + roundDecimals(rm.averageThroughput(),5) +  " Jobs per time step" +
            "\n\tUtilization: " + roundDecimals(rm.averageUtilization(),5) + 
                "%\n\t\tUnused Partitions: " + im.UnusedMemory +"%" + 
                "\n\t\tHeavily Used Partitions: " + partition_getPartitionsHeavilyUsedPercentage() +
                "\n\t\t\t(occurences: " + old_partition_getPartitionsHeavilyUsedPercentage() + "% of all timesteps)" +
                "\n\t\tSorted Usage: [" + partition_getUsedCountList() +"]" +
            "\n\tQueue Length: " + roundDecimals(rm.averageQueueLength(), 5) +  " Jobs" +
            "\n\tQueue Waiting Time: " + roundDecimals(jobs_getAverageWaitingTime(),5) +  " secs" +
            "\n\tInternal Fragmentation: " + roundDecimals(rm.averageInternalFragmentation(),5) + "%"
            );
    }

    public static double roundDecimals(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
    
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
