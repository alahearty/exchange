<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="head.jsp">
        <jsp:param name="title" value="Alo Exchange - My Offers" />
    </jsp:include>
</head>
<body>
    <jsp:include page="nav.jsp">
        <jsp:param name="active" value="${navActive != null ? navActive : 'offers'}" />
    </jsp:include>

    <div class="container">
        <h2 class="page-title mt-4">My Offers</h2>

        <c:choose>
            <c:when test="${empty interests}">
                <div class="card-alo p-5 text-center text-muted">
                    <p class="mb-0">You haven't made any offers yet.</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-alo mt-3">Browse listings</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="d-flex flex-column gap-4 mb-5">
                    <c:forEach var="interest" items="${interests}">
                        <div class="row g-3">
                            <div class="col-md-5">
                                <div class="offer-card p-3">
                                    <h6 class="text-uppercase text-muted small fw-bold mb-2">Your offer</h6>
                                    <div class="d-flex gap-3">
                                        <img src="${interest.offeror.imageUrl}" alt="" class="rounded" width="80" height="80" style="object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2280%22 height=%2280%22%3E%3Crect fill=%22%23e2e8f0%22 width=%2280%22 height=%2280%22/%3E%3C/svg%3E'">
                                        <div class="flex-grow-1">
                                            <strong>${interest.offeror.name}</strong>
                                            <p class="small text-muted mb-1">${interest.offeror.description}</p>
                                            <div class="small">Qty: ${interest.offeror.quantity} &bull; ${interest.offeror.condition}</div>
                                            <div class="small mt-1"><strong>Offering:</strong> ${interest.offerorQuantity} &bull; <strong>Requesting:</strong> ${interest.offereeQuantity}</div>
                                            <span class="badge bg-secondary mt-1">${interest.offereeStatus}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2 d-flex align-items-center justify-content-center offer-divider">
                                <span>&rarr;</span>
                            </div>
                            <div class="col-md-5">
                                <div class="offer-card p-3">
                                    <h6 class="text-uppercase text-muted small fw-bold mb-2">For</h6>
                                    <div class="d-flex gap-3">
                                        <img src="${interest.offeree.imageUrl}" alt="" class="rounded" width="80" height="80" style="object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2280%22 height=%2280%22%3E%3Crect fill=%22%23e2e8f0%22 width=%2280%22 height=%2280%22/%3E%3C/svg%3E'">
                                        <div class="flex-grow-1">
                                            <strong>${interest.offeree.name}</strong>
                                            <p class="small text-muted mb-1">${interest.offeree.description}</p>
                                            <div class="small">Qty: ${interest.offeree.quantity} &bull; ${interest.offeree.condition}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
