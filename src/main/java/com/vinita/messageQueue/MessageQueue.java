package com.vinita.messageQueue;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    private final Queue<Message> queue;
    private final int queueSize;

    public MessageQueue(int queueSize) {
        queue = new LinkedList<>();
        this.queueSize = queueSize;
    }

    // The thread which access this method will acquire a buffer lock as it is a synchronized method
    public synchronized void produce(Message message) throws InterruptedException {
        while (queue.size() == queueSize){
            System.out.println("Message Queue is full, Producer is waiting for Consumer to consume messages");
            // producer thread will wait till the consumer notifies the message queue has some space or empty
            wait();
        }
        queue.add(message);
        // notify thread that a new message is ready to be processed in queue
        notify();
        System.out.println("Produced :" + message);
    }

    // The thread which access this method will acquire a buffer lock as it is a synchronized method
    public synchronized Message consume() throws InterruptedException {
        while (queue.isEmpty()){
            System.out.println("Message Queue is empty, Consumer is waiting for Producer to produce messages");
            // producer thread will wait till the consumer notifies the message queue has some space or empty
            wait();
        }
        Message message = queue.poll();
        System.out.println("Consumed :" + message);
        // notify thread that one message is been consumed from queue
        notify();
        return message;
    }
}
