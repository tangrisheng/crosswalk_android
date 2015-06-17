package com.sensoro.demo.js;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

public class LoadingActivity extends Activity {
	ImageView imageView;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading_activity);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {

		Runnable closeRunnable = new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent();
				intent.setClass(LoadingActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		};
		handler.postDelayed(closeRunnable, 3000);
		super.onResume();
	}

}
