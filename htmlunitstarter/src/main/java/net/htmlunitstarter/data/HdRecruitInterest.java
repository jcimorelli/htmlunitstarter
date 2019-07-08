package net.wistools.data;

import net.wistools.nav.WisPage;

public class HdRecruitInterest implements IDataObject, IHdRecruit {
   @ReportTransient
   private Integer recruitId;
   private String recruitName;
   @ReportField(name = "Interest")
   private String interestLevel;
   private String scholarshipOffer;
   private String position;
   private String positionRank;
   private String overallRank;
   @ReportField(name = "Div")
   private String projectedDiv;
   private String eligibility;
   private String years;
   private String city;
   private String signedWith;
   private String url;

   public HdRecruitInterest( Integer recruitId ) {
      this.recruitId = recruitId;
      this.url = WisPage.HD_RecruitProfile_Ratings.url() + recruitId;
   }

   @Override
   public String getRecruitName() {
      return recruitName;
   }

   @Override
   public void setRecruitName( String recruitName ) {
      this.recruitName = recruitName;
   }

   public String getInterestLevel() {
      return interestLevel;
   }

   public void setInterestLevel( String interestLevel ) {
      this.interestLevel = interestLevel;
   }

   public String getScholarshipOffer() {
      return scholarshipOffer;
   }

   public void setScholarshipOffer( String scholarshipOffer ) {
      this.scholarshipOffer = scholarshipOffer;
   }

   @Override
   public String getUrl() {
      return url;
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
   public String getPosition() {
      return position;
   }

   @Override
   public void setPosition( String position ) {
      this.position = position;
   }

   @Override
   public Integer getRecruitId() {
      return recruitId;
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
   public String getPositionRank() {
      return positionRank;
   }

   @Override
   public void setPositionRank( String positionRank ) {
      this.positionRank = positionRank;
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
   public String getProjectedDiv() {
      return projectedDiv;
   }

   @Override
   public void setProjectedDiv( String projectedDiv ) {
      this.projectedDiv = projectedDiv;
   }

   public String getOverallRank() {
      return overallRank;
   }

   public void setOverallRank( String overallRank ) {
      this.overallRank = overallRank;
   }

   @Override
   public String getColorCategory() {
      return null;
   }

   @Override
   public void setColorCategory( String colorCategory ) {}
}
