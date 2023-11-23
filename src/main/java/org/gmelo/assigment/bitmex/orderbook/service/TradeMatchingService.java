package org.gmelo.assigment.bitmex.orderbook.service;

import org.gmelo.assigment.bitmex.orderbook.model.Order;
import org.gmelo.assigment.bitmex.orderbook.model.Output;
import org.gmelo.assigment.bitmex.orderbook.model.Trade;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Optional.ofNullable;

public interface TradeMatchingService {


    public Output matchOrders(List<Order> orderList);

}
