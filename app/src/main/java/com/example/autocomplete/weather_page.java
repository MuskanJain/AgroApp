package com.example.autocomplete;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class weather_page extends Activity {
	
	private ProgressDialog pDialog;
	ImageView img;
	String responseString=null,img_url,condition,temp,responseString_woeid=null,woeid,responseString_crop=null;
	String a,state,area,district;
	int error;
	TextView cond,temperature,area_name;
	Button mp_btn,ins_btn,dis_btn,farm_btn,btn_seed,btn_fert,btn_frm_info,btn_soil,btn_best_frm,btn_about_us;
	Spinner spinner_c;
	List<String> sp_data_crop = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme);
		setContentView(R.layout.weather_page);
		Intent i = getIntent(); // get the intent that started me
		state = i.getStringExtra("state");
		district = i.getStringExtra("district");
		area = i.getStringExtra("area");
		//state = "Orrisa";
		//district= "Ganjam";
		//area="Surada";
		mp_btn = (Button) findViewById(R.id.btn_mp);
		ins_btn = (Button) findViewById(R.id.btn_ins);
		dis_btn = (Button) findViewById(R.id.btn_dis);
		farm_btn= (Button) findViewById(R.id.btn_frm);
		btn_seed = (Button) findViewById(R.id.btn_seed);
		btn_fert = (Button) findViewById(R.id.btn_fertilizer);
		btn_soil = (Button) findViewById(R.id.btn_soil_info);
		btn_frm_info = (Button) findViewById(R.id.btn_frm_info);
		btn_best_frm = (Button) findViewById(R.id.btn_best_frm);
		btn_about_us = (Button) findViewById(R.id.btn_about_us);
		spinner_c = (Spinner) findViewById(R.id.spinner_crops);
		area_name = (TextView) findViewById(R.id.area_name);
		area_name.setText(area+"-"+district+"-"+state);
		img = (ImageView) findViewById(R.id.cond_img);
		cond = (TextView) findViewById(R.id.txtcond);
		temperature = (TextView) findViewById(R.id.txttemp);
		new DownloadImageTask((ImageView) findViewById(R.id.cond_img))
        .execute("http://l.yimg.com/a/i/us/we/52/21.gif");
		
		mp_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent i = new Intent(weather_page.this,mp.class);
				  i.putExtra("district", district);
				  i.putExtra("crop", spinner_c.getSelectedItem().toString());
				  startActivity(i);
			}
		});
		
		ins_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(weather_page.this,insect.class);
				  i.putExtra("district", district);
				  i.putExtra("crop", spinner_c.getSelectedItem().toString());
				  i.putExtra("type", 1);
				  startActivity(i);
			}
		});
		
		dis_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(weather_page.this,insect.class);
				  i.putExtra("district", district);
				  i.putExtra("crop", spinner_c.getSelectedItem().toString());
				  i.putExtra("type", 2);
				  startActivity(i);
			}
		});
		
		btn_seed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(weather_page.this,insect.class);
				  i.putExtra("district", district);
				  i.putExtra("crop", spinner_c.getSelectedItem().toString());
				  i.putExtra("type", 3);
				  startActivity(i);
			}
		});
		
		btn_fert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(weather_page.this,insect.class);
				  i.putExtra("district", district);
				  i.putExtra("crop", spinner_c.getSelectedItem().toString());
				  i.putExtra("type", 4);
				  startActivity(i);
			}
		});
		
		farm_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(weather_page.this,farm.class);
				  i1.putExtra("district", district);
				  i1.putExtra("crop", spinner_c.getSelectedItem().toString());
				  startActivity(i1);
				
			}
		});
		
		btn_frm_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(weather_page.this,farmer_info.class);
				  i1.putExtra("district", district);
				  i1.putExtra("crop", spinner_c.getSelectedItem().toString());
				  startActivity(i1);
			}
		});
		
		btn_soil.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(weather_page.this,soil.class);
				  i1.putExtra("district", district);
				  startActivity(i1);
			}
		});
		
		btn_best_frm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(weather_page.this,best_farmer.class);
				  i1.putExtra("district", district);
				  startActivity(i1);
			}
		});
		btn_about_us.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(weather_page.this,about_us.class);
				  
				  startActivity(i1);
			}
		});
		
	}
	public Bitmap getBitmap(String url) {    //pass the complete URL of the web service query
		        Bitmap bmp = null;
		        try {
		            HttpURLConnection client = new DefaultHttpClient();
		            URI imageURI = new URI(url);
		            HttpGet req = new HttpGet();
		            req.setURI(imageURI);
		            HttpResponse response = client.execute(req);
		            bmp = BitmapFactory.decodeStream(response.getEntity().getContent());    //BitmapFactory decodes the InputStream of the HttpResponse
		       } catch (URISyntaxException e) {        //catch those exceptions
		           Log.e("ERROR",e.getMessage());
		       } catch (ClientProtocolException e) {
		           Log.e("ERROR",e.getMessage());
		       } catch (IllegalStateException e) {
		           Log.e("ERROR",e.getMessage());
		       } catch (IOException e) {
		           Log.e("ERROR",e.getMessage());
		       }
		       return bmp;
		   }
	
	//Async task
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		  ImageView bmImage;

		  public DownloadImageTask(ImageView bmImage) {
		      this.bmImage = bmImage;
		  }
		  
		  @Override
	         protected void onPreExecute() {
			  super.onPreExecute();	
	    		 pDialog = new ProgressDialog(weather_page.this);
					pDialog.setMessage("Loading...");
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(false);
					pDialog.show();
	         }
		  
		  protected Bitmap doInBackground(String... urls) {
			  
			  Bitmap mIcon11 = null;
			  error=0;
		      try {
		    	  connecting_to_yahoo_woeid();
		    	  parseResponse_woeid(responseString_woeid);
		    	  connecting_to_db(woeid);
				  parseResponse(responseString);
				  String urldisplay = urls[0];
			      a = responseString.substring(responseString.indexOf("[CDATA") + 1,responseString.indexOf("]>") );
				  a = a.substring(a.indexOf("\"")+1, a.indexOf(">"));
				  img_url = a.substring(a.indexOf("h"),a.indexOf("\"") );
				  urldisplay = img_url;
				  connecting_to_db_crop();
			     
		        InputStream in = new java.net.URL(urldisplay).openStream();
		        mIcon11 = BitmapFactory.decodeStream(in);
		        
		      } catch (Exception e) {
		          Log.e("Error", e.getMessage());
		          e.printStackTrace();
		         error=1;
		      }
		      return mIcon11;
		  }

		  protected void onPostExecute(Bitmap result) {
		      bmImage.setImageBitmap(result);
		      pDialog.dismiss();
		      if(error==1)
		    	  Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_LONG).show();
		      cond.setText("  "+condition);
			  temperature.setText(temp+ " \u2103"); 
			  if(responseString.length() != 0)
				  insertSampleData();
	        	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(weather_page.this,
	  				  android.R.layout.simple_spinner_item, sp_data_crop);
	  				  dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  				  spinner_c.setAdapter(dataAdapter);
	        	pDialog.dismiss();
		  }
		}
	
	public  String parseResponse (String resp) {
	    Log.d("SwA", "Response ["+resp+"]");
	    try {
	        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
	        parser.setInput(new StringReader(resp));
	 
	        String tagName = null;
	        String currentTag = null;
	        
	        int event = parser.getEventType();
	        boolean isFirstDayForecast = true;
	        while (event != XmlPullParser.END_DOCUMENT) {
	            tagName = parser.getName();
	            
	           
	            if (event == XmlPullParser.START_TAG) {
	                if (tagName.equals("yweather:wind")) {
	                    
	                }
	                else if (tagName.equals("yweather:atmosphere")) {
	                    
	                }
	                else if (tagName.equals("yweather:forecast")) {
	                    
	                }
	                else if (tagName.equals("yweather:condition")) {
	                    condition = parser.getAttributeValue(null, "text");
	                    temp = parser.getAttributeValue(null, "temp");
	               }
	                else if (tagName.equals("yweather:units")) {
	                   
	                }
	                else if (tagName.equals("yweather:location")) {
	                   
	                }
	                else if (tagName.equals("description")){
	                	
	                }
	                else if (tagName.equals("description")) {
	                  //  if (currentTag == null) {
	                       a = parser.getAttributeValue(null, "src");
	                    
	                      
	                   // }
	                }
	                else if (tagName.equals("lastBuildDate")) {
	                   currentTag="update";
	                }
	                else if (tagName.equals("yweather:astronomy")) {
	                
	             }
	 
	            }
	            else if (event == XmlPullParser.END_TAG) {
	                if ("image".equals(currentTag)) {
	                   currentTag = null;
	                }
	            }
	            else if (event == XmlPullParser.TEXT) {
	                //if ("update".equals(currentTag))
	                   // result.lastUpdate = parser.getText();
	            }
	            event = parser.next();
	        }
	        
	    }
	    catch(Throwable t) {
	        t.printStackTrace();
	    }
	 
	    return null;
	}
	
	public void connecting_to_db(String woeid)
    {
    	org.apache.http.client.HttpClient  httpClient = new org.apache.http.impl.client.DefaultHttpClient();
		String url="http://weather.yahooapis.com/forecastrss?w="+woeid+"&u=c";
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
		       // int strtidx = responseString.indexOf("<");
		       // strtidx -=1;
		       // responseString=responseString.substring(0, strtidx-2);
		        
			}
			//String answer1 = response.getHeaders("connection")[0].getValue();
			//Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_LONG).show();
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String error = e.toString();
			Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String error = e.toString();
			Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
		}
    }

	
	@SuppressWarnings("deprecation")
	public void connecting_to_yahoo_woeid()

    {
		String place;
		place = area + "%20" + district + "%20" + state;
    	org.apache.http.client.HttpClient  httpClient = new org.apache.http.impl.client.DefaultHttpClient();
    	String appi_id = "lH2MtxHV34GiDcXJoQPmMUY44poyHMmTtNop2v2d0JBYVcXBCI8qklSS_NCkj8GiwoB4x_bqdd6ofg";
		String url="http://where.yahooapis.com/v1/places.q('"+place+"')?appid="+appi_id;
		url = url.replaceAll(" ", "%20");
		//url = URLEncoder.encode(url);
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
		        responseString_woeid = out.toString();
		       // int strtidx = responseString.indexOf("<");
		       // strtidx -=1;
		       // responseString=responseString.substring(0, strtidx-2);
		        
			}
			
			//Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_LONG).show();
		}
		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String error = e.toString();
			Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String error = e.toString();
			Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
		}
    }
    
	
	public  String parseResponse_woeid (String resp) {
	    Log.d("SwA", "Response ["+resp+"]");
	    try {
	        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
	        parser.setInput(new StringReader(resp));
	 
	        String tagName = null;
	        String currentTag = null;
	        
	        int event = parser.getEventType();
	        boolean isFirstDayForecast = true;
	        while (event != XmlPullParser.END_DOCUMENT) {
	            tagName = parser.getName();
	            
	           
	            if (event == XmlPullParser.START_TAG) {
	                if (tagName.equals("place")) {
	                	woeid = parser.getAttributeValue(null, "yahoo:uri");
	                	//a = a.substring(a.indexOf("\"")+1, a.indexOf(">"));
	                	woeid = woeid.substring(woeid.indexOf("place")+6);
	                }
	                
	            }
	            else if (event == XmlPullParser.END_TAG) {
	                
	            }
	            else if (event == XmlPullParser.TEXT) {
	                //if ("update".equals(currentTag))
	                   // result.lastUpdate = parser.getText();
	            }
	            event = parser.next();
	        }
	        
	    }
	    catch(Throwable t) {
	        t.printStackTrace();
	    }
	 
	    return null;
	}

	
	public void connecting_to_db_crop()
    {
    	String place ;
    	responseString_crop = null;
    	place = null;
    	place = district;
    	org.apache.http.client.HttpClient  httpClient = new org.apache.http.impl.client.DefaultHttpClient();
		String url="http://weather-android.esy.es/crop.php/?type=1&place="+place;
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
		        responseString_crop = out.toString();
		       // int strtidx = responseString_crop.indexOf("<");
		       // strtidx -=1;
		        {
		        	responseString_crop=responseString_crop.substring(0, responseString_crop.length()-1);
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

	
	public void insertSampleData(){
        
        // CREATE
    	
    	sp_data_crop.clear();
    	if(responseString_crop.length() != 0)
    	{
    	String[] entries = responseString_crop.split(",");
    	
    		for(int i=0;i<entries.length;i++)
        	{
        		sp_data_crop.add(entries[i]);
        		
        	}
    	}
	}

}
