package com.example.autocomplete;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class farmer_info extends Activity{

	private ProgressDialog pDialog;
	String district,crop;
	int error = 0;
	EditText edit1,edit2,edit3,edit4,edit5;
	Button submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.farmer_info);
		Intent i = getIntent(); // get the intent that started me
		district = i.getStringExtra("district");
		crop = i.getStringExtra("crop");
		edit1 = (EditText) findViewById(R.id.editText1);
		edit2 = (EditText) findViewById(R.id.editText2);
		edit3 = (EditText) findViewById(R.id.editText3);
		edit4 = (EditText) findViewById(R.id.editText4);
		edit5 = (EditText) findViewById(R.id.editText5);
		submit = (Button) findViewById(R.id.btnSubmit_farmer_info);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(edit1.getText().toString().trim().length() > 0 && edit2.getText().toString().trim().length() > 0
						&&edit3.getText().toString().trim().length() > 0 && edit4.getText().toString().trim().length() > 0
						&&edit5.getText().toString().trim().length() > 0){
					new LongOperation3().execute();
				}
				else
					Toast.makeText(getApplicationContext(), "Some fields are empty", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	//Async task
    public class LongOperation3 extends AsyncTask<String, String, String> {
   	
   	 @Override
        protected void onPreExecute() {
   		 pDialog = new ProgressDialog(farmer_info.this);
				pDialog.setMessage("Submitting details...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
        }
        	
   	 @Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
   		 try {
   			 
   	         	connecting_to_db__insert();
   	         } catch (Exception e) {
				// TODO: handle exception
				error = 1;
			}
			
      //	      
			return null;
		}
      

       @Override
       protected void onPostExecute(String result) {
       //	Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
    	   if(error == 1)
    		   Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
    	   else
    		   Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
    	   clear_edit_text();
    	   submit.requestFocus();
       	pDialog.dismiss();
       	
	        
       }
    }
    
    public void connecting_to_db__insert()
    {
    	String place ;
    	place = null;
    	place = district;
    	
    	org.apache.http.client.HttpClient  httpClient = new org.apache.http.impl.client.DefaultHttpClient();
		String url="http://weather-android.esy.es/farmer_info.php/?name="+edit1.getText().toString()+"&contact="
				+edit2.getText().toString()+"&place="+place+"&crop="+edit4.getText().toString()+"&area="+
				edit3.getText().toString()+"&produce="+edit5.getText().toString();
		url = url.replaceAll(" ", "%20");
		HttpGet httpPost = new HttpGet(url);
		try {
			
			HttpResponse response = httpClient.execute(httpPost);
			StatusLine answer = response.getStatusLine();
			
			
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Unable to Connect", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String error = e.toString();
			Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
		}
    }
    
    public void clear_edit_text()
    {
    	edit1.setText("");
    	edit2.setText("");
    	edit3.setText("");
    	edit4.setText("");
    	edit5.setText("");
    }
    

}
