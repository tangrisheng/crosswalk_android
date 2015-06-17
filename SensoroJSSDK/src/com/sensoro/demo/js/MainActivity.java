package com.sensoro.demo.js;

import java.util.ArrayList;

import org.xwalk.core.XWalkView;

import com.google.gson.Gson;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.demo.js.SensoroApp.BeaconListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity implements BeaconListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	XWalkView xWalkView;
	Button callButton;
	Gson gson;
	SensoroApp app;

	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initURL();
		initCtrl();
	}

	private void initCtrl() {
		app = (SensoroApp) getApplication();
		app.addBeaconListener(this);
		xWalkView = (XWalkView) findViewById(R.id.xwalkview);
		// xWalkView.addJavascriptInterface(new JSToAndroid(this),
		// "JSToAndroid");
		xWalkView.load(url, null);

		gson = new Gson();

		// callButton = (Button) findViewById(R.id.btn);
		// callButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// xWalkView.load("javascript:myFunction('111111')", null);
		// }
		// });
	}

	private void initURL() {
		Intent intent = getIntent();
		if (intent == null) {
			url = "file:///android_asset/H5.html";
		} else {
			url = intent.getStringExtra(SensoroApp.EXTRA_URL);
		}

		if (url == null) {
			url = "file:///android_asset/H5.html";
		}
	}

	@Override
	protected void onDestroy() {
		app.removeBeaconListener(this);
		super.onDestroy();
	}

	@Override
	public void onNewBeacon(final Beacon beacon) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("javascript:");
				stringBuffer.append("onNewBeacon");
				stringBuffer.append("(");
				JsBeacon jsBeacon = new JsBeacon(beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
				String beaconString = gson.toJson(jsBeacon);
				stringBuffer.append(beaconString);
				stringBuffer.append(")");

				xWalkView.load(stringBuffer.toString(), null);

			}
		});

	}

	@Override
	public void onGoneBeacon(Beacon beacon) {

	}

	@Override
	public void onUpdateBeacon(ArrayList<Beacon> beacons) {

	}
}
