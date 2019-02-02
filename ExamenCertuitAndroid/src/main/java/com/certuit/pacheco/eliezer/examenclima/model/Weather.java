package com.certuit.pacheco.eliezer.examenclima.model;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class Weather {
  private double temperature;
  private double maxTemperature;
  private double minTemperature;
  private City city;
  private GregorianCalendar date;
  private int conditionId;

  public Weather(){}

  public Weather(@NotNull JsonObject json){
    init(json);
  }

  public Weather(@NotNull JsonObject json, City city){
    init(json);
    this.city = city;
  }

  private void init(@NotNull JsonObject json){
    temperature = (json.get("main").getAsJsonObject().get("temp").getAsDouble()) - 273.15; //kelvin to celsius
    minTemperature = (json.get("main").getAsJsonObject().get("temp_min").getAsDouble()) - 273.15; //kelvin to celsius
    maxTemperature = (json.get("main").getAsJsonObject().get("temp_max").getAsDouble()) - 273.15; //kelvin to celsius
//    if(json.has("dt_txt")){
//      date = formatGregorianCalendarFromString(json.get("dt_txt").getAsString());
//    }else{}
    //set time in UNIX time (seconds)
    date = new GregorianCalendar();
    date.setTimeInMillis(json.get("dt").getAsLong()*1000L);

    conditionId = json.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsInt();
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public double getMaxTemperature() {
    return maxTemperature;
  }

  public void setMaxTemperature(double maxTemperature) {
    this.maxTemperature = maxTemperature;
  }

  public double getMinTemperature() {
    return minTemperature;
  }

  public void setMinTemperature(double minTemperature) {
    this.minTemperature = minTemperature;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public GregorianCalendar getDate() {
    return date;
  }

  public void setDate(GregorianCalendar date) {
    this.date = date;
  }

  public int getConditionId() {
    return conditionId;
  }

  public void setConditionId(int conditionId) {
    this.conditionId = conditionId;
  }

  private GregorianCalendar formatGregorianCalendarFromString(@NotNull String dateTimeString){
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date date = fmt.parse(dateTimeString);
      GregorianCalendar gregorianCalendar = new GregorianCalendar();
      gregorianCalendar.setTime(date);
      return gregorianCalendar;
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getDateString(){
    SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    fmt.setCalendar(date);
    return fmt.format(date.getTime());
  }

  public String getTimeString(){
    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
    fmt.setCalendar(date);
    return fmt.format(date.getTime());
  }

  @Override
  public String toString() {
    return "Weather{" +
        "temperature=" + temperature +
        ", maxTemperature=" + maxTemperature +
        ", minTemperature=" + minTemperature +
        ", city=" + city +
        ", date=" + date.getTime().toString() +
        ", conditionId=" + conditionId +
        '}';
  }
}
