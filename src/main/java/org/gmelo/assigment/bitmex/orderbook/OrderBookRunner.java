package org.gmelo.assigment.bitmex.orderbook;

import jakarta.annotation.Resource;
import org.gmelo.assigment.bitmex.orderbook.model.Order;
import org.gmelo.assigment.bitmex.orderbook.model.Output;
import org.gmelo.assigment.bitmex.orderbook.service.ReaderService;
import org.gmelo.assigment.bitmex.orderbook.service.TradeMatchingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public class OrderBookRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(OrderBookRunner.class);

    @Resource
    ReaderService readerService;

    @Resource
    TradeMatchingService tradeMatchingService;


    @Override
    public void run(String... args) throws Exception {
        String path;
        try {
            path = args[0];
            List<Order> orderList = readerService.parseFile(Path.of(path));
            Output output = tradeMatchingService.matchOrders(orderList);
            System.out.println(output.prettyPrint());
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("No file passed", e);
            System.out.println("No file parameter passed to application");
        } catch (NullPointerException e){
            logger.error("Malformed file", e);
            System.out.println("Malformed file received - missing fields");
        }


    }
}
