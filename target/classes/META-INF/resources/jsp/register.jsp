<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="head.jsp">
        <jsp:param name="title" value="Alo Exchange - Register" />
    </jsp:include>
</head>
<body>
    <jsp:include page="nav.jsp">
        <jsp:param name="active" value="register" />
    </jsp:include>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card-alo p-4 mt-5">
                    <h2 class="page-title mb-4">Create account</h2>

                    <form action="${pageContext.request.contextPath}/register" method="post">
                        <c:if test="${not empty message}">
                            <div class="alert alert-success alert-alo mb-3" style="white-space: pre-line;">${message}</div>
                        </c:if>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-alo mb-3" style="white-space: pre-line;">${error}</div>
                        </c:if>

                        <div class="row g-3">
                            <div class="col-6">
                                <label class="form-label">First name</label>
                                <input type="text" class="form-control form-control-alo" placeholder="John" name="firstname" value="${registration.firstName}">
                            </div>
                            <div class="col-6">
                                <label class="form-label">Last name</label>
                                <input type="text" class="form-control form-control-alo" placeholder="Doe" name="lastname" value="${registration.lastName}">
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" class="form-control form-control-alo" placeholder="you@example.com" name="email" value="${registration.email}">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Password</label>
                            <input type="password" class="form-control form-control-alo" placeholder="********" name="password">
                        </div>
                        <div class="mb-4">
                            <label class="form-label">Confirm password</label>
                            <input type="password" class="form-control form-control-alo" placeholder="********" name="confirmpassword">
                        </div>
                        <button type="submit" class="btn btn-alo w-100">Register</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
