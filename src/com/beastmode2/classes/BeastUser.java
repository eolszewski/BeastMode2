package com.beastmode2.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.Joiner;

public class BeastUser {

	public String username;
	public boolean facebook;
	private HashSet<Long> subscriptions;
	private HashSet<Long> owned;
	public Date createDate;

	@SuppressWarnings("unused")
	private BeastUser() {
	}

	public BeastUser(String username, boolean facebook) {
		this.username = username;
		this.facebook = facebook;
		this.subscriptions = new HashSet<Long>();
		this.owned = new HashSet<Long>();
		createDate = new Date();
	}
	
	public void subscribeTo( Long streamId ) {
		if ( subscriptions == null ) {
			subscriptions = new HashSet<Long>();
		}
		subscriptions.add( streamId );
	}
	
	public void ownStream( Long streamId ) {
		if ( owned == null ) {
			owned = new HashSet<Long>();
		}
		owned.add( streamId );
	}
	
	public void disownStream( Long streamId ) {
		if ( owned != null ) {
			owned.remove( streamId );
		}
		
	}
	
	public void unsubscribeFrom( Long streamId ) {
		if ( subscriptions != null ) {
			subscriptions.remove( streamId );
		}
	}
	
	public boolean isSubscribedTo( Long streamId ) {
		if ( subscriptions != null) {
			return subscriptions.contains( streamId ); 
		} else {
			return false;
		}
	}
	
	/*public List<Stream> getSubscriptionStreams() {
		List<Stream> streams = new ArrayList<Stream>();
		if ( subscriptions != null ) {
			for (Long id : subscriptions ) {
			
				streams.add( ofy().load().type(Stream.class).id( id ).now() );
			}
		}
		return streams;
	}
	
	public List<Stream> getOwnedStreams() {
		List<Stream> streams = new ArrayList<Stream>();
		if ( owned != null ) {
			for (Long id : owned ) {
			
				streams.add( ofy().load().type(Stream.class).id( id ).now() );
			}
		}
		return streams;
	}
	
	
	public static BeastUser fetchUser( String username ) {
		return ofy().load().type(BeastUser.class).id( username ).now();
	}
	
	public void save() {
		ofy().save().entities(this).now();
	}*/

}
