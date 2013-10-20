package com.example.beastmode2;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.beastmode2.classes.BeastImage;
import com.beastmode2.classes.Stream;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ViewStream extends Activity{
    static ArrayList<ImageView> images;
    String streamID, streamname;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	Intent myIntent= getIntent();
    	streamID = myIntent.getStringExtra("id"); 
    	images = new ArrayList<ImageView>();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewstream);
		postData();
		GridView gridview = (GridView) findViewById(R.id.gridview2);
	    gridview.setAdapter(new ImageAdapter(this, images));
	    
	    final Button upload = (Button) findViewById(R.id.Upload);
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityChangeIntent = new Intent(getBaseContext(), UploadImage.class);
                activityChangeIntent.putExtra("id", streamID);
                activityChangeIntent.putExtra("name", streamname);
                startActivity(activityChangeIntent);
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
		URL url = new URL("http://ericissunny.appspot.com/streamservlet?type=single&id="+this.streamID);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		String string = CharStreams.toString( new InputStreamReader( connection.getInputStream(), "UTF-8" ) );
		Gson gson = new Gson();
		Map<Stream, List<BeastImage>> result = new HashMap<Stream, List<BeastImage>>();

		result = gson.fromJson(string, new TypeToken<Map<Stream, List<BeastImage>>>(){}.getType() );
		TextView streamName = (TextView) findViewById(R.id.ViewStream);
		for(Stream str : result.keySet())
		{
			this.streamname = str.name;
			streamName.setText("View A Stream: " + this.streamname);
		}
		
		for(List<BeastImage> imageList : result.values())
		{
			for(BeastImage image : imageList)
			{
				String urlString = image.bkUrl;
				try { url = new URL(urlString); } catch (Exception e) {
	            	url = new URL("http://students.ou.edu/A/Jesus.I.Avila-1/white.jpg"); }
	            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
	            ImageView imageView = new ImageView(this);
	            imageView.setImageBitmap(bmp);
	            images.add(imageView);
			}
		}
	        

	    } catch (Exception e) {
	    	//streamsOutput.setText(e.toString());
	        // TODO Auto-generated catch block
	    }
	} 
}


