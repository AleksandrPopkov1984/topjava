<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>

<form method="POST" action='meals' name="frmAddMeal">

    <input type="hidden" name="mealId" value="<c:out value="${meal.mealId}" />"/>

    DateTime: <input type="text" name="dateTime" style=position:absolute;left:12% size="30"
                     value="<tags:localDateTime pattern="yyyy-MM-dd HH:mm" date="${meal.dateTime}" />"/>
    <br/><br/>
    Description: <input type="text" name="description" style=position:absolute;left:12% size="50"
                        value="<c:out value="${meal.description}" />"/>
    <br/><br/>
    Calories: <input type="text" name="calories" style=position:absolute;left:12% size="25"
                     value="<c:out value="${meal.calories}" />"/>
    <br/><br/>
    <input type="submit" value="Save"/>
    <input type="button" value="Cancel" onClick="window.location.href='meals'"/>
</form>

</body>
</html>