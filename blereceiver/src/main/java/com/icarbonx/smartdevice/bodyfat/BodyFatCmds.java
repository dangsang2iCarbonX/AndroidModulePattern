package com.icarbonx.smartdevice.bodyfat;

import java.util.Arrays;

/**
 * Body fat command class
 */
public class BodyFatCmds {

    /**
     * Generate adding user information command from {@link BodyFatUser}
     *
     * @param bodyFatUser {@link BodyFatUser} object
     * @return Byte array
     */
    public byte[] getAddUserInfo(BodyFatUser bodyFatUser) {
        byte[] data = new byte[4 + 8 + 8];
        data[0] = (byte) 0xac;
        data[1] = 2;
        data[2] = (byte) 0xfd;
        data[3] = 0;//添加用户信息标识
        data[4] = (byte) bodyFatUser.getId();
        data[5] = (byte) bodyFatUser.getGender();
        data[6] = (byte) bodyFatUser.getAge();
        data[7] = (byte) bodyFatUser.getHeight();
        data[8] = (byte) (bodyFatUser.getWeight() >> 8);
        data[9] = (byte) bodyFatUser.getWeight();
        data[10] = (byte) ((byte) bodyFatUser.getImpedance() >> 8);
        data[11] = (byte) bodyFatUser.getImpedance();
        Arrays.fill(data, 12, data.length, (byte) 0);

        return data;
    }
    /**
     * Get user information from {@link BodyFatUser}
     *
     * @return Byte array
     */
    public byte[] getAddUserEnd() {
        byte[] data = new byte[8];
        data[0] = (byte) 0xac;
        data[1] = 2;
        data[2] = (byte) 0xfd;
        data[3] = 2;//添加用户结束标识
        data[4] = 0;
        data[5] = 0;
        data[6] = (byte) 0xcf;
        data[7] = (byte) 0xce;

        return data;
    }
    /**
     * Generate user id  setting command from {@link BodyFatUser}
     *
     * @param bodyFatUser {@link BodyFatUser} object
     * @return Byte array
     */
    public byte[] getUserId(BodyFatUser bodyFatUser) {
        byte[] data = new byte[ 8];
        Arrays.fill(data,(byte) 0);

        data[0] = (byte) 0xac;
        data[1] = 2;
        data[2] = (byte) 0xfa;
        data[3] = (byte) bodyFatUser.getId();
        data[7] = sumCheck(data, 02, 5);

        return data;
    }
    /**
     * Generate user information besides  {@link BodyFatUser#getId()}
     *
     * @param bodyFatUser {@link BodyFatUser} object
     * @return Byte array
     */
    public byte[] getUserOther(BodyFatUser bodyFatUser) {
        byte[] data = new byte[ 8];
        data[0] = (byte) 0xac;
        data[1] = 2;
        data[2] = (byte) 0xfb;
        data[3] = (byte) bodyFatUser.getGender();
        data[4] = (byte) bodyFatUser.getAge();
        data[5] = (byte) bodyFatUser.getHeight();
        data[6] = 0;
        data[7] = sumCheck(data, 02, 5);

        return data;
    }

    /**
     * Data checking by sum, and use last byte.
     */
    private byte sumCheck(byte[] data, int offset, int length) {
        int v = 0;
        for (int i = offset; i < length + offset; i++) {
            v = v + (data[i] & 0xff);
        }

        return (byte) v;
    }
}
