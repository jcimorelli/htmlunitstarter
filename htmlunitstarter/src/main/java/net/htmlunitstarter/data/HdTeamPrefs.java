package net.wistools.data;

public class HdTeamPrefs {

   public static enum PrefValue {
      VeryGood, Good, Neutral, Bad, VeryBad, Null;
   }

   //Offense
   private PrefValue flexOffense = PrefValue.Null;
   private PrefValue motionOffense = PrefValue.Null;
   private PrefValue triangleOffense = PrefValue.Null;
   private PrefValue fastbreakOffense = PrefValue.Null;

   //Defense
   private PrefValue manDefense = PrefValue.Null;
   private PrefValue zoneDefense = PrefValue.Null;
   private PrefValue pressDefense = PrefValue.Null;

   //PlayStyle
   private PrefValue strongDefense = PrefValue.Null;
   private PrefValue perimeterOffense = PrefValue.Null;
   private PrefValue paintOffense = PrefValue.Null;
   private PrefValue fastTempo = PrefValue.Null;

   //Success
   private PrefValue wantsSuccess = PrefValue.Null;
   private PrefValue wantsRebuild = PrefValue.Null;

   //Conference
   private PrefValue strongConference = PrefValue.Null;

   //Longevity
   private PrefValue wantsLongTimeCoach = PrefValue.Null;

   public PrefValue getFlexOffense() {
      return flexOffense;
   }

   public void setFlexOffense( PrefValue flexOffense ) {
      this.flexOffense = flexOffense;
   }

   public PrefValue getMotionOffense() {
      return motionOffense;
   }

   public void setMotionOffense( PrefValue motionOffense ) {
      this.motionOffense = motionOffense;
   }

   public PrefValue getTriangleOffense() {
      return triangleOffense;
   }

   public void setTriangleOffense( PrefValue triangleOffense ) {
      this.triangleOffense = triangleOffense;
   }

   public PrefValue getFastbreakOffense() {
      return fastbreakOffense;
   }

   public void setFastbreakOffense( PrefValue fastbreakOffense ) {
      this.fastbreakOffense = fastbreakOffense;
   }

   public PrefValue getManDefense() {
      return manDefense;
   }

   public void setManDefense( PrefValue manDefense ) {
      this.manDefense = manDefense;
   }

   public PrefValue getZoneDefense() {
      return zoneDefense;
   }

   public void setZoneDefense( PrefValue zoneDefense ) {
      this.zoneDefense = zoneDefense;
   }

   public PrefValue getPressDefense() {
      return pressDefense;
   }

   public void setPressDefense( PrefValue pressDefense ) {
      this.pressDefense = pressDefense;
   }

   public PrefValue getStrongDefense() {
      return strongDefense;
   }

   public void setStrongDefense( PrefValue strongDefense ) {
      this.strongDefense = strongDefense;
   }

   public PrefValue getPerimeterOffense() {
      return perimeterOffense;
   }

   public void setPerimeterOffense( PrefValue perimeterOffense ) {
      this.perimeterOffense = perimeterOffense;
   }

   public PrefValue getPaintOffense() {
      return paintOffense;
   }

   public void setPaintOffense( PrefValue paintOffense ) {
      this.paintOffense = paintOffense;
   }

   public PrefValue getFastTempo() {
      return fastTempo;
   }

   public void setFastTempo( PrefValue fastTempo ) {
      this.fastTempo = fastTempo;
   }

   public PrefValue getWantsSuccess() {
      return wantsSuccess;
   }

   public void setWantsSuccess( PrefValue wantsSuccess ) {
      this.wantsSuccess = wantsSuccess;
   }

   public PrefValue getWantsRebuild() {
      return wantsRebuild;
   }

   public void setWantsRebuild( PrefValue wantsRebuild ) {
      this.wantsRebuild = wantsRebuild;
   }

   public PrefValue getStrongConference() {
      return strongConference;
   }

   public void setStrongConference( PrefValue strongConference ) {
      this.strongConference = strongConference;
   }

   public PrefValue getWantsLongTimeCoach() {
      return wantsLongTimeCoach;
   }

   public void setWantsLongTimeCoach( PrefValue wantsLongTimeCoach ) {
      this.wantsLongTimeCoach = wantsLongTimeCoach;
   }

}
