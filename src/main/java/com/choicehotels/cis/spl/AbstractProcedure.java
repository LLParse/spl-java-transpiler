package com.choicehotels.cis.spl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 * Encapsulates connection logic as well as Informix aggregate and special functions.
 * <p>
 * Copyright 2012 Choice Hotels International, Inc.  All Rights Reserved.
 * 
 * @author James Oliver
 * @version $Id$
 */
public abstract class AbstractProcedure {

   private static Logger logger = Logger.getLogger(AbstractProcedure.class.getName());
   //private static DBConnectionManager   dbConnMgr = DBConnectionManager.getInstance();
   private Connection  conn         = null;
   protected boolean   trace        = false;
   protected String    debugFile    = "c:/tmp/spl.log";

   protected Integer   rowsAffected = null;
   protected ResultSet rs           = null;
   
   protected boolean testMode = true;

   public AbstractProcedure() {
      String tm = System.getProperty("test.mode");
      if (tm != null) {
         testMode = Boolean.valueOf(tm);
      }
   }

   protected Integer dbinfo(String option) throws SQLException {
      Integer result = 0;

      /*
       * The 'sqlca.sqlerrd1' option returns a single integer that provides the last serial value that
       *  is inserted into a table. To ensure valid results, use this option immediately following a 
       *  singleton INSERT statement that inserts a single row with a serial value into a table.
       */
      if (option.equals("sqlca.sqlerrd1")) {
         if (rs != null && rs.next()) {
            result = rs.getInt(1);
         }
      }
      /*
       * The 'sqlca.sqlerrd2' option returns a single integer that provides the number of rows that 
       * SELECT, INSERT, DELETE, UPDATE, EXECUTE PROCEDURE, and EXECUTE FUNCTION statements processed. 
       * To ensure valid results, use this option after SELECT, EXECUTE PROCEDURE, and EXECUTE FUNCTION 
       * statements have completed executing. In addition, to ensure valid results when you use this 
       * option within cursors, make sure that all rows are fetched before the cursors are closed.
       */
      else if (option.equals("sqlca.sqlerrd2")) {

         // INSERT, UPDATE, and DELETE take precedence since there will always be
         // a result set after a SQL statement
         if (rowsAffected != null) {
            result = rowsAffected;
         }
         // May lead to performance problems for large result sets
         else if (rs != null) {
            try {
               rs.last();
               result = rs.getRow();
               rs.beforeFirst();
            }
            catch (SQLException e) {
               e.printStackTrace();
            }
         }

      }
      /*
       * The 'sessionid' option of the DBINFO function returns the session ID of your current session. 
       * When a client application makes a connection to the database server, the database server starts 
       * a session with the client and assigns a session ID for the client. The session ID serves as a 
       * unique identifier for a given connection between a client and a database server.
       */
      else if (option.equals("sessionid")) {
         PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT DBINFO('sessionid') AS sid"
               + " FROM systables"
               + " WHERE tabname = 'systables';");
         ResultSet rs = pstmt.executeQuery();
         result = rs.getInt(1);
      }

      return result;
   }

   protected Integer length(String str) {
      return str.length();
   }

   /**
    * SUBSTR Function:<br>
    * |--SUBSTR--(--source_string--,--start_position--)--|
    */
   protected String substr(String str, Integer beginIndex) {
      return str.substring(beginIndex);
   }

   /**
    * SUBSTR Function:<br>
    * |--SUBSTR--(--source_string--,--start_position---,--length---)--|
    */
   protected String substr(String str, Integer beginIndex, Integer length) {
      Integer endIndex;
      if ((beginIndex + length) > str.length()) {
         endIndex = str.length() - 1;
      }
      else {
         endIndex = beginIndex + length;
      }

      return str.substring(beginIndex, endIndex);
   }

   /**
    * SPL builtin substring is 1-based, we need to translate to java 0-based
    * @param str
    * @param beginIndex
    * @param endIndex
    * @return
    */
   protected String substring(String str, Integer beginIndex, Integer endIndex) {
      return str.substring(--beginIndex, --endIndex);
   }

   /**
    * The TRUNC function resembles the ROUND function, but truncates (rather than rounds to the nearest 
    * whole number) any portion of its first argument that is smaller than the least significant digit 
    * or time unit within the precision that its second argument specifies.
    * 
    * For numeric expressions, TRUNC replaces with zero any digits less than the specified precision.   
    */
   protected Double trunc(Double value) {
      return trunc(value, 0);
   }

   protected Double trunc(Double value, Integer precision) {
      value = value * Math.pow(10, precision);
      value = Math.floor(value);
      value = value / Math.pow(10, precision);
      return value;
   }

   /**
    * The MOD function takes as arguments two real number operands, and returns the remainder from integer 
    * division of the integer part of the first argument (the dividend) by the integer part of the second 
    * argument (the divisor). The value returned is an INT data type (or INT8 for remainders outside the 
    * range of INT). The quotient and any fractional part of the remainder are discarded. The divisor 
    * cannot be 0. Thus, MOD (x,y) returns y (modulo x). Make sure that any variable that receives the 
    * result is of a data type that can store the returned value.
    */
   protected Integer mod(Double dividend, Double divisor) {
      return dividend.intValue() % divisor.intValue();
   }
   
   protected Integer mod(Integer dividend, Integer divisor) {
      return dividend % divisor;
   }

   /**
    * The ABS function returns the absolute value of its numeric argument, returning the same data type 
    * as its argument.
    * 
    * Our scope only requires input parameters of Integer type.
    */
   protected int abs(int value) {
      return Math.abs(value);
   }
   
   protected double abs(double value) {
      return Math.abs(value);
   }
   
   protected <T> T nvl(T o1, T o2) {
      if (o1 != null)
         return o1;
      else
         return o2;
   }

   /**
    * The TRACE statement generates output that is sent to the file that the SET DEBUG FILE TO statement 
    * specifies. Tracing writes to the debug file the current values of the following program objects:
    * <ul>
    * <li>SPL variables</li>
    * <li>Routine arguments</li>
    * <li>Return values</li>
    * <li>SQL error codes</li>
    * <li>ISAM error codes</li>
    * </ul>
    * <p>
    * The output of each executed TRACE statement appears on a separate line.
    * <p>
    * If you use the TRACE statement before you specify a DEBUG file to contain the output, an error is generated.
    * <p>
    * Any routine that the SPL routine calls inherits the trace state. That is, a called routine (on the 
    * same database server) assumes the same trace state (ON, OFF, or PROCEDURE) as the calling routine. The 
    * called routine can set its own trace state, but that state is not passed back to the calling routine.
    * <p>
    * A routine that is executed on a remote database server does not inherit the trace state.
    * 
    * @param command
    * @throws ProcedureException
    */
   protected void trace(String command) throws ProcedureException {
      if (command.equalsIgnoreCase("on")) {
         trace = true;
      }
      else if (command.equalsIgnoreCase("off")) {
         trace = false;
      }
      else if (command.equalsIgnoreCase("procedure")) {
         // TODO unimplemented
      }
      else {
         throw new ProcedureException(-746, 0, "Invalid trace command!");
      }
   }

   /**
    * Use the SET DEBUG FILE statement to identify the file that is to receive the runtime trace output 
    * of an SPL routine.
    * <p>
    * This statement is an extension to the ANSI/ISO standard for SQL.
    */
   protected void setDebugFile(String debugFile) {
      this.debugFile = "c:/" + debugFile;
   }

   protected String trim(String value) {
      return value.trim();
   }

   protected String upper(String value) {
      return value.toUpperCase();
   }

   /*
    * SQL execution methods
    */
   protected PreparedStatement prepareStatement(String sql) throws SQLException {
      if (testMode)
         conn.setAutoCommit(false);
      
      rs = null;
      rowsAffected = null;
      return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
   }

   protected PreparedStatement prepareInsert(String sql) throws SQLException {
      if (testMode)
         conn.setAutoCommit(false);
      
      rs = null;
      rowsAffected = null;
      return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
   }

   protected void executeUpdate(PreparedStatement pstmt) throws SQLException {
      rowsAffected = pstmt.executeUpdate();
      rs = pstmt.getGeneratedKeys();
      if (testMode) {
         conn.rollback();
         logger.info("Connection rolled back");
      }
   }

   protected ResultSet executeQuery(PreparedStatement pstmt) throws SQLException {
      rs = pstmt.executeQuery();
      return rs;
   }

   /*
    * Transaction management methods
    */
   protected void begin() {
      try {
         conn.setAutoCommit(false);
      }
      catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   protected void commit() {
      if (testMode) {
         try {
            conn.rollback();
         }
         catch (SQLException e) {
            e.printStackTrace();
         }
         return;
      }
      
      try {
         conn.commit();
         conn.setAutoCommit(true);
      }
      catch (SQLException e) {
         e.printStackTrace();
      }
   }

   protected void rollback() {
      if (testMode) {
         try {
            conn.rollback();
         }
         catch (SQLException e) {
            e.printStackTrace();
         }
         return;
      }
      
      try {
         conn.rollback();
         conn.setAutoCommit(true);
      }
      catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   protected void setHoldability(int holdability) {
      try {
         conn.setHoldability(holdability);
      }
      catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   protected DateTime date(DateTime dateTime) {
      return new DateTime(dateTime.toDate());
   }
   
   protected DateTime date(String date) {
      DateTime result = null;
      try {
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         result = new DateTime(sdf.parse(date));
      }
      catch (ParseException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return result;
   }
   
   protected int year(DateTime dateTime) {
      return dateTime.getYear();
   }
   
   protected DateTime mdy(int month, int day, int year) {
      return new DateTime(year, month, day, 0, 0);
   }

}
