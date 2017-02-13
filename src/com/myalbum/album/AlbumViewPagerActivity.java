package com.myalbum.album;

import java.io.File;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class AlbumViewPagerActivity extends Album {
	
	@Override
	public void onBackPressed() {    
	   finish();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_view_pager);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.album_view_pager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		ViewPager viewPager;
	    PagerAdapter adapter;
	    

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_album_view_pager, container, false);
			
	File file  = (File)	getActivity().getIntent().getSerializableExtra(ALBUM_FILE_OBJECT);
			
	 
	        // Locate the ViewPager in viewpager_main.xml
	        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
	        // Pass results to ViewPagerAdapter Class
	        DisplayMetrics metrics = new DisplayMetrics();
	      getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        adapter = new ViewPagerAdapter(getActivity(), file,metrics.widthPixels, metrics.heightPixels);        
	        
	        // Binds the Adapter to the ViewPager
	        viewPager.setAdapter(adapter);
	       
	        
	        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
	        	 private static final float MIN_SCALE = 0.5f;

	        	    public void transformPage(View view, float position) {
	        	        int pageWidth = view.getWidth();

	        	        if (position < -1) { // [-Infinity,-1)
	        	            // This page is way off-screen to the left.
	        	            view.setAlpha(0);

	        	        } else if (position <= 0) { // [-1,0]
	        	            // Use the default slide transition when moving to the left page
	        	            view.setAlpha(1);
	        	            view.setTranslationX(0);
	        	            view.setScaleX(1);
	        	            view.setScaleY(1);

	        	        } else if (position <= 1) { // (0,1]
	        	            // Fade the page out.
	        	            view.setAlpha(1 - position);

	        	            // Counteract the default slide transition
	        	            view.setTranslationX(pageWidth * -position);

	        	            // Scale the page down (between MIN_SCALE and 1)
	        	            float scaleFactor = MIN_SCALE
	        	                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
	        	            view.setScaleX(scaleFactor);
	        	            view.setScaleY(scaleFactor);

	        	        } else { // (1,+Infinity]
	        	            // This page is way off-screen to the right.
	        	            view.setAlpha(0);
	        	        }
	        	    }
	        });
			return rootView;
		}
	}

}
