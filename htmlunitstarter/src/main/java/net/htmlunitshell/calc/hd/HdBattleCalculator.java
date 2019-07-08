package net.wistools.calc.hd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

public class HdBattleCalculator {

   public static final Map<String, Integer> PRESTIGE_VALUE_MAP = ImmutableMap.<String, Integer> builder()
         .put( "A+", 12 ).put( "A", 11 ).put( "A-", 10 )
         .put( "B+", 9 ).put( "B", 8 ).put( "B-", 7 )
         .put( "C+", 6 ).put( "C", 5 ).put( "C-", 4 )
         .put( "D+", 3 ).put( "D", 2 ).put( "D-", 1 )
         .build();

   public static class Assumptions {
      public static final Integer APs_Per_HV = 30;
      public static final Integer HVs_Per_CV = 3;
      public static final Double PValue_HV_Multiplier = 1.08d;//How much is 1/3 of a letter grade prestige advantage worth (as a multiplier)?  IE if every 1/3 letter grade increases your effor by 8%, the value should be 1.08.
      public static final Double NetPref_Per_PValue = 0.67d;//Under the assumption that 1 Very Good is worth 2 Good prefs, and that 1 Good and 1 Bad cancel each other out...How many 1/3 letter grades in prestige is 1 Good pref worth.  IE if the value is 1, then a B with a Very Good is equal to a B with a Good and also an A- with neutral prefs. 
      public static final Integer HVs_Per_Start = 7;
      public static final Integer HVs_Per_25m = 5;
      public static final Integer HVs_Per_20m = 4;
      public static final Integer HVs_Per_15m = 3;
      public static final Integer HVs_Per_10m = 2;

      //Percent thresholds for determining how far behind the battle leader means which interest level
      public static final Double VeryHigh_High_Threshold = .2d;
      public static final Double High_Moderate_Threshold = .5d;
      public static final Double Moderate_Low_Threshold = .8d;
      public static final Double Low_VeryLow_Threshold = 1.1d;
   }

   private List<HdBattleTeam> teams = new ArrayList<>();

   public HdBattleCalculator( String[] teamParams, String[] prestigeParams, String[] netprefsParams,
         String[] apsParams, String[] cvParams, String[] hvsParams, String[] startParams, String[] minutesParams ) {
      for( int i = 0; i < teamParams.length; i++ ) {
         teams.add( new HdBattleTeam( teamParams[i], prestigeParams[i], netprefsParams[i], apsParams[i], cvParams[i],
               hvsParams[i], startParams[i], minutesParams[i] ) );
      }
   }

   public String result() {
      final HdBattleResult result = calcBattle();
      return new Gson().toJson( result );
   }

   private HdBattleResult calcBattle() {
      final Double leaderAAHVs = teams.stream().map( t -> t.getAbsoluteAggregateHVs() ).reduce( ( a, b ) -> Math.max( a, b ) ).get();
      for( HdBattleTeam team : teams ) {
         team.setAbsoluteAggregateHVsBehind( leaderAAHVs - team.getAbsoluteAggregateHVs() );
         team.setPercentAAHVsBehind( team.getAbsoluteAggregateHVsBehind() / leaderAAHVs );
      }
      teams.forEach( t -> t.calculateOddsIntermediateValues() );
      teams.sort( ( t1, t2 ) -> t2.getVacuumOdds().compareTo( t1.getVacuumOdds() ) );
      final Double secondBestVacuumOdds = teams.get( 1 ).getVacuumOdds();
      Double totalUnadjustedOdds = 0d;
      for( HdBattleTeam team : teams ) {
         final Double unadjustedOdds = team.getAbsoluteAggregateHVsBehind() == 0d ? 1d - secondBestVacuumOdds : team.getVacuumOdds();
         team.setUnadjustedOdds( unadjustedOdds );
         totalUnadjustedOdds += unadjustedOdds;
      }
      for( HdBattleTeam team : teams ) {
         team.setAdjustedOdds( team.getUnadjustedOdds() / totalUnadjustedOdds );
      }
      return new HdBattleResult( teams );
   }
}
