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

public class erwin-procs extends AbstractProcedure {

	public void execute(Integer cust_id, String action) throws SQLException, ProcedureException {		
		
		Integer acct_id;		
		String acct_status;		
		
		acct_id = null;		
		acct_status = null;		
		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select cust_acct.acct_id, acct.acct_status"
				+ " from cust_acct, acct"
				+ " where cust_acct.cust_id = ?"
				+ " and acct.acct_id = cust_acct.acct_id"
				+ " and cust_acct.primary_cust = \"Y\"");
		
		if (cust_id != null) {
			pstmt1.setInt(1, cust_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		while (rs1.next()) {
			acct_id = rs1.getInt(1);
			acct_status = rs1.getString(2);			
			
			new sync_acct().execute(acct_id, acct_status, action);
		}
		pstmt1.close();
	}

	public void execute(Integer acct_id, String acct_status, String action) throws SQLException, ProcedureException {		
		
		String external_pgm;		
		String recog_cd;		
		
		external_pgm = null;		
		recog_cd = new get_recog_cd().execute(acct_id);		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select p.external_pgm"
				+ " from recog_pgm p"
				+ " where p.recog_cd = ?");
		
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
		
		if (external_pgm.equals("Y") || recog_cd.equals("EC")) {			
			return;
		}		
		
		if (action.equals("N") && !acct_status.equals("A")) {			
			return;
		}		
		
		if (action.equals("U") && (!acct_status.equals("A") && !acct_status.equals("I") && !acct_status.equals("T"))) {			
			return;
		}		
		
		if (action.equals("D")) {			
			return;
		}		
		

		PreparedStatement pstmt2 = prepareInsert(
				  "insert into acct_sync_out (sync_id, acct_id, action, event_dtime)"
				+ " values (0, ?, ?, current)");
		if (acct_id != null) {
			pstmt2.setInt(1, acct_id);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (action != null) {
			pstmt2.setString(2, action);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt2);
		pstmt2.close();
	}

	public Integer execute(String recog_cd, String recog_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the account id of the specified account.
		 */		
		
		Integer acct_id;		
		
		acct_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct.acct_id"
				+ " from acct"
				+ " where acct.recog_cd = ?"
				+ " and acct.recog_id = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (recog_id != null) {
			pstmt1.setString(2, recog_id);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		acct_id = rs1.getInt(1);
		pstmt1.close();		
		
		return acct_id;
	}

	public String execute(Integer acct_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the recog code corresponding to the specified account id.
		 */		
		
		String recog_cd;		
		
		recog_cd = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct.recog_cd"
				+ " from acct"
				+ " where acct.acct_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		recog_cd = rs1.getString(1);
		pstmt1.close();		
		
		return recog_cd;
	}

	public String execute(Integer acct_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the recog id corresponding to the specified account id.
		 */		
		
		String recog_id;		
		
		recog_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct.recog_id"
				+ " from acct"
				+ " where acct.acct_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		recog_id = rs1.getString(1);
		pstmt1.close();		
		
		return recog_id;
	}

	public String execute(Integer acct_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the account status of the specified account.
		 */		
		
		String acct_status;		
		
		acct_status = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct.acct_status"
				+ " from acct"
				+ " where acct.acct_id = ?");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		acct_status = rs1.getString(1);
		pstmt1.close();		
		
		if (acct_status == null) {			
			throw new ProcedureException(-746, 0, "get_acct_status: Account not found.");
		}		
		
		return acct_status;
	}

	public String execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the status of the specified account transaction.
		 */		
		
		String trans_status;		
		
		trans_status = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select acct_trans.trans_status"
				+ " from acct_trans"
				+ " where acct_trans.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		trans_status = rs1.getString(1);
		pstmt1.close();		
		
		if (trans_status == null) {			
			throw new ProcedureException(-746, 0, "get_acct_trans_sta: Account transaction not found.");
		}		
		
