package org.example.Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private boolean ok;

    public Server(int capacity) {
        this.tasks = new ArrayBlockingQueue<Task>(capacity);
        this.waitingPeriod = new AtomicInteger(0);
        this.ok = true;
    }

    public Task[] getTasks() {
        return this.tasks.toArray(new Task[0]);
    }

    public void addTask(Task newTask){
        this.tasks.add(newTask);
        this.waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    @Override
    public void run(){
        while(ok){
            try{
                Task task = this.tasks.peek();
                Thread.sleep(1000);
                if(task != null){
                    task.setServiceTime(task.getServiceTime() - 1);
                    this.waitingPeriod.decrementAndGet();
                    if(task.getServiceTime() == 0){
                        this.tasks.take();
                    }
                }
            } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
            }
        }
    }

    public AtomicInteger getWaitingPeriod(){
        return this.waitingPeriod;
    }

    public void stopServer(){
        this.ok = false;
    }
}
