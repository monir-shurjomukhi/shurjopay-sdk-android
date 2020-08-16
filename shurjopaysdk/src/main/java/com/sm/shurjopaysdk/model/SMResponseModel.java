package com.sm.shurjopaysdk.model;

//        _                     _        ___  ___        _     _      _       _      _        _
//       | |                   (_)       |  \/  |       | |   | |    (_)     | |    | |      | |
//   ___ | |__   _   _  _ __    _   ___  | .  . | _   _ | | __| |__   _      | |    | |_   __| |
//  / __|| '_ \ | | | || '__|  | | / _ \ | |\/| || | | || |/ /| '_ \ | |     | |    | __| / _` |
//  \__ \| | | || |_| || |     | || (_) || |  | || |_| ||   < | | | || |     | |____| |_ | (_| | _
//  |___/|_| |_| \__,_||_|     | | \___/ \_|  |_/ \__,_||_|\_\|_| |_||_|     \_____/ \__| \__,_|(_)
//                            _/ |
//

public class SMResponseModel {
  private boolean success;
  private String bank_tx_id;
  private String bank_status;
  private String amount;
  private String method;
  private String card_holder;
  private String card_number;
  private String spay_id;
  private String tc_status;
  private String err_msg;
  private String tc_amount;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getBank_tx_id() {
    return bank_tx_id;
  }

  public void setBank_tx_id(String bank_tx_id) {
    this.bank_tx_id = bank_tx_id;
  }

  public String getBank_status() {
    return bank_status;
  }

  public void setBank_status(String bank_status) {
    this.bank_status = bank_status;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getCard_holder() {
    return card_holder;
  }

  public void setCard_holder(String card_holder) {
    this.card_holder = card_holder;
  }

  public String getCard_number() {
    return card_number;
  }

  public void setCard_number(String card_number) {
    this.card_number = card_number;
  }

  public String getSpay_id() {
    return spay_id;
  }

  public void setSpay_id(String spay_id) {
    this.spay_id = spay_id;
  }

  public String getTc_status() {
    return tc_status;
  }

  public void setTc_status(String tc_status) {
    this.tc_status = tc_status;
  }

  public String getErr_msg() {
    return err_msg;
  }

  public void setErr_msg(String err_msg) {
    this.err_msg = err_msg;
  }

  public String getTc_amount() {
    return tc_amount;
  }

  public void setTc_amount(String tc_amount) {
    this.tc_amount = tc_amount;
  }

  @Override
  public String toString() {
    return "SMResponseModel{"
        + "success="
        + success
        + ", bank_tx_id='"
        + bank_tx_id
        + '\''
        + ", bank_status='"
        + bank_status
        + '\''
        + ", amount='"
        + amount
        + '\''
        + ", method='"
        + method
        + '\''
        + ", card_holder='"
        + card_holder
        + '\''
        + ", card_number='"
        + card_number
        + '\''
        + ", spay_id='"
        + spay_id
        + '\''
        + ", tc_status='"
        + tc_status
        + '\''
        + ", err_msg='"
        + err_msg
        + '\''
        + ", tc_amount='"
        + tc_amount
        + '\''
        + '}';
  }
}
