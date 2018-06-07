package com.icarbonx.smartdevice.bodyfat;


import com.icarbonx.smartdevice.common.IBleParser;

/**
 * BodyFat helper parsing data and generate a {@link BodyFatData} object.
 */
public class BodyFatDataHelper implements IBleParser {
    /**
     * {@link BodyFatData} to store parsed data.
     */
    BodyFatData mBodyFatData;
    IDataParser mIDataParser;

    public BodyFatData getmBodyFatData() {
        return mBodyFatData;
    }

    public BodyFatDataHelper setmBodyFatData(BodyFatData mBodyFatData) {
        this.mBodyFatData = mBodyFatData;
        return this;
    }

    public IDataParser getmIDataParser() {
        return mIDataParser;
    }

    public BodyFatDataHelper setmIDataParser(IDataParser mIDataParser) {
        this.mIDataParser = mIDataParser;
        return this;
    }

    /**
     * Data parse interface.
     */
    public interface IDataParser {
        /**
         * Changable weight while testing.
         *
         * @param weight User weight tested.
         */
        void onChangingWeight(int weight);

        /**
         * Stable weight tested.
         *
         * @param weight User weight.
         */
        void onStableWeight(int weight);

        /**
         * Body fat rate testing.
         */
        void onBodyFatTesting();

        /**
         * Body impedance tested.
         *
         * @param impedance The body impedance.
         */
        void onBodyFatImpedance(int impedance);

        /**
         * Test body fat failed cause of impedance.
         */
        void onBodyFatTestFailed();

        /**
         * Body fat tested for weight.
         *
         * @param weight User weight.
         */
        void onBodyWeight(int weight);

        /**
         * Body mass tested.
         *
         * @param mass Body mass.
         */
        void onBodyMass(int mass);

        /**
         * Body fat rate tested.
         *
         * @param rate fat rate.
         */
        void onBodyFatRate(int rate);

        /**
         * Subcutaneous fat rate.
         *
         * @param rate The rate.
         */
        void onSubcutaneousFatRate(int rate);

        /**
         * Visceral adipose.
         *
         * @param index The index.
         */
        void onVisceralAdiposeIndex(int index);

        /**
         * Body mussle rate tested.
         *
         * @param rate mussle rate.
         */
        void onMussleRate(int rate);

        /**
         * Body basal metabolic rate tested.
         *
         * @param rate Basal metabolic rate.
         */
        void onBasalMetabolicRate(int rate);

        /**
         * Body bone weight tested.
         *
         * @param weight Bone weight.
         */
        void onBoneWeight(int weight);

        /**
         * Body water percent tested.
         *
         * @param percent Water percent of body.
         */
        void onWaterPercent(int percent);

        /**
         * Body age tested.
         *
         * @param age Body age.
         */
        void onAge(int age);

        /**
         * Body protein rate tested.
         *
         * @param rate Protein rate.
         */
        void onProteinRate(int rate);

        /**
         * Body fat data ends sending.
         *
         * @param bodyFatData {@link BodyFatData} object.
         */
        void onBodyFatDataEndSending(BodyFatData bodyFatData);

        /**
         * Year,month,day stored in device.
         *
         * @param year  Year plus 2000 is actual year.
         * @param month Month
         * @param day   Day
         */
        void onYearMonthDay(int year, int month, int day);

        /**
         * Hour,minutes,seconds stored in device.
         *
         * @param hour    Hour
         * @param minutes Minutes
         * @param seconds Seconds
         */
        void onHourMinutesSeconds(int hour, int minutes, int seconds);
    }

    public BodyFatDataHelper() {
        this.mBodyFatData = new BodyFatData();
    }

