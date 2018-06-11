package com.demo.icarbox.blereceiver_test;

import com.google.gson.Gson;
import com.icarbonx.smartdevice.account.LoginResp;
import com.icarbonx.smartdevice.account.UserAccountHttpService;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.println("233214q34");

        System.out.println(new Gson().toJson(new LoginResp.FamilyAccount().setAddress("sad")));
        String str = "{\n" +
                "    \"personId\": 1017575,\n" +
                "    \"fatherId\": -34661,\n" +
                "    \"motherId\": -34662,\n" +
                "    \"mateId\": -34663,\n" +
                "    \"mainProfile\": 1,\n" +
                "    \"createrAccountId\": 1016679,\n" +
                "    \"sex\": null,\n" +
                "    \"addedTime\": 1528705711000,\n" +
                "    \"img\": null,\n" +
                "    \"birthday\": null,\n" +
                "    \"deathday\": null,\n" +
                "    \"name\": \"\",\n" +
                "    \"status\": null,\n" +
                "    \"ancestryProvince\": null,\n" +
                "    \"ancestryCity\": null,\n" +
                "    \"ancestryDistrict\": null,\n" +
                "    \"ancestryFullAddress\": \" shenz\",\n" +
                "    \"liveProvince\": null,\n" +
                "    \"liveCity\": null,\n" +
                "    \"liveDistrict\": null,\n" +
                "    \"liveFullAddress\": null,\n" +
                "    \"modifiedTime\": 1528705711000,\n" +
                "    \"personalIdNo\": null,\n" +
                "    \"address\": null,\n" +
                "    \"relativePersonId\": 0,\n" +
                "    \"relation\": 1,\n" +
                "    \"phoneNumber\": null\n" +
                "  }";
        LoginResp.FamilyAccount familyAccount = new Gson().fromJson(str, LoginResp.FamilyAccount.class);

        System.out.println(familyAccount.toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://mainapi.icarbonx.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserAccountHttpService accountHttpInterface = retrofit.create(UserAccountHttpService.class);


//        accountHttpInterface.getVerifyCode("17607617511",1).enqueue(
//                new Callback<BaseResp>() {
//                    @Override
//                    public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
//                        System.out.println("2"+response.body().toString()+response.body().getErrorCode());
//                    }
//
//                    @Override
//                    public void onFailure(Call<BaseResp> call, Throwable t) {
//
//                    }
//                }
//        );
        System.out.println(new LoginResp().setData(new LoginResp.FamilyAccount()));

        Object object = String.class;
        accountHttpInterface.login("17607617511","5790").enqueue(
                new Callback<LoginResp>() {
                    @Override
                    public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                        System.out.println(""+response.body());
                    }

                    @Override
                    public void onFailure(Call<LoginResp> call, Throwable t) {
                        System.out.println(""+t.getMessage());
                    }
                }
        );


       // assertEquals(4, 2 + 2);
        while(true);
    }


}