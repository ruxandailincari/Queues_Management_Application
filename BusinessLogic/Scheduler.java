package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.servers = new ArrayList<>();
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;

        for(int i=0; i<this.maxNoServers; i++){
            Server server = new Server(this.maxTasksPerServer);
            this.servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy selectionPolicy){
        if(selectionPolicy == SelectionPolicy.SHORTEST_QUEUE){
            this.strategy = new ShortestQueueStrategy();
        }
        if(selectionPolicy == SelectionPolicy.SHORTEST_TIME){
            this.strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task task){
        strategy.addTask(this.servers, task);
    }

    public List<Server> getServers(){
        return this.servers;
    }

    public void stopSimulation(){
        this.servers.forEach(Server::stopServer);
    }
}
