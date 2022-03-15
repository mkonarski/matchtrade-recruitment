package com.matchtrade.feed.coinbase;

import java.util.Arrays;

public enum MessageType {
    TICKER("ticker"),
    UNKNOWN("unknown");

    private final String typeName;

    MessageType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static MessageType findByName(String type) {
        return Arrays.stream(values())
                .filter(msgType -> msgType.getTypeName().equals(type))
                .findFirst()
                .orElse(UNKNOWN);

    }
}
