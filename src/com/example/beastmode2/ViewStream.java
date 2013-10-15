package com.example.beastmode2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.beastmode2.classes.BeastImage;
import com.beastmode2.classes.Stream;
import com.google.gson.Gson;

public class ViewStream extends Activity{
    private TextView streamsOutput;
    private LinearLayout main;
    static ArrayList<ImageView> images;
    static ArrayList imageURLs;
    String streamID;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	Intent myIntent= getIntent();
    	streamID = myIntent.getStringExtra("id"); 
    	images = new ArrayList<ImageView>();
    	imageURLs = new ArrayList();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewstream);
		postData();
		GridView gridview = (GridView) findViewById(R.id.gridview2);
	    gridview.setAdapter(new ImageAdapter(this, images));


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void postData() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.ericissunny.appspot.com/streamservlet");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("type", "single"));
	        nameValuePairs.add(new BasicNameValuePair("id", streamID));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);

	        Gson gson = new Gson();
			Map<Stream, List<BeastImage>> result = new HashMap<Stream, List<BeastImage>>();
			
	        // Execute HTTP Post Request
	        result = gson.fromJson(EntityUtils.toString(response.getEntity()), HashMap.class);
	        Iterator it = result.entrySet().iterator();

	        int count = 0;
	        while (it.hasNext()) {
	            Map.Entry pairs = (Map.Entry)it.next();

	            List imagesArr = (List)pairs.getValue();
	            for(Object o : imagesArr){
	            	String s = "";
	            	s = o.toString().replace("{", "").replace("}", "");
	            	List<String> imageList = Arrays.asList(s.split(", "));
	            	
		            //BeastImage img = gson.fromJson(imgs.get(0).toString(), BeastImage.class);
		            String urlString = imageList.get(4).replace("bkUrl=", "");
		            URL url;
		            try { url = new URL(urlString); } catch (Exception e) {
		            	url = new URL("http://students.ou.edu/A/Jesus.I.Avila-1/white.jpg"); }
		            
		            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		            ImageView imageView = new ImageView(this);
		            imageView.setImageBitmap(bmp);
		            images.add(imageView);
		            //imageURLs.add(str.url);
		            count++;
		        }
	            it.remove(); 

	        }
	
	    } catch (Exception e) {
	    	//streamsOutput.setText(e.toString());
	        // TODO Auto-generated catch block
	    }
	} 
}


