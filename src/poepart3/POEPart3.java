/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poepart3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Student
 */
public class POEPart3 {

    /**
     * @param args the command line arguments
     */
    
        // TODO code application logic here
    // Message storage
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> messageIdsList = new ArrayList<>();
    private final HashSet<String> messageIds = new HashSet<>();
    private final ArrayList<String> msgHash = new ArrayList<>();
    private final ArrayList<String> recipientNumber = new ArrayList<>();
    private final ArrayList<String> messageStatus = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<String> storedMessages = new ArrayList<>();
    private final ArrayList<String> senders = new ArrayList<>();
    private final ArrayList<String> messages = new ArrayList<>();

    private final Random random = new Random();
    private final Scanner input = new Scanner(System.in);

    private int numMessageSent = 0;

    public static void main(String[] args) {

        POEPart3 poe3 = new POEPart3();

        poe3.populateTestData();

        System.out.println("=====================================");
        System.out.println("       QUICKCHAT MESSAGE SYSTEM");
        System.out.println("=====================================");

        poe3.printMessages();

        poe3.runMenu();
    }

    // Main menu
    public void runMenu() {

        int choice;

        do {
            System.out.println("\n=====================================");
            System.out.println("             MAIN MENU");
            System.out.println("=====================================");
            System.out.println("1. Send a message");
            System.out.println("2. Display all messages");
            System.out.println("3. Display longest message");
            System.out.println("4. Search message by ID");
            System.out.println("5. Search messages by recipient");
            System.out.println("6. Delete message by hash");
            System.out.println("7. Display message report");
            System.out.println("8. Display sender and recipient");
            System.out.println("9. Read stored JSON messages");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = readInteger();

            switch (choice) {

                case 1:
                    sendingMessages();
                    break;

                case 2:
                    printMessages();
                    break;

                case 3:
                    String longest = displayLongestMessage();

                    if (longest.isEmpty()) {
                        System.out.println("No messages available.");
                    } else {
                        System.out.println("Longest message: " + longest);
                    }
                    break;

                case 4:
                    System.out.print("Enter message ID: ");
                    String searchID = input.nextLine();

                    System.out.println(searchMessageByID(searchID));
                    break;

                case 5:
                    System.out.print("Enter recipient number: ");
                    String recipient = input.nextLine();

                    String results = searchMessagesByRecipient(recipient);

                    if (results.isEmpty()) {
                        System.out.println("No messages found.");
                    } else {
                        System.out.println(results);
                    }
                    break;

                case 6:
                    System.out.print("Enter message hash: ");
                    String hash = input.nextLine();

                    System.out.println(deleteMessageByHash(hash));
                    break;

                case 7:
                    System.out.println(displayReport());
                    break;

                case 8:
                    System.out.println(displaySenderAndRecipient());
                    break;

                case 9:
                    System.out.println(
                            readJSONFile("storedMessages.json")
                    );
                    break;

                case 0:
                    System.out.println("Thank you for using QuickChat!");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }

        } while (choice != 0);
    }

    // Read integer safely
    private int readInteger() {

        while (!input.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            input.nextLine();
        }

        int number = input.nextInt();
        input.nextLine();

        return number;
    }

