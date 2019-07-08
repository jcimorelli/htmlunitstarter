package net.wistools.nav.hd;

public class HdRecruitSearchCriteria {
   public static final String[] ColorCriteria = {
         "Any", "White", "Blue", "Green", "Yellow", "Red"
   };

   public static final String[] ColorSet = {
         "White", "Blue", "Green", "Yellow", "Red"
   };

   public static final String[] Position = {
         "Any", "PG", "SG", "SF", "PF", "C", "PG or SG", "SG or SF", "SF or PF", "PF or C"
   };

   public static final String[] Distance = {
         "Any", "100", "200", "300", "400", "500", "750", "1,000", "1,500", "2,000"
   };

   public static final String[] State = {
         "Any", "Canada", "Mexico", "International", "AK", "AL", "AR", "AZ", "CA",
         "CO", "CT", "DC", "DE", "FL", "GA", "HI", "IA", "ID", "IL", "IN", "KS",
         "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND",
         "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "PR", "RI",
         "SC", "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"
   };

   public static final String[] ProjectedLevel = {
         "Any", "Division I", "Division II", "Division III"
   };

   public static final String[] PlayingPref = {
         "Any", "No Preference", "Wants to Play"
   };

   public static final String[] DistancePref = {
         "Any", "No Preference", "Far From Home", "Near Home"
   };

   public static final String[] StylePref = {
         "Any", "No Preference", "Fast Tempo", "Perimeter Offense", "Paint Offense", "Strong Defense"
   };

   public static final String[] OffensePref = {
         "Any", "No Preference", "Flex", "Motion", "Triangle", "Fast Break"
   };

   public static final String[] DefensePref = {
         "Any", "No Preference", "Zone", "Man to Man", "Fullcourt Press"
   };

   public static final String[] EligibleYears = {
         "Any", "1", "2", "3", "4"
   };

   public static final String[] Eligibility = {
         "Any", "Qualified", "Not Qualified"
   };

   public static final String[] SigningPref = {
         "Any", "Whenever", "Early", "By End Of Period 1", "Late"
   };

   public static final String[] SuccessPref = {
         "Any", "No Preference", "Wants Success", "Wants Rebuild"
   };

   public static final String[] ConferencePref = {
         "Any", "No Preference", "Strong Conference"
   };

   public static final String[] LongevityPref = {
         "Any", "No Preference", "Wants Long-time Coach"
   };

   public static final String[] DecisionStatus = {
         "Any", "Undecided", "Unsigned", "Signed"
   };

   public static final String[] ScoutingLevel = {
         "Any", "1", "2", "3", "4"
   };

   public static final String[] SortBy = {
         "Overall Rating", "Position", "Overall Rank", "Athleticism", "Ball Handling",
         "Defense", "Rebounding", "Durability", "Stamina", "Speed", "Shot Blocking",
         "Passing", "Perimeter", "Low Post", "Work Ethic", "Category", "Distance", "GPA"
   };

   public static final String[] SortDirection = {
         "Descending", "Ascending"
   };

   private String currentColor = ColorCriteria[0];
   private String position = Position[0];
   private String distanceFrom = Distance[0];
   private String distanceTo = Distance[0];
   private String state = State[0];
   private String projectedLevel = ProjectedLevel[0];
   private String playingPref = PlayingPref[0];
   private String distancePref = DistancePref[0];
   private String stylePref = StylePref[0];
   private String offensePref = OffensePref[0];
   private String defensePref = DefensePref[0];
   private String eligibleYears = EligibleYears[0];
   private String eligibility = Eligibility[0];
   private String signingPref = SigningPref[0];
   private String successPref = SuccessPref[0];
   private String conferencePref = ConferencePref[0];
   private String longevityPref = LongevityPref[0];
   private String decisionStatus = DecisionStatus[0];
   private String scoutingLevel = ScoutingLevel[0];
   private String sortBy = SortBy[0];
   private String sortDirection = SortDirection[0];

   public String getPosition() {
      return position;
   }

   public void setPosition( String position ) {
      this.position = position;
   }

   public String getDistanceFrom() {
      return distanceFrom;
   }

   public void setDistanceFrom( String distanceFrom ) {
      this.distanceFrom = distanceFrom;
   }

   public String getDistanceTo() {
      return distanceTo;
   }

   public void setDistanceTo( String distanceTo ) {
      this.distanceTo = distanceTo;
   }

   public String getState() {
      return state;
   }

   public void setState( String state ) {
      this.state = state;
   }

   public String getProjectedLevel() {
      return projectedLevel;
   }

   public void setProjectedLevel( String projectedLevel ) {
      this.projectedLevel = projectedLevel;
   }

   public String getPlayingPref() {
      return playingPref;
   }

   public void setPlayingPref( String playingPref ) {
      this.playingPref = playingPref;
   }

   public String getDistancePref() {
      return distancePref;
   }

   public void setDistancePref( String distancePref ) {
      this.distancePref = distancePref;
   }

   public String getStylePref() {
      return stylePref;
   }

   public void setStylePref( String stylePref ) {
      this.stylePref = stylePref;
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

   public String getEligibleYears() {
      return eligibleYears;
   }

   public void setEligibleYears( String eligibleYears ) {
      this.eligibleYears = eligibleYears;
   }

   public String getEligibility() {
      return eligibility;
   }

   public void setEligibility( String eligibility ) {
      this.eligibility = eligibility;
   }

   public String getSigningPref() {
      return signingPref;
   }

   public void setSigningPref( String signingPref ) {
      this.signingPref = signingPref;
   }

   public String getSuccessPref() {
      return successPref;
   }

   public void setSuccessPref( String successPref ) {
      this.successPref = successPref;
   }

   public String getConferencePref() {
      return conferencePref;
   }

   public void setConferencePref( String conferencePref ) {
      this.conferencePref = conferencePref;
   }

   public String getLongevityPref() {
      return longevityPref;
   }

   public void setLongevityPref( String longevityPref ) {
      this.longevityPref = longevityPref;
   }

   public String getDecisionStatus() {
      return decisionStatus;
   }

   public void setDecisionStatus( String decisionStatus ) {
      this.decisionStatus = decisionStatus;
   }

   public String getScoutingLevel() {
      return scoutingLevel;
   }

   public void setScoutingLevel( String scoutingLevel ) {
      this.scoutingLevel = scoutingLevel;
   }

   public String getSortBy() {
      return sortBy;
   }

   public void setSortBy( String sortBy ) {
      this.sortBy = sortBy;
   }

   public String getSortDirection() {
      return sortDirection;
   }

   public void setSortDirection( String sortDirection ) {
      this.sortDirection = sortDirection;
   }

   public String getCurrentColor() {
      return currentColor;
   }

   public void setCurrentColor( String currentColor ) {
      this.currentColor = currentColor;
   }

}
