package com.myalbum.album;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myalbum.album.AlbumDialogFragment.NoticeDialogListener;
import com.myalbum.album.CreateAlbumActivity.PlaceholderFragment.LoadFolderImages;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewAlbumActivity extends Album {
	
	private boolean layoutset;
	
	@Override
	public void onBackPressed() {    
	   finish();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	
		final	ViewAlbumActivity.PlaceholderFragment  fragment  = (ViewAlbumActivity.PlaceholderFragment)getSupportFragmentManager().getFragments().get(0);
			
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int height = metrics.heightPixels;
			int width = metrics.widthPixels;
			scaleContents(fragment.rootView, width, height);
			layoutset = true;
		
		
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_view_album);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_album, menu);
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
	public static class PlaceholderFragment extends Fragment  implements AlbumActionDialog.NoticeDialogListener {
		
		private File[] dataObjects;
		private File[] allAlbum;
		public  View rootView;
		
		public View containerParam;
		String[] albumNameList;
		ArrayAdapter<String> albumNameListAdapter;
		
		
		private void createAlbumNameList(File[] albumlist) {
			albumNameList = new String[albumlist.length+1];
			if(albumlist != null) {
				for(int i=0; i<albumlist.length;i++ ) {
					File file = albumlist[i];
					albumNameList[i] =(String)(file.getName());
				}
				albumNameList[albumlist.length] = "All";
			}
			
		}
		
		 @Override
			public void onActivityResult(int requestCode, int resultCode,
					Intent data) {
				 if (requestCode == 1) {
				        if(resultCode == RESULT_OK){
				        	
				        	refreshPage();
				        }
				        if (resultCode == RESULT_CANCELED) {
				            //Write your code if there's no result
				        }
				    }
			}
			
		
		
		public String getAppFolderPath() {	
			
			String appFolder  = null;
			SharedPreferences settings = getActivity().getSharedPreferences(APP_PREFERNCE, Context.MODE_PRIVATE); 
			appFolder = settings.getString(SAVE_FOLDER_ROOT_PREF, "");
			
			if("".equals(appFolder)) {
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					appFolder = Environment.getExternalStorageDirectory().getPath()+"/"+APP_FOLDER;
				} else {
					File[] files  = Environment.getExternalStorageDirectory().getParentFile().listFiles();
					for(File file: files) {
						if(!file.getPath().equals(Environment.getExternalStorageDirectory().getPath())) {
							appFolder = file.getPath()+ "/"+APP_FOLDER;
						}
					}
					
				}	
				
				SharedPreferences.Editor spe = settings.edit();
				spe.putString(SAVE_FOLDER_ROOT_PREF, appFolder);
				spe.commit(); 
			}
				
			
			return 	appFolder;
		}

		public PlaceholderFragment() {
		}
		
		private void refreshPage() {
			File appFolder  = new File(getAppFolderPath());			
			allAlbum =  dataObjects = appFolder.listFiles();
			mAdapter.notifyDataSetChanged();
			AutoCompleteTextView textView  = (AutoCompleteTextView)rootView.findViewById(R.id.autoCompleteTextView1);
			textView.setText("");
			createAlbumNameList(appFolder.listFiles());			
			albumNameListAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
	                 android.R.layout.simple_dropdown_item_1line, albumNameList);		        
	         textView.setAdapter(albumNameListAdapter);
			
		}
		
		
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_view_album,
					container, false);
			containerParam = container;
			
			//scaleContents(rootView, container);		
			/*This code is is written to set the height of list by calculating the height of other layout*/
		   final  ViewTreeObserver observer = rootView .getViewTreeObserver();		    
		            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

		        @Override
		        public void onGlobalLayout() {
		            // TODO Auto-generated method stub
		            int headerLayoutHeight= rootView.getHeight();
		            int autoSearchH =	 rootView.findViewById(R.id.autoCompleteTextView1).getLayoutParams().height;
		    		int btnH =	 rootView.findViewById(R.id.createNewAlbumBtn).getLayoutParams().height; 
		    		
		    		float pixeValue  =TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.activity_vertical_margin), getResources().getDisplayMetrics());
		    		
		    	Double padding  =	Math.ceil(pixeValue*2);
		    	rootView.findViewById(R.id.viewalbumlistlayout).getLayoutParams().height = (headerLayoutHeight - (autoSearchH+btnH+padding.intValue()));	
		    		System.out.println("");
		    		rootView .getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    }
		});
		
		
			File appFolder  = new File(getAppFolderPath());
			if (!appFolder.exists()) {
				appFolder.mkdir();
			}
			allAlbum =  dataObjects = appFolder.listFiles();					
			ListView  listViw  = (ListView)rootView.findViewById(R.id.viewalbumlist);
			listViw.setAdapter(mAdapter);
			setAutoSearchTextBoxView();	
			
			
	  Button btn  = (Button)rootView.findViewById(R.id.createNewAlbumBtn);
	  btn.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent myIntent = new Intent( getActivity(), CreateAlbumActivity.class);
			myIntent.putExtra(ACTIVITY_CA_MODE_S, ACTIVITY_CA_MODE.CREATE);
	        startActivityForResult(myIntent, 1);
			
		}
	});
			
			
			return rootView;
		}
		
		
		private void setAutoSearchTextBoxView() {
		final	AutoCompleteTextView textView  = (AutoCompleteTextView)rootView.findViewById(R.id.autoCompleteTextView1);
			Object[] params  =  new Object[2];
			params[0] = textView;
			params[1] = allAlbum;
			textView.setThreshold(2);
			
			textView.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.background));
			
			textView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					TextView textView  = (TextView)arg1;
					if(textView.getText().toString().equals("All")) {
						if(allAlbum != null && allAlbum.length >0) {
							dataObjects = allAlbum; 
							mAdapter.notifyDataSetChanged();
						} else {
							Toast.makeText(getActivity(), "No Albums in the Directory", Toast.LENGTH_LONG).show();
						}
						
					} else {
						
						
						for(File file : allAlbum) {
							if(file.getName().equals(textView.getText().toString())) {
								dataObjects = new File[]{file};								
								mAdapter.notifyDataSetChanged();
								break;
							}
							
						}
					}
					
					
					
					
				}
			});
			
			
			
			
			new LoadAutoSearchData().execute(params);
			
		}
		
		
		
		private BaseAdapter mAdapter = new BaseAdapter() {
			
			
			
			
			@Override
			public int getCount() {
				return dataObjects.length;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}


			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View retView = convertView;		
				if(retView == null) {
					
					retView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.create_view_list_item, null);
					
				} 
				
				RelativeLayout relativeLaout =(RelativeLayout) retView.findViewById(R.id.viewItemlayout);
			    relativeLaout.setVisibility(View.INVISIBLE);
				final ImageView imageVIew  = (ImageView)retView.findViewById(R.id.viewImgItem);				
				
				final File currentAlbum  = dataObjects[position];
				Object[] params = new Object[3];
				params[0] = dataObjects[position];
				params[1] = imageVIew;
				params[2] = relativeLaout;
				new LoadAlbum().execute(params);
				final File fileobject  = (File)dataObjects[position];
				imageVIew.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						ScaleAnimation scale =  new ScaleAnimation(1f, 0.5f, 1f, 0.5f, 50, 50);		
						scale.setAnimationListener(new AnimationListener() {
							
							@Override
							public void onAnimationStart(Animation animation) {
								
								
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								Intent intent = new Intent(getActivity(), AlbumViewPagerActivity.class);							   
							    intent.putExtra(ALBUM_FILE_OBJECT, fileobject);
							    startActivity(intent);							
							}
						});
	                	imageVIew.startAnimation(scale);
	                	
	                	
						
					}
				});
				
				final Button actionBtn  = (Button)retView.findViewById(R.id.actionBtn);	
				actionBtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						AlbumActionDialog diaglog  = new AlbumActionDialog(currentAlbum.getName(),currentAlbum);
						diaglog.setCancelable(false);
							
						diaglog.show(getActivity().getSupportFragmentManager(), "Take Action");
					}
				});
				
					
				return retView;
			}
		
			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
		
		class LoadAutoSearchData extends AsyncTask<Object, Void, Void> {
			@Override
			protected Void doInBackground(Object... params) {
				AutoCompleteTextView textView   = (AutoCompleteTextView)params[0];
				File[] albumlist  = (File[])params[1];
				createAlbumNameList(albumlist);
				
				if(albumNameList.length >0) {
					Object[] handlerObject = new Object[2];
					handlerObject[0] = textView;
					handlerObject[1] = albumNameList;
					Message msgObj = autoSearchHandler.obtainMessage();
					msgObj.obj  = handlerObject;
					autoSearchHandler.sendMessage(msgObj);					
				}
				
				
				return null;
				}
			}
		
		private final Handler autoSearchHandler = new Handler() {
			@Override
		 public void handleMessage(Message msg) {
				Object[] handlerObject = (Object[])	msg.obj;				
				AutoCompleteTextView textView  = (AutoCompleteTextView)handlerObject[0];
				albumNameList =(String[]) handlerObject[1];	
				

				albumNameListAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
		                 android.R.layout.simple_dropdown_item_1line, albumNameList);		        
		         textView.setAdapter(albumNameListAdapter);
		        
		   
				
			}
		};
		
		class LoadAlbum extends AsyncTask<Object, Void, Void> {

			@Override
			protected Void doInBackground(Object... params) {
				
			File file  = (File)params[0];
			ImageView imageView  = (ImageView)params[1];
			RelativeLayout itemLayout = (RelativeLayout)params[2];
			Bitmap bitmap = null;
			Integer totoalPhotos  = 0;
			
			String albumName  = "";
			if (file.isDirectory()) {
				albumName = file.getName();
			}
			
			File[] fileList = file.listFiles();
			
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
						totoalPhotos = albumPhotos.length();
						for(int i =0;i<= albumPhotos.length();i++) {
							JSONObject jsonOBject = albumPhotos.getJSONObject(i);
							String  imagePath = (String)jsonOBject.get("path");
							File imageFile  = new File(imagePath);
							if(imageFile.exists()) {
								float density = rootView.getContext().getResources().getDisplayMetrics().density;
								int height =(int) (imageView.getLayoutParams().height*density);
								int width =(int) (imageView.getLayoutParams().width*density);	
								bitmap  = decodeSampledBitmapFromResource(imageFile.getAbsolutePath(),height , width);	
								break;
							}
						}
						
						Object[] handlerObject = new Object[5];
						handlerObject[0] = itemLayout;
						handlerObject[1]  = imageView;
						handlerObject[2] = albumName;
						handlerObject[3] = totoalPhotos;
						handlerObject[4] = bitmap;
						Message msgObj = handler.obtainMessage();
						msgObj.obj  = handlerObject;
						handler.sendMessage(msgObj);
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
				
				
				
			} else {
				Object[] handlerObject = new Object[5];
				handlerObject[0] = itemLayout;
				handlerObject[1]  = imageView;
				handlerObject[2] = albumName;
				handlerObject[3] = totoalPhotos;
				handlerObject[4] = bitmap;
				Message msgObj = handler.obtainMessage();
				msgObj.obj  = handlerObject;
				handler.sendMessage(msgObj);
				
			}
			
				
				
				return null;
			}

			
			
		}
		
		private final Handler handler = new Handler() {
			@Override
		 public void handleMessage(Message msg) {
				
				Object[] handlerObject  = (Object[])msg.obj;
				RelativeLayout itemLayout =(RelativeLayout) handlerObject[0];
				ImageView  imageView = (ImageView)handlerObject[1];
				String albumName = (String)handlerObject[2];
				Integer totoalPhotos  = (Integer)handlerObject[3];
				Bitmap bitmap =(Bitmap) handlerObject[4];
				
				imageView.setImageBitmap(bitmap);
			 TextView albumNameTextView  = (TextView)itemLayout.findViewById(R.id.textView1);
			 albumNameTextView.setText(albumName);
			 
			 TextView totalPhotosextView  = (TextView)itemLayout.findViewById(R.id.textView2);
			 totalPhotosextView.setText(totoalPhotos + " Photos");
			 itemLayout.setVisibility(View.VISIBLE);
			 
			}
		};

		@Override
		public void onDialogPositiveClick(DialogFragment dialog) {
			TextView textView = (TextView)dialog.getDialog().findViewById(R.id.textViewAlbumName);
			File file = (File)textView.getTag();
			Toast.makeText(getActivity(), file.getAbsolutePath(), Toast.LENGTH_LONG).show();
			
			Intent myIntent = new Intent( getActivity(), CreateAlbumActivity.class);
			myIntent.putExtra(ACTIVITY_CA_MODE_S, ACTIVITY_CA_MODE.EDIT);
			myIntent.putExtra(ALBUM_FILE_OBJECT, file);
	        startActivityForResult(myIntent, 1);
		}

		@Override
		public void onDialogNegativeClick(DialogFragment dialog) {
			TextView textView = (TextView)dialog.getDialog().findViewById(R.id.textViewAlbumName);
			Object[] params = new Object[1];
			params[0] = textView;
			new DeleteAlbumTask().execute(params);
			
			
			
		}
		
		class DeleteAlbumTask extends AsyncTask<Object, Void, Void> {
			ProgressDialog barProgressDialog;
			@Override
			protected void onPreExecute() {
				barProgressDialog = new  ProgressDialog(getActivity());
				barProgressDialog.setTitle("Deleting Album ...");
				barProgressDialog.setMessage("Deleting in progress ...");
				barProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				barProgressDialog.setProgress(0);
				barProgressDialog.show();
			}
   
			@Override
			protected Void doInBackground(Object... params) {
			TextView textView  = (TextView)params[0];
			File file = (File)textView.getTag();
			//Toast.makeText(getActivity(), file.getAbsolutePath(), Toast.LENGTH_LONG).show();
			
			
			
			File[] listFile  = file.listFiles();
			for(File inFile: listFile) {
				inFile.delete();
			}
			file.delete();
			//Update current data set
			File[] afterDelData = new File[dataObjects.length-1];
			int j =0;
			for(int i=0; i<dataObjects.length;i++) {
				if(!dataObjects[i].equals(file)) {
					afterDelData[j] = dataObjects[i];
					j++;
				}				
			}		
			dataObjects = afterDelData;
			Message msgObj = deleteHandler.obtainMessage();
			msgObj.obj = mAdapter;
			deleteHandler.sendMessage(msgObj);
			
			
			//Update all data set
			File[] afterDelData1 = new File[allAlbum.length-1];
			int m =0;
			for(int i=0; i<allAlbum.length;i++) {
				if(!allAlbum[i].equals(file)) {
					afterDelData1[m] = allAlbum[i];
					m++;
				}				
			}	
			
			allAlbum = afterDelData1;
			
			
			
			String[] newalbumNameList = new String[albumNameList.length-1];
			int k =0;			
			String albumName  = textView.getText().toString();
			for(int i=0; i<albumNameList.length;i++) {
				if(!albumNameList[i].equals(albumName)) {
					newalbumNameList[k] = albumNameList[i];
					k++;
				}				
			}	
			albumNameList = newalbumNameList;
			
			Object[] handlerObject = new Object[2];
			AutoCompleteTextView autoSearchTextView  = (AutoCompleteTextView)rootView.findViewById(R.id.autoCompleteTextView1);
		
			handlerObject[0] = autoSearchTextView;
			handlerObject[1] = albumNameList;
			Message msgObj2 = autoSearchHandler.obtainMessage();
			msgObj2.obj  = handlerObject;
			autoSearchHandler.sendMessage(msgObj2);	
			
	
			
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				barProgressDialog.dismiss();
				
			}
			
		}
		
		private final Handler deleteHandler  = new Handler() {
			
			@Override
			 public void handleMessage(Message msg) {
				BaseAdapter baseAdapter  = (BaseAdapter)msg.obj;
				baseAdapter.notifyDataSetInvalidated();
			}
			
		};

	}
	
	
	

}


