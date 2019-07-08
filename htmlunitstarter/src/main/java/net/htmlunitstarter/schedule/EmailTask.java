package net.htmlunitstarter.schedule;

import net.htmlunitstarter.utl.EmailUtl;
import net.htmlunitstarter.utl.LoggingUtl;
import net.htmlunitstarter.utl.ValidateUtl;

//Just for proof of concept
@Deprecated
public class EmailTask implements Runnable {

   private String subject;
   private String body;
   private String recipientAddress;

   public EmailTask( String subject, String body, String recipientAddress ) {
      this.subject = subject;
      this.body = body;
      this.recipientAddress = recipientAddress;
   }

   @Override
   public void run() {
      try {
         LoggingUtl.log( "Sending Email..." );
         EmailUtl.sendEmail( subject, body, recipientAddress );
      }
      catch( Exception e ) {
         LoggingUtl.log( "Email Send failed." );
         ValidateUtl.fail( e );
      }
      LoggingUtl.log( "Email Sent." );
   }
}
