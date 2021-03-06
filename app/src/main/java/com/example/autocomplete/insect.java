package com.example.autocomplete;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.spec.MGF1ParameterSpec;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract.Colors;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class insect extends Activity {
	private ProgressDialog pDialog;
	String responseString=null,district,crop;
	int type_;
	TextView ins_crop,name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme);
		setContentView(R.layout.insect);
		ins_crop = (TextView) findViewById(R.id.crop_ins);
		name = (TextView) findViewById(R.id.insec_t);
		Intent i = getIntent(); // get the intent that started me
		district = i.getStringExtra("district");
		crop = i.getStringExtra("crop");
		type_ = i.getIntExtra("type", 0);
		if(type_==1)
			name.setText("Insecticides");
		else if (type_ ==2) {
			name.setText("Diseases");
		}
		else if (type_ ==3) {
			name.setText("Seeds");
		}
		else if (type_ ==4) {
			name.setText("Fertilizers");
		}
			
		ins_crop.setText(crop);
		new LongOperation3().execute();
	}

	//Async task
    public class LongOperation3 extends AsyncTask<String, String, String> {
   	
   	 @Override
        protected void onPreExecute() {
   		 pDialog = new ProgressDialog(insect.this);
				pDialog.setMessage("Fetching information..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
        }
        	
   	 @Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
   		 try {
   			 
   	         	connecting_to_db_mp();
   	         } catch (Exception e) {
				// TODO: handle exception
				String a;
				a="";
			}
			
      //	      
			return null;
		}
      

       @Override
       protected void onPostExecute(String result) {
       //	Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
    	   if(responseString!=null)
    		   insertSampleData();
    	   else
    		   Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
       	pDialog.dismiss();
	        
       }
    }
		
		public void connecting_to_db_mp()
	    {
			
	    	String place,url,type ;
	    	responseString = null;
	    	place = null;
	    	place = district;
	    	org.apache.http.client.HttpClient  httpClient = new org.apache.http.impl.client.DefaultHttpClient();
	    	
	    		url="http://weather-android.esy.es/insect.php/?type="+type_+"&crop="+crop+"&place="+place;
			url = url.replaceAll(" ", "%20");
			HttpGet httpPost = new HttpGet(url);
			try {
				
				HttpResponse response = httpClient.execute(httpPost);
				StatusLine answer = response.getStatusLine();
				if(answer.getStatusCode() == HttpStatus.SC_OK){
			        ByteArrayOutputStream out = new ByteArrayOutputStream();
			        response.getEntity().writeTo(out);
			        out.close();
			        responseString = out.toString();
			    
				}
				
			}
			catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String error = e.toString();
				Toast.makeText(getApplicationContext(), "Unable to Connect", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String error = e.toString();
				Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
			}
	    }
    
		
		public void insertSampleData(){
	        
	        // CREATEs
	    	
	    	if(responseString.length() != 0)
	    	{
	    		//String text = null;
	    		int j;
	    		LinearLayout mylayout = (LinearLayout) findViewById(R.id.ins_linear);
	    		LayoutParams lparams = new LayoutParams(
		    			   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    	    String[] entries = responseString.split(",");
	    	    for(int i=0;i<entries.length;i++)
	    	    {
	    	    	j=i+1; 
	    	    	TextView tv=new TextView(this);
	    			tv.setLayoutParams(lparams);
	    			tv.setText(j+")   "+entries[i]);
	    			tv.setTextColor(Color.BLACK);
	    			tv.setPadding(4,0,0,0);
	    			tv.setTextSize(20);
	    			mylayout.addView(tv);
	    	    }
	    	
	    	
	      }
		}

}
