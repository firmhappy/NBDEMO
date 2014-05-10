package com.example.nbdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MyOverlay extends ItemizedOverlay<OverlayItem> {
	
	static Button view;
	private PopupOverlay pop;
	public static GeoPoint p;
	
	public MyOverlay(Drawable arg0, MapView arg1) {
		super(arg0, arg1);
		pop=new PopupOverlay(arg1, new PopupClickListener(){

			@Override
			public void onClickedPopup(int arg0) {
				System.out.println("POPIndex:"+arg0);
				
			}});
	}

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		if(pop!=null){
			pop.hidePop();
			arg1.removeView(view);
		}
		
		return false;
	}

	@Override
	protected boolean onTap(int arg0) {
		System.out.println("NBIndex:"+arg0);
		System.out.println(p.getLatitudeE6()+"\n"+p.getLongitudeE6());
		pop.hidePop();
		Bitmap bit=BMapUtil.getBitmapFromView(view);
		pop.showPopup(bit, p, 32);
		return true;
	}
	
	
	
	
	
}
