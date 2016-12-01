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
package org.openmrs.module.billingmigration.api.impl;

import java.util.Date;
import java.util.List;

import org.openmrs.User;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.billingmigration.api.serviceService;
import org.openmrs.module.billingmigration.api.db.serviceDAO;

/**
 * It is a default implementation of {@link serviceService}.
 */
public class serviceServiceImpl extends BaseOpenmrsService implements serviceService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private serviceDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(serviceDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public serviceDAO getDao() {
	    return dao;
    }

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.serviceService#getExistingInsurancePolicy(java.util.Date)
	 */
	@Override
	public Integer getExistingInsurancePolicy(Date date) {
		return dao.getExistingInsurancePolicy(date);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.serviceService#createGlobalBills()
	 */
	@Override
	public Integer createGlobalBills() {
		return dao.createGlobalBills();
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.serviceService#updateConsommation()
	 */
	@Override
	public Integer updateConsommation() {
		return dao.updateConsommation();
	}

	@Override
	public Integer getPayments() {
		return dao.getPayments();
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.serviceService#updateBillItems()
	 */
	@Override
	public void updateBillItems() {
		dao.updateBillItems();
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.billingmigration.api.serviceService#closeGlobalBills(java.util.Date, org.openmrs.User)
	 */
	@Override
	public Integer closeGlobalBills(Date date,User user) {
		return dao.closeGlobalBills(date,user);
	}
	
	
}