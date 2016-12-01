/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.billingmigration.api.db.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.User;
import org.openmrs.module.billingmigration.api.db.serviceDAO;

/**
 * It is a default implementation of  {@link serviceDAO}.
 */
public class HibernateserviceDAO implements serviceDAO {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
	    this.sessionFactory = sessionFactory;
    }
    
	/**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
	    return sessionFactory;
    }

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.db.serviceDAO#getExistingInsurancePolicy(java.util.Date)
	 */
	@Override
	public Integer getExistingInsurancePolicy(Date date) {
		Session session = sessionFactory.getCurrentSession();
			StringBuilder queryStr = new StringBuilder("");

			queryStr.append("select b.insurance_policy_id from moh_bill_beneficiary b,moh_bill_consommation c where c.beneficiary_id=b.beneficiary_id");
			
			SQLQuery query = session.createSQLQuery(queryStr.toString());
			List<Integer> policies = query.list();
			
			StringBuilder strb = new StringBuilder("");
			Date today = new Date();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String date1 = formatter.format(today);
			
			//admit all existing insurance policies
			strb.append("insert ignore into moh_bill_admission(insurance_policy_id,is_admitted,admission_date,discharging_date,discharged_by,creator,created_date,voided)");
			strb.append("values");
			
			int i=0;
			int total=policies.size();
			for (Integer policyId : policies) {
				i++;
				if(i<total)
				strb.append("("+policyId+","+1+",'"+date1+"'"+",'"+date1+"',"+1+","+1+",'"+date1+"',"+0+"),");
				else 
					strb.append("("+policyId+","+1+",'"+date1+"'"+",'"+date1+"',"+1+","+1+",'"+date1+"',"+0+")");
			}
			int result= session.createSQLQuery(strb.toString()).executeUpdate();
		return result;
		
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.db.serviceDAO#createGlobalBills()
	 */
	@Override
	public Integer createGlobalBills() {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder globalBillQuery = new StringBuilder("");
		//all global bills are closed!
		globalBillQuery.append("insert into moh_bill_global_bill(admission_id,bill_identifier,global_amount,creator,created_date,closed,voided)"
				+ "select c.consommation_id, CONCAT('bill',c.consommation_id),pb.amount,c.creator,c.created_date,0,0 "
				+ " from moh_bill_consommation c,moh_bill_patient_bill pb where pb.patient_bill_id=c.patient_bill_id");
		int globalBills = session.createSQLQuery(globalBillQuery.toString()).executeUpdate();
		return globalBills;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.db.serviceDAO#updateConsommation()
	 */
	@Override
	public Integer updateConsommation() {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder updateQuery = new StringBuilder("");
		//there was no link between migrated consommations with any global bills, global_bill_ids in consommations were NULL
		//TODO:global_bill_id=consommation_id because Consommation Ids have been used to create global bills, so they are equal
		updateQuery.append("UPDATE moh_bill_consommation SET global_bill_id=consommation_id");
		int updatedConsom = session.createSQLQuery(updateQuery.toString()).executeUpdate();
		return updatedConsom;
	}

	@Override
	public Integer getPayments() {
		Session session = sessionFactory.getCurrentSession();
		StringBuilder payQuery = new StringBuilder("");
		payQuery.append("select cash_payment_id from moh_bill_cash_payment");
		int payments = session.createSQLQuery(payQuery.toString()).list().size();
		return payments;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.db.serviceDAO#updateBillItems()
	 */
	@Override
	public void updateBillItems() {
		Session session = sessionFactory.getCurrentSession();
		//set MISC id as service_id to all existing patientservicebill
		StringBuilder updateQuery = new StringBuilder("");
		updateQuery.append("update moh_bill_patient_service_bill set service_id=(select service_id from moh_bill_hop_service where name='ALL')");

		//updateQuery.append("update moh_bill_patient_service_bill set service_id=22");
		session.createSQLQuery(updateQuery.toString()).executeUpdate();
		
		
		/*StringBuilder itemsStr = new StringBuilder("select patient_service_bill_id from moh_bill_patient_service_bill");
		List<Integer> items = session.createSQLQuery(itemsStr.toString()).list();*/
		
		//populate paidservicebill from patient service bill
		/**
		 * codes should be here
		 * risk: if marked as fullypaid it can block to add other payments in case there some partial payments
		 * if needed, do it from GUI
		 */

		//update paid quantity on each billed item, mark all requested quantity as full paid
		/*for (Integer itemId : items) {
			StringBuilder updateQ = new StringBuilder("UPDATE moh_bill_paid_service_bill paidSb JOIN moh_bill_patient_service_bill psb "
					+ "ON paidSb.patient_service_bill_id=psb.patient_service_bill_id "
					+ "SET paidSb.paid_quantity = psb.quantity where paidSb.psb.patient_service_bill_id="+itemId);
			session.createSQLQuery(updateQ.toString()).executeUpdate();
		}*/
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.db.serviceDAO#closeGlobalBills(java.util.Date, org.openmrs.User)
	 */
	@Override
	public Integer closeGlobalBills(Date date,User user) {
		Session session = sessionFactory.getCurrentSession();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String datef = df.format(date);
		StringBuilder updateQuery = new StringBuilder("update moh_bill_global_bill set closed="+user.getUserId()+",closed_by="+user.getUserId()+", closing_date='"+datef+"'");
		return session.createSQLQuery(updateQuery.toString()).executeUpdate();
	}

}