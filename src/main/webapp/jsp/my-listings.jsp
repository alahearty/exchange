<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="head.jsp">
        <jsp:param name="title" value="Alo Exchange - My Listings" />
    </jsp:include>
</head>
<body>
    <jsp:include page="nav.jsp">
        <jsp:param name="active" value="mine" />
    </jsp:include>

    <div class="container">
        <div class="d-flex justify-content-between align-items-center mt-4 mb-3">
            <div>
                <h2 class="page-title mb-1">My listings</h2>
                <p class="text-muted mb-0">Manage the items you have put forward for exchange.</p>
            </div>
            <a href="${pageContext.request.contextPath}/listing/create" class="btn btn-alo">List an item</a>
        </div>

        <c:choose>
            <c:when test="${empty listings}">
                <div class="card-alo p-5 text-center text-muted">
                    <p class="mb-3">You have not listed any items yet.</p>
                    <a href="${pageContext.request.contextPath}/listing/create" class="btn btn-alo">Create your first listing</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card-alo p-3">
                    <div class="table-responsive">
                        <table class="table align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Item</th>
                                    <th>Category</th>
                                    <th>Qty</th>
                                    <th>Condition</th>
                                    <th>Offers</th>
                                    <th>Status</th>
                                    <th class="text-end">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${listings}">
                                    <tr>
                                        <td>
                                            <a class="fw-bold text-decoration-none text-dark" href="${pageContext.request.contextPath}/listing/view?id=${item.id}">
                                                ${item.name}
                                            </a>
                                        </td>
                                        <td>${item.category.name}</td>
                                        <td>${item.quantity}</td>
                                        <td>${item.condition}</td>
                                        <td>${item.offers}</td>
                                        <td>
                                            <span class="badge ${item.visible ? 'bg-success' : 'bg-secondary'}">${item.visible ? 'Visible' : 'Closed'}</span>
                                        </td>
                                        <td class="text-end">
                                            <a href="${pageContext.request.contextPath}/listing/edit?id=${item.id}" class="btn btn-outline-secondary btn-sm">Edit</a>
                                            <form action="${pageContext.request.contextPath}/listing/delete" method="post" class="d-inline"
                                                onsubmit="return confirm('Remove this listing from the marketplace?');">
                                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                                <input type="hidden" name="id" value="${item.id}">
                                                <button type="submit" class="btn btn-outline-danger btn-sm">Remove</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
