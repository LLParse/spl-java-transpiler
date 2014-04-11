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

public class convert_currency extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Double from_conv_rate, Double to_conv_rate, Double amount) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 7752 $ | CDATE=$Date: 2005-09-13 12:54:08 -0700 (Tue, 13 Sep 2005) $ ~
         * 
         *   Convert the specified amount from one currency to another.
         * 
         *       Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Double new_amount;
        new_amount = null;
        // convert amount

        if (to_conv_rate <= 0.0 || to_conv_rate == null) {
            throw new ProcedureException(-746, 0, "convert_currency: Invalid destination rate.");
        }
        if (from_conv_rate <= 0.0 || from_conv_rate == null) {
            throw new ProcedureException(-746, 0, "convert_currency: Invalid source rate.");
        }
        new_amount = amount * to_conv_rate / from_conv_rate;
        return new_amount;
    }

}