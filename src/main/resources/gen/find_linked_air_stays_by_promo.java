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

public class find_linked_air_stays_by_promo extends AbstractProcedure {

	public Collection<Object> execute(Integer promo_id_in, Timestamp begin_date_in, Timestamp end_date_in, Integer acct_id_in) throws SQLException, ProcedureException {		
		
		/*
		 * $Id: find_linked_air_stays_by_promo.sql 99 2012-12-19 21:14:54Z michael_giroux $
		 * 
		 *  find_linked_air_stays_by_promo Checks property specific items against the
		 *  current promo linked stay results. Essentially we are just comparing non-null
		 *  fields in the prop_partic record against the matching
		 *  field read from the property record itself.
		 *  Returns cursor of filtered linked stays.
		 * 
		 *       Copyright (C) 2006 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer answer;		
		
		Integer l_prop_id;		
		String l_chain_id;		
		String l_mkt_area;		
		String l_country;		
		String l_prop_country;		
		String l_prop_type;		
		String l_ioc_region;		
		String l_prop_class;		
		String l_rm_type;		
		String l_srp_code;		
		Double l_rm_revenue;		
		Double l_fb_revenue;		
		Double l_other_revenue;		
		String l_prop_curr_cd;		
		
		Integer l_appl_group;		
		String l_recog_cd;		
		
		String l_stay_type;		
		Integer l_stay_id;		
		String l_prop_cd;		
		Timestamp l_doa;		
		Timestamp l_dod;		
		Integer l_los;		
		Integer l_linked_id;		
		Integer l_trans_id;		
		
		
		answer = 0;		
		
		//set debug file to '/tmp/find_linked_air_stays_by_promo.log';
		//trace on;
		

		Iterator<Object> it0 = find_linked_air_stays(acct_id_in, begin_date_in, end_date_in).iterator();
		l_stay_id = (Integer) it0.next();
		l_prop_cd = (String) it0.next();
		l_doa = (Timestamp) it0.next();
		l_dod = (Timestamp) it0.next();
		l_linked_id = (Integer) it0.next();
		l_trans_id = (Integer) it0.next();
		while (it0.hasNext()) {			
			
			l_prop_id = new get_prop_id_date().execute(l_prop_cd, l_doa);			
			

			PreparedStatement pstmt1 = prepareStatement(
					  "select chain_id, mkt_area, country, prop_type, ioc_region, prop_class, country, prop_type"
					+ " from prop"
					+ " where prop_id = ?");
			
			if (l_prop_id != null) {
				pstmt1.setInt(1, l_prop_id);
			}
			else {
				pstmt1.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs1 = executeQuery(pstmt1);
			rs1.next();
			l_chain_id = rs1.getString(1);
			l_mkt_area = rs1.getString(2);
			l_country = rs1.getString(3);
			l_prop_type = rs1.getString(4);
			l_ioc_region = rs1.getString(5);
			l_prop_class = rs1.getString(6);
			l_prop_country = rs1.getString(7);
			l_prop_type = rs1.getString(8);
			pstmt1.close();			
			

			PreparedStatement pstmt2 = prepareStatement(
					  "select los, rm_type, srp_code, rm_revenue_pc, fb_revenue_pc, other_revenue_pc, prop_curr_cd, stay_type"
					+ " from stay"
					+ " where stay_id = ?");
			
			if (l_stay_id != null) {
				pstmt2.setInt(1, l_stay_id);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs2 = executeQuery(pstmt2);
			rs2.next();
			l_los = rs2.getInt(1);
			l_rm_type = rs2.getString(2);
			l_srp_code = rs2.getString(3);
			l_rm_revenue = rs2.getDouble(4);
			l_fb_revenue = rs2.getDouble(5);
			l_other_revenue = rs2.getDouble(6);
			l_prop_curr_cd = rs2.getString(7);
			l_stay_type = rs2.getString(8);
			pstmt2.close();			
			

			PreparedStatement pstmt3 = prepareStatement(
					  "select recog_cd, appl_group_id"
					+ " from acct"
					+ " where acct_id = ?");
			
			if (acct_id_in != null) {
				pstmt3.setInt(1, acct_id_in);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			ResultSet rs3 = executeQuery(pstmt3);
			rs3.next();
			l_recog_cd = rs3.getString(1);
			l_appl_group = rs3.getInt(2);
			pstmt3.close();			
			
			answer = new chk_prop_partic().execute(acct_id_in, l_prop_id, l_doa, l_los, l_rm_type, l_srp_code, l_rm_revenue, l_fb_revenue, l_other_revenue, promo_id_in, l_recog_cd, l_prop_curr_cd, l_appl_group, l_chain_id, l_mkt_area, l_prop_country, l_prop_type, l_ioc_region, l_prop_class, l_stay_id, l_stay_type);			
			
			// chk_prop_partic is false so this
			// stay gets excluded from the results				
			if (answer.equals(0)) {				
				continue;
			}			
			
			// to make it here is to survive all property checks for one record
			return new ArrayList<Object>(Arrays.<Object>asList(l_stay_id, l_prop_cd, l_doa, l_dod, l_linked_id, l_trans_id));
		}
	}

}