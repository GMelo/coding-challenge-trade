package org.gmelo.assigment.bitmex.orderbook.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OrderComparatorTest {

    @Test
    void checkOrdering_() {
        List<OrderDTO> allDTOOrders = new ArrayList<>();
        allDTOOrders.add(new OrderDTO("1002", Direction.B, 100, 130, null));
        allDTOOrders.add(new OrderDTO("1003", Direction.B, 100, 130, null));
        allDTOOrders.add(new OrderDTO("1001", Direction.B, 150, 150, null));

        List<Order> allOrders = allDTOOrders.stream()
                // map from the DTO to the Order
                .map(OrderDTO::toOrder)
                .collect(Collectors.toList());

        PriorityQueue<Order> sellingOrders = new PriorityQueue<>(new OrderComparator());
        for (Order order : allOrders) {
            sellingOrders.add(order);
        }
        Order order = sellingOrders.poll();
        assertEquals("1001", order.getOrderId());
        order = sellingOrders.poll();
        assertEquals("1002", order.getOrderId());
    }

    @Test
    void checkOrdering() {
        PriorityQueue<Order> sellingOrders = new PriorityQueue<>(new OrderComparator());
        sellingOrders.add(new Order("1002", Direction.B, 100, 130, null));
        sellingOrders.add(new Order("1003", Direction.B, 100, 130, null));
        sellingOrders.add(new Order("1001", Direction.B, 150, 150, null));
        Order order = sellingOrders.poll();
        assertEquals("1001", order.getOrderId());
        order = sellingOrders.poll();
        assertEquals("1002", order.getOrderId());
    }
}