<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <head>
        <title>Alo Exchange - Make Offer</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
    </head>

    <body>
        <jsp:include page="nav.jsp" />
        <div class="container">
            <p class="fs-4 mb-5">My Offers</p>

            <c:forEach var="interest" items="${interests}">
                <div class="row">
                    <div class="col-5 border p-1">
                        <div class="row">
                            <div class="col-3">
                                <img src="${interest.offeror.imageUrl}" alt="" class="" width="120px" height="120px" />
                            </div>
                            <div class="col-9">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">${interest.offeror.name}</strong>
                                </div>
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <p class="mb-1">${interest.offeror.description}</p>
                                </div>
                                <div class="col-10 mb-1 small"><strong>Quantity:</strong>
                                    ${interest.offeror.quantity}
                                </div>
                                <div class="col-10 mb-1 small"><strong>Condition:</strong>
                                    ${interest.offeror.condition}
                                </div>
                                <div class="col-10 mb-1 small"><strong>Category:</strong>
                                    ${interest.offeror.category.name}</div>
                                <hr>
                                <div class="col-10 mb-1 small"><strong>Offered Quantity:</strong>
                                    ${interest.offerorQuantity}
                                </div>
                                <div class="col-10 mb-1 small"><strong>Requested Quantity:</strong>
                                    ${interest.offereeQuantity}
                                </div>
                                <div class="col-10 mb-1 small"><strong>Offer Status:</strong>
                                    ${interest.offereeStatus}
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-1 my-auto text-center">
                        <strong>For -></strong>
                    </div>
                    <div class="col-5 border p-1">
                        <div class="row">
                            <div class="col-3">
                                <img src="${interest.offeree.imageUrl}" alt="" class="" width="120px" height="120px" />
                            </div>
                            <div class="col-9">
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <strong class="mb-1">${interest.offeree.name}</strong>
                                </div>
                                <div class="d-flex w-100 align-items-center justify-content-between">
                                    <p class="mb-1">${interest.offeree.description}</p>
                                </div>
                                <div class="col-10 mb-1 small"><strong>Quantity:</strong>
                                    ${interest.offeree.quantity}
                                </div>
                                <div class="col-10 mb-1 small"><strong>Condition:</strong>
                                    ${interest.offeree.condition}
                                </div>
                                <div class="col-10 mb-1 small"><strong>Category:</strong>
                                    ${interest.offeree.category.name}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.min.js"></script>
    </body>

    </html>