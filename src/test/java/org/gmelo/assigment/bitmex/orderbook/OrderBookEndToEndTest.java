package org.gmelo.assigment.bitmex.orderbook;

import org.gmelo.assigment.bitmex.orderbook.model.Order;
import org.gmelo.assigment.bitmex.orderbook.model.Output;
import org.gmelo.assigment.bitmex.orderbook.model.Trade;
import org.gmelo.assigment.bitmex.orderbook.service.CSVReaderService;
import org.gmelo.assigment.bitmex.orderbook.service.TradeMatchingService;
import org.gmelo.assigment.bitmex.orderbook.service.TradeMatchingServiceImpl;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderBookEndToEndTest {

    private final CSVReaderService csvReaderService = new CSVReaderService();

    TradeMatchingService tradeMatchingService = new TradeMatchingServiceImpl();

    @Test
    public void endToEndTest_WhenNoMatch() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("sampleNoMatch.txt");
        Path path = Paths.get(url.toURI());
        List<Order> orderList = csvReaderService.parseFile(path);
        Output output = tradeMatchingService.matchOrders(orderList);
        List<Order> buyOrders = output.getBuyList();
        List<Order> sellOrders = output.getSellingList();
        List<Trade> tradeList = output.getTradeList();
        System.out.println(output.prettyPrint());
        //trades
        assertEquals(0, tradeList.size());
        //buy
        assertEquals(50000, buyOrders.get(0).getVolume());
        assertEquals(99, buyOrders.get(0).getPriceInPence());
        assertEquals(25500, buyOrders.get(1).getVolume());
        assertEquals(98, buyOrders.get(1).getPriceInPence());
        //sell
        assertEquals(500, sellOrders.get(0).getVolume());
        assertEquals(100, sellOrders.get(0).getPriceInPence());
        assertEquals(10000, sellOrders.get(1).getVolume());
        assertEquals(100, sellOrders.get(1).getPriceInPence());
        assertEquals(100, sellOrders.get(2).getVolume());
        assertEquals(103, sellOrders.get(2).getPriceInPence());
        assertEquals(20000, sellOrders.get(3).getVolume());
        assertEquals(105, sellOrders.get(3).getPriceInPence());

    }

    @Test
    public void endToEndTest_WhenMatch() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("sampleMatch.txt");
        Path path = Paths.get(url.toURI());
        List<Order> orderList = csvReaderService.parseFile(path);
        Output output = tradeMatchingService.matchOrders(orderList);
        System.out.println(output.prettyPrint());
        List<Order> buyOrders = output.getBuyList();
        List<Order> sellOrders = output.getSellingList();
        List<Trade> tradeList = output.getTradeList();
        //buy
        assertEquals(50000, buyOrders.get(0).getVolume());
        assertEquals(99, buyOrders.get(0).getPriceInPence());
        assertEquals(25500, buyOrders.get(1).getVolume());
        assertEquals(98, buyOrders.get(1).getPriceInPence());
        //sell
        assertEquals(14600, sellOrders.get(0).getVolume());
        assertEquals(105, sellOrders.get(0).getPriceInPence());
        //trades
        assertEquals("10006", tradeList.get(0).getBuyOrderId());
        assertEquals("10001", tradeList.get(0).getSellOrderId());
        assertEquals("10006", tradeList.get(1).getBuyOrderId());
        assertEquals("10002", tradeList.get(1).getSellOrderId());
        assertEquals("10006", tradeList.get(2).getBuyOrderId());
        assertEquals("10004", tradeList.get(2).getSellOrderId());
        assertEquals("10006", tradeList.get(3).getBuyOrderId());
        assertEquals("10005", tradeList.get(3).getSellOrderId());


    }

    @Test
    public void endToEndTest_WhenMatchIceberg() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("sampleMatchIceberg.txt");
        Path path = Paths.get(url.toURI());
        List<Order> orderList = csvReaderService.parseFile(path);
        Output output = tradeMatchingService.matchOrders(orderList);
        System.out.println(output.prettyPrint());
        List<Order> buyOrders = output.getBuyList();
        List<Order> sellOrders = output.getSellingList();
        List<Trade> tradeList = output.getTradeList();
        //sell
        assertEquals(20000, sellOrders.get(0).getVolume());
        assertEquals(101, sellOrders.get(0).getPriceInPence());
        //buy
        assertEquals(82500, buyOrders.get(0).getVolume());
        assertEquals(100, buyOrders.get(0).getPriceInPence());
        assertEquals(50000, buyOrders.get(1).getVolume());
        assertEquals(99, buyOrders.get(1).getPriceInPence());
        assertEquals(25500, buyOrders.get(2).getVolume());
        assertEquals(98, buyOrders.get(2).getPriceInPence());
        //trades
        assertEquals("ice1", tradeList.get(0).getBuyOrderId());
        assertEquals("10002", tradeList.get(0).getSellOrderId());
        assertEquals("ice1", tradeList.get(1).getBuyOrderId());
        assertEquals("10001", tradeList.get(1).getSellOrderId());
    }

    @Test
    public void endToEndTest_WhenMatchIcebergRoll() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("sampleMatchIcebergSplit.txt");
        Path path = Paths.get(url.toURI());
        List<Order> orderList = csvReaderService.parseFile(path);
        Output output = tradeMatchingService.matchOrders(orderList);
        System.out.println(output.prettyPrint());
        List<Order> buyOrders = output.getBuyList();
        List<Order> sellOrders = output.getSellingList();
        List<Trade> tradeList = output.getTradeList();
        //sell
        assertEquals(20000, sellOrders.get(0).getVolume());
        assertEquals(101, sellOrders.get(0).getPriceInPence());
        //buy
        assertEquals(82499, buyOrders.get(0).getVolume());
        assertEquals(100, buyOrders.get(0).getPriceInPence());
        assertEquals(50000, buyOrders.get(1).getVolume());
        assertEquals(99, buyOrders.get(1).getPriceInPence());
        assertEquals(25500, buyOrders.get(2).getVolume());
        assertEquals(98, buyOrders.get(2).getPriceInPence());
        //trades
        assertEquals("ice1", tradeList.get(0).getBuyOrderId());
        assertEquals("10002", tradeList.get(0).getSellOrderId());
        assertEquals("ice1", tradeList.get(2).getBuyOrderId());
        assertEquals("10001", tradeList.get(2).getSellOrderId());
        assertEquals("ice1", tradeList.get(1).getBuyOrderId());
        assertEquals("10002", tradeList.get(1).getSellOrderId());
    }
}