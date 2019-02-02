package com.certuit.pacheco.eliezer.examenclima.model;

public class City {
  private String name;
  private String countryCode;

  public City(){
    this.name = "Mexicali";
    this.countryCode = "MX";
  }

  public City(String name, String countryCode){
    this.name = name;
    this.countryCode = countryCode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  @Override
  public String toString() {
    return "City{" +
        "name='" + name + '\'' +
        ", countryCode='" + countryCode + '\'' +
        '}';
  }
}
