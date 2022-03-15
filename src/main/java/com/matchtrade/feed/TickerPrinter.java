package com.matchtrade.feed;

import com.matchtrade.feed.coinbase.TickerMessage;

import java.util.function.Consumer;

public class TickerPrinter implements Consumer<TickerMessage> {

    @Override
    public void accept(TickerMessage tickerMessage) {
        System.out.println(tickerMessage);
    }
}
