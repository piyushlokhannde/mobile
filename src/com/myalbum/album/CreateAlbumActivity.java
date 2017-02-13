package com.myalbum.album;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("NewApi") public class CreateAlbumActivity extends Album {
	
	
	@Override
	public void onBackPressed() {  		
		Intent resultIntent = new Intent();		
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	   
	}

  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_album);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		
	}
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_album, menu);
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
	
	public static Bitmap getBitMapFromImageView (ImageView  mImageView) {
		Bitmap bitmap;
		if (mImageView.getDrawable() instanceof BitmapDrawable) {
		    bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
		} else {
		    Drawable d = mImageView.getDrawable();
		    bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);		    
		}
		
		return bitmap;
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("ValidFragment") public static class PlaceholderFragment extends Fragment implements AlbumDialogFragment.NoticeDialogListener {
		private View rootView;
		private ImageView currentSelection;
		private HorizontialListView listview;
		private int currentPosition = -1;
		private  List<AlbumElement> dataObjects = new ArrayList<AlbumElement>();
		
		private String albumName;
		
		
	
		
		 @Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			 if (requestCode == 1) {
			        if(resultCode == RESULT_OK){
			            String result=data.getStringExtra("result");
			           File selectedFolder = (File) data.getSerializableExtra("selectedFolder");
			           new LoadFolderImages().execute(selectedFolder);
			        }
			        if (resultCode == RESULT_CANCELED) {
			            //Write your code if there's no result
			        }
			    }
		}
		
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			 rootView = inflater.inflate(R.layout.fragment_create_album,
					container, false);
		//    addImageToAlbum();
			setAlbumDestinationList();
			setButtonClickListeners();
		
	
			
			return rootView;
		}
		
		
		private void setFolderImages(String folderPath)  {
			
			String[] projection = {MediaStore.Images.Thumbnails._ID};
			
		}
		 
		private void setButtonClickListeners() {
			
			
			
			
			
		final Button selectFolderBtn  = (Button)	rootView.findViewById(R.id.selectfolderbutton);	
		selectFolderBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(rootView.getContext(), "Select Folder Button Cicked", Toast.LENGTH_SHORT).show();	
					Intent i = new Intent(getActivity(), SelectFolderAcitivity.class);
					startActivityForResult(i, 1);
					//new LoadFolderImages().execute(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
					
					
						
				}
			});	
		
		final Button doneBtn  = (Button)	rootView.findViewById(R.id.button0);	
		doneBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(rootView.getContext(), "Done Button Click", Toast.LENGTH_SHORT).show();	
				Intent resultIntent = new Intent();		
				getActivity().setResult(Activity.RESULT_OK, resultIntent);
				getActivity().finish();
				
			}
			
			
		});
			
			
		 final	Button saveButton =(Button)	rootView.findViewById(R.id.button1);
		 
		 saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(), "Save Button Click", Toast.LENGTH_SHORT).show();				
				
			AlbumDialogFragment diaglog  = new AlbumDialogFragment(albumName);
			diaglog.setCancelable(false);
				
			diaglog.show(getActivity().getSupportFragmentManager(), "Save Button");
			}
		});
		 
		 final	Button moveLeft =(Button)	rootView.findViewById(R.id.button2);
		 moveLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(), "Move Left Button Click", Toast.LENGTH_SHORT).show();
				if(currentSelection == null) {
					return;
				}
				
				int viewPosition =listview.getPositionForView(currentSelection);
				if((viewPosition) > 0) {				
				View prevView =	listview.getChildAt(viewPosition-1);
				final ImageView image  =  (ImageView)prevView.findViewById(R.id.image);
				ScaleAnimation scaleAnim  =  new ScaleAnimation(1,0,1,0, 50, 50);					
				scaleAnim.setDuration(500);
				image.startAnimation(scaleAnim);
				scaleAnim.setAnimationListener(new Animation.AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						Bitmap prevImageBitmap  = getBitMapFromImageView(image);
						image.setImageBitmap(getBitMapFromImageView(currentSelection));
						image.setBackgroundResource(R.drawable.createalbumimagebackgroundafterselect);
						image.invalidate();
						currentSelection.setImageBitmap(prevImageBitmap);
						currentSelection.setBackgroundResource(R.drawable.createalbumimagebackground);
						currentSelection.invalidate();
						currentSelection = image;
						AlbumElement currentImage = dataObjects.get(currentPosition-1);
						dataObjects.set(currentPosition-1, dataObjects.get(currentPosition));
						dataObjects.set(currentPosition, currentImage);
						currentPosition = currentPosition-1;	
					}
				});
				
				
						
				
								
						
				
				}
				
				
			}
		});
		 
		 final	Button moveRight =(Button)	rootView.findViewById(R.id.button3);
		 moveRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(), "Move Right Button Click", Toast.LENGTH_SHORT).show();
				if(currentSelection == null) {
					return;
				}
				int viewPosition =listview.getPositionForView(currentSelection);
				int[] draState  = listview.getDrawableState();				
				if(viewPosition < draState.length) {
					View nextView  =	listview.getChildAt(viewPosition+1);
					final ImageView image  =  (ImageView)nextView.findViewById(R.id.image);ScaleAnimation scaleAnim  =  new ScaleAnimation(1,0,1,0, 50, 50);					
					scaleAnim.setDuration(500);
					image.startAnimation(scaleAnim);
					scaleAnim.setAnimationListener(new Animation.AnimationListener() {

						@Override
						public void onAnimationEnd(Animation animation) {
							Bitmap nextImageBitmap = getBitMapFromImageView(image);
							image.setImageBitmap(getBitMapFromImageView(currentSelection));
							image.setBackgroundResource(R.drawable.createalbumimagebackgroundafterselect);
							image.invalidate();					
							currentSelection.setImageBitmap(nextImageBitmap);
							currentSelection.setBackgroundResource(R.drawable.createalbumimagebackground);
							currentSelection.invalidate();
							currentSelection = image;
							AlbumElement currentImage = dataObjects.get(currentPosition+1);
							dataObjects.set(currentPosition+1, dataObjects.get(currentPosition));
							dataObjects.set(currentPosition, currentImage);
							currentPosition = currentPosition+1;
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onAnimationStart(Animation animation) {
							
							
						}
						
					});
					
					
					
				}
				
			}
		});
		 final	Button deleteBtn =(Button)	rootView.findViewById(R.id.button4);
		 deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(rootView.getContext(), "Delete Button Click", Toast.LENGTH_SHORT).show();
				if(currentSelection  == null)  {
					return;
				}
				
				Animation anim =  new AlphaAnimation(1, 0);
				anim.setDuration(500);
				currentSelection.startAnimation(anim);
				
				anim.setAnimationListener(new Animation.AnimationListener() {

					@Override
					public void onAnimationEnd(Animation animation) {
						dataObjects.remove(currentPosition);				
						mAdapter.notifyDataSetChanged();
						currentPosition = -1;
						currentSelection= null;
						
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
				});
				
				
			}
			
		});
		 
		 final Button canBtn = (Button)	rootView.findViewById(R.id.button5);	
		 canBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Toast.makeText(rootView.getContext(), "Cancel Button Click", Toast.LENGTH_SHORT).show();	
					Intent resultIntent = new Intent();		
					getActivity().setResult(Activity.RESULT_CANCELED, resultIntent);
					getActivity().finish();
					
				}
				
				
			});
			
		}
		
		private void setAlbumDestinationList() {
			
			ACTIVITY_CA_MODE activityMode  =(ACTIVITY_CA_MODE)getActivity().getIntent().getSerializableExtra(Album.ACTIVITY_CA_MODE_S);
			if(ACTIVITY_CA_MODE.EDIT.equals(activityMode)) {
				File file = (File)getActivity().getIntent().getSerializableExtra(Album.ALBUM_FILE_OBJECT);
				dataObjects =	Album.getAlbumObjecListFromFile(file);
				albumName = file.getName();
			}
			
			listview = (HorizontialListView)rootView.findViewById(R.id.listview);			
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Toast.makeText(getActivity(), "Picture Selected", Toast.LENGTH_SHORT).show();
					ImageView imageVIw =(ImageView)	arg1.findViewById(R.id.image);
					if(currentSelection != null) {
						currentSelection.setBackgroundResource(R.drawable.createalbumimagebackground);
					}
					currentSelection = imageVIw;
					currentPosition = arg2;
					currentSelection.setBackgroundResource(R.drawable.createalbumimagebackgroundafterselect);
					
					
				}
			});
			listview.setAdapter(mAdapter);
			
		}
		
		
		
		
		
		private BaseAdapter mAdapter = new BaseAdapter() {
			private boolean addIntialAnim = false;
			
			@Override
			public int getCount() {
				return dataObjects.size();
			}

			@Override
			public Object getItem(int position) {
				return null;
			}


			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View retView = convertView;			
			
					retView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.viewitem, null);
					ImageView imageVIw = (ImageView) retView
							.findViewById(R.id.image);
					
					if(dataObjects.get(position).getImageBitMap() != null) {
						imageVIw.setImageBitmap(dataObjects.get(position).getImageBitMap());
					} else {
						float density = rootView.getContext().getResources().getDisplayMetrics().density;
						int height =(int) (imageVIw.getLayoutParams().height*density);
						int width =(int) (imageVIw.getLayoutParams().width*density);	
						Bitmap bitmap  = decodeSampledBitmapFromResource(dataObjects.get(position).getAbsolutePath(),height , width);
						dataObjects.get(position).setImageBitMap(bitmap);
						imageVIw.setImageBitmap(bitmap);
						
					}
					
					
					
					if (position == 0  && addIntialAnim) {
						
						AnimationSet animSet =  new AnimationSet(true);
						TranslateAnimation a = new TranslateAnimation(0.0f, 0.0f,
								300.0f, 0.0f);
						a.setDuration(1500);
						a.setStartOffset(500);
						imageVIw.bringToFront();
						
						ScaleAnimation scaleAnim  =  new ScaleAnimation(0.5f, 1f, 0.5f,1, 50, 50);
						scaleAnim.setDuration(1000);
						scaleAnim.setStartOffset(1000);
						animSet.addAnimation(a);
						animSet.addAnimation(scaleAnim);
						imageVIw.startAnimation(animSet);
						addIntialAnim = false;
					}		
				
				return retView;
			}
			@Override
			public void notifyDataSetInvalidated() {
				addIntialAnim = true;
			   super.notifyDataSetInvalidated();
			};

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
	
		class  LoadFolderImages extends AsyncTask<File, Void, Void> {
			ProgressDialog barProgressDialog;
			ArrayList<File> listImages;
			
			
			
		
			
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(listImages != null && !listImages.isEmpty()) {
					Message msgObj = handler.obtainMessage();
					Bundle bundleObject = new Bundle();
					bundleObject.putSerializable("imageList", listImages);
					msgObj.obj = barProgressDialog;
					 msgObj.setData(bundleObject);
	                 handler.sendMessage(msgObj);
					
				} else {
					barProgressDialog.dismiss();
					Toast.makeText(rootView.getContext(), "No Images Found in the Folder", Toast.LENGTH_LONG).show();
				}
				
				//barProgressDialog.dismiss();
			}
			
			@Override
			protected void onPreExecute() {				
				super.onPreExecute();
				barProgressDialog = new  ProgressDialog(getActivity());
				barProgressDialog.setTitle("Downloading Image ...");
				barProgressDialog.setMessage("Download in progress ...");
				barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				barProgressDialog.setProgress(0);
				barProgressDialog.show();
				
				
			}

			@Override
			protected Void doInBackground(File... params) {				
				barProgressDialog.setMax(params[0].list().length);
				listImages = new ArrayList<File>(params[0].listFiles().length);
				
				for (File file : params[0].listFiles()) {
					if (file.isDirectory()) {
						continue;
					}
					String extension = file.getAbsolutePath().substring(
							file.getAbsolutePath().lastIndexOf('.'));

					if (SUPPORTED_IMG.contains(extension)) {
						listImages.add(file);
					}
				}
				
				return null;
			}

			

			
		}
		
		private final Handler handler = new Handler() {
				@Override
			 public void handleMessage(Message msg) {
				ArrayList<File>	listImages = (ArrayList<File>)msg.getData().getSerializable("imageList");
				ProgressDialog barProgressDialog  = (ProgressDialog)msg.obj;
				barProgressDialog.dismiss();
					BaseAdapter baseAdapter =	new GridAdapter(getActivity(), listImages);
					GridView gridView  =(GridView)	rootView.findViewById(R.id.gridView1);
					
					gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View view,
								int arg2, long arg3) {
							//ImageView imageView = (ImageView)view.findViewById(R.id.gridimage);
						}
					});
					
					
					gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View view, int arg2, long arg3) {
						final ImageView imageView = (ImageView)view.findViewById(R.id.gridimage);
						
						if(View.INVISIBLE == imageView.getVisibility()) {
							return false;
						}
						
							AnimationSet animationSet = new AnimationSet(true);							
							Animation scaleDown =  new ScaleAnimation(1, 0.5f, 1, 0.5f, 50, 50);
							scaleDown.setDuration(500);
							TranslateAnimation a = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
									TranslateAnimation.RELATIVE_TO_SELF, -300.0f);
							a.setStartOffset(600);
							a.setDuration(1000);
							
							animationSet.addAnimation(scaleDown);
							animationSet.addAnimation(a);
							imageView.bringToFront();
							imageView.startAnimation(animationSet);
							animationSet.setAnimationListener(new Animation.AnimationListener() {

								@Override
								public void onAnimationEnd(Animation animation) {
									imageView.setVisibility(View.INVISIBLE);									
								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub
									
								}
								
							});
							
							
							
							AlbumElement albumElement  = new AlbumElement();
							albumElement.setImageBitMap(getBitMapFromImageView(imageView));
							albumElement.setAbsolutePath((String)imageView.getTag());							
							dataObjects.add(0,albumElement);							
							//mAdapter.notifyDataSetChanged();
							mAdapter.notifyDataSetInvalidated();
							return true;
						}
						
					});
					gridView.setAdapter(baseAdapter);					
					
				 
			 }
			
		};

		class GridAdapter extends BaseAdapter {
			
			private Context context;
			private List<File> imageList;
			
			public GridAdapter(Context contexIn, List<File> imageListIn){
				context = contexIn;
				imageList = imageListIn;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return imageList.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View grid =null;
				LayoutInflater inflater = (LayoutInflater) context
				        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if (convertView == null) {
					grid = new View(context);
					grid = inflater.inflate(R.layout.createalbumgriditem, null);
								
				} else {
					grid = (View) convertView;
				}
				
				ImageView imageView = (ImageView)grid.findViewById(R.id.gridimage);
				imageView.setImageBitmap(null);
				imageView.setVisibility(View.VISIBLE);
				imageView.setTag(imageList.get(position).getAbsolutePath());
				Object[] params  = new Object[3];
				params[0] = imageView;
				params[1] = position;
				params[2] = imageList;
				new SetBitMapToGridImageAsycTask().execute(params);		
				
				return grid;
			}
			
		}
		
		class SetBitMapToGridImageAsycTask extends AsyncTask<Object, Void, Void> {

			@Override
			protected Void doInBackground(Object... params) {
				ImageView imageView  =  (ImageView)  params[0];
				int position  =  (Integer)  params[1];
				List<File> imageList = (List<File>)params[2];
				
				float density = rootView.getContext().getResources().getDisplayMetrics().density;
				int height =(int) (imageView.getLayoutParams().height*density);
				int width =(int) (imageView.getLayoutParams().width*density);	
				Bitmap bitmap  = decodeSampledBitmapFromResource(imageList.get(position).getAbsolutePath(),height , width);
			
				Message msgObj = setBitMapToGridImageHandler.obtainMessage();
				Bundle bundleObject = new Bundle();		
				msgObj.obj = imageView;				
				bundleObject.putParcelable("bitMap", bitmap);							
				msgObj.setData(bundleObject);
				setBitMapToGridImageHandler.sendMessage(msgObj);
				
				
				return null;
			}
			
		}
		
		private  Handler  setBitMapToGridImageHandler  = new  Handler () {
			
			@Override
			public void handleMessage(Message msg) {				
				Bitmap bitmap = (Bitmap)msg.getData().getParcelable("bitMap");				
				ImageView imageView  =(ImageView) msg.obj;
				imageView.setImageBitmap(bitmap);						
				Animation anim =  new AlphaAnimation(0, 1);
				anim.setDuration(500);
				
				imageView.startAnimation(anim);		
			}
			
		};
		

		@Override
		public void onDialogPositiveClick(DialogFragment dialog) {
			boolean rename = false;
			EditText myText =(EditText) dialog.getDialog().findViewById(R.id.editText1);
			
			if(dataObjects != null && !dataObjects.isEmpty()) {				
				
				SharedPreferences settings = getActivity().getSharedPreferences(APP_PREFERNCE, Context.MODE_PRIVATE);
				final String appFolder = settings.getString(SAVE_FOLDER_ROOT_PREF, Environment.getExternalStorageDirectory()+"/"+APP_FOLDER);
				
				if(this.albumName != null &&  !this.albumName.equals(myText.getText().toString())) {
					File prevFolder = new File (appFolder+"/"+this.albumName);
					if(prevFolder.exists()) {
						File[] files = prevFolder.listFiles();
						for (int i = 0; i < files.length; i++) {
							files[i].delete();
						}
						prevFolder.delete();
						rename = true;
				    }
					
				}
				
				File folder = new File (appFolder+"/"+myText.getText());
				
				if(this.albumName == null &&  folder.exists()) {
					
					AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			            alert.setTitle("Error");
			            alert.setMessage("Album \""+myText.getText().toString()+"\" Already Exists.... Please Enter New Name");
			            alert.setPositiveButton("OK", null);
			            alert.show();									
					return;
				}
				
				
				
				
				
				int i =1;
				
				JSONArray jsonArray =  new JSONArray();
				for(AlbumElement element : dataObjects) {
					JSONObject  jsonObject  = new JSONObject();
					try {
						jsonObject.put("position", i++);
						jsonObject.put("path", element.getAbsolutePath());
						jsonArray.put(jsonObject);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				
				if(!folder.exists()) {
					folder.mkdirs();
					folder.exists();
				}
			    File file = new File(folder,myText.getText()+".txt");			  
			    FileWriter writer = null;
			    try {				    	
					writer =  new FileWriter(file);
					writer.append(jsonArray.toString());
					writer.flush();
					AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		            alert.setTitle("Information");		           
		            alert.setPositiveButton("OK", null);
		           	
					if (rename) {
						 alert.setMessage("Album \""+ this.albumName+ "\" Renamed to \""+myText.getText().toString()+"\" and Saved Successfully");
						
					} else {
						 alert.setMessage("Album \""+ myText.getText().toString()+"\" Saved Successfully");
						
					}
					 alert.show();	
					this.albumName = myText.getText().toString();
				} catch (IOException e) {
					Toast.makeText(rootView.getContext(), "Error In Saving Album try Again!!", 3000).show();
					e.printStackTrace();
				} finally {
					
					dialog.dismiss();
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}
			
		}

		@Override
		public void onDialogNegativeClick(DialogFragment dialog) {
			
			
		}
		
		
	}

}

class AlbumDialogFragment extends DialogFragment {
	
	private String albumName;
	
	public AlbumDialogFragment (String albumNameIn) {
		super();
		albumName = albumNameIn;
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
	        	ActionBarActivity aba =  (ActionBarActivity)activity;
	            // Instantiate the NoticeDialogListener so we can send events to the host
	            mListener = (NoticeDialogListener) aba.getSupportFragmentManager().getFragments().get(0);
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
		View view  = inflater.inflate(R.layout.create_album_dialog_layout, null);
		if(this.albumName != null) {
			EditText editText = (EditText)view.findViewById(R.id.editText1);
			editText.setText(this.albumName);
		}		
		builder.setView(view);
		
		
		
		builder.setPositiveButton(R.string.create_album_dialog_positive_btn_text, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			
				mListener.onDialogPositiveClick(AlbumDialogFragment.this);
			}
			
		});
		builder.setNegativeButton(R.string.create_album_dialog_negative_btn_text, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				mListener.onDialogNegativeClick(AlbumDialogFragment.this);
				
				
			}
			
		});
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
				
			}
			
		});
	
		
		return dialog;
	}
	
}
