import com.vinita.Main;
import com.vinita.consumer.Consumer;
import com.vinita.messageQueue.Message;
import com.vinita.messageQueue.MessageQueue;
import com.vinita.producer.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    private MessageQueue messageQueue;
    private AtomicInteger successCount;
    private AtomicInteger errorCount;

    @BeforeEach
    void setUp() {
        messageQueue = new MessageQueue(5);  // Assuming MessageQueue constructor takes capacity
        successCount = new AtomicInteger(0);
        errorCount = new AtomicInteger(0);
    }

   @Test
    void testAllMessagesProcessedSuccessfully() throws InterruptedException {

       Main.main(new String[]{});
        // Setup producer with valid messages
        Thread producerThread = new Thread(new Producer(messageQueue,
                List.of(
                        new Message("Akash Patel"),
                        new Message("Rohan Mehra"),
                        new Message("Nidhi Mehta"),
                        new Message("Karan Gohil"),
                        new Message("Akshar Patel"),
                        new Message("Dharmesh Bhatt"),
                        new Message("Manan Gohil"),
                        new Message("Gauri Kalra"))));  // No invalid message

        // Setup consumer
        Thread consumerThread = new Thread(new Consumer(messageQueue, successCount, errorCount));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        // Assertions
        assertEquals(8, successCount.get(), "All 8 messages should be processed successfully.");
        assertEquals(0, errorCount.get(), "No error should be encountered.");
    }

    @Test
    void testMessagesWithErrors() throws InterruptedException {

        Main.main(new String[]{});

        // Setup producer with one invalid message
        Thread producerThread = new Thread(new Producer(messageQueue,
                List.of(
                        new Message("Akash Patel"),
                        new Message("Rohan Mehra"),
                        new Message("Nidhi Mehta"),
                        new Message("123 3455"))));  // This one should be treated as an error

        // Setup consumer
        Thread consumerThread = new Thread(new Consumer(messageQueue, successCount, errorCount));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        // Assertions
        assertEquals(3, successCount.get(), "Three messages should be processed successfully.");
        assertEquals(1, errorCount.get(), "One error should be encountered.");
    }

    @Test
    void testEmptyQueue() throws InterruptedException {
        Main.main(new String[]{});

        // No messages in the producer
        Thread producerThread = new Thread(new Producer(messageQueue, List.of()));  // Empty list

        // Setup consumer
        Thread consumerThread = new Thread(new Consumer(messageQueue, successCount, errorCount));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        // Assertions
        assertEquals(0, successCount.get(), "No messages should be processed.");
        assertEquals(0, errorCount.get(), "No errors should be encountered.");
    }
}
