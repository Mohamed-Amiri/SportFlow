<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Membres</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <h1>Membres</h1>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
                ${errorMessage}
        </div>
    </c:if>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Date de Naissance</th>
            <th>Sport</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="membre" items="${membres}">
            <tr>
                <td>${membre.id}</td>
                <td>${membre.nom}</td>
                <td>${membre.dateNaissance}</td>
                <td>${membre.sport}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h2>Add Membre</h2>
    <form action="${pageContext.request.contextPath}/membres" method="post">
        <input type="hidden" name="action" value="add">
        <div class="form-group">
            <label for="nom">Nom:</label>
            <input type="text" class="form-control" id="nom" name="nom" required>
        </div>
        <div class="form-group">
            <label for="dateNaissance">Date de Naissance:</label>
            <input type="date" class="form-control" id="dateNaissance" name="dateNaissance" required>
        </div>
        <div class="form-group">
            <label for="sport">Sport:</label>
            <input type="text" class="form-control" id="sport" name="sport" required>
        </div>
        <button type="submit" class="btn btn-primary">Add</button>
    </form>

    <h2>Update Membre</h2>
    <form action="${pageContext.request.contextPath}/membres" method="post">
        <input type="hidden" name="action" value="update">
        <div class="form-group">
            <label for="id">ID to Update:</label>
            <input type="number" class="form-control" id="id" name="id" required>
        </div>
        <div class="form-group">
            <label for="nom">Nom:</label>
            <input type="text" class="form-control" id="nom" name="nom" required>
        </div>
        <div class="form-group">
            <label for="dateNaissance">Date de Naissance:</label>
            <input type="date" class="form-control" id="dateNaissance" name="dateNaissance" required>
        </div>
        <div class="form-group">
            <label for="sport">Sport:</label>
            <input type="text" class="form-control" id="sport" name="sport" required>
        </div>
        <button type="submit" class="btn btn-primary">Update</button>
    </form>

    <h2>Delete Membre</h2>
    <form action="${pageContext.request.contextPath}/membres" method="post">
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
