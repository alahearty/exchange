<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-expand-lg navbar-alo">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Alo Exchange</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link ${param.active == 'home' ? 'active' : ''}" href="${pageContext.request.contextPath}/home">Home</a>
                </li>
                <c:if test="${authenticateduser != null}">
                    <li class="nav-item">
                        <a class="nav-link ${param.active == 'offers' ? 'active' : ''}" href="${pageContext.request.contextPath}/my-offers">Offers</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.active == 'interests' ? 'active' : ''}" href="${pageContext.request.contextPath}/listing/interest">Interests</a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${authenticateduser == null}">
                        <li class="nav-item">
                            <a class="nav-link ${param.active == 'login' ? 'active' : ''}" href="${pageContext.request.contextPath}/login">Login</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link btn btn-alo btn-sm ms-2 ${param.active == 'register' ? 'active' : ''}" href="${pageContext.request.contextPath}/register" style="color:white!important">Register</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <form action="${pageContext.request.contextPath}/logout" method="post" class="d-inline">
                                <button type="submit" class="btn-logout">Logout ${authenticateduser.email}</button>
                            </form>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
