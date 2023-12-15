package cpu;

import java.util.*;

/**
 *
 * @author Kerollos Mansour
 * @author Mina Hany
 * @author Adel Magdy
 */

class Process {
    String name;
    String color;
    int arrivalTime;
    int burstTime;
    int priority;
    int waitTime;
    int turnaroundTime;
    int quantumTime;
    int remainingTime;
    
    public Process(String name, int arrivalTime , int burst, int priority, String color) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime ;
        this.burstTime = burst;
        this.priority = priority;
        this.waitTime = 0;
        this.turnaroundTime = 0;
        this.quantumTime = burstTime;
        this.remainingTime = burstTime;
        
    }
    
    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }
    
    public int getArrivalTime() {
        return arrivalTime;
    }
    
    @Override
    public String toString() {
        return "Process {" +
                "name='" + name + '\'' +
                ", burstTime=" + burstTime +
                ", arrivalTime=" + arrivalTime +
                ", priority=" + priority +
                ", quantumTime=" + quantumTime +
                ", remainingTime=" + remainingTime +
                '}';
    }
}
public class CPU {
    
    //Shortest- Job First (SJF) Non-Preemptive
    private static void SJF(List<Process> processes){
        
        processes.sort(Comparator.comparing(p -> p.burstTime));
        int currentTime = 0;
        double totalTurnaroundTime = 0;
        double totalWaitingTime = 0;
        
        for (Process process : processes) {
            // Wait for the process to arrive
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }
            // Execute the process
            process.waitTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            process.turnaroundTime = currentTime - process.arrivalTime;

            System.out.println("Turnaround Time for " + process.name + ": " + process.turnaroundTime);
            System.out.println("Waiting Time for " + process.name + ": " + process.waitTime + "\n");
            
            totalTurnaroundTime +=  process.turnaroundTime;
            totalWaitingTime += process.waitTime;
        }
//        processes.sort(Comparator.comparing(p -> p.name));        //Re-order the list as it created or inserted
        System.out.println("Average Turnaround Time: " + totalTurnaroundTime/processes.size());
        System.out.println("Average Waiting Time: " + totalWaitingTime/processes.size());
        
    }
        
    // Shortest Remaining Time First (SRTF) Scheduling
    private static void SRTF(List<Process> processes) {
            int currentTime = 0;
            double totalTurnaroundTime = 0;
            double totalWaitingTime = 0;
            int completedProcesses = 0;

            while (completedProcesses < processes.size()) {
                // Sort the processes based on remaining burst time first then priority and ArrivalTime
                Collections.sort(processes, Comparator.comparingInt(Process::getBurstTime)
                        .thenComparingInt(Process::getPriority)
                        .thenComparingInt(Process::getArrivalTime));
                Process currentProcess = processes.get(0);

                // Wait for the process to arrive
                if (currentTime < currentProcess.arrivalTime) {
                    currentTime = currentProcess.arrivalTime;
                }

                // Execute the process
                currentProcess.waitTime = currentTime - currentProcess.arrivalTime;
                currentTime += currentProcess.burstTime;
                currentProcess.turnaroundTime = currentTime - currentProcess.arrivalTime;

                System.out.println("\nTime " + currentTime + ": Executing " + currentProcess.name +
                        " (Color: " + currentProcess.color + ")");
                System.out.println("Turnaround Time for " + currentProcess.name + ": " + currentProcess.turnaroundTime);
                System.out.println("Waiting Time for " + currentProcess.name + ": " + currentProcess.waitTime + "\n");

                totalTurnaroundTime += currentProcess.turnaroundTime;
                totalWaitingTime += currentProcess.waitTime;

                // Remove the completed process
                processes.remove(currentProcess);
                completedProcesses++;
            }

            double avgTurnaroundTime = completedProcesses > 0 ? totalTurnaroundTime / completedProcesses : 0;
            double avgWaitingTime = completedProcesses > 0 ? totalWaitingTime / completedProcesses : 0;

            System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
            System.out.println("Average Waiting Time: " + avgWaitingTime);
        }
    
    //Priority Scheduling Non-Preemptive
    private static void priority(List<Process> processes){
        
        processes.sort(Comparator.comparing(p -> p.priority));
        int currentTime = 0;
        double totalTurnaroundTime = 0;
        double totalWaitingTime = 0;
        
        for (Process process : processes) {
            // Wait for the process to arrive
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }
            // Execute the process
            process.waitTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            process.turnaroundTime = currentTime - process.arrivalTime;

            System.out.println("Turnaround Time for " + process.name + ": " + process.turnaroundTime);
            System.out.println("Waiting Time for " + process.name + ": " + process.waitTime + "\n");
            
            totalTurnaroundTime +=  process.turnaroundTime;
            totalWaitingTime += process.waitTime;
        }
        processes.sort(Comparator.comparing(p -> p.name));        //Re-order the list as it created or inserted
        System.out.println("Average Turnaround Time: " + totalTurnaroundTime/processes.size());
        System.out.println("Average Waiting Time: " + totalWaitingTime/processes.size());
        
    }

     private static void AG(List<Process> processes) {
        int time = 0;
        while (!processes.isEmpty()) {
            Process currentProcess = getNextProcess(processes);
            processes.remove(currentProcess);
            currentProcess.remainingTime -= currentProcess.quantumTime;
            currentProcess.quantumTime = 0;
            System.out.println("At time " + time + " : " + currentProcess);

            if (currentProcess.remainingTime > 0) {
                currentProcess.quantumTime = getUpdatedQuantumTime(currentProcess.quantumTime);
                processes.add(currentProcess);
            }
            time++;
        }
    }

    private static Process getNextProcess(List<Process> processes) {
        Process currentProcess = processes.get(0);
        for (Process process : processes) {
            if (process.quantumTime < currentProcess.quantumTime) {
                currentProcess = process;
            }
        }
        return currentProcess;
    }

    private static int getUpdatedQuantumTime(int quantumTime) {
        return quantumTime + (int) Math.ceil(0.1 * quantumTime);
    }
    
    public static void main(String[] args) {

        //List for the processes
        List<Process> processes = new ArrayList<>();

        Process p1 = new Process("p1", 0, 17, 4, "Red");
        Process p2 = new Process("p2", 3, 6, 9, "Red");
        Process p3 = new Process("p3", 4, 10, 3, "Red");
        Process p4 = new Process("p4", 29, 4, 8, "Red");
        
        processes.add(p1);
        processes.add(p2);
        processes.add(p3);
        processes.add(p4);

        //        SJF(processes);           //Shortest- Job First (SJF) Non-Preemptive
        //        SRTF(processes);
        //        priority(processes);      //Priority Scheduling Non-Preemptive

        AG(processes);   
    }
}
