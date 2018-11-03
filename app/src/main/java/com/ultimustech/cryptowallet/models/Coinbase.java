package com.ultimustech.cryptowallet.models;

public class Coinbase {
    private static final String COINBASE_ADDR = "ada9814897";
    private static final String COINBASE_PHONE= "233548419676";
    public  static double COINBASE_BALANCE = 100000;

    public static String getCoinbaseAddr(){
        return COINBASE_ADDR;
    }

    public static String getCoinbasePhone(){
        return COINBASE_PHONE;
    }

    public void setCoinbaseBalance(double COINBASE_BALANCE){
        this.COINBASE_BALANCE = COINBASE_BALANCE;
    }

    public double getCoinbaseBalance(){
        return this.COINBASE_BALANCE;
    }

}
