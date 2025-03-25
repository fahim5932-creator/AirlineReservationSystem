import java.util.Random;

public class RandomGenerator {
    private static final String[][] DESTINATIONS = {
            {"Karachi", "24.871940", "66.988060"},
            {"Bangkok", "13.921430", "100.595337"},
    };

    private final Random random = new Random();

    public String[][] randomDestinations() {
        int origin = random.nextInt(DESTINATIONS.length);
        int destination;
        do {
            destination = random.nextInt(DESTINATIONS.length);
        } while (destination == origin);

        return new String[][] {
                DESTINATIONS[origin],
                DESTINATIONS[destination]
        };
    }

    public int generateSeats() {
        return random.nextInt(500 - 75) + 75;
    }

    public String generateFlightNumber() {
        char first = (char) ('A' + random.nextInt(26));
        char second = (char) ('A' + random.nextInt(26));
        int number = random.nextInt(900) + 100;
        return String.format("%c%c-%d", first, second, number);
    }

    public String generateGate() {
        char letter = (char) ('A' + random.nextInt(26));
        int number = random.nextInt(30) + 1;
        return String.format("%c%d", letter, number);
    }

    public String generateUserId() {
        return String.valueOf(random.nextInt(1000000 - 20000) + 20000);
    }
}