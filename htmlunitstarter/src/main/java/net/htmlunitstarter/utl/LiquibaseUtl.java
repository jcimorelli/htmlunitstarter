package net.htmlunitstarter.utl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

public abstract class LiquibaseUtl {
   private static final String ChangeLogPackage = "net.htmlunitstarter.db.liquibase";
   private static final String ChangeLogLocation_Prefix = "/net/net.htmlunitstarter/db/liquibase/";
   private static final String ChangeLogLocation_Suffix = ".xml";

   private static Logger logger = Logger.getLogger( LiquibaseUtl.class );

   public static void updateLiquibase( StringBuilder output ) {
      new LiquibaseInstance().update();
   }

   private static List<String> getChangeLogNames() {
      List<String> changeLogNames = null;
      try {
         changeLogNames = ClasspathUtl.getClassNamesFromPackage( ChangeLogPackage );
      }
      catch( IOException | URISyntaxException e ) {
         ValidateUtl.fail( e );
      }
      return changeLogNames;

   }

   private static void closeConnection( Connection connection ) {
      if( connection != null ) {
         try {
            connection.rollback();
            connection.close();
         }
         catch( SQLException e ) {
            logger.info( "Failed to close Liquibase db connection" );
         }
      }
   }

   @Configurable
   public static class LiquibaseInstance {
      @Autowired
      private DataSource datasource;

      public void update() {
         Connection connection = null;
         try {
            connection = datasource.getConnection();
            final JdbcConnection jdbcConnection = new JdbcConnection( connection );
            final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation( jdbcConnection );
            for( String changeLogName : getChangeLogNames() ) {
               final String changeLogFilePath = LiquibaseUtl.class.getResource( ChangeLogLocation_Prefix + changeLogName + ChangeLogLocation_Suffix ).getPath();
               final ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
               final Liquibase liquibase = new Liquibase( changeLogFilePath, resourceAccessor, database );
               liquibase.update( null );
            }
         }
         catch( SQLException | LiquibaseException e ) {
            ValidateUtl.fail( e );
         }
         finally {
            closeConnection( connection );
         }
      }

   }
}
