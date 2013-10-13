package com.beastmode2.classes;

import java.util.Date;

import com.google.android.maps.GeoPoint;
import com.google.common.base.Joiner;


public class BeastImage implements Comparable<BeastImage> {

	public Long id;
	public Long streamId;
	public String comments;
	public String owner;
	public String bkUrl;
	public Date createDate;
	public GeoPoint location;

	@SuppressWarnings("unused")
	private BeastImage() {
	}

	public BeastImage(Long streamId, String owner, String content, String bkUrl) {
		this.streamId = streamId;
		this.owner = owner;
		this.bkUrl = bkUrl;
		this.comments = content;
		createDate = new Date();
	}
	
	public BeastImage(Long streamId, String owner, String content, String bkUrl, GeoPoint gp) {
		this.streamId = streamId;
		this.owner = owner;
		this.bkUrl = bkUrl;
		this.comments = content;
		this.location = gp;
		createDate = new Date();
	}
	
	/*public static BeastImage fetchStream( Long id ) {
		return ofy().load().type(BeastImage.class).id( id ).now();
	}
	
	public void save() {
		ofy().save().entities(this).now();
	}*/

	@Override
	public String toString() {
		// Joiner is from google Guava (Java utility library), makes the toString method a little cleaner
		Joiner joiner = Joiner.on(":");
		System.out.println(id);
		System.out.println(streamId);
		System.out.println(bkUrl);
		System.out.println(createDate.toString());
		return joiner.join(id.toString(), streamId, comments, bkUrl==null ? "null" : bkUrl, createDate.toString());
	}

	// Need this for sorting images by date
	@Override
	public int compareTo(BeastImage other) {
		if (createDate.after(other.createDate)) {
			return -1;
		} else if (createDate.before(other.createDate)) {
			return 1;
		}
		return 0;
	}

}
