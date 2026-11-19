
import java.io.File;
import java.util.Scanner;
import poepart3.POEPart3;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
public class messageApp {
    
    
Scanner input = new Scanner(System.in);
    POEPart3 message = new POEPart3();

    void run() {
        message.populateTestData();
        boolean running = true;
        while (running) {
            System.out.println("======================================");
            System.out.println("Welcome to QuickChat");
            System.out.println("======================================");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.println("Choose an option: ");
            String choice = input.nextLine();

            if (choice.equals("1")) {
                message.sendingMessages();
            } else if (choice.equals("2")) {
                System.out.println("Coming Soon.");
            } else if (choice.equals("3")) {
                System.out.println("Total messages sent: " + message.returnTotalMessages());
                running = false;
            } else if (choice.equals("4")) {
                showStoredMessagesMenu();
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    void showStoredMessagesMenu() {
        File f = new File("storedMessages.json");
        if (f.exists()) {
            System.out.println(message.readJSONFile("storedMessages.json"));
        }
        boolean back = false;
        while (!back) {
            System.out.println("Stored Messages");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search for all messages for a particular recipient");
            System.out.println("e) Delete a message using message hash");
            System.out.println("f) Display report");
            System.out.println("g) Back");
            System.out.println("Choose an option: ");
            String c = input.nextLine();
            if (c.equalsIgnoreCase("a")) {
                System.out.println(message.displaySenderAndRecipient());
            } else if (c.equalsIgnoreCase("b")) {
                System.out.println("Longest Message:");
                System.out.println(message.displayLongestMessage());
            } else if (c.equalsIgnoreCase("c")) {
                System.out.println("Enter Message ID to search (e.g. 08388884567):");
                String id = input.nextLine();
                System.out.println(message.searchMessageByID(id));
            } else if (c.equalsIgnoreCase("d")) {
                System.out.println("Enter recipient (e.g. +27838884567):");
                String rec = input.nextLine();
                System.out.println(message.searchMessagesByRecipient(rec));
            } else if (c.equalsIgnoreCase("e")) {
                System.out.println("Enter Message Hash to delete:");
                String hash = input.nextLine();
                System.out.println(message.deleteMessageByHash(hash));
            } else if (c.equalsIgnoreCase("f")) {
                System.out.println(message.displayReport());
            } else if (c.equalsIgnoreCase("g")) {
                back = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
