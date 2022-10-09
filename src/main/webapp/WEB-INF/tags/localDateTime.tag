<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="date" required="true" type="java.time.LocalDateTime" %>
<%@ attribute name="pattern" required="false" type="java.lang.String" %>

<fmt:parseDate value="${date}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="date"/>
<fmt:formatDate value="${parsedDateTime}" type="date" pattern="${pattern}"/>