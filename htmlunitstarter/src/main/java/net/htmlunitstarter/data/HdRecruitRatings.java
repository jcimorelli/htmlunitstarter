package net.wistools.data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import net.wistools.nav.WisPage;
import net.wistools.utl.MathUtl;
import net.wistools.utl.ReflectionUtl;

public class HdRecruitRatings implements IDataObject, IHdRecruit {

   @ReportTransient
   private Integer recruitId;
   private String recruitName;
   @ReportField(name = "Pos")
   private String position;
   @ReportField(name = "PosRank")
   private String positionRank;
   @ReportField(name = "OvrRank")
   private String overallRank;
   @ReportField(name = "Div")
   private String projectedDiv;
   @ReportField(name = "Elig")
   private String eligibility;
   private String years;
   private String city;
   private String colorCategory;
   private String signedWith;

   @ReportField(name = "ScoutLvl")
   private String scoutingLevel;
   @ReportField(name = "Dist")
   private Integer distance;
   private BigDecimal costToMax;//calculated based on distance
   private String offenseSet;
   private String defenseSet;

   //Ratings
   @ReportField(name = "ATH")
   private Integer athleticism;
   @ReportField(name = "SPD")
   private Integer speed;
   @ReportField(name = "REB")
   private Integer rebounding;
   @ReportField(name = "DEF")
   private Integer defense;
   @ReportField(name = "SB")
   private Integer shotBlocking;
   @ReportField(name = "LP")
   private Integer lowPost;
   @ReportField(name = "PER")
   private Integer perimeter;
   @ReportField(name = "BH")
   private Integer ballHandling;
   @ReportField(name = "PAS")
   private Integer passing;
   @ReportField(name = "WE")
   private Integer workEthic;
   @ReportField(name = "STA")
   private Integer stamina;
   @ReportField(name = "DUR")
   private Integer durability;
   @ReportField(name = "FT")
   private Integer ftShooting;
   @ReportField(name = "OVR")
   private Integer overall;
   @ReportField(name = "OLD")
   private Integer overallLessDur;
   @ReportField(name = "OLDPFA")
   private Integer overallLessDurPlusFt;

   //Potentials (Very Low, Low, Average, High, Very High)
   @ReportField(name = "ATHpot")
   private String athleticism_potential;
   @ReportField(name = "SPDpot")
   private String speed_potential;
   @ReportField(name = "REBpot")
   private String rebounding_potential;
   @ReportField(name = "DEFpot")
   private String defense_potential;
   @ReportField(name = "SBpot")
   private String shotBlocking_potential;
   @ReportField(name = "LPpot")
   private String lowPost_potential;
   @ReportField(name = "PERpot")
   private String perimeter_potential;
   @ReportField(name = "BHpot")
   private String ballHandling_potential;
   @ReportField(name = "PASpot")
   private String passing_potential;
   @ReportField(name = "STApot")
   private String stamina_potential;
   @ReportField(name = "DURpot")
   private String durability_potential;
   @ReportField(name = "FTpot")
   private String ftShooting_potential;

   //Calculated Projections
   @ReportField(name = "ATHpro")
   private Integer athleticism_projected;
   @ReportField(name = "SPDpro")
   private Integer speed_projected;
   @ReportField(name = "REBpro")
   private Integer rebounding_projected;
   @ReportField(name = "DEFpro")
   private Integer defense_projected;
   @ReportField(name = "SBpro")
   private Integer shotBlocking_projected;
   @ReportField(name = "LPpro")
   private Integer lowPost_projected;
   @ReportField(name = "PERpro")
   private Integer perimeter_projected;
   @ReportField(name = "BHpro")
   private Integer ballHandling_projected;
   @ReportField(name = "PASpro")
   private Integer passing_projected;
   @ReportField(name = "STApro")
   private Integer stamina_projected;
   @ReportField(name = "DURpro")
   private Integer durability_projected;
   @ReportField(name = "FTpro")
   private Integer ftShooting_projected;
   @ReportField(name = "OVRpro")
   private Integer overall_projected;
   @ReportField(name = "OLDpro")
   private Integer overallLessDur_projected;
   @ReportField(name = "OLDPFTpro")
   private Integer overallLessDurPlusFt_projected;
   @ReportField(name = "COVR")
   private Integer customOverall;
   @ReportField(name = "COVR_LS")
   private Integer customOverallLessShooting;
   @ReportField(name = "Role_Idx")
   private Integer rolePlayerIndex;
   @ReportField(name = "COVR_DEF")
   private Integer customOverallDefenseOnly;
   @ReportField(name = "COVR_OFF")
   private Integer customOverallOffenseOnly;
   @ReportField(name = "COVR_CUR")
   private Integer customCurrentOverall;
   @ReportField(name = "Growth_Idx")
   private Integer growthIndex;

