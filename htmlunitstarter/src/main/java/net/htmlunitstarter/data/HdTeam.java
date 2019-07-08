package net.wistools.data;

import java.util.List;

import net.wistools.nav.WisPage;

public class HdTeam implements IDataObject {
   @ReportTransient
   private Integer teamId;
   private String teamName;
   private String conference;
   private String division;
   private String city;
   private String prestige;
   private String homecourt;
   private String record;
   private String rank;
   private String rpiRank;
   private String sosRank;
   private Integer departingSeniors = 0;
   private Integer walkons = 0;
   @ReportField(name = "LikelyEEs")
   private Integer likelyEarlyEntries = 0;
   @ReportField(name = "Cuts")
   private Integer playersCut = 0;
   @ReportField(name = "Departures")
   private Integer totalDepartures = 0;
   @ReportField(name = "OriginalOpenings")
   private Integer originalOpenings = 0;
   @ReportField(name = "CurrentOpenings")
   private Integer currentOpenings = 0;
   private Integer signings = 0;
   @ReportField(name = "ActualEEs")
   private Integer actualEarlyEntries = 0;
   private String coach;
   private List<String> availableScheduleSpots;
   private String url;
   @ReportTransient
   private HdTeamPrefs prefs = new HdTeamPrefs();

   public HdTeam( Integer teamId ) {
      this.teamId = teamId;
      this.url = WisPage.HD_TeamProfile_Roster.url() + teamId;
   }

   public HdTeam( String teamName ) {
      this.teamName = teamName;
   }

   @Override
   public boolean equals( Object obj ) {
      return obj instanceof HdTeam && ( ( HdTeam )obj ).teamId != null && ( ( HdTeam )obj ).teamId.equals( teamId );
   }

   @Override
   public String toString() {
      return teamName;
   }

   public String getTeamName() {
      return teamName;
   }

   public void setTeamName( String teamName ) {
      this.teamName = teamName;
   }

   public String getConference() {
      return conference;
   }

   public void setConference( String conference ) {
      this.conference = conference;
   }

   public String getRecord() {
      return record;
   }

   public void setRecord( String record ) {
      this.record = record;
   }

   public Integer getDepartingSeniors() {
      return departingSeniors;
   }

   public void setDepartingSeniors( Integer departingSeniors ) {
      this.departingSeniors = departingSeniors;
   }

   public Integer getWalkons() {
      return walkons;
   }

   public void setWalkons( Integer walkons ) {
      this.walkons = walkons;
   }

   public Integer getTeamId() {
      return teamId;
   }

   public String getUrl() {
      return url;
   }

   public Integer getLikelyEarlyEntries() {
      return likelyEarlyEntries;
   }

   public void setLikelyEarlyEntries( Integer likelyEarlyEntries ) {
      this.likelyEarlyEntries = likelyEarlyEntries;
   }

   public String getPrestige() {
      return prestige;
   }

   public void setPrestige( String prestige ) {
      this.prestige = prestige;
   }

   public void setTeamId( Integer teamId ) {
      this.teamId = teamId;
      this.url = WisPage.HD_TeamProfile_Ratings.url() + teamId;
   }

   public String getRpiRank() {
      return rpiRank;
   }

   public void setRpiRank( String rpiRank ) {
      this.rpiRank = rpiRank;
   }

   public String getCoach() {
      return coach;
   }

   public void setCoach( String coach ) {
      this.coach = coach;
   }

   public String getSosRank() {
      return sosRank;
   }

   public void setSosRank( String sosRank ) {
      this.sosRank = sosRank;
   }

   public String getDivision() {
      return division;
   }

   public void setDivision( String division ) {
      this.division = division;
   }

   public String getCity() {
      return city;
   }

   public void setCity( String city ) {
      this.city = city;
   }

   public String getHomecourt() {
      return homecourt;
   }

   public void setHomecourt( String homecourt ) {
      this.homecourt = homecourt;
   }

   public String getRank() {
      return rank;
   }

   public void setRank( String rank ) {
      this.rank = rank;
   }

   public Integer getPlayersCut() {
      return playersCut;
   }

   public void setPlayersCut( Integer playersCut ) {
      this.playersCut = playersCut;
   }

   public List<String> getAvailableScheduleSpots() {
      return availableScheduleSpots;
   }

   public void setAvailableScheduleSpots( List<String> availableScheduleSpots ) {
      this.availableScheduleSpots = availableScheduleSpots;
   }

   public Integer getOriginalOpenings() {
      return originalOpenings;
   }

   public void setTotalOpenings( Integer openings ) {
      this.originalOpenings = openings;
   }

   public Integer getTotalDepartures() {
      return totalDepartures;
   }

   public void setTotalDepartures( Integer totalDepartures ) {
      this.totalDepartures = totalDepartures;
   }

   public HdTeamPrefs getPrefs() {
      return prefs;
   }

   public Integer getCurrentOpenings() {
      return currentOpenings;
   }

   public void setCurrentOpenings( Integer currentOpenings ) {
      this.currentOpenings = currentOpenings;
   }

   public Integer getActualEarlyEntries() {
      return actualEarlyEntries;
   }

   public void setActualEarlyEntries( Integer actualEarlyEntries ) {
      this.actualEarlyEntries = actualEarlyEntries;
   }

   public Integer getSignings() {
      return signings;
   }

   public void setSignings( Integer signings ) {
      this.signings = signings;
   }

   public void setOriginalOpenings( Integer originalOpenings ) {
      this.originalOpenings = originalOpenings;
   }

   public void doFinalCalculations() {
      originalOpenings = departingSeniors + walkons;
      actualEarlyEntries = currentOpenings == null || signings == null ? 0 : currentOpenings + signings - originalOpenings - playersCut;
      totalDepartures = departingSeniors + playersCut + actualEarlyEntries;
   }

}
