package com.sensoro.demo.js;

import java.util.ArrayList;

import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.beacon.kit.SensoroBeaconManager;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

public class SensoroApp extends Application {
	SensoroBeaconManager sensoroBeaconManager;

	ArrayList<BeaconListener> beaconListeners;

	String NOTIFICATION_1_UUID = "";
	int NOTIFICATION_1_MAJOR = 0x2712;
	int NOTIFICATION_1_MINOR = 0xE535;

	String NOTIFICATION_2_UUID = "";
	int NOTIFICATION_2_MAJOR = 0xC652;
	int NOTIFICATION_2_MINOR = 0x7A6F;

	public static final String EXTRA_URL = "EXTRA_URL";

	@Override
	public void onCreate() {
		initBeacon();
		beaconListeners = new ArrayList<SensoroApp.BeaconListener>();
		super.onCreate();
	}

	private void initBeacon() {
		sensoroBeaconManager = SensoroBeaconManager.getInstance(this);
		sensoroBeaconManager.setBeaconManagerListener(new MyBeaconManagerListener());
		try {
			sensoroBeaconManager.startService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyBeaconManagerListener implements BeaconManagerListener {

		@Override
		public void onGoneBeacon(Beacon arg0) {

		}

		@Override
		public void onNewBeacon(Beacon arg0) {
			if (beaconListeners != null && beaconListeners.size() > 0) {
				for (BeaconListener beaconListener : beaconListeners) {
					beaconListener.onNewBeacon(arg0);
				}
			}
			// Notification
			triggerNotification(arg0);
		}

		@Override
		public void onUpdateBeacon(ArrayList<Beacon> arg0) {

		}

	}

	public void addBeaconListener(BeaconListener beaconListener) {
		if (beaconListeners != null) {
			boolean exist = false;
			for (int i = 0; i < beaconListeners.size(); i++) {
				if (beaconListener == beaconListeners.get(i)) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				beaconListeners.add(beaconListener);
			}
		}

	}

	public void removeBeaconListener(BeaconListener beaconListener) {
		if (beaconListeners != null) {
			for (int i = 0; i < beaconListeners.size(); i++) {
				if (beaconListener == beaconListeners.get(i)) {
					beaconListeners.remove(i);
				}
			}
		}
	}

	private void triggerNotification(Beacon beacon) {
		String uuid = beacon.getProximityUUID();
		Integer major = beacon.getMajor();
		Integer minor = beacon.getMinor();
		if (uuid == null || major == null || minor == null) {
			return;
		}

		if (major == NOTIFICATION_1_MAJOR && minor == NOTIFICATION_1_MINOR) {
			// notification 1
			showNotification("Notification 1", "click to H5", null);
		} else if (major == NOTIFICATION_2_MAJOR && minor == NOTIFICATION_2_MINOR) {
			// notification 2
			showNotification("Notification 2", "click to yunlai.", "http://w.liveapp.cn/auto/index/4085?channel=1&weixin.qq.com=");
		}
	}

	private void showNotification(String title, String message, String url) {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(this);
		builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentInfo(message);
		builder.setAutoCancel(true);
		builder.setContentTitle(title);
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(EXTRA_URL, url);
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);
		nm.notify(R.string.app_name, builder.build());
	}

	interface BeaconListener {
		void onNewBeacon(Beacon beacon);

		void onGoneBeacon(Beacon beacon);

		void onUpdateBeacon(ArrayList<Beacon> beacons);
	}
}
