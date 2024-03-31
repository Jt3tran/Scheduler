import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Collections;


class JobObject implements Comparable<JobObject> {
    private static int jobIdCounter = 1; 
    private int jobId;
    private int arrTime;
    private int cpuBurst;
    private int priority;
    private int exitTime;
    private int turnaroundTime;
    private int remainingTime;


public JobObject(int arrTime, int cpuBurst, int priority) {
    this.jobId = jobIdCounter++;
    this.arrTime = arrTime;
    this.cpuBurst = cpuBurst;
    this.priority = priority;
    this.remainingTime = cpuBurst;
}


public int getArrTime() {
    return arrTime;
}



public int getCpuBurst() {
    return cpuBurst;
}


public int getPriority() {
    return priority;
}


public int getExitTime() {
        return exitTime;
}


public int getTurnaroundTime() {
    return turnaroundTime;
}


public int getRemainingTime() {
    return remainingTime;
}


public void setExitTime(int exitTime) {
    this.exitTime = exitTime;
}


public void setTurnaroundTime() {
    turnaroundTime = exitTime - arrTime;
}


public void updateRemainingTime(int time) {
    remainingTime -= time;
}


public int compareTo(JobObject other) {
    return Integer.compare(this.arrTime, other.arrTime);
}


public String toString() {
    
    return String.format("%-10s%-10d%-10d%-10d%-10d",jobId,arrTime, cpuBurst, priority, exitTime, remainingTime);
    }
}


public class JobScheduling {
public static void main(String[] args) {
List<JobObject> jobList = generateJobs(25);

    System.out.println("\nFIFO Scheduling Results:");
    System.out.println(String.format("%-10s%-10s%-10s%-10s%-10s", "Job","Arrival", "cpuBurst", "Priority", "exitTime", "remainingTime"));
    FIFOScheduling(jobList);
    AverageTurnaroundTime(jobList, "FIFO");

    System.out.println("\nShortest Job First Results:");
    System.out.println(String.format("%-10s%-10s%-10s%-10s%-10s", "Job","Arrival", "cpuBurst", "Priority", "exitTime", "remainingTime"));
    ShortestJobFirst(jobList);
    AverageTurnaroundTime(jobList, "Shortest Job First");

    System.out.println("\nShortest Remaining Job Results :");
    System.out.println(String.format("%-10s%-10s%-10s%-10s%-10s", "Job","Arrival", "cpuBurst", "Priority", "exitTime", "remainingTime"));
    ShortestRemainingJob(jobList);
    AverageTurnaroundTime(jobList, "SRT (Preemptive)");

    System.out.println("\nHighest Priority Scheduling Results:");
    System.out.println(String.format("%-10s%-10s%-10s%-10s%-10s", "Job","Arrival", "cpuBurst", "Priority", "exitTime", "remainingTime"));
    HighestPriority(jobList);
    AverageTurnaroundTime(jobList, "Highest Priority");

    System.out.println("\nRound Robin Scheduling With Context Switch");
    System.out.println(String.format("%-10s%-10s%-10s%-10s%-10s", "Job","Arrival", "cpuBurst", "Priority", "exitTime", "remainingTime"));
    RoundRobin(jobList, 4);
    AverageTurnaroundTime(jobList, "Round Robin with Context Switch");

    System.out.println("\nRound Robin Scheduling Results (without Context Switch):");
    System.out.println(String.format("%-10s%-10s%-10s%-10s%-10s", "Job","Arrival", "cpuBurst", "Priority", "exitTime", "remainingTime"));
    RoundRobinWithoutContextSwitch(jobList, 4);
    AverageTurnaroundTime(jobList, "Round Robin (without Context Switch)");
}

public static void AverageTurnaroundTime(List<JobObject> jobList, String scheduler) {
    int totalTurnaroundTime = 0;

    for (JobObject job : jobList) {
        totalTurnaroundTime += job.getTurnaroundTime();
    }

    double averageTurnaroundTime = (double) totalTurnaroundTime / jobList.size();
    System.out.println(scheduler + " Average Turnaround Time: " + averageTurnaroundTime);
}

public static List<JobObject> generateJobs(int numJobs) {
    List<JobObject> jobs = new ArrayList<>();
        for (int i = 0; i < numJobs; i++) {
            int arrTime = (int) (Math.random() * 250) + 1;
            int cpuBurst = (int) (Math.random() * 14) + 2;
            int priority = (int) (Math.random() * 5) + 1;
            JobObject job = new JobObject(arrTime, cpuBurst, priority);
 jobs.add(job);
    }
        return jobs;
}

public static void FIFOScheduling(List<JobObject> jobList) {
    
    int currentTime = 0;

    for (JobObject job : jobList) {
        if (job.getArrTime() > currentTime) {
            currentTime = job.getArrTime();
        }

        job.setExitTime(currentTime + job.getCpuBurst());
        job.setTurnaroundTime();
        currentTime = job.getExitTime();
        
        System.out.println(job);
    }
}

public static void ShortestJobFirst(List<JobObject> jobList) {
    List<JobObject> sortedJobs = new ArrayList<>(jobList);
    Collections.sort(sortedJobs, (job1, job2) -> job1.getCpuBurst() - job2.getCpuBurst());
    
    int currentTime = 0;

    for (JobObject job : sortedJobs) {
        
        if (job.getArrTime() > currentTime) {
            currentTime = job.getArrTime();
        }

        
        job.setExitTime(currentTime + job.getCpuBurst());
        job.setTurnaroundTime();
        currentTime = job.getExitTime();

        
        System.out.println(job);
    }
}

