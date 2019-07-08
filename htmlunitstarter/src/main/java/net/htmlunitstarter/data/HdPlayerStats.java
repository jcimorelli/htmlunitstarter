package net.wistools.data;

public class HdPlayerStats implements IDataObject, Comparable<HdPlayerStats> {

   private String name;
   private double minutes = 0d;
   private int plusMinus = 0;
   private double plusMinusPer25 = 0d;
   private double offPct = 0d;
   private double ppg = 0d;
   private double scoringEfficiency = 0d;

   public HdPlayerStats( String name ) {
      this.name = name;
   }

   @Override
   public String toString() {
      return name;
   }

   @Override
   public int compareTo( HdPlayerStats otherPlayer ) {
      return name.compareTo( otherPlayer.getName() );
   }

   public String getName() {
      return name;
   }

   public void setName( String name ) {
      this.name = name;
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

   public void updateMinutes( double minutes ) {
      this.minutes += minutes;
   }

   public double getOffPct() {
      return offPct;
   }

   public void setOffPct( double offPct ) {
      this.offPct = offPct;
   }

   public double getPpg() {
      return ppg;
   }

   public void setPpg( double ppg ) {
      this.ppg = ppg;
   }

   public double getScoringEfficiency() {
      return scoringEfficiency;
   }

   public void setScoringEfficiency( double scoringEfficiency ) {
      this.scoringEfficiency = scoringEfficiency;
   }

}