		return trans_status;
	}

	public Integer execute(String recog_cd, String appl_group_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the application group id corresponding to the
		 *   specified application group code for the specified 
		 *   program.
		 */		
		
		Integer appl_group_id;		
		
		appl_group_id = null;		
		
		//lookup the appl_group_id by recog_cd and appl_group_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select appl_group.appl_group_id"
				+ " from appl_group"
				+ " where appl_group.recog_cd = ?"
				+ " and appl_group.appl_group_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (appl_group_cd != null) {
			pstmt1.setString(2, appl_group_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		appl_group_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the application group id
		return appl_group_id;
	}

	public Double execute(String curr_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the currency conversion rate for the specified currency.
		 * 
		 *   The conversion rate will be the most recent average rate.
		 */		
		
		Double conv_rate;		
		
		conv_rate = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select avg_rate"
				+ " from currency_conv"
				+ " where currency_conv.curr_cd = ?"
				+ " and currency_conv.conv_date = ("
					+ "select max(currency_conv.conv_date)"
					+ " from currency_conv"
					+ " where currency_conv.curr_cd = ?"
				+ ")");
		
		if (curr_cd != null) {
			pstmt1.setString(1, curr_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (curr_cd != null) {
			pstmt1.setString(2, curr_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		conv_rate = rs1.getDouble(1);
		pstmt1.close();		
		
		return conv_rate;
	}

	public String execute(Integer cust_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the customer name corresponding to the specified customer id.
		 */		
		
		String cust_name;		
		
		cust_name = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select trim(cust.frst_name) || \" \" || trim(cust.last_name)"
				+ " from cust"
				+ " where cust.cust_id = ?");
		
		if (cust_id != null) {
			pstmt1.setInt(1, cust_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		cust_name = rs1.getString(1);
		pstmt1.close();		
		
		return cust_name;
	}

	public Integer execute(Integer acct_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the primary customer id associated with the specified account.
		 */		
		
		Integer cust_id;		
		
		cust_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select cust_acct.cust_id"
				+ " from cust_acct"
				+ " where cust_acct.acct_id = ?"
				+ " and cust_acct.primary_cust = \"Y\"");
		
		if (acct_id != null) {
			pstmt1.setInt(1, acct_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		cust_id = rs1.getInt(1);
		pstmt1.close();		
		
		if (cust_id == null) {			
			throw new ProcedureException(-746, 0, "get_pri_cust_id: unable to get primary customer for account");
		}		
		
		return cust_id;
	}

	public Integer execute(Integer cust_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the preferred account id associated with the specified customer.
		 */		
		
		Integer acct_id;		
		
		acct_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select cust_acct.acct_id"
				+ " from cust_acct"
				+ " where cust_acct.cust_id = ?"
				+ " and cust_acct.pref_acct = \"Y\"");
		
		if (cust_id != null) {
			pstmt1.setInt(1, cust_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		acct_id = rs1.getInt(1);
		pstmt1.close();		
		
		return acct_id;
	}

	public Integer execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the cust call id associated with the specified transaction.
		 */		
		
		Integer cust_call_id;		
		
		cust_call_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select cust_call.cust_call_id"
				+ " from cust_call"
				+ " where cust_call.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		cust_call_id = rs1.getInt(1);
		pstmt1.close();		
		
		return cust_call_id;
	}

	public Integer execute(String location_name) throws SQLException, ProcedureException {		
		
		/*
		 * Add a location and return the corresponding location id.  If
		 *   the location already exists just return the location id.
		 */		
		
		Integer location_id;		
		
		location_id = null;		
		
		location_name = upper(location_name);		
		
		//lookup the location_id by location_name

		PreparedStatement pstmt1 = prepareStatement(
				  "select location.location_id"
				+ " from location"
				+ " where location.location_name = ?");
		
		if (location_name != null) {
			pstmt1.setString(1, location_name);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		location_id = rs1.getInt(1);
		pstmt1.close();		
		
		//if the record does not exist insert a new record
		if (dbinfo("sqlca.sqlerrd2").equals(0)) {			

			PreparedStatement pstmt2 = prepareInsert(
					  "insert into location (location_id, location_name)"
					+ " values (0, ?)");
			if (location_name != null) {
				pstmt2.setString(1, location_name);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt2);
			pstmt2.close();			
			location_id = dbinfo("sqlca.sqlerrd1");
		}		
		
		//return the new or existing location_id
		return location_id;
	}

	public Integer execute(String location_name) throws SQLException, ProcedureException {		
		
		/*
		 * Get the location id corresponding to the specified location name.
		 */		
		
		Integer location_id;		
		
		location_id = null;		
		
		location_name = upper(location_name);		
		
		//lookup the location_id by location_name

		PreparedStatement pstmt1 = prepareStatement(
				  "select location.location_id"
				+ " from location"
				+ " where location.location_name = ?");
		
		if (location_name != null) {
			pstmt1.setString(1, location_name);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		location_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the location_id
		return location_id;
	}

	public String execute(Integer location_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the location name corresponding to the specified location id.
		 */		
		
		String location_name;		
		
		location_name = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select location.location_name"
				+ " from location"
				+ " where location.location_id = ?");
		
		if (location_id != null) {
			pstmt1.setInt(1, location_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		location_name = rs1.getString(1);
		pstmt1.close();		
		
		return location_name;
	}

	public Integer execute(String recog_cd, String offer_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the offer id corresponding to the
		 *   specified offer code for the specified 
		 *   program.
		 */		
		
		Integer offer_id;		
		
		offer_id = null;		
		
		//lookup the offer_id by recog_cd and offer_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select offer.offer_id"
				+ " from offer"
				+ " where offer.recog_cd = ?"
				+ " and offer.offer_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (offer_cd != null) {
			pstmt1.setString(2, offer_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		offer_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the offer id
		return offer_id;
	}

	public String execute(Integer par_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the status of the specified partner activity request.
		 */		
		
		String request_status;		
		
		request_status = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select par.request_status"
				+ " from par"
				+ " where par.par_id = ?");
		
		if (par_id != null) {
			pstmt1.setInt(1, par_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		request_status = rs1.getString(1);
		pstmt1.close();		
		
		if (request_status == null) {			
			throw new ProcedureException(-746, 0, "get_par_status: Partner activity request not found.");
		}		
		
		return request_status;
	}

	public Integer execute(String partner_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the partner id corresponding to the
		 *   specified partner code.
		 */		
		
		Integer partner_id;		
		
		partner_id = null;		
		
		//lookup the partner_id by partner_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select partner.partner_id"
				+ " from partner"
				+ " where partner.partner_cd = ?");
		
		if (partner_cd != null) {
			pstmt1.setString(1, partner_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		partner_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the partner id
		return partner_id;
	}

	public Integer execute(String recog_cd, String promo_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the promotion id corresponding to the
		 *   specified promotion code for the specified 
		 *   program.
		 */		
		
		Integer promo_id;		
		
		promo_id = null;		
		
		//lookup the promo_id by recog_cd and promo_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select promo.promo_id"
				+ " from promo"
				+ " where promo.recog_cd = ?"
				+ " and promo.promo_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (promo_cd != null) {
			pstmt1.setString(2, promo_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		promo_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the promotion id
		return promo_id;
	}

	public Integer execute(String prop_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the most current property id of the specified property code.
		 */		
		
		Integer prop_id;		
		
		prop_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select prop.prop_id"
				+ " from prop"
				+ " where prop.prop_cd = ?"
				+ " and prop.eff_date = ("
					+ "select max(prop.eff_date)"
					+ " from prop"
					+ " where prop.prop_cd = ?"
				+ ")");
		
		if (prop_cd != null) {
			pstmt1.setString(1, prop_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (prop_cd != null) {
			pstmt1.setString(2, prop_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		prop_id = rs1.getInt(1);
		pstmt1.close();		
		
		return prop_id;
	}

	public Integer execute(String prop_cd, Timestamp eff_date) throws SQLException, ProcedureException {		
		
		/*
		 * Get the most current property id of the specified property code
		 *   for the specified date.
		 */		
		
		Integer prop_id;		
		
		prop_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select prop.prop_id"
				+ " from prop"
				+ " where prop.prop_cd = ?"
				+ " and prop.eff_date = ("
					+ "select max(prop.eff_date)"
					+ " from prop"
					+ " where prop.prop_cd = ?"
					+ " and prop.eff_date <= ?"
				+ ")");
		
		if (prop_cd != null) {
			pstmt1.setString(1, prop_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (prop_cd != null) {
			pstmt1.setString(2, prop_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (eff_date != null) {
			pstmt1.setObject(3, eff_date);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		prop_id = rs1.getInt(1);
		pstmt1.close();		
		
		return prop_id;
	}

	public String execute(Integer prop_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the property name corresponding to the specified property id.
		 */		
		
		String prop_name;		
		
		prop_name = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select prop.name"
				+ " from prop"
				+ " where prop.prop_id = ?");
		
		if (prop_id != null) {
			pstmt1.setInt(1, prop_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		prop_name = rs1.getString(1);
		pstmt1.close();		
		
		return prop_name;
	}

	public String execute(Integer prop_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the property code corresponding to the specified property id.
		 */		
		
		String prop_cd;		
		
		prop_cd = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select prop.prop_cd"
				+ " from prop"
				+ " where prop.prop_id = ?");
		
		if (prop_id != null) {
			pstmt1.setInt(1, prop_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		prop_cd = rs1.getString(1);
		pstmt1.close();		
		
		return prop_cd;
	}

	public Integer execute(String recog_cd, String question_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the question id corresponding to the
		 *   specified question code for the specified
		 *   program.
		 */		
		
		Integer question_id;		
		
		question_id = null;		
		
		//lookup the question_id by recog_cd and question_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select question.question_id"
				+ " from question"
				+ " where question.recog_cd = ?"
				+ " and question.question_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (question_cd != null) {
			pstmt1.setString(2, question_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		question_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the promotion id
		return question_id;
	}

	public Integer execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the redemption id associated with the specified transaction.
		 */		
		
		Integer redemption_id;		
		
		redemption_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select redemption.redemption_id"
				+ " from redemption"
				+ " where redemption.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		redemption_id = rs1.getInt(1);
		pstmt1.close();		
		
		return redemption_id;
	}

	public Integer execute(String recog_cd, String response_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the response id corresponding to the
		 *   specified response code for the specified
		 *   program.
		 */		
		
		Integer response_id;		
		
		response_id = null;		
		
		//lookup the response_id by recog_cd and response_cd

		PreparedStatement pstmt1 = prepareStatement(
				  "select response.response_id"
				+ " from response"
				+ " where response.recog_cd = ?"
				+ " and response.response_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (response_cd != null) {
			pstmt1.setString(2, response_cd);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		response_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the promotion id
		return response_id;
	}

	public Integer execute(String seq_nbr_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the next sequence number for the specified code.
		 */		
		
		Integer current_val;		
		Integer next_val;		
		Integer min_val;		
		Integer max_val;		
		Integer error_val;		
		String rollover;		
		
		current_val = null;		
		next_val = null;		
		min_val = null;		
		max_val = null;		
		error_val = null;		
		rollover = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select seq_nbr_cntrl.next_val, seq_nbr_cntrl.min_val, seq_nbr_cntrl.max_val, seq_nbr_cntrl.error_val, seq_nbr_cntrl.rollover"
				+ " from seq_nbr_cntrl"
				+ " where seq_nbr_cntrl.seq_nbr_cd = ?");
		
		if (seq_nbr_cd != null) {
			pstmt1.setString(1, seq_nbr_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		next_val = rs1.getInt(1);
		min_val = rs1.getInt(2);
		max_val = rs1.getInt(3);
		error_val = rs1.getInt(4);
		rollover = rs1.getString(5);
		pstmt1.close();		
		
		if (next_val.equals(error_val)) {			
			return error_val;
		}		
		
		current_val = next_val;		
		
		next_val = next_val + 1;		
		if (next_val > max_val) {			
			if (rollover.equals("Y")) {				
				next_val = min_val;
			}			
			else {				
				next_val = error_val;
			}
		}		
		

		PreparedStatement pstmt2 = prepareStatement(
				  "update seq_nbr_cntrl"
				+ " set seq_nbr_cntrl.next_val = ?"
				+ " where seq_nbr_cntrl.seq_nbr_cd = ?");
		
		if (next_val != null) {
			pstmt2.setInt(1, next_val);
		}
		else {
			pstmt2.setNull(1, Types.JAVA_OBJECT);
		}
		if (seq_nbr_cd != null) {
			pstmt2.setString(2, seq_nbr_cd);
		}
		else {
			pstmt2.setNull(2, Types.JAVA_OBJECT);
		}
		executeUpdate(pstmt2);
		pstmt2.close();		
		
		return current_val;
	}

	public Integer execute(Integer acct_trans_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the stay id associated with the specified transaction.
		 */		
		
		Integer stay_id;		
		
		stay_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select stay.stay_id"
				+ " from stay"
				+ " where stay.acct_trans_id = ?");
		
		if (acct_trans_id != null) {
			pstmt1.setInt(1, acct_trans_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		stay_id = rs1.getInt(1);
		pstmt1.close();		
		
		return stay_id;
	}

	public String execute(Integer std_desc_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the client description corresponding to the specified
		 *   standard description id.
		 */		
		
		String client_desc;		
		
		client_desc = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select std_desc.client_desc"
				+ " from std_desc"
				+ " where std_desc.std_desc_id = ?");
		
		if (std_desc_id != null) {
			pstmt1.setInt(1, std_desc_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		client_desc = rs1.getString(1);
		pstmt1.close();		
		
		return client_desc;
	}

	public String execute(Integer std_desc_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the user description corresponding to the specified
		 *   standard description id.
		 */		
		
		String user_desc;		
		
		user_desc = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select std_desc.user_desc"
				+ " from std_desc"
				+ " where std_desc.std_desc_id = ?");
		
		if (std_desc_id != null) {
			pstmt1.setInt(1, std_desc_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		user_desc = rs1.getString(1);
		pstmt1.close();		
		
		return user_desc;
	}

	public Integer execute(String recog_cd, String clazz, String subclass, String std_desc_cd) throws SQLException, ProcedureException {		
		
		/*
		 * Get the standard description id of the specified standard
		 *   description.
		 */		
		
		Integer std_desc_id;		
		
		std_desc_id = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select std_desc.std_desc_id"
				+ " from std_desc"
				+ " where std_desc.recog_cd = ?"
				+ " and std_desc.class = ?"
				+ " and std_desc.subclass = ?"
				+ " and std_desc.std_desc_cd = ?");
		
		if (recog_cd != null) {
			pstmt1.setString(1, recog_cd);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		if (clazz != null) {
			pstmt1.setString(2, clazz);
		}
		else {
			pstmt1.setNull(2, Types.JAVA_OBJECT);
		}
		if (subclass != null) {
			pstmt1.setString(3, subclass);
		}
		else {
			pstmt1.setNull(3, Types.JAVA_OBJECT);
		}
		if (std_desc_cd != null) {
			pstmt1.setString(4, std_desc_cd);
		}
		else {
			pstmt1.setNull(4, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		std_desc_id = rs1.getInt(1);
		pstmt1.close();		
		
		return std_desc_id;
	}

	public Integer execute(String user_name) throws SQLException, ProcedureException {		
		
		/*
		 * Add a user and return the corresponding user id.  If
		 *   the user already exists just return the user id.
		 */		
		
		Integer user_id;		
		
		user_id = null;		
		
		user_name = upper(user_name);		
		
		//lookup the user_id by user_name

		PreparedStatement pstmt1 = prepareStatement(
				  "select user.user_id"
				+ " from user"
				+ " where user.user_name = ?");
		
		if (user_name != null) {
			pstmt1.setString(1, user_name);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		user_id = rs1.getInt(1);
		pstmt1.close();		
		
		//if the record does not exist insert a new record
		//otherwise just update the timestamp
		if (dbinfo("sqlca.sqlerrd2").equals(0)) {			

			PreparedStatement pstmt2 = prepareInsert(
					  "insert into user (user_id, user_name, last_access_dtime)"
					+ " values (0, ?, current)");
			if (user_name != null) {
				pstmt2.setString(1, user_name);
			}
			else {
				pstmt2.setNull(1, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt2);
			pstmt2.close();			
			user_id = dbinfo("sqlca.sqlerrd1");
		}		
		else {			

			PreparedStatement pstmt3 = prepareStatement(
					  "update user"
					+ " set user.last_access_dtime = current"
					+ " where user.user_id = ?");
			
			if (user_id != null) {
				pstmt3.setInt(1, user_id);
			}
			else {
				pstmt3.setNull(1, Types.JAVA_OBJECT);
			}
			executeUpdate(pstmt3);
			pstmt3.close();
		}		
		
		//return the new or existing user_id
		return user_id;
	}

	public String execute(Integer user_id) throws SQLException, ProcedureException {		
		
		/*
		 * Get the user name corresponding to the specified user id.
		 */		
		
		String user_name;		
		
		user_name = null;		
		

		PreparedStatement pstmt1 = prepareStatement(
				  "select user.user_name"
				+ " from user"
				+ " where user.user_id = ?");
		
		if (user_id != null) {
			pstmt1.setInt(1, user_id);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		user_name = rs1.getString(1);
		pstmt1.close();		
		
		return user_name;
	}

	public Integer execute(String user_name) throws SQLException, ProcedureException {		
		
		/*
		 * Get the user id corresponding to the specified user name.
		 */		
		
		Integer user_id;		
		
		user_id = null;		
		
		user_name = upper(user_name);		
		
		//lookup the user_id by user_name

		PreparedStatement pstmt1 = prepareStatement(
				  "select user.user_id"
				+ " from user"
				+ " where user.user_name = ?");
		
		if (user_name != null) {
			pstmt1.setString(1, user_name);
		}
		else {
			pstmt1.setNull(1, Types.JAVA_OBJECT);
		}
		ResultSet rs1 = executeQuery(pstmt1);
		rs1.next();
		user_id = rs1.getInt(1);
		pstmt1.close();		
		
		//return the user_id
		return user_id;
	}

}