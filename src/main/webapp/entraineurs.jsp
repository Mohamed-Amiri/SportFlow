<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Entraineurs</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="container">
    <h1>Entraineurs</h1>

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
            <th>Specialite</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="entraineur" items="${entraineurs}">
            <tr>
                <td>${entraineur.id}</td>
                <td>${entraineur.nom}</td>
                <td>${entraineur.specialite}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h2>Add Entraineur</h2>
    <form action="${pageContext.request.contextPath}/entraineurs" method="post">
        <input type="hidden" name="action" value="add">
        <div class="form-group">
            <label for="nom">Nom:</label>
            <input type="text" class="form-control" id="nom" name="nom" required>
        </div>
        <div class="form-group">
            <label for="specialite">Specialite:</label>
            <input type="text" class="form-control" id="specialite" name="specialite" required>
        </div>
        <button type="submit" class="btn btn-primary">Add</button>
    </form>

    <h2>Update Entraineur</h2>
    <form action="${pageContext.request.contextPath}/entraineurs" method="post">
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
            <label for="specialite">Specialite:</label>
            <input type="text" class="form-control" id="specialite" name="specialite" required>
        </div>
        <button type="submit" class="btn btn-primary">Update</button>
    </form>

    <h2>Delete Entraineur</h2>
    <form action="${pageContext.request.contextPath}/entraineurs" method="post">
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