    // Send messages
    public void sendingMessages() {

        System.out.print("How many messages do you want to send? ");

        int numberOfMessages = readInteger();

        if (numberOfMessages <= 0) {
            System.out.println("Please enter a number greater than zero.");
            return;
        }

        for (int i = 1; i <= numberOfMessages; i++) {

            System.out.println("\n=====================================");
            System.out.println("MESSAGE " + i);
            System.out.println("=====================================");

            System.out.print("Enter recipient cell number: ");
            String recipient = input.nextLine();

            String recipientResult = checkRecipientCell(recipient);

            if (!recipientResult.equals("Valid number")) {
                System.out.println(recipientResult);
                i--;
                continue;
            }

            System.out.print("Enter your message: ");
            String messageText = input.nextLine();

            if (messageText.trim().isEmpty()
                    || messageText.length() > 250) {

                System.out.println(
                        "Please enter a message of 1 to 250 characters."
                );

                i--;
                continue;
            }

            System.out.print("Enter sender name: ");
            String sender = input.nextLine();

            String messageID = generateMessageID();

            int messageNumber = messages.size() + 1;

            String messageHash = createMessageHash(
                    messageID,
                    messageNumber,
                    messageText
            );

            // Add message details
            sentMessages.add(messageText);
            messages.add(messageText);
            messageIdsList.add(messageID);
            messageIds.add(messageID);
            recipientNumber.add(recipient);
            senders.add(sender);
            msgHash.add(messageHash);

            String status = SentMessage();

            messageStatus.add(status);

            if (status.equals("Sent")) {
                numMessageSent++;

            } else if (status.equals("Stored")) {
                storedMessages.add(messageText);

            } else if (status.equals("Disregarded")) {
                disregardedMessages.add(messageText);
            }
        }

        printMessages();

        System.out.println(
                "Total messages sent: " + returnTotalMessages()
        );

        storeMessagesToJSON("storedMessages.json");
    }

    // Return total sent messages
    public int returnTotalMessages() {
        return numMessageSent;
    }

    // Create message hash
    public String createMessageHash(
            String messageID,
            int messageNumber,
            String messageText) {

        String firstTwoNumbers = messageID.substring(0, 2);

        String cleanMessage = messageText.trim();

        String[] words = cleanMessage.split("\\s+");

        String firstWord = words[0];

        String lastWord = words[words.length - 1];

        String hash = firstTwoNumbers
                + ":" + messageNumber
                + ":" + firstWord
                + lastWord;

        return hash.toUpperCase();
    }

    // Generate unique 10-digit message ID
    public String generateMessageID() {

        String messageID;

        do {
            StringBuilder id = new StringBuilder();

            id.append(random.nextInt(9) + 1);

            for (int i = 0; i < 9; i++) {
                id.append(random.nextInt(10));
            }

            messageID = id.toString();

        } while (messageIds.contains(messageID));

        return messageID;
    }

    // Validate South African cellphone number
    public String checkRecipientCell(String cellphone) {

        if (cellphone.matches("^\\+27[0-9]{9}$")
                || cellphone.matches("^0[0-9]{9}$")) {

            return "Valid number";

        } else {
            return "Invalid number. Please enter a valid South African number.";
        }
    }

    // Choose what to do with a message
    public String SentMessage() {

        System.out.println("\nChoose what you want to do with this message!");
        System.out.println("1. Send");
        System.out.println("2. Disregard");
        System.out.println("3. Store");
        System.out.print("Enter your choice: ");

        int choice = readInteger();

        switch (choice) {

            case 1:
                System.out.println("Message successfully sent.");
                return "Sent";

            case 2:
                System.out.println("Message disregarded.");
                return "Disregarded";

            case 3:
                System.out.println("Message successfully stored.");
                return "Stored";

            default:
                System.out.println("Invalid option. Message disregarded.");
                return "Disregarded";
        }
    }

    // Display all messages
    public String printMessages() {

        StringBuilder allMessages = new StringBuilder();

        System.out.println("\n========== ALL MESSAGES ==========");

        if (messages.isEmpty()) {
            System.out.println("No messages available.");
            return "";
        }

        for (int i = 0; i < messages.size(); i++) {

            int messageNumber = i + 1;

            allMessages.append("\nMessage Number: ")
                    .append(messageNumber);

            allMessages.append("\nMessage ID: ")
                    .append(messageIdsList.get(i));

            allMessages.append("\nRecipient: ")
                    .append(recipientNumber.get(i));

            allMessages.append("\nMessage: ")
                    .append(messages.get(i));

            allMessages.append("\nMessage Hash: ")
                    .append(msgHash.get(i));

            allMessages.append("\nStatus: ")
                    .append(messageStatus.get(i));

            System.out.println("\n=====================================");
            System.out.println("Message Number: " + messageNumber);
            System.out.println("Message ID: " + messageIdsList.get(i));
            System.out.println("Recipient: " + recipientNumber.get(i));
            System.out.println("Message: " + messages.get(i));
            System.out.println("Message Hash: " + msgHash.get(i));
            System.out.println("Status: " + messageStatus.get(i));
        }

        return allMessages.toString();
    }

