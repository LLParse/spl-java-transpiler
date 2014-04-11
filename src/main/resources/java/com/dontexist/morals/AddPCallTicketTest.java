package com.dontexist.morals;

import java.sql.SQLException;

import com.choicehotels.gen.ProcedureException;
import com.choicehotels.gen.add_pcall_ticket;

/**
 * 
 * <p>
 * Copyright 2013 Choice Hotels International, Inc.  All Rights Reserved.
 * 
 * @author James Oliver
 * @version $Id$
 */
public class AddPCallTicketTest {

   public static void main(String[] args) {
      try {
         add_pcall_ticket add = new add_pcall_ticket();
         Integer prop_call_id = add.execute(19656, "O", "H", 36, "spl-translate testing", null, null, "GP", 523, "spl-translate testing", null, "James", "Oliver", "6029534546", null, null);
         System.out.println("Created with id: " + prop_call_id);
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
