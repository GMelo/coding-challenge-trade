package org.gmelo.assigment.bitmex.orderbook.service;

import org.gmelo.assigment.bitmex.orderbook.model.Direction;
import org.gmelo.assigment.bitmex.orderbook.model.Output;
import org.gmelo.assigment.bitmex.orderbook.model.Trade;
import org.gmelo.assigment.bitmex.orderbook.model.Order;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TradeMatchingServiceTest {

    TradeMatchingServiceImpl tradeMatchingService = new TradeMatchingServiceImpl();

    @Test
    void matchOrders_WhenSingleSellingElement() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order("1001", Direction.S, 100, 130, null));
        Output output = tradeMatchingService.matchOrders(allOrders);
        List<Order> sellingOrders = output.getSellingList();
        assertEquals(sellingOrders.size(), 1);
        Order orderReturned = sellingOrders.get(0);
        assertEquals(orderReturned.getVolume(), 100);
        assertEquals(orderReturned.getPriceInPence(), 130);
        assertEquals(0, output.getTradeList().size());
    }

    @Test
    void matchOrders_WhenPerfectMatch() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order("1001", Direction.S, 100, 130, null));
        allOrders.add(new Order("1001", Direction.B, 100, 130, null));
        Output output = tradeMatchingService.matchOrders(allOrders);
        List<Order> sellingOrders = output.getSellingList();
        assertEquals(sellingOrders.size(), 0);
        List<Order> buyOrders = output.getBuyList();
        assertEquals(buyOrders.size(), 0);

        assertEquals(1, output.getTradeList().size());
    }

    @Test
    void matchOrders_WhenPerfectMatchIceberg() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order("1001", Direction.S, 1000, 130, 500));
        allOrders.add(new Order("1001", Direction.B, 1000, 130, null));
        Output output = tradeMatchingService.matchOrders(allOrders);
        List<Order> sellingOrders = output.getSellingList();
        assertEquals(sellingOrders.size(), 0);
        List<Order> buyOrders = output.getBuyList();
        assertEquals(buyOrders.size(), 0);

        assertEquals(2, output.getTradeList().size());
    }

    @Test
    void matchOrders_WhenHighestMatchFirst() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order("1001", Direction.S, 1000, 99, null));
        allOrders.add(new Order("1002", Direction.S, 1000, 130, null));
        allOrders.add(new Order("1003", Direction.B, 1000, 130, null));
        Output output = tradeMatchingService.matchOrders(allOrders);
        List<Order> sellingOrders = output.getSellingList();
        assertEquals(sellingOrders.size(), 1);
        Order order = sellingOrders.get(0);
        assertEquals("1002", order.getOrderId());
        List<Order> buyOrders = output.getBuyList();
        assertEquals(buyOrders.size(), 0);

        assertEquals(1, output.getTradeList().size());
    }

    @Test
    void matchOrders_WhenPerfectMatchIcebergBuyer() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order("1001", Direction.S, 1000, 130, null));
        allOrders.add(new Order("1001", Direction.B, 1000, 130, 500));
        Output output = tradeMatchingService.matchOrders(allOrders);
        List<Order> sellingOrders = output.getSellingList();
        assertEquals(sellingOrders.size(), 0);
        List<Order> buyOrders = output.getBuyList();
        assertEquals(buyOrders.size(), 0);

        assertEquals(2, output.getTradeList().size());
    }

    @Test
    void matchOrders_WhenSingleSellingElementBuy() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order("1001", Direction.B, 100, 130, null));
        Output output = tradeMatchingService.matchOrders(allOrders);
        List<Order> buyingOrders = output.getBuyList();
        assertEquals(buyingOrders.size(), 1);
        Order orderReturned = buyingOrders.get(0);
        assertEquals(orderReturned.getVolume(), 100);
        assertEquals(orderReturned.getPriceInPence(), 130);
        assertEquals(0, output.getTradeList().size());
    }

    @Test
    void matchOrders_twoObjectsNoMatch() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order("1001", Direction.S, 100, 130, null));
        allOrders.add(new Order("1003", Direction.B, 50, 120, null));
        Output output = tradeMatchingService.matchOrders(allOrders);
        List<Order> sellingOrders = output.getSellingList();
        List<Order> buyingOrders = output.getBuyList();
        assertEquals(sellingOrders.size(), 1);
        assertEquals(buyingOrders.size(), 1);
        Order orderReturned = sellingOrders.get(0);
        assertEquals(orderReturned.getVolume(), 100);
        orderReturned = buyingOrders.get(0);
        assertEquals(orderReturned.getVolume(), 50);
        assertEquals(output.getTradeList().size(), 0);
    }

    @Test
    void matchOrders_When3Matches() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.add(new Order("1001", Direction.S, 100, 130, null));
        allOrders.add(new Order("1002", Direction.S, 40, 100, null));
        allOrders.add(new Order("1003", Direction.B, 50, 150, null));
        Output output = tradeMatchingService.matchOrders(allOrders);
        List<Order> sellingOrders = output.getSellingList();
        assertEquals(sellingOrders.size(), 1);
        Order orderReturned = sellingOrders.get(0);
        assertEquals(orderReturned.getVolume(), 90);
        assertEquals(orderReturned.getPriceInPence(), 130);
        assertEquals(output.getTradeList().size(), 2);
    }

    @Test
    void matchInputOrder_WhenOnlyTwo() {
        PriorityQueue<Order> sellingOrders = new PriorityQueue<>(Comparator.comparing(Order::getPriceInPence));
        sellingOrders.add(new Order("1001", Direction.S, 100, 130, null));
        sellingOrders.add(new Order("1002", Direction.S, 40, 100, null));
        Order order = new Order("1003", Direction.B, 50, 150, null);
        List<Trade> output = tradeMatchingService.matchInputOrder(sellingOrders, order);
        assertEquals(sellingOrders.size(), 1);
        Order orderReturned = sellingOrders.peek();
        assertEquals(orderReturned.getVolume(), 90);
        assertEquals(orderReturned.getPriceInPence(), 130);
        assertEquals(output.size(), 2);

    }

    @Test
    void matchInputOrder_WhenOnlyTwoReverse() {
        PriorityQueue<Order> sellingOrders = new PriorityQueue<>(Comparator.comparing(Order::getPriceInPence));
        sellingOrders.add(new Order("1001", Direction.B, 100, 130, null));
        sellingOrders.add(new Order("1002", Direction.B, 40, 100, null));
        Order order = new Order("1003", Direction.S, 50, 99, null);
        List<Trade> output = tradeMatchingService.matchInputOrder(sellingOrders, order);
        assertEquals(sellingOrders.size(), 1);
        Order orderReturned = sellingOrders.peek();
        assertEquals(orderReturned.getVolume(), 90);
        assertEquals(orderReturned.getPriceInPence(), 130);
        assertEquals(output.size(), 2);
    }

}