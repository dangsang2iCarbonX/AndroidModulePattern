package com.icarbonx.smartdevice.bodyfat;

import com.icarbonx.smartdevice.common.IBleParser;

/**
 * Body fat data class.
 */
public class BodyFatData {
    private boolean isSuccess;
    private boolean bodyFatTestingComplete;
    private int weight;
    private int bodyMass;
    private int bodyFatRate;
    private int subcutaneousFatRate;
    private int visceralAdiposeIndex;
    private int mussleRate;
    private int basalMetabolicRate;
    private int boneWeight;
    private int waterPercent;
    private int age;
    private int proteinRate;
    private boolean bodyFatDataEndSending;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private int seconds;

    public boolean isSuccess() {
        return isSuccess;
    }

    public BodyFatData setSuccess(boolean success) {
        isSuccess = success;
        return this;
    }

    public boolean isBodyFatTestingComplete() {
        return bodyFatTestingComplete;
    }

    public BodyFatData setBodyFatTestingComplete(boolean bodyFatTestingComplete) {
        this.bodyFatTestingComplete = bodyFatTestingComplete;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public BodyFatData setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public int getBodyMass() {
        return bodyMass;
    }

    public BodyFatData setBodyMass(int bodyMass) {
        this.bodyMass = bodyMass;
        return this;
    }

    public int getBodyFatRate() {
        return bodyFatRate;
    }

    public BodyFatData setBodyFatRate(int bodyFatRate) {
        this.bodyFatRate = bodyFatRate;
        return this;
    }

    public int getSubcutaneousFatRate() {
        return subcutaneousFatRate;
    }

    public BodyFatData setSubcutaneousFatRate(int subcutaneousFatRate) {
        this.subcutaneousFatRate = subcutaneousFatRate;
        return this;
    }

    public int getVisceralAdiposeIndex() {
        return visceralAdiposeIndex;
    }

    public BodyFatData setVisceralAdiposeIndex(int visceralAdiposeIndex) {
        this.visceralAdiposeIndex = visceralAdiposeIndex;
        return this;
    }

    public int getMussleRate() {
        return mussleRate;
    }

    public BodyFatData setMussleRate(int mussleRate) {
        this.mussleRate = mussleRate;
        return this;
    }

    public int getBasalMetabolicRate() {
        return basalMetabolicRate;
    }

    public BodyFatData setBasalMetabolicRate(int basalMetabolicRate) {
        this.basalMetabolicRate = basalMetabolicRate;
        return this;
    }

    public int getBoneWeight() {
        return boneWeight;
    }

    public BodyFatData setBoneWeight(int boneWeight) {
        this.boneWeight = boneWeight;
        return this;
    }

    public int getWaterPercent() {
        return waterPercent;
    }

    public BodyFatData setWaterPercent(int waterPercent) {
        this.waterPercent = waterPercent;
        return this;
    }

    public int getAge() {
        return age;
    }

    public BodyFatData setAge(int age) {
        this.age = age;
        return this;
    }

    public int getProteinRate() {
        return proteinRate;
    }

    public BodyFatData setProteinRate(int proteinRate) {
        this.proteinRate = proteinRate;
        return this;
    }

    public boolean isBodyFatDataEndSending() {
        return bodyFatDataEndSending;
    }

    public BodyFatData setBodyFatDataEndSending(boolean bodyFatDataEndSending) {
        this.bodyFatDataEndSending = bodyFatDataEndSending;
        return this;
    }

    public int getYear() {
        return year;
    }

    public BodyFatData setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public BodyFatData setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getDay() {
        return day;
    }

    public BodyFatData setDay(int day) {
        this.day = day;
        return this;
    }

    public int getHour() {
        return hour;
    }

    public BodyFatData setHour(int hour) {
        this.hour = hour;
        return this;
    }

    public int getMinutes() {
        return minutes;
    }

    public BodyFatData setMinutes(int minutes) {
        this.minutes = minutes;
        return this;
    }

    public int getSeconds() {
        return seconds;
    }

    public BodyFatData setSeconds(int seconds) {
        this.seconds = seconds;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("	BodyFat testing complete	\n");
        stringBuilder.append(""+isBodyFatTestingComplete()+"\n");
        stringBuilder.append("	weight	\n");
        stringBuilder.append(""+getWeight()+"\n");
        stringBuilder.append("	body mass	\n");
        stringBuilder.append(""+getBodyMass()+"\n");
        stringBuilder.append("	bodyfat rate	\n");
        stringBuilder.append(""+getBodyFatRate()+"\n");
        stringBuilder.append("	Subcutaneous fat rate\n");
        stringBuilder.append(""+getSubcutaneousFatRate()+"\n");
        stringBuilder.append("	Visceral adipose index 	\n");
        stringBuilder.append(""+getVisceralAdiposeIndex()+"\n");
        stringBuilder.append("	mussle rate	\n");
        stringBuilder.append(""+getMussleRate()+"\n");
        stringBuilder.append("	Basal metabolic rate\n");
        stringBuilder.append(""+getBasalMetabolicRate()+"\n");
        stringBuilder.append("	bone weight	\n");
        stringBuilder.append(""+getBoneWeight()+"\n");
        stringBuilder.append("	water percent	\n");
        stringBuilder.append(""+getWaterPercent()+"\n");
        stringBuilder.append("	age	\n");
        stringBuilder.append(""+getAge()+"\n");
        stringBuilder.append("	Protein rate 	\n");
        stringBuilder.append(""+getProteinRate()+"\n");
        stringBuilder.append("	Body fat data end sending 	\n");
        stringBuilder.append(""+isBodyFatTestingComplete()+"\n");
        stringBuilder.append(String.format("at %4d-%2d-%2d ",getYear()+2000,getMonth(),getDay()));
        stringBuilder.append(String.format("%2d-%2d-%2d \n",getHour(),getMinutes(),getSeconds()));

        return stringBuilder.toString();
    }
}
