import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// ==========================================
// 1. LOGIN CLASS (Part 1)
// ==========================================
class Login {
    String username;
    String password;
    String cellPhoneNumber;
    String firstName;
    String lastName;

    
    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // Checks password complexity requirements
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) hasCapital = true;
            if (Character.isDigit(ch)) hasNumber = true;
            if (!Character.isLetterOrDigit(ch)) hasSpecial = true;
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    // Checks if cellphone number starts with international code (+27) and has correct length
    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber != null && cellPhoneNumber.startsWith("+27") && cellPhoneNumber.length() == 12;
    }

    // Handles registration status messages
    public String registerUser() {
        String userMsg = checkUserName()
            ? "Username successfully captured."
            : "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";

        String passMsg = checkPasswordComplexity()
            ? "Password successfully captured."
            : "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";

        String cellMsg = checkCellPhoneNumber()
            ? "Cell phone number successfully added."
            : "Cell phone number incorrectly formatted or does not contain international code.";

        if (checkUserName() && checkPasswordComplexity() && checkCellPhoneNumber()) {
            return userMsg + "\n" + passMsg + "\n" + cellMsg;
        } else {
            return (!checkUserName() ? userMsg : (!checkPasswordComplexity() ? passMsg : cellMsg));
        }
    }

    // Verifies user login credentials
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return this.username != null && this.password != null
            && this.username.equals(enteredUsername)
            && this.password.equals(enteredPassword);
    }

    // Returns formatted login welcome message
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + " " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}

// ==========================================
// 2. MESSAGE CLASS (Part 2 & Part 3)
// ==========================================
final class Message {
    private final String messageID;
    private final int numMessagesSent;
    private final String sender;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    public Message(int messageNumber, String recipient, String messageText) {
        this(messageNumber, "CurrentUser", recipient, messageText, null);
    }

    public Message(int messageNumber, String sender, String recipient, String messageText, String fixedMessageID) {
        this.numMessagesSent = messageNumber;
        this.sender = sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = (fixedMessageID != null && !fixedMessageID.isBlank()) ? fixedMessageID : generateMessageID();
        this.messageHash = createMessageHash();
    }

   
    private String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.startsWith("+") && recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Validates message length (max 250 characters)
    public String checkMessageLength() {
        if (messageText != null && messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = (messageText != null ? messageText.length() : 0) - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

   
    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || messageText == null || messageText.trim().isEmpty()) {
            return "";
        }

        String firstTwoID = messageID.substring(0, 2);
        String cleanedText = messageText.replaceAll("[^a-zA-Z0-9\\s]", " ").trim();
        String[] words = cleanedText.isEmpty() ? messageText.trim().split("\\s+") : cleanedText.split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        String combinedWords = (firstWord + lastWord).toUpperCase();
        return firstTwoID + ":" + numMessagesSent + ":" + combinedWords;
    }

    // Handles option selection (1: Send, 2: Disregard, 3: Store)
    public String SentMessage(int choice) {
        return switch (choice) {
            case 1 -> "Message successfully sent.";
            case 2 -> "Press 0 to delete the message";
            case 3 -> "Message successfully stored.";
            default -> "Invalid option selected.";
        };
    }

    // Formats and returns full details of the message
    public String printMessages() {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Sender: " + sender + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText;
    }

    public String getMessageID() { return messageID; }
    public int getNumMessagesSent() { return numMessagesSent; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
}


class MessageStore {
    private static final String JSON_FILE_PATH = "stored_messages.json";

    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();

    public void recordSentMessage(Message msg) {
        sentMessages.add(msg);
        messageHashes.add(msg.getMessageHash());
        messageIDs.add(msg.getMessageID());
    }

    public void recordDisregardedMessage(Message msg) {
        disregardedMessages.add(msg);
        messageHashes.add(msg.getMessageHash());
        messageIDs.add(msg.getMessageID());
    }

    public void recordStoredMessage(Message msg) {
        storedMessages.add(msg);
        messageHashes.add(msg.getMessageHash());
        messageIDs.add(msg.getMessageID());
        saveStoredMessagesToJson();
    }

    public Message[] getSentMessagesArray() {
        return sentMessages.toArray(Message[]::new);
    }

    public Message[] getDisregardedMessagesArray() {
        return disregardedMessages.toArray(Message[]::new);
    }

    public Message[] getStoredMessagesArray() {
        return storedMessages.toArray(Message[]::new);
    }

    public String[] getMessageHashesArray() {
        return messageHashes.toArray(String[]::new);
    }

