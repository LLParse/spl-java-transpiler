/* Generated on 03-01-2013 12:10:57 PM by SPLParser v0.9 */
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

public class round_up extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Double execute(Double amount, Double trans_unit_value) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 5719 $ | CDATE=$Date: 2004-11-30 06:14:49 -0700 (Tue, 30 Nov 2004) $ ~
         * 
         *   Round the given amount up to the nearest program unit.
         * 
         *       Copyright (C) 2004 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        Integer in_amount;
        Integer in_unit_value;
        Double new_amount;
        Double check_amount;
        // set debug file to '/tmp/round_up.trace';

        // trace on;

        new_amount = null;
        check_amount = null;
        if (trans_unit_value == null) {
            throw new ProcedureException(-746, 0, "round_up: 'unit_value' is null.");
        }
        if (trans_unit_value.equals(0)) {
            throw new ProcedureException(-746, 0, "round_up: 'unit_value' is zero.");
        }
        // resolve input into integers for mod()

        in_amount = amount.intValue() * 100;
        in_unit_value = trans_unit_value.intValue() * 100;
        // check if rounding needed

        check_amount = mod(in_amount, in_unit_value) / 100.0;
        if (check_amount > 0.0) {
            new_amount = trunc(amount, 0) + trans_unit_value;
        }
        // yes round up

        else {
            new_amount = amount;
        }
        return new_amount;
    }

}