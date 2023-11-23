package org.gmelo.assigment.bitmex.orderbook.model;

/**
 * Representation of the direction of a trade
 */
public enum Direction {

    B("Buy"), S("Sell");

    public final String prettyValue;

    Direction(String prettyValue) {
        this.prettyValue = prettyValue;
    }
}
