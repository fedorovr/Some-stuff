package com.roman_fedorov.cashpoint;

import java.util.Scanner;

public class Cashpoint {
    private static final int[] banknotes = new int[]{5000, 1000, 500, 100, 50, 25, 10, 5, 3, 1};
    private int[] banknotesCount = new int[banknotes.length];

    // Fields for get() method. Store solution for withdrawing.
    private int minRemainder;
    private int[] leftBanknotesOnMinRemainder;

    static void print(String s) {
        System.out.println(">" + s);
    }

    public static void main(String[] args) {
        Cashpoint cashpoint = new Cashpoint();
        Scanner scanner = new Scanner(System.in);
        String input;
        while (!(input = scanner.nextLine().toLowerCase()).equals("quit")) {
            String[] commands = input.split(" ");
            try {
                switch (commands[0]) {
                    case "state":
                        print(String.valueOf(cashpoint.state()));
                        break;
                    case "put":
                        print(String.valueOf(cashpoint.put(Integer.valueOf(commands[1]), Integer.valueOf(commands[2]))));
                        break;
                    case "dump":
                        print(cashpoint.dump());
                        break;
                    case "get":
                        print(cashpoint.get(Integer.valueOf(commands[1])));
                        break;
                    default:
                        throw new RuntimeException("Invalid input");
                }
            } catch (Exception e) {
                System.out.println("Unknown input, please try again");
            }
        }
    }

    private void getMaxAmount(int[] leftBanknotes, int leftSum) {
        // If current solution is better, update global best solution
        if (leftSum < minRemainder) {
            minRemainder = leftSum;
            leftBanknotesOnMinRemainder = leftBanknotes.clone();
        }
        for (int i = 0; i < banknotes.length; i++) {
            // If optimal solution not found, cashpoint has this banknote and we can give it to the user
            if ((minRemainder > 0) && (leftBanknotes[i] > 0) && (leftSum - banknotes[i] >= 0)) {
                int[] newBanknotes = leftBanknotes.clone();
                newBanknotes[i]--;
                getMaxAmount(newBanknotes, leftSum - banknotes[i]);
            }
        }
    }

    public String get(int query) {
        minRemainder = query;
        leftBanknotesOnMinRemainder = banknotesCount.clone();
        getMaxAmount(leftBanknotesOnMinRemainder, query);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < banknotes.length; i++) {
            int banknotesToGive = banknotesCount[i] - leftBanknotesOnMinRemainder[i];
            if (banknotesToGive > 0) {
                sb.append(banknotes[i]).append("=").append(banknotesToGive).append(", ");
            }
        }
        sb.append("всего ").append(query - minRemainder);
        if (minRemainder > 0) {
            sb.append("\n").append("без ").append(minRemainder);
        }
        banknotesCount = leftBanknotesOnMinRemainder.clone();
        return sb.toString();
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < banknotes.length; i++) {
            sb.append(banknotes[i]).append(" ").append(banknotesCount[i]).append("\n");
        }
        return sb.toString();
    }

    public int put(int insertedBanknote, int insertedCount) {
        for (int i = 0; i < banknotes.length; i++) {
            if (banknotes[i] == insertedBanknote) {
                banknotesCount[i] += insertedCount;
            }
        }
        return state();
    }

    public int state() {
        int acc = 0;
        for (int i = 0; i < banknotes.length; i++) {
            acc += banknotes[i] * banknotesCount[i];
        }
        return acc;
    }
}
