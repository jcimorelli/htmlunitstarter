package net.htmlunitstarter.utl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.htmlunitstarter.config.HtmlUnitStarterProperties;

//This needs to be activated for Gmail to work: (https://www.google.com/settings/security/lesssecureapps)
public class EmailUtl {

   public static void sendEmail( String subject, String bodyText, String recipient ) {
      sendEmail( subject, bodyText, recipient, false );
   }

   public static void sendEmail( String subject, String bodyText, String[] recipients ) {
      sendEmail( subject, bodyText, recipients, false );
   }

   public static void sendEmail( String subject, String bodyText, String recipient, File attachment ) {
      sendEmail( subject, bodyText, recipient, attachment, false );
   }

   public static void sendEmail( String subject, String bodyText, String[] recipients, File attachment ) {
      sendEmail( subject, bodyText, recipients, attachment, false );
   }

   public static void sendEmail( String subject, String bodyText, String recipient, File[] attachments ) {
      sendEmail( subject, bodyText, recipient, attachments, false );
   }

   public static void sendEmail( String subject, String bodyText, String recipient, boolean copyAdmin ) {
      sendEmail( subject, bodyText, new String[]{ recipient }, copyAdmin );
   }

   public static void sendEmail( String subject, String bodyText, String[] recipients, boolean copyAdmin ) {
      sendEmail( subject, bodyText, Arrays.asList( recipients ), new File[]{}, copyAdmin );
   }

   public static void sendEmail( String subject, String bodyText, String recipient, File attachment, boolean copyAdmin ) {
      sendEmail( subject, bodyText, Arrays.asList( recipient ), new File[]{ attachment }, copyAdmin );
   }

   public static void sendEmail( String subject, String bodyText, String[] recipients, File attachment, boolean copyAdmin ) {
      sendEmail( subject, bodyText, Arrays.asList( recipients ), new File[]{ attachment }, copyAdmin );
   }

   public static void sendEmail( String subject, String bodyText, String recipient, File[] attachments, boolean copyAdmin ) {
      sendEmail( subject, bodyText, Arrays.asList( recipient ), attachments, copyAdmin );
   }

   public static void sendEmail( String subject, String bodyText, List<String> recipients, File[] attachments, boolean copyAdmin ) {

      ValidateUtl.nullCheck( subject, "Email Subject cannot be null." );
      ValidateUtl.nullCheck( bodyText, "Email Body cannot be null." );
      ValidateUtl.check( recipients == null || recipients.size() == 0, "Need at least one Email Recipient." );

      if( copyAdmin && !recipients.contains( HtmlUnitStarterProperties.getAdminEmail() ) ) {
         recipients.add( HtmlUnitStarterProperties.getAdminEmail() );
      }

      final String username = HtmlUnitStarterProperties.getSmtpUsername();
      final String password = HtmlUnitStarterProperties.getSmtpPassword();
      final String host = HtmlUnitStarterProperties.getSmtpHost();
      final String port = HtmlUnitStarterProperties.getSmtpPort();

      final Session mailSession = setupMailSession( username, password, host, port );
      try {
         final Message message = new MimeMessage( mailSession );
         setMessageMetaData( message, subject, recipients, username );
         setMessageContent( message, bodyText, attachments );
         Transport.send( message );
      }
      catch( MessagingException e ) {
         ValidateUtl.fail( e );
      }
   }

   private static Session setupMailSession( final String username, final String password, final String host, final String port ) {
      final Properties props = new Properties();
      props.put( "mail.smtp.auth", "true" );
      props.put( "mail.smtp.starttls.enable", "true" );
      props.put( "mail.smtp.host", host );
      props.put( "mail.smtp.port", port );

      final Session session = Session.getInstance( props, new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication( username, password );
         }
      } );
      return session;
   }

   private static void setMessageMetaData( Message message, String subject, List<String> recipients, final String username ) throws MessagingException, AddressException {
      message.setFrom( new InternetAddress( username ) );
      for( String recipient : recipients ) {
         message.addRecipients( Message.RecipientType.TO, InternetAddress.parse( recipient ) );
      }
      message.setSubject( subject );
   }

   private static void setMessageContent( Message message, String bodyText, File[] attachments ) throws MessagingException {
      final Multipart content = new MimeMultipart();
      final BodyPart textBodyPart = new MimeBodyPart();
      textBodyPart.setText( bodyText );
      content.addBodyPart( textBodyPart );
      for( File file : attachments ) {
         final BodyPart fileBodyPart = new MimeBodyPart();
         final DataSource source = new FileDataSource( file );
         fileBodyPart.setDataHandler( new DataHandler( source ) );
         fileBodyPart.setFileName( file.getName() );
         content.addBodyPart( fileBodyPart );
      }
      message.setContent( content );
   }

   public static String getNamePart( String email ) {
      return email != null ? email.substring( 0, email.indexOf( "@" ) ) : "";
   }
}
