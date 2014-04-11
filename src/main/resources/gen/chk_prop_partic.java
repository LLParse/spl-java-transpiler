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

public class chk_prop_partic extends AbstractProcedure {

	public Integer execute(Integer acct_id, Integer prop_id, Timestamp doa, Integer los, String rm_type, String srp_code, Double rm_revenue, Double fb_revenue, Double other_revenue, Integer p_promo_id, String recog_cd, String prop_curr_cd, Integer m_appl_group, String p_chain_id, String p_mkt_area, String p_country, String p_prop_type, String p_ioc_region, String p_prop_class, Integer stay_id, String stay_type) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  chk_prop_partic Checks property specific items against the
		 *  current promo. Essentially we are just comparing non-null
		 *  fields in the prop_partic record against the matching
		 *  field read from the property record itself.
		 *  Returns 1 on success, 0 on failure..
		 * 
		 *       Copyright (C) 2003 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		String answer;		
		Integer filter_id;		
		String filter;		
		String pr_prop_cd;		
		String chain_id;		
		String mkt_area;		
		String country;		
		String prop_type;		
		String ioc_region;		
		String prop_class;		
		Integer l_region_id;		
		Integer chain_group_id;		
		Timestamp start_date;		
		Timestamp stop_date;		
		Integer chk_group_id;		
		Integer chk_region_id;		
		Timestamp chk_start;		
		Timestamp chk_stop;		
		String l_prop_cd;		
		
		//set debug file to '/tmp/chk_prop_partic.trace';
		//trace on;
		
		answer = "N";		
		filter = null;		
		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select promo_prop_partic.filter_id, promo_prop_partic.prop_cd, promo_prop_partic.chain_id, promo_prop_partic.mkt_area, promo_prop_partic.country, promo_prop_partic.prop_type, promo_prop_partic.ioc_region, promo_prop_partic.prop_class, promo_prop_partic.region_id, promo_prop_partic.chain_group_id, promo_prop_partic.start_date, promo_prop_partic.stop_date"
				+ " from promo_prop_partic"
				+ " where promo_prop_partic.promo_id = ?");
		
		if (p_promo_id != null) {
			pstmt1.setInt(1, p_promo_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		while (rs1.next()) {
			filter_id = rs1.getInt(1);
			pr_prop_cd = rs1.getString(2);
			chain_id = rs1.getString(3);
			mkt_area = rs1.getString(4);
			country = rs1.getString(5);
			prop_type = rs1.getString(6);
			ioc_region = rs1.getString(7);
			prop_class = rs1.getString(8);
			l_region_id = rs1.getInt(9);
			chain_group_id = rs1.getInt(10);
			start_date = rs1.getTimestamp(11);
			stop_date = rs1.getTimestamp(12);			
			
			// clean out test vars for each record
			chk_group_id = null;			
			chk_region_id = null;			
			chk_start = null;			
			chk_stop = null;			
			
			if (doa != null) {				
				if (start_date != null) {					
					if (doa < start_date) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (doa != null) {				
				if (stop_date != null) {					
					if (doa > stop_date) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (pr_prop_cd != null) {				
				if (prop_id != null) {					
					

					PreparedStatement pstmt2 = prepareStatement(
							  "select prop_cd"
							+ " from prop"
							+ " where prop.prop_id = ?");
					
					if (prop_id != null) {
						pstmt2.setInt(1, prop_id);
					}
					else {
						pstmt2.setNull(1, Types.JAVA_OBJECT);
					}
					ResultSet rs2 = executeQuery(pstmt2);
					rs2.next();
					l_prop_cd = rs2.getString(1);
					pstmt2.close();					
					
					if (!l_prop_cd.equals(pr_prop_cd)) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (chain_id != null) {				
				if (p_chain_id != null) {					
					if (!chain_id.equals(p_chain_id)) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (chain_group_id != null) {				

				PreparedStatement pstmt3 = prepareStatement(
						  "select c.chain_group_id, c.start_date, c.stop_date"
						+ " from chain_group_detail c"
						+ " where c.chain_group_id = ?"
						+ " and c.chain_id = ?");
				
				if (chain_group_id != null) {
					pstmt3.setInt(1, chain_group_id);
				}
				else {
					pstmt3.setNull(1, Types.JAVA_OBJECT);
				}
				if (p_chain_id != null) {
					pstmt3.setString(2, p_chain_id);
				}
				else {
					pstmt3.setNull(2, Types.JAVA_OBJECT);
				}
				ResultSet rs3 = executeQuery(pstmt3);
				rs3.next();
				chk_group_id = rs3.getInt(1);
				chk_start = rs3.getTimestamp(2);
				chk_stop = rs3.getTimestamp(3);
				pstmt3.close();				
				if (chk_group_id != null) {					
					if (!chk_group_id.equals(chain_group_id)) {						
						continue;
					}					
					if (doa != null) {						
						if (doa < chk_start || doa > chk_stop) {							
							continue;
						}
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (prop_class != null) {				
				if (p_prop_class != null) {					
					if (!prop_class.equals(p_prop_class)) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (mkt_area != null) {				
				if (p_mkt_area != null) {					
					if (!mkt_area.equals(p_mkt_area)) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (country != null) {				
				if (p_country != null) {					
					if (!country.equals(p_country)) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (l_region_id != null) {				
				if (p_country != null) {					

					PreparedStatement pstmt4 = prepareStatement(
							  "select r.region_id, r.start_date, r.stop_date"
							+ " from region_country r"
							+ " where r.country_cd = ?"
							+ " and r.region_id = ?");
					
					if (p_country != null) {
						pstmt4.setString(1, p_country);
					}
					else {
						pstmt4.setNull(1, Types.JAVA_OBJECT);
					}
					if (l_region_id != null) {
						pstmt4.setInt(2, l_region_id);
					}
					else {
						pstmt4.setNull(2, Types.JAVA_OBJECT);
					}
					ResultSet rs4 = executeQuery(pstmt4);
					rs4.next();
					chk_region_id = rs4.getInt(1);
					chk_start = rs4.getTimestamp(2);
					chk_stop = rs4.getTimestamp(3);
					pstmt4.close();					
					if (chk_region_id == null) {						
						continue;
					}					
					if (!l_region_id.equals(chk_region_id)) {						
						continue;
					}					
					if (doa != null) {						
						if (doa < chk_start || doa > chk_stop) {							
							continue;
						}
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (prop_type != null) {				
				if (p_prop_type != null) {					
					if (!prop_type.equals(p_prop_type)) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			if (ioc_region != null) {				
				if (p_ioc_region != null) {					
					if (!ioc_region.equals(p_ioc_region)) {						
						continue;
					}
				}				
				else {					
					continue;
				}
			}			
			
			// apply the filter if it exists here.
			if (filter_id != null) {				
				// look up that procedure and run the thing.
				answer = new check_filter().execute(filter_id, acct_id, p_promo_id, "F", prop_id, doa, los, rm_type, srp_code, prop_curr_cd, rm_revenue, fb_revenue, other_revenue, null, stay_id);				
				
				if (!answer.equals("T")) {					
					continue;
				}
			}			// end if looking at filters
			
			// to make it here is to survive all property checks for one record
			return 1;
		}
		pstmt1.close();		
		
		// looked at all the records and none worked.
		return 0;
	}

}