package com.dontexist.morals;

import com.choicehotels.gen.ProcedureException;
import com.choicehotels.gen.add_call_ticket;

/**
 * 
 * <p>
 * Copyright 2013 Choice Hotels International, Inc.  All Rights Reserved.
 * 
 * @author James Oliver
 * @version $Id$
 */
public class AddCallTicketTest {

   public static void main(String[] args) {
         try {
            add_call_ticket act = new add_call_ticket();
            Integer cust_call_id = act.execute(238417, 238417, "C", "L", 19, "spl-translate test", null, null, 578, "spl-translate test");
            System.out.println("Created with id: " + cust_call_id);
         }
         catch (ProcedureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
   }
}
