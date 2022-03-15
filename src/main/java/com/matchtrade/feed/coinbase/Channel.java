package com.matchtrade.feed.coinbase;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Channel {
    private String name;
    @JsonProperty("product_ids")
    private List<String> symbols;

    public Channel(String name, List<String> symbols) {
        this.name = name;
        this.symbols = symbols;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }
}
