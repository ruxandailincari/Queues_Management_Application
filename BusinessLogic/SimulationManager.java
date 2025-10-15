package org.example.BusinessLogic;

import org.example.GUI.SimulationFrame;
import org.example.Model.Server;
import org.example.Model.Task;
import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

public class SimulationManager implements Runnable{
    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> tasks;
    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;
    private int timeLimit = 60;
    private int numberOfQueues = 2;
    private int numberOfClients = 4;
    private int maxServiceTime = 5;
    private int minServiceTime = 3;
    private int minArrivalTime = 3;
    private int maxArrivalTime = 30;
    private boolean ok;
    public static final Logger LOGGER = Logger.getLogger(SimulationManager.class.getName());

    public SimulationManager(){
        this.frame = new SimulationFrame("Queues management");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
        this.frame.getStartButton().addActionListener(e -> {
            ok = true;
            //getInfoFromGui1();
            this.scheduler = new Scheduler(numberOfQueues, numberOfClients);
            this.tasks = new ArrayList<>();
            this.scheduler.changeStrategy(selectionPolicy);
            generateRandomTasks();
            Thread t = new Thread(this);
            t.start();
            stopCurrentSimulation();
        });
    }

    public void generateRandomTasks(){
        Random random = new Random();
        for(int i=0; i<numberOfClients; i++){
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            int serviceTime = random.nextInt(maxServiceTime - minServiceTime + 1) + minServiceTime;
            Task t = new Task(i+1, arrivalTime, serviceTime);
            this.tasks.add(t);
        }
        tasks.sort(Comparator.comparing(Task::getArrivalTime));
    }

