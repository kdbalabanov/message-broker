package main.java.com.company.messagebroker.utils;

public enum MessageType {
    BUY_ORDER, SELL_ORDER, ORDER_CONFIRMED;

    public static MessageType getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