 public static void ShortestRemainingJob(List<JobObject> jobList) {
        PriorityQueue<JobObject> queue = new PriorityQueue<>();
        int currentTime = 0;
        int currentJobIndex = 0;

        while (currentJobIndex < jobList.size() || !queue.isEmpty()) {
  
            while (currentJobIndex < jobList.size() && jobList.get(currentJobIndex).getArrTime() <= currentTime) {
                queue.offer(jobList.get(currentJobIndex));
                currentJobIndex++;
            }

            if (!queue.isEmpty()) {
                JobObject currentJob = queue.poll();
                int executionTime = Math.min(currentJob.getRemainingTime(), 1);
                currentTime += executionTime;
                currentJob.updateRemainingTime(executionTime);

                if (currentJob.getRemainingTime() == 0) {
                    currentJob.setExitTime(currentTime);
                    currentJob.setTurnaroundTime();
                    System.out.println(currentJob);
                } 
                else {
                   
                    queue.offer(currentJob);
                }
            } 
            else {
                currentTime++; 
            }
        }
    }

   public static void HighestPriority(List<JobObject> jobList) {
        List<JobObject> JobPriority = new ArrayList<>(jobList);
        Collections.sort(JobPriority);

        int currentTime = 0;

        for (JobObject job : JobPriority) {
            if (job.getArrTime() > currentTime) {
                currentTime = job.getArrTime();
            }

            job.setExitTime(currentTime + job.getCpuBurst());
            job.setTurnaroundTime();
            currentTime = job.getExitTime();

            System.out.println(job);
        }
    }

 public static void RoundRobin(List<JobObject> jobList, int timeQuantum) {
        Queue<JobObject> queue = new LinkedList<>();
        int currentTime = 0;
        int currentJobIndex = 0;

        while (currentJobIndex < jobList.size() || !queue.isEmpty()) {
            
            while (currentJobIndex < jobList.size() && jobList.get(currentJobIndex).getArrTime() <= currentTime) {
                queue.offer(jobList.get(currentJobIndex));
                currentJobIndex++;
            }

            if (!queue.isEmpty()) {
                JobObject currentJob = queue.poll();
                int executionTime = Math.min(currentJob.getRemainingTime(), timeQuantum);
                currentTime += executionTime;
                currentJob.updateRemainingTime(executionTime);

                
                if (currentJob.getRemainingTime() == 0) {
                    currentJob.setExitTime(currentTime);
                    currentJob.setTurnaroundTime();
                    System.out.println(currentJob);
                } 
                else {
                    
                    queue.offer(currentJob);
                }
            } 
            else {
                currentTime++; 
            }
        }
    }
    public static void RoundRobinWithoutContextSwitch(List<JobObject> jobList, int timeQuantum) {
        int currentTime = 0;
        int currentJobIndex = 0;

        while (currentJobIndex < jobList.size()) {
            JobObject currentJob = jobList.get(currentJobIndex);

            if (currentJob.getArrTime() <= currentTime) {
                
                currentJob.setExitTime(currentTime + currentJob.getCpuBurst());
                currentJob.setTurnaroundTime();
                System.out.println(currentJob);
                currentTime = currentJob.getExitTime();
                currentJobIndex++;
            } else {
                
                currentTime++;
            }
        }
    }
}














