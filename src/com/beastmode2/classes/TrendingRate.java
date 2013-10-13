package com.beastmode2.classes;

public class TrendingRate {

	
		public String id = "TrendingRate";
		public String value;
		
		@SuppressWarnings("unused")
		private TrendingRate() {
		}
		
		public TrendingRate( String value ) {
			this.value = value;
		}
		/*
		public void save() {
			ofy().save().entities(this).now();
		}
		
		public static TrendingRate fetchRate( ) {
			return ofy().load().type(TrendingRate.class).id( "TrendingRate" ).now();
		}*/

}
