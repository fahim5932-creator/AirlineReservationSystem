import java.util.*;
import java.util.function.Consumer;

public class User {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RolesAndPermissions auth = new RolesAndPermissions();
    private static final FlightReservation reservation = new FlightReservation();

    public static void main(String[] args) {
        Flight.scheduleRandomFlights(15);

        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter choice: ", 0, 5);

            switch (choice) {
                case 0 -> System.exit(0);
                case 1 -> handleAdminLogin();
                case 2 -> handleAdminRegistration();
                case 3 -> handleCustomerLogin();
                case 4 -> new Customer().register();
                case 5 -> displayManual();
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void handleAdminLogin() {
        String username = getInput("Username: ");
        String password = getInput("Password: ");

        auth.authenticateAdmin(username, password).ifPresentOrElse(
                user -> adminMenu(user),
                () -> System.out.println("Invalid credentials")
        );
    }

    private static void adminMenu(String username) {
        System.out.printf("Welcome admin %s%n", username);

        Map<Integer, Consumer<String>> menu = Map.of(
                1, opt -> new Customer().register(),
                2, opt -> searchCustomer(),
                3, opt -> updateCustomer(),
                4, opt -> deleteCustomer(),
                5, opt -> Customer.getAllCustomers().forEach(System.out::println),
                6, opt -> reservation.displayRegisteredUsersForAllFlight(),
                7, opt -> deleteFlight(),
                0, opt -> {}
        );

        do {
            displayAdminMenu(username);
            int choice = getIntInput("Enter choice: ", 0, 7);
            menu.getOrDefault(choice, opt -> System.out.println("Invalid choice"))
                    .accept("");
        } while (true);
    }

    private static void searchCustomer() {
        String id = getInput("Enter customer ID: ");
        Customer.getAllCustomers().stream()
                .filter(c -> c.getUserID().equals(id))
                .findFirst()
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Customer not found")
                );
    }

    private static void updateCustomer() {
        // Implementation for updating customer
    }

    private static void deleteCustomer() {
        // Implementation for deleting customer
    }

    private static void deleteFlight() {
        // Implementation for deleting flight
    }

    private static void handleAdminRegistration() {
        String username = getInput("New username: ");
        String password = getInput("New password: ");

        if (auth.registerAdmin(username, password)) {
            System.out.println("Admin registered successfully");
        } else {
            System.out.println("Admin registration failed");
        }
    }

    private static void handleCustomerLogin() {
        String email = getInput("Email: ");
        String password = getInput("Password: ");

        auth.authenticateCustomer(email, password).ifPresentOrElse(
                userId -> customerMenu(userId),
                () -> System.out.println("Invalid credentials")
        );
    }

    private static void customerMenu(String userId) {
        Customer customer = Customer.getAllCustomers().stream()
                .filter(c -> c.getUserID().equals(userId))
                .findFirst()
                .orElseThrow();

        System.out.printf("Welcome %s%n", customer.getName());

        Map<Integer, Consumer<String>> menu = Map.of(
                1, opt -> bookFlight(userId),
                2, opt -> updateCustomerProfile(userId),
                3, opt -> deleteAccount(userId),
                4, opt -> Flight.displayAllFlights(),
                5, opt -> reservation.cancelFlight(userId),
                6, opt -> reservation.displayFlightsRegisteredByOneUser(userId),
                0, opt -> {}
        );

        do {
            displayCustomerMenu(customer.getName());
            int choice = getIntInput("Enter choice: ", 0, 6);
            menu.getOrDefault(choice, opt -> System.out.println("Invalid choice"))
                    .accept("");
        } while (true);
    }

    private static void bookFlight(String userId) {
        Flight.displayAllFlights();
        String flightNo = getInput("Enter flight number: ");
        int tickets = getIntInput("Enter tickets (1-10): ", 1, 10);
        reservation.bookFlight(flightNo, tickets, userId);
    }

    private static void updateCustomerProfile(String userId) {
        // Implementation for updating profile
    }

    private static void deleteAccount(String userId) {
        // Implementation for deleting account
    }

    // Helper methods
    private static String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.printf("Please enter a number between %d-%d%n", min, max);
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Admin Login");
        System.out.println("2. Admin Registration");
        System.out.println("3. Customer Login");
        System.out.println("4. Customer Registration");
        System.out.println("5. User Manual");
        System.out.println("0. Exit");
    }

    private static void displayAdminMenu(String username) {
        System.out.printf("\n=== Admin Menu (%s) ===%n", username);
        System.out.println("1. Add Customer");
        System.out.println("2. Search Customer");
        System.out.println("3. Update Customer");
        System.out.println("4. Delete Customer");
        System.out.println("5. List All Customers");
        System.out.println("6. View Flight Passengers");
        System.out.println("7. Delete Flight");
        System.out.println("0. Logout");
    }

    private static void displayCustomerMenu(String name) {
        System.out.printf("\n=== Customer Menu (%s) ===%n", name);
        System.out.println("1. Book Flight");
        System.out.println("2. Update Profile");
        System.out.println("3. Delete Account");
        System.out.println("4. View Flight Schedule");
        System.out.println("5. Cancel Booking");
        System.out.println("6. View My Bookings");
        System.out.println("0. Logout");
    }

    private static void displayManual() {
        System.out.println("\n=== User Manual ===");
        System.out.println("1. Admins can manage customers and flights");
        System.out.println("2. Customers can book and manage flights");
        System.out.println("3. All users must register before logging in");
        //FIxed  Manual
    }
}