<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <head>
        <title>Alo Exchange - Register</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
    </head>

    <body>
        <jsp:include page="nav.jsp" />
        <div class="container">
            <form action="/exchange/register" method="POST">
                <p class="my-3 fs-4">Register</p>

                <c:if test="${not empty message}">
                    <div class="alert alert-success" style="white-space: pre-line;">
                        ${message}
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger" style="white-space: pre-line;">
                        ${error}
                    </div>
                </c:if>
    
                <div class="mb-3">
                    <input type="text" class="form-control rounded-0" placeholder="first name" name="firstname" value="${registration.firstName}">
                </div>
    
                <div class="mb-3">
                    <input type="text" class="form-control rounded-0" placeholder="last name" name="lastname" value="${registration.lastName}">
                </div>
    
                <div class="mb-3">
                    <input type="text" class="form-control rounded-0" placeholder="email" name="email" value="${registration.email}">
                </div>
    
                <div class="mb-3">
                    <input type="password" class="form-control rounded-0" placeholder="password" name="password">
                </div>
    
                <div class="mb-3">
                    <input type="password" class="form-control rounded-0" placeholder="confirm password" name="confirmpassword">
                </div>
    
                <div class="d-flex justify-content-end">
                    <button type="submit" class="btn btn-primary rounded-0 w-25" name="button" value="login">Register</button>
                </div>
            </form>
        </div>

        <script src="js/bootstrap.min.js"></script>
    </body>

    </html>