    @Override
    public void parse(byte[] data) {
        //变化的体重
        if (data[data.length - 2] == (byte) 0xce) {
            int weight = (data[2] << 8 | data[3]) & 0xffff;
            mIDataParser.onChangingWeight(weight);
        } //稳定的体重
        else if (data[data.length - 2] == (byte) 0xca) {
            int weight = (data[2] << 8 | data[3]) & 0xffff;
            mIDataParser.onStableWeight(weight);
        }//体脂测量中
        else if ((data[2] == (byte) 0xfd) && data[3] == 0 && (data[data.length - 2] == (byte) 0xcb)) {
            mBodyFatData.setBodyFatTestingComplete(false);
            mIDataParser.onBodyFatTesting();
        }//测量的阻抗不符合条件，测量出错
        else if ((data[2] == (byte) 0xfd) && (data[3] == (byte) 0xff) && (data[data.length - 2] == (byte) 0xcb)) {
            mBodyFatData.setSuccess(false);
            mBodyFatData.setBodyFatTestingComplete(true);
            mIDataParser.onBodyFatTestFailed();
        }//体脂完成获得阻抗
        else if ((data[2] == (byte) 0xfd) && data[3] == 1 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setSuccess(true);
            mBodyFatData.setBodyFatTestingComplete(true);
            mIDataParser.onBodyFatImpedance(value);
        }//体重
        else if ((data[2] == (byte) 0xfe) && data[3] == 0 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setWeight(value);
            mIDataParser.onBodyWeight(value);

        }//身体质量
        else if ((data[2] == (byte) 0xfe) && data[3] == 1 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setBodyMass(value);
            mIDataParser.onBodyMass(value);
        }//体脂率
        else if ((data[2] == (byte) 0xfe) && data[3] == 2 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setBodyFatRate(value);
            mIDataParser.onBodyFatRate(value);
        }//皮下脂肪率
        else if ((data[2] == (byte) 0xfe) && data[3] == 3 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setSubcutaneousFatRate(value);
            mIDataParser.onSubcutaneousFatRate(value);
        }//内脏脂肪指数
        else if ((data[2] == (byte) 0xfe) && data[3] == 4 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setVisceralAdiposeIndex(value);
            mIDataParser.onVisceralAdiposeIndex(value);
        }//肌肉率
        else if ((data[2] == (byte) 0xfe) && data[3] == 5 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setMussleRate(value);
            mIDataParser.onMussleRate(value);
        }//基础代谢率
        else if ((data[2] == (byte) 0xfe) && data[3] == 6 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setBasalMetabolicRate(value);
            mIDataParser.onBasalMetabolicRate(value);
        }//骨重量
        else if ((data[2] == (byte) 0xfe) && data[3] == 7 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setBoneWeight(value);
            mIDataParser.onBoneWeight(value);
        }//水含量
        else if ((data[2] == (byte) 0xfe) && data[3] == 8 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setWaterPercent(value);
            mIDataParser.onWaterPercent(value);
        }//身体年龄
        else if ((data[2] == (byte) 0xfe) && data[3] == 9 && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setAge(value);
            mIDataParser.onAge(value);
        }//蛋白率
        else if ((data[2] == (byte) 0xfe) && data[3] == 0x0a && (data[data.length - 2] == (byte) 0xcb)) {
            int value = (data[4] << 8 | data[5]) & 0xffff;
            mBodyFatData.setProteinRate(value);
            mIDataParser.onProteinRate(value);
        }//体脂数据发送结束
        else if ((data[2] == (byte) 0xfe) && (data[3] == (byte) 0xfc) && (data[data.length - 2] == (byte) 0xcb)) {
            mIDataParser.onBodyFatDataEndSending(mBodyFatData);
        }

    }

    public static abstract class  AbstractParser implements IDataParser{

        @Override
        public void onChangingWeight(int weight) {

        }

        @Override
        public void onStableWeight(int weight) {

        }

        @Override
        public void onBodyFatTesting() {

        }

        @Override
        public void onBodyFatImpedance(int impedance) {

        }

        @Override
        public void onBodyFatTestFailed() {

        }

        @Override
        public void onBodyWeight(int weight) {

        }

        @Override
        public void onBodyMass(int mass) {

        }

        @Override
        public void onBodyFatRate(int rate) {

        }

        @Override
        public void onSubcutaneousFatRate(int rate) {

        }

        @Override
        public void onVisceralAdiposeIndex(int index) {

        }

        @Override
        public void onMussleRate(int rate) {

        }

        @Override
        public void onBasalMetabolicRate(int rate) {

        }

        @Override
        public void onBoneWeight(int weight) {

        }

        @Override
        public void onWaterPercent(int percent) {

        }

        @Override
        public void onAge(int age) {

        }

        @Override
        public void onProteinRate(int rate) {

        }

        @Override
        public void onBodyFatDataEndSending(BodyFatData bodyFatData) {

        }

        @Override
        public void onYearMonthDay(int year, int month, int day) {

        }

        @Override
        public void onHourMinutesSeconds(int hour, int minutes, int seconds) {

        }
    }
}
