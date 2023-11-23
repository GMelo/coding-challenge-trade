package org.gmelo.assigment.bitmex.orderbook.service;

import org.gmelo.assigment.bitmex.orderbook.model.Order;
import org.gmelo.assigment.bitmex.orderbook.model.OrderComparator;
import org.gmelo.assigment.bitmex.orderbook.model.Output;
import org.gmelo.assigment.bitmex.orderbook.model.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Optional.ofNullable;

/**
 * Service that matches a list of Orders to create a list of trades
 */
@Service
public class TradeMatchingServiceImpl implements TradeMatchingService {

    private static final Logger logger = LoggerFactory.getLogger(TradeMatchingServiceImpl.class);
    public static final Comparator<Order> COMPARATOR_PRICE = new OrderComparator();

    /**
     * Matches orders as they arrive, aggressively matching the sell orders to the buy orders with the highest price, as
     * per: "As there are no Sell orders yet, they rest on the order book as follows (note Buy for 98 is lowest priority):
     * Bids (buying)         Asks (selling)
     * Volume  Price (p)
     * 1,000   99
     * 500     99
     * 1,200   98"
     * <p>
     * And when a buy order arrives it is matches against the lowest price for the selling, as per
     * 50,000 99 | 100 500
     * 25,500 98 | 100 10,000
     * | 103 100
     * | 105 20,000
     *
     * @param orderList the input from the csv
     * @return Output with trades
     */
    public Output matchOrders(List<Order> orderList) {
        logger.info("Started Order Matching ...");
        PriorityQueue<Order> sellingOrders = new PriorityQueue<>(COMPARATOR_PRICE.reversed());
        PriorityQueue<Order> buyingOrders = new PriorityQueue<>(COMPARATOR_PRICE);
        List<Trade> createdTrades = new ArrayList<>();
        for (Order order : orderList) {
            if (order.isBuy()) {
                List<Trade> matched = matchInputOrder(sellingOrders, order);
                createdTrades.addAll(matched);
                if (order.hasOutstandingVolume()) {
                    buyingOrders.add(order);
                }
            } else {
                List<Trade> matched = matchInputOrder(buyingOrders, order);
                createdTrades.addAll(matched);
                if (order.hasOutstandingVolume()) {
                    sellingOrders.add(order);
                }
            }
        }
        // we re-sort as stream does not guarantee the ordering
        List<Order> buyingOrdersList = buyingOrders.stream().sorted(COMPARATOR_PRICE).toList();
        List<Order> sellingOrdersList = sellingOrders.stream().sorted(COMPARATOR_PRICE.reversed()).toList();
        Output output = new Output(
                createdTrades,
                buyingOrdersList,
                sellingOrdersList
        );

        logger.info("Finished order matching produced output: {}", output);

        return output;
    }

    protected List<Trade> matchInputOrder(PriorityQueue<Order> orderList,
                                          Order order) {
        if (orderList.isEmpty()) return Collections.emptyList();
        List<Trade> tradeList = new ArrayList<>();
        Order firstElement = orderList.peek();
        if (firstElement.matches(order)) {
            if (firstElement.getVolume() >= order.getVolume()) {
                Integer tradeVolume = order.getVolume();
                Integer outstandingValue = firstElement.reduceVolume(tradeVolume);
                order.allVolumeMatched();
                if (outstandingValue == 0) {
                    orderList.remove(firstElement);
                }
                List<Trade> trade = createTradeWithUnknown(order, firstElement, tradeVolume);
                tradeList.addAll(trade);
                return tradeList;
            } else {
                Integer tradeVolume = firstElement.getVolume();
                order.reduceVolume(firstElement.getVolume());
                firstElement.allVolumeMatched();
                orderList.remove(firstElement);
                List<Trade> trade = createTradeWithUnknown(order, firstElement, tradeVolume);
                tradeList.addAll(trade);
                List<Trade> returned = matchInputOrder(orderList, order);
                tradeList.addAll(returned);
            }
        }
        return tradeList;
    }

    protected List<Trade> createTradeWithUnknown(Order order1,
                                                 Order order2,
                                                 Integer tradeVolume) {
        if (order1.isBuy()) {
            return createTrade(order1, order2, tradeVolume);
        }
        return createTrade(order2, order1, tradeVolume);
    }

    protected List<Trade> createTrade(Order buyOrder,
                                      Order sellOrder,
                                      Integer tradeVolume) {
        List<Trade> tradeList = new ArrayList<>();
        if (
                (!buyOrder.isIceberg() || buyOrder.getIcebergLimit() >= tradeVolume) &&
                        (!sellOrder.isIceberg() || sellOrder.getIcebergLimit() >= tradeVolume)) {
            tradeList.add(new Trade(buyOrder, sellOrder, tradeVolume));
        } else {
            Integer icebergLimit = Math.min(
                    ofNullable(sellOrder.getIcebergLimit()).orElse(MAX_VALUE),
                    ofNullable(buyOrder.getIcebergLimit()).orElse(MAX_VALUE));
            do {
                tradeList.add(new Trade(buyOrder, sellOrder, icebergLimit));
                tradeVolume = tradeVolume - icebergLimit;
            } while (icebergLimit < tradeVolume);
            tradeList.add(new Trade(buyOrder, sellOrder, tradeVolume));
        }
        return tradeList;
    }

}