   //Preferences (in relation to my team, very high to very low)
   private String playingTimePref;
   private String distancePref;
   private String successPref;
   private String playStylePref;
   private String offensePref;
   private String defensePref;
   @ReportField(name = "ConferencePref")
   private String confStrengthPref;
   @ReportField(name = "LongevityPref")
   private String coachLongevityPref;
   private String signingPref;
   private Integer netPrefs;
   private Integer grossPrefsPct;
   private Integer coreNetPrefs;
   private Integer coreGrossPrefsPct;

   private String considering;
   private String url;

   public HdRecruitRatings( Integer recruitId ) {
      this.recruitId = recruitId;
      this.url = WisPage.HD_RecruitProfile_Ratings.url() + recruitId;
   }

   public String getScoutingLevel() {
      return scoutingLevel;
   }

   public void setScoutingLevel( String scoutingLevel ) {
      this.scoutingLevel = scoutingLevel;
   }

   public String getPlayingTimePref() {
      return playingTimePref;
   }

   public void setPlayingTimePref( String playingTimePref ) {
      this.playingTimePref = playingTimePref;
   }

   public String getDistancePref() {
      return distancePref;
   }

   public void setDistancePref( String distancePref ) {
      this.distancePref = distancePref;
   }

   public String getSuccessPref() {
      return successPref;
   }

   public void setSuccessPref( String successPref ) {
      this.successPref = successPref;
   }

   public String getPlayStylePref() {
      return playStylePref;
   }

   public void setPlayStylePref( String playStylePref ) {
      this.playStylePref = playStylePref;
   }

   public String getOffensePref() {
      return offensePref;
   }

   public void setOffensePref( String offensePref ) {
      this.offensePref = offensePref;
   }

   public String getDefensePref() {
      return defensePref;
   }

   public void setDefensePref( String defensePref ) {
      this.defensePref = defensePref;
   }

   public String getConfStrengthPref() {
      return confStrengthPref;
   }

   public void setConfStrengthPref( String confStrengthPref ) {
      this.confStrengthPref = confStrengthPref;
   }

   public String getCoachLongevityPref() {
      return coachLongevityPref;
   }

   public void setCoachLongevityPref( String coachLongevityPref ) {
      this.coachLongevityPref = coachLongevityPref;
   }

   public String getSigningPref() {
      return signingPref;
   }

   public void setSigningPref( String signingPref ) {
      this.signingPref = signingPref;
   }

   public Integer getAthleticism() {
      return athleticism;
   }

   public void setAthleticism( Integer athleticism ) {
      this.athleticism = athleticism;
   }

   public Integer getSpeed() {
      return speed;
   }

   public void setSpeed( Integer speed ) {
      this.speed = speed;
   }

   public Integer getRebounding() {
      return rebounding;
   }

   public void setRebounding( Integer rebounding ) {
      this.rebounding = rebounding;
   }

   public Integer getDefense() {
      return defense;
   }

   public void setDefense( Integer defense ) {
      this.defense = defense;
   }

   public Integer getShotBlocking() {
      return shotBlocking;
   }

   public void setShotBlocking( Integer shotBlocking ) {
      this.shotBlocking = shotBlocking;
   }

   public Integer getLowPost() {
      return lowPost;
   }

   public void setLowPost( Integer lowPost ) {
      this.lowPost = lowPost;
   }

   public Integer getPerimeter() {
      return perimeter;
   }

   public void setPerimeter( Integer perimeter ) {
      this.perimeter = perimeter;
   }

   public Integer getBallHandling() {
      return ballHandling;
   }

   public void setBallHandling( Integer ballHandling ) {
      this.ballHandling = ballHandling;
   }

   public Integer getPassing() {
      return passing;
   }

   public void setPassing( Integer passing ) {
      this.passing = passing;
   }

   public Integer getWorkEthic() {
      return workEthic;
   }

