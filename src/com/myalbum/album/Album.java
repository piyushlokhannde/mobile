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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class Album extends ActionBarActivity {
	
	public static String APP_PREFERNCE = "albumPreference";
	public static String SELECTED_FOLDER = "selectedFolder"; 
	public static String SAVE_FOLDER_ROOT_PREF = "saveFolderRoot";
	public static String APP_FOLDER = "Album";
	public static String ACTIVITY_CA_MODE_S = "activityCAmode";
	public static List<String> SUPPORTED_IMG = new ArrayList<String>(4);
	final public static String ALBUM_FILE_OBJECT = "albumFileObject";
	public enum ACTIVITY_CA_MODE { CREATE, EDIT};
	public static String CREATE_ACTIVITY_INPUT = "createActivityInput";
	static {
		SUPPORTED_IMG.add(".jpg");
		SUPPORTED_IMG.add(".gif");
		SUPPORTED_IMG.add(".png");
		SUPPORTED_IMG.add(".bmp");
	}
	
	public String getAppFolderPath() {		
		SharedPreferences settings = getSharedPreferences(APP_PREFERNCE, Context.MODE_PRIVATE); 
		return settings.getString(SAVE_FOLDER_ROOT_PREF, Environment.getExternalStorageDirectory()+"/"+APP_FOLDER);		
	}
	
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	public static Bitmap decodeSampledBitmapFromResource(String  res, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;	   
	    BitmapFactory.decodeFile(res, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(res, options);
	}
	
	static List<AlbumElement> getAlbumObjecListFromFile(File fileIn) {
		
		List<AlbumElement> photoList = null;
		
		String  albumName = null;
    	if (fileIn.isDirectory()) {
			albumName = fileIn.getName();
		}
		
		File[] fileList = fileIn.listFiles();
		
		if(fileList != null && fileList.length >0 && fileList[0].exists() &&  fileList[0].isFile()) {
			
			StringBuilder fileContents  = new StringBuilder();
			try {
				BufferedReader reader  =  new BufferedReader(new FileReader(fileList[0]));
				String readLine = null;
				
				try {
					while ((readLine = reader.readLine()) != null) {
						fileContents.append(readLine);
					}
					
					JSONArray albumPhotos  = new JSONArray(fileContents.toString());
				int	totoalPhotos = albumPhotos.length();
					photoList = new ArrayList<AlbumElement>(totoalPhotos);
					for(int i =0;i<= albumPhotos.length();i++) {
						JSONObject jsonOBject = albumPhotos.getJSONObject(i);
						AlbumElement element  = new AlbumElement();
						element.setAbsolutePath((String)jsonOBject.get("path"));
						element.setPosition(jsonOBject.getInt("position"));
						photoList.add(element);
					}					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		if(photoList == null) {
			photoList = new ArrayList<AlbumElement>(1);
		}
		
		return photoList;
	}
	
	public  static void scaleContents(View rootView, int containerWidth, int containerheight )
	{
	// Compute the scaling ratio
	float xScale = (float)containerWidth / rootView.getWidth();
	float yScale = (float)containerheight/ rootView.getHeight();
	float scale = Math.min(xScale, yScale);
	System.out.println(scale);
	// Scale our contents
	//scaleViewAndChildren(rootView,scale);
	}
	
	
	public static void scaleViewAndChildren(View root, float scale)
	{
	// Retrieve the view's layout information
	ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
	// Scale the view itself
	if (layoutParams.width != ViewGroup.LayoutParams.FILL_PARENT &&
	layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT)
	{
	layoutParams.width *= scale;
	}
	if (layoutParams.height != ViewGroup.LayoutParams.FILL_PARENT &&
	layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT)
	{
	layoutParams.height *= scale;
	}
	// If this view has margins, scale those too
	if (layoutParams instanceof ViewGroup.MarginLayoutParams)
	{
	ViewGroup.MarginLayoutParams marginParams =
	(ViewGroup.MarginLayoutParams)layoutParams;
	marginParams.leftMargin *= scale;
	marginParams.rightMargin *= scale;
	marginParams.topMargin *= scale;
	marginParams.bottomMargin *= scale;
	}
	// Set the layout information back into the view
	root.setLayoutParams(layoutParams);
	
	// Scale the view's padding
	root.setPadding(
	(int)(root.getPaddingLeft() * scale),
	(int)(root.getPaddingTop() * scale),
	(int)(root.getPaddingRight() * scale),
	(int)(root.getPaddingBottom() * scale));
	// If the root view is a TextView, scale the size of its text
	if (root instanceof TextView)
	{
	TextView textView = (TextView)root;
	textView.setTextSize(textView.getTextSize() * scale);
	}
	// If the root view is a ViewGroup, scale all of its children recursively
	if (root instanceof ViewGroup)
	{
	ViewGroup groupView = (ViewGroup)root;
	for (int cnt = 0; cnt < groupView.getChildCount(); ++cnt)
	scaleViewAndChildren(groupView.getChildAt(cnt), scale);
	}
	}

}