    @Override
    public void run() {
        int currentTime = 0;
        int[] peakHourForMaxClients = {0, 0};
        float avgServiceTime = getAverageServiceTime();
        Map<Integer, Integer> waitingTimePerCustomer = getCustomerServiceTime();
        while(currentTime <= timeLimit && ok){
            List<Server> servers = scheduler.getServers();
            peakHourForMaxClients = updatePeakHour(servers, peakHourForMaxClients[0], currentTime, peakHourForMaxClients[1]);
            updateWaitingTimePerClient(servers, waitingTimePerCustomer);
            boolean ok1 = servers.stream().anyMatch(s -> s.getTasks().length != 0);
            logInfo(currentTime);
            showResultsGUI(currentTime);
            if(tasks.isEmpty() && !ok1){
                scheduler.stopSimulation();
                break;
            }
            dispatchNewTask(currentTime);
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        logFinalStats(waitingTimePerCustomer, avgServiceTime, peakHourForMaxClients[0]);
    }

    public void dispatchNewTask(int currentTime){
        Iterator<Task> iterator = this.tasks.iterator();
        while(iterator.hasNext()){
            Task t = iterator.next();
            if(t.getArrivalTime() == currentTime + 1){
                scheduler.dispatchTask(t);
                iterator.remove();
            }
        }
    }

    public void updateWaitingTimePerClient(List<Server> servers, Map<Integer, Integer> waitingTimePerCustomer){
        servers.stream().filter(s-> s.getTasks().length > 1).forEach(s -> {
            Task[] t = s.getTasks();
            for (int i = 1; i < t.length; i++) {
                Task t1 = t[i];
                int time = waitingTimePerCustomer.get(t1.getId());
                waitingTimePerCustomer.replace(t1.getId(), time + 1);
            }
        });
    }

    public int[] updatePeakHour(List<Server> servers, int peakHour, int currentTime, int maxClients){
        int[] peakHourM = {peakHour, maxClients};
        int currentClients = getAllCurrentClients(servers);
        if(currentClients > maxClients){
            peakHourM[0]= currentTime;
            peakHourM[1] = currentClients;
        }
        return peakHourM;
    }


    public void logInfo(int currentTime) {
        LOGGER.info("\nTime " + currentTime + "\n");
        LOGGER.info("Waiting clients: ");
        tasks.forEach(t -> LOGGER.info(t.toString() + ";"));

        List<Server> servers = scheduler.getServers();
        for (int i = 0; i < servers.size(); i++) {
            Server s = servers.get(i);
            LOGGER.info("\nQueue " + (i + 1) + " :");

            Task[] serverTasks = s.getTasks();
            if (serverTasks.length == 0) {
                LOGGER.info(" closed");
            } else {
                Arrays.stream(serverTasks).forEach(t1 -> LOGGER.info(t1.toString()));
            }
        }
    }

    public void logFinalStats(Map<Integer, Integer> waitingTimePerCustomer, float avgServiceTime, int peakHour){
        Collection<Integer> clientWaitingPeriod = waitingTimePerCustomer.values();
        float totalWaitingTime = 0;
        for(Integer i : clientWaitingPeriod){
            totalWaitingTime+=i;
        }
        totalWaitingTime = totalWaitingTime / numberOfClients;
        LOGGER.info("\nAverage waiting time: " + totalWaitingTime);
        LOGGER.info("\nAverage service time: " + avgServiceTime);
        LOGGER.info("\nPeak hour " + peakHour);
        frame.addOutput("Average waiting time: " + totalWaitingTime + "\n");
        frame.addOutput("Average service time: " + avgServiceTime + "\n");
        frame.addOutput("Peak hour: " + peakHour + "\n");
    }

    public void showResultsGUI(int currentTime){
        frame.addOutput("Time " + currentTime + "\n");
        StringBuilder waitingClients = new StringBuilder("Waiting clients: ");
        for(Task t : tasks){
            waitingClients.append(t.toString()).append(";");
        }
        frame.addOutput(waitingClients.toString() + "\n");
        List<Server> servers = scheduler.getServers();
        for(Server s : servers){
            StringBuilder inQueue = new StringBuilder();
            int index = servers.indexOf(s) + 1;
            frame.addOutput("Queue " + index +  " :");
            if(s.getTasks().length == 0){
                frame.addOutput(" closed" + "\n");
            }
            else {
                Task[] t = s.getTasks();
                for(Task t1 : t){
                    inQueue.append(t1.toString()).append(" ");
                }
                frame.addOutput(inQueue + "\n");
            }
        }
    }

    public float getAverageServiceTime(){
        return (float) tasks.stream().mapToDouble(Task::getServiceTime).average().orElse(0.0);
    }


    public int getAllCurrentClients(List<Server> servers){
        return servers.stream().mapToInt(s -> s.getTasks().length).sum();
    }

    public Map<Integer,Integer> getCustomerServiceTime(){
        return tasks.stream().collect(Collectors.toMap(Task::getId, Task::getServiceTime));
    }

    public void getInfoFromGui1(){
        if(frame.getTextTimeLimit().getText().isEmpty() || Integer.parseInt(frame.getTextTimeLimit().getText()) < 0){
            JOptionPane.showMessageDialog(new JFrame(), "Time limit must be a positive integer!");
            return;
        }
        this.timeLimit = Integer.parseInt(frame.getTextTimeLimit().getText());
        if(frame.getTextNbOfClients().getText().isEmpty() || Integer.parseInt(frame.getTextNbOfClients().getText()) < 0){
            JOptionPane.showMessageDialog(new JFrame(), "Number of clients must be a positive integer!");
            return;
        }
        this.numberOfClients = Integer.parseInt(frame.getTextNbOfClients().getText());
        if(frame.getTextNbOfQueues().getText().isEmpty() || Integer.parseInt(frame.getTextNbOfQueues().getText()) < 0){
            JOptionPane.showMessageDialog(new JFrame(), "Number of queues must be a positive integer!");
            return;
        }
        this.numberOfQueues = Integer.parseInt(frame.getTextNbOfQueues().getText());
        this.selectionPolicy = (SelectionPolicy) frame.getPolicyJComboBox().getSelectedItem();
        getInfoFromGui2();
    }

    public void getInfoFromGui2(){
        if(frame.getTextMinArrivalTime().getText().isEmpty() || Integer.parseInt(frame.getTextMinArrivalTime().getText()) < 0){
            JOptionPane.showMessageDialog(new JFrame(), "Min arrival time must be a positive integer!");
            return;
        }
        this.minArrivalTime = Integer.parseInt(frame.getTextMinArrivalTime().getText());
        if(frame.getTextMinServiceTime().getText().isEmpty() || Integer.parseInt(frame.getTextMinServiceTime().getText()) < 0){
            JOptionPane.showMessageDialog(new JFrame(), "Min service time must be a positive integer!");
            return;
        }
        this.minServiceTime = Integer.parseInt(frame.getTextMinServiceTime().getText());
        if(frame.getTextMaxArrivalTime().getText().isEmpty() || Integer.parseInt(frame.getTextMaxArrivalTime().getText()) < 0 || Integer.parseInt(frame.getTextMaxArrivalTime().getText()) < this.minArrivalTime){
            JOptionPane.showMessageDialog(new JFrame(), "Max arrival time must be a positive integer greater than min arrival time!");
            return;
        }
        this.maxArrivalTime = Integer.parseInt(frame.getTextMaxArrivalTime().getText());
        if(frame.getTextMaxServiceTime().getText().isEmpty() || Integer.parseInt(frame.getTextMaxServiceTime().getText()) < 0 || Integer.parseInt(frame.getTextMaxServiceTime().getText()) < this.minServiceTime){
            JOptionPane.showMessageDialog(new JFrame(), "Max service time must be a positive integer greater than min service time!");
            return;
        }
        this.maxServiceTime = Integer.parseInt(frame.getTextMaxServiceTime().getText());
    }

    public void stopCurrentSimulation(){
        this.frame.getStopButton().addActionListener(e -> ok = false);
    }

    public static void main(String[] args) {
        try {
            FileHandler f = new FileHandler("C:\\Users\\Ruxanda\\Desktop\\an2\\tp\\tema2\\log.txt");
            LOGGER.addHandler(f);
            SimpleFormatter formatter = new SimpleFormatter() {
                @Override
                public String format(LogRecord record){
                    return record.getMessage();
                }
            };
            f.setFormatter(formatter);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        LOGGER.setUseParentHandlers(false);

        SimulationManager sim = new SimulationManager();
    }
}
