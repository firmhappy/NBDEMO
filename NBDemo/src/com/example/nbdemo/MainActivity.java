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
		option.setLocationMode(LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ�ȣ�Ĭ��ֵgcj02
		option.setScanSpan(5000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ
		option.setNeedDeviceDirect(true);// ���صĶ�λ��������ֻ���ͷ�ķ���
		mLocationClient.setLocOption(option);
		System.out.println("!!!!!!!");
		mLocationClient.start();
		System.out.println(mLocationClient.isStarted());
		if (mLocationClient != null && mLocationClient.isStarted()) {
			System.out.println("@@@@@@@");
		}
	}

	private void initMap() {
		// ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		System.out.println("find bt!!!");
		// ��ʼ����λ����
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(this);
		// �����������õ����ſؼ�
		MapController mMapController = mMapView.getController();
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));
		// �ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		mMapController.setCenter(point);// ���õ�ͼ���ĵ�
		mMapController.setZoom(16);// ���õ�ͼzoom����
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
