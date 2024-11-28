package org.products.productreviews.app.util;

import org.products.productreviews.app.entities.Account;
import org.products.productreviews.app.entities.Review;
import org.springframework.data.util.Pair;

import java.util.HashSet;
import java.util.Set;

/**
 * Jaccard Distance between accounts: Size of intersection / Size of Union.
 *
 * <br>
 * Each unit in a JDistance set is Product + Rating.
 * <br> <br>
 *
 * <strong>Equality is defined by the following:</strong>
 * <br>
 * Let A and B be JDistanceUnits. <br>
 * <code>
 *     A.product == B.product and A.rating == B.rating
 * </code>
 */
public class JDistance {

    private Account firstAccount;
    private Account secondAccount;

    /**
     * Creates a JDistance object. The Accounts set to A or B can be changed with
     * {@link JDistance#setAccountA(Account)} and {@link JDistance#setAccountB(Account)}
     * @param A The first Account
     * @param B The second Account
     */
    public JDistance(Account A, Account B) {
        this.firstAccount = A;
        this.secondAccount = B;
    }

    /**
     * Creates a JDistance object. A and B must be set with
     * {@link JDistance#setAccountA(Account)} and {@link JDistance#setAccountB(Account)}
     */
    public JDistance() {}

    /**
     * Sets the first account
     * @param A The first account
     */
    public void setAccountA(Account A){
        this.firstAccount = A;
    }

    /**
     * Sets the second account
     * @param B The second account
     */
    public void setAccountB(Account B){
        this.secondAccount = B;
    }

    /**
     * Finds the Jaccard distance between two Accounts.
     *<br>
     * Distance ranges from 0 to 1, where 0 is no similarity and where 1 is exactly the same.
     *
     * @return The Jaccard distance
     * @throws IllegalArgumentException If neither Account A nor Account B are set
     */
    public float distance() throws IllegalArgumentException {

        Pair<Set<JDistUnit>, Set<JDistUnit>> jDistUnitSets = this.createAllJaccardSets();

        Set<JDistUnit> setA = jDistUnitSets.getFirst();
        Set<JDistUnit> setB = jDistUnitSets.getSecond();
        Set<JDistUnit> collectorSetUnion = new HashSet<>(setA);
        Set<JDistUnit> collectorSetIntersection = new HashSet<>(setA);

        collectorSetIntersection.retainAll(setB);
        collectorSetUnion.addAll(setB);

        if (collectorSetUnion.isEmpty()) return 0;

        return (float) collectorSetIntersection.size() / (float) collectorSetUnion.size();
    }

    /**
     * Create JDistUnit Sets for Account A and B
     * @return The JDistUnit Sets for Account A and Account B. The sets are placed in the pair in that order.
     * @throws IllegalArgumentException If neither Account A nor Account B are set
     */
    private Pair<Set<JDistUnit>, Set<JDistUnit>> createAllJaccardSets() throws IllegalArgumentException {
        if (firstAccount == null || secondAccount == null)
            throw new IllegalArgumentException("Both Accounts must be set!!");

        Set<JDistUnit> firstAccountSet = createJacardSet(firstAccount);
        Set<JDistUnit> secondAccountSet = createJacardSet(secondAccount);

        return Pair.of(firstAccountSet, secondAccountSet);
    }

    /**
     * Creates a JDistUnit Set for a specific Account
     * @param account The Account to create the set from.
     * @return A JDistUnit Set
     */
    private Set<JDistUnit> createJacardSet(Account account) {
        Set<JDistUnit> jDistUnitSet = new HashSet<>();

        for (Review review : account.getReviews()) {
            String uniqueProdName = review.getProduct().getName();
            Review.Star rating = review.getRating();
            jDistUnitSet.add(new JDistUnit(uniqueProdName, rating));
        }

        return jDistUnitSet;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     * @apiNote In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * The string output is not necessarily stable over time or across
     * JVM invocations.
     * @implSpec The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     */
    @Override
    public String toString() {
        return "JDistance[" + firstAccount + ", " + secondAccount + "]";
    }
}
