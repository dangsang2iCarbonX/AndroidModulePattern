package com.demo.icarbox.blereceiver_test;

import com.google.gson.Gson;
import com.icarbonx.smartdevice.UrlConstants;
import com.icarbonx.smartdevice.account.Apis;
import com.icarbonx.smartdevice.account.ApisOld;
import com.icarbonx.smartdevice.account.BaseResponse;
import com.icarbonx.smartdevice.account.FamilyAccount;
import com.icarbonx.smartdevice.account.UserAccountHttpService;
import com.icarbonx.smartdevice.http.AbstractObserver;

import org.junit.Test;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws InterruptedException {
        System.out.println("233214q34");

        System.out.println(new Gson().toJson(new FamilyAccount().setAddress("sad")));
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
        FamilyAccount familyAccount = new Gson().fromJson(str, FamilyAccount.class);

//        System.out.println(familyAccount.toString());

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//
//        UserAccountHttpService accountHttpInterface = retrofit.create(UserAccountHttpService.class);

//        accountHttpInterface.getVerifyCode("17607617511",1).enqueue(
//                new Callback<BaseResponse>() {
//                    @Override
//                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                        System.out.println("2"+response.body().toString()+response.body().getErrorCode());
//                    }
//
//                    @Override
//                    public void onFailure(Call<BaseResponse> call, Throwable t) {
//
//                    }
//                }
//        );
//        System.out.println(new LoginResponse().setData(new FamilyAccount()));

//        accountHttpInterface.login("17607617511","3003").enqueue(
//                new Callback<LoginResponse>() {
//                    @Override
//                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                        System.out.println(""+response.body());
//                    }
//
//                    @Override
//                    public void onFailure(Call<LoginResponse> call, Throwable t) {
//                        System.out.println(""+t.getMessage());
//                    }
//                }
//        );

//        accountHttpInterface.updateUserPhoto("2");

        String[] names = {"123", "3213"};

//        ApisOld.getInstance().getUserAccountService()
//                .getVerifyCode("18923889519",1)
//                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new AbstractObserver<BaseResponse>() {
//                    @Override
//                    public void onSuccess(BaseResponse baseResponse) {
//                        System.out.println(baseResponse.toJson());
//                        if(baseResponse.getErrorCode()!=0){
//                            System.out.println(baseResponse.getErrMsg());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//
//                    }
//                });
        Apis<UserAccountHttpService> apis = new Apis<UserAccountHttpService>()
                .setService(UserAccountHttpService.class, UrlConstants.MEUM_URL);

        apis.getService()
                .verifyPhoneNumber("18923889519")
                .subscribeOn(Schedulers.single())
                .subscribe(new AbstractObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        System.out.println(baseResponse.toJson());
                    }
                });


//                    @Override public void onNext(MovieSubject movieSubject)
// { mMovieAdapter.setMovies(movieSubject.subjects);
// mMovieAdapter.notifyDataSetChanged(); } });


//        Observable.fromArray(names)
//                .subscribe(nw Action<String>())


        Flowable.just("Hello world")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println(s);
                    }
                });


        // assertEquals(4, 2 + 2);
        while (true) ;
    }


}