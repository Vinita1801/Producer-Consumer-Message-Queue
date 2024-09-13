import com.vinita.consumer.Consumer;
import com.vinita.messageQueue.Message;
import com.vinita.messageQueue.MessageQueue;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsumerTest {

    @Test
    void testConsumerSuccess() throws Exception {
        MessageQueue messageQueue = new MessageQueue(3);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        messageQueue.produce(new Message("Anil Mehta"));
        messageQueue.produce(new Message("Akash Patel"));
        messageQueue.produce(new Message("Ekta Doshi"));

        // Setup consumer
        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(500);

        // Assertions
        assertEquals(3, successCount.get());
        assertEquals(0, errorCount.get());
    }

    @Test
    void testConsumerFailure() throws Exception {
        MessageQueue messageQueue = new MessageQueue(6);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        messageQueue.produce(new Message("Heena Doshi"));
        messageQueue.produce(new Message("Anil Patel"));
        messageQueue.produce(new Message("203 321"));

        // Setup consumer
        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(500);

        // Assertions
        assertEquals(2, successCount.get());
        assertEquals(1, errorCount.get());
    }

    @Test
    void testConsumerTermination() throws Exception {
        MessageQueue messageQueue = new MessageQueue(3);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        messageQueue.produce(new Message("PROCESS_KILL"));

        // Setup consumer
        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(500);

        // Assertions
        assertEquals(0, successCount.get());
        assertEquals(0, errorCount.get());
    }

    @Test
    void testMultipleConsumersSuccessFailure() throws Exception {
        MessageQueue messageQueue = new MessageQueue(4);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        messageQueue.produce(new Message("Jalak Patel"));
        messageQueue.produce(new Message("890 567"));
        messageQueue.produce(new Message("754 890"));

        // Setup consumer
        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        Thread consumerThread1 = new Thread(consumer);
        Thread consumerThread2 = new Thread(consumer);
        consumerThread1.start();
        consumerThread2.start();
        consumerThread1.join(500);
        consumerThread2.join(500);

        // Assertions
        assertEquals(1, successCount.get());
        assertEquals(2, errorCount.get());
    }
}
