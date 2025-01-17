package org.example.utility;

import java.util.Scanner;

public class InputUtils {

    private final static Scanner scanner = new Scanner(System.in);

    public static String readString() {
        while (true) {
            String input = scanner.nextLine();
            if (input.startsWith(" ") || input.length() < 2 || input.length() > 35) {
                System.out.println("Please input a valid name, no longer than 35 characters");
            } else {
                return input;
            }
        }
    }

    public static int readInt() {
        while (true) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (Exception e) {
                System.out.println("Invalid input, has to be a whole number");
                scanner.nextLine();
            }
        }
    }
}