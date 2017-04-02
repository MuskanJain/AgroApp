package com.example.autocomplete;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import com.example.autocomplete.mp.LongOperation3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class best_farmer extends Activity {
	private ProgressDialog pDialog;
	String responseString,district;
	TextView bf_name , bf_crop,bf_produce,bf_seed,bf_fertilizers,bf_dis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme);
		setContentView(R.layout.best_farmer);
		bf_name = (TextView) findViewById(R.id.bf_name);
		bf_crop = (TextView) findViewById(R.id.bf_crop);
		bf_produce = (TextView) findViewById(R.id.bf_prod);
		bf_seed = (TextView) findViewById(R.id.bf_seed);
		bf_fertilizers = (TextView) findViewById(R.id.bf_ferti);
		bf_dis = (TextView) findViewById(R.id.bf_dis_name);
		Intent i = getIntent(); // get the intent that started me
		district = i.getStringExtra("district");
		bf_dis.setText(district);
		new LongOperation3().execute();
	}
	
	//Async task
    public class LongOperation3 extends AsyncTask<String, String, String> {
   	
   	 @Override
        protected void onPreExecute() {
   		 pDialog = new ProgressDialog(best_farmer.this);
				pDialog.setMessage("Getting details...");
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
			String url="http://weather-android.esy.es/best_farmer.php/?place="+place;
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
	    	String[] entries = responseString.split("#");
	    	bf_name.setText(entries[0]);
	    	bf_crop.setText(entries[1]);
	    	bf_produce.setText(entries[2]+" /beegha");
	    	bf_seed.setText(entries[3]);
	    	bf_fertilizers.setText(entries[4]);
	    	
	      }
		}

}