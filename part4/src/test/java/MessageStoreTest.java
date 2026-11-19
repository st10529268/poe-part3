import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageStoreTest {
    private MessageStore store;

    @Before
    public void setUp() {
        store = new MessageStore();
        store.loadPart3TestData("Developer");
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        Message[] sent = store.getSentMessagesArray();
        assertEquals(2, sent.length);
        assertEquals("Did you get the cake?", sent[0].getMessageText());
        assertEquals("It is dinner time !", sent[1].getMessageText());
    }

    @Test
    public void testDisplayLongestMessage() {
        String longest = store.displayLongestStoredMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchForMessageID() {
        String result = store.searchByMessageID("0838884567");
        assertTrue(result.contains("It is dinner time !"));
    }

    @Test
    public void testSearchMessagesByRecipient() {
        String result = store.searchMessagesByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        Message storedMsg2 = store.getStoredMessagesArray()[0];
        String response = store.deleteMessageByHash(storedMsg2.getMessageHash());
        assertEquals(
            "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
            response
        );
    }

    @Test
    public void testDisplayReport() {
        String report = store.displaySentMessagesReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));
    }
}