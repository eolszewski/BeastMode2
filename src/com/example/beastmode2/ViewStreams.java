package com.example.beastmode2;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import android.widget.TextView;

import com.beastmode2.classes.BeastImage;
import com.beastmode2.classes.Stream;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ViewStreams extends Activity{
    private TextView streamsOutput;
    private LinearLayout main;
    static ArrayList<ImageView> images;
    static ArrayList imageIDs;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	images = new ArrayList<ImageView>();
    	imageIDs = new ArrayList();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewstreams);
		postData();
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this, images));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Intent myIntent = new Intent(getBaseContext(), ViewStream.class);
	        	myIntent.putExtra("id",String. valueOf((imageIDs.get(position))));
	        	startActivity(myIntent);    
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void postData() {
	    try {
		URL url = new URL("http://ericissunny.appspot.com/streamservlet?type=all");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		String string = CharStreams.toString( new InputStreamReader( connection.getInputStream(), "UTF-8" ) );
		Gson gson = new Gson();
		Map<Stream, List<BeastImage>> result = new HashMap<Stream, List<BeastImage>>();

		result = gson.fromJson(string, new TypeToken<Map<Stream, List<BeastImage>>>(){}.getType() );

	        Iterator it = result.entrySet().iterator();

	        int count = 0;
	        while (it.hasNext()) {
	            Map.Entry pairs = (Map.Entry)it.next();
	            
	            Stream str = (Stream) pairs.getKey();
	            String urlString = str.coverImageUrl;

	            try { url = new URL(urlString); } catch (Exception e) {
	            	url = new URL("http://students.ou.edu/A/Jesus.I.Avila-1/white.jpg"); }
	            
	            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
	            ImageView imageView = new ImageView(this);
	            imageView.setImageBitmap(bmp);
	            images.add(imageView);
	            imageIDs.add(str.id);
	            count++;
	            it.remove(); 
	        }
	        

	    } catch (Exception e) {
	    	//streamsOutput.setText(e.toString());
	        // TODO Auto-generated catch block
	    }
	} 
}


