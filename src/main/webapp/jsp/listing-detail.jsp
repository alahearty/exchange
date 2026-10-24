<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="head.jsp">
        <jsp:param name="title" value="${listing.name} - Alo Exchange" />
    </jsp:include>
</head>
<body>
    <jsp:include page="nav.jsp">
        <jsp:param name="active" value="home" />
    </jsp:include>

    <div class="container">
        <c:if test="${param.message == 'created'}">
            <div class="alert alert-success alert-alo mt-4">Your listing was published.</div>
        </c:if>
        <c:if test="${param.message == 'updated'}">
            <div class="alert alert-success alert-alo mt-4">Your listing was updated.</div>
        </c:if>
        <c:if test="${param.error == 'delete-failed'}">
            <div class="alert alert-danger alert-alo mt-4">The listing could not be removed.</div>
        </c:if>

        <div class="row g-4 mt-1">
            <div class="col-lg-6">
                <div class="card-alo overflow-hidden">
                    <c:choose>
                        <c:when test="${not empty listing.imageUrl}">
                            <img src="<c:out value="${listing.imageUrl}" />" alt="<c:out value="${listing.name}" />"
                                class="w-100" style="max-height:420px;object-fit:cover"
                                onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22600%22 height=%22400%22%3E%3Crect fill=%22%23e2e8f0%22 width=%22600%22 height=%22400%22/%3E%3Ctext x=%22300%22 y=%22200%22 fill=%22%2394a3b8%22 text-anchor=%22middle%22 dy=%22.3em%22 font-size=%2224%22%3ENo image%3C/text%3E%3C/svg%3E'">
                        </c:when>
                        <c:otherwise>
                            <div class="d-flex align-items-center justify-content-center bg-light text-muted" style="height:420px;">No image supplied</div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="card-alo p-4 h-100">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <h1 class="page-title mb-1"><c:out value="${listing.name}" /></h1>
                            <p class="text-muted mb-2"><c:out value="${listing.category.name}" /></p>
                        </div>
                        <c:if test="${isOwner}">
                            <span class="badge bg-secondary">${listing.offers} pending offer(s)</span>
                        </c:if>
                    </div>

                    <p class="mb-3"><c:out value="${listing.description}" /></p>
                    <div class="row g-2 mb-4">
                        <div class="col-6">
                            <div class="border rounded p-3">
                                <div class="text-muted small">Condition</div>
                                <strong><c:out value="${listing.condition}" /></strong>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="border rounded p-3">
                                <div class="text-muted small">Available quantity</div>
                                <strong>${listing.quantity}</strong>
                            </div>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${isOwner}">
                            <div class="d-flex flex-wrap gap-2">
                                <a href="${pageContext.request.contextPath}/listing/edit?id=${listing.id}" class="btn btn-alo">Edit listing</a>
                                <a href="${pageContext.request.contextPath}/my-offers" class="btn btn-outline-secondary">Review offers</a>
                                <form action="${pageContext.request.contextPath}/listing/delete" method="post" class="d-inline"
                                    onsubmit="return confirm('Remove this listing from the marketplace?');">
                                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                                    <input type="hidden" name="id" value="${listing.id}">
                                    <button type="submit" class="btn btn-outline-danger">Remove listing</button>
                                </form>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/home?interest=${listing.id}" class="btn btn-alo btn-lg">Make an offer</a>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary btn-lg ms-2">Back to listings</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
