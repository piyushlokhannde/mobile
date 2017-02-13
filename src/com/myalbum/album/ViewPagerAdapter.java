package com.myalbum.album;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPagerAdapter extends  PagerAdapter {
	
	

	Context context;
   int totoalPhotos;
    LayoutInflater inflater;
    List<AlbumElement> photoList;
    int screenWidth;
    int screenHeight;
    
    public ViewPagerAdapter(Context context, File fileIn, int screenWidthIn, int screenHeightIn) {
    	this.context = context;
    	this.screenWidth = screenWidthIn;
    	this.screenHeight = screenHeightIn;
    	photoList = Album.getAlbumObjecListFromFile(fileIn);
    	totoalPhotos = photoList.size();
    
    }

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return totoalPhotos;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		 return view == ((RelativeLayout) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup  container, int position) {
		// Declare Variables
        
        ImageView imgflag;
 
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        
        View itemView = inflater.inflate(R.layout.viewpager_item, (ViewGroup) container,
                false);
         // Locate the ImageView in viewpager_item.xml
        imgflag = (ImageView) itemView.findViewById(R.id.photo);
       	AlbumElement element  = photoList.get(position);
		Bitmap bitmap  = Album.decodeSampledBitmapFromResource(element.getAbsolutePath(),this.screenWidth , this.screenHeight);	
		imgflag.setImageBitmap(bitmap);   
		
		TextView textView  = (TextView)itemView.findViewById(R.id.photoSeq);
		textView.setText((position+1)+" of " +this.totoalPhotos);
 
        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);
 
        return itemView;
	}
	
	 @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        // Remove viewpager_item.xml from ViewPager
	        ((ViewPager) container).removeView((RelativeLayout) object);
	 
	    }

}
