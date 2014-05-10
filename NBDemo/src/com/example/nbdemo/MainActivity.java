package com.example.nbdemo;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements BDLocationListener {

	private BMapManager mBMapMan = null;
	private MapView mMapView = null;
	private LocationClient mLocationClient = null;
	private LocationData locData;
	private MyLocationOverlay myLocationOverlay;
	private Button bt,publish;
	private MyOverlay mOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(null);
		setContentView(R.layout.activity_main);
		bt=(Button) LayoutInflater.from(this).inflate(R.layout.demoview, null).findViewById(R.id.button1);
		publish=(Button) this.findViewById(R.id.publishbt);
		publish.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				System.out.println("ADD");
				GeoPoint p=new GeoPoint((int)(locData.latitude*1e6),(int)(locData.longitude*1e6));
				Drawable mark = MainActivity.this.getResources()
						.getDrawable(R.drawable.icon_gcoding);
				OverlayItem item = new OverlayItem(p, "!!!", "???");
				mOverlay.addItem(item);
				mMapView.refresh();
				
			}});
		initMap();
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		System.out.println("!!!!!!!");
		mLocationClient.start();
		System.out.println(mLocationClient.isStarted());
		if (mLocationClient != null && mLocationClient.isStarted()) {
			System.out.println("@@@@@@@");
		}
	}

	private void initMap() {
		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		System.out.println("find bt!!!");
		// 初始化定位功能
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(this);
		// 设置启用内置的缩放控件
		MapController mMapController = mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);// 设置地图中心点
		mMapController.setZoom(16);// 设置地图zoom级别
		mMapController.enableClick(true);
		
		myLocationOverlay=new MyLocationOverlay(mMapView);
		locData=new LocationData();
		locData.latitude = 39.945;  
		locData.longitude = 116.404;  
		locData.direction = 2.0f;  
		myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		mMapView.refresh();
		mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6),  
				(int)(locData.longitude* 1e6)));
		
		initMyOverlay();
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		locData.latitude = location.getLatitude();
		locData.longitude = location.getLongitude();
		locData.direction = location.getDerect();
		locData.accuracy = 0;
		myLocationOverlay.setData(locData);
		mMapView.refresh();
		mMapView.getController().animateTo(
				new GeoPoint((int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));

	}

	private void initMyOverlay() {
		GeoPoint p = new GeoPoint((int) (37.872973 * 1e6),
				(int) (112.603397 * 1e6));
		Drawable mark = this.getResources()
				.getDrawable(R.drawable.icon_gcoding);
		OverlayItem item = new OverlayItem(p, "!!!", "???");
		mOverlay = new MyOverlay(mark, mMapView);
		MyOverlay.view = bt;
		mOverlay.p=p;
		mMapView.getOverlays().add(mOverlay);
		mMapView.refresh();
		mOverlay.addItem(item);
		mMapView.refresh();
	}

	@Override
	public void onReceivePoi(BDLocation location) {
		// TODO Auto-generated method stub
	}

}