   public void setWorkEthic( Integer workEthic ) {
      this.workEthic = workEthic;
   }

   public Integer getStamina() {
      return stamina;
   }

   public void setStamina( Integer stamina ) {
      this.stamina = stamina;
   }

   public Integer getDurability() {
      return durability;
   }

   public void setDurability( Integer durability ) {
      this.durability = durability;
   }

   public Integer getFtShooting() {
      return ftShooting;
   }

   public void setFtShooting( Integer ftShooting ) {
      this.ftShooting = ftShooting;
   }

   public Integer getOverall() {
      return overall;
   }

   public void setOverall( Integer overall ) {
      this.overall = overall;
   }

   public String getAthleticism_potential() {
      return athleticism_potential;
   }

   public void setAthleticism_potential( String athleticism_potential ) {
      this.athleticism_potential = athleticism_potential;
   }

   public String getSpeed_potential() {
      return speed_potential;
   }

   public void setSpeed_potential( String speed_potential ) {
      this.speed_potential = speed_potential;
   }

   public String getRebounding_potential() {
      return rebounding_potential;
   }

   public void setRebounding_potential( String rebounding_potential ) {
      this.rebounding_potential = rebounding_potential;
   }

   public String getDefense_potential() {
      return defense_potential;
   }

   public void setDefense_potential( String defense_potential ) {
      this.defense_potential = defense_potential;
   }

   public String getShotBlocking_potential() {
      return shotBlocking_potential;
   }

   public void setShotBlocking_potential( String shotBlocking_potential ) {
      this.shotBlocking_potential = shotBlocking_potential;
   }

   public String getLowPost_potential() {
      return lowPost_potential;
   }

   public void setLowPost_potential( String lowPost_potential ) {
      this.lowPost_potential = lowPost_potential;
   }

   public String getPerimeter_potential() {
      return perimeter_potential;
   }

   public void setPerimeter_potential( String perimeter_potential ) {
      this.perimeter_potential = perimeter_potential;
   }

   public String getBallHandling_potential() {
      return ballHandling_potential;
   }

   public void setBallHandling_potential( String ballHandling_potential ) {
      this.ballHandling_potential = ballHandling_potential;
   }

   public String getPassing_potential() {
      return passing_potential;
   }

   public void setPassing_potential( String passing_potential ) {
      this.passing_potential = passing_potential;
   }

   public String getStamina_potential() {
      return stamina_potential;
   }

   public void setStamina_potential( String stamina_potential ) {
      this.stamina_potential = stamina_potential;
   }

   public String getDurability_potential() {
      return durability_potential;
   }

   public void setDurability_potential( String durability_potential ) {
      this.durability_potential = durability_potential;
   }

   public String getFtShooting_potential() {
      return ftShooting_potential;
   }

   public void setFtShooting_potential( String ftShooting_potential ) {
      this.ftShooting_potential = ftShooting_potential;
   }

   public Integer getAthleticism_projected() {
      return athleticism_projected;
   }

   public void setAthleticism_projected( Integer athleticism_projected ) {
      this.athleticism_projected = athleticism_projected;
   }

   public Integer getSpeed_projected() {
      return speed_projected;
   }

   public void setSpeed_projected( Integer speed_projected ) {
      this.speed_projected = speed_projected;
   }

   public Integer getRebounding_projected() {
      return rebounding_projected;
   }

   public void setRebounding_projected( Integer rebounding_projected ) {
      this.rebounding_projected = rebounding_projected;
   }

   public Integer getDefense_projected() {
      return defense_projected;
   }

   public void setDefense_projected( Integer defense_projected ) {
      this.defense_projected = defense_projected;
   }

   public Integer getShotBlocking_projected() {
      return shotBlocking_projected;
   }

   public void setShotBlocking_projected( Integer shotBlocking_projected ) {
      this.shotBlocking_projected = shotBlocking_projected;
   }

   public Integer getLowPost_projected() {
      return lowPost_projected;
   }

   public void setLowPost_projected( Integer lowPost_projected ) {
      this.lowPost_projected = lowPost_projected;
   }

   public Integer getPerimeter_projected() {
      return perimeter_projected;
   }

   public void setPerimeter_projected( Integer perimeter_projected ) {
      this.perimeter_projected = perimeter_projected;
   }

