import java.util.*;
import java.util.function.Predicate;

public class Customer {
    private static final int MIN_USER_ID = 20000;
    private static final int MAX_AGE = 120;

    private final String userID;
    private String email;
    private String name;
    private String phone;
    private final String password;
    private String address;
    private int age;
    private final List<Flight> flightsRegisteredByUser;
    private final List<Integer> numOfTicketsBookedByUser;
    private static final List<Customer> customerCollection = new ArrayList<>();

    public Customer() {
        this("", "", "", "", "", 0);
    }

    public Customer(String name, String email, String password, String phone, String address, int age) {
        this.userID = generateUserId();
        setName(name);
        setEmail(email);
        this.password = validatePassword(password);
        setPhone(phone);
        setAddress(address);
        setAge(age);
        this.flightsRegisteredByUser = new ArrayList<>();
        this.numOfTicketsBookedByUser = new ArrayList<>();
    }

    private String generateUserId() {
        Random random = new Random();
        return String.valueOf(random.nextInt(1000000 - MIN_USER_ID) + MIN_USER_ID);
    }

    private String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return password;
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Customer Registration ===");

        setName(getInput(scanner, "Enter your name: ",
                input -> !input.trim().isEmpty(), "Name cannot be empty"));

        setEmail(getInput(scanner, "Enter your email: ",
                this::isEmailUnique, "Email already exists"));

        this.password = getInput(scanner, "Enter your password: ",
                input -> input.length() >= 8, "Password must be at least 8 characters");

        setPhone(getInput(scanner, "Enter your phone: ",
                input -> input.matches("\\+?[0-9]{10,15}"), "Invalid phone number"));

        setAddress(getInput(scanner, "Enter your address: ",
                input -> true, ""));

        setAge(Integer.parseInt(getInput(scanner, "Enter your age: ",
                input -> {
                    try {
                        int age = Integer.parseInt(input);
                        return age > 0 && age <= MAX_AGE;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }, "Age must be between 1-" + MAX_AGE)));

        customerCollection.add(this);
        System.out.println("Registration successful! Your ID: " + this.userID);
    }

    private String getInput(Scanner scanner, String prompt,
                            Predicate<String> validator, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (validator.test(input)) {
                return input;
            }
            System.out.println("Error: " + errorMsg);
        }
    }

    private boolean isEmailUnique(String email) {
        return customerCollection.stream()
                .noneMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }

    public void addFlightBooking(Flight flight, int tickets) {
        int index = findFlightIndex(flight);
        if (index >= 0) {
            numOfTicketsBookedByUser.set(index,
                    numOfTicketsBookedByUser.get(index) + tickets);
        } else {
            flightsRegisteredByUser.add(flight);
            numOfTicketsBookedByUser.add(tickets);
        }
    }

    private int findFlightIndex(Flight flight) {
        for (int i = 0; i < flightsRegisteredByUser.size(); i++) {
            if (flightsRegisteredByUser.get(i).getFlightNumber()
                    .equals(flight.getFlightNumber())) {
                return i;
            }
        }
        return -1;
    }

    // Getters and Setters
    public String getUserID() { return userID; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getAddress() { return address; }
    public int getAge() { return age; }
    public List<Flight> getFlights() { return Collections.unmodifiableList(flightsRegisteredByUser); }
    public List<Integer> getTicketsBooked() { return Collections.unmodifiableList(numOfTicketsBookedByUser); }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setPhone(String phone) {
        if (phone == null || !phone.matches("\\+?[0-9]{10,15}")) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address != null ? address.trim() : "";
    }

    public void setAge(int age) {
        if (age <= 0 || age > MAX_AGE) {
            throw new IllegalArgumentException("Age must be between 1-" + MAX_AGE);
        }
        this.age = age;
    }

    public static List<Customer> getAllCustomers() {
        return Collections.unmodifiableList(customerCollection);
    }
}