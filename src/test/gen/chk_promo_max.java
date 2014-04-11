/* Generated on 03-01-2013 12:10:56 PM by SPLParser v0.9 */
package com.choicehotels.cis.spl.gen;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.joda.time.DateTime;import org.joda.time.LocalDate;

import com.choicehotels.cis.spl.AbstractProcedure;
import com.choicehotels.cis.spl.ProcedureException;

public class chk_promo_max extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Integer execute(Integer acct_id, Integer promo_id, Integer max_uses) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 5804 $ | CDATE=$Date: 2004-12-13 04:10:28 -0700 (Mon, 13 Dec 2004) $ ~
         *   chk_promo_max looks at number of times a promotion was
         *   awarded. returns 1 if below max , 0 if at max
         * 
         *         Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer count;
        // If max_uses is null the promotion is unlimited

        if (max_uses == null) {
            return 1;
        }
        // There is a limit check it ignoring the reversed transactions

        count = new chk_promo_use_cnt().execute(promo_id, acct_id);
        if (count >= max_uses) {
            return 0;
        }
        // promotion not valid

        else {
            return 1;
        }
    }

}