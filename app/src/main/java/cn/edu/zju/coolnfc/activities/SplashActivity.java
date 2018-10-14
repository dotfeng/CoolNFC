package cn.edu.zju.coolnfc.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import cn.edu.zju.coolnfc.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class SplashActivity extends Activity {
	public static final String TAG = "ActivitySplash";
	private CountDownTimer contador;
	private ImageView blurImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		findViews();
		initDate();
		contador = new CountDownTimer(2000, 1000) {
			@Override
			public void onFinish() {
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onTick(long millisUntilFinished) {
			}
		}.start();
	}

    private void findViews() {
        blurImageView = findViewById(R.id.sea);
    }


    private void initDate() {
	    Glide.with(this).load(R.id.sea)
                .bitmapTransform(new BlurTransformation(this,25))
                .into(blurImageView);
    }

    @Override
	public void onBackPressed() {
		contador.cancel();
		super.onBackPressed();
	}

	@Override
	protected void onStop() {
		contador.cancel();
		super.onStop();
	}
}
