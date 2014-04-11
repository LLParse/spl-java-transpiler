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

public class add_promo extends AbstractProcedure {

	public Integer execute(String promo_cd, String promo_name, String promo_desc, String trigger, Timestamp start_date, Timestamp stop_date, String rule, Integer max_uses, String requires_base, String requires_registration, String use_rm_rev, String use_fb_rev, String use_other_rev, Integer campaign_id, Integer billing_cat_id, String recog_cd, String parent_promo_cd, String post_proc, String clazz, String user_name) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~ 
		 * 
		 *  Add a promotion to the system.
		 * 
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer promo_id;		
		Integer user_id;		
		String external_pgm;		
		Integer parent_promo_id;		
		
		promo_id = null;		
		user_id = null;		
		external_pgm = null;		
		parent_promo_id = null;		
		
		// set debug file to '/tmp/add_promo.trace';
		// trace on;
		
		promo_cd = trim(promo_cd);		
		if (length(promo_cd) < 2) {			
			throw new ProcedureException(-746, 0, "add_promo: a meaningful promo_cd must be specified");
		}		
		
		if (recog_cd == null) {			
			throw new ProcedureException(-746, 0, "add_promo: recog_cd must not be null");
		}		
		
		promo_name = trim(promo_name);		
		if (length(promo_name) < 10) {			
			throw new ProcedureException(-746, 0, "add_promo: a meaningful promo_name must be specified");
		}		
		
		promo_desc = trim(promo_desc);		
		if (length(promo_desc) < 10) {			
			throw new ProcedureException(-746, 0, "add_promo: a meaningful promo_desc must be specified");
		}		
		
		if (!trigger.equals("STAY") && !trigger.equals("SGNUPJ") && !trigger.equals("ATTRIB") && !trigger.equals("AWARD") && !trigger.equals("STAY_J") && !trigger.equals("NONE") && !trigger.equals("SIGNUP") && !trigger.equals("REF") && !trigger.equals("EMAIL") && !trigger.equals("REFJ") && !trigger.equals("CBPAY") && !trigger.equals("CBENRL") && !trigger.equals("EMAILA")) {			
			throw new ProcedureException(-746, 0, "add_promo: invalid promotion trigger");
		}		
		
		// JGG grant some leeway in loading promos
		if (start_date <= new Timestamp(System.currentTimeMillis()) - 2day) {			
			throw new ProcedureException(-746, 0, "add_promo: start_date must be in the future");
		}		
		
		if (stop_date < start_date) {			
			throw new ProcedureException(-746, 0, "add_promo: stop_date cannot be before the start_date");
		}		
		
		if (!rule.equals("A") && !rule.equals("S") && !rule.equals("E")) {			
			throw new ProcedureException(-746, 0, "add_promo: invalid promotion rule");
		}		
		
		if (!requires_base.equals("Y") && !requires_base.equals("N")) {			
			throw new ProcedureException(-746, 0, "add_promo: requires_base must be either 'Y' or 'N'");
		}		
		
		if (!requires_registration.equals("Y") && !requires_registration.equals("N")) {			
			throw new ProcedureException(-746, 0, "add_promo: requires_registration must be either 'Y' or 'N'");
		}		
		
		if (max_uses != null) {			
			if (max_uses < 1) {				
				throw new ProcedureException(-746, 0, "add_promo: max_uses must be greater than zero");
			}
		}		
		
		if (!use_rm_rev.equals("Y") && !use_rm_rev.equals("N")) {			
			throw new ProcedureException(-746, 0, "add_promo: use_rm_rev must be either 'Y' or 'N'");
		}		
		
		if (!use_fb_rev.equals("Y") && !use_fb_rev.equals("N")) {			
			throw new ProcedureException(-746, 0, "add_promo: use_fb_rev must be either 'Y' or 'N'");
		}		
		
		if (!use_other_rev.equals("Y") && !use_other_rev.equals("N")) {			
			throw new ProcedureException(-746, 0, "add_promo: use_other_rev must be either 'Y' or 'N'");
		}		
		
		user_id = new get_user_id().execute(user_name);		
		if (user_id == null) {			
			throw new ProcedureException(-746, 0, "add_promo: user_name is not known");
		}		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select recog_pgm.external_pgm"
				+ " from recog_pgm"
				+ " where recog_pgm.recog_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		external_pgm = rs1.getString(1);
		pstmt1.close();		
		if (external_pgm.equals("Y")) {			
			if (billing_cat_id == null) {				
				throw new ProcedureException(-746, 0, "add_promo: billing_cat_id must be defined for an external program");
			}
		}		
		
		if (clazz == null) {			
			throw new ProcedureException(-746, 0, "add_promo: class must not be null");
		}		
		
