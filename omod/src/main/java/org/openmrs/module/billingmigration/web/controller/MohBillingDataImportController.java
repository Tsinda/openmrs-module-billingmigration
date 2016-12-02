package org.openmrs.module.billingmigration.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.billingmigration.api.serviceService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class MohBillingDataImportController extends ParameterizableViewController {
	protected final Log log = LogFactory.getLog(getClass());
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());


		String importDateStr = request.getParameter("importDate");
		Date importDate = null;Integer policyList = null;
		Integer globalBills=null;Integer updatedCons=null;
		Integer payments=null;Integer closedBills = null;
		
			if(importDateStr!=null && !importDateStr.equals("")){
				importDate = Context.getDateFormat().parse(importDateStr);
				policyList=Context.getService(serviceService.class).getExistingInsurancePolicy(importDate);
				globalBills=Context.getService(serviceService.class).createGlobalBills();
				updatedCons=Context.getService(serviceService.class).updateConsommation();
				payments=Context.getService(serviceService.class).getPayments();
				Context.getService(serviceService.class).updateBillItems();
				closedBills=Context.getService(serviceService.class).closeGlobalBills(importDate, Context.getAuthenticatedUser());
				
			}
			mav.addObject("policyList", policyList);
			mav.addObject("importDate", importDateStr);
			mav.addObject("globalBills", globalBills);
			mav.addObject("updatedCons", updatedCons);
			mav.addObject("payments", payments);
			mav.addObject("closedBills", closedBills);
		return mav;
	}

}
