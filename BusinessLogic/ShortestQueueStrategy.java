package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.Comparator;
import java.util.List;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task){
        servers.stream().min(Comparator.comparingInt(s -> s.getTasks().length)).ifPresent(s -> s.addTask(task));
    }
}
