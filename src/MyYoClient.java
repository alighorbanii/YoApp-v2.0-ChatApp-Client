/**
 * This Java project brings the whole class together to revive the legendary “Yo” app, a minimalist chat client that only lets users send “Yo” and “Howdy” messages.
 * Everyone builds their own version of the client by extending a provided abstract class, connecting to a shared server where messages are broadcasted to all connected classmates in real-time.
 * But there is a twist! The user is rewarded for sending Howdys, tacked by the "cool factor", and is roasted by the app if you spam too many Yos.
 * The project utilizes a GUI, network, inheritance, and real-time communication — all while literally yelling “Yo!” at your classmates across campus or over UT Dallas' campus VPN.
 *
 * AUTHOR: Ali Ghorbani
 */

import java.io.IOException;
import java.util.Random;

// MyYoClient extends the abstract YoClient class
// It handles user interactions, tracks statistics, and updates the GUI accordingly
public class MyYoClient extends YoClient {
    private int yoCount = 0;         // Tracks how many Yo messages are received from other user
    private int howdyCount = 0;      // Tracks how many Howdy messages are received from other users
    private int yoStreak = 0;        // Tracks how many Yo messages this client has sent in a row and resets after sending Howdy
    private int msgCount = 0;        // Tracks the total number of messages (Yo or Howdy) is received from other users

    // Used to randomly choose a reminder message after every 5 consecutive Yos sent
    // Predefined reminder messages that encourage the user to send a Howdy
    private Random rand = new Random();
    private final String[] msgReminder = {
            "Yo, the 'Send Howdy' button is a thing.",
            "The 'Send Yo' button is getting a little tired, don't you think?",
            "Try sending a 'Howdy' to everyone."
    };

    // This method is automatically called whenever a message (Yo or Howdy) is sent or received
    // It determines what type of message was processed and updates counters or GUI
    @Override
    public void updateStats(char msgTag) {
        // Message received from another user
        // count++ for howdy, yo, msg, and the yo streak to increase the counters for them
        if (msgTag == RECV_YO) {
            yoCount++;
            msgCount++;
            // Message received from another user
        } else if (msgTag == RECV_HOWDY) {
            howdyCount++;
            msgCount++;
            // Message sent by this client (yo)
        } else if (msgTag == SEND_YO) {
            yoStreak++;
            // If 5 Yos were sent in a row, print a random reminder encouraging a Howdy
            if (yoStreak % 5 == 0) {
                int msgIdx = rand.nextInt(msgReminder.length); // Choose random reminder index
                updateGUIConsole(msgReminder[msgIdx]);         // Print to GUI console
            }
            // Message sent by this client (Howdy)
        } else if (msgTag == SEND_HOWDY) {
            yoStreak = 0; // Reset Yo streak counter
        }
        // Every 10 messages received from others (Yo or Howdy), display the cool factor
        if (msgCount > 0 && msgCount % 10 == 0) {
            double coolFactor = (double) howdyCount / (howdyCount + yoCount); // Calculate cool factor
            updateGUIConsole(String.format("Cool factor: %.2f", coolFactor)); // Display result
        }
    }

    // Called automatically when user clicks the "Disconnect" button
    // Outputs final message stats to the GUI
    @Override
    public void finalStats() {
        updateGUIConsole("Final Stats");
        updateGUIConsole("Yo received: " + yoCount);
        updateGUIConsole("Howdy received: " + howdyCount);
    }

    // Main function. Sets up and launches the GUI app
    public static void main(String[] args) {
        MyYoClient client = new MyYoClient();
        try {
            client.initUI();             // Initialize and display the GUI
            if (client.connect()) {      // Attempt to connect to the Yo server
                client.listen();         // Start listening for incoming messages
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());   // Handle any network or connection issues
        }
    }

}
