package com.robinhood.ticker.sample;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public abstract class BaseActivity extends AppCompatActivity {
    protected static final Random RANDOM = new Random(System.currentTimeMillis());

    private Handler handler = new Handler();
    private boolean resumed;

    @Override
    protected void onResume() {
        super.onResume();
        resumed = true;
        handler.postDelayed(createRunnable(), 1000);
    }

    private Runnable createRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                onUpdate();
                if (resumed) {
                    handler.postDelayed(createRunnable(), RANDOM.nextInt(1750) + 5000);
                }
            }
        };
    }

    protected abstract void onUpdate();

    @Override
    protected void onPause() {
        resumed = false;
        super.onPause();
    }

    protected String getRandomNumber(int digits) {
        final int digitsInPowerOf10 = (int) Math.pow(10, digits);
        return Integer.toString(RANDOM.nextInt(digitsInPowerOf10) +
                digitsInPowerOf10 * (RANDOM.nextInt(8) + 1));
    }
}