    public String[] getMessageIDsArray() {
        return messageIDs.toArray(String[]::new);
    }

    
    public void loadPart3TestData(String currentSender) {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();

        Message m1 = new Message(1, currentSender, "+27834557896", "Did you get the cake?", null);
        Message m2 = new Message(2, currentSender, "+27838884567", "Where are you? You are late! I have asked you to be on time.", null);
        Message m3 = new Message(3, currentSender, "+27834484567", "Yohoooo, I am at your gate.", null);
        Message m4 = new Message(4, currentSender, "0838884567", "It is dinner time !", "0838884567");
        Message m5 = new Message(5, currentSender, "+27838884567", "Ok, I am leaving without you.", null);

        recordSentMessage(m1);
        recordStoredMessage(m2);
        recordDisregardedMessage(m3);
        recordSentMessage(m4);
        recordStoredMessage(m5);
    }

    // 
    public void saveStoredMessagesToJson() {
        try (PrintWriter out = new PrintWriter(new FileWriter(JSON_FILE_PATH))) {
            out.println("[");
            for (int i = 0; i < storedMessages.size(); i++) {
                Message m = storedMessages.get(i);
                out.println("  {");
                out.println("    \"messageID\": \"" + escapeJson(m.getMessageID()) + "\",");
                out.println("    \"messageNumber\": " + m.getNumMessagesSent() + ",");
                out.println("    \"sender\": \"" + escapeJson(m.getSender()) + "\",");
                out.println("    \"recipient\": \"" + escapeJson(m.getRecipient()) + "\",");
                out.println("    \"messageText\": \"" + escapeJson(m.getMessageText()) + "\",");
                out.println("    \"messageHash\": \"" + escapeJson(m.getMessageHash()) + "\"");
                out.print("  }");
                if (i < storedMessages.size() - 1) {
                    out.println(",");
                } else {
                    out.println();
                }
            }
            out.println("]");
        } catch (IOException e) {
            System.out.println("Error saving JSON file: " + e.getMessage());
        }
    }

    // Reads stored messages from stored_messages.json into storedMessages list
    public void loadStoredMessagesFromJson() {
        File file = new File(JSON_FILE_PATH);
        if (!file.exists()) {
            return;
        }

        storedMessages.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line.trim());
            }
            String json = jsonBuilder.toString().trim();
            if (json.length() <= 2) {
                return;
            }

            String inner = json.substring(1, json.length() - 1).trim();
            if (inner.isEmpty()) {
                return;
            }

            String[] objects = inner.split("\\},\\s*\\{");
            for (String rawObj : objects) {
                String obj = rawObj;
                if (!obj.startsWith("{")) obj = "{" + obj;
                if (!obj.endsWith("}")) obj = obj + "}";

                String id = extractJsonString(obj, "messageID");
                int num = extractJsonInt(obj, "messageNumber");
                String sender = extractJsonString(obj, "sender");
                String recipient = extractJsonString(obj, "recipient");
                String text = extractJsonString(obj, "messageText");

                Message loaded = new Message(num, sender, recipient, text, id);
                storedMessages.add(loaded);
                if (!messageIDs.contains(loaded.getMessageID())) {
                    messageIDs.add(loaded.getMessageID());
                }
                if (!messageHashes.contains(loaded.getMessageHash())) {
                    messageHashes.add(loaded.getMessageHash());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    private String unescapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\\"", "\"")
                    .replace("\\\\", "\\")
                    .replace("\\n", "\n")
                    .replace("\\r", "\r");
    }

