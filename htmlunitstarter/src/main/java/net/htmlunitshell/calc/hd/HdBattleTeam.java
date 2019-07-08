package net.wistools.calc.hd;

import net.wistools.calc.hd.HdBattleCalculator.Assumptions;

import org.apache.commons.lang3.StringUtils;

public class HdBattleTeam {
   //Inputs
   private String name;
   private String prestige;
   private Integer netprefs;
   private Integer aps;
   private Integer cvs;
   private Integer hvs;
   private Integer starts;
   private Integer minutes;

   //Independent Calculated Values
   private Integer pValue;
   private Double adjustedPValue;
   private Double aggregateHVs;
   private Double hvMultiplier;
   private Double absoluteAggregateHVs;

   //Dependent Calculated Values
   private Double absoluteAggregateHVsBehind;
   private Double percentAAHVsBehind;

   //Odds Intermediate Values
   private Double nextLowestAahvThreshold;
   private Double pctBelowAahvThreshold;
   private Double aahvThresholdSpread;
   private Double nextLowestOddsThreshold;
   private Double pctBelowOddsThreshold;
   private Double oddsThresholdSpread;
   private Double vacuumOdds;

   //Battle Odds Values
   private Double unadjustedOdds;
   private Double adjustedOdds;

   public HdBattleTeam( String name, String prestige, String netprefs, String aps, String cvs, String hvs, String starts, String minutes ) {
      this.name = name;
      this.prestige = prestige;
      this.netprefs = StringUtils.isEmpty( netprefs ) ? 0 : Integer.parseInt( netprefs );
      this.aps = StringUtils.isEmpty( aps ) ? 0 : Integer.parseInt( aps );
      this.cvs = StringUtils.isEmpty( cvs ) ? 0 : Integer.parseInt( cvs );
      this.hvs = StringUtils.isEmpty( hvs ) ? 0 : Integer.parseInt( hvs );
      this.starts = StringUtils.isEmpty( starts ) ? 0 : Integer.parseInt( starts );
      this.minutes = StringUtils.isEmpty( minutes ) || "None".equals( minutes ) ? 0 : Integer.parseInt( minutes );
      calculateTeamIndependentValues();
   }

   private void calculateTeamIndependentValues() {
      pValue = HdBattleCalculator.PRESTIGE_VALUE_MAP.get( prestige );
      adjustedPValue = pValue + netprefs.doubleValue() / Assumptions.NetPref_Per_PValue;
      aggregateHVs = hvs
            + aps.doubleValue() / Assumptions.APs_Per_HV.doubleValue()
            + cvs.doubleValue() * Assumptions.HVs_Per_CV.doubleValue()
            + starts.doubleValue() * Assumptions.HVs_Per_Start.doubleValue()
            + ( minutes == 10 ? Assumptions.HVs_Per_10m : 0 )
            + ( minutes == 15 ? Assumptions.HVs_Per_15m : 0 )
            + ( minutes == 20 ? Assumptions.HVs_Per_20m : 0 )
            + ( minutes == 25 ? Assumptions.HVs_Per_25m : 0 );
      hvMultiplier = Math.pow( Assumptions.PValue_HV_Multiplier, adjustedPValue );
      absoluteAggregateHVs = aggregateHVs * hvMultiplier;
   }

   public void calculateOddsIntermediateValues() {
      final boolean isVeryHigh = percentAAHVsBehind < Assumptions.VeryHigh_High_Threshold;
      final boolean isHigh = !isVeryHigh && percentAAHVsBehind < Assumptions.High_Moderate_Threshold;
      final boolean isModerate = !isHigh && percentAAHVsBehind < Assumptions.Moderate_Low_Threshold;
      final boolean isLow = !isModerate && percentAAHVsBehind < Assumptions.Low_VeryLow_Threshold;
      nextLowestAahvThreshold = isVeryHigh ? 0 :
            isHigh ? Assumptions.VeryHigh_High_Threshold :
                  isModerate ? Assumptions.High_Moderate_Threshold :
                        isLow ? Assumptions.Moderate_Low_Threshold :
                              Assumptions.Low_VeryLow_Threshold;
      pctBelowAahvThreshold = percentAAHVsBehind - nextLowestAahvThreshold;
      aahvThresholdSpread = isVeryHigh ? Assumptions.VeryHigh_High_Threshold :
            isHigh ? Assumptions.High_Moderate_Threshold - Assumptions.VeryHigh_High_Threshold :
                  isModerate ? Assumptions.Moderate_Low_Threshold - Assumptions.High_Moderate_Threshold :
                        isLow ? Assumptions.Low_VeryLow_Threshold - Assumptions.Moderate_Low_Threshold :
                              0d;
      nextLowestOddsThreshold = isVeryHigh ? .5d : isHigh ? .4d : isModerate ? .25d : 0d;
      pctBelowOddsThreshold = pctBelowAahvThreshold / aahvThresholdSpread;
      oddsThresholdSpread = isVeryHigh ? .1d : isHigh ? .15d : 0d;
      vacuumOdds = isVeryHigh || isHigh ? nextLowestOddsThreshold - pctBelowOddsThreshold * oddsThresholdSpread : 0d;
   }

