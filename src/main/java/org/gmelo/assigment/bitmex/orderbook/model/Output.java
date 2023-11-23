package org.gmelo.assigment.bitmex.orderbook.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *  Output of Executing the input of a runbook
 */
public class Output {

    private static final Logger logger = LoggerFactory.getLogger(Output.class);

    private final List<Trade> tradeList;
    private final List<Order> buyList;
    private final List<Order> sellingList;

    public Output(List<Trade> tradeList,
                  List<Order> buyList,
                  List<Order> sellingList) {

        this.tradeList = tradeList;
        this.buyList = buyList;
        this.sellingList = sellingList;
        logger.debug("Created new: {}", this);
    }

    /**
     * Formats the list of trades and orders to the expected output
     *
     * @return the string representation of a trade
     */
    public String prettyPrint() {
        StringBuilder builder = new StringBuilder();
        for (Trade trade : tradeList) {
            builder
                    .append("trade ")
                    .append(trade.getBuyOrderId())
                    .append(",")
                    .append(trade.getSellOrderId())
                    .append(",")
                    .append(trade.getSoldPrice())
                    .append(",")
                    .append(trade.getTradeVolume())
                    .append("\n");
        }
        Iterator<Order> unMatchedBuyOrders = buyList.iterator();
        Iterator<Order> unMatchedSellOrders = sellingList.iterator();

        while (unMatchedBuyOrders.hasNext() || unMatchedSellOrders.hasNext()) {
            if (unMatchedBuyOrders.hasNext()) {
                Order buyOrder = unMatchedBuyOrders.next();
                builder.append(buyOrderToPrettyString(buyOrder));
            } else {
                builder.append(String.format("%18s", ""));
            }
            builder
                    .append("  ")
                    .append("|")
                    .append("  ");

            if (unMatchedSellOrders.hasNext()) {
                Order sellOrder = unMatchedSellOrders.next();
                builder.append(addSellOrderToBuilder(sellOrder));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    protected String buyOrderToPrettyString(Order order) {
        return String.format("%,9d   %-6d", getVolumeFor(order), order.getPriceInPence());
    }

    protected String addSellOrderToBuilder(Order order) {
        return String.format("%,6d   %,-9d", order.getPriceInPence(), getVolumeFor(order));

    }

    protected Integer getVolumeFor(Order order) {
        if (order.isIceberg() && (order.getIcebergLimit() <= order.getVolume())) {
            return order.getIcebergLimit();
        } else {
            return order.getVolume();
        }
    }

    /*
     * Auto Generated
     */

    public List<Trade> getTradeList() {
        return tradeList;
    }

    public List<Order> getBuyList() {
        return buyList;
    }

    public List<Order> getSellingList() {
        return sellingList;
    }


    @Override
    public String toString() {
        return "Output{" +
                "tradeList=" + tradeList +
                ", buyList=" + buyList +
                ", sellingList=" + sellingList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Output output = (Output) o;
        return Objects.equals(tradeList, output.tradeList) && Objects.equals(buyList, output.buyList) && Objects.equals(sellingList, output.sellingList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeList, buyList, sellingList);
    }
}