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

public class redeem_points extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(Integer acct_id, Integer entry_id, Integer award_id, String express_mail) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 8487 $ | CDATE=$Date: 2005-11-08 09:40:41 -0700 (Tue, 08 Nov 2005) $ ~
         * 
         *  Process one acount adjustment transaction. 
         *  Assumption is that 'BEGIN WORK' is called prior to this
         *  procedure and 'ROLLBACK/COMMIT WORK' after depending on result. Returns
         *  points_trans_id and redeem_tran_id on success.
         * 
         * 
         * 	Copyright (C) 2001 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        // Data from new records/calculations

        Integer acct_trans_id;
        Integer detail_id;
        Integer redemption_id;
        String external_pgm;
        String express_mail_ind;
        Integer redeem_amt;
        //- account table

        String acct_status;
        Integer sql_error;
        Integer isam_error;
        String error_data;
        try {
            begin();
            // Initialize variables

            acct_trans_id = null;
            detail_id = null;
            redemption_id = null;
            external_pgm = null;
            acct_status = null;
            express_mail_ind = null;
            redeem_amt = null;
            // set debug file to '/tmp/redeem_points.trace';

            // trace on;

            //----------------------------------------------------------

            // First determine that the member is at an 'A'ctive status

            //----------------------------------------------------------

            acct_status = new get_acct_status().execute(acct_id);
            if (acct_status != "A") {
                throw new ProcedureException(-746, 0, "redeem_points: Account status is not 'A'ctive.");
            }
            //----------------------------------------------------------

            // Redemption can only be made for internal partners.

            //----------------------------------------------------------

            external_pgm = new get_pgm_type().execute(acct_id);
            if (!external_pgm.equals("N")) {
                throw new ProcedureException(-746, 0, "redeem_points: Redemption valid only for internal accounts.");
            }
            //----------------------------------------------------------

            // Express mail selection is required.

            // Check if express mail is available for award.  If not but 

            // the request is yes, reject the redemption.

            //----------------------------------------------------------

            if (!express_mail.equals("Y") && !express_mail.equals("N")) {
                throw new ProcedureException(-746, 0, "redeem_points: Express_mail must be set to 'Y' or 'N'");
            }
            PreparedStatement pstmt1 = prepareStatement(
                      "select award.express_mail"
                    + " from award"
                    + " where award.award_id = ?");
            
            if (award_id != null) {
                pstmt1.setInt(1, award_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            express_mail_ind = rs1.getString(1);
            pstmt1.close();
            rs1.close();
            if (express_mail_ind.equals("N") && express_mail.equals("Y")) {
                throw new ProcedureException(-746, 0, "redeem_points: Express_mail is not available for selected award");
            }
            //----------------------------------------------------------

            // Look up the award and get the cost in program units

            //----------------------------------------------------------

            redeem_amt = new get_redeem_amt().execute(award_id);
            redeem_amt = -redeem_amt;
            //----------------------------------------------------------

            // generate the transaction and associated detail first.

            //----------------------------------------------------------

            acct_trans_id = new _post_trans().execute(acct_id, "R", redeem_amt, null, null, null, null, null);
            detail_id = new _ins_acct_trans_de().execute(acct_trans_id, 1, redeem_amt.doubleValue(), null, null);
            redemption_id = new _add_redemption().execute(acct_id, acct_trans_id, entry_id, entry_id, null, award_id, express_mail, null);
            // Return success

            commit();
            return new ArrayList<Object>(Arrays.<Object>asList(acct_trans_id, redemption_id));
        }
        catch (SQLException e) {
            sql_error = e.getErrorCode();
            isam_error = 0;
            error_data = e.getMessage();
            {
                rollback();
                throw new ProcedureException(sql_error, isam_error, error_data);
            }
        }
    }

}