   public Integer getBallHandling_projected() {
      return ballHandling_projected;
   }

   public void setBallHandling_projected( Integer ballHandling_projected ) {
      this.ballHandling_projected = ballHandling_projected;
   }

   public Integer getPassing_projected() {
      return passing_projected;
   }

   public void setPassing_projected( Integer passing_projected ) {
      this.passing_projected = passing_projected;
   }

   public Integer getStamina_projected() {
      return stamina_projected;
   }

   public void setStamina_projected( Integer stamina_projected ) {
      this.stamina_projected = stamina_projected;
   }

   public Integer getDurability_projected() {
      return durability_projected;
   }

   public void setDurability_projected( Integer durability_projected ) {
      this.durability_projected = durability_projected;
   }

   public Integer getFtShooting_projected() {
      return ftShooting_projected;
   }

   public void setFtShooting_projected( Integer ftShooting_projected ) {
      this.ftShooting_projected = ftShooting_projected;
   }

   public Integer getOverall_projected() {
      return overall_projected;
   }

   public void setOverall_projected( Integer overall_projected ) {
      this.overall_projected = overall_projected;
   }

   public Integer getDistance() {
      return distance;
   }

   public void setDistance( Integer distance ) {
      this.distance = distance;
   }

   public BigDecimal getCostToMax() {
      return costToMax;
   }

   public void setCostToMax( BigDecimal costToMax ) {
      this.costToMax = costToMax;
   }

   @Override
   public String getRecruitName() {
      return recruitName;
   }

   @Override
   public void setRecruitName( String recruitName ) {
      this.recruitName = recruitName;
   }

   @Override
   public String getPosition() {
      return position;
   }

   @Override
   public void setPosition( String position ) {
      this.position = position;
   }

   @Override
   public String getPositionRank() {
      return positionRank;
   }

   @Override
   public void setPositionRank( String positionRank ) {
      this.positionRank = positionRank;
   }

   @Override
   public String getProjectedDiv() {
      return projectedDiv;
   }

   @Override
   public void setProjectedDiv( String projectedDiv ) {
      this.projectedDiv = projectedDiv;
   }

   @Override
   public String getEligibility() {
      return eligibility;
   }

   @Override
   public void setEligibility( String eligibility ) {
      this.eligibility = eligibility;
   }

   @Override
   public String getYears() {
      return years;
   }

   @Override
   public void setYears( String years ) {
      this.years = years;
   }

   @Override
   public String getCity() {
      return city;
   }

   @Override
   public void setCity( String city ) {
      this.city = city;
   }

   @Override
   public String getSignedWith() {
      return signedWith;
   }

   @Override
   public void setSignedWith( String signedWith ) {
      this.signedWith = signedWith;
   }

   @Override
   public Integer getRecruitId() {
      return recruitId;
   }

   @Override
   public String getUrl() {
      return url;
   }

   public String getOffenseSet() {
      return offenseSet;
   }

   public void setOffenseSet( String offenseSet ) {
      this.offenseSet = offenseSet;
   }

   public String getDefenseSet() {
      return defenseSet;
   }

   public void setDefenseSet( String defenseSet ) {
      this.defenseSet = defenseSet;
   }

   public Integer getOverallLessDur() {
      return overallLessDur;
   }

   public void setOverallLessDur( Integer overallLessDur ) {
      this.overallLessDur = overallLessDur;
   }

   public Integer getOverallLessDur_projected() {
      return overallLessDur_projected;
   }

   public void setOverallLessDur_projected( Integer overallLessDur_projected ) {
      this.overallLessDur_projected = overallLessDur_projected;
   }

   public Integer getOverallLessDurPlusFt() {
      return overallLessDurPlusFt;
   }

   public void setOverallLessDurPlusFt( Integer overallLessDurPlusFt ) {
      this.overallLessDurPlusFt = overallLessDurPlusFt;
   }

   public Integer getOverallLessDurPlusFt_projected() {
      return overallLessDurPlusFt_projected;
   }

   public void setOverallLessDurPlusFt_projected( Integer overallLessDurPlusFt_projected ) {
      this.overallLessDurPlusFt_projected = overallLessDurPlusFt_projected;
   }

   public String getOverallRank() {
      return overallRank;
   }

   public void setOverallRank( String overallRank ) {
      this.overallRank = overallRank;
   }

