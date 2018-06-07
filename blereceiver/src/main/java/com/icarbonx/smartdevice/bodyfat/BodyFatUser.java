package com.icarbonx.smartdevice.bodyfat;

/**
 * Body fat user class contains user information.
 */
public class BodyFatUser {
    //user ID no bigger than 8
    int id;
    //用户性别,0 女，1 男
    int gender;
    //用户年龄
    int age;
    //用户身高
    int height;
    //用户体重
    int weight;
    //用户阻抗
    int impedance;

    /**
     * Get use id.
     */
    public int getId() {
        return id;
    }

    /**
     * Set user id
     *
     * @param id user id
     * @return {@link BodyFatUser}
     */
    public BodyFatUser setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * Get use gender.
     */
    public int getGender() {
        return gender;
    }

    /**
     * Set user gender
     *
     * @param gender user gender,0 for female, 1 for male.
     * @return {@link BodyFatUser}
     */
    public BodyFatUser setGender(int gender) {
        this.gender = gender;
        return this;
    }

    /**
     * Get use age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Add user age
     *
     * @param age user age
     * @return {@link BodyFatUser}
     */
    public BodyFatUser setAge(int age) {
        this.age = age;
        return this;
    }

    /**
     * Get use height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set user height
     *
     * @param height user height
     * @return {@link BodyFatUser}
     */
    public BodyFatUser setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * Get use weight.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Set user weight
     *
     * @param weight user weight
     * @return {@link BodyFatUser}
     */
    public BodyFatUser setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    /**
     * Get use impedance.
     */
    public int getImpedance() {
        return impedance;
    }

    /**
     * Set user impedance
     *
     * @param impedance user impedance
     * @return {@link BodyFatUser}
     */
    public BodyFatUser setImpedance(int impedance) {
        this.impedance = impedance;
        return this;
    }
}
