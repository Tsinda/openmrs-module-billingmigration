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
package org.openmrs.module.billingmigration.api;

import java.util.Date;
import java.util.List;

import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(serviceService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface serviceService extends OpenmrsService {
    
	
	/**
	 * get all existing insurance policies and admit them
	 * @param date
	 * @return
	 */
	public Integer getExistingInsurancePolicy(Date date);
	
	/**
	 * create global bills corresponding to created admission
	 * @return
	 */
	public Integer createGlobalBills();
	
	/**
	 * update created consommations to created global bills
	 * @return
	 */
	public Integer updateConsommation();
	
	/**
	 * get all existing payments
	 * @return
	 */
	public Integer getPayments();
	
	/**
	 * set ALL as hop service to all old billed items before new release billing module
	 */
	public void updateBillItems();
	
	/**
	 * closes all bills done before the provided date and set the user as the user who closed them
	 * @param date
	 * @param user
	 * @return
	 */
	public Integer closeGlobalBills(Date date,User user);
	
	
}