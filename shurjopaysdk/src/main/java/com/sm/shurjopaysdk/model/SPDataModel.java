package com.sm.shurjopaysdk.model;

public class SPDataModel {
  private String spdata;

  @Override
  public String toString() {
    return "SPDataModel{" +
        "spdata='" + spdata + '\'' +
        '}';
  }

  public String getSpdata() {
    return spdata;
  }

  public void setSpdata(String spdata) {
    this.spdata = spdata;
  }
}
