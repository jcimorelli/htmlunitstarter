package net.wistools.data;

import net.wistools.nav.WisElement;
import net.wistools.nav.WisPage;
import net.wistools.nav.WistoolsNavigator;

import com.gargoylesoftware.htmlunit.html.DomElement;

public class HdPlayerRatings implements IDataObject, Comparable<HdPlayerRatings> {

   @ReportTransient
   private Integer playerId;
   private String name;
   private String position;
   private String year;
   private String team;
   private String divisionAndCity;

   private Integer athleticism;
   private Integer speed;
   private Integer rebounding;
   private Integer defense;
   private Integer shotBlocking;
   private Integer lowPost;
   private Integer perimeter;
   private Integer ballHandling;
   private Integer passing;
   private Integer workEthic;
   private Integer stamina;
   private Integer durability;
   private String ftShooting;
   private Integer overall;

   private Integer athleticismChange;
   private Integer speedChange;
   private Integer reboundingChange;
   private Integer defenseChange;
   private Integer shotBlockingChange;
   private Integer lowPostChange;
   private Integer perimeterChange;
   private Integer ballHandlingChange;
   private Integer passingChange;
   private Integer workEthicChange;
   private Integer staminaChange;
   private Integer durabilityChange;
   private Integer overallChange;

   private String flexIq;
   private String motionIq;
   private String triangleIq;
   private String fastbreakIq;
   private String manToManIq;
   private String zoneIq;
   private String pressIq;

   @ReportTransient
   private boolean graduating = false;

   private String url;

   public HdPlayerRatings( Integer playerId ) {
      this.playerId = playerId;
      this.url = WisPage.HD_PlayerProfile_Ratings.url() + playerId;
   }

   public void populateCurrentRatings( WistoolsNavigator nav ) {
      nav.loadPage( WisPage.HD_PlayerProfile_Ratings, String.valueOf( playerId ) );
      final DomElement nameSpan = nav.currentPage().getElementById( WisElement.ID_PlayerName );
      name = nameSpan.getTextContent().trim();
      final DomElement positionSpan = nav.currentPage().getElementById( WisElement.ID_PlayerPosition );
      position = positionSpan.getTextContent().trim();
      final DomElement athleticismElement = nav.currentPage().getElementById( WisElement.ID_AthleticismRating );
      athleticism = Integer.parseInt( athleticismElement.getTextContent().trim() );
      final DomElement speedElement = nav.currentPage().getElementById( WisElement.ID_SpeedRating );
      speed = Integer.parseInt( speedElement.getTextContent().trim() );
      final DomElement reboundingElement = nav.currentPage().getElementById( WisElement.ID_ReboundingRating );
      rebounding = Integer.parseInt( reboundingElement.getTextContent().trim() );
      final DomElement defenseElement = nav.currentPage().getElementById( WisElement.ID_DefenseRating );
      defense = Integer.parseInt( defenseElement.getTextContent().trim() );
      final DomElement shotBlockingElement = nav.currentPage().getElementById( WisElement.ID_ShotBlockingRating );
      shotBlocking = Integer.parseInt( shotBlockingElement.getTextContent().trim() );
      final DomElement lowPostElement = nav.currentPage().getElementById( WisElement.ID_LowPostRating );
      lowPost = Integer.parseInt( lowPostElement.getTextContent().trim() );
      final DomElement perimeterElement = nav.currentPage().getElementById( WisElement.ID_PerimeterRating );
      perimeter = Integer.parseInt( perimeterElement.getTextContent().trim() );
      final DomElement ballHandlingElement = nav.currentPage().getElementById( WisElement.ID_BallHandlingRating );
      ballHandling = Integer.parseInt( ballHandlingElement.getTextContent().trim() );
      final DomElement passingElement = nav.currentPage().getElementById( WisElement.ID_PassingRating );
      passing = Integer.parseInt( passingElement.getTextContent().trim() );
      final DomElement workEthicElement = ( ( DomElement )passingElement.getParentNode() ).getNextElementSibling().getFirstElementChild().getNextElementSibling();
      workEthic = Integer.parseInt( workEthicElement.getTextContent().trim() );
      final DomElement staminaElement = nav.currentPage().getElementById( WisElement.ID_StaminaRating );
      stamina = Integer.parseInt( staminaElement.getTextContent().trim() );
      final DomElement durabilityElement = nav.currentPage().getElementById( WisElement.ID_DurabilityRating );
      durability = Integer.parseInt( durabilityElement.getTextContent().trim() );
      final DomElement ftShootingElement = nav.currentPage().getElementById( WisElement.ID_FTShootingRating );
      ftShooting = ftShootingElement.getTextContent().trim();
      final DomElement overallElement = nav.currentPage().getElementById( WisElement.ID_OverallRating );
      overall = Integer.parseInt( overallElement.getTextContent().trim() );

      flexIq = nav.currentPage().getElementById( WisElement.ID_FlexIQ ).getTextContent().trim();
      motionIq = nav.currentPage().getElementById( WisElement.ID_MotionIQ ).getTextContent().trim();
      triangleIq = nav.currentPage().getElementById( WisElement.ID_TriangleIQ ).getTextContent().trim();
      fastbreakIq = nav.currentPage().getElementById( WisElement.ID_FastbreakIQ ).getTextContent().trim();
      manToManIq = nav.currentPage().getElementById( WisElement.ID_ManToManIQ ).getTextContent().trim();
      zoneIq = nav.currentPage().getElementById( WisElement.ID_ZoneIQ ).getTextContent().trim();
      pressIq = nav.currentPage().getElementById( WisElement.ID_PressIQ ).getTextContent().trim();

      year = nav.currentPage().getElementById( WisElement.ID_PlayerYear ).getTextContent().trim();
      if( "Sr/5".equals( year ) ) {
         graduating = true;
      }
      else if( "Sr.".equals( year ) ) {
         nav.loadPage( WisPage.HD_PlayerProfile_Stats, String.valueOf( playerId ) );
         if( !nav.currentPage().asText().contains( "Academic Non-Qualifier" ) ) {
            graduating = true;
         }
      }
   }

