package org.gmelo.assigment.bitmex.orderbook.model;

import com.opencsv.bean.CsvBindByPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * DTO to use with open CSV.
 * this is done to allow the order not to be dependent on a library and have null protected fields.
 */
public class OrderDTO {

    private static final Logger logger = LoggerFactory.getLogger(OrderDTO.class);
    @CsvBindByPosition(position = 0)
    public String orderId;
    @CsvBindByPosition(position = 1)
    public Direction direction;
    @CsvBindByPosition(position = 2)
    public Integer priceInPence;
    @CsvBindByPosition(position = 3)
    public Integer volume;
    @CsvBindByPosition(position = 4)
    public Integer icebergLimit;

    public OrderDTO(){
        logger.trace("Created new OrderDTO");
    }

    public OrderDTO(String orderId, Direction direction, Integer priceInPence, Integer volume, Integer icebergLimit) {
        this.orderId = orderId;
        this.direction = direction;
        this.priceInPence = priceInPence;
        this.volume = volume;
        this.icebergLimit = icebergLimit;
    }

    public Order toOrder() {
        // This is the only time we perform checking in the solution, as it comes from external input
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(direction, "Direction cannot be null");
        Objects.requireNonNull(volume, "Volume cannot be null");
        Objects.requireNonNull(priceInPence, "Price cannot be null");

        return new Order(orderId, direction, volume, priceInPence, icebergLimit);
    }

    @Override
    public String toString() {
        return "Order{" +
                "volume=" + volume +
                ", direction='" + direction + '\'' +
                ", orderId=" + orderId +
                ", priceInPence=" + priceInPence +
                '}';
    }
}
