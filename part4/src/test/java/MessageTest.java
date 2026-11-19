import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    private Message message;

    public MessageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        message = new Message(1, "Thabo Mokoena", "+27834557896", "Did you get the cake?", "1234567890");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCheckMessageID() {
        boolean result = message.checkMessageID();
        assertTrue(result);
    }

    @Test
    public void testCheckRecipientCell() {
        String expResult = "Cell phone number successfully captured.";
        String result = message.checkRecipientCell();
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckMessageLength() {
        String expResult = "Message ready to send.";
        String result = message.checkMessageLength();
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateMessageHash() {
        String expResult = "12:1:DIDCAKE";
        String result = message.createMessageHash();
        assertEquals(expResult, result);
    }

    @Test
    public void testSentMessage() {
        assertEquals("Message successfully sent.", message.SentMessage(1));
        assertEquals("Press 0 to delete the message", message.SentMessage(2));
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }

    @Test
    public void testPrintMessages() {
        String result = message.printMessages();
        assertTrue(result.contains("Message ID: 1234567890"));
        assertTrue(result.contains("Message Hash: 12:1:DIDCAKE"));
        assertTrue(result.contains("Sender: Thabo Mokoena"));
        assertTrue(result.contains("Recipient: +27834557896"));
        assertTrue(result.contains("Message: Did you get the cake?"));
    }

    @Test
    public void testGetMessageID() {
        assertEquals("1234567890", message.getMessageID());
    }

    @Test
    public void testGetNumMessagesSent() {
        assertEquals(1, message.getNumMessagesSent());
    }

    @Test
    public void testGetSender() {
        assertEquals("Thabo Mokoena", message.getSender());
    }

    @Test
    public void testGetRecipient() {
        assertEquals("+27834557896", message.getRecipient());
    }

    @Test
    public void testGetMessageText() {
        assertEquals("Did you get the cake?", message.getMessageText());
    }

    @Test
    public void testGetMessageHash() {
        assertEquals("12:1:DIDCAKE", message.getMessageHash());
    }
}