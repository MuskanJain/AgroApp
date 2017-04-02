package com.example.autocomplete;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;


import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends Activity {
 
    /*
     * Change to type CustomAutoCompleteView instead of AutoCompleteTextView 
     * since we are extending to customize the view and disable filter
     * The same with the XML view, type will be CustomAutoCompleteView
     */
	int region_type = 1;
    CustomAutoCompleteView myAutoComplete;
    Spinner spinner_d;
    Spinner spinner_a;
    Button btn_submit;
	String  responseString,responseString_d,responseString_a;
    String error_interent = "No Internet Connection";
    // adapter for auto-complete
    ArrayAdapter<String> myAdapter;
    // for database operations
    DatabaseHandler databaseH;
    // just to add some initial value
    String[] item = new String[] {"Please search..."};
    List<String> sp_data_d = new ArrayList<String>();
    List<String> sp_data_a = new ArrayList<String>();
    private ProgressDialog pDialog;
     
   
	@Override
    protected void onCreate(Bundle savedInstanceState) {
         
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme);
        setContentView(R.layout.activity_main);
        
        try{
        	spinner_d = (Spinner) findViewById(R.id.spinner_d);
            spinner_a = (Spinner) findViewById(R.id.spinner_a);
            btn_submit = (Button) findViewById(R.id.btnSubmit);
            // instantiate database handler
            databaseH = new DatabaseHandler(MainActivity.this);
            
            //Connect to database-akshat
           // connecting_to_db(1);
            new LongOperation2().execute(); 
            // put sample data to database
            //insertSampleData(1);
             
            // autocompletetextview is in activity_main.xml
            myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);
           
            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));
             
            // set our adapter
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete.setAdapter(myAdapter);
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        myAutoComplete = (CustomAutoCompleteView)findViewById(R.id.myautocomplete);
        myAutoComplete.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				sp_data_a.clear();
				responseString = null;
				new LongOperation().execute();
				//connecting_to_db(2);
				//insertSampleData(2);
				//sp_data_a.clear();
			}
		});
        btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sp_data_a.isEmpty())
				{
					Toast.makeText(getApplicationContext(), "Please Select your region", Toast.LENGTH_SHORT).show();
				}
				else
				{
				  Intent i = new Intent(MainActivity.this,weather_page.class);
				  i.putExtra("state", myAutoComplete.getText().toString());
				  i.putExtra("district", spinner_d.getSelectedItem().toString());
				  i.putExtra("area", spinner_a.getSelectedItem().toString());
				  startActivity(i);
				}
			}
		});
       spinner_d.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String a;
			a = spinner_d.getSelectedItem().toString();
			//Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
			//spinner_d.setOnItemSelectedListener(new CustomOnItemSelectedListener());
     	    if(a=="Select District") {
		    	sp_data_a.clear();
		    }
		    
		    else {
		    	responseString = null;
		    	new LongOperation3().execute();
				 // insertSampleData(3);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	});
	}
    public void connecting_to_db(int type)
    {
    	String place ;
    	responseString = null;
    	place = null;
    	if(type == 1) {
    		place = null;
    	}
    	else if(type==2) {
    		place =myAutoComplete.getText().toString();
    	}
    	else if(type == 3) {
    		place = spinner_d.getSelectedItem().toString();
    	}
    	org.apache.http.client.HttpClient  httpClient = new org.apache.http.impl.client.DefaultHttpClient();
		String url="http://weather-android.esy.es/default.php/?type="+type+"&place="+place;
		url = url.replaceAll(" ", "%20");
		HttpGet httpPost = new HttpGet(url);
		try {
			//httpClient.execute(httpPost);
			HttpResponse response = httpClient.execute(httpPost);
			//String a = response.toString();
			//HttpEntity ent = response.getEntity();
			StatusLine answer = response.getStatusLine();
			if(answer.getStatusCode() == HttpStatus.SC_OK){
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
		        //int strtidx = responseString.indexOf("<");
		        //strtidx -=1;
				{
		        	responseString=responseString.substring(0, responseString.length()-1);
		        }
			}
			//String answer1 = response.getHeaders("connection")[0].getValue();
			//Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_LONG).show();
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
    
    public void insertSampleData(int type){
         
        // CREATE
		sp_data_a.clear();
    	if(responseString.length() != 0)
    	{
    	String[] entries = responseString.split(",");
    	
    	if(type==1)
    	{
    		for(int i=0;i<entries.length;i++)
    		{
    			databaseH.create( new MyObject(entries[i]) ); 
    		}
    		/*databaseH.create( new MyObject("JAMMU-KASHMIR") );
            databaseH.create( new MyObject("HIMACHAL PRADESH") ); 
            databaseH.create( new MyObject("PUNJAB") );
            databaseH.create( new MyObject("UTTARAKHAND") ); 
            databaseH.create( new MyObject("HARYANA") );
            databaseH.create( new MyObject("DELHI") ); 
            databaseH.create( new MyObject("UTTAR PRADESH") );
            databaseH.create( new MyObject("BIHAR") ); 
            databaseH.create( new MyObject("SIKKIM") );
            databaseH.create( new MyObject("ASSAM") ); 
            databaseH.create( new MyObject("ARUNACHAL PRADESH") );
            databaseH.create( new MyObject("NAGALAND") ); 
            databaseH.create( new MyObject("MANIPUR") );
            databaseH.create( new MyObject("MIZORAM") ); 
            databaseH.create( new MyObject("TRIPURA") );
            databaseH.create( new MyObject("MEGHALAYA") ); 
            databaseH.create( new MyObject("WEST BENGAL") );
            databaseH.create( new MyObject("JHARKHAND") ); 
            databaseH.create( new MyObject("ORISSA") );
            databaseH.create( new MyObject("CHATTISGARH") ); 
            databaseH.create( new MyObject("ANDHRA PRADESH") );
            databaseH.create( new MyObject("TELANGANA") ); 
            databaseH.create( new MyObject("KARNATAKA") );
            databaseH.create( new MyObject("TAMIL NADU") ); 
            databaseH.create( new MyObject("KERALA") );
            databaseH.create( new MyObject("GOA") ); 
            databaseH.create( new MyObject("MAHARASHTRA") );
            databaseH.create( new MyObject("GUJARAT") ); 
            databaseH.create( new MyObject("LAKSHADWEEP") );
            databaseH.create( new MyObject("DAMAN AND DIU") );
            databaseH.create( new MyObject("ANDAMAN AND NICOBAR") );
            databaseH.create( new MyObject("MADHYA PRADESH") );
            databaseH.create( new MyObject("RAJASTHAN") );*/
            //databaseH.create( new MyObject("") ); 
    	}
    	else if(type==2){
    		sp_data_d.clear();
    		sp_data_d.add("Select District");
    		for(int i=0;i<entries.length;i++)
        	{
        		sp_data_d.add(entries[i]);
        		
        	}
    	}
    	else if(type==3){
    		sp_data_a.clear();
    		//sp_data_a.add("Select Area");
    		for(int i=0;i<entries.length;i++)
        	{
        		sp_data_a.add(entries[i]);
        		
        	}
    		
    	}}
     /*   databaseH.create( new MyObject("January") );
        databaseH.create( new MyObject("February") ); 
        databaseH.create( new MyObject("March") );
        databaseH.create( new MyObject("April") );
        databaseH.create( new MyObject("May") );
        databaseH.create( new MyObject("June") );
      /*  databaseH.create( new MyObject("July") );
        databaseH.create( new MyObject("August") );
        databaseH.create( new MyObject("September") );
        databaseH.create( new MyObject("Indian-coffee-two") );*/
         
    }
     
    // this function is used in CustomAutoCompleteTextChangedListener.java
    public String[] getItemsFromDb(String searchTerm){
         
        // add items on the array dynamically
    	//List<MyObject> products = databaseH_a.read(searchTerm);
    	List<MyObject> products;
		products = databaseH.read(searchTerm);
		int rowCount = products.size();
         
        String[] item = new String[rowCount];
        int x = 0;
        for (MyObject record : products) {
            item[x] = record.objectName;
            x++;
        }
        return item;
    }
    
    //Async task
     public class LongOperation extends AsyncTask<String, String, String> {
    	
    	 @Override
         protected void onPreExecute() {
    		 pDialog = new ProgressDialog(MainActivity.this);
				pDialog.setMessage("Fetching District Details...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
         }
         	
    	 @Override
 		protected String doInBackground(String... params) {
 			// TODO Auto-generated method stub
    		 try {
    			 String a;
    	         	a="ak";
    	         	connecting_to_db(2);
    	         	//insertSampleData(2);
    	         	//
			} catch (Exception e) {
				// TODO: handle exception
			}
 			return null;
 		}
       

        @Override
        protected void onPostExecute(String result) {
        	//Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
        	if(responseString != null)
        	    insertSampleData(2);
        	else
        		Toast.makeText(getApplicationContext(), error_interent, Toast.LENGTH_SHORT).show();
        	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
    				android.R.layout.simple_spinner_item, sp_data_d);
    				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    				spinner_d.setAdapter(dataAdapter);
        	pDialog.dismiss();
	        //super.onPostExecute(result);  	
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }
    }
     
   //Async task
     public class LongOperation2 extends AsyncTask<String, String, String> {
    	
    	 @Override
         protected void onPreExecute() {
    		 pDialog = new ProgressDialog(MainActivity.this);
				pDialog.setMessage("Initializing...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
         }
         	
    	 @Override
 		protected String doInBackground(String... params) {
 			// TODO Auto-generated method stub
    		 try {
    			 String a;
    	         	a="ak";
    	         	connecting_to_db(1);
    	         	//insertSampleData(1);
    	         	//
			} catch (Exception e) {
				// TODO: handle exception
			}
 			return null;
 		}
       

        @Override
        protected void onPostExecute(String result) {
        	//Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
        	
        	if(responseString != null)
        	  insertSampleData(1);
        	else
        		Toast.makeText(getApplicationContext(), error_interent, Toast.LENGTH_SHORT).show();
        	pDialog.dismiss();
	        //super.onPostExecute(result);  	
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }
        
    }
     
   //Async task
     public class LongOperation3 extends AsyncTask<String, String, String> {
    	
    	 @Override
         protected void onPreExecute() {
    		 pDialog = new ProgressDialog(MainActivity.this);
				pDialog.setMessage("Fetching block details...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
         }
         	
    	 @Override
 		protected String doInBackground(String... params) {
 			// TODO Auto-generated method stub
    		 try {
    	         	connecting_to_db(3);
    	         	//insertSampleData(3);
    	         	//
			} catch (Exception e) {
				// TODO: handle exception
			}
 			return null;
 		}
       

        @Override
        protected void onPostExecute(String result) {
        //	Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
        	if(responseString != null)
        		insertSampleData(3);
        	else
        		Toast.makeText(getApplicationContext(), error_interent, Toast.LENGTH_SHORT).show();
        	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
  				  android.R.layout.simple_spinner_item, sp_data_a);
  				  dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  				  spinner_a.setAdapter(dataAdapter);
        	pDialog.dismiss();
	        //super.onPostExecute(result);  	
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

    }

}
