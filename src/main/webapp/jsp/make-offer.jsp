<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="head.jsp">
        <jsp:param name="title" value="Alo Exchange - Make Offer" />
    </jsp:include>
</head>
<body>
    <jsp:include page="nav.jsp">
        <jsp:param name="active" value="home" />
    </jsp:include>

    <div class="container">
        <h2 class="page-title mt-4">Make Offer</h2>

        <c:if test="${not empty message}">
            <div class="alert alert-success alert-alo mb-4" style="white-space: pre-line;">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-alo mb-4" style="white-space: pre-line;">${error}</div>
        </c:if>

        <div class="row g-4 mb-5">
            <div class="col-md-5">
                <div class="offer-card h-100">
                    <h6 class="text-uppercase text-muted small fw-bold mb-3">Your item</h6>
                    <div class="row">
                        <div class="col-4">
                            <img id="offerorImageUrl" alt="" class="rounded" width="100" height="100" style="object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22100%22 height=%22100%22%3E%3Crect fill=%22%23e2e8f0%22 width=%22100%22 height=%22100%22/%3E%3C/svg%3E'">
                        </div>
                        <div class="col-8">
                            <strong id="offerorName" class="d-block mb-1">-</strong>
                            <p id="offerorDescription" class="small text-muted mb-1">-</p>
                            <div class="small"><strong>Qty:</strong> <span id="offerorQuantity">-</span></div>
                            <div class="small"><strong>Condition:</strong> <span id="offerorCondition">-</span></div>
                            <div class="small"><strong>Category:</strong> <span id="offerorCategory">-</span></div>
                        </div>
                    </div>
                    <hr>
                    <form action="${pageContext.request.contextPath}/home" method="post" id="makeOfferForm" onsubmit="document.getElementById('interest').value='${interest}'">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        <input type="hidden" id="offerorlistingid" name="offerorlistingid">
                        <input type="hidden" id="interest" name="interest">
                        <div class="mb-3">
                            <label class="form-label small">Select your item</label>
                            <select class="form-select form-control-alo" name="listingid" id="listingid">
                                <c:forEach var="entry" items="${offeror}">
                                    <option value='${entry.value}'>${entry.key}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <input type="number" min="1" class="form-control form-control-alo" placeholder="Offer quantity" name="offerorquantity" required>
                        </div>
                        <div class="mb-3">
                            <input type="number" min="1" class="form-control form-control-alo" placeholder="Requested quantity" name="offereequantity" required>
                        </div>
                        <button type="submit" class="btn btn-alo w-100">Make Offer</button>
                    </form>
                </div>
            </div>

            <div class="col-md-2 d-flex align-items-center justify-content-center offer-divider">
                <span>&rarr;</span>
            </div>

            <div class="col-md-5">
                <div class="offer-card h-100">
                    <h6 class="text-uppercase text-muted small fw-bold mb-3">Their item</h6>
                    <div class="row">
                        <div class="col-4">
                            <img src="${offeree.imageUrl}" alt="${offeree.name}" class="rounded" width="100" height="100" style="object-fit:cover" onerror="this.src='data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 width=%22100%22 height=%22100%22%3E%3Crect fill=%22%23e2e8f0%22 width=%22100%22 height=%22100%22/%3E%3C/svg%3E'">
                        </div>
                        <div class="col-8">
                            <strong class="d-block mb-1">${offeree.name}</strong>
                            <p class="small text-muted mb-1">${offeree.description}</p>
                            <div class="small"><strong>Qty:</strong> ${offeree.quantity}</div>
                            <div class="small"><strong>Condition:</strong> ${offeree.condition}</div>
                            <div class="small"><strong>Category:</strong> ${offeree.category.name}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" crossorigin="anonymous"></script>
    <script>
        $(document).ready(function() {
            listingChanged();
            $("#listingid").on("change", listingChanged);
        });
        function listingChanged() {
            var val = $("#listingid").val();
            if (!val) return;
            var data = JSON.parse(val);
            $("#offerorImageUrl").attr("src", data.imageUrl);
            $("#offerorlistingid").val(data.id);
            $("#offerorName").text(data.name);
            $("#offerorDescription").text(data.description);
            $("#offerorQuantity").text(data.quantity);
            $("#offerorCondition").text(data.condition);
            $("#offerorCategory").text(data.category.name);
        }
    </script>
</body>
</html>
