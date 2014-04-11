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

public class elig_srp extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(String recog_cd, String srp_code) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 6374 $ | CDATE=$Date: 2005-03-07 06:42:24 -0700 (Mon, 07 Mar 2005) $ ~
         *   The SRP logic is as follows:
         *     null = eligible
         *     present but not in recog_srp = not eligible,
         *     present and in recog_srp and eligible ind is 'N' = not eligible
         *     present and in recog_srp and eligible ind is 'Y' = eligible
         * 
         *   
         *       Copyright (C) 2003 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        String eligible;
        eligible = null;
        return "Y";
    }

}