import java.util.*;

public class RolesAndPermissions {
    private static final String[][] ADMIN_CREDENTIALS = new String[10][2];
    static {
        ADMIN_CREDENTIALS[0] = new String[]{"root", "root"};
    }

    public Optional<String> authenticateAdmin(String username, String password) {
        for (String[] creds : ADMIN_CREDENTIALS) {
            if (creds != null && creds[0].equals(username) {
                return creds[1].equals(password) ? Optional.of(username) : Optional.empty();
            }
        }
        return Optional.empty();
    }

    public Optional<String> authenticateCustomer(String email, String password) {
        return Customer.getAllCustomers().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email)
                        && c.getPassword().equals(password))
                .findFirst()
                .map(Customer::getUserID);
    }

    public boolean registerAdmin(String username, String password) {
        for (int i = 1; i < ADMIN_CREDENTIALS.length; i++) {
            if (ADMIN_CREDENTIALS[i] == null) {
                ADMIN_CREDENTIALS[i] = new String[]{username, password};
                return true;
            }
        }
        return false;
    }
}