    // Populate test data
    public void populateTestData() {

        sentMessages.clear();
        messageIdsList.clear();
        messageIds.clear();
        msgHash.clear();
        recipientNumber.clear();
        messageStatus.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        senders.clear();
        messages.clear();

        numMessageSent = 0;

        addTestMessage(
                "+27834557896",
                "Did you get the cake?",
                "Sent",
                "08388884567",
                "1234567890"
        );

        addTestMessage(
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "Stored",
                "08388884567",
                "1234567891"
        );

        addTestMessage(
                "+27834484567",
                "Yohoooo, I am at your gate.",
                "Disregarded",
                "08388884567",
                "1234567892"
        );

        addTestMessage(
                "08388884567",
                "It is dinner time!",
                "Sent",
                "08388884567",
                "08388884567"
        );

        addTestMessage(
                "+27838884567",
                "Ok, I am leaving without you.",
                "Stored",
                "08388884567",
                "1234567894"
        );
    }

    // Add test message
    private void addTestMessage(
            String recipient,
            String text,
            String flag,
            String sender,
            String id) {

        messageIdsList.add(id);
        messageIds.add(id);

        recipientNumber.add(recipient);
        senders.add(sender);
        messageStatus.add(flag);

        messages.add(text);
        sentMessages.add(text);

        msgHash.add(
                createMessageHash(id, messageIdsList.size(), text)
        );

        if (flag.equals("Sent")) {
            numMessageSent++;

        } else if (flag.equals("Stored")) {
            storedMessages.add(text);

        } else if (flag.equals("Disregarded")) {
            disregardedMessages.add(text);
        }
    }

    // Display longest message
    public String displayLongestMessage() {

        String longest = "";

        for (String m : messages) {

            if (m.length() > longest.length()) {
                longest = m;
            }
        }

        return longest;
    }

    // Search message by ID
    public String searchMessageByID(String searchID) {

        for (int i = 0; i < messageIdsList.size(); i++) {

            if (messageIdsList.get(i).equals(searchID)) {
                return messages.get(i);
            }
        }

        return "Message not found.";
    }