   public Integer getCustomOverall() {
      return customOverall;
   }

   public void setCustomOverall( Integer customOverall ) {
      this.customOverall = customOverall;
   }

   public Integer getNetPrefs() {
      return netPrefs;
   }

   public void setNetPrefs( Integer netPrefs ) {
      this.netPrefs = netPrefs;
   }

   public void doFinalCalculations( CustomOverallWeights overallWeights ) {
      calculateProjections();
      calculateOveralls();
      calculateCustomCurrentOverall( overallWeights, "customCurrentOverall" );
      calculateCustomOverall( overallWeights, "customOverall" );
      calculateCustomOverallLessShooting( overallWeights );
      calculateCustomOverallDefenseOnly( overallWeights );
      calculateCustomOverallOffenseOnly( overallWeights );
      calculateIndexes();
      calculateMaxCost();
      calculateNetPrefs();
      calculateGrossPrefsPct();
   }

   private void calculateProjections() {
      athleticism_projected = athleticism + getPotentialIncrease( athleticism_potential );
      speed_projected = speed + getPotentialIncrease( speed_potential );
      rebounding_projected = rebounding + getPotentialIncrease( rebounding_potential );
      defense_projected = defense + getPotentialIncrease( defense_potential );
      shotBlocking_projected = shotBlocking + getPotentialIncrease( shotBlocking_potential );
      lowPost_projected = lowPost + getPotentialIncrease( lowPost_potential );
      perimeter_projected = perimeter + getPotentialIncrease( perimeter_potential );
      ballHandling_projected = ballHandling + getPotentialIncrease( ballHandling_potential );
      passing_projected = passing + getPotentialIncrease( passing_potential );
      stamina_projected = stamina + getPotentialIncrease( stamina_potential );
      durability_projected = durability + getPotentialIncrease( durability_potential );
      ftShooting_projected = ftShooting + getPotentialIncrease( ftShooting_potential );
   }

   private int getPotentialIncrease( String potential ) {
      if( "Very High".equals( potential ) ) {
         return 36;
      }
      else if( "High".equals( potential ) ) {
         if( "3".equals( scoutingLevel ) ) {//blend high and very high for SL3
            return 30;
         }
         else {
            return 23;
         }
      }
      else if( "Low".equals( potential ) ) {
         if( "3".equals( scoutingLevel ) ) {//blend low and very low for SL3
            return 3;
         }
         else {
            return 5;
         }
      }
      else if( "Very Low".equals( potential ) ) {
         return 1;
      }
      return 13;//"Average", null, or ""
   }

   private void calculateOveralls() {
      overall = athleticism + speed + rebounding + defense + shotBlocking + lowPost + perimeter + ballHandling + passing + stamina + durability + workEthic;
      overallLessDur = overall - durability;
      overallLessDurPlusFt = overallLessDur + ftShooting;
      overall_projected = athleticism_projected + speed_projected + rebounding_projected + defense_projected + shotBlocking_projected + lowPost_projected + perimeter_projected + ballHandling_projected + passing_projected + stamina_projected + durability_projected + workEthic;
      overallLessDur_projected = overall_projected - durability_projected;
      overallLessDurPlusFt_projected = overallLessDur_projected + ftShooting_projected;
   }

