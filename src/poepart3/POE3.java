/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepart3;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Student
 */
class POE3 {
    
        ArrayList<String> sentMessages = new ArrayList<>();
    ArrayList<String> messageIdsList = new ArrayList<>();
    HashSet<String> messageIds = new HashSet<>();
    ArrayList<String> msgHash = new ArrayList<>();
    ArrayList<String> recipientNumber = new ArrayList<>();
    ArrayList<String> messageStatus = new ArrayList<>();
    ArrayList<String> disregardedMessages = new ArrayList<>();
    ArrayList<String> storedMessages = new ArrayList<>();
    ArrayList<String> senders = new ArrayList<>();
    ArrayList<String> messages = new ArrayList<>();

    Random random = new Random();
    Scanner input = new Scanner(System.in);

    int numMessageSent = 0;

    // YOUR main - now only calls the menu, methods are outside
    public static void main(String[] args) {
        POE3 poe3 = new POE3();
        poe3.populateTestData();
        System.out.println("Total messages sent: " + poe3.returnTotalMessages());
        System.out.println(poe3.printMessages());
    }

    // YOUR METHOD - sendingMessages()
    public void sendingMessages() {
        System.out.print("How many messages do you want to send? ");
        int numberOfMessages = input.nextInt();

        input.nextLine();

        for (int i = 1; i <= numberOfMessages; i++) {

            System.out.println("\n=====================================");
            System.out.println("MESSAGE " + i);
            System.out.println("=====================================");

            String messageID = generateMessageID();

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

            if (messageText.length() > 250 || messageText.trim().isEmpty()) {
                System.out.println("Please enter a message of 1 to 250 characters.");
                i--;
                continue;
            }

            int messageNumber = sentMessages.size() + 1;

            String messageHash = createMessageHash(messageID, messageNumber, messageText);

            msgHash.add(messageHash);
            sentMessages.add(messageText);
            messages.add(messageText);
            messageIdsList.add(messageID);
            messageIds.add(messageID);
            recipientNumber.add(recipient);

            System.out.print("Enter sender name: ");
            String sender = input.nextLine();
            senders.add(sender);

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

    public int returnTotalMessages() {
        return numMessageSent;
    }

    // YOUR METHOD - createMessageHash()
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        String firstTwoNumbers = messageID.substring(0, 2);

        String cleanMessage = messageText.trim();

        String[] words = cleanMessage.split("\\s+");

        String firstWord = words[0];

        String lastWord = words[words.length - 1];

        String hash = firstTwoNumbers + ":" + messageNumber + ":" + firstWord + lastWord;

        return hash.toUpperCase();
    }

    // YOUR METHOD - generateMessageID()
    public String generateMessageID() {
        String messageID;

        do {
            long number = 1000000000L + random.nextLong(9000000000L);

            messageID = String.valueOf(number);

        } while (messageIds.contains(messageID));

        return messageID;
    }

    // YOUR METHOD - checkRecipientCell()
    public String checkRecipientCell(String cellphone) {
        if (cellphone.matches("^\\+27[0-9]{9}$") || cellphone.matches("^0[0-9]{9}$")) {
            return "Valid number";
        } else {
            return "Invalid number";
        }
    }

    // YOUR METHOD - SentMessage()
    public String SentMessage() {
        System.out.println("\nChoose what you want to do with this message!");

        System.out.println("1. Send");
        System.out.println("2. Disregard");
        System.out.println("3. Store");

        System.out.println("Enter your choice: ");
        int choice = input.nextInt();

        input.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.println("Message successfully sent");
                return "Sent";
            }

            case 2 -> {
                System.out.println("Press 0 to delete the message");
                int deleteChoice = input.nextInt();

                input.nextLine();

                if (deleteChoice == 0) {
                    System.out.println("Message deleted");

                    int lastIndex = sentMessages.size() - 1;

                    String messageID = messageIdsList.get(lastIndex);
                    messageIds.remove(messageID);

                    recipientNumber.remove(lastIndex);
                    sentMessages.remove(lastIndex);
                    messages.remove(lastIndex);
                    msgHash.remove(lastIndex);
                    messageIdsList.remove(lastIndex);
                    senders.remove(lastIndex);
                    messageStatus.remove(lastIndex);

                    return "Disregarded";
                }

                return "Disregarded";
            }

            case 3 -> {
                System.out.println("Message successfully stored");
                return "Stored";
            }

            default -> {
                System.out.println("Invalid option");
                return "Disregarded";
            }
        }
    }

    // YOUR METHOD - printMessages()
    public String printMessages() {
        StringBuilder allMessages = new StringBuilder();

        System.out.println("\n==== All Messages ====");

        for (int i = 0; i < sentMessages.size(); i++) {
            int messageNumber = i + 1;

            allMessages.append("\nMessage Number: ").append(messageNumber);
            allMessages.append("\nMessage ID: ").append(messageIdsList.get(i));
            allMessages.append("\nRecipient: ").append(recipientNumber.get(i));
            allMessages.append("\nMessage: ").append(sentMessages.get(i));
            allMessages.append("\nMessage Hash: ").append(msgHash.get(i));
            allMessages.append("\nStatus: ").append(messageStatus.get(i));

            System.out.println("\n=====================================");
            System.out.println("\nMessage Number: " + messageNumber);
            System.out.println("Message ID: " + messageIdsList.get(i));
            System.out.println("Recipient: " + recipientNumber.get(i));
            System.out.println("Message: " + sentMessages.get(i));
            System.out.println("Message Hash: " + msgHash.get(i));
            System.out.println("Status: " + messageStatus.get(i));
        }

        return allMessages.toString();
    }

    // ===== POE3 ADDED METHODS - Same names, for rubric =====

    public void populateTestData() {
        sentMessages.clear(); messageIdsList.clear(); messageIds.clear();
        msgHash.clear(); recipientNumber.clear(); messageStatus.clear();
        disregardedMessages.clear(); storedMessages.clear(); senders.clear(); messages.clear();
        numMessageSent = 0;

        addTestMessage("+27834557896", "Did you get the cake?", "Sent", "08388884567", "1234567890");
        addTestMessage("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored", "08388884567", "1234567891");
        addTestMessage("+27834484567", "Yohoooo, I am at your gate.", "Disregard", "08388884567", "1234567892");
        addTestMessage("08388884567", "It is dinner time !", "Sent", "08388884567", "08388884567");
        addTestMessage("+27838884567", "Ok, I am leaving without you.", "Stored", "08388884567", "1234567894");
    }

    private void addTestMessage(String recipient, String text, String flag, String sender, String id) {
        messageIdsList.add(id);
        messageIds.add(id);
        recipientNumber.add(recipient);
        senders.add(sender);
        messageStatus.add(flag);
        messages.add(text);
        sentMessages.add(text);
        msgHash.add(createMessageHash(id, messageIdsList.size(), text));
        if (flag.equals("Sent")) {
            numMessageSent++;
        } else if (flag.equals("Stored")) {
            storedMessages.add(text);
        } else if (flag.equals("Disregard")) {
            disregardedMessages.add(text);
        }
    }

    public String displayLongestMessage() {
        String longest = "";
        for (String m : messages) {
            if (m.length() > longest.length()) longest = m;
        }
        return longest;
    }

    public String searchMessageByID(String searchID) {
        for (int i = 0; i < messageIdsList.size(); i++) {
            if (messageIdsList.get(i).equals(searchID)) return messages.get(i);
        }
        return "Message not found.";
    }

    public String searchMessagesByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recipientNumber.size(); i++) {
            if (recipientNumber.get(i).equals(recipient)) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(messages.get(i));
            }
        }
        return sb.toString();
    }

    public String deleteMessageByHash(String hash) {
        for (int i = 0; i < msgHash.size(); i++) {
            if (msgHash.get(i).equals(hash)) {
                String deleted = messages.get(i);
                String flag = messageStatus.get(i);
                messages.remove(i);
                msgHash.remove(i);
                messageIds.remove(messageIdsList.get(i));
                messageIdsList.remove(i);
                recipientNumber.remove(i);
                senders.remove(i);
                messageStatus.remove(i);
                sentMessages.remove(deleted);
                if (flag.equals("Stored")) storedMessages.remove(deleted);
                if (flag.equals("Disregard")) disregardedMessages.remove(deleted);
                return "Message: \"" + deleted + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public String displayReport() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            sb.append("Message Hash: ").append(msgHash.get(i)).append("\n");
            sb.append("Recipient: ").append(recipientNumber.get(i)).append("\n");
            sb.append("Message: ").append(messages.get(i)).append("\n\n");
        }
        return sb.toString();
    }

    public String displaySenderAndRecipient() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            sb.append("Sender: ").append(senders.get(i)).append(" Recipient: ").append(recipientNumber.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String readJSONFile(String filePath) {
        try {
            String content = Files.readString(Path.of(filePath));
            String[] parts = content.split("\\{");
            int count = 0;
            for (String p : parts) {
                if (p.contains("recipient")) {
                    String msg = p.split("\"message\"\\s*:\\s*\"")[1].split("\"")[0];
                    if (!storedMessages.contains(msg)) storedMessages.add(msg);
                    count++;
                }
            }
            return "JSON file read successfully. " + count + " stored messages loaded.";
        } catch (Exception e) {
            return "Error reading JSON file: " + e.getMessage();
        }
    }

    public void storeMessagesToJSON(String filePath) {
        try (FileWriter w = new FileWriter(filePath)) {
            w.write("[\n");
            boolean first = true;
            for (int i = 0; i < messages.size(); i++) {
                if (messageStatus.get(i).equals("Stored")) {
                    if (!first) w.write(",\n");
                    w.write("  {\"recipient\": \"" + recipientNumber.get(i) + "\", \"message\": \"" + messages.get(i) + "\", \"flag\": \"Stored\"}");
                    first = false;
                }
            }
            w.write("\n]\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
    

