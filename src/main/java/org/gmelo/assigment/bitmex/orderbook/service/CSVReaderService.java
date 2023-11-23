package org.gmelo.assigment.bitmex.orderbook.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.gmelo.assigment.bitmex.orderbook.model.Order;
import org.gmelo.assigment.bitmex.orderbook.model.OrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
/**
 * Service wrapping the csv reader
 */
public class CSVReaderService implements ReaderService {

    private static final Logger logger = LoggerFactory.getLogger(CSVReaderService.class);

    /**
     * Parse a file from path and builds the list of Orders
     *
     * @param path the path of the csv file
     * @return a list of orders
     * @throws IOException if there are issues with the parsing
     */
    public List<Order> parseFile(Path path) throws IOException {
        logger.info("Attempting to parse file {}", path);
        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<OrderDTO> cb = new CsvToBeanBuilder<OrderDTO>(reader)
                    .withType(OrderDTO.class)
                    .build();

            List<Order> orderList = cb
                    .parse()
                    .stream()
                    // map from the DTO to the Order
                    .map(OrderDTO::toOrder)
                    .collect(Collectors.toList());
            logger.info("Parsed file {} with output {}", path, orderList);
            return orderList;
        }
    }
}
