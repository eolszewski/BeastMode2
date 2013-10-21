package com.example.beastmode2;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.beastmode2.classes.BeastImage;
import com.beastmode2.classes.Stream;
import com.google.android.maps.GeoPoint;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NearbyImages extends Activity {
	static ArrayList<ImageView> images;
	static ArrayList imageIDs;
	String query;
	GeoPoint point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		images = new ArrayList<ImageView>();
		imageIDs = new ArrayList();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearbyimages);
		postData();
		Button viewAllStreams = (Button) findViewById(R.id.viewAllStreams);

		GridView gridview = (GridView) findViewById(R.id.nearbyGridview);
		gridview.setAdapter(new ImageAdapter(this, images));

		viewAllStreams.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(getBaseContext(),
						ViewStreams.class);
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
			Gson gson = new Gson();
			LocationManager locationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
			Location lastKnownLocation = locationManager
					.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

			int lat = (int) (lastKnownLocation.getLatitude() * 1E6);
			int lng = (int) (lastKnownLocation.getLongitude() * 1E6);
			URL url = new URL(
					"http://ericissunny.appspot.com/api/nearby?lat="
							+ lat + "&long=" + lng);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			String string = CharStreams.toString(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			Map<Stream, List<BeastImage>> result = new HashMap<Stream, List<BeastImage>>();

			result = gson.fromJson(string,
					new TypeToken<Map<Stream, List<BeastImage>>>() {
					}.getType());

			for (List<BeastImage> imageList : result.values()) {
				for (BeastImage image : imageList) {
					String urlString = image.bkUrl;
					try {
						url = new URL(urlString);
					} catch (Exception e) {
						url = new URL(
								"http://students.ou.edu/A/Jesus.I.Avila-1/white.jpg");
					}
					Bitmap bmp = BitmapFactory.decodeStream(url
							.openConnection().getInputStream());
					ImageView imageView = new ImageView(this);
					imageView.setImageBitmap(bmp);
					images.add(imageView);
				}
			}

		} catch (Exception e) {
			String itme = e.getMessage();
			System.out.println(itme);
			// TODO Auto-generated catch block
		}
	}
}
