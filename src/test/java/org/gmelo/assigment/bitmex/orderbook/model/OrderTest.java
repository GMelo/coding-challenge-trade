package org.gmelo.assigment.bitmex.orderbook.model;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {


    @Test
    void reduceVolume_WhenValid() {
        Order order = new Order("1001", Direction.B, 100, 130, null);
        order.reduceVolume(50);
        assertEquals(50, order.getVolume());
    }

    @Test
    void allVolumeMatched() {
        Order order = new Order("1001", Direction.B, 100, 130, null);
        order.allVolumeMatched();
        assertEquals(0, order.getVolume());
    }

    @Test
    void isBuy() {
        Order orderB = new Order("1001", Direction.B, 100, 130, null);
        assertTrue(orderB.isBuy());
        Order orderS = new Order("1001", Direction.S, 100, 130, null);
        assertFalse(orderS.isBuy());
    }

    @Test
    void hasOutstandingVolume_whenHasVolume() {
        Order order = new Order("1001", Direction.B, 100, 130, null);
        assertTrue(order.hasOutstandingVolume());
    }

    @Test
    void hasOutstandingVolume_whenHasNoVolume() {
        Order order = new Order("1001", Direction.B, 0, 130, null);
        assertFalse(order.hasOutstandingVolume());
    }

    @Test
    void matches_WhenBuyMatches() {
        Order orderB = new Order("1001", Direction.B, 100, 140, null);
        Order orderS = new Order("1001", Direction.S, 100, 130, null);
        assertTrue(orderB.matches(orderS));
    }

    @Test
    void matches_WhenBuyNotMatches() {
        Order orderB = new Order("1001", Direction.B, 100, 140, null);
        Order orderS = new Order("1001", Direction.S, 100, 150, null);
        assertFalse(orderB.matches(orderS));
    }

    @Test
    void matches_WhenSellMatches() {
        Order orderB = new Order("1001", Direction.S, 100, 140, null);
        Order orderS = new Order("1001", Direction.B, 100, 150, null);
        assertTrue(orderB.matches(orderS));
    }

    @Test
    void matches_WhenSellNotMatches() {
        Order orderB = new Order("1001", Direction.S, 100, 140, null);
        Order orderS = new Order("1001", Direction.B, 100, 130, null);
        assertFalse(orderB.matches(orderS));
    }

    @Test
    void isIceberg_WhenIs() {
        Order orderB = new Order("1001", Direction.S, 100, 140, 50);
        assertTrue(orderB.isIceberg());
    }

    @Test
    void isIceberg_WhenIsnt() {
        Order orderB = new Order("1001", Direction.S, 100, 140, null);
        assertFalse(orderB.isIceberg());
    }
}