package com.beastmode2.classes;


import java.util.ArrayList;
import java.util.List;

public class TrendingStreams {

	
		public String id = "TrendingStreams";
		public List<Long> streams = new ArrayList<Long>();
		
		@SuppressWarnings("unused")
		public TrendingStreams() {
		}
		
		public void add( Stream stream ) {
			if ( streams == null ) {
				streams = new ArrayList<Long>();
			}
			streams.add( stream.id );
		}

		
		/*public void save() {
			ofy().save().entities(this).now();
		}
		
		public static List<Stream> fetchStreams( ) {
			
			List<Long> streamIds = ofy().load().type(TrendingStreams.class).id( "TrendingStreams" ).now().streams;
			List<Stream> streams = new ArrayList<Stream>();
			for ( Long id : streamIds ) {
				streams.add( Stream.fetchStream(id));
			}
			
			return streams;
		}*/

}
