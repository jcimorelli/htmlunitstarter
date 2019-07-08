package net.wistools.nav.hbd;

import java.util.ArrayList;
import java.util.List;

import net.wistools.utl.ValidateUtl;

public enum HbdWorld {
   CLEMENTE("Clemente"),
   CLEMENS("Clemens*"),
   MICKEY("7Mickey7"),
   SAYHEY("Say Hey");

   private String worldName;

   private HbdWorld( String worldName ) {
      this.worldName = worldName;
   }

   public String getWorldName() {
      return worldName;
   }

   public static HbdWorld getByWorldName( String worldName ) {
      for( HbdWorld world : HbdWorld.values() ) {
         if( world.worldName.equals( worldName.trim() ) ) {
            return world;
         }
      }
      ValidateUtl.fail( "Unknown HBD World: '" + worldName + "'" );
      return null;
   }

   public static String[] getWorldNames() {
      final List<String> worldNames = new ArrayList<>();
      for( HbdWorld world : values() ) {
         worldNames.add( world.getWorldName() );
      }
      return worldNames.toArray( new String[worldNames.size()] );
   }
}
