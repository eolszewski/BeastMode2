package com.beastmode2.classes;

import java.io.IOException;

import com.google.android.maps.GeoPoint;
import com.google.gson.Gson;

@SuppressWarnings("deprecation")
public class UploadedImage {
	private GeoPoint location;
	private Long streamId;
	private String comments;
	private String owner;
	private String bkUrl;
	private byte[] image;

	@SuppressWarnings("unused")
	private UploadedImage() {
	}

	public UploadedImage(Long streamId, String owner, String content, byte[] image) {
		this.streamId = streamId;
		this.owner = owner;
		this.comments = content;
		this.image = image;
	}
	
	public String toJson() {
		return new Gson().toJson(this);
	}
	
	public static UploadedImage fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, UploadedImage.class);
	}
	
	public BeastImage toBeastImage() {
		return new BeastImage(this.streamId, this.owner, this.comments, this.bkUrl, this.location);
	}

}
