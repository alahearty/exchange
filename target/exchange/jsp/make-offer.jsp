<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <head>
        <title>Alo Exchange - Make Offer</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
    </head>

    <body>
        <jsp:include page="nav.jsp" />
        <div class="container">
            <p class="fs-4 mb-5">Make Offer</p>

            <c:if test="${not empty message}">
                <div class="alert alert-success" style="white-space: pre-line;">
                    ${message}
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger" style="white-space: pre-line;">
                    ${error}
                </div>
            </c:if>

            <div class="row">
                <div class="col-6 border p-1">
                    <div class="row">
                        <div class="col-3">
                            <img id="offerorImageUrl" alt="" class="" width="120px" height="120px" />
                        </div>
                        <div class="col-4 p-0 m-0">
                            <div class="d-flex w-100 align-items-center justify-content-between">
                                <strong id="offerorName" class="mb-1">name:</strong>
                            </div>
                            <div class="d-flex w-100 align-items-center justify-content-between">
                                <p id="offerorDescription" class="mb-1">${offeree.description}</p>
                            </div>
                            <div class="col-10 mb-1 small"><strong>Quantity:</strong>
                                <span id="offerorQuantity"></span>
                            </div>
                            <div class="col-10 mb-1 small"><strong>Condition:</strong>
                                <span id="offerorCondition"></span>
                            </div>
                            <div class="col-10 mb-1 small"><strong>Category:</strong>
                                <span id="offerorCategory"></span>
                            </div>
                        </div>
                        <div class="col-5">
                            <form action="/exchange/home" method="post" id="makeOfferForm">
                                <input type="hidden" id="offerorlistingid" name="offerorlistingid">
                                <input type="hidden" id="interest" name="interest">
                                <div class="mb-3">
                                    <select class="form-control rounded-0" name="listingid" id="listingid">
                                        <c:forEach var="entry" items="${offeror}">
                                            <option value='${entry.value}'>${entry.key}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <input type="number" min="1" class="form-control rounded-0"
                                        placeholder="offer quantity" name="offerorquantity">
                                </div>

                                <div class="mb-3">
                                    <input type="number" min="1" class="form-control rounded-0"
                                        placeholder="requested quantity" name="offereequantity">
                                </div>

                                <div class="d-flex justify-content-end">
                                    <input type="submit" class="btn btn-primary rounded-0"
                                        onclick="document.getElementById('interest').value = '${interest}';document.getElementById('makeOfferForm').submit();"
                                        value="Make Offer" />
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-1 my-auto text-center">
                    <strong>For -></strong>
                </div>
                <div class="col-5 border p-1">
                    <div class="row">
                        <div class="col-3">
                            <img src="${offeree.imageUrl}" alt="" class="" width="120px" height="120px" />
                        </div>
                        <div class="col-9">
                            <div class="d-flex w-100 align-items-center justify-content-between">
                                <strong class="mb-1">${offeree.name}</strong>
                            </div>
                            <div class="d-flex w-100 align-items-center justify-content-between">
                                <p class="mb-1">${offeree.description}</p>
                            </div>
                            <div class="col-10 mb-1 small"><strong>Quantity:</strong>
                                ${offeree.quantity}
                            </div>
                            <div class="col-10 mb-1 small"><strong>Condition:</strong>
                                ${offeree.condition}
                            </div>
                            <div class="col-10 mb-1 small"><strong>Offers:</strong>
                                ${offeree.offers}
                            </div>
                            <div class="col-10 mb-1 small"><strong>Category:</strong>
                                ${offeree.category.name}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.min.js"></script>
    </body>

    </html>

    <script>
        $(document).ready(function () {
            listingChanged();

            $("#listingid").on("change", function () {
                listingChanged();
            });
        });
        function listingChanged() {
            // const index = $("#listingid").prop("selectedIndex");
            const val = $("#listingid").val();
            const data = JSON.parse(val);

            $("#offerorImageUrl").attr("src", data.imageUrl);
            $("#offerorlistingid").val(data.id)
            $("#offerorName").text(data.name)
            $("#offerorDescription").text(data.description)
            $("#offerorQuantity").text(data.quantity)
            $("#offerorCondition").text(data.condition)
            $("#offerorCategory").text(data.category.name)
        }
    </script>