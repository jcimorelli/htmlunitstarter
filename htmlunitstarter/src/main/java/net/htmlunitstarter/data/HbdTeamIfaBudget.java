package net.wistools.data;

import java.math.BigDecimal;

import net.wistools.utl.MathUtl;

public class HbdTeamIfaBudget implements IDataObject {
   @ReportTransient
   private Integer teamId;
   @ReportTransient
   private boolean myTeam;
   private String fullTeamName;
   private String coach;
   private BigDecimal coachBudget = BigDecimal.ZERO;
   private BigDecimal coachSpending = BigDecimal.ZERO;
   private BigDecimal freeCoachBudget = BigDecimal.ZERO;
   private BigDecimal potentialCoachTransfer = BigDecimal.ZERO;
   private BigDecimal playerBudget = BigDecimal.ZERO;
   private BigDecimal playerSpending = BigDecimal.ZERO;
   private BigDecimal freePlayerBudget = BigDecimal.ZERO;
   private BigDecimal potentialPlayerTransfer = BigDecimal.ZERO;
   private BigDecimal prospectBudget = BigDecimal.ZERO;
   private BigDecimal grossPotentialProspectBudget = BigDecimal.ZERO;
   private BigDecimal ifaSpending = BigDecimal.ZERO;
   private BigDecimal draftSpending = BigDecimal.ZERO;
   private BigDecimal netPotentialProspectBudget = BigDecimal.ZERO;

   public HbdTeamIfaBudget( Integer teamId ) {
      this.teamId = teamId;
   }

   public void calculateTotals() {
      freeCoachBudget = coachBudget.subtract( coachSpending );
      freePlayerBudget = playerBudget.subtract( playerSpending );
      final BigDecimal coachPreTransferMoney = freeCoachBudget.subtract( freeCoachBudget.remainder( BigDecimal.valueOf( 2000000 ) ) );
      final BigDecimal playerPreTransferMoney = freePlayerBudget.subtract( freePlayerBudget.remainder( BigDecimal.valueOf( 2000000 ) ) );
      potentialCoachTransfer = MathUtl.divide( coachPreTransferMoney, BigDecimal.valueOf( 2 ) );
      potentialPlayerTransfer = MathUtl.divide( playerPreTransferMoney, BigDecimal.valueOf( 2 ) );
      grossPotentialProspectBudget = prospectBudget.add( potentialCoachTransfer ).add( potentialPlayerTransfer );
      netPotentialProspectBudget = grossPotentialProspectBudget.subtract( ifaSpending ).subtract( draftSpending );
   }

   public BigDecimal getProspectBudget() {
      return prospectBudget;
   }

   public void setProspectBudget( BigDecimal prospectBudget ) {
      this.prospectBudget = prospectBudget;
   }

   public BigDecimal getIfaSpending() {
      return ifaSpending;
   }

   public void setIfaSpending( BigDecimal ifaSpending ) {
      this.ifaSpending = ifaSpending;
   }

   public BigDecimal getDraftSpending() {
      return draftSpending;
   }

   public void setDraftSpending( BigDecimal draftSpending ) {
      this.draftSpending = draftSpending;
   }

   public Integer getTeamId() {
      return teamId;
   }

   public String getFullTeamName() {
      return fullTeamName;
   }

   public void setFullTeamName( String fullTeamName ) {
      this.fullTeamName = fullTeamName;
   }

   public String getCoach() {
      return coach;
   }

   public void setCoach( String coach ) {
      this.coach = coach;
   }

   public BigDecimal getPlayerBudget() {
      return playerBudget;
   }

   public void setPlayerBudget( BigDecimal playerBudget ) {
      this.playerBudget = playerBudget;
   }

   public BigDecimal getCoachBudget() {
      return coachBudget;
   }

   public void setCoachBudget( BigDecimal coachBudget ) {
      this.coachBudget = coachBudget;
   }

   public BigDecimal getCoachSpending() {
      return coachSpending;
   }

   public void setCoachSpending( BigDecimal coachSpending ) {
      this.coachSpending = coachSpending;
   }

   public BigDecimal getPlayerSpending() {
      return playerSpending;
   }

   public void setPlayerSpending( BigDecimal playerSpending ) {
      this.playerSpending = playerSpending;
   }

   public void addIfaSpending( BigDecimal ifaSpending ) {
      this.ifaSpending = this.ifaSpending.add( ifaSpending );
   }

   public void addDraftSpending( BigDecimal draftSpending ) {
      this.draftSpending = this.draftSpending.add( draftSpending );
   }

   public void addCoachSpending( BigDecimal coachSpending ) {
      this.coachSpending = this.coachSpending.add( coachSpending );
   }

   public boolean isMyTeam() {
      return myTeam;
   }

   public void setMyTeam( boolean myTeam ) {
      this.myTeam = myTeam;
   }

   public BigDecimal getPotentialCoachTransfer() {
      return potentialCoachTransfer;
   }

   public void setPotentialCoachTransfer( BigDecimal potentialCoachTransfer ) {
      this.potentialCoachTransfer = potentialCoachTransfer;
   }

   public BigDecimal getPotentialPlayerTransfer() {
      return potentialPlayerTransfer;
   }

   public void setPotentialPlayerTransfer( BigDecimal potentialPlayerTransfer ) {
      this.potentialPlayerTransfer = potentialPlayerTransfer;
   }

   public BigDecimal getGrossPotentialProspectBudget() {
      return grossPotentialProspectBudget;
   }

   public void setGrossPotentialProspectBudget( BigDecimal grossPotentialProspectBudget ) {
      this.grossPotentialProspectBudget = grossPotentialProspectBudget;
   }

   public BigDecimal getNetPotentialProspectBudget() {
      return netPotentialProspectBudget;
   }

   public void setNetPotentialProspectBudget( BigDecimal netPotentialProspectBudget ) {
      this.netPotentialProspectBudget = netPotentialProspectBudget;
   }

   public BigDecimal getFreeCoachBudget() {
      return freeCoachBudget;
   }

   public void setFreeCoachBudget( BigDecimal freeCoachBudget ) {
      this.freeCoachBudget = freeCoachBudget;
   }

   public BigDecimal getFreePlayerBudget() {
      return freePlayerBudget;
   }

   public void setFreePlayerBudget( BigDecimal freePlayerBudget ) {
      this.freePlayerBudget = freePlayerBudget;
   }

}
