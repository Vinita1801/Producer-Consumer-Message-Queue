package com.vinita.producer;

import com.vinita.messageQueue.Message;
import com.vinita.messageQueue.MessageQueue;

import java.util.List;

public class Producer implements Runnable {
    private final MessageQueue messageQueue;
    private final List<Message> messages;

    public Producer(MessageQueue messageQueue, List<Message> messages) {
        this.messageQueue = messageQueue;
        this.messages = messages;
    }

    @Override
    public void run() {
        try {
            // Produce all the regular messages
            for (Message message : messages) {
                messageQueue.produce(message);
            }
            // Produce the special "process kill" message to signal the consumer to stop
            messageQueue.produce(new Message("PROCESS_KILL"));
        } catch (InterruptedException e) {
            // Log and handle the interrupted exception
            Thread.currentThread().interrupt();  // Restore interrupted status
            e.printStackTrace();  // Optionally log the error, but prefer proper logging in real code
        }
    }
}
