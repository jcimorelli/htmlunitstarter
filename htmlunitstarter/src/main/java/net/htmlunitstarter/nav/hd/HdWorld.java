package net.wistools.nav.hd;

import java.util.ArrayList;
import java.util.List;

import net.wistools.utl.ValidateUtl;

public enum HdWorld {
   ALLEN("Allen"),
   SMITH("Smith"),
   CRUM("Crum"),
   NAISMITH("Naismith");

   private String worldName;

   private HdWorld( String worldName ) {
      this.worldName = worldName;
   }

   public String getWorldName() {
      return worldName;
   }

   public static HdWorld getByWorldName( String worldName ) {
      for( HdWorld world : HdWorld.values() ) {
         if( world.worldName.equals( worldName.trim() ) ) {
            return world;
         }
      }
      ValidateUtl.fail( "Unknown HD World: '" + worldName + "'" );
      return null;
   }

   public static String[] getWorldNames() {
      final List<String> worldNames = new ArrayList<>();
      for( HdWorld world : values() ) {
         worldNames.add( world.getWorldName() );
      }
      return worldNames.toArray( new String[worldNames.size()] );
   }
}
