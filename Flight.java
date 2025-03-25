import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

public class Flight extends FlightDistance {
    private static final int MIN_SEATS = 75;
    private static final int MAX_SEATS = 500;
    private static final double AVERAGE_SPEED_KNOTS = 450.0;
    private static int nextFlightDay = 0;
    private static final List<Flight> flightList = new ArrayList<>();

    private final String flightNumber;
    private final String fromCity;
    private final String toCity;
    private final String gate;
    private final LocalDateTime departureTime;
    private final Duration flightDuration;
    private int availableSeats;
    private final List<Customer> registeredCustomers;

    public Flight() {
        this("", "", "", "", LocalDateTime.now(), 0, 0);
    }

    public Flight(String flightNumber, String fromCity, String toCity,
                  String gate, LocalDateTime departureTime,
                  double distanceMiles, int totalSeats) {
        this.flightNumber = validateFlightNumber(flightNumber);
        this.fromCity = validateCity(fromCity);
        this.toCity = validateCity(toCity);
        this.gate = validateGate(gate);
        this.departureTime = validateDepartureTime(departureTime);
        this.flightDuration = calculateFlightDuration(distanceMiles);
        this.availableSeats = validateSeats(totalSeats);
        this.registeredCustomers = new ArrayList<>();
        flightList.add(this);
    }

    // Validation methods
    private String validateFlightNumber(String number) {
        if (number == null || !number.matches("^[A-Z]{2}-\\d{3}$")) {
            throw new IllegalArgumentException("Invalid flight number format");
        }
        return number;
    }

    private String validateCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        return city.trim();
    }

    private String validateGate(String gate) {
        if (gate == null || !gate.matches("^[A-Z]\\d{1,2}$")) {
            throw new IllegalArgumentException("Invalid gate format");
        }
        return gate;
    }

    private LocalDateTime validateDepartureTime(LocalDateTime time) {
        if (time == null || time.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Departure time must be in future");
        }
        return time;
    }

    private int validateSeats(int seats) {
        if (seats < MIN_SEATS || seats > MAX_SEATS) {
            throw new IllegalArgumentException(
                    String.format("Seats must be between %d-%d", MIN_SEATS, MAX_SEATS));
        }
        return seats;
    }

    // Core methods
    private Duration calculateFlightDuration(double distanceMiles) {
        double hours = distanceMiles / AVERAGE_SPEED_KNOTS;
        long minutes = Math.round((hours % 1) * 60);
        return Duration.ofHours((long) hours).plusMinutes(minutes);
    }

    public LocalDateTime getArrivalTime() {
        return departureTime.plus(flightDuration);
    }

    public boolean bookSeats(Customer customer, int seats) {
        if (seats <= 0 || seats > availableSeats) {
            return false;
        }

        if (!registeredCustomers.contains(customer)) {
            registeredCustomers.add(customer);
        }
        availableSeats -= seats;
        customer.addFlightBooking(this, seats);
        return true;
    }

    public static void scheduleRandomFlights(int count) {
        RandomGenerator random = new RandomGenerator();
        for (int i = 0; i < count; i++) {
            String[][] destinations = random.randomDestinations();
            String[] distances = calculateDistance(
                    Double.parseDouble(destinations[0][1]),
                    Double.parseDouble(destinations[0][2]),
                    Double.parseDouble(destinations[1][1]),
                    Double.parseDouble(destinations[1][2])
            );

            new Flight(
                    random.generateFlightNumber(),
                    destinations[0][0],
                    destinations[1][0],
                    random.generateGate(),
                    generateRandomDepartureTime(),
                    Double.parseDouble(distances[0]),
                    random.generateSeats()
            );
        }
    }

    private static LocalDateTime generateRandomDepartureTime() {
        nextFlightDay += (int)(Math.random() * 7) + 1;
        return LocalDateTime.now()
                .plusDays(nextFlightDay)
                .plusHours(nextFlightDay)
                .withMinute(0)
                .truncatedTo(ChronoUnit.HOURS);
    }

    // Getters
    public String getFlightNumber() { return flightNumber; }
    public String getFromCity() { return fromCity; }
    public String getToCity() { return toCity; }
    public String getGate() { return gate; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public Duration getFlightDuration() { return flightDuration; }
    public int getAvailableSeats() { return availableSeats; }
    public List<Customer> getPassengers() { return Collections.unmodifiableList(registeredCustomers); }
    public static List<Flight> getAllFlights() { return Collections.unmodifiableList(flightList); }

    // Display methods
    public void displaySchedule() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm");
        System.out.printf("%-8s | %-15s -> %-15s | Dep: %s | Arr: %s | Gate: %-4s | Seats: %-3d%n",
                flightNumber,
                fromCity,
                toCity,
                departureTime.format(formatter),
                getArrivalTime().format(formatter),
                gate,
                availableSeats);
    }

    public static void displayAllFlights() {
        System.out.println("\n=== Flight Schedule ===");
        flightList.forEach(Flight::displaySchedule);
    }
}