import java.util.Scanner;

// Define the Frequency Enum
enum Frequency {
    WEEK(1),
    TWO_WEEK(2),
    FOUR_WEEK(4),
    MONTH(0),  // Set to 0 to skip monthly calculations, as it doesn't divide to a whole number of weeks
    QUARTER(13),
    YEAR(52);

    private final int weekMultiplier;

    Frequency(int weekMultiplier) {
        this.weekMultiplier = weekMultiplier;
    }

    public int getWeekMultiplier() {
        return weekMultiplier;
    }

    public static Frequency fromString(String input) {
        try {
            return Frequency.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

// Define the RegularAmount class
class RegularAmount {
    private Frequency frequency;
    private String amount; // Stored as String to retain pence precision

    public RegularAmount(Frequency frequency, String amount) {
        this.frequency = frequency;
        this.amount = amount;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public String getAmount() {
        return amount;
    }
}

// Implement the WeeklyDivisibleValidator
class WeeklyDivisibleValidator {

    public boolean isValid(RegularAmount regularAmount) {
        if (regularAmount == null || regularAmount.getFrequency() == null) return true; // Handle null cases as no error

        Frequency frequency = regularAmount.getFrequency();
        String amountStr = regularAmount.getAmount();

        // Return true for MONTH to comply with no error requirement
        if (frequency == Frequency.MONTH) return true;

        try {
            // Convert amount to pence (integer)
            int amountInPence = (int) (Double.parseDouble(amountStr) * 100);
            int weeks = frequency.getWeekMultiplier();

            // Calculate amount per week in pence
            return amountInPence % weeks == 0;
        } catch (NumberFormatException e) {
            // According to the spec, invalid or blank amounts should not produce an error
            return true;
        }
    }
}

// Main class to test the validation logic
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt for frequency
        System.out.print("Enter frequency (WEEK, TWO_WEEK, FOUR_WEEK, MONTH, QUARTER, YEAR): ");
        String frequencyInput = scanner.nextLine();
        Frequency frequency = Frequency.fromString(frequencyInput);

        if (frequency == null) {
            System.out.println("Invalid frequency entered.");
            scanner.close();
            return;
        }

        // Prompt for amount
        System.out.print("Enter amount in GBP (e.g., 57.90): ");
        String amount = scanner.nextLine();

        RegularAmount regularAmount = new RegularAmount(frequency, amount);
        WeeklyDivisibleValidator validator = new WeeklyDivisibleValidator();

        // Validate and print result
        boolean isValid = validator.isValid(regularAmount);
        System.out.println("Amount Valid: " + isValid);

        scanner.close();
    }
}
