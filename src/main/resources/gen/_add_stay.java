/* Generated on 02-25-2013 12:49:25 PM by SPLParser v0.9 */
package com.choicehotels.gen;

import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class _add_stay extends AbstractProcedure {

	public Integer execute(Integer cust_id, Integer acct_trans_id, String cxl_flag, Integer conf_nbr, Integer prop_id, Timestamp doa, Integer los, String rm_type, Integer rooms, String srp_code, Double rm_revenue_pc, Double rm_revenue_cc, Double fb_revenue_pc, Double fb_revenue_cc, Double other_revenue_pc, Double other_revenue_cc, String prop_curr_code, String cust_cur_code, String payment_meth, String invoice_prop, String denial, Timestamp folio_xmit_date, String stay_type, String data_source, String res_source, Timestamp tai_date) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 *  
		 *  
		 *  Assumption is that 'BEGIN WORK' is called prior to this
		 *  procedure and 'ROLLBACK/COMMIT WORK' after depending on result. Returns
		 *  stay_id on success. 
		 * 
		 * 	Copyright (C) 2000 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer stay_id;		
		
		stay_id = null;		
		
		// set debug file to '/tmp/_add_stay.trace';
		// trace on;
		
		// Build stay record
		

		PreparedStatement pstmt1 = prepareInsert(
				  "insert into stay (stay_id, acct_trans_id, cust_id, post_dtime, stay_type, cxl_flag, conf_nbr, prop_id, doa, los, rm_type, srp_code, rooms, rm_revenue_pc, rm_revenue_cc, fb_revenue_pc, fb_revenue_cc, other_revenue_pc, other_revenue_cc, prop_curr_cd, cust_curr_cd, payment_meth, tai_date, invoice_prop, denial, folio_xmit_date, data_source, res_source)"
				+ " values (0, ?, ?, CURRENT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (cust_id != null) {
			pstmt1.setInt(2, cust_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (stay_type != null) {
			pstmt1.setString(3, stay_type);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		if (cxl_flag != null) {
			pstmt1.setString(4, cxl_flag);
		}
		else {
			pstmt1.setNull(4, Types.JAVA_OBJECT);
		}
		if (conf_nbr != null) {
			pstmt1.setInt(5, conf_nbr);
		}
		else {
			pstmt1.setNull(5, Types.JAVA_OBJECT);
		}
		if (prop_id != null) {
			pstmt1.setInt(6, prop_id);
		}
		else {
			pstmt1.setNull(6, Types.JAVA_OBJECT);
		}
		if (doa != null) {
			pstmt1.setObject(7, doa);
		}
		else {
			pstmt1.setNull(7, Types.JAVA_OBJECT);
		}
		if (los != null) {
			pstmt1.setInt(8, los);
		}
		else {
			pstmt1.setNull(8, Types.JAVA_OBJECT);
		}
		if (rm_type != null) {
			pstmt1.setString(9, rm_type);
		}
		else {
			pstmt1.setNull(9, Types.JAVA_OBJECT);
		}
		if (srp_code != null) {
			pstmt1.setString(10, srp_code);
		}
		else {
			pstmt1.setNull(10, Types.JAVA_OBJECT);
		}
		if (rooms != null) {
			pstmt1.setInt(11, rooms);
		}
		else {
			pstmt1.setNull(11, Types.JAVA_OBJECT);
		}
		if (rm_revenue_pc != null) {
			pstmt1.setDouble(12, rm_revenue_pc);
		}
		else {
			pstmt1.setNull(12, Types.JAVA_OBJECT);
		}
		if (rm_revenue_cc != null) {
			pstmt1.setDouble(13, rm_revenue_cc);
		}
		else {
			pstmt1.setNull(13, Types.JAVA_OBJECT);
		}
		if (fb_revenue_pc != null) {
			pstmt1.setDouble(14, fb_revenue_pc);
		}
		else {
			pstmt1.setNull(14, Types.JAVA_OBJECT);
		}
		if (fb_revenue_cc != null) {
			pstmt1.setDouble(15, fb_revenue_cc);
		}
		else {
			pstmt1.setNull(15, Types.JAVA_OBJECT);
		}
		if (other_revenue_pc != null) {
			pstmt1.setDouble(16, other_revenue_pc);
		}
		else {
			pstmt1.setNull(16, Types.JAVA_OBJECT);
		}
		if (other_revenue_cc != null) {
			pstmt1.setDouble(17, other_revenue_cc);
		}
		else {
			pstmt1.setNull(17, Types.JAVA_OBJECT);
		}
		if (prop_curr_code != null) {
			pstmt1.setString(18, prop_curr_code);
		}
		else {
			pstmt1.setNull(18, Types.JAVA_OBJECT);
		}
		if (cust_cur_code != null) {
			pstmt1.setString(19, cust_cur_code);
		}
		else {
			pstmt1.setNull(19, Types.JAVA_OBJECT);
		}
		if (payment_meth != null) {
			pstmt1.setString(20, payment_meth);
		}
		else {
			pstmt1.setNull(20, Types.JAVA_OBJECT);
		}
		if (tai_date != null) {
			pstmt1.setObject(21, tai_date);
		}
		else {
			pstmt1.setNull(21, Types.JAVA_OBJECT);
		}
		if (invoice_prop != null) {
			pstmt1.setString(22, invoice_prop);
		}
		else {
			pstmt1.setNull(22, Types.JAVA_OBJECT);
		}
		if (denial != null) {
			pstmt1.setString(23, denial);
		}
		else {
			pstmt1.setNull(23, Types.JAVA_OBJECT);
		}
		if (folio_xmit_date != null) {
			pstmt1.setObject(24, folio_xmit_date);
		}
		else {
			pstmt1.setNull(24, Types.JAVA_OBJECT);
		}
		if (data_source != null) {
			pstmt1.setString(25, data_source);
		}
		else {
			pstmt1.setNull(25, Types.JAVA_OBJECT);
		}
		if (res_source != null) {
			pstmt1.setString(26, res_source);
		}
		else {
			pstmt1.setNull(26, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt1);
		pstmt1.close();		
		
		stay_id = dbinfo("sqlca.sqlerrd1");		
		// Return success
		return stay_id;
	}

}