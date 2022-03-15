package com.matchtrade.feed.coinbase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.websocket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ClientEndpoint
public class CoinbaseClient extends Endpoint {
    private static final Logger log = LoggerFactory.getLogger(CoinbaseClient.class);

    private final String url;
    private final List<String> symbols;
    private final MessageHandler messageHandler;
    private final ObjectMapper mapper;
    private final List<Consumer<TickerMessage>> listeners;
    private Session session;

    public CoinbaseClient(String url, List<String> symbols) {
        this.url = url;
        this.symbols = symbols;
        this.mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        this.listeners = new ArrayList<>();
        this.messageHandler = createMessageHandler();
    }

    public void addListener(Consumer<TickerMessage> onTickerMessage) {
        listeners.add(onTickerMessage);
    }

    public void connect() {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, new URI(url));
        } catch (DeploymentException | IOException | URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.session.addMessageHandler(messageHandler);
        log.info("Open session ...");
        SubscriptionMessage subscription = SubscriptionMessage.tickerSubscription(symbols);
        sendSubscriptionMessage(subscription);
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        this.session = null;
        log.info("Close session ...");
    }

    @Override
    public void onError(Session session, Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
    }

    public void sendSubscriptionMessage(SubscriptionMessage message) {
        try {
            sendMessage(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendMessage(String message) {
        try {
            log.info("Send msg: {}", message);
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private MessageHandler createMessageHandler() {
        return new CoinbaseMessageHandler(
                tickerMessage -> listeners.forEach(listener -> listener.accept(tickerMessage)),
                mapper
        );
    }
}
