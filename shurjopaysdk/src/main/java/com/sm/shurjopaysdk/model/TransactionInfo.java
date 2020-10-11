package com.sm.shurjopaysdk.model;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class TransactionInfo {
  private String txID;
  private String bankTxID;
  private String bankTxStatus;
  private double txnAmount;
  private int spCode;
  private String gateWay;
  private String method;
  private String requestTime;
  private String paymentTime;
  private String cardHolderName;
  private String cardNumber;

  @Override
  public String toString() {
    return "TransactionInfo{" +
        "txID='" + txID + '\'' +
        ", bankTxID='" + bankTxID + '\'' +
        ", bankTxStatus='" + bankTxStatus + '\'' +
        ", txnAmount=" + txnAmount +
        ", spCode=" + spCode +
        ", gateWay='" + gateWay + '\'' +
        ", method='" + method + '\'' +
        ", requestTime='" + requestTime + '\'' +
        ", paymentTime='" + paymentTime + '\'' +
        ", cardHolderName='" + cardHolderName + '\'' +
        ", cardNumber='" + cardNumber + '\'' +
        '}';
  }

  public String getTxID() {
    return txID;
  }

  public void setTxID(String txID) {
    this.txID = txID;
  }

  public String getBankTxID() {
    return bankTxID;
  }

  public void setBankTxID(String bankTxID) {
    this.bankTxID = bankTxID;
  }

  public String getBankTxStatus() {
    return bankTxStatus;
  }

  public void setBankTxStatus(String bankTxStatus) {
    this.bankTxStatus = bankTxStatus;
  }

  public double getTxnAmount() {
    return txnAmount;
  }

  public void setTxnAmount(double txnAmount) {
    this.txnAmount = txnAmount;
  }

  public int getSpCode() {
    return spCode;
  }

  public void setSpCode(int spCode) {
    this.spCode = spCode;
  }

  public String getGateWay() {
    return gateWay;
  }

  public void setGateWay(String gateWay) {
    this.gateWay = gateWay;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(String requestTime) {
    this.requestTime = requestTime;
  }

  public String getPaymentTime() {
    return paymentTime;
  }

  public void setPaymentTime(String paymentTime) {
    this.paymentTime = paymentTime;
  }

  public String getCardHolderName() {
    return cardHolderName;
  }

  public void setCardHolderName(String cardHolderName) {
    this.cardHolderName = cardHolderName;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
  }
}
