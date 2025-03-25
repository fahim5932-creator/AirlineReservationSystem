import java.util.*;

public class Customer {
    private final String userID;
    private String email;
    private String name;
    private String phone;
    private final String password;
    private String address;
    private int age;
    private final List<Flight> flightsRegisteredByUser;
    private final List<Integer> numOfTicketsBookedByUser;
    public static final List<Customer> customerCollection = User.getCustomersCollection();

    public Customer() {
        this(null, null, null, null, null, 0);
    }

    public Customer(String name, String email, String password, String phone, String address, int age) {
        RandomGenerator random = new RandomGenerator();
        random.randomIDGen();
        this.userID = random.getRandomNumber();
        this.name = validateName(name);
        this.email = validateEmail(email);
        this.password = validatePassword(password);
        this.phone = validatePhone(phone);
        this.address = address;
        this.age = validateAge(age);
        this.flightsRegisteredByUser = new ArrayList<>();
        this.numOfTicketsBookedByUser = new ArrayList<>();
    }

    // Input validation methods
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return name;
    }

    private String validateEmail(String email) {
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email;
    }

    private String validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        return password;
    }

    private String validatePhone(String phone) {
        if (phone == null || !phone.matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        return phone;
    }

    private int validateAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Age must be between 0-120");
        }
        return age;
    }

    // Core methods
    public void registerNewCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Customer Registration ===");

        this.name = promptForInput(scanner, "Enter your name: ", this::validateName);
        this.email = promptForUniqueInput(scanner, "Enter your email: ", this::validateEmail);
        this.password = promptForInput(scanner, "Enter your password: ", this::validatePassword);
        this.phone = promptForInput(scanner, "Enter your phone: ", this::validatePhone);
        this.address = promptForInput(scanner, "Enter your address: ", s -> s);
        this.age = Integer.parseInt(promptForInput(scanner, "Enter your age: ", s -> validateAge(Integer.parseInt(s)).toString());

        customerCollection.add(this);
        System.out.println("Registration successful! Your ID: " + this.userID);
    }

    private String promptForInput(Scanner scanner, String prompt, java.util.function.Function<String, String> validator) {
        while (true) {
            System.out.print(prompt);
            try {
                return validator.apply(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private String promptForUniqueInput(Scanner scanner, String prompt, java.util.function.Function<String, String> validator) {
        while (true) {
            String input = promptForInput(scanner, prompt, validator);
            if (isEmailUnique(input)) {
                return input;
            }
            System.out.println("Error: Email already registered");
        }
    }

    private boolean isEmailUnique(String email) {
        return customerCollection.stream().noneMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }

    // Getters (no setters for final fields)
    public String getUserID() { return userID; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public int getAge() { return age; }
    public List<Flight> getFlightsRegisteredByUser() { return flightsRegisteredByUser; }
    public List<Integer> getNumOfTicketsBookedByUser() { return numOfTicketsBookedByUser; }

    // Additional business methods...
    public void addFlightBooking(Flight flight, int tickets) {
        int existingIndex = findFlightIndex(flight);
        if (existingIndex >= 0) {
            numOfTicketsBookedByUser.set(existingIndex,
                    numOfTicketsBookedByUser.get(existingIndex) + tickets);
        } else {
            flightsRegisteredByUser.add(flight);
            numOfTicketsBookedByUser.add(tickets);
        }
    }

    private int findFlightIndex(Flight flight) {
        for (int i = 0; i < flightsRegisteredByUser.size(); i++) {
            if (flightsRegisteredByUser.get(i).getFlightNumber().equals(flight.getFlightNumber())) {
                return i;
            }
        }
        return -1;
    }
}