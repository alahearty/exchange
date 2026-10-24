<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="head.jsp">
        <jsp:param name="title" value="${mode == 'edit' ? 'Alo Exchange - Edit Listing' : 'Alo Exchange - New Listing'}" />
    </jsp:include>
</head>
<body>
    <jsp:include page="nav.jsp">
        <jsp:param name="active" value="home" />
    </jsp:include>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card-alo p-4 mt-4">
                    <h2 class="page-title mb-1">${mode == 'edit' ? 'Edit listing' : 'List an item'}</h2>
                    <p class="text-muted mb-4">Describe the item you want to exchange. Other users will offer one of their listings in return.</p>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-alo mb-3" style="white-space: pre-line;">${error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/listing/${mode == 'edit' ? 'edit' : 'create'}" method="post">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        <c:if test="${mode == 'edit'}">
                            <input type="hidden" name="id" value="${listing.id}">
                        </c:if>

                        <div class="row g-3">
                            <div class="col-md-8">
                                <label class="form-label">Item name</label>
                                <input type="text" class="form-control form-control-alo" name="name" maxlength="100" required
                                    value="<c:out value="${listing.name}" />">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Category</label>
                                <select class="form-select form-control-alo" name="categoryId" required>
                                    <option value="">Choose...</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.id}" ${listing.categoryId == category.id ? 'selected' : ''}>
                                            <c:out value="${category.name}" />
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-12">
                                <label class="form-label">Description</label>
                                <textarea class="form-control form-control-alo" name="description" rows="4" maxlength="1000" required><c:out value="${listing.description}" /></textarea>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Condition</label>
                                <select class="form-select form-control-alo" name="condition" required>
                                    <option value="new" ${listing.condition == 'new' ? 'selected' : ''}>New</option>
                                    <option value="used" ${listing.condition == 'used' ? 'selected' : ''}>Used</option>
                                    <option value="old" ${listing.condition == 'old' ? 'selected' : ''}>Old</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Quantity</label>
                                <input type="number" class="form-control form-control-alo" name="quantity" min="1" required
                                    value="${listing.quantity > 0 ? listing.quantity : 1}">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label">Image URL (optional)</label>
                                <input type="url" class="form-control form-control-alo" name="imageUrl"
                                    placeholder="https://..." value="<c:out value="${listing.imageUrl}" />">
                            </div>
                        </div>

                        <div class="d-flex gap-2 mt-4">
                            <button type="submit" class="btn btn-alo">${mode == 'edit' ? 'Save changes' : 'Publish listing'}</button>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
