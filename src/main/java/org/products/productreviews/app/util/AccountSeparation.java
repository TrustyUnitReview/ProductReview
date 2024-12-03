package org.products.productreviews.app.util;

import org.products.productreviews.app.entities.Account;

import java.util.*;

public class AccountSeparation {

    /**
     * Gets the degree of separation between two accounts. Upper limit of separation is three.
     * @param sourceAccount The account from which separation is being found
     * @param goalAccount The account which the target to reach
     * @return integer representing the degree of separation. -1 is starting node and 0 is a direct follow.
     */
    public static int getSeparation(Account sourceAccount, Account goalAccount) {
        Set<Account> repeatAccounts = new HashSet<>();
        Queue<Account> nodeQueue = new ArrayDeque<>();
        nodeQueue.add(sourceAccount);

        Account currentAccount;
        int depth = -1;
        int count = nodeQueue.size();

        //BFS algorithm
        while (!nodeQueue.isEmpty()) {
            // Determining depth of graph -> degree of separation between accounts
            if (count == 0) {
                depth++;
                if (depth == 3) return depth;
                count = nodeQueue.size();
            }

            currentAccount = nodeQueue.remove();
            count--;

            if (currentAccount.equals(goalAccount)) return depth;

            // Adding all neighbors to the queue except repeat nodes -> the follow list of the current account
            repeatAccounts.add(currentAccount);
            nodeQueue.addAll(currentAccount.getFollows());
            nodeQueue.removeAll(repeatAccounts);
        }
        return -1;
    }
}