		if (parent_promo_cd != null) {			

			PreparedStatement pstmt2 = prepareStatement(
					  "select promo.promo_id"
					+ " from promo"
					+ " where promo.promo_cd = ?"
					+ " and promo.recog_cd = ?");
			
			if (parent_promo_cd != null) {
				pstmt2.setString(1, parent_promo_cd);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			if (recog_cd != null) {
				pstmt2.setString(2, recog_cd);
			}
			else {
				pstmt2.setNull(2, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			parent_promo_id = rs2.getInt(1);
			pstmt2.close();			
			
			if (parent_promo_id == null) {				
				throw new ProcedureException(-746, 0, "add_promo: parent promo specified and does not exist");
			}
		}		
		

		PreparedStatement pstmt3 = prepareInsert(
				  "insert into promo (promo_id, promo_cd, name, desc, clazz, trigger, start_date, stop_date, rule, max_uses, requires_base, requires_registration, use_rm_rev, use_fb_rev, use_other_rev, campaign_id, billing_cat_id, recog_cd, entry_id, last_update_dtime, parent_promo_id, post_proc, last_update_id)"
				+ " values (0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current, ?, ?, ?)");
		if (promo_cd != null) {
			pstmt3.setString(1, promo_cd);
		}
		else {
			pstmt3.setNull(1, Types.JAVA_OBJECT);
		}
		if (promo_name != null) {
			pstmt3.setString(2, promo_name);
		}
		else {
			pstmt3.setNull(2, Types.JAVA_OBJECT);
		}
		if (promo_desc != null) {
			pstmt3.setString(3, promo_desc);
		}
		else {
			pstmt3.setNull(3, Types.JAVA_OBJECT);
		}
		if (clazz != null) {
			pstmt3.setString(4, clazz);
		}
		else {
			pstmt3.setNull(4, Types.JAVA_OBJECT);
		}
		if (trigger != null) {
			pstmt3.setString(5, trigger);
		}
		else {
			pstmt3.setNull(5, Types.JAVA_OBJECT);
		}
		if (start_date != null) {
			pstmt3.setObject(6, start_date);
		}
		else {
			pstmt3.setNull(6, Types.JAVA_OBJECT);
		}
		if (stop_date != null) {
			pstmt3.setObject(7, stop_date);
		}
		else {
			pstmt3.setNull(7, Types.JAVA_OBJECT);
		}
		if (rule != null) {
			pstmt3.setString(8, rule);
		}
		else {
			pstmt3.setNull(8, Types.JAVA_OBJECT);
		}
		if (max_uses != null) {
			pstmt3.setInt(9, max_uses);
		}
		else {
			pstmt3.setNull(9, Types.JAVA_OBJECT);
		}
		if (requires_base != null) {
			pstmt3.setString(10, requires_base);
		}
		else {
			pstmt3.setNull(10, Types.JAVA_OBJECT);
		}
		if (requires_registration != null) {
			pstmt3.setString(11, requires_registration);
		}
		else {
			pstmt3.setNull(11, Types.JAVA_OBJECT);
		}
		if (use_rm_rev != null) {
			pstmt3.setString(12, use_rm_rev);
		}
		else {
			pstmt3.setNull(12, Types.JAVA_OBJECT);
		}
		if (use_fb_rev != null) {
			pstmt3.setString(13, use_fb_rev);
		}
		else {
			pstmt3.setNull(13, Types.JAVA_OBJECT);
		}
		if (use_other_rev != null) {
			pstmt3.setString(14, use_other_rev);
		}
		else {
			pstmt3.setNull(14, Types.JAVA_OBJECT);
		}
		if (campaign_id != null) {
			pstmt3.setInt(15, campaign_id);
		}
		else {
			pstmt3.setNull(15, Types.JAVA_OBJECT);
		}
		if (billing_cat_id != null) {
			pstmt3.setInt(16, billing_cat_id);
		}
		else {
			pstmt3.setNull(16, Types.JAVA_OBJECT);
		}
		if (recog_cd != null) {
			pstmt3.setString(17, recog_cd);
		}
		else {
			pstmt3.setNull(17, Types.JAVA_OBJECT);
		}
		if (user_id != null) {
			pstmt3.setInt(18, user_id);
		}
		else {
			pstmt3.setNull(18, Types.JAVA_OBJECT);
		}
		if (parent_promo_id != null) {
			pstmt3.setInt(19, parent_promo_id);
		}
		else {
			pstmt3.setNull(19, Types.JAVA_OBJECT);
		}
		if (post_proc != null) {
			pstmt3.setString(20, post_proc);
		}
		else {
			pstmt3.setNull(20, Types.JAVA_OBJECT);
		}
		if (user_id != null) {
			pstmt3.setInt(21, user_id);
		}
		else {
			pstmt3.setNull(21, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt3);
		pstmt3.close();		
		
		promo_id = dbinfo("sqlca.sqlerrd1");		
		
		return promo_id;
	}

}