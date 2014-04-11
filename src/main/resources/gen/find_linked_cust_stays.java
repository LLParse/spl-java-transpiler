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

public class find_linked_cust_stays extends AbstractProcedure {

	public Collection<Object> execute(Integer cust_id_in, Timestamp begin_date_in, Timestamp end_date_in) throws SQLException, ProcedureException {		
		
		/*
		 * find_linked_cust_stays.sql - iterates through stays and links concurrent and consequtive stays
		 * for a customer.          
		 * $Id: find_linked_cust_stays.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 *    
		 *        Copyright (C) 2007 Choice Hotels International, Inc.
		 */		
		
		Integer f_id;		
		Timestamp f_doa;		
		Timestamp f_dod;		
		String f_prop;		
		Integer f_link;		
		Integer n_id;		
		Integer n_link;		
		Integer n_cnt;		
		Integer f_trans_id;		
		
		f_id = null;		
		f_doa = null;		
		f_dod = null;		
		f_prop = null;		
		f_link = null;		
		n_id = null;		
		n_link = null;		
		n_cnt = null;		
		f_trans_id = null;		
		
		if (begin_date_in == null) {			
			begin_date_in = date(new Timestamp(System.currentTimeMillis()) - 540day);
		}		
		
		if (end_date_in == null) {			
			end_date_in = date(new Timestamp(System.currentTimeMillis()));
		}		
		
		//    set debug file to '/tmp/find_linked_cust_stays.trace';
		//    trace on;
		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select s.stay_id, new get_prop_cd().execute(s.prop_id), s.doa, s.doa + s.los, -1, t.acct_trans_id"
				+ " from stay s, acct_trans t"
				+ " where s.stay_type in (\"N\", \"F\")"
				+ " and s.stay_id = t.stay_id"
				+ " and s.cust_id = ?"
				+ " and t.stay_id = s.stay_id"
				+ " and t.rev_acct_trans_id is null"
				+ " and new get_acct_trans_sum().execute(t.acct_trans_id) > 0"
				+ " and s.doa >= ?"
				+ " and s.doa <= ?"
				+ " order by doa, stay_id");
		
		if (cust_id_in != null) {
			pstmt1.setInt(1, cust_id_in);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (begin_date_in != null) {
			pstmt1.setObject(2, begin_date_in);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (end_date_in != null) {
			pstmt1.setObject(3, end_date_in);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		while (rs1.next()) {
			f_id = rs1.getInt(1);
			f_prop = rs1.getString(2);
			f_doa = rs1.getTimestamp(3);
			f_dod = rs1.getTimestamp(4);
			f_link = rs1.getInt(5);
			f_trans_id = rs1.getInt(6);			
			

			PreparedStatement pstmt2 = prepareStatement(
					  "select max(l.id)"
					+ " from linked_stays_tmp l"
					+ " where l.prop_cd = ?"
					+ " and (" || 
					+ ")");
			
			if (f_prop != null) {
				pstmt2.setString(1, f_prop);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			n_id = rs2.getInt(1);
			pstmt2.close();			
			
			if (n_id != null) {				

				PreparedStatement pstmt3 = prepareStatement(
						  "select nvl(linked_id, stay_id)"
						+ " from linked_stays_tmp"
						+ " where id = ?");
				
				if (n_id != null) {
					pstmt3.setInt(1, n_id);
				}
				else {
					pstmt3.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs3 = executeQuery(pstmt3);
				rs3.next();
				n_link = rs3.getInt(1);
				pstmt3.close();
			}			
			else {				
				n_link = f_id;
			}			
			

			PreparedStatement pstmt4 = prepareInsert(
					  "insert into linked_stays_tmp (stay_id, prop_cd, doa, dod, linked_id)"
					+ " values (?, ?, ?, ?, ?)");
			if (f_id != null) {
				pstmt4.setInt(1, f_id);
			}
			else {
				pstmt4.setNull(1, Types.JAVA_OBJECT);
			}
			if (f_prop != null) {
				pstmt4.setString(2, f_prop);
			}
			else {
				pstmt4.setNull(2, Types.JAVA_OBJECT);
			}
			if (f_doa != null) {
				pstmt4.setObject(3, f_doa);
			}
			else {
				pstmt4.setNull(3, Types.JAVA_OBJECT);
			}
			if (f_dod != null) {
				pstmt4.setObject(4, f_dod);
			}
			else {
				pstmt4.setNull(4, Types.JAVA_OBJECT);
			}
			if (n_link != null) {
				pstmt4.setInt(5, n_link);
			}
			else {
				pstmt4.setNull(5, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt4);
			pstmt4.close();			
			
			return new ArrayList<Object>(Arrays.<Object>asList(f_id, f_prop, f_doa, f_dod, n_link, f_trans_id));
		}
		pstmt1.close();
	}

}