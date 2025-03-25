public abstract class FlightDistance {
    private static final double MILES_TO_KM = 1.609344;
    private static final double MILES_TO_NAUTICAL = 0.8684;

    public abstract String toString(int i);

    public String[] calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double distanceMiles = calculateDistanceInMiles(lat1, lon1, lat2, lon2);
        return new String[] {
                String.format("%.2f", distanceMiles * MILES_TO_NAUTICAL),
                String.format("%.2f", distanceMiles * MILES_TO_KM),
                String.format("%.2f", distanceMiles)
        };
    }

    private double calculateDistanceInMiles(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double distance = Math.sin(degreeToRadian(lat1)) * Math.sin(degreeToRadian(lat2))
                + Math.cos(degreeToRadian(lat1)) * Math.cos(degreeToRadian(lat2))
                * Math.cos(degreeToRadian(theta));
        distance = Math.acos(distance);
        distance = radianToDegree(distance);
        return distance * 60 * 1.1515;
    }

    private double degreeToRadian(double deg) {
        return deg * Math.PI / 180.0;
    }

    private double radianToDegree(double rad) {
        return rad * 180.0 / Math.PI;
    }

    public void displayMeasurementInfo() {
        System.out.println("\n=== Flight Distance Information ===");
        System.out.println("1. Distances are calculated using airport coordinates");
        System.out.println("2. Actual flight paths may vary due to air traffic regulations");
        System.out.println("3. Flight times are estimates based on 450 knots ground speed");
        System.out.println("4. Arrival times may vary by ±1 hour due to operational factors");
    }
}