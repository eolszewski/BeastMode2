package com.beastmode2.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import com.google.common.base.Joiner;
import com.google.gson.Gson;


public class Stream implements Comparable<Stream> {

	// id is set by the datastore for us
	public Long id;
	public String name;
	public String tags;
	public String owner;
	public String subscribers;
	public String welcomeMessage;
	public Date createDate;
	public String coverImageUrl;
	public Queue<Date> views;
//	private List<BeastImage> images;
  
	// TODO: figure out why this is needed
	@SuppressWarnings("unused")
	private Stream() {
	}	

	public Stream(String name, String tags, String owner, String subscribers, String welcomeMessage, String coverImageUrl) {
		this.name = name;
		this.tags = tags;
		this.owner = owner;
		this.subscribers = subscribers;
		this.welcomeMessage = welcomeMessage;
		this.coverImageUrl = coverImageUrl;
		this.createDate = new Date();
		this.views = new PriorityQueue<Date>();
	}
	/*
	public static Stream fetchStream( Long id ) {
		return ofy().load().type(Stream.class).id( id ).now();
	}
	
	public static List<Stream> fetchStreams( ) {
		return ofy().load().type(Stream.class).list();
	}
	
	public List<BeastImage> getImages() {
		return ofy().load().type(BeastImage.class).filter("streamId", id).list();
	}*/
	
	public void recordView( ) {
		Date date = new Date();
		views.add(date);
		removeOldViews(date);
	}
	
	public int getViewCount() {
		if ( views == null ) {
			System.out.println("ViewQueue is Null");
			return 0;
		} else {
			return views.size();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Long getId() {
		return id;
	}

	public void removeOldViews(Date current) {
		Long delta = 60L*60L*1000L;
		while ( current.getTime() - views.peek().getTime() > delta ) {
				views.poll();
		}
	}
	
	/*
	public void save() {
		ofy().save().entity(this).now();
	}
	
	public void delete() {
		ofy().delete().entity(this);
	}*/

	@Override
	public int compareTo(Stream other) {
		if (createDate.after(other.createDate)) {
			return -1;
		} else if (createDate.before(other.createDate)) {
			return 11;
		}
		return 0;
	}
	
	/*@Override
	public String toString() {
		Joiner joiner = Joiner.on(":");
		return joiner.join(id.toString(), name, tags, createDate.toString());
 	}*/
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
}
