package org.gmelo.assigment.bitmex.orderbook.model;

import java.util.Comparator;
import java.util.Objects;

/**
 * Comparator to return the reverse order of the price and on a tie to compare the created date
 */
public class OrderComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        if (Objects.equals(o1.getPriceInPence(), o2.getPriceInPence())) {
            return Long.compare(o1.getCreatedDate(), o2.getCreatedDate());
        }
        //reversed the compare as we want to order by the highest
        return Integer.compare(o2.getPriceInPence(), o1.getPriceInPence());
    }
}
