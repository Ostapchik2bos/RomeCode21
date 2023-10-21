package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class RomeCode {

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.,«»\"':!? ";
    public static final int ALPHABET_SIZE = ALPHABET.length();

    public static String encrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            int index = ALPHABET.indexOf(character);
            if (index != -1) {
                result.append(ALPHABET.charAt((index + key) % ALPHABET_SIZE));
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public static String decrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            int index = ALPHABET.indexOf(character);
            if (index != -1) {
                int newIndex = (index - key) % ALPHABET_SIZE;
                if (newIndex < 0) {
                    newIndex += ALPHABET_SIZE; // make sure the index is positive
                }
                result.append(ALPHABET.charAt(newIndex));
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter command (ENCRYPT/DECRYPT/BRUTE_FORCE): ");
        String command = scanner.nextLine();

        System.out.print("Enter file path: ");
        String filePath = scanner.nextLine();

        String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");

        switch (command) {
            case "ENCRYPT":
                System.out.print("Enter encryption key: ");
                int encryptionKey = scanner.nextInt();
                writeToFile(filePath, "[ENCRYPTED]", encrypt(content, encryptionKey));
                break;

            case "DECRYPT":
                System.out.print("Enter decryption key: ");
                int decryptionKey = scanner.nextInt();
                writeToFile(filePath, "[DECRYPTED]", decrypt(content, decryptionKey));
                break;

            case "BRUTE_FORCE":
                for (int key = 0; key < ALPHABET_SIZE; key++) {
                    System.out.println("Key " + key + ": " + decrypt(content, key));
                }
                break;

            default:
                System.out.println("Невідома команда.");
        }

        scanner.close();
    }

    public static void writeToFile(String filePath, String suffix, String content) throws IOException {
        File file = new File(filePath);
        String parentPath = file.getParent();
        String fileNameWithoutExtension = file.getName().replaceFirst("[.][^.]+$", "");
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        String newFileName = fileNameWithoutExtension + " " + suffix + "." + extension;
        Path newPath = Paths.get(parentPath, newFileName);

        Files.write(newPath, content.getBytes("UTF-8"));
    }
}
