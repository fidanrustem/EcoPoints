
import java.io.*;
import java.util.*;
import java.time.LocalDate;
public class EcoPointsRecyclingTracker {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Household> households = new HashMap<>(); // Task 2
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Eco-Points Recycling Tracker ===");
            System.out.println("1. Register Household");
            System.out.println("2. Log Recycling Event");
            System.out.println("3. Display Households");
            System.out.println("4. Display Household Recycling Events");
            System.out.println("5. Generate Reports");
            System.out.println("6. Save and Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    registerHousehold();
                    break;
                case "2":
                    logRecyclingEvent();
                    break;
                case "3":
                    displayHouseholds();
                    break;
                case "4":
                    displayHouseholdRecyclingEvents();
                    break;
                case "5":
                    generateReports();
                    break;
                case "6":
                    saveHouseholdsToFile();
                    running = false;
                    System.out.println("Data saved. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-6.");
            }

            // You will handle the choice entered here using switch(case)
        }
    }

    private static void registerHousehold(){
        System.out.print("Enter household ID: ");
        String id = scanner.nextLine().trim();
        if (households.containsKey(id)) {
            System.out.println("Error: Household ID already exists.");
            return;
        }
        System.out.print("Enter household name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter household address: ");
        String address = scanner.nextLine().trim();
        Household household = new Household(id, name, address);
        households.put(id, household);
        System.out.println("Household registered successfully on " + household.getJoinDate());
    }
    private static void logRecyclingEvent(){
        System.out.print("Enter event ID: ");
        String id = scanner.nextLine().trim();
        Household household = households.get(id);
        if (household == null) {
            System.out.println("Error: Household with ID " + id + " does not exist.");
            return;
        }
        System.out.print("Enter material type (plastic/glass/metal/paper): ");
        String material = scanner.nextLine().trim();
        double weight = 0.0;
        while (true) {
            try {
                System.out.print("Enter weight in kilograms: ");
                weight = Double.parseDouble(scanner.nextLine());  // Convert input to double

                // Check that weight is a positive number
                if (weight <= 0) throw new IllegalArgumentException();

                break;  // Exit loop if input is valid
            }  catch (NumberFormatException e) {
                System.out.println("Invalid weight. Must be a positive number.");
            }  catch (IllegalArgumentException e) {
                System.out.println("Invalid weight. Must be a positive number.");
            }
        }
        RecyclingEvent event = new RecyclingEvent(material, weight);
        household.addEvent(event);
        System.out.println("Recycling event logged! Points earned: " + event.getEcoPoints());

    }
    private static void displayHouseholds(){
        if (households.isEmpty()) {
            System.out.println("No households found.");
            return;
        }
        System.out.println("Households found:");
        for (Household h : households.values()) {
            System.out.println("ID: " + h.getId() +
                    ", Name: " + h.getName() +
                    ", Address: " + h.getAddress() +
                    ", Joined: " + h.getJoinDate());
        }
    }
    private static void displayHouseholdRecyclingEvents(){
        System.out.print("Enter event ID: ");
        String id = scanner.nextLine().trim();
        Household household = households.get(id);
        if (household == null) {
            System.out.println("Error: Household with ID " + id + " does not exist.");

        }
        System.out.println("\nRecycling Events for " + household.getName() + ":");

        // Check if the household has any recycling events
        if (household.getEvents().isEmpty()) {
            System.out.println("No events logged.");
        }
        else{
            for (RecyclingEvent event : household.getEvents()) {
                System.out.println(event);
            }
        }
        System.out.println("Total Weight: " + household.getTotalWeight() + " kg");
        System.out.println("Total Points: " + household.getTotalPoints() + " pts");
    }
    private static void generateReports(){
        if (households.isEmpty()) {
            System.out.println("No households found.");
            return;
        }
        Household top=null;
        for (Household h : households.values()) {
            if(top==null||h.getTotalPoints()>top.getTotalPoints()){
                top=h;
            }
        }
        System.out.println("\nHousehold with Highest Points:");
        System.out.println("ID: " + top.getId() +
                ", Name: " + top.getName() +
                ", Points: " + top.getTotalPoints());
        double totalWeight = 0.0;
        for (Household h : households.values()) {
            totalWeight += h.getTotalWeight();
        }
        System.out.println("Total Weight: " + totalWeight + " kg");
    }
    // Task 5
    private static void saveHouseholdsToFile() {
        try {
            // Create a FileOutputStream to write to the file named "households.ser"
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("households.ser")
            );
            // Write the entire households map to the file
            out.writeObject(households);

            // If successful, no message is printed here — could add confirmation if you like
        } catch (IOException e) {
            // Task 8
            // If something goes wrong while saving, print an error message
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked") // Suppresses unchecked cast warning when reading the object
    private static void loadHouseholdsFromFile() {
        // Use a try-with-resources block to automatically close the input stream
        try (
                // Open an ObjectInputStream to read from the file "households.ser"
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("households.ser"))
        ) {
            // Read the object from the file and cast it back to the correct type
            households = (Map<String, Household>) in.readObject();

            // Confirmation message to let the user know data was loaded
            System.out.println("Household data loaded.");
        } catch (FileNotFoundException e) {
            // Task 8
            // If the file doesn't exist yet, that's okay — start with empty data
            System.out.println("No saved data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            // Handle other errors, like if the file is corrupted or unreadable
            System.out.println("Error loading data: " + e.getMessage());
        }
    }



}
