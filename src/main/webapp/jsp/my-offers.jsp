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
        <p class="text-muted">Track offers you made and respond to offers on your own listings.</p>

        <c:if test="${not empty success}">
            <div class="alert alert-success alert-alo">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-alo" style="white-space: pre-line;">${error}</div>
        </c:if>

        <h5 class="fw-bold mt-4 mb-3">Offers you made</h5>
        <c:choose>
            <c:when test="${empty interests}">
                <div class="card-alo p-4 text-center text-muted mb-4">
                    <p class="mb-1">You haven't made any offers yet.</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-alo mt-2">Browse listings</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="d-flex flex-column gap-3 mb-5">
                    <c:forEach var="interest" items="${interests}">
                        <div class="card-alo p-3">
                            <div class="row g-3 align-items-center">
                                <div class="col-md-5">
                                    <div class="small text-uppercase text-muted fw-bold mb-1">You offer</div>
                                    <div class="d-flex gap-3">
                                        <img src="${interest.offeror.imageUrl}" alt="" class="rounded" width="70" height="70" style="object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2270%22 height=%2270%22%3E%3Crect fill=%22%23e2e8f0%22 width=%2270%22 height=%2270%22/%3E%3C/svg%3E'">
                                        <div>
                                            <a class="fw-bold text-decoration-none text-dark" href="${pageContext.request.contextPath}/listing/view?id=${interest.offeror.id}">${interest.offeror.name}</a>
                                            <div class="small text-muted">Qty: ${interest.offerorQuantity} &bull; ${interest.offeror.condition}</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-1 text-center fs-4">&rarr;</div>
                                <div class="col-md-4">
                                    <div class="small text-uppercase text-muted fw-bold mb-1">You request</div>
                                    <div class="d-flex gap-3">
                                        <img src="${interest.offeree.imageUrl}" alt="" class="rounded" width="70" height="70" style="object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2270%22 height=%2270%22%3E%3Crect fill=%22%23e2e8f0%22 width=%2270%22 height=%2270%22/%3E%3C/svg%3E'">
                                        <div>
                                            <a class="fw-bold text-decoration-none text-dark" href="${pageContext.request.contextPath}/listing/view?id=${interest.offeree.id}">${interest.offeree.name}</a>
                                            <div class="small text-muted">Qty: ${interest.offereeQuantity} &bull; ${interest.offeree.condition}</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-2 text-md-end">
                                    <span class="badge ${interest.offereeStatus == 'accepted' ? 'bg-success' : (interest.offereeStatus == 'rejected' ? 'bg-danger' : 'bg-secondary')}">${interest.offereeStatus}</span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <h5 class="fw-bold mt-4 mb-3">Offers on your listings</h5>
        <c:choose>
            <c:when test="${empty receivedInterests}">
                <div class="card-alo p-4 text-center text-muted">
                    <p class="mb-1">No one has offered on your listings yet.</p>
                    <a href="${pageContext.request.contextPath}/listing/create" class="btn btn-alo mt-2">List an item</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="d-flex flex-column gap-3 mb-5">
                    <c:forEach var="interest" items="${receivedInterests}">
                        <div class="card-alo p-3">
                            <div class="row g-3 align-items-center">
                                <div class="col-md-5">
                                    <div class="small text-uppercase text-muted fw-bold mb-1">They offer</div>
                                    <div class="d-flex gap-3">
                                        <img src="${interest.offeror.imageUrl}" alt="" class="rounded" width="70" height="70" style="object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2270%22 height=%2270%22%3E%3Crect fill=%22%23e2e8f0%22 width=%2270%22 height=%2270%22/%3E%3C/svg%3E'">
                                        <div>
                                            <span class="fw-bold">${interest.offeror.name}</span>
                                            <div class="small text-muted">Qty: ${interest.offerorQuantity} &bull; ${interest.offeror.condition}</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-1 text-center fs-4">&rarr;</div>
                                <div class="col-md-4">
                                    <div class="small text-uppercase text-muted fw-bold mb-1">Your listing</div>
                                    <div class="d-flex gap-3">
                                        <img src="${interest.offeree.imageUrl}" alt="" class="rounded" width="70" height="70" style="object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%2270%22 height=%2270%22%3E%3Crect fill=%22%23e2e8f0%22 width=%2270%22 height=%2270%22/%3E%3C/svg%3E'">
                                        <div>
                                            <a class="fw-bold text-decoration-none text-dark" href="${pageContext.request.contextPath}/listing/view?id=${interest.offeree.id}">${interest.offeree.name}</a>
                                            <div class="small text-muted">Qty requested: ${interest.offereeQuantity}</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-2 text-md-end">
                                    <span class="badge ${interest.offereeStatus == 'accepted' ? 'bg-success' : (interest.offereeStatus == 'rejected' ? 'bg-danger' : 'bg-secondary')} mb-2 d-inline-block">${interest.offereeStatus}</span>
                                    <c:if test="${interest.offereeStatus == 'pending'}">
                                        <form action="${pageContext.request.contextPath}/offer/respond" method="post" class="d-inline">
                                            <input type="hidden" name="csrfToken" value="${csrfToken}">
                                            <input type="hidden" name="interestId" value="${interest.interestId}">
                                            <input type="hidden" name="action" value="accept">
                                            <button type="submit" class="btn btn-success btn-sm">Accept</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/offer/respond" method="post" class="d-inline">
                                            <input type="hidden" name="csrfToken" value="${csrfToken}">
                                            <input type="hidden" name="interestId" value="${interest.interestId}">
                                            <input type="hidden" name="action" value="reject">
                                            <button type="submit" class="btn btn-outline-danger btn-sm">Reject</button>
                                        </form>
                                    </c:if>
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
