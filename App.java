import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.io.*;
import java.util.*;

public class App {
    private static final int AES_KEY_LENGTH = 128;
    private static final int RSA_KEY_LENGTH = 2048;

    private static KeyPair rsaKeyPair;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    hash(scanner);
                    break;
                case 2:
                    desEncrypt(scanner);
                    break;
                case 3:
                    desDecrypt(scanner);
                    break;
                case 4:
                    aesEncrypt(scanner);
                    break;
                case 5:
                    aesDecrypt(scanner);
                    break;
                case 6:
                    rsaEncrypt(scanner);
                    break;
                case 7:
                    rsaDecrypt(scanner);
                    break;
                case 8:
                    System.out.println("Exiting program.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Encryption App Menu ---");
        System.out.println("1. Hash");
        System.out.println("2. DES Encrypt");
        System.out.println("3. DES Decrypt");
        System.out.println("4. AES Encrypt");
        System.out.println("5. AES Decrypt");
        System.out.println("6. RSA Encrypt");
        System.out.println("7. RSA Decrypt");
        System.out.println("8. Exit");
    }

    private static void hash(Scanner scanner) {
        System.out.print("Enter input string: ");
        String input = scanner.nextLine();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes());
            String hashedString = Base64.getEncoder().encodeToString(hashedBytes);
            System.out.println("Hashed string: " + hashedString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static SecretKey generateAESKey(Scanner scanner) {
        SecretKey key = null;
        boolean validKey = false;
        do {
            System.out.print("Enter key (must be 16, 24, or 32 characters long): ");
            String keyString = scanner.nextLine();
            if (keyString.length() == 16 || keyString.length() == 24 || keyString.length() == 32) {
                validKey = true;
                key = new SecretKeySpec(keyString.getBytes(), "AES");
            } else {
                System.out.println("Invalid key size. Key must be 16, 24, or 32 characters long.");
            }
        } while (!validKey);
        return key;
    }

    private static void desEncrypt(Scanner scanner) {
        System.out.print("Enter input string: ");
        String input = scanner.nextLine();
        System.out.print("Enter output file path: ");
        String output = scanner.nextLine();
        SecretKey key = generateDESKey(scanner, 8);
        if (key == null) return;
        performEncryption("DES", "DES/ECB/PKCS5Padding", key, input, output);
    }

    private static void desDecrypt(Scanner scanner) {
        System.out.print("Enter input file path: ");
        String input = scanner.nextLine();
        System.out.print("Enter output file path: ");
        String output = scanner.nextLine();
        SecretKey key = generateDESKey(scanner, 8);
        if (key == null) return;
        performDecryption("DES", "DES/ECB/PKCS5Padding", key, input, output);
    }

    private static SecretKey generateDESKey(Scanner scanner, int keyLength) {
        SecretKey key = null;
        boolean validKey = false;
        do {
            System.out.print("Enter key (must be " + keyLength + " characters long): ");
            String keyString = scanner.nextLine();
            if (keyString.length() == keyLength) {
                validKey = true;
                key = new SecretKeySpec(keyString.getBytes(), "DES");
            } else {
                System.out.println("Invalid key size. Key must be " + keyLength + " characters long.");
            }
        } while (!validKey);
        return key;
    }

    private static void aesEncrypt(Scanner scanner) {
        System.out.print("Enter input string: ");
        String input = scanner.nextLine();
        System.out.print("Enter output file path: ");
        String output = scanner.nextLine();
        SecretKey key = generateAESKey(scanner);
        if (key == null) return;
        performEncryption("AES", "AES/ECB/PKCS5Padding", key, input, output);
    }

    private static void aesDecrypt(Scanner scanner) {
        System.out.print("Enter input file path: ");
        String input = scanner.nextLine();
        System.out.print("Enter output file path: ");
        String output = scanner.nextLine();
        SecretKey key = generateAESKey(scanner);
        if (key == null) return;
        performDecryption("AES", "AES/ECB/PKCS5Padding", key, input, output);
    }

    private static void rsaEncrypt(Scanner scanner) {
        if (rsaKeyPair == null) {
            rsaKeyPair = generateRSAKeyPair();
            if (rsaKeyPair == null) return;
        }
        System.out.print("Enter input string: ");
        String input = scanner.nextLine();
        System.out.print("Enter output file path: ");
        String output = scanner.nextLine();
        performEncryption("RSA", "RSA/ECB/PKCS1Padding", rsaKeyPair.getPublic(), input, output);
    }

    private static void rsaDecrypt(Scanner scanner) {
        if (rsaKeyPair == null) {
            System.out.println("RSA key pair not generated yet. Cannot perform decryption.");
            return;
        }
        System.out.print("Enter input file path: ");
        String input = scanner.nextLine();
        System.out.print("Enter output file path: ");
        String output = scanner.nextLine();
        performDecryption("RSA", "RSA/ECB/PKCS1Padding", rsaKeyPair.getPrivate(), input, output);
    }

    private static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(RSA_KEY_LENGTH);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void performEncryption(String algorithm, String transformation, Key key, String input, String output) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());
            writeToFile(output, encryptedBytes);
            System.out.println(algorithm + " encrypted string written to file: " + output);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    private static void performDecryption(String algorithm, String transformation, Key key, String input, String output) {
        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(readFromFile(input));
            System.out.println(algorithm + " decrypted string: " + new String(decryptedBytes));
            writeToFile(output, decryptedBytes);
            System.out.println(algorithm + " decrypted string written to file: " + output);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(String filePath, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFromFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] data = new byte[fis.available()];
            fis.read(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