class AlbumActionDialog extends DialogFragment {
	
	private String albumName;
	
	private File albumFile;
	
	public AlbumActionDialog (String albumNameIn, File albumFileIn) {
		super();
		albumName = albumNameIn;
		albumFile = albumFileIn;
	}
	
	NoticeDialogListener mListener;
	 public interface NoticeDialogListener {
	        public void onDialogPositiveClick(DialogFragment dialog);
	        public void onDialogNegativeClick(DialogFragment dialog);
	    }
	 
	 
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			ActionBarActivity aba = (ActionBarActivity) activity;
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (NoticeDialogListener) aba.getSupportFragmentManager()
					.getFragments().get(0);
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view  = inflater.inflate(R.layout.view_album_action_layout, null);
		if(this.albumName != null) {
			TextView textView = (TextView)view.findViewById(R.id.textViewAlbumName);
			textView.setText(this.albumName);
			textView.setTag(this.albumFile);
		}		
		builder.setView(view);
		
		
		
		builder.setPositiveButton(R.string.view_album_dialog_positive_btn_text, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			
				mListener.onDialogPositiveClick(AlbumActionDialog.this);
			}
			
		});
		builder.setNegativeButton(R.string.view_album_dialog_neagative_btn_text, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				mListener.onDialogNegativeClick(AlbumActionDialog.this);
				
				
			}
			
			
			
		});
		
		builder.setNeutralButton(R.string.view_album_dialog_neautral_btn_text, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}} );
		final Dialog dialog  = builder.create();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface arg0) {						
				Button positiveButton = ((AlertDialog) dialog)
                       .getButton(AlertDialog.BUTTON_POSITIVE);
				positiveButton.setBackgroundResource(R.drawable.createactivityfolderbutton);
				
				Button negativeButton = ((AlertDialog) dialog)
                       .getButton(AlertDialog.BUTTON_NEGATIVE);
				negativeButton.setBackgroundResource(R.drawable.createactivityfolderbutton);
				
				Button neautralButton = ((AlertDialog) dialog)
	                       .getButton(AlertDialog.BUTTON_NEUTRAL);
				neautralButton.setBackgroundResource(R.drawable.createactivityfolderbutton);
				
			}
			
		});
	
		
		return dialog;
	}
	
}

