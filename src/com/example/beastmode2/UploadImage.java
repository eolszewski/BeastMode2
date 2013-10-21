package com.example.beastmode2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beastmode2.classes.UploadedImage;

public class UploadImage extends Activity{
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    Intent takePictureIntent;
    String mCurrentPhotoPath;
    String streamID;
    String streamName;
    Bitmap mImageBitmap;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	Intent myIntent= getIntent();
    	streamID = myIntent.getStringExtra("id"); 
    	streamName = myIntent.getStringExtra("name"); 

		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);

		final TextView name = (TextView) findViewById(R.id.uploadStream);
		name.setText("Stream: " + streamName);
		
		final Button upload = (Button) findViewById(R.id.CameraUpload);
        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		dispatchTakePictureIntent(11);
            }
        });
        
		final Button library = (Button) findViewById(R.id.Library);
		library.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            	photoPickerIntent.setType("image/*");
            	startActivityForResult(photoPickerIntent, 100);
        	}
        });
		
		final Button imageUpload = (Button) findViewById(R.id.imageUploader);
		imageUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(mImageBitmap != null) {
	        		final EditText additionalInfo = (EditText) findViewById(R.id.AdditionalInfo);
	        		ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        		mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        		byte[] byteArray = stream.toByteArray();
	        		UploadedImage img = new UploadedImage(Long.parseLong(streamID), "Anonymous", additionalInfo.getText().toString(), byteArray);
					try {
						HttpClient httpClient = new DefaultHttpClient();


					        HttpPost request = new HttpPost("http://ericissunny.appspot.com/api/upload");
					        StringEntity params =new StringEntity(img.toJson());
					        request.addHeader("content-type", "application/x-www-form-urlencoded");
					        request.setEntity(params);
					        httpClient.execute(request);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					finish();
            	}
            }
        });
	}

    private void dispatchTakePictureIntent(int actionCode) {
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if(requestCode == 100) { 
            if(resultCode == RESULT_OK){  
                //Uri selectedImage = data.getData();
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 16;
					
					InputStream imageStream = getContentResolver().openInputStream(data.getData());
					mImageBitmap = BitmapFactory.decodeStream(imageStream,null,options);
	                ImageView mImageView = (ImageView) findViewById(R.id.imageUpload);
	                mImageView.setImageBitmap(mImageBitmap);
	                imageStream.close();
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    	else {
	        try {
	            handleSmallCameraPhoto(data);
	    		Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, new String[]{Media.DATA, Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, Media.DATE_ADDED, null, "date_added ASC");
	            String photoPath = "";
				if(cursor != null && cursor.moveToFirst())
	            {
	                do {
	                    Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(Media.DATA)));
	                    photoPath = uri.toString();
	                }while(cursor.moveToNext());
	                cursor.close();
	            }
	
	            if(photoPath != null) {
	                Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
	                ///Do Implement your logic whatever you want.
	                ImageView mImageView = (ImageView) findViewById(R.id.imageUpload);
	                mImageView.setImageBitmap(bitmap);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    }

    private void handleSmallCameraPhoto(Intent intent) throws IOException {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        LayoutInflater inflater = LayoutInflater.from(this);
        
        File f = createImageFile();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        galleryAddPic();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(0));
        String imageFileName = timeStamp + "_";
        File image = File.createTempFile(
            imageFileName, 
            JPEG_FILE_SUFFIX, 
            null //default location for temporary files
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}


