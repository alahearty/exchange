<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <nav class=" navbar navbar-expand-lg bg-body-secondary mb-5">
        <div class="container">
            <a class="navbar-brand" href="/exchange/home">Alo Exchange</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
                aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNavDropdown">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/exchange/home">Home</a>
                    </li>
                    <c:if test="${authenticateduser != null}">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="/exchange/my-offers">Offers</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="/exchange/listing/interest">Interests</a>
                        </li>
                    </c:if>
                </ul>
                <ul class="navbar-nav ms-auto">
                    <c:choose>
                        <c:when test="${authenticateduser == null}">
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="/exchange/login">Login</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link active" aria-current="page" href="/exchange/register">Register</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <form action="/exchange/logout" method="post">
                                    <button type="submit" class="nav-link">
                                        Logout ${authenticateduser.email}
                                    </button>
                                </form>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>