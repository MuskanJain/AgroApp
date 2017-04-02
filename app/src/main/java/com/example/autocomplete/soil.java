package com.example.autocomplete;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import com.example.autocomplete.insect.LongOperation3;

import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class soil extends Activity{
	private ProgressDialog pDialog;
	String responseString=null,district,crop;
	TextView soil_area;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme);
		setContentView(R.layout.soil);
		soil_area = (TextView) findViewById(R.id.area_name_soil);
		Intent i = getIntent(); // get the intent that started me
		district = i.getStringExtra("district");
		soil_area.setText(district);
		new LongOperation3().execute();
	}
	
	
	//Async task
    public class LongOperation3 extends AsyncTask<String, String, String> {
   	
   	 @Override
        protected void onPreExecute() {
   		 pDialog = new ProgressDialog(soil.this);
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
	    	
	    		url="http://weather-android.esy.es/soil.php/?place="+place;
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
    		TableLayout mylayout = (TableLayout) findViewById(R.id.soil_table);
    		
    		LayoutParams lparams = new LayoutParams(
	    			   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	    String[] entries = responseString.split("#");
    	    for(int i=0;i<entries.length;i=i+2)
    	    {
    	    	TableRow row = new TableRow(this);
    	    	row.setLayoutParams(lparams);
    	    	TextView tv=new TextView(this);
    	    	TextView tv2=new TextView(this);
    			tv.setBackgroundColor(Color.parseColor("#b0b0b0"));
    			tv2.setBackgroundColor(Color.parseColor("#a09f9f"));
    			tv.setText(entries[i]);
    			tv2.setText(entries[i+1]);
    			tv.setTextColor(Color.BLACK);
    			tv.setPadding(4,0,0,0);
    			tv.setTextSize(20);
    			tv2.setTextColor(Color.BLACK);
    			tv2.setPadding(4,0,0,0);
    			tv2.setTextSize(20);
    			row.addView(tv);
    			row.addView(tv2);
    			mylayout.addView(row, i+1);
    	    }
    	
    	
      }
	}

}
