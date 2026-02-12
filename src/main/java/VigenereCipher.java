import java.util.Scanner;

public class VigenereCipher {

    // Constants for the character sets
    private static final char[] ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private static final int ASCII_START = 32;  // Space character
    private static final int ASCII_END = 126;   // Tilde character
    private static final int ASCII_RANGE = ASCII_END - ASCII_START + 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Vigenère Cipher Encryption/Decryption System ===\n");

        // Select mode
        int mode = selectMode(scanner);

        // Get user input
        System.out.print("Enter the plaintext message: ");
        String plaintext = scanner.nextLine();

        System.out.print("Enter the encryption key: ");
        String key = scanner.nextLine();

        // Validate inputs
        if (!validateInputs(plaintext, key, mode)) {
            System.out.println("Invalid input. Please ensure both plaintext and key are not empty.");
            if (mode == 1) {
                System.out.println("In basic mode, the key must contain only alphabetic characters.");
            }
            scanner.close();
            return;
        }

    }
}