package com.sm.shurjopaysdk.model;

public class SMRequestModel {
  private String merchantName;
  private String merchantPass;
  private String userIP;
  private String uniqID;
  private String totalAmount;
  private String paymentOption;
  private String returnURL;

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public String getMerchantPass() {
    return merchantPass;
  }

  public void setMerchantPass(String merchantPass) {
    this.merchantPass = merchantPass;
  }

  public String getUserIP() {
    return userIP;
  }

  public void setUserIP(String userIP) {
    this.userIP = userIP;
  }

  public String getUniqID() {
    return uniqID;
  }

  public void setUniqID(String uniqID) {
    this.uniqID = uniqID;
  }

  public String getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(String totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getPaymentOption() {
    return paymentOption;
  }

  public void setPaymentOption(String paymentOption) {
    this.paymentOption = paymentOption;
  }

  public String getReturnURL() {
    return returnURL;
  }

  public void setReturnURL(String returnURL) {
    this.returnURL = returnURL;
  }

  @Override
  public String toString() {
    return "SMRequestModel{"
        + "merchantName='"
        + merchantName
        + '\''
        + ", merchantPass='"
        + merchantPass
        + '\''
        + ", userIP='"
        + userIP
        + '\''
        + ", uniqID='"
        + uniqID
        + '\''
        + ", totalAmount='"
        + totalAmount
        + '\''
        + ", paymentOption='"
        + paymentOption
        + '\''
        + ", returnURL='"
        + returnURL
        + '\''
        + '}';
  }
}
