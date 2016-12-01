<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<%@ taglib prefix="fn" uri="/WEB-INF/taglibs/fn.tld"%>
<c:if test="${importDate!=null}"><p style="font-style: italic; color: red;">No Data Imported Yet...</p>
</c:if>
<c:if test="${importDate!=null}">
<p style="color: blue;font-style: italic;">Billing Data imported succefully...</p>
<p>Date: <b>${importDate}</b></p>
<p><b>${policyList }</b> insurance policies imported</p>
<p><b>${updatedCons }</b> patient bills and <b>${globalBills }</b> global bills created</p>
<p><b>${payments }</b> payments imported</p>
<p><b style="color: red;">${closedBills } closed bills</b></p>
</c:if>
<%@ include file="/WEB-INF/template/footer.jsp"%>ml>