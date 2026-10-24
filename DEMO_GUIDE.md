# Alo Exchange - Academic Demonstration Guide

## 1. What problem does the software solve?

Alo Exchange lets people trade items they already own instead of buying new ones. Users publish
listings and propose an exchange against another user's listing. The receiving owner can accept or
reject the proposal.

## 2. Why does the problem matter?

Bartering reduces waste and cost for students and communities. It also demonstrates a complete
multi-user workflow: ownership, offers, state transitions and persistent history.

## 3. Main objectives

- Provide account registration and session login.
- Let users publish, edit and remove their own listings.
- Let users browse other listings and submit exchange offers.
- Let listing owners review offers and accept or reject them.
- Persist offer status and close the exchange atomically.
- Prevent unauthorised edits and duplicate/invalid offer transitions.

## 4. Technologies used

| Concern | Technology |
|---|---|
| Language | Java 21 |
| Web | Servlet 4, JSP, JSTL |
| Build | Maven WAR |
| Runtime server | Jetty 9.4 |
| Database | PostgreSQL 18 |
| Data access | JDBC with prepared statements |
| Front end | JSP, Bootstrap 5, jQuery |
| Recommendation | Weka 3.8 |
| Tests | JUnit 4 against `exchange_test` |

## 5. Architecture

```
Browser
  -> AuthFilter (session check)
  -> Servlets: Home, Login, Register, Listing, MyOffers, OfferResponse
      -> DAO: UserDao, ListingDao, RecommendationDao
          -> PostgreSQL
      -> JSP views
```

- `AuthFilter` protects `/listing`, `/my-offers` and `/offer` URLs.
- `ListingDao` owns listing CRUD and the transactional offer workflow.
- `ListingServlet` handles create/view/edit/delete.
- `OfferResponseServlet` handles accept/reject and stores flash messages.

## 6. Database design

| Table | Purpose | Key columns |
|---|---|---|
| `user` | Accounts | id, email, firstname, lastname, password (BCrypt hash) |
| `category` | Listing categories | id, name |
| `listing` | Items offered for exchange | id, name, description, condition, quantity, imageurl, visible, userid, categoryid |
| `interest` | Offers between two listings | id, offerorquantity, offereequantity, offereestatus, offerorlistingid, offereelistingid |

Relationships: a listing belongs to a user and a category; an interest references an offeror
listing and an offeree listing. Accepted exchanges set both listings to `visible = false`.

## 7. Main modules

- Authentication and registration.
- Listing management (`Sell an item`, `My listings`, detail/edit/remove).
- Browsing, category filtering and recommendations.
- Offer creation.
- Offer inbox and outbox (`My Offers`).
- Owner accept/reject with transaction and duplicate protection.

## 8. Demonstration sequence

Use two synthetic accounts. On a fresh `script.sql` load, `john@doe.com` / `john.doe` and
`bill@gates.com` / `bill.gates` are available.

1. Sign in as John and open **Sell an item**.
2. Publish "Demo Tent" in Electronics, quantity 1.
3. Open **My listings** and edit the listing name to "Demo Tent Updated".
4. Sign out and sign in as Bill.
5. Publish "Demo Lamp" as Bill's offered item.
6. Open John's listing and choose **Make an offer**; submit 1 for 1.
7. Sign back in as John and open **Offers**.
8. Point out the received offer in the "Offers on your listings" section.
9. Click **Accept**. Show the success message and `accepted` status.
10. Show that both listings are now closed and no further duplicate acceptance is possible.

## 9. Data flow

1. The browser posts a form to a servlet.
2. `AuthFilter` attaches the signed-in `UserModel` to the request.
3. The servlet validates fields and calls a DAO method.
4. `ListingDao` uses prepared statements against PostgreSQL.
5. For acceptance, a transaction locks the offer and listing, checks ownership and pending status,
   updates the offer, rejects competing offers and closes both listings.
6. The servlet redirects with a flash message and a JSP renders the updated state.

## 10. Limitations and future improvements

- Passwords are BCrypt-hashed; a one-time startup migration upgrades any imported plaintext rows.
- Listings are soft-deleted and remain available for offer history.
- Image URLs are external; there is no image upload.
- State-changing POSTs require a session CSRF token; there is no pagination or admin moderation.
- Future work: image upload, notifications, chat, pagination and an admin role.
