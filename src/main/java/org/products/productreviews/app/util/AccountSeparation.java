package org.products.productreviews.app.util;

import org.products.productreviews.app.entities.Account;

import java.util.*;

/**
 * Class AccountSeparation provides a static method to find the degree of separation between two accounts
 */

public class AccountSeparation {

    /**
     * Gets the degree of separation between two accounts. Upper limit of separation is three.
     * @param sourceAccount The account from which separation is being found
     * @param goalAccount The account which the target to reach
     * @return integer representing the degree of separation. -1 is starting node and 0 is a direct follow.
     */
    public static int getSeparation(Account sourceAccount, Account goalAccount) {
        Set<Account> repeatAccounts = new HashSet<>();
        Queue<Account> accountQueue = new ArrayDeque<>();
        accountQueue.add(sourceAccount);

        Account currentAccount;
        int depth = 0;
        int count = accountQueue.size();

        //BFS algorithm
        while (!accountQueue.isEmpty()) {
            // Determining depth of graph -> degree of separation between accounts
            if (count == 0) {
                depth++;
                if (depth == 3) return depth;
                count = accountQueue.size();
            }
            currentAccount = accountQueue.remove();
            count--;

            if (currentAccount.equals(goalAccount)) return depth;
            // Adding all neighbors to the queue except repeat nodes -> the follow list of the current account
            repeatAccounts.add(currentAccount);
            accountQueue.addAll(currentAccount.getFollows());
            accountQueue.removeAll(repeatAccounts);
        }
        // return -1 if no separation is found
        return -1;
    }
}
