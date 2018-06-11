package com.icarbonx.smartdevice.account;

import com.google.gson.Gson;

public class LoginResp extends BaseResp<LoginResp.FamilyAccount> {

    /**
     * Detail information for user.
     *
     * @author lavi
     */
    public static final class FamilyAccount extends Account {
        /**
         "personId": 1017575,
         "fatherId": -34661,
         "motherId": -34662,
         "mateId": -34663,
         "mainProfile": 1,
         "createrAccountId": 1016679,
         "sex": null,
         "addedTime": 1528705711000,
         "img": null,
         "birthday": null,
         "deathday": null,
         "name": "",
         "status": null,
         "ancestryProvince": null,
         "ancestryCity": null,
         "ancestryDistrict": null,
         "ancestryFullAddress": null,
         "liveProvince": null,
         "liveCity": null,
         "liveDistrict": null,
         "liveFullAddress": null,
         "modifiedTime": 1528705711000,
         "personalIdNo": null,
         "address": null,
         "relativePersonId": 0,
         "relation": 1,
         "phoneNumber": null
         */
        //person id
        long personId;
        //person's father id
        long fatherId;
        //person's mother id
        long motherId;
        //person's mate id
        long mateId;
        //main profile id
        int mainProfile;
        //creater account id
        long createrAccountId;
        //gender
        String sex;
        //added time
        long addedTime;
        //img url
        String img;
        //birthday
        String birthday;
        //deathday
        String deathday;
        //name
        String name;
        //status of living or dying
        String status;
        //ancestry province
        String ancestryProvince;
        //ancestry city
        String ancestryCity;
        //ancestry district
        String ancestryDistrict;
        //ancestry full address
        String ancestryFullAddress;
        //living province
        String liveProvince;
        //living city
        String liveCity;
        //living district
        String liveDistrict;
        //living full address
        String liveFullAddress;
        //last modified time
        long modifiedTime;
        //persional id No.
        long personalIdNo;
        //person living address
        String address;
        //relative person id
        long relativePersonId;
        //relation
        int relation;
        //phone number
        String phoneNumber;

        @Override
        public String toJson() {
            return new Gson().toJson(this, FamilyAccount.class);
        }

        public long getPersonId() {
            return personId;
        }

        public FamilyAccount setPersonId(long personId) {
            this.personId = personId;
            return this;
        }

        public long getFatherId() {
            return fatherId;
        }

        public FamilyAccount setFatherId(long fatherId) {
            this.fatherId = fatherId;
            return this;
        }

        public long getMotherId() {
            return motherId;
        }

        public FamilyAccount setMotherId(long motherId) {
            this.motherId = motherId;
            return this;
        }

        public long getMateId() {
            return mateId;
        }

        public FamilyAccount setMateId(long mateId) {
            this.mateId = mateId;
            return this;
        }

        public int getMainProfile() {
            return mainProfile;
        }

        public FamilyAccount setMainProfile(int mainProfile) {
            this.mainProfile = mainProfile;
            return this;
        }

        public long getCreaterAccountId() {
            return createrAccountId;
        }

        public FamilyAccount setCreaterAccountId(long createrAccountId) {
            this.createrAccountId = createrAccountId;
            return this;
        }

        public String getSex() {
            return sex;
        }

        public FamilyAccount setSex(String sex) {
            this.sex = sex;
            return this;
        }

        public long getAddedTime() {
            return addedTime;
        }

        public FamilyAccount setAddedTime(long addedTime) {
            this.addedTime = addedTime;
            return this;
        }

        public String getImg() {
            return img;
        }

        public FamilyAccount setImg(String img) {
            this.img = img;
            return this;
        }

        public String getBirthday() {
            return birthday;
        }

        public FamilyAccount setBirthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public String getDeathday() {
            return deathday;
        }

        public FamilyAccount setDeathday(String deathday) {
            this.deathday = deathday;
            return this;
        }

        public String getName() {
            return name;
        }

        public FamilyAccount setName(String name) {
            this.name = name;
            return this;
        }

        public String getStatus() {
            return status;
        }

        public FamilyAccount setStatus(String status) {
            this.status = status;
            return this;
        }

        public String getAncestryProvince() {
            return ancestryProvince;
        }

        public FamilyAccount setAncestryProvince(String ancestryProvince) {
            this.ancestryProvince = ancestryProvince;
            return this;
        }

        public String getAncestryCity() {
            return ancestryCity;
        }

        public FamilyAccount setAncestryCity(String ancestryCity) {
            this.ancestryCity = ancestryCity;
            return this;
        }

        public String getAncestryDistrict() {
            return ancestryDistrict;
        }

        public FamilyAccount setAncestryDistrict(String ancestryDistrict) {
            this.ancestryDistrict = ancestryDistrict;
            return this;
        }

        public String getAncestryFullAddress() {
            return ancestryFullAddress;
        }

        public FamilyAccount setAncestryFullAddress(String ancestryFullAddress) {
            this.ancestryFullAddress = ancestryFullAddress;
            return this;
        }

        public String getLiveProvince() {
            return liveProvince;
        }

        public FamilyAccount setLiveProvince(String liveProvince) {
            this.liveProvince = liveProvince;
            return this;
        }

        public String getLiveCity() {
            return liveCity;
        }

        public FamilyAccount setLiveCity(String liveCity) {
            this.liveCity = liveCity;
            return this;
        }

        public String getLiveDistrict() {
            return liveDistrict;
        }

        public FamilyAccount setLiveDistrict(String liveDistrict) {
            this.liveDistrict = liveDistrict;
            return this;
        }

        public String getLiveFullAddress() {
            return liveFullAddress;
        }

        public FamilyAccount setLiveFullAddress(String liveFullAddress) {
            this.liveFullAddress = liveFullAddress;
            return this;
        }

        public long getModifiedTime() {
            return modifiedTime;
        }

        public FamilyAccount setModifiedTime(long modifiedTime) {
            this.modifiedTime = modifiedTime;
            return this;
        }

        public long getPersonalIdNo() {
            return personalIdNo;
        }

        public FamilyAccount setPersonalIdNo(long personalIdNo) {
            this.personalIdNo = personalIdNo;
            return this;
        }

        public String getAddress() {
            return address;
        }

        public FamilyAccount setAddress(String address) {
            this.address = address;
            return this;
        }

        public long getRelativePersonId() {
            return relativePersonId;
        }

        public FamilyAccount setRelativePersonId(long relativePersonId) {
            this.relativePersonId = relativePersonId;
            return this;
        }

        public int getRelation() {
            return relation;
        }

        public FamilyAccount setRelation(int relation) {
            this.relation = relation;
            return this;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public FamilyAccount setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        @Override
        public String toString() {
            return toJson();
        }
    }
}
