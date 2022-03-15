package com.matchtrade.feed;

import com.matchtrade.feed.coinbase.CoinbaseClient;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        final TickerPrinter tickPrinter = new TickerPrinter();

        final CoinbaseClient client = new CoinbaseClient("wss://ws-feed.pro.coinbase.com", List.of("BTC-USD", "ETH-USD"));
        client.addListener(tickPrinter);
        client.connect();


        Thread.sleep(10000);
    }
}