    private String extractJsonString(String obj, String key) {
        String search = "\"" + key + "\"";
        int keyIdx = obj.indexOf(search);
        if (keyIdx == -1) return "";
        int colonIdx = obj.indexOf(":", keyIdx + search.length());
        int firstQuote = obj.indexOf("\"", colonIdx + 1);
        if (firstQuote == -1) return "";

        StringBuilder sb = new StringBuilder();
        boolean escaping = false;
        for (int i = firstQuote + 1; i < obj.length(); i++) {
            char c = obj.charAt(i);
            if (escaping) {
                sb.append("\\").append(c);
                escaping = false;
            } else if (c == '\\') {
                escaping = true;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return unescapeJson(sb.toString());
    }

    private int extractJsonInt(String obj, String key) {
        String search = "\"" + key + "\"";
        int keyIdx = obj.indexOf(search);
        if (keyIdx == -1) return 0;
        int colonIdx = obj.indexOf(":", keyIdx + search.length());
        int endIdx = colonIdx + 1;
        while (endIdx < obj.length() && (obj.charAt(endIdx) == ' ' || Character.isDigit(obj.charAt(endIdx)))) {
            endIdx++;
        }
        String digits = obj.substring(colonIdx + 1, endIdx).trim();
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    // 2a. Display sender and recipient of all stored messages
    public String displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder("--- Stored Messages: Sender & Recipient ---\n");
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(m.getSender())
              .append(" | Recipient: ").append(m.getRecipient()).append("\n");
        }
        return sb.toString().trim();
    }

    // 2b. Display the longest stored message (and helper for any message list)
    public String displayLongestStoredMessage() {
        return findLongestMessageText(storedMessages, "No stored messages found.");
    }

    public String displayLongestSentMessage() {
        return findLongestMessageText(sentMessages, "No sent messages found.");
    }

    private String findLongestMessageText(List<Message> list, String emptyMsg) {
        if (list.isEmpty()) {
            return emptyMsg;
        }
        Message longest = list.get(0);
        for (Message m : list) {
            if (m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest.getMessageText();
    }

    // 2c. Search for a message ID and display corresponding recipient and message
    public String searchByMessageID(String targetID) {
        for (Message m : storedMessages) {
            if (m.getMessageID().equals(targetID)) {
                return "Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessageText();
            }
        }
        for (Message m : sentMessages) {
            if (m.getMessageID().equals(targetID)) {
                return "Recipient: " + m.getRecipient() + "\nMessage: " + m.getMessageText();
            }
        }
        return "Message ID not found.";
    }

    // 2d. Search for all messages stored/sent for a particular recipient
    public String searchMessagesByRecipient(String targetRecipient) {
        List<String> matches = new ArrayList<>();
        for (Message m : storedMessages) {
            if (m.getRecipient().equals(targetRecipient)) {
                matches.add(m.getMessageText());
            }
        }
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(targetRecipient)) {
                matches.add(m.getMessageText());
            }
        }
        if (matches.isEmpty()) {
            return "No messages found for recipient: " + targetRecipient;
        }
        return String.join("\n", matches);
    }

    // 2e. Delete a message using the message hash
    public String deleteMessageByHash(String targetHash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            if (m.getMessageHash().equals(targetHash)) {
                String deletedText = m.getMessageText();
                storedMessages.remove(i);
                messageHashes.remove(targetHash);
                saveStoredMessagesToJson();
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        for (int i = 0; i < sentMessages.size(); i++) {
            Message m = sentMessages.get(i);
            if (m.getMessageHash().equals(targetHash)) {
                String deletedText = m.getMessageText();
                sentMessages.remove(i);
                messageHashes.remove(targetHash);
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "No message found with hash: " + targetHash;
    }

    // 2f. Display report listing full details of all stored messages
    public String displayStoredMessagesReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages to report.";
        }
        StringBuilder sb = new StringBuilder("=== STORED MESSAGES REPORT ===\n");
        for (Message m : storedMessages) {
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Message ID: ").append(m.getMessageID()).append("\n")
              .append("Sender: ").append(m.getSender()).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getMessageText()).append("\n")
              .append("------------------------------\n");
        }
        return sb.toString().trim();
    }

    // Unit test report for sent messages (Message Hash, Recipient, Message)
    public String displaySentMessagesReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to report.";
        }
        StringBuilder sb = new StringBuilder("=== SENT MESSAGES REPORT ===\n");
        for (Message m : sentMessages) {
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getMessageText()).append("\n")
              .append("----------------------------\n");
        }
        return sb.toString().trim();
    }
}

// ==========================================
// 4. MAIN APPLICATION
// ==========================================
public class Main {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Login user = new Login();
            MessageStore store = new MessageStore();
            store.loadStoredMessagesFromJson();

            // — PART 1: User Registration —
            System.out.println("==================================");
            System.out.println("       USER REGISTRATION");
            System.out.println("==================================");
            System.out.print("Enter first name: ");
            user.firstName = sc.nextLine();

            System.out.print("Enter last name: ");
            user.lastName = sc.nextLine();

            System.out.print("Enter username: ");
            user.username = sc.nextLine();

            System.out.print("Enter password: ");
            user.password = sc.nextLine();

            System.out.print("Enter cell phone number (+27...): ");
            user.cellPhoneNumber = sc.nextLine();

            System.out.println("\n" + user.registerUser());

            // — PART 1: User Login —
            System.out.println("\n==================================");
            System.out.println("           USER LOGIN");
            System.out.println("==================================");
            System.out.print("Enter username: ");
            String loginUser = sc.nextLine();

