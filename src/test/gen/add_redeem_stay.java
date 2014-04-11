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

public class add_redeem_stay extends AbstractProcedure {
    
    @SuppressWarnings("unused")
    public Collection<Object> execute(String prop, DateTime doa, Integer los, Integer gnr_num, String cxl_ind, String rm_type, String srp_code, Double rm_revenue, String curr_cd, String recog_cd, String recog_id, Double amount, String res_source) throws SQLException, ProcedureException {
        /*
         * (]$[) $RCSfile$:$Revision: 37369 $ | CDATE=$Date: 2011-03-15 14:03:46 -0700 (Tue, 15 Mar 2011) $ ~
         * 
         *  Process one redemption or cancellation from the CRS by normalizing the data
         *  for procedure process_stay.
         * 
         *  Returns acct_trans_id, stay_id,and amount on success, 0 on failure.
         * 
         * 
         *         Copyright (C) 2000 Choice Hotels International, Inc.
         *                       All Rights Reserved
         */
        // Data from new records

        Integer acct_trans_id;
        Integer stay_id;
        Integer entry_id;
        // data from tables

        Integer prop_id;
        Integer acct_id;
        Integer cust_id;
        Double prop_rate;
        String earning_cd;
        Double earning_rate;
        String reporting_cd;
        Double reporting_rate;
        String acct_status;
        Integer acct_balance;
        Integer exp_bal;
        Integer exp_temp_bal;
        Integer redeem_amount;
        String description;
        String notes;
        Integer desc_id;
        Integer cust_call_id;
        Double balance_diff;
        Integer ovr_std_desc_id;
        Integer ovr_cust_call_id;
        Integer dummy;
        // Initialize all defined variables to null

        acct_trans_id = null;
        stay_id = null;
        entry_id = null;
        prop_id = null;
        acct_id = null;
        cust_id = null;
        prop_rate = null;
        earning_cd = null;
        earning_rate = null;
        reporting_cd = null;
        reporting_rate = null;
        acct_status = null;
        acct_balance = null;
        exp_bal = null;
        exp_temp_bal = null;
        redeem_amount = null;
        description = null;
        notes = null;
        desc_id = null;
        cust_call_id = null;
        balance_diff = null;
        ovr_std_desc_id = null;
        ovr_cust_call_id = null;
        dummy = null;
        //set debug file to '/tmp/add_redeem_stay.trace';

        //trace on;

        //----------------------------------------------------------

        // First convert CRS property and regognition codes to CIS

        //----------------------------------------------------------

        // Validate property by converting prop to prop_id.

        prop_id = new get_prop_id_date().execute(prop, doa);
        // Validate member id by getting member ID, status and currency info

        acct_id = new get_linked_acct().execute(recog_cd, recog_id);
        acct_status = new get_acct_status().execute(acct_id);
        if (acct_status != "A") {
            throw new ProcedureException(-746, 0, "add_redeem_stay: Member status is not 'Active.'");
        }
        cust_id = new get_pri_cust_id().execute(acct_id);
        // Make sure the account has enough points

        redeem_amount = amount.intValue();
        if (redeem_amount < 0) {
            PreparedStatement pstmt1 = prepareStatement(
                      "select sum(acct_exp.amount)"
                    + " from acct_exp"
                    + " where acct_exp.acct_id = ?");
            
            if (acct_id != null) {
                pstmt1.setInt(1, acct_id);
            }
            else {
                pstmt1.setNull(1, Types.JAVA_OBJECT);
            }
            ResultSet rs1 = executeQuery(pstmt1);
            rs1.next();
            exp_bal = rs1.getInt(1);
            pstmt1.close();
            rs1.close();
            PreparedStatement pstmt2 = prepareStatement(
                      "select sum(acct_exp_temp.amount) * -1"
                    + " from acct_exp_temp"
                    + " where acct_exp_temp.acct_id = ?"
                    + " and ? < 0.0"
                    + " and ins_dtime > current.minusHours(8)");
            
            if (acct_id != null) {
                pstmt2.setInt(1, acct_id);
            }
            else {
                pstmt2.setNull(1, Types.JAVA_OBJECT);
            }
            if (amount != null) {
                pstmt2.setDouble(2, amount);
            }
            else {
                pstmt2.setNull(2, Types.JAVA_OBJECT);
            }
            ResultSet rs2 = executeQuery(pstmt2);
            rs2.next();
            exp_temp_bal = rs2.getInt(1);
            pstmt2.close();
            rs2.close();
            acct_balance = exp_bal + nvl(exp_temp_bal, 0);
            if (acct_balance < abs(redeem_amount)) {
                balance_diff = acct_balance.doubleValue() + redeem_amount.doubleValue() * -1.0;
                PreparedStatement pstmt3 = prepareStatement(
                          "select std_desc_id"
                        + " from std_desc s"
                        + " where s.class = \"MemberCall\""
                        + " and s.subclass = \"PointAdjustment\""
                        + " and s.recog_cd = ?"
                        + " and s.std_desc_cd = \"SRDOVR\"");
                
                if (recog_cd != null) {
                    pstmt3.setString(1, recog_cd);
                }
                else {
                    pstmt3.setNull(1, Types.JAVA_OBJECT);
                }
                ResultSet rs3 = executeQuery(pstmt3);
                rs3.next();
                ovr_std_desc_id = rs3.getInt(1);
                pstmt3.close();
                rs3.close();
                ovr_cust_call_id = new _add_call_ticket().execute(cust_id, acct_id, "C", "U", ovr_std_desc_id, "Automatically adjusted points to allow SRD already booked in CRS to post", null, 1, "Balance difference: " + balance_diff);
                dummy = new adj_acct_bal().execute(acct_id, ovr_cust_call_id, balance_diff, null);
            }
        }
        // Get the earning and reporting currency codes.

        // If different than the property currency code

        // we will need to convert the property currency amount

        // to respective earning and reporting amounts.

        // First get cust info

        PreparedStatement pstmt4 = prepareStatement(
                  "select earning_curr_cd, reporting_curr_cd"
                + " from recog_pgm"
                + " where recog_pgm.recog_cd = ?");
        
        if (recog_cd != null) {
            pstmt4.setString(1, recog_cd);
        }
        else {
            pstmt4.setNull(1, Types.JAVA_OBJECT);
        }
        ResultSet rs4 = executeQuery(pstmt4);
        rs4.next();
        earning_cd = rs4.getString(1);
        reporting_cd = rs4.getString(2);
        pstmt4.close();
        rs4.close();
        // Next get the required property rate

        PreparedStatement pstmt5 = prepareStatement(
                  "select avg_rate"
                + " from currency_conv"
                + " where currency_conv.curr_cd = ?"
                + " and currency_conv.conv_date = 
                    + "select max(currency_conv.conv_date)"
                    + " from currency_conv"
                    + " where currency_conv.curr_cd = ?"
                    + " and currency_conv.conv_date < ?");
            
            if (curr_cd != null) {
                pstmt5.setString(1, curr_cd);
            }
            else {
                pstmt5.setNull(1, Types.JAVA_OBJECT);
            }
            if (curr_cd != null) {
                pstmt5.setString(2, curr_cd);
            }
            else {
                pstmt5.setNull(2, Types.JAVA_OBJECT);
            }
            if (doa != null) {
                pstmt5.setObject(3, doa);
            }
            else {
                pstmt5.setNull(3, Types.JAVA_OBJECT);
            }
            ResultSet rs5 = executeQuery(pstmt5);
            rs5.next();
            prop_rate = rs5.getDouble(1);
            rs5.close();
            // Get earning rate only if currency codes are different

            if (earning_cd != curr_cd) {
                PreparedStatement pstmt6 = prepareStatement(
                          "select avg_rate"
                        + " from currency_conv"
                        + " where currency_conv.curr_cd = ?"
                        + " and currency_conv.conv_date = 
                            + "select max(currency_conv.conv_date)"
                            + " from currency_conv"
                            + " where currency_conv.curr_cd = ?"
                            + " and currency_conv.conv_date < ?");
                    
                    if (earning_cd != null) {
                        pstmt6.setString(1, earning_cd);
                    }
                    else {
                        pstmt6.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (earning_cd != null) {
                        pstmt6.setString(2, earning_cd);
                    }
                    else {
                        pstmt6.setNull(2, Types.JAVA_OBJECT);
                    }
                    if (doa != null) {
                        pstmt6.setObject(3, doa);
                    }
                    else {
                        pstmt6.setNull(3, Types.JAVA_OBJECT);
                    }
                    ResultSet rs6 = executeQuery(pstmt6);
                    rs6.next();
                    earning_rate = rs6.getDouble(1);
                    rs6.close();
                }
                else {
                    earning_rate = prop_rate;
                }
                // Get reporting rate only if currency codes are different

                if (reporting_cd != curr_cd) {
                    PreparedStatement pstmt7 = prepareStatement(
                              "select avg_rate"
                            + " from currency_conv"
                            + " where currency_conv.curr_cd = ?"
                            + " and currency_conv.conv_date = 
                                + "select max(currency_conv.conv_date)"
                                + " from currency_conv"
                                + " where currency_conv.curr_cd = ?"
                                + " and currency_conv.conv_date < ?");
                        
                        if (reporting_cd != null) {
                            pstmt7.setString(1, reporting_cd);
                        }
                        else {
                            pstmt7.setNull(1, Types.JAVA_OBJECT);
                        }
                        if (reporting_cd != null) {
                            pstmt7.setString(2, reporting_cd);
                        }
                        else {
                            pstmt7.setNull(2, Types.JAVA_OBJECT);
                        }
                        if (doa != null) {
                            pstmt7.setObject(3, doa);
                        }
                        else {
                            pstmt7.setNull(3, Types.JAVA_OBJECT);
                        }
                        ResultSet rs7 = executeQuery(pstmt7);
                        rs7.next();
                        reporting_rate = rs7.getDouble(1);
                        rs7.close();
                    }
                    else {
                        reporting_rate = prop_rate;
                    }
                    entry_id = new get_user_id().execute("crs2fms");
                    // Add the stay record

                    PreparedStatement pstmt8 = prepareInsert(
                              "insert into stay (stay_id, cust_id, cust_ident_id, post_dtime, stay_type, crs_conf_nbr, pms_conf_nbr, prop_id, doa, los, rm_type, srp_code, rooms, rm_revenue_pc, fb_revenue_pc, other_revenue_pc, earning_curr_cd, earning_curr_rate, reporting_curr_cd, reporting_curr_rate, prop_curr_cd, prop_curr_rate, payment_meth, tai_date, invoice_prop, denial, folio_xmit_date, data_source, res_source, share_acct, primary_rec, stay_status, invoice_status, award_status, data_error, pms_batch_id, ips_batch_id, last_update_dtime)"
                            + " values (0, ?, null, current, \"R\", ?, null, ?, ?, ?, ?, ?, 1, ?, 0.0, 0.0, ?, ?, ?, ?, ?, ?, null, null, null, \"N\", null, \"C\", ?, null, \"Y\", ?, \"C\", \"C\", \"N\", null, null, current)");
                    if (cust_id != null) {
                        pstmt8.setInt(1, cust_id);
                    }
                    else {
                        pstmt8.setNull(1, Types.JAVA_OBJECT);
                    }
                    if (gnr_num != null) {
                        pstmt8.setInt(2, gnr_num);
                    }
                    else {
                        pstmt8.setNull(2, Types.JAVA_OBJECT);
                    }
                    if (prop_id != null) {
                        pstmt8.setInt(3, prop_id);
                    }
                    else {
                        pstmt8.setNull(3, Types.JAVA_OBJECT);
                    }
                    if (doa != null) {
                        pstmt8.setObject(4, doa);
                    }
                    else {
                        pstmt8.setNull(4, Types.JAVA_OBJECT);
                    }
                    if (los != null) {
                        pstmt8.setInt(5, los);
                    }
                    else {
                        pstmt8.setNull(5, Types.JAVA_OBJECT);
                    }
                    if (rm_type != null) {
                        pstmt8.setString(6, rm_type);
                    }
                    else {
                        pstmt8.setNull(6, Types.JAVA_OBJECT);
                    }
                    if (srp_code != null) {
                        pstmt8.setString(7, srp_code);
                    }
                    else {
                        pstmt8.setNull(7, Types.JAVA_OBJECT);
                    }
                    if (rm_revenue != null) {
                        pstmt8.setDouble(8, rm_revenue);
                    }
                    else {
                        pstmt8.setNull(8, Types.JAVA_OBJECT);
                    }
                    if (earning_cd != null) {
                        pstmt8.setString(9, earning_cd);
                    }
                    else {
                        pstmt8.setNull(9, Types.JAVA_OBJECT);
                    }
                    if (earning_rate != null) {
                        pstmt8.setDouble(10, earning_rate);
                    }
                    else {
                        pstmt8.setNull(10, Types.JAVA_OBJECT);
                    }
                    if (reporting_cd != null) {
                        pstmt8.setString(11, reporting_cd);
                    }
                    else {
                        pstmt8.setNull(11, Types.JAVA_OBJECT);
                    }
                    if (reporting_rate != null) {
                        pstmt8.setDouble(12, reporting_rate);
                    }
                    else {
                        pstmt8.setNull(12, Types.JAVA_OBJECT);
                    }
                    if (curr_cd != null) {
                        pstmt8.setString(13, curr_cd);
                    }
                    else {
                        pstmt8.setNull(13, Types.JAVA_OBJECT);
                    }
                    if (prop_rate != null) {
                        pstmt8.setDouble(14, prop_rate);
                    }
                    else {
                        pstmt8.setNull(14, Types.JAVA_OBJECT);
                    }
                    if (res_source != null) {
                        pstmt8.setString(15, res_source);
                    }
                    else {
                        pstmt8.setNull(15, Types.JAVA_OBJECT);
                    }
                    if (cxl_ind != null) {
                        pstmt8.setString(16, cxl_ind);
                    }
                    else {
                        pstmt8.setNull(16, Types.JAVA_OBJECT);
                    }
                    executeUpdate(pstmt8);
                    pstmt8.close();
                    stay_id = dbinfo("sqlca.sqlerrd1");
                    Iterator<Object> it0 = new process_stay().execute(acct_id, stay_id, null, amount, null).iterator();
                    acct_trans_id = (Integer) it0.next();
                    amount = (Double) it0.next();
                    return new ArrayList<Object>(Arrays.<Object>asList(acct_trans_id, stay_id));
                }
            
            }