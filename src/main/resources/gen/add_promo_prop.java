/* Generated on 02-25-2013 12:49:24 PM by SPLParser v0.9 */
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

public class add_promo_prop extends AbstractProcedure {

	public Integer execute(Integer promo_id, String prop_cd, String chain_cd, String chain_group, String prop_class, String mkt_area, String master_fran, String cntrl_ofc, String mgt_company, String country, String region_name, String ioc_region, String prop_type, String filter_name, Timestamp start_date, Timestamp stop_date, String user_name) throws SQLException, ProcedureException {		
		
		/*
		 * (]$[) $RCSfile$:$Revision: 99 $ | CDATE=$Date: 2012-12-19 14:14:54 -0700 (Wed, 19 Dec 2012) $ ~
		 * 
		 *  Add a property participation specifcation to a promotion.
		 * 
		 * 	Copyright (C) 2002 Choice Hotels International, Inc.
		 *                       All Rights Reserved
		 */		
		
		Integer ord;		
		Integer prop_id;		
		Integer filter_id;		
		Integer user_id;		
		Integer region_id;		
		Integer chain_group_id;		
		
		ord = null;		
		prop_id = null;		
		filter_id = null;		
		user_id = null;		
		region_id = null;		
		chain_group_id = null;		
		
		
		// set debug file to '/tmp/add_promo_prop.trace';
		// trace on;
		
		// compute the ord value

		PreparedStatement pstmt1 = prepareStatement(
				  "select max(pp.ord)"
				+ " from promo_prop_partic pp"
				+ " where pp.promo_id = ?");
		
		if (promo_id != null) {
			pstmt1.setInt(1, promo_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		ord = rs1.getInt(1);
		pstmt1.close();		
		if (ord == null) {			
			ord = 1;
		}		
		else {			
			ord = ord + 1;
		}		
		
		// validate and encode the prop_cd
		if (prop_cd != null) {			
			prop_id = new get_prop_id().execute(prop_cd);			
			if (prop_id == null) {				
				throw new ProcedureException(-746, 0, "add_promo_prop: prop_cd is invalid");
			}
		}		
		
		// validate the start and stop dates
		if (start_date == null) {			
			throw new ProcedureException(-746, 0, "add_promo_prop: start_date must not be null");
		}		
		if (stop_date == null) {			
			throw new ProcedureException(-746, 0, "add_promo_prop: stop_date must not be null");
		}		
		
		// validate the prop_class
		if (prop_class != null) {			
			prop_class = trim(prop_class);			
			if (length(prop_class) < 1) {				
				throw new ProcedureException(-746, 0, "add_promo_prop: prop_class is invalid");
			}
		}		
		
		// validate the mkt_area
		if (mkt_area != null) {			
			mkt_area = trim(mkt_area);			
			if (length(mkt_area) < 1) {				
				throw new ProcedureException(-746, 0, "add_promo_prop: mkt_area is invalid");
			}
		}		
		
		// validate the master_fran
		if (master_fran != null) {			
			master_fran = trim(master_fran);			
			if (length(master_fran) < 1) {				
				throw new ProcedureException(-746, 0, "add_promo_prop: master_fran is invalid");
			}
		}		
		
		// validate the cntrl_ofc
		if (cntrl_ofc != null) {			
			cntrl_ofc = trim(cntrl_ofc);			
			if (length(cntrl_ofc) < 1) {				
				throw new ProcedureException(-746, 0, "add_promo_prop: cntrl_ofc is invalid");
			}
		}		
		
		// validate the mgt_company
		if (mgt_company != null) {			
			mgt_company = trim(mgt_company);			
			if (length(mgt_company) < 1) {				
				throw new ProcedureException(-746, 0, "add_promo_prop: mgt_company is invalid");
			}
		}		
		
		// validate the chain/chain_group pair
		if (chain_cd != null && chain_group != null) {			
			throw new ProcedureException(-746, 0, "add_promo_prop: cannot have both a chain_cd and chain_group");
		}		
		
		// validate the chain_cd
		if (chain_cd != null) {			
			if (
			PreparedStatement pstmt2 = prepareStatement(
					  "select *"
					+ " from chain c"
					+ " where c.chain_id = ?") {					
					throw new ProcedureException(-746, 0, \"add_promo_prop: chain is invalid\");
				}
			}			
			
			// validate the chain_group
			if (? is not null) {				

				PreparedStatement pstmt3 = prepareStatement(
						  "select g.chain_group_id"
						+ " from chain_group g"
						+ " where g.name = ?");
				
				if (chain_group != null) {
					pstmt3.setString(1, chain_group);
				}
				else {
					pstmt3.setNull(1, Types.JAVA_OBJECT);
				}
				ResultSet rs2 = executeQuery(pstmt3);
				rs2.next();
				chain_group_id = rs2.getInt(1);
				pstmt3.close();				
				if (chain_group_id == null) {					
					throw new ProcedureException(-746, 0, "add_promo_prop: chain_group is invalid");
				}
			}			
			
			// validate the country/region pair
			if (country != null && region_name != null) {				
				throw new ProcedureException(-746, 0, "add_promo_prop: cannot have both country and region");
			}			
			
			// validate the country
			if (country != null) {				
				if (
				PreparedStatement pstmt4 = prepareStatement(
						  "select *"
						+ " from country c"
						+ " where c.country_cd = ?") {						
						throw new ProcedureException(-746, 0, \"add_promo_prop: country is invalid\");
					}
				}				
				
				// validate the region
				if (? is not null) {					

					PreparedStatement pstmt5 = prepareStatement(
							  "select r.region_id"
							+ " from region r"
							+ " where r.name = ?");
					
					if (region_name != null) {
						pstmt5.setString(1, region_name);
					}
					else {
						pstmt5.setNull(1, Types.JAVA_OBJECT);
					}
					ResultSet rs3 = executeQuery(pstmt5);
					rs3.next();
					region_id = rs3.getInt(1);
					pstmt5.close();					
					if (region_id == null) {						
						throw new ProcedureException(-746, 0, "add_promo_prop: region is invalid");
					}
				}				
				
				
				// validate the ioc_region
				if (ioc_region != null) {					
					ioc_region = trim(ioc_region);					
					if (length(ioc_region) < 1) {						
						throw new ProcedureException(-746, 0, "add_promo_prop: ioc_region is invalid");
					}
				}				
				
				// validate the prop_type
				if (prop_type != null) {					
					prop_type = trim(prop_type);					
					if (length(prop_type) < 1) {						
						throw new ProcedureException(-746, 0, "add_promo_prop: prop_type is invalid");
					}
				}				
				
				// validate and encode the filter_name
				if (filter_name != null) {
					{						
						String recog_cd;						
						recog_cd = null;						
						

						PreparedStatement pstmt6 = prepareStatement(
								  "select p.recog_cd"
								+ " from promo p"
								+ " where p.promo_id = ?");
						
						if (promo_id != null) {
							pstmt6.setInt(1, promo_id);
						}
						else {
							pstmt6.setNull(1, Types.JAVA_OBJECT);
						}
						ResultSet rs4 = executeQuery(pstmt6);
						rs4.next();
						recog_cd = rs4.getString(1);
						pstmt6.close();						
						

						PreparedStatement pstmt7 = prepareStatement(
								  "select pf.filter_id"
								+ " from promo_filter pf"
								+ " where pf.filter_name = ?");
						
						if (filter_name != null) {
							pstmt7.setString(1, filter_name);
						}
						else {
							pstmt7.setNull(1, Types.JAVA_OBJECT);
						}
						ResultSet rs5 = executeQuery(pstmt7);
						rs5.next();
						filter_id = rs5.getInt(1);
						pstmt7.close();
					}					
					
					if (filter_id == null) {						
						throw new ProcedureException(-746, 0, "add_promo_prop: filter_id is invalid");
					}
				}				
				
				// validate and encode the user_name
				user_id = new get_user_id().execute(user_name);				
				if (user_id == null) {					
					throw new ProcedureException(-746, 0, "add_promo_prop: user_name is not known");
				}				
				

				PreparedStatement pstmt8 = prepareInsert(
						  "insert into promo_prop_partic (promo_id, ord, prop_cd, chain_id, prop_class, mkt_area, master_fran, cntrl_ofc, mgt_company, country, ioc_region, prop_type, filter_id, region_id, chain_group_id, start_date, stop_date, entry_id, last_update_dtime, last_update_id)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current, ?)");
				if (promo_id != null) {
					pstmt8.setInt(1, promo_id);
				}
				else {
					pstmt8.setNull(1, Types.JAVA_OBJECT);
				}
				if (ord != null) {
					pstmt8.setInt(2, ord);
				}
				else {
					pstmt8.setNull(2, Types.JAVA_OBJECT);
				}
				if (prop_cd != null) {
					pstmt8.setString(3, prop_cd);
				}
				else {
					pstmt8.setNull(3, Types.JAVA_OBJECT);
				}
				if (chain_cd != null) {
					pstmt8.setString(4, chain_cd);
				}
				else {
					pstmt8.setNull(4, Types.JAVA_OBJECT);
				}
				if (prop_class != null) {
					pstmt8.setString(5, prop_class);
				}
				else {
					pstmt8.setNull(5, Types.JAVA_OBJECT);
				}
				if (mkt_area != null) {
					pstmt8.setString(6, mkt_area);
				}
				else {
					pstmt8.setNull(6, Types.JAVA_OBJECT);
				}
				if (master_fran != null) {
					pstmt8.setString(7, master_fran);
				}
				else {
					pstmt8.setNull(7, Types.JAVA_OBJECT);
				}
				if (cntrl_ofc != null) {
					pstmt8.setString(8, cntrl_ofc);
				}
				else {
					pstmt8.setNull(8, Types.JAVA_OBJECT);
				}
				if (mgt_company != null) {
					pstmt8.setString(9, mgt_company);
				}
				else {
					pstmt8.setNull(9, Types.JAVA_OBJECT);
				}
				if (country != null) {
					pstmt8.setString(10, country);
				}
				else {
					pstmt8.setNull(10, Types.JAVA_OBJECT);
				}
				if (ioc_region != null) {
					pstmt8.setString(11, ioc_region);
				}
				else {
					pstmt8.setNull(11, Types.JAVA_OBJECT);
				}
				if (prop_type != null) {
					pstmt8.setString(12, prop_type);
				}
				else {
					pstmt8.setNull(12, Types.JAVA_OBJECT);
				}
				if (filter_id != null) {
					pstmt8.setInt(13, filter_id);
				}
				else {
					pstmt8.setNull(13, Types.JAVA_OBJECT);
				}
				if (region_id != null) {
					pstmt8.setInt(14, region_id);
				}
				else {
					pstmt8.setNull(14, Types.JAVA_OBJECT);
				}
				if (chain_group_id != null) {
					pstmt8.setInt(15, chain_group_id);
				}
				else {
					pstmt8.setNull(15, Types.JAVA_OBJECT);
				}
				if (start_date != null) {
					pstmt8.setObject(16, start_date);
				}
				else {
					pstmt8.setNull(16, Types.JAVA_OBJECT);
				}
				if (stop_date != null) {
					pstmt8.setObject(17, stop_date);
				}
				else {
					pstmt8.setNull(17, Types.JAVA_OBJECT);
				}
				if (user_id != null) {
					pstmt8.setInt(18, user_id);
				}
				else {
					pstmt8.setNull(18, Types.JAVA_OBJECT);
				}
				if (user_id != null) {
					pstmt8.setInt(19, user_id);
				}
				else {
					pstmt8.setNull(19, Types.JAVA_OBJECT);
				}
				executeUpdate(pstmt8);
				pstmt8.close();				
				
				return ord;
			}

		}