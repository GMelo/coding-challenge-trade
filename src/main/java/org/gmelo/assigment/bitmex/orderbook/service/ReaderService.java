package org.gmelo.assigment.bitmex.orderbook.service;

import org.gmelo.assigment.bitmex.orderbook.model.Order;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ReaderService {

     List<Order> parseFile(Path path) throws IOException ;
}
