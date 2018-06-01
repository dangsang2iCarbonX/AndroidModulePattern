package com.demo.icarbox.blereceiver;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * BL advertised data upload intent service.
 * @author lavi
 */
public class BleHttpIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BleHttpIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }


}
