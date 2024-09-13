import com.vinita.messageQueue.Message;
import com.vinita.messageQueue.MessageQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageQueueTest {

    @Test
    void testProducerAndConsume() throws Exception {
        MessageQueue messageQueue = new MessageQueue(3);
        Message message = new Message("Emiley Christian");
        messageQueue.produce(message);

        Message consumedMessage = messageQueue.consume();
        assertEquals(message, consumedMessage);
    }

    @Test
    void testMessageQueueIsFull() throws Exception {
        MessageQueue messageQueue = new MessageQueue(2);
        messageQueue.produce(new Message("Dhaval Desai"));
        messageQueue.produce(new Message("Aman Joshi"));

        // Test the producer is waiting when the queue is full
        Thread producerThread = new Thread(() -> {
            try {
                messageQueue.produce(new Message("Aman Joshi"));
            } catch (Exception e) {
                fail("Producer should wait instead of throwing an exception");
            }
        });
        producerThread.start();
        Thread.sleep(500); // Ensure the producer thread is waiting
        assertTrue(producerThread.isAlive());

        // Consume a message to free space in the queue
        messageQueue.consume();
        producerThread.join();
    }

    @Test
    void testMessageQueueIsEmpty() throws Exception {
        MessageQueue messageQueue = new MessageQueue(2);

        // Test the consumer is waiting when the queue is empty
        Thread consumerThread = new Thread(() -> {
            try {
                messageQueue.consume();
            } catch (Exception e) {
                fail("Consumer should wait instead of throwing an exception");
            }
        });
        consumerThread.start();
        Thread.sleep(500); // Ensure the consumer thread is waiting
        assertTrue(consumerThread.isAlive());

        // Produce a message to wake up the consumer
        messageQueue.produce(new Message("Sahil Patel"));
        consumerThread.join();
    }
}
