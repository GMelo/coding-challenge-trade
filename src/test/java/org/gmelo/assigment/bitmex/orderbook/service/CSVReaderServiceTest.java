package org.gmelo.assigment.bitmex.orderbook.service;

import org.gmelo.assigment.bitmex.orderbook.model.Order;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CSVReaderServiceTest {

    private final CSVReaderService csvReaderService = new CSVReaderService();

    @Test
    public void parseFile_WhenSimpleExample() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("sampleNoMatch.txt");
        Path path = Paths.get(url.toURI());
        List<Order> output = csvReaderService.parseFile(path);
        assertEquals(6, output.size());
    }

}