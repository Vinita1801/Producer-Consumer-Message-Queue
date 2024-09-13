import com.vinita.messageQueue.Message;
import com.vinita.messageQueue.MessageQueue;
import com.vinita.producer.Producer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProducerTest {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;

    @Test
    void testProducer() throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue(3);
        List<Message> messages = List.of(new Message("Anil Patel"), new Message("Akash Mehta"));

        Producer producer = new Producer(messageQueue,messages);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join();

        assertEquals("Anil Patel", messageQueue.consume().getEmployeeName());
        assertEquals("Akash Mehta", messageQueue.consume().getEmployeeName());

        // Assert that the process kill is the next message in the queue
        assertEquals("PROCESS_KILL", messageQueue.consume().getEmployeeName(), "Process Kill should be the last message");
    }

    @Test
    void testProducerInterrupted() throws InterruptedException {
        // AtomicBoolean to track if the thread was interrupted
        AtomicBoolean wasInterrupted = new AtomicBoolean(false);

        MessageQueue messageQueue = new MessageQueue(2);
        List<Message> messages = List.of(new Message("Anil Patel"), new Message("Akash Mehta"));


        // Create a custom producer that sets the flag when interrupted
        Producer producer = new Producer(messageQueue, messages) {
            @Override
            public void run() {
                try {
                    // Produce messages and interrupt the thread after first message
                    for (Message message : messages) {
                        messageQueue.produce(message);

                        // Simulate an interruption after the first message
                        if ("Anil Patel".equals(message.getEmployeeName())) {
                            Thread.currentThread().interrupt();  // Interrupt the thread
                        }
                    }
                    messageQueue.produce(new Message("PROCESS_KILL"));  // Process Kill
                } catch (InterruptedException e) {
                    // Handle the interruption and set the flag
                    wasInterrupted.set(true);
                    Thread.currentThread().interrupt();  // Restore the interrupted status
                }
            }
        };

        // Create the producer thread and start it
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join();

        // Assert that the thread was interrupted and handled the InterruptedException
        assertTrue(wasInterrupted.get(), "Producer thread should have been interrupted.");
    }
}