    // Search messages by recipient
    public String searchMessagesByRecipient(String recipient) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < recipientNumber.size(); i++) {

            if (recipientNumber.get(i).equals(recipient)) {

                if (sb.length() > 0) {
                    sb.append("\n");
                }

                sb.append(messages.get(i));
            }
        }

        return sb.toString();
    }

    // Delete message by hash
    public String deleteMessageByHash(String hash) {

        for (int i = 0; i < msgHash.size(); i++) {

            if (msgHash.get(i).equals(hash)) {

                String deleted = messages.get(i);
                String flag = messageStatus.get(i);

                removeMessageAt(i);

                if (flag.equals("Sent")) {
                    numMessageSent--;
                }

                return "Message: \"" + deleted
                        + "\" successfully deleted.";
            }
        }

        return "Message hash not found.";
    }

    // Remove message and keep all lists synchronised
    private void removeMessageAt(int index) {

        String deletedMessage = messages.get(index);
        String id = messageIdsList.get(index);
        String status = messageStatus.get(index);

        messages.remove(index);
        sentMessages.remove(index);
        msgHash.remove(index);

        messageIds.remove(id);
        messageIdsList.remove(index);

        recipientNumber.remove(index);
        senders.remove(index);
        messageStatus.remove(index);

        if (status.equals("Stored")) {
            storedMessages.remove(deletedMessage);
        }

        if (status.equals("Disregarded")) {
            disregardedMessages.remove(deletedMessage);
        }
    }

    // Display message report
    public String displayReport() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < messages.size(); i++) {

            sb.append("Message Hash: ")
                    .append(msgHash.get(i))
                    .append("\n");

            sb.append("Recipient: ")
                    .append(recipientNumber.get(i))
                    .append("\n");

            sb.append("Message: ")
                    .append(messages.get(i))
                    .append("\n");

            sb.append("Status: ")
                    .append(messageStatus.get(i))
                    .append("\n\n");
        }

        return sb.toString();
    }

    // Display sender and recipient
    public String displaySenderAndRecipient() {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < messages.size(); i++) {

            sb.append("Sender: ")
                    .append(senders.get(i));

            sb.append(" | Recipient: ")
                    .append(recipientNumber.get(i));

            sb.append("\n");
        }

        return sb.toString();
    }

    // Read JSON file
    public String readJSONFile(String filePath) {

        try {

            String content = new String(
                    Files.readAllBytes(Paths.get(filePath)),
                    StandardCharsets.UTF_8
            );

            Pattern objectPattern = Pattern.compile("\\{([^}]*)\\}");

            Matcher objectMatcher = objectPattern.matcher(content);

            int count = 0;

            while (objectMatcher.find()) {

                String object = objectMatcher.group(1);

                String recipient = extractJSONValue(
                        object, "recipient"
                );

                String msg = extractJSONValue(
                        object, "message"
                );

                if (recipient != null && msg != null) {

                    if (!storedMessages.contains(msg)) {
                        storedMessages.add(msg);
                    }

                    count++;
                }
            }

            return "JSON file read successfully. "
                    + count + " stored messages loaded.";

        } catch (IOException e) {

            return "Error reading JSON file: " + e.getMessage();
        }
    }

    // Extract JSON string values safely
    private String extractJSONValue(String object, String key) {

        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\""
        );

        Matcher matcher = pattern.matcher(object);

        if (matcher.find()) {
            return unescapeJSON(matcher.group(1));
        }

        return null;
    }

    // Decode JSON escaped characters
    private String unescapeJSON(String value) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {

            char c = value.charAt(i);

            if (c == '\\' && i + 1 < value.length()) {

                char next = value.charAt(++i);

                switch (next) {

                    case '"':
                        result.append('"');
                        break;

                    case '\\':
                        result.append('\\');
                        break;

                    case '/':
                        result.append('/');
                        break;

                    case 'n':
                        result.append('\n');
                        break;

                    case 'r':
                        result.append('\r');
                        break;

                    case 't':
                        result.append('\t');
                        break;

                    case 'b':
                        result.append('\b');
                        break;

                    case 'f':
                        result.append('\f');
                        break;

                    default:
                        result.append(next);
                        break;
                }

            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    // Escape JSON special characters
    private String escapeJSON(String value) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {

            char c = value.charAt(i);

            switch (c) {

                case '"':
                    result.append("\\\"");
                    break;

                case '\\':
                    result.append("\\\\");
                    break;

                case '\n':
                    result.append("\\n");
                    break;

                case '\r':
                    result.append("\\r");
                    break;

                case '\t':
                    result.append("\\t");
                    break;

                default:
                    if (c < 0x20) {
                        result.append(String.format("\\u%04x", (int) c));
                    } else {
                        result.append(c);
                    }
                    break;
            }
        }
        return result.toString();
    }

    // Store messages in JSON file
    public void storeMessagesToJSON(String filePath) {

        try {

            StringBuilder json = new StringBuilder();

            json.append("[\n");

            boolean first = true;

            for (int i = 0; i < messages.size(); i++) {

                if (messageStatus.get(i).equals("Stored")) {

                    if (!first) {
                        json.append(",\n");
                    }

                    json.append("  {")
                            .append("\"recipient\": \"")
                            .append(escapeJSON(recipientNumber.get(i)))
                            .append("\", ")
                            .append("\"message\": \"")
                            .append(escapeJSON(messages.get(i)))
                            .append("\", ")
                            .append("\"flag\": \"Stored\"")
                            .append("}");

                    first = false;
                }
            }

            json.append("\n]\n");

            Files.write(
                    Paths.get(filePath),
                    json.toString().getBytes(StandardCharsets.UTF_8)
            );

            System.out.println("Messages successfully saved to JSON.");

        } catch (IOException e) {
            
            System.out.println(
                    "Error saving JSON file: " + e.getMessage()
            );
        }
    }
}
        
    
    

