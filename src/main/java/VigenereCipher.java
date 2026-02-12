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

        System.out.println("=== Vigenere Cipher Encryption/Decryption System ===\n");

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
        // Perform encryption
        String encrypted;
        if (mode == 1) {
            encrypted = encryptBasic(plaintext, key);
        } else {
            encrypted = encryptAdvanced(plaintext, key);
        }

        System.out.println("\n--- Results ---");
        System.out.println("Original text: " + plaintext);
        System.out.println("Encryption key: " + key);
        System.out.println("Encrypted text: " + encrypted);

        // Perform decryption
        String decrypted;
        if (mode == 1) {
            decrypted = decryptBasic(encrypted, key);
        } else {
            decrypted = decryptAdvanced(encrypted, key);
        }

        System.out.println("Decrypted text: " + decrypted);

        // Verify correctness
        if (plaintext.equals(decrypted)) {
            System.out.println("\n✓ Decryption successful! Original and decrypted texts match.");
        } else {
            System.out.println("\n✗ Decryption mismatch!");
        }

        scanner.close();
    }

    //Allows user to select encryption mode
    private static int selectMode(Scanner scanner) {
        System.out.println("Select encryption mode:");
        System.out.println("1. Basic mode (alphabetic characters only)");
        System.out.println("2. Advanced mode (full ASCII range 32-126)");
        System.out.print("Enter your choice (1 or 2): ");

        int mode = 1;
        try {
            mode = Integer.parseInt(scanner.nextLine());
            if (mode != 1 && mode != 2) {
                System.out.println("Invalid choice. Using basic mode (1) by default.\n");
                mode = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Using basic mode (1) by default.\n");
        }

        System.out.println();
        return mode;
    }

    // Validates user inputs based on the selected mode
    private static boolean validateInputs(String plaintext, String key, int mode) {
        if (plaintext == null || plaintext.isEmpty() || key == null || key.isEmpty()) {
            return false;
        }

        // In basic mode key must contain only alphabetic characters
        if (mode == 1) {
            for (char c : key.toCharArray()) {
                if (!Character.isLetter(c)) {
                    return false;
                }
            }
        }

        return true;
    }

    //BASIC MODE, Encrypts text using Vigenere cipher alphabetic characters only
    private static String encryptBasic(String plaintext, String key) {
        StringBuilder result = new StringBuilder();
        String normalizedKey = key.toUpperCase();
        int keyIndex = 0;

        for (int i = 0; i < plaintext.length(); i++) {
            char currentChar = plaintext.charAt(i);

            if (Character.isLetter(currentChar)) {
                boolean isUpperCase = Character.isUpperCase(currentChar);
                char normalizedChar = Character.toUpperCase(currentChar);

                //Find position in alphabet
                int charPos = findCharPosition(normalizedChar);
                int keyPos = findCharPosition(normalizedKey.charAt(keyIndex % normalizedKey.length()));

                //Encrypt using Vigenere formula
                int encryptedPos = (charPos + keyPos) % ALPHABET.length;
                char encryptedChar = ALPHABET[encryptedPos];

                //Preserve original case
                if (!isUpperCase) {
                    encryptedChar = Character.toLowerCase(encryptedChar);
                }

                result.append(encryptedChar);
                keyIndex++;
            } else {
                //Non alphabetic characters remain unchanged
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    //BASIC MODE: Decrypts text using Vigenere cipher
    private static String decryptBasic(String ciphertext, String key) {
        StringBuilder result = new StringBuilder();
        String normalizedKey = key.toUpperCase();
        int keyIndex = 0;

        for (int i = 0; i < ciphertext.length(); i++) {
            char currentChar = ciphertext.charAt(i);

            if (Character.isLetter(currentChar)) {
                boolean isUpperCase = Character.isUpperCase(currentChar);
                char normalizedChar = Character.toUpperCase(currentChar);

                //Find position in alphabet
                int charPos = findCharPosition(normalizedChar);
                int keyPos = findCharPosition(normalizedKey.charAt(keyIndex % normalizedKey.length()));

                //Decrypt using Vigenere formula
                int decryptedPos = (charPos - keyPos + ALPHABET.length) % ALPHABET.length;
                char decryptedChar = ALPHABET[decryptedPos];

                //Preserve original case
                if (!isUpperCase) {
                    decryptedChar = Character.toLowerCase(decryptedChar);
                }

                result.append(decryptedChar);
                keyIndex++;
            } else {
                //Non alphabetic characters remain unchanged
                result.append(currentChar);
            }
        }

        return result.toString();
    }
    //ADVANCED MODE Encrypts all ASCII characters (32-126)
    private static String encryptAdvanced(String plaintext, String key) {
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (int i = 0; i < plaintext.length(); i++) {
            char currentChar = plaintext.charAt(i);
            int charValue = (int) currentChar;

            //Only encrypt characters within the defined ASCII range
            if (charValue >= ASCII_START && charValue <= ASCII_END) {
                //Get key character
                char keyChar = key.charAt(keyIndex % key.length());
                int keyValue = (int) keyChar;

                //Normalize to 0 based position
                int charPos = charValue - ASCII_START;
                int keyPos = keyValue - ASCII_START;

                //Encrypt using Vigenere formula
                int encryptedPos = (charPos + keyPos) % ASCII_RANGE;
                char encryptedChar = (char) (encryptedPos + ASCII_START);

                result.append(encryptedChar);
                keyIndex++;
            } else {
                //Characters outside range remain unchanged
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    //ADVANCED MODE Decrypts all ASCII characters (32-126)
    private static String decryptAdvanced(String ciphertext, String key) {
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (int i = 0; i < ciphertext.length(); i++) {
            char currentChar = ciphertext.charAt(i);
            int charValue = (int) currentChar;

            // Only decrypt characters within the defined ASCII range
            if (charValue >= ASCII_START && charValue <= ASCII_END) {
                //Get key character
                char keyChar = key.charAt(keyIndex % key.length());
                int keyValue = (int) keyChar;

                //Normalize to 0 based position
                int charPos = charValue - ASCII_START;
                int keyPos = keyValue - ASCII_START;

                //Decrypt using Vigenere formula
                int decryptedPos = (charPos - keyPos + ASCII_RANGE) % ASCII_RANGE;
                char decryptedChar = (char) (decryptedPos + ASCII_START);

                result.append(decryptedChar);
                keyIndex++;
            } else {
                //characters outside range remain unchanged
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    //Finds the position of a character in the alphabet array
    private static int findCharPosition(char c) {
        for (int i = 0; i < ALPHABET.length; i++) {
            if (ALPHABET[i] == c) {
                return i;
            }
        }
        return -1;
    }
}

