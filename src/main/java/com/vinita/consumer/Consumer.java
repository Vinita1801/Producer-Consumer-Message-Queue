package com.vinita.consumer;

import com.vinita.messageQueue.Message;
import com.vinita.messageQueue.MessageQueue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Consumer implements Runnable {
    // Regex allows employee name with characters and space
    private final String employeeNameRegex = "^[a-zA-Z]+[\\-'\\s]?[a-zA-Z ]+$";
    private final MessageQueue messageQueue;
    private final AtomicInteger successCount;
    private final AtomicInteger errorCount;

    public Consumer(MessageQueue messageQueue, AtomicInteger successCount, AtomicInteger errorCount) {
        this.messageQueue = messageQueue;
        this.successCount = successCount;
        this.errorCount = errorCount;
    }

    @Override
    public void run(){
        while (true){
            try {
                Message message = messageQueue.consume();
                if(message.getEmployeeName().equals("PROCESS_KILL")){
                    break;
                }
                processMessage(message);
                successCount.incrementAndGet();
            } catch (Exception e) {
                errorCount.incrementAndGet();
            }
        }
    }

    private void processMessage(Message message){
        String employeeName = message.getEmployeeName();

        if(!Pattern.matches(employeeNameRegex, employeeName)){
            throw new RuntimeException("Failed to process message" + message);
        } else {
            System.out.println("Message processed successfully: " + message);
        }
    }
}
