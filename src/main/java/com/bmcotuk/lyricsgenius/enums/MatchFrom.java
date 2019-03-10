package com.bmcotuk.lyricsgenius.enums;

/**
 * Author: B. Mert Cotuk
 * Date:   12.01.2019
 * Time:   14:49
 * https://github.com/bmertcotuk
 */
public enum MatchFrom {

    HEAD("HEAD"),
    TAIL("TAIL"),
    BOTH("BOTH");

    private String value;

    MatchFrom(String value) {
        this.value = value;
    }
}