   public String getName() {
      return name;
   }

   public void setName( String name ) {
      this.name = name;
   }

   public String getPrestige() {
      return prestige;
   }

   public void setPrestige( String prestige ) {
      this.prestige = prestige;
   }

   public Integer getNetprefs() {
      return netprefs;
   }

   public void setNetprefs( Integer netprefs ) {
      this.netprefs = netprefs;
   }

   public Integer getAps() {
      return aps;
   }

   public void setAps( Integer aps ) {
      this.aps = aps;
   }

   public Integer getCvs() {
      return cvs;
   }

   public void setCvs( Integer cvs ) {
      this.cvs = cvs;
   }

   public Integer getHvs() {
      return hvs;
   }

   public void setHvs( Integer hvs ) {
      this.hvs = hvs;
   }

   public Integer getStarts() {
      return starts;
   }

   public void setStarts( Integer starts ) {
      this.starts = starts;
   }

   public Integer getMinutes() {
      return minutes;
   }

   public void setMinutes( Integer minutes ) {
      this.minutes = minutes;
   }

   public Integer getpValue() {
      return pValue;
   }

   public void setpValue( Integer pValue ) {
      this.pValue = pValue;
   }

   public Double getAdjustedPValue() {
      return adjustedPValue;
   }

   public void setAdjustedPValue( Double adjustedPValue ) {
      this.adjustedPValue = adjustedPValue;
   }

   public Double getAggregateHVs() {
      return aggregateHVs;
   }

   public void setAggregateHVs( Double aggregateHVs ) {
      this.aggregateHVs = aggregateHVs;
   }

   public Double getHvMultiplier() {
      return hvMultiplier;
   }

   public void setHvMultiplier( Double hvMultiplier ) {
      this.hvMultiplier = hvMultiplier;
   }

   public Double getAbsoluteAggregateHVs() {
      return absoluteAggregateHVs;
   }

   public void setAbsoluteAggregateHVs( Double absoluteAggregateHVs ) {
      this.absoluteAggregateHVs = absoluteAggregateHVs;
   }

   public Double getAbsoluteAggregateHVsBehind() {
      return absoluteAggregateHVsBehind;
   }

   public void setAbsoluteAggregateHVsBehind( Double absoluteAggregateHVsBehind ) {
      this.absoluteAggregateHVsBehind = absoluteAggregateHVsBehind;
   }

   public Double getPercentAAHVsBehind() {
      return percentAAHVsBehind;
   }

   public void setPercentAAHVsBehind( Double percentAAHVsBehind ) {
      this.percentAAHVsBehind = percentAAHVsBehind;
   }

   public Double getNextLowestAahvThreshold() {
      return nextLowestAahvThreshold;
   }

   public void setNextLowestAahvThreshold( Double nextLowestAahvThreshold ) {
      this.nextLowestAahvThreshold = nextLowestAahvThreshold;
   }

   public Double getPctBelowAahvThreshold() {
      return pctBelowAahvThreshold;
   }

   public void setPctBelowAahvThreshold( Double pctBelowAahvThreshold ) {
      this.pctBelowAahvThreshold = pctBelowAahvThreshold;
   }

   public Double getAahvThresholdSpread() {
      return aahvThresholdSpread;
   }

   public void setAahvThresholdSpread( Double aahvThresholdSpread ) {
      this.aahvThresholdSpread = aahvThresholdSpread;
   }

   public Double getNextLowestOddsThreshold() {
      return nextLowestOddsThreshold;
   }

   public void setNextLowestOddsThreshold( Double nextLowestOddsThreshold ) {
      this.nextLowestOddsThreshold = nextLowestOddsThreshold;
   }

   public Double getPctBelowOddsThreshold() {
      return pctBelowOddsThreshold;
   }

   public void setPctBelowOddsThreshold( Double pctBelowOddsThreshold ) {
      this.pctBelowOddsThreshold = pctBelowOddsThreshold;
   }

   public Double getOddsThresholdSpread() {
      return oddsThresholdSpread;
   }

   public void setOddsThresholdSpread( Double oddsThresholdSpread ) {
      this.oddsThresholdSpread = oddsThresholdSpread;
   }

   public Double getVacuumOdds() {
      return vacuumOdds;
   }

   public void setVacuumOdds( Double vacuumOdds ) {
      this.vacuumOdds = vacuumOdds;
   }

   public Double getUnadjustedOdds() {
      return unadjustedOdds;
   }

   public void setUnadjustedOdds( Double unadjustedOdds ) {
      this.unadjustedOdds = unadjustedOdds;
   }

   public Double getAdjustedOdds() {
      return adjustedOdds;
   }

   public void setAdjustedOdds( Double adjustedOdds ) {
      this.adjustedOdds = adjustedOdds;
   }
}
