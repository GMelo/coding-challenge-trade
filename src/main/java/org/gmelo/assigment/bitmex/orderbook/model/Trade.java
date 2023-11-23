package org.gmelo.assigment.bitmex.orderbook.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Representation of a @Trade, created by receiving two @Orders
 */
public class Trade {

    private static final Logger logger = LoggerFactory.getLogger(Trade.class);
    private final String buyOrderId;
    private final String sellOrderId;
    private final Integer tradeVolume;
    private final Integer soldPrice;

    public Trade(Order buyOrder,
                 Order sellOrder,
                 Integer volume) {
        tradeVolume = volume;
        buyOrderId = buyOrder.getOrderId();
        sellOrderId = sellOrder.getOrderId();
        soldPrice = sellOrder.getPriceInPence();
        logger.debug("Created new {}", this);
    }

    public String getBuyOrderId() {
        return buyOrderId;
    }

    public String getSellOrderId() {
        return sellOrderId;
    }

    public Integer getTradeVolume() {
        return tradeVolume;
    }

    public Integer getSoldPrice() {
        return soldPrice;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "buyOrderId=" + buyOrderId +
                ", sellOrderId=" + sellOrderId +
                ", tradeVolume=" + tradeVolume +
                ", soldPrice=" + soldPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(buyOrderId, trade.buyOrderId) && Objects.equals(sellOrderId, trade.sellOrderId) && Objects.equals(tradeVolume, trade.tradeVolume) && Objects.equals(soldPrice, trade.soldPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyOrderId, sellOrderId, tradeVolume, soldPrice);
    }
}