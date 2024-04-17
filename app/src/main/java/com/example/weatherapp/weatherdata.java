package com.example.weatherapp;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherdata {
    private String mTemp,mIcon,mCity,mweatherType;
    private int mCondition;
    public static weatherdata fromJson(JSONObject jsonObject){
        try {
            weatherdata weatherD= new weatherdata();
            weatherD.mCity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mweatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.mIcon=updateweatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int) Math.rint(tempResult);
            weatherD.mTemp=Integer.toString(roundedValue);
            return weatherD;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String updateweatherIcon(int condition){
        if(condition>=0 && condition<=300)
            return "thunderstorm";
        else if(condition>=300 && condition<=500)
            return "lightrain";
        else if(condition>=500 && condition<=600)
            return "shower";
        else if(condition>=600 && condition<=700)
            return "snow";
        else if(condition>=701 && condition<=771)
            return "fog";
        else if(condition>=772 && condition<=800)
            return "overcast";
        else if(condition == 800)
            return "sunny";
        else if(condition>=801 && condition<=804)
            return "cloudy";
        else if(condition>=900 && condition<=902)
            return "thunderstorm";
        else if(condition == 903)
            return "snow";
        else if(condition == 904)
            return "sunny";
        else if(condition>=905 && condition<=1000)
            return "thunderstorm";
        else
            return "confuse";
    }

    public String getmTemp(){
        return mTemp + "°C";
    }
    public String getmIcon(){
        return mIcon;
    }
    public String getmCity(){
        return mCity;
    }
    public String getMweatherType(){
        return mweatherType;
    }
}