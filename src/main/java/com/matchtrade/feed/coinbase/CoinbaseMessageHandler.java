package com.matchtrade.feed.coinbase;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

class CoinbaseMessageHandler implements MessageHandler.Whole<String> {

    private static final Logger log = LoggerFactory.getLogger(CoinbaseMessageHandler.class);
    private final ObjectMapper mapper;
    private final Consumer<TickerMessage> onTickerMessage;

    CoinbaseMessageHandler(Consumer<TickerMessage> onTickerMessage, ObjectMapper mapper) {
        this.onTickerMessage = onTickerMessage;
        this.mapper = mapper;
    }

    @Override
    public void onMessage(String message) {
        log.trace("Message received: {}", message);
        try {
            JsonNode jsonNode = mapper.readTree(message);
            String type = jsonNode.get("type").asText();
            switch (MessageType.findByName(type)) {
                case TICKER: {
                    JsonParser parser = mapper.createParser(message);
                    TickerMessage tickerMessage = mapper.readValue(parser, TickerMessage.class);
                    onTickerMessage.accept(tickerMessage);
                    break;
                }
                case UNKNOWN: {
                    log.warn("Unknown message: {}", message);
                    break;
                }
                default: {
                    log.warn("Undefined type");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
