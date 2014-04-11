package com.xerxes500.transpiler;

/**
 * An exception which parallels the constructs of a SQL exception generated within
 * an Informix 11.7 stored procedure.
 * 
 * @author James Oliver
 * @version $Id$
 */
public class ProcedureException extends Exception {

   private static final long serialVersionUID = -5306492052745092409L;
   private Integer sqlError = null;
   private Integer isamError = null;
   private String message = null;

   /**
    * 
    * @param sqlError
    */
   public ProcedureException(Integer sqlError) {
      this.sqlError = sqlError;
   }

   /**
    * 
    * @param sqlError
    * @param isamError
    */
   public ProcedureException(Integer sqlError, Integer isamError) {
      this.sqlError = sqlError;
      this.isamError = isamError;
   }

   /**
    * 
    * @param sqlError
    * @param isamError
    * @param message
    */
   public ProcedureException(Integer sqlError, Integer isamError, String message) {
      this.sqlError = sqlError;
      this.isamError = isamError;
      this.message = message;
   }
   
   public Integer getSQLError() {
      return sqlError;
   }
   
   public Integer getISAMError() {
      return isamError;
   }
   
   public String getMessage() {
      return message;
   }

}
