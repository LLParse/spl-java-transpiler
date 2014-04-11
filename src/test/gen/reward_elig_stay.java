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

public class reward_elig_stay extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public String execute(String recog_cd, Integer prop_id, String stay_type, String cxl_flag, String srp_code, String denial) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 24157 $ | CDATE=$Date: 2009-12-10 11:18:50 -0700 (Thu, 10 Dec 2009) $ ~
         * 
         *   Check reward eligibility for a stay. Returns 'Y' if eligible for reward, 'N' 
         *   if not. The following are checked:
         * 
         *   Must not be a denial.
         *   The type of stay, must be 'N'ormal or 'F'olio.
         *   Must not be cancelled.
         *   The SRP, if present must be reward eligible. 
         *   The property against the recognition program.
         */
        // set debug file to '/tmp/reward_elig_stay.trace';

        // trace on;

        // Check the denial indicator

        if (denial.equals("Y")) {
            return "N";
        }
        // Check stay type

        if (!stay_type.equals("N") && !stay_type.equals("F")) {
            return "N";
        }
        // Check if cancelled

        if (!cxl_flag.equals("S")) {
            if (cxl_flag.equals("X") || cxl_flag.equals("N") && !srp_code.equals("SADV") && !srp_code.equals("LADV")) {
                return "N";
            }
        }
        // Check the SRP

        if (new elig_srp().execute(recog_cd, srp_code).equals("N")) {
            return "N";
        }
        // All checks passed

        return "Y";
    }

}