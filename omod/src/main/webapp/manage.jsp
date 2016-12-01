<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />

<form action="dataImport.list" method="post">
<table>
 <tr>
 <td>Date: </td>
  <td> <input type="text" size="11" value="" name="importDate" onclick="showCalendar(this)" /></td>
 <td><input type="submit" value="Submit"/></td>
 <td><p style="font-style: italic; color: red;">(Note: All Patient Bills done before provided date are closed!)</p></td>
 </tr>
</table>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>