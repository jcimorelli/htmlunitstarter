package net.wistools.calc.hd;

import java.util.ArrayList;
import java.util.List;

import net.wistools.calc.hd.HdBattleCalculator.Assumptions;

public class HdBattleResult {
   private List<TeamResult> teams = new ArrayList<>();

   public HdBattleResult( List<HdBattleTeam> battleTeams ) {
      battleTeams.forEach( bt -> teams.add( new TeamResult( bt ) ) );
   }

   public List<TeamResult> getTeams() {
      return teams;
   }

   public void setTeams( List<TeamResult> teams ) {
      this.teams = teams;
   }

   public static class TeamResult {
      private String name;
      private String interestLevel;
      private Integer signingOdds;

      public TeamResult( HdBattleTeam battleTeam ) {
         this.name = battleTeam.getName();
         if( battleTeam.getPercentAAHVsBehind() <= Assumptions.VeryHigh_High_Threshold ) {
            this.interestLevel = "Very High";
         }
         else if( battleTeam.getPercentAAHVsBehind() <= Assumptions.High_Moderate_Threshold ) {
            this.interestLevel = "High";
         }
         else if( battleTeam.getPercentAAHVsBehind() <= Assumptions.Moderate_Low_Threshold ) {
            this.interestLevel = "Moderate";
         }
         else if( battleTeam.getPercentAAHVsBehind() <= Assumptions.Low_VeryLow_Threshold ) {
            this.interestLevel = "Low";
         }
         else {
            this.interestLevel = "Very Low";
         }
         this.signingOdds = ( int )Math.round( 100d * battleTeam.getAdjustedOdds() );
      }

      public String getName() {
         return name;
      }

      public void setName( String name ) {
         this.name = name;
      }

      public String getInterestLevel() {
         return interestLevel;
      }

      public void setInterestLevel( String interestLevel ) {
         this.interestLevel = interestLevel;
      }

      public Integer getSigningOdds() {
         return signingOdds;
      }

      public void setSigningOdds( Integer signingOdds ) {
         this.signingOdds = signingOdds;
      }
   }
}
