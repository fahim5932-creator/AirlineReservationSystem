import java.util.*;
import java.util.stream.Collectors;

public class FlightReservation implements DisplayClass {
    private static final int MAX_TICKETS_PER_BOOKING = 10;

    public void bookFlight(String flightNo, int tickets, String userId) {
        Optional<Flight> flightOpt = findFlight(flightNo);
        Optional<Customer> customerOpt = findCustomer(userId);

        if (flightOpt.isEmpty() || customerOpt.isEmpty()) {
            System.out.println("Invalid flight number or user ID");
            return;
        }

        Flight flight = flightOpt.get();
        Customer customer = customerOpt.get();

        if (tickets <= 0 || tickets > MAX_TICKETS_PER_BOOKING) {
            System.out.printf("Ticket count must be between 1-%d%n", MAX_TICKETS_PER_BOOKING);
            return;
        }

        if (flight.getAvailableSeats() < tickets) {
            System.out.println("Not enough available seats");
            return;
        }

        flight.bookSeats(customer, tickets);
        System.out.printf("Successfully booked %d tickets on flight %s%n", tickets, flightNo);
    }

    public void cancelBooking(String userId) {
        Optional<Customer> customerOpt = findCustomer(userId);
        if (customerOpt.isEmpty()) {
            System.out.println("User not found");
            return;
        }

        Customer customer = customerOpt.get();
        if (customer.getFlights().isEmpty()) {
            System.out.println("No bookings to cancel");
            return;
        }

        displayUserFlights(customer);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter flight number to cancel: ");
        String flightNo = scanner.nextLine();

        Optional<Flight> flightOpt = findFlight(flightNo);
        if (flightOpt.isEmpty()) {
            System.out.println("Invalid flight number");
            return;
        }

        Flight flight = flightOpt.get();
        int index = customer.getFlights().indexOf(flight);
        if (index == -1) {
            System.out.println("No booking found for this flight");
            return;
        }

        System.out.print("Enter number of tickets to cancel: ");
        int tickets = scanner.nextInt();
        int bookedTickets = customer.getTicketsBooked().get(index);

        if (tickets <= 0 || tickets > bookedTickets) {
            System.out.printf("Must cancel between 1-%d tickets%n", bookedTickets);
            return;
        }

        if (tickets == bookedTickets) {
            customer.getFlights().remove(index);
            customer.getTicketsBooked().remove(index);
        } else {
            customer.getTicketsBooked().set(index, bookedTickets - tickets);
        }

        flight.setAvailableSeats(flight.getAvailableSeats() + tickets);
        System.out.printf("Cancelled %d tickets on flight %s%n", tickets, flightNo);
    }

    // Helper methods
    private Optional<Flight> findFlight(String flightNo) {
        return Flight.getAllFlights().stream()
                .filter(f -> f.getFlightNumber().equalsIgnoreCase(flightNo))
                .findFirst();
    }

    private Optional<Customer> findCustomer(String userId) {
        return Customer.getAllCustomers().stream()
                .filter(c -> c.getUserID().equals(userId))
                .findFirst();
    }

    // DisplayClass implementation
    @Override
    public void displayRegisteredUsersForAllFlight() {
        Flight.getAllFlights().forEach(flight -> {
            if (!flight.getPassengers().isEmpty()) {
                displayFlightPassengers(flight);
            }
        });
    }

    @Override
    public void displayRegisteredUsersForASpecificFlight(String flightNum) {
        findFlight(flightNum).ifPresentOrElse(
                this::displayFlightPassengers,
                () -> System.out.println("Flight not found")
        );
    }

    @Override
    public void displayFlightsRegisteredByOneUser(String userId) {
        findCustomer(userId).ifPresentOrElse(
                this::displayUserFlights,
                () -> System.out.println("User not found")
        );
    }

    private void displayFlightPassengers(Flight flight) {
        System.out.printf("%n=== Passengers on Flight %s ===%n", flight.getFlightNumber());
        System.out.printf("%-15s | %-20s | %-5s | %-10s%n",
                "User ID", "Name", "Age", "Tickets");

        for (int i = 0; i < flight.getPassengers().size(); i++) {
            Customer passenger = flight.getPassengers().get(i);
            int tickets = passenger.getTicketsBooked().get(
                    passenger.getFlights().indexOf(flight));

            System.out.printf("%-15s | %-20s | %-5d | %-10d%n",
                    passenger.getUserID(),
                    passenger.getName(),
                    passenger.getAge(),
                    tickets);
        }
    }

    private void displayUserFlights(Customer customer) {
        System.out.printf("%n=== Bookings for %s ===%n", customer.getName());
        System.out.printf("%-10s | %-15s -> %-15s | %-15s | %-5s%n",
                "Flight", "From", "To", "Departure", "Tickets");

        for (int i = 0; i < customer.getFlights().size(); i++) {
            Flight flight = customer.getFlights().get(i);
            System.out.printf("%-10s | %-15s -> %-15s | %-15s | %-5d%n",
                    flight.getFlightNumber(),
                    flight.getFromCity(),
                    flight.getToCity(),
                    flight.getDepartureTime().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    customer.getTicketsBooked().get(i));
        }
    }

    @Override
    public void displayHeaderForUsers(Flight flight, List<Customer> customers) {
        displayFlightPassengers(flight);
    }
}