package org.gmelo.assigment.bitmex.orderbook.model;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

class OutputTest {

    @Test
    void prettyPrint_WhenOnlyBuy() {
        Output output = new Output(
                emptyList(),
                List.of(new Order("1001", Direction.B, 100, 130, null),
                        new Order("1002", Direction.B, 1000, 99, null)),
                emptyList());

        String val = output.prettyPrint();
        assertEquals(
                "      100   130     |  \n" +
                         "    1,000   99      |  \n"
                , val);
    }

    @Test
    void prettyPrint_WhenOnlySell() {
        Output output = new Output(
                emptyList(),
                emptyList(),
                List.of(new Order("1001", Direction.S, 100, 130, null),
                        new Order("1002", Direction.S, 1000, 99, null))
        );

        String val = output.prettyPrint();
        assertEquals(
                "                    |     130   100      \n" +
                         "                    |      99   1,000    \n", val);
    }

    @Test
    void prettyPrint_WhenOnlySellAndBuyNoTrades() {
        Output output = new Output(
                emptyList(),
                List.of(new Order("1001", Direction.B, 100, 130, null)),
                List.of(new Order("1001", Direction.S, 100, 130, null),
                        new Order("1002", Direction.S, 1000, 99, null))
        );

        String val = output.prettyPrint();
        assertEquals(
                "      100   130     |     130   100      \n" +
                         "                    |      99   1,000    \n", val);
    }

    @Test
    void prettyPrint_WhenOnlySellAndBuyAndTrades() {
        Output output = new Output(
                List.of(new Trade(new Order("1001", Direction.B, 100, 130, null),
                        new Order("1001", Direction.B, 100, 130, null), 400)),
                List.of(new Order("1001", Direction.B, 100, 130, null)),
                List.of(new Order("1001", Direction.S, 100, 130, null),
                        new Order("1002", Direction.S, 1000, 99, null))
        );

        String val = output.prettyPrint();
        assertEquals(
                "trade 1001,1001,130,400\n" +
                        "      100   130     |     130   100      \n" +
                        "                    |      99   1,000    \n", val);
    }


    @Test
    void buyOrderToPrettyString() {
        Output output = new Output(emptyList(), emptyList(), emptyList());
        Order order = new Order("1001", Direction.B, 100, 130, null);
        String val = output.buyOrderToPrettyString(order);
        assertEquals("      100   130   ", val);
    }

    @Test
    void addSellOrderToBuilder() {
        Output output = new Output(emptyList(), emptyList(), emptyList());
        Order order = new Order("1001", Direction.S, 100, 130, null);
        String val = output.addSellOrderToBuilder(order);
        assertEquals("   130   100      ", val);
    }

    @Test
    void getVolumeFor_WhenNotIceberg() {
        Output output = new Output(emptyList(), emptyList(), emptyList());
        Order order = new Order("1001", Direction.B, 100, 130, null);
        Integer volume = output.getVolumeFor(order);
        assertEquals(100, volume);
    }

    @Test
    void getVolumeFor_WhenIceberg() {
        Output output = new Output(emptyList(), emptyList(), emptyList());
        Order order = new Order("1001", Direction.B, 100, 130, 50);
        Integer volume = output.getVolumeFor(order);
        assertEquals(50, volume);
    }
}

