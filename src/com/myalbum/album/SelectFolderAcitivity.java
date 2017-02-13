package com.myalbum.album;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectFolderAcitivity extends Album {
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
            WindowManager.LayoutParams.FLAG_DIM_BEHIND);
      /*  LayoutParams params = this.getWindow().getAttributes(); 
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        this.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params); */

        // This sets the window size, while working around the IllegalStateException thrown by ActionBarView
        
        DisplayMetrics metrics  =  new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Double height  = metrics.heightPixels * (0.8);
        Double width  = metrics.widthPixels * (0.8);
      this.getWindow().setLayout(width.intValue(), height.intValue());

        setContentView(R.layout.activity_select_folder_acitivity);
        

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_folder_acitivity, menu);
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
    	
    	int rootFolderInd = 0;
    	
    	File currentFolder;
	
    	File[]   rootFolder;
    	View rootView;
        public PlaceholderFragment() {
        	
        	File[] fileList  = Environment.getExternalStorageDirectory().getParentFile().listFiles();
        	rootFolder = new File[fileList.length];
        	int i= 0;
        	for(File file : fileList) {
        		rootFolder[i++] = file;
        	}        	
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
             rootView = inflater.inflate(R.layout.fragment_select_folder_acitivity, container, false);      
             
         setTopBanner();
         setOnClickOnBackButton(rootView);         
         setSelectFolderList();
            
            return rootView;
        }
        
        private void setTopBanner() {   
        	TextView textView = (TextView)	rootView.findViewById(R.id.bannerTextView);
        	if(currentFolder != null) {
        		
                textView.setText(currentFolder.getAbsolutePath());
        	} else {
        		textView.setText("/");
        	}
        
        }
        
		private void setOnClickOnBackButton(View rootView) {
			final Button button = (Button) rootView.findViewById(R.id.imageView1);
			
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				//	if(!currentFolder.getAbsolutePath().equals(rootFolderPath)) {
					if(currentFolder != null) {
						File parentFile =	currentFolder.getParentFile();
						currentFolder = parentFile;
						setTopBanner();						
						refreshSelectList(parentFile, false);
					}
						
					//}
					
				}
			});
		}
		
		private void setSelectFolderList() {
			currentFolder = null;
			
			 if(rootFolder == null) {
				 return;
			 }
	         List<File> fileList = new ArrayList<File>();
	         for(File fileTemp : rootFolder) {
	         	if(fileTemp != null && fileTemp.isDirectory()) {
	         		fileList.add(fileTemp);
	         	}
	         }
			final ListView listView = (ListView) rootView.findViewById(R.id.listViewSelectFolder);
			SelectFolderListAdapter selectFolderListAdapter = new SelectFolderListAdapter(rootView.getContext(), fileList);
			listView.setAdapter(selectFolderListAdapter);
			
		}
		
		
		
		
		
		private void refreshSelectList(File currentFolderIn, boolean incrementCount) {
			
			if(incrementCount) {
				rootFolderInd++;
			} else {
				rootFolderInd--;
			}
			
			if(rootFolderInd == 0) {
				setSelectFolderList();
			} else {
				File[] childeFiles  = currentFolderIn.listFiles();
				List<File> fileList = new ArrayList<File>();
				if(childeFiles != null) {
					for(File fileTemp : childeFiles) {
			         	if(fileTemp != null && fileTemp.isDirectory()) {
			         		fileList.add(fileTemp);
			         	}
			         }
				}
				 
				
				final ListView listView = (ListView) rootView.findViewById(R.id.listViewSelectFolder);
				SelectFolderListAdapter selectFolderListAdapter = (SelectFolderListAdapter)	listView.getAdapter();
				selectFolderListAdapter.refresAdapter(fileList);
				this.currentFolder = currentFolderIn;
				setTopBanner();
				
			}
			
			
		}
		
		
		class SelectFolderListAdapter extends  ArrayAdapter<File> {
			private final Context context;
			  private final List<File> values;
			public SelectFolderListAdapter(Context context, List<File> objects) {
				super(context, R.layout.select_folder_folder_list_item, objects);
				this.context = context;
				this.values = objects;
			}
			
			
			public synchronized void refresAdapter(List<File> fileList) {   
				values.clear();
				values.addAll(fileList);
			    notifyDataSetChanged();
			   }
			
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {	
				View rowView = convertView;
				
				if (rowView == null) {
					LayoutInflater inflater = (LayoutInflater) context
					        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				  rowView = inflater.inflate(R.layout.select_folder_folder_list_item, parent, false);
				} 
				 
				final TextView textView = (TextView) rowView.findViewById(R.id.textView1);
				 textView.setText(values.get(position).getName());
				 textView.setTag(values.get(position));				 
				 
				 
				 textView.setOnLongClickListener(new View.OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View arg0) {
							File file  = (File)textView.getTag();							
							Toast.makeText(SelectFolderListAdapter.this.context, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
							Intent returnIntent = new Intent();
							returnIntent.putExtra("selectedFolder", file);							
							getActivity().setResult(RESULT_OK,returnIntent);
							getActivity().finish();
							
							
							
							return true;
						}
					}); 
				 
				 textView.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View view) {						
					       refreshSelectList( ((File)textView.getTag()), true);						
					}
				});
				 
				
				 
				 return rowView;
			}
			
		}	

        
        
    }

}



	