   private void calculateCustomCurrentOverall( CustomOverallWeights weights, String overallFieldName ) {
      final BigDecimal ath = BigDecimal.valueOf( athleticism ).multiply( BigDecimal.valueOf( weights.athleticismWeight ) );
      final BigDecimal spd = BigDecimal.valueOf( speed ).multiply( BigDecimal.valueOf( weights.speedWeight ) );
      final BigDecimal reb = BigDecimal.valueOf( rebounding ).multiply( BigDecimal.valueOf( weights.reboundingWeight ) );
      final BigDecimal def = BigDecimal.valueOf( defense ).multiply( BigDecimal.valueOf( weights.defenseWeight ) );
      final BigDecimal sb = BigDecimal.valueOf( shotBlocking ).multiply( BigDecimal.valueOf( weights.shotBlockingWeight ) );
      final BigDecimal lp = BigDecimal.valueOf( lowPost ).multiply( BigDecimal.valueOf( weights.lowPostWeight ) );
      final BigDecimal per = BigDecimal.valueOf( perimeter ).multiply( BigDecimal.valueOf( weights.perimeterWeight ) );
      final BigDecimal bh = BigDecimal.valueOf( ballHandling ).multiply( BigDecimal.valueOf( weights.ballHandlingWeight ) );
      final BigDecimal pas = BigDecimal.valueOf( passing ).multiply( BigDecimal.valueOf( weights.passingWeight ) );
      final BigDecimal sta = BigDecimal.valueOf( stamina ).multiply( BigDecimal.valueOf( weights.staminaWeight ) );
      final BigDecimal dur = BigDecimal.valueOf( durability ).multiply( BigDecimal.valueOf( weights.durabilityWeight ) );
      final BigDecimal ft = BigDecimal.valueOf( ftShooting ).multiply( BigDecimal.valueOf( weights.ftShootingWeight ) );
      final BigDecimal we = BigDecimal.valueOf( workEthic ).multiply( BigDecimal.valueOf( weights.workEthicWeight ) );
      final BigDecimal all = MathUtl.sum( ath, spd, reb, def, sb, lp, per, bh, pas, sta, dur, ft, we );
      final BigDecimal customOverallDecimal = MathUtl.divide( all, BigDecimal.valueOf( weights.denominator ) ).multiply( BigDecimal.valueOf( 10 ) );
      ReflectionUtl.setField( this, overallFieldName, customOverallDecimal.intValue() );
   }

   private void calculateCustomOverall( CustomOverallWeights weights, String overallFieldName ) {
      final BigDecimal ath = BigDecimal.valueOf( athleticism_projected ).multiply( BigDecimal.valueOf( weights.athleticismWeight ) );
      final BigDecimal spd = BigDecimal.valueOf( speed_projected ).multiply( BigDecimal.valueOf( weights.speedWeight ) );
      final BigDecimal reb = BigDecimal.valueOf( rebounding_projected ).multiply( BigDecimal.valueOf( weights.reboundingWeight ) );
      final BigDecimal def = BigDecimal.valueOf( defense_projected ).multiply( BigDecimal.valueOf( weights.defenseWeight ) );
      final BigDecimal sb = BigDecimal.valueOf( shotBlocking_projected ).multiply( BigDecimal.valueOf( weights.shotBlockingWeight ) );
      final BigDecimal lp = BigDecimal.valueOf( lowPost_projected ).multiply( BigDecimal.valueOf( weights.lowPostWeight ) );
      final BigDecimal per = BigDecimal.valueOf( perimeter_projected ).multiply( BigDecimal.valueOf( weights.perimeterWeight ) );
      final BigDecimal bh = BigDecimal.valueOf( ballHandling_projected ).multiply( BigDecimal.valueOf( weights.ballHandlingWeight ) );
      final BigDecimal pas = BigDecimal.valueOf( passing_projected ).multiply( BigDecimal.valueOf( weights.passingWeight ) );
      final BigDecimal sta = BigDecimal.valueOf( stamina_projected ).multiply( BigDecimal.valueOf( weights.staminaWeight ) );
      final BigDecimal dur = BigDecimal.valueOf( durability_projected ).multiply( BigDecimal.valueOf( weights.durabilityWeight ) );
      final BigDecimal ft = BigDecimal.valueOf( ftShooting_projected ).multiply( BigDecimal.valueOf( weights.ftShootingWeight ) );
      final BigDecimal we = BigDecimal.valueOf( workEthic ).multiply( BigDecimal.valueOf( weights.workEthicWeight ) );
      final BigDecimal all = MathUtl.sum( ath, spd, reb, def, sb, lp, per, bh, pas, sta, dur, ft, we );
      final BigDecimal customOverallDecimal = MathUtl.divide( all, BigDecimal.valueOf( weights.denominator ) ).multiply( BigDecimal.valueOf( 10 ) );
      ReflectionUtl.setField( this, overallFieldName, customOverallDecimal.intValue() );
   }

   private void calculateCustomOverallLessShooting( CustomOverallWeights overallWeights ) {
      final CustomOverallWeights overallLessShootingWeights = new CustomOverallWeights(
            overallWeights.athleticismWeight,
            overallWeights.speedWeight,
            overallWeights.reboundingWeight,
            overallWeights.defenseWeight,
            overallWeights.shotBlockingWeight,
            0,
            0,
            overallWeights.ballHandlingWeight,
            overallWeights.passingWeight,
            overallWeights.staminaWeight,
            overallWeights.durabilityWeight,
            0,
            overallWeights.workEthicWeight );
      calculateCustomOverall( overallLessShootingWeights, "customOverallLessShooting" );
   }

   private void calculateIndexes() {
      rolePlayerIndex = customOverallLessShooting - customOverall;
      growthIndex = customOverall - customCurrentOverall;
   }

   private void calculateCustomOverallDefenseOnly( CustomOverallWeights overallWeights ) {
      final CustomOverallWeights defenseOnlyWeights = new CustomOverallWeights(
            overallWeights.athleticismWeight,
            overallWeights.speedWeight,
            overallWeights.reboundingWeight,
            overallWeights.defenseWeight,
            overallWeights.shotBlockingWeight,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0 );
      calculateCustomOverall( defenseOnlyWeights, "customOverallDefenseOnly" );
   }

   private void calculateCustomOverallOffenseOnly( CustomOverallWeights overallWeights ) {
      final CustomOverallWeights offenseOnlyWeights = new CustomOverallWeights(
            overallWeights.athleticismWeight,
            overallWeights.speedWeight,
            0,
            0,
            0,
            overallWeights.lowPostWeight,
            overallWeights.perimeterWeight,
            overallWeights.ballHandlingWeight,
            overallWeights.passingWeight,
            0,
            0,
            overallWeights.ftShootingWeight,
            0 );
      calculateCustomOverall( offenseOnlyWeights, "customOverallOffenseOnly" );
   }

   private void calculateMaxCost() {
      if( distance != null ) {
         final BigDecimal homeVisitCost = MathUtl.minOf( BigDecimal.valueOf( .3 ).multiply( BigDecimal.valueOf( distance ) ).add( BigDecimal.valueOf( 200 ) ), BigDecimal.valueOf( 1000 ) );
         final BigDecimal campusVisitCost = MathUtl.minOf( BigDecimal.valueOf( .5 ).multiply( BigDecimal.valueOf( distance ) ).add( BigDecimal.valueOf( 400 ) ), BigDecimal.valueOf( 1700 ) );
         costToMax = BigDecimal.valueOf( 20 ).multiply( homeVisitCost ).add( campusVisitCost );
      }
   }

   private List<String> getAllPrefs() {
      return Arrays.asList( playingTimePref, distancePref, successPref, playStylePref, offensePref, defensePref, confStrengthPref, coachLongevityPref );
   }

   private List<String> getCorePrefs() {
      return Arrays.asList( distancePref, playStylePref, offensePref, defensePref );
   }

   private void calculateNetPrefs() {
      netPrefs = doCalcNetPrefs( getAllPrefs() );
      coreNetPrefs = doCalcNetPrefs( getCorePrefs() );
   }

   private int doCalcNetPrefs( final List<String> prefs ) {
      int net = 0;
      for( String pref : prefs ) {
         if( "Very Good".equals( pref ) ) {
            net += 2;
         }
         else if( "Good".equals( pref ) ) {
            net += 1;
         }
         else if( "Bad".equals( pref ) ) {
            net -= 1;
         }
         else if( "Very Bad".equals( pref ) ) {
            net -= 2;
         }
      }
      return net;
   }

   private void calculateGrossPrefsPct() {
      grossPrefsPct = doCalcGrossPrefsPct( getAllPrefs() );
      coreGrossPrefsPct = doCalcGrossPrefsPct( getCorePrefs() );
   }

   private int doCalcGrossPrefsPct( List<String> prefs ) {
      int gross = 0;
      int possible = 0;
      for( String pref : prefs ) {
         if( "Very Good".equals( pref ) ) {
            gross += 4;
            possible += 4;
         }
         else if( "Good".equals( pref ) ) {
            gross += 3;
            possible += 4;
         }
         else if( "Neutral".equals( pref ) ) {
            gross += 2;
            possible += 4;
         }
         else if( "Bad".equals( pref ) ) {
            gross += 1;
            possible += 4;
         }
         else if( "Very Bad".equals( pref ) ) {
            possible += 4;
         }
      }
      if( possible > 0 ) {
         return MathUtl.round( MathUtl.divide( BigDecimal.valueOf( gross ), BigDecimal.valueOf( possible ) ).multiply( BigDecimal.valueOf( 100 ) ), 0 ).toBigInteger().intValue();
      }
      else {
         return 100;
      }
   }

   public static class CustomOverallWeights {
      final Integer athleticismWeight;
      final Integer speedWeight;
      final Integer reboundingWeight;
      final Integer defenseWeight;
      final Integer shotBlockingWeight;
      final Integer lowPostWeight;
      final Integer perimeterWeight;
      final Integer ballHandlingWeight;
      final Integer passingWeight;
      final Integer staminaWeight;
      final Integer durabilityWeight;
      final Integer ftShootingWeight;
      final Integer workEthicWeight;
      final Integer denominator;

      public CustomOverallWeights( Integer athleticismWeight, Integer speedWeight, Integer reboundingWeight, Integer defenseWeight, Integer shotBlockingWeight, Integer lowPostWeight, Integer perimeterWeight, Integer ballHandlingWeight, Integer passingWeight, Integer staminaWeight, Integer durabilityWeight, Integer ftShootingWeight, Integer workEthicWeight ) {
         this.athleticismWeight = athleticismWeight;
         this.speedWeight = speedWeight;
         this.reboundingWeight = reboundingWeight;
         this.defenseWeight = defenseWeight;
         this.shotBlockingWeight = shotBlockingWeight;
         this.lowPostWeight = lowPostWeight;
         this.perimeterWeight = perimeterWeight;
         this.ballHandlingWeight = ballHandlingWeight;
         this.passingWeight = passingWeight;
         this.staminaWeight = staminaWeight;
         this.durabilityWeight = durabilityWeight;
         this.ftShootingWeight = ftShootingWeight;
         this.workEthicWeight = workEthicWeight;
         this.denominator = athleticismWeight + speedWeight + reboundingWeight + defenseWeight + shotBlockingWeight + lowPostWeight + perimeterWeight + ballHandlingWeight + passingWeight + staminaWeight + durabilityWeight + ftShootingWeight + workEthicWeight;
      }
   }

   @Override
   public String getColorCategory() {
      return colorCategory;
   }

   @Override
   public void setColorCategory( String colorCategory ) {
      this.colorCategory = colorCategory;
   }

   public Integer getGrossPrefsPct() {
      return grossPrefsPct;
   }

   public void setGrossPrefsPct( Integer grossPrefsPct ) {
      this.grossPrefsPct = grossPrefsPct;
   }

   public Integer getCustomOverallLessShooting() {
      return customOverallLessShooting;
   }

   public void setCustomOverallLessShooting( Integer customOverallLessShooting ) {
      this.customOverallLessShooting = customOverallLessShooting;
   }

   public String getConsidering() {
      return considering;
   }

   public void setConsidering( String considering ) {
      this.considering = considering;
   }

   public Integer getRolePlayerIndex() {
      return rolePlayerIndex;
   }

   public void setRolePlayerIndex( Integer rolePlayerIndex ) {
      this.rolePlayerIndex = rolePlayerIndex;
   }

   public Integer getCustomOverallDefenseOnly() {
      return customOverallDefenseOnly;
   }

   public void setCustomOverallDefenseOnly( Integer customOverallDefenseOnly ) {
      this.customOverallDefenseOnly = customOverallDefenseOnly;
   }

   public Integer getCustomOverallOffenseOnly() {
      return customOverallOffenseOnly;
   }

   public void setCustomOverallOffenseOnly( Integer customOverallOffenseOnly ) {
      this.customOverallOffenseOnly = customOverallOffenseOnly;
   }

   public Integer getCoreNetPrefs() {
      return coreNetPrefs;
   }

   public void setCoreNetPrefs( Integer coreNetPrefs ) {
      this.coreNetPrefs = coreNetPrefs;
   }

   public Integer getCoreGrossPrefsPct() {
      return coreGrossPrefsPct;
   }

   public void setCoreGrossPrefsPct( Integer coreGrossPrefsPct ) {
      this.coreGrossPrefsPct = coreGrossPrefsPct;
   }

   public Integer getCustomCurrentOverall() {
      return customCurrentOverall;
   }

   public void setCustomCurrentOverall( Integer customCurrentOverall ) {
      this.customCurrentOverall = customCurrentOverall;
   }

   public Integer getGrowthIndex() {
      return growthIndex;
   }

   public void setGrowthIndex( Integer growthIndex ) {
      this.growthIndex = growthIndex;
   }

}
