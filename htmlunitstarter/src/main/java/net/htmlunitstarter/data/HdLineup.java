package net.wistools.data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class HdLineup implements IDataObject {

   private Set<String> players = new TreeSet<>();
   private double minutes = 0;
   private int plusMinus = 0;
   private double plusMinusPer25 = 0;

   @Override
   public String toString() {
      final Iterator<String> iter = players.iterator();
      final StringBuilder sb = new StringBuilder();
      while( iter.hasNext() ) {
         sb.append( iter.next() );
         if( iter.hasNext() ) {
            sb.append( "_" );
         }
      }
      return sb.toString().replace( " ", "" );
   }

   @Override
   public HdLineup clone() {
      final HdLineup clone = new HdLineup();
      clone.getPlayers().addAll( players );
      return clone;
   }

   public Set<String> getPlayers() {
      return players;
   }

   public void setPlayers( Set<String> players ) {
      this.players = players;
   }

   public void addPlayer( String player ) {
      players.add( player );
   }

   public void removePlayer( String player ) {
      players.remove( player );
   }

   public double getMinutes() {
      return minutes;
   }

   public void setMinutes( double minutes ) {
      this.minutes = minutes;
   }

   public int getPlusMinus() {
      return plusMinus;
   }

   public void setPlusMinus( int plusMinus ) {
      this.plusMinus = plusMinus;
   }

   public double getPlusMinusPer25() {
      return plusMinusPer25;
   }

   public void setPlusMinusPer25( double plusMinusPer25 ) {
      this.plusMinusPer25 = plusMinusPer25;
   }

   public void updatePlusMinus( Integer scoreDelta ) {
      plusMinus += scoreDelta;
   }

   public void updateMinutes( double timeOnFloor ) {
      minutes += timeOnFloor;
   }

}