   @Override
   public String toString() {
      return name;
   }

   @Override
   public int compareTo( HdPlayerRatings otherPlayer ) {
      return overall.compareTo( otherPlayer.overall );
   }

   public String getName() {
      return name;
   }

   public void setName( String name ) {
      this.name = name;
   }

   public Integer getPlayerId() {
      return playerId;
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

   public String getFtShooting() {
      return ftShooting;
   }

   public void setFtShooting( String ftShooting ) {
      this.ftShooting = ftShooting;
   }

   public Integer getOverall() {
      return overall;
   }

   public void setOverall( Integer overall ) {
      this.overall = overall;
   }

   public Integer getAthleticismChange() {
      return athleticismChange;
   }

   public void setAthleticismChange( Integer athleticismChange ) {
      this.athleticismChange = athleticismChange;
   }

   public Integer getSpeedChange() {
      return speedChange;
   }

   public void setSpeedChange( Integer speedChange ) {
      this.speedChange = speedChange;
   }

   public Integer getReboundingChange() {
      return reboundingChange;
   }

   public void setReboundingChange( Integer reboundingChange ) {
      this.reboundingChange = reboundingChange;
   }

   public Integer getDefenseChange() {
      return defenseChange;
   }

   public void setDefenseChange( Integer defenseChange ) {
      this.defenseChange = defenseChange;
   }

   public Integer getShotBlockingChange() {
      return shotBlockingChange;
   }

   public void setShotBlockingChange( Integer shotBlockingChange ) {
      this.shotBlockingChange = shotBlockingChange;
   }

   public Integer getLowPostChange() {
      return lowPostChange;
   }

   public void setLowPostChange( Integer lowPostChange ) {
      this.lowPostChange = lowPostChange;
   }

   public Integer getPerimeterChange() {
      return perimeterChange;
   }

   public void setPerimeterChange( Integer perimeterChange ) {
      this.perimeterChange = perimeterChange;
   }

   public Integer getBallHandlingChange() {
      return ballHandlingChange;
   }

   public void setBallHandlingChange( Integer ballHandlingChange ) {
      this.ballHandlingChange = ballHandlingChange;
   }

   public Integer getPassingChange() {
      return passingChange;
   }

   public void setPassingChange( Integer passingChange ) {
      this.passingChange = passingChange;
   }

   public Integer getWorkEthicChange() {
      return workEthicChange;
   }

   public void setWorkEthicChange( Integer workEthicChange ) {
      this.workEthicChange = workEthicChange;
   }

   public Integer getStaminaChange() {
      return staminaChange;
   }

   public void setStaminaChange( Integer staminaChange ) {
      this.staminaChange = staminaChange;
   }

   public Integer getDurabilityChange() {
      return durabilityChange;
   }

   public void setDurabilityChange( Integer durabilityChange ) {
      this.durabilityChange = durabilityChange;
   }

   public String getFlexIq() {
      return flexIq;
   }

   public void setFlexIq( String flexIq ) {
      this.flexIq = flexIq;
   }

   public String getMotionIq() {
      return motionIq;
   }

   public void setMotionIq( String motionIq ) {
      this.motionIq = motionIq;
   }

   public String getTriangleIq() {
      return triangleIq;
   }

   public void setTriangleIq( String triangleIq ) {
      this.triangleIq = triangleIq;
   }

   public String getFastbreakIq() {
      return fastbreakIq;
   }

   public void setFastbreakIq( String fastbreakIq ) {
      this.fastbreakIq = fastbreakIq;
   }

   public String getManToManIq() {
      return manToManIq;
   }

   public void setManToManIq( String manToManIq ) {
      this.manToManIq = manToManIq;
   }

   public String getZoneIq() {
      return zoneIq;
   }

   public void setZoneIq( String zoneIq ) {
      this.zoneIq = zoneIq;
   }

   public String getPressIq() {
      return pressIq;
   }

   public void setPressIq( String pressIq ) {
      this.pressIq = pressIq;
   }

   public String getUrl() {
      return url;
   }

   public String getPosition() {
      return position;
   }

   public void setPosition( String position ) {
      this.position = position;
   }

   public String getTeam() {
      return team;
   }

   public void setTeam( String team ) {
      this.team = team;
   }

   public String getDivisionAndCity() {
      return divisionAndCity;
   }

   public void setDivisionAndCity( String divisionAndCity ) {
      this.divisionAndCity = divisionAndCity;
   }

   public Integer getOverallChange() {
      return overallChange;
   }

   public void setOverallChange( Integer overallChange ) {
      this.overallChange = overallChange;
   }

   public boolean isGraduating() {
      return graduating;
   }

   public void setGraduating( boolean graduating ) {
      this.graduating = graduating;
   }

   public String getYear() {
      return year;
   }

   public void setYear( String year ) {
      this.year = year;
   }

}
