<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="head.jsp">
        <jsp:param name="title" value="Alo Exchange - Login" />
    </jsp:include>
</head>
<body>
    <jsp:include page="nav.jsp">
        <jsp:param name="active" value="login" />
    </jsp:include>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-5 col-lg-4">
                <div class="card-alo p-4 mt-5">
                    <h2 class="page-title mb-4">Log in</h2>

                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        <input type="hidden" name="origin" value="${origin}">

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-alo mb-3" style="white-space: pre-line;">${error}</div>
                        </c:if>

                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" class="form-control form-control-alo" placeholder="you@example.com" name="email" value="john@doe.com" required>
                        </div>
                        <div class="mb-4">
                            <label class="form-label">Password</label>
                            <input type="password" class="form-control form-control-alo" placeholder="********" name="password" value="john.doe" required>
                        </div>
                        <button type="submit" class="btn btn-alo w-100">Sign in</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
