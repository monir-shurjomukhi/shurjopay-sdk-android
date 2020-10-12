package com.sm.shurjopaysdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SPDataModel {
  @SerializedName("jsdata")
  @Expose
  private String jsdata;

  public SPDataModel(String jsdata) {
    this.jsdata = jsdata;
  }

  @Override
  public String toString() {
    return "SPDataModel{" +
        "spdata='" + jsdata + '\'' +
        '}';
  }

  public String getJsdata() {
    return jsdata;
  }

  public void setJsdata(String jsdata) {
    this.jsdata = jsdata;
  }
}
