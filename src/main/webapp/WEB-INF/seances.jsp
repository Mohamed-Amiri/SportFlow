<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Seances</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <h1>Seances</h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
                ${errorMessage}
        </div>
    </c:if>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>ID Membre</th>
            <th>ID Entraineur</th>
            <th>Date Heure</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="seance" items="${seances}">
            <tr>
                <td>${seance.id}</td>
                <td>${seance.idMembre}</td>
                <td>${seance.idEntraineur}</td>
                <td>${seance.dateHeure}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h2>Add Seance</h2>
    <form action="${pageContext.request.contextPath}/seances" method="post">
        <input type="hidden" name="action" value="add">
        <div class="form-group">
            <label for="idMembre">ID Membre:</label>
            <input type="number" class="form-control" id="idMembre" name="idMembre" required>
        </div>
        <div class="form-group">
            <label for="idEntraineur">ID Entraineur:</label>
            <input type="number" class="form-control" id="idEntraineur" name="idEntraineur" required>
        </div>
        <div class="form-group">
            <label for="dateHeure">Date Heure:</label>
            <input type="datetime-local" class="form-control" id="dateHeure" name="dateHeure" required>
        </div>
        <button type="submit" class="btn btn-primary">Add</button>
    </form>

    <h2>Update Seance</h2>
    <form action="${pageContext.request.contextPath}/seances" method="post">
        <input type="hidden" name="action" value="update">
        <div class="form-group">
            <label for="id">ID to Update:</label>
            <input type="number" class="form-control" id="id" name="id" required>
        </div>
        <div class="form-group">
            <label for="idMembre">ID Membre:</label>
            <input type="number" class="form-control" id="idMembre" name="idMembre" required>
        </div>
        <div class="form-group">
            <label for="idEntraineur">ID Entraineur:</label>
            <input type="number" class="form-control" id="idEntraineur" name="idEntraineur" required>
        </div>
        <div class="form-group">
            <label for="dateHeure">Date Heure:</label>
            <input type="datetime-local" class="form-control" id="dateHeure" name="dateHeure" required>
        </div>
        <button type="submit" class="btn btn-primary">Update</button>
    </form>

    <h2>Delete Seance</h2>
    <form action="${pageContext.request.contextPath}/seances" method="post">
        <input type="hidden" name="action" value="delete">
        <div class="form-group">
            <label for="id">ID to Delete:</label>
            <input type="number" class="form-control" id="id" name="id" required>
        </div>
        <button type="submit" class="btn btn-danger">Delete</button>
    </form>
</div>
</body>
</html>
