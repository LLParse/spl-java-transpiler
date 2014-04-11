package com.dontexist.morals;

import java.sql.SQLException;

import com.choicehotels.gen.ProcedureException;
import com.choicehotels.gen.add_acct_offer;

/**
 * 
 * <p>
 * Copyright 2013 Choice Hotels International, Inc.  All Rights Reserved.
 * 
 * @author James Oliver
 * @version $Id$
 */
public class AddAcctOfferTest {

   /**
    * @param args
    */
   public static void main(String[] args) {
      add_acct_offer test = new add_acct_offer();
      try {
         test.execute("GP", "XI1212O", "JXG121210");
      }
      catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (ProcedureException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
