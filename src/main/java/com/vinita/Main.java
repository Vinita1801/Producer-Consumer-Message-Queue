package com.vinita;

import com.vinita.consumer.Consumer;
import com.vinita.messageQueue.Message;
import com.vinita.messageQueue.MessageQueue;
import com.vinita.producer.Producer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue(5);

        // Using AtomicInteger for thready safety and atomic operations
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        // Producer Thread producing messages to queue
        Thread producerThread = new Thread(new Producer(messageQueue,
                List.of(new Message("Akash Patel"),
                        new Message("Rohan Mehra"),
                        new Message("Nidhi Mehta"),
                        new Message("Karan Gohil"),
                        new Message("Akshar Patel"),
                        new Message("Dharmesh Bhatt"),
                        new Message("Manan Gohil"),
                        new Message("Gauri Kalra"),
                        new Message("123 3455"),
                        new Message("321 6857"),
                        new Message("PROCESS_KILL"))));

        // Consumer Thread consuming messages from queue with success and error messages count
        Thread consumerThread = new Thread(new Consumer(messageQueue,successCount,errorCount));

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch(InterruptedException e){
            e.printStackTrace();
        }


        System.out.println("Total messages processed successfully: " + successCount.get());
        System.out.println("Total errors encountered: " + errorCount.get());
    }

}
