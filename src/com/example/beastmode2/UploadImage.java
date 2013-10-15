package com.example.beastmode2;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	}

    private void dispatchTakePictureIntent(int actionCode) {
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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

    private void handleSmallCameraPhoto(Intent intent) throws IOException {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        LayoutInflater inflater = LayoutInflater.from(this);
        /*View view = inflater.inflate(R.layout.camera, null);

        ImageView mImageView = (ImageView) view.findViewById(R.id.image);
        mImageView.setImageBitmap(mImageBitmap);*/

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


