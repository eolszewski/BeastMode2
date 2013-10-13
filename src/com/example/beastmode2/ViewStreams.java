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

public class ViewStreams extends Activity{
    private TextView streamsOutput;
    private LinearLayout main;
    static ArrayList<ImageView> images = new ArrayList<ImageView>();
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewstreams);
		postData();
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(ViewStreams.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
        //main = (LinearLayout) this.findViewById(R.id.screen);
        //streamsOutput = (TextView) this.findViewById(R.id.streamsOutput);
		//postData();
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
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("type", "all"));
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
	            List<String> streamComponents = Arrays.asList(pairs.getKey().toString().split(":"));

	            String urlString = "";
	            for(int i = 3; i < streamComponents.size()-3; i++)
	            	urlString += streamComponents.get(i)+":";
	            urlString = urlString.substring(0, urlString.length()-1);
	            try {
	            URL url = new URL(urlString);
	            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
	            LayoutParams lparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
	                    LayoutParams.WRAP_CONTENT);
	            ImageView imageView = new ImageView(this);
	            imageView.setLayoutParams(lparams);
	            imageView.setImageBitmap(bmp);
	            images.add(imageView);
	            //main.addView(imageView, count);
	            count++; } catch (Exception e) {
	            	System.out.println(urlString);
	            }
	            it.remove(); 
	        }
	        

	    } catch (Exception e) {
	    	//streamsOutput.setText(e.toString());
	        // TODO Auto-generated catch block
	    }
	} 
}