            System.out.print("Enter password: ");
            String loginPass = sc.nextLine();

            boolean isLoggedIn = user.loginUser(loginUser, loginPass);
            System.out.println("\n" + user.returnLoginStatus(isLoggedIn));

            // — PART 2 & PART 3: QuickChat Application —
            if (isLoggedIn) {
                String currentSender = (user.firstName != null && !user.firstName.isBlank())
                    ? user.firstName + " " + user.lastName
                    : user.username;

                System.out.println("\n==================================");
                System.out.println("     Welcome to QuickChat.");
                System.out.println("==================================");

                boolean exit = false;
                int totalSentCount = 0;

                while (!exit) {
                    System.out.println("\nPlease choose an option:");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.println("4) Stored Messages");
                    System.out.println("5) Load Part 3 Test Data into Arrays");
                    System.out.print("Option: ");

                    int menuOption = sc.nextInt();
                    sc.nextLine();

                    switch (menuOption) {
                        case 1 -> {
                            System.out.print("\nHow many messages would you like to enter? ");
                            int numMessages = sc.nextInt();
                            sc.nextLine();

                            for (int i = 0; i < numMessages; i++) {
                                System.out.println("\n— Message " + (i + 1) + " of " + numMessages + " —");

                                System.out.print("Enter Recipient Number: ");
                                String recipient = sc.nextLine();

                                System.out.print("Enter Message: ");
                                String msgText = sc.nextLine();

                                Message msg = new Message(i + 1, currentSender, recipient, msgText, null);

                                String lengthStatus = msg.checkMessageLength();
                                if (!lengthStatus.equals("Message ready to send.")) {
                                    System.out.println(lengthStatus);
                                    i--; // Re-prompt for this slot if invalid length
                                    continue;
                                }

                                System.out.println("\nChoose an action:");
                                System.out.println("1) Send Message");
                                System.out.println("2) Disregard Message");
                                System.out.println("3) Store Message to send later");
                                System.out.print("Selection: ");
                                int sendOption = sc.nextInt();
                                sc.nextLine();

                                String resultMsg = msg.SentMessage(sendOption);
                                System.out.println("\n" + resultMsg);

                                if (sendOption == 1) {
                                    totalSentCount++;
                                    store.recordSentMessage(msg);
                                    System.out.println("\n— Message Details —");
                                    System.out.println(msg.printMessages());
                                } else if (sendOption == 2) {
                                    store.recordDisregardedMessage(msg);
                                } else if (sendOption == 3) {
                                    store.recordStoredMessage(msg);
                                }
                            }

                            System.out.println("\nTotal messages sent in this session: " + totalSentCount);
                        }

                        case 2 -> System.out.println(store.displaySentMessagesReport());

                        case 3 -> {
                            System.out.println("Exiting QuickChat. Goodbye!");
                            exit = true;
                        }

                        case 4 -> handleStoredMessagesMenu(sc, store);

                        case 5 -> {
                            store.loadPart3TestData(currentSender);
                            System.out.println("Part 3 test data (Messages 1–5) loaded into arrays and saved to stored_messages.json.");
                        }

                        default -> System.out.println("Invalid option. Please try again.");
                    }
                }
            } else {
                System.out.println("Access denied. You must be logged in to access QuickChat.");
            }
        }
    }

    private static void handleStoredMessagesMenu(Scanner sc, MessageStore store) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID (recipient & message)");
            System.out.println("d) Search for all messages stored/sent for a particular recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display a report listing full details of all stored messages");
            System.out.println("g) Back to Main Menu");
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim().toLowerCase();
            switch (choice) {
                case "a" -> System.out.println("\n" + store.displayStoredSendersAndRecipients());
                case "b" -> System.out.println("\nLongest Stored Message:\n" + store.displayLongestStoredMessage());
                case "c" -> {
                    System.out.print("Enter Message ID: ");
                    String id = sc.nextLine().trim();
                    System.out.println("\n" + store.searchByMessageID(id));
                }
                case "d" -> {
                    System.out.print("Enter Recipient Number: ");
                    String recipient = sc.nextLine().trim();
                    System.out.println("\n" + store.searchMessagesByRecipient(recipient));
                }
                case "e" -> {
                    System.out.print("Enter Message Hash: ");
                    String hash = sc.nextLine().trim();
                    System.out.println("\n" + store.deleteMessageByHash(hash));
                }
                case "f" -> System.out.println("\n" + store.displayStoredMessagesReport());
                case "g" -> back = true;
                default -> System.out.println("Invalid choice. Please select a through g.");
            }
        }
    }
}