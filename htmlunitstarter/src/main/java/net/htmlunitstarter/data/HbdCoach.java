package net.wistools.data;

import java.math.BigDecimal;

import net.wistools.nav.WisPage;

public class HbdCoach implements IDataObject {
   @ReportTransient
   private Integer coachId;
   @ReportField(name = "Name")
   private String coachName;
   private Integer age;
   @ReportField(name = "Exp")
   private Integer experience;

   @ReportField(name = "Level")
   private String levelDemand;
   @ReportField(name = "Role")
   private String roleDemand;
   @ReportField(name = "Salary")
   private BigDecimal salaryDemand;
   @ReportField(name = "Interest")
   private String interestedFranchises;

   @ReportField(name = "HittingValue")
   private Integer hittingCoachAbility;
   @ReportField(name = "PitchingValue")
   private Integer pitchingCoachAbility;
   @ReportField(name = "FieldingValue")
   private Integer fieldingCoachAbility;
   @ReportField(name = "BenchValue")
   private Integer benchCoachAbility;
   @ReportField(name = "BaseValue")
   private Integer baseCoachAbility;

   private Integer hittingIq;
   private Integer pitchingIq;
   private Integer fieldingIq;
   private Integer baserunningIq;
   private Integer patience;
   private Integer strategy;
   private Integer discipline;
   private Integer loyalty;

   private String url;

   public HbdCoach( Integer coachId ) {
      this.coachId = coachId;
      this.url = WisPage.HBD_CoachNegotiations.url() + coachId;
   }

   @Override
   public boolean equals( Object obj ) {
      return obj instanceof HbdCoach && ( ( HbdCoach )obj ).coachId != null && ( ( HbdCoach )obj ).coachId.equals( coachId );
   }

   @Override
   public String toString() {
      return coachName;
   }

   public String getUrl() {
      return url;
   }

   public void setCoachId( Integer coachId ) {
      this.coachId = coachId;
      this.url = WisPage.HBD_CoachProfile.url() + coachId;
   }

   public String getCoachName() {
      return coachName;
   }

   public void setCoachName( String coachName ) {
      this.coachName = coachName;
   }

   public Integer getCoachId() {
      return coachId;
   }

   public Integer getAge() {
      return age;
   }

   public void setAge( Integer age ) {
      this.age = age;
   }

   public Integer getExperience() {
      return experience;
   }

   public void setExperience( Integer experience ) {
      this.experience = experience;
   }

   public Integer getHittingIq() {
      return hittingIq;
   }

   public void setHittingIq( Integer hittingIq ) {
      this.hittingIq = hittingIq;
   }

   public Integer getPitchingIq() {
      return pitchingIq;
   }

   public void setPitchingIq( Integer pitchingIq ) {
      this.pitchingIq = pitchingIq;
   }

   public Integer getFieldingIq() {
      return fieldingIq;
   }

   public void setFieldingIq( Integer fieldingIq ) {
      this.fieldingIq = fieldingIq;
   }

   public Integer getBaserunningIq() {
      return baserunningIq;
   }

   public void setBaserunningIq( Integer baserunningIq ) {
      this.baserunningIq = baserunningIq;
   }

   public Integer getPatience() {
      return patience;
   }

   public void setPatience( Integer patience ) {
      this.patience = patience;
   }

   public Integer getStrategy() {
      return strategy;
   }

   public void setStrategy( Integer strategy ) {
      this.strategy = strategy;
   }

   public Integer getDiscipline() {
      return discipline;
   }

   public void setDiscipline( Integer discipline ) {
      this.discipline = discipline;
   }

   public Integer getLoyalty() {
      return loyalty;
   }

   public void setLoyalty( Integer loyalty ) {
      this.loyalty = loyalty;
   }

   public BigDecimal getSalaryDemand() {
      return salaryDemand;
   }

   public void setSalaryDemand( BigDecimal salaryDemand ) {
      this.salaryDemand = salaryDemand;
   }

   public String getRoleDemand() {
      return roleDemand;
   }

   public void setRoleDemand( String roleDemand ) {
      this.roleDemand = roleDemand;
   }

   public String getLevelDemand() {
      return levelDemand;
   }

   public void setLevelDemand( String levelDemand ) {
      this.levelDemand = levelDemand;
   }

   public Integer getHittingCoachAbility() {
      return hittingCoachAbility;
   }

   public void setHittingCoachAbility( Integer hittingCoachAbility ) {
      this.hittingCoachAbility = hittingCoachAbility;
   }

   public Integer getPitchingCoachAbility() {
      return pitchingCoachAbility;
   }

   public void setPitchingCoachAbility( Integer pitchingCoachAbility ) {
      this.pitchingCoachAbility = pitchingCoachAbility;
   }

   public Integer getFieldingCoachAbility() {
      return fieldingCoachAbility;
   }

   public void setFieldingCoachAbility( Integer fieldingCoachAbility ) {
      this.fieldingCoachAbility = fieldingCoachAbility;
   }

   public Integer getBenchCoachAbility() {
      return benchCoachAbility;
   }

   public void setBenchCoachAbility( Integer benchCoachAbility ) {
      this.benchCoachAbility = benchCoachAbility;
   }

   public Integer getBaseCoachAbility() {
      return baseCoachAbility;
   }

   public void setBaseCoachAbility( Integer baseCoachAbility ) {
      this.baseCoachAbility = baseCoachAbility;
   }

   public String getInterestedFranchises() {
      return interestedFranchises;
   }

   public void setInterestedFranchises( String interestedFranchises ) {
      this.interestedFranchises = interestedFranchises;
   }

}
