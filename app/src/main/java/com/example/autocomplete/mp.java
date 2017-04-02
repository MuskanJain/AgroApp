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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class mp extends Activity {
	private ProgressDialog pDialog;
	String responseString,district,crop;
	TextView crop_name , crop_var , crop_min , crop_max  , crop_mar, crop_msp,crop_oth_mar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme);
		setContentView(R.layout.custom_dropdown);
		crop_name = (TextView) findViewById(R.id.crop_name);
		crop_var = (TextView) findViewById(R.id.crop_var);
		crop_min = (TextView) findViewById(R.id.crop_min);
		crop_max = (TextView) findViewById(R.id.crop_max);
		crop_mar = (TextView) findViewById(R.id.crop_mar);
		crop_msp = (TextView) findViewById(R.id.crop_msp);
		crop_oth_mar = (TextView) findViewById(R.id.crop_oth_mar);
		Intent i = getIntent(); // get the intent that started me
		district = i.getStringExtra("district");
		crop = i.getStringExtra("crop");
		crop_name.setText(crop);
		new LongOperation3().execute();
	}

	//Async task
    public class LongOperation3 extends AsyncTask<String, String, String> {
   	
   	 @Override
        protected void onPreExecute() {
   		 pDialog = new ProgressDialog(mp.this);
				pDialog.setMessage("Getting price details...");
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
      // 	Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
       	if(responseString.length() != 0)
       	insertSampleData();
       	pDialog.dismiss();
	        
       }
    }
		
		public void connecting_to_db_mp()
	    {
	    	String place ;
	    	responseString = null;
	    	place = null;
	    	place = district;
	    	
	    	org.apache.http.client.HttpClient  httpClient = new org.apache.http.impl.client.DefaultHttpClient();
			String url="http://weather-android.esy.es/mp.php/?crop="+crop+"&place="+place;
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
	    	String[] entries = responseString.split(",");
	    	crop_var.setText(entries[0]);
	    	crop_min.setText(entries[2]);
	    	crop_max.setText(entries[3]);
	    	crop_mar.setText(entries[1]);
	    	crop_msp.setText(entries[4]);
	    	crop_oth_mar.setText(entries[5]+" kms");
	      }
		}

}
