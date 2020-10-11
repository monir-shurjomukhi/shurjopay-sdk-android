package com.sm.shurjopaysdk.model;

public class SPDataModel {
  private String spdata;

  public SPDataModel(String spdata) {
    this.spdata = spdata;
  }

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
