<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="head.jsp">
        <jsp:param name="title" value="Alo Exchange - Home" />
    </jsp:include>
</head>
<body>
    <jsp:include page="nav.jsp">
        <jsp:param name="active" value="home" />
    </jsp:include>

    <div class="container">
        <div class="hero-section">
            <h1>Welcome to Alo Exchange</h1>
            <p class="lead">We exchange all kinds of items. Browse, offer, and trade.</p>
        </div>

        <c:if test="${not empty recommendations}">
            <div class="mb-4">
                <h5 class="mb-3 fw-bold">Recommended for you</h5>
                <form action="${pageContext.request.contextPath}/home" method="get" id="recForm">
                    <input type="hidden" id="recInterest" name="interest">
                    <div class="row g-3">
                        <c:forEach var="rec" items="${recommendations}">
                            <div class="col-md-4 col-lg-3">
                                <div class="card-alo h-100 listing-card">
                                    <img src="${rec.imageUrl}" alt="${rec.name}" class="card-img-top" style="height:120px;object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22100%22 height=%22100%22%3E%3Crect fill=%22%23e2e8f0%22 width=%22100%22 height=%22100%22/%3E%3Ctext x=%2250%22 y=%2250%22 fill=%22%2394a3b8%22 text-anchor=%22middle%22 dy=%22.3em%22 font-size=%2214%22%3ENo image%3C/text%3E%3C/svg%3E'">
                                    <div class="card-body p-2">
                                        <h6 class="mb-1 small fw-bold text-truncate" title="${rec.name}">${rec.name}</h6>
                                        <p class="mb-1 text-muted small text-truncate" style="font-size:0.75rem">${rec.category.name}</p>
                                        <button type="button" class="btn btn-alo btn-sm w-100"
                                            onclick="document.getElementById('recInterest').value='${rec.id}';document.getElementById('recForm').submit();">
                                            Make Offer
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </form>
            </div>
        </c:if>

        <div class="row">
            <div class="col-lg-3 mb-4">
                <div class="card-alo p-3 category-sidebar">
                    <h6 class="text-uppercase text-muted mb-3 fw-bold">Categories</h6>
                    <form action="${pageContext.request.contextPath}/home" method="get" id="categoryForm">
                        <input type="hidden" id="category" name="category">
                        <a href="#" class="category-item ${active == 'all' ? 'active' : ''}"
                            onclick="document.getElementById('category').value='all';document.getElementById('categoryForm').submit();">All</a>
                        <c:forEach var="category" items="${listing.categories}">
                            <a href="#" class="category-item ${category.name.toLowerCase() == active ? 'active' : ''}"
                                onclick="document.getElementById('category').value='${category.name}';document.getElementById('categoryForm').submit();">${category.name}</a>
                        </c:forEach>
                    </form>
                </div>
            </div>

            <div class="col-lg-9">
                <form action="${pageContext.request.contextPath}/home" method="get" id="listingForm">
                    <input type="hidden" id="interest" name="interest">
                    <c:choose>
                        <c:when test="${empty listing.listings}">
                            <div class="card-alo p-5 text-center text-muted">
                                <p class="mb-0">No listings in this category yet.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                    <div class="d-flex flex-column gap-3">
                        <c:forEach var="item" items="${listing.listings}">
                            <div class="card-alo listing-card">
                                <img src="${item.imageUrl}" alt="${item.name}" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22100%22 height=%22100%22%3E%3Crect fill=%22%23e2e8f0%22 width=%22100%22 height=%22100%22/%3E%3Ctext x=%2250%22 y=%2250%22 fill=%22%2394a3b8%22 text-anchor=%22middle%22 dy=%22.3em%22 font-size=%2214%22%3ENo image%3C/text%3E%3C/svg%3E'">
                                <div class="flex-grow-1">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div>
                                            <h6 class="mb-1 fw-bold">${item.name}</h6>
                                            <p class="mb-1 text-muted small">${item.description}</p>
                                            <div class="small text-muted">
                                                Qty: ${item.quantity} &bull; ${item.condition} &bull; ${item.category.name}
                                            </div>
                                        </div>
                                        <div class="text-end">
                                            <span class="badge bg-light text-dark mb-2">${item.offers} offers</span>
                                            <button type="button" class="btn btn-alo btn-sm"
                                                onclick="document.getElementById('interest').value='${item.id}';document.getElementById('listingForm').submit();">
                                                Make Offer
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
