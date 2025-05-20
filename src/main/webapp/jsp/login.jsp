<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <head>
        <title>Alo Exchange - Login</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
    </head>

    <body>
        <jsp:include page="nav.jsp" />
        <div class="container">
            <form action="/exchange/login" method="POST">
                <input type="hidden" name="origin" value="${origin}">
                
                <p class="my-3 fs-4">Log in</p>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger" style="white-space: pre-line;">
                        ${error}
                    </div>
                </c:if>

                <div class="mb-3">
                    <input type="text" class="form-control rounded-0" placeholder="email" name="email" value="john@doe.com">
                </div>

                <div class="mb-3">
                    <input type="password" class="form-control rounded-0" placeholder="password" name="password" value="john.doe">
                </div>

                <div class="d-flex justify-content-end">
                    <button type="submit" class="btn btn-primary rounded-0 w-25" name="button" value="login">Sign
                        in</button>
                </div>
            </form>
        </div>

        <script src="js/bootstrap.min.js"></script>
    </body>

    </html>