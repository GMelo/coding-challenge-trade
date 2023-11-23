package org.gmelo.assigment.bitmex.orderbook.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static java.lang.System.currentTimeMillis;

/**
 * Representation of an Order
 */
public class Order {

    private static final Logger logger = LoggerFactory.getLogger(Order.class);
    private final String orderId;
    private final Direction direction;
    private Integer volume;
    private final Integer priceInPence;

    //Nullable iceberg Limit
    private final Integer icebergLimit;

    private long createdDate = currentTimeMillis();

    public Order(String orderId,
                 Direction direction,
                 Integer volume,
                 Integer priceInPence,
                 Integer icebergLimit
    ) {
        // ensuring all fields are valid
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        Objects.requireNonNull(direction, "Direction cannot be null");
        Objects.requireNonNull(volume, "Volume cannot be null");
        Objects.requireNonNull(priceInPence, "Price cannot be null");
        this.orderId = orderId;
        this.direction = direction;
        this.volume = volume;
        this.priceInPence = priceInPence;
        this.icebergLimit = icebergLimit;

        logger.debug("Created order: {}", this);
    }


    /**
     * Reduces the outstanding volume of the order by the parameter value
     *
     * @param volume the amount to reduce
     * @return the outstanding volume
     * <p>
     * * For the purpose of this assignment we do not validate the input, as the service only sends valid values
     */
    public Integer reduceVolume(Integer volume) {
        logger.trace("reducing volume of: {} by: {}", this, volume);
        Integer outstandingVolume = this.volume - volume;
        this.volume = outstandingVolume;
        return outstandingVolume;
    }

    public void allVolumeMatched() {
        logger.trace("reducing volume of: {} to zero", this);
        this.volume = 0;
    }

    public boolean isBuy() {
        return direction == Direction.B;
    }

    public boolean hasOutstandingVolume() {
        return volume > 0;
    }

    /**
     * Verifies if this @Order matches the passed order, if this is a buy offer the price must be higher or equal to
     * the price of the offer we are comparing to. If this is a sell order their value must be greater than ours
     *
     * @param order the @Order to compare to
     * @return true if they match or false if they don't
     * <p>
     * For the purpose of this assignment we do not validate the input, as the service only sends valid values
     */
    public boolean matches(Order order) {
        logger.trace("verifying if: {} matches: {}", this, order);
        if (isBuy()) {
            return priceInPence >= order.priceInPence;
        } else {
            return priceInPence <= order.priceInPence;
        }
    }

    public boolean isIceberg() {
        return icebergLimit != null;
    }

    /*
    Auto generated fields
     */
    public String getOrderId() {
        return orderId;
    }

    public Direction getDirection() {
        return direction;
    }

    public Integer getVolume() {
        return volume;
    }

    public Integer getPriceInPence() {
        return priceInPence;
    }

    public Integer getIcebergLimit() {
        return icebergLimit;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) && direction == order.direction && Objects.equals(volume, order.volume) && Objects.equals(priceInPence, order.priceInPence) && Objects.equals(icebergLimit, order.icebergLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, direction, volume, priceInPence, icebergLimit);
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
