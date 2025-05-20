<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <html>

    <head>
        <title>Alo Exchange - Home</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
    </head>

    <body>
        <jsp:include page="nav.jsp" />
        <div class="container">
            <h2>Welcome to Alo Exchange</h2>
            <h5>We exchange all kinds of items.</h5>

            <div class="row mt-5">
                <div class="col-3">
                    <form action="/exchange/home" method="get" id="categoryForm">
                        <input type="hidden" id="category" name="category">
                        <div class="d-flex flex-column align-items-stretch flex-shrink-0 bg-white">
                            <div class="list-group list-group-flush border-bottom scrollarea">
                                <a href="#"
                                    class="list-group-item list-group-item-action ${active == 'all' ? 'active' : ''} py-3 lh-tight"
                                    aria-current="true"
                                    onclick="document.getElementById('category').value = 'all';document.getElementById('categoryForm').submit();">
                                    All
                                </a>
                                <c:forEach var="category" items="${listing.categories}">
                                    <a href="#"
                                        class="list-group-item list-group-item-action ${category.name.toLowerCase() == active ? 'active' : ''} py-3 lh-tight"
                                        aria-current="true"
                                        onclick="document.getElementById('category').value = '${category.name}';document.getElementById('categoryForm').submit();">
                                        ${category.name}
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-9">
                    <form action="/exchange/home" method="get" id="listingForm">
                        <input type="hidden" id="interest" name="interest">
                        <div class="d-flex flex-column align-items-stretch flex-shrink-0 bg-white">
                            <div class="list-group list-group-flush border-bottom scrollarea">
                                <c:forEach var="item" items="${listing.listings}">
                                    <a href="#" class="list-group-item list-group-item-action py-3 lh-tight"
                                        aria-current="true">
                                        <div class="row">
                                            <div class="col-2">
                                                <img src="${item.imageUrl}" alt="" class="" width="96px"
                                                    height="96px" />
                                            </div>
                                            <div class="col-10">
                                                <div class="d-flex w-100 align-items-center justify-content-between">
                                                    <strong class="mb-1">${item.name}</strong>
                                                    <button ${item.offerStatus} class="btn btn-primary btn-sm"
                                                        onclick="document.getElementById('interest').value = '${item.id}';document.getElementById('listingForm').submit();">Make
                                                        Offer</button>
                                                </div>

                                                <div class="d-flex w-100 align-items-center justify-content-between">
                                                    <p class="mb-1">${item.description}</p>
                                                    <small>Offers: ${item.offers}</small>
                                                </div>
                                                <div class="col-10 mb-1 small"><strong>quantity:</strong>
                                                    ${item.quantity}
                                                </div>
                                                <div class="col-10 mb-1 small"><strong>condition:</strong>
                                                    ${item.condition}
                                                </div>
                                                <div class="col-10 mb-1 small"><strong>category:</strong>
                                                    ${item.category.name}</div>
                                            </div>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="js/bootstrap.min.js"></script>
    </body>

    </html>