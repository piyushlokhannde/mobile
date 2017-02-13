package com.myalbum.album;
import android.graphics.Bitmap;

import java.io.File;
import java.lang.ref.SoftReference;


public class AlbumElement {
	
	private String absolutePath;
	private int position;	
	private File imageFile;	
	private SoftReference<Bitmap>  imageBitMapsoftRef;
	
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public File getImageFile() {
		return imageFile;
	}
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}
	public Bitmap getImageBitMap() {		
		Bitmap imageBitMap = null;
		if(this.imageBitMapsoftRef != null ) {
			imageBitMap = imageBitMapsoftRef.get();
		}
		return imageBitMap;
	}

	public void setImageBitMap(Bitmap imageBitMap) {
		if (this.imageBitMapsoftRef == null) {
			this.imageBitMapsoftRef = new SoftReference<Bitmap>(imageBitMap);
		}

	}
}
