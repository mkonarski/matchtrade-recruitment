package com.matchtrade.feed.coinbase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class CoinbaseMessageHandlerTest {

    @Test
    void shouldHandleTickerMessage() {
        //given
        AtomicReference<TickerMessage> msg = new AtomicReference<>();
        CoinbaseMessageHandler handler = new CoinbaseMessageHandler(msg::set, objectMapper());

        //when
        handler.onMessage(tickerMessage());

        //then
        assertThat(msg.get()).extracting(
                TickerMessage::getSymbol,
                TickerMessage::getBid,
                TickerMessage::getAsk
        ).containsExactly(
                "BTC-USD",
                BigDecimal.valueOf(38854.74),
                BigDecimal.valueOf(38858.53)
        );
    }

    @Test
    void shouldNotHandleInvalidTickerMessage() {
        //given
        AtomicReference<TickerMessage> msg = new AtomicReference<>();
        CoinbaseMessageHandler handler = new CoinbaseMessageHandler(msg::set, objectMapper());

        //when
        handler.onMessage(invalidTickerMessage());

        //then
        assertThat(msg.get()).isNull();
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    private String tickerMessage() {
        return "{\"type\":\"ticker\",\"sequence\":34410269314,\"product_id\":\"BTC-USD\",\"price\":\"38858.53\",\"open_24h\":\"37145.14\",\"volume_24h\":\"18305.67298922\",\"low_24h\":\"37093.71\",\"high_24h\":\"39014.33\",\"volume_30d\":\"469723.14494718\",\"best_bid\":\"38854.74\",\"best_ask\":\"38858.53\",\"side\":\"buy\",\"time\":\"2022-02-23T09:37:40.613029Z\",\"trade_id\":285397594,\"last_size\":\"0.00110079\"}";
    }

    private String invalidTickerMessage() {
        return "{\"type\":\"InvalidTicker\",\"sequence\":34410269314,\"product_id\":\"BTC-USD\",\"price\":\"38858.53\",\"open_24h\":\"37145.14\",\"volume_24h\":\"18305.67298922\",\"low_24h\":\"37093.71\",\"high_24h\":\"39014.33\",\"volume_30d\":\"469723.14494718\",\"best_bid\":\"38854.74\",\"best_ask\":\"38858.53\",\"side\":\"buy\",\"time\":\"2022-02-23T09:37:40.613029Z\",\"trade_id\":285397594,\"last_size\":\"0.00110079\"}";
    }
}