# Alo Exchange — Barter Marketplace

## Overview

Alo Exchange is a server-rendered barter/exchange web application. Users register, sign in, browse
items listed by other users, filter by category, receive simple recommendations, and create trade
offers (interest records) against another user's listing.

The application is a Java Servlet/JSP web application packaged as a WAR and run locally with the
Jetty Maven plugin.

## Final-year project summary

The project demonstrates a complete multi-user marketplace workflow without payments: account
registration and session login, listing CRUD with ownership rules, category browsing, Weka-based
recommendations, offer creation, owner-only offer acceptance/rejection and persistent offer history.
Security controls include BCrypt password hashes, CSRF tokens, an authentication filter and
transactional offer acceptance.

The frontend is server-rendered with JSP, Bootstrap and jQuery; there is no separate frontend
process. The backend and frontend run together in one Jetty process.

## Problem statement

Students and community members often have usable items they no longer need but cannot easily find
someone who wants to trade for them. Alo Exchange provides a small, self-hosted marketplace where
users list items and propose direct exchanges without using money.

## Aim

To provide a working, self-hosted barter exchange that demonstrates a complete multi-user listing and offer lifecycle using Java web technologies and PostgreSQL.

## Objectives

- Provide account registration and session-based login.
- Let users browse and filter visible listings by category.
- Show recommended listings using a simple Weka-based recommendation service.
- Allow a user to propose an exchange against another user's listing.
- Demonstrate a layered Java web application (Servlet -> DAO -> PostgreSQL).

## Technology stack

| Layer | Technology |
|---|---|
| Language | Java 21 (tested with JDK 25) |
| Web | Jakarta `javax.servlet` 4.0, JSP, JSTL 1.2 |
| Build | Maven (WAR packaging) |
| Server | Jetty 9.4 Maven plugin |
| Database | PostgreSQL |
| Libraries | PostgreSQL JDBC 42.7.3, Jackson, Weka 3.8.6 |
| Frontend | JSP + Bootstrap 5 (CDN) + jQuery |

## Architecture

```
Browser
  -> Jetty (context path /exchange)
      -> Servlet controllers (Home, Login, Register, Logout, MyOffers, Interest)
          -> DAO layer (UserDao, ListingDao, RecommendationDao)
              -> PostgreSQL (user, category, listing, interest)
      -> JSP views (home, login, register, make-offer, my-offers)
```

Authentication uses an HTTP session (`session.setAttribute("user", ...)`) and an `AuthFilter`
that protects `/user`, `/listing`, `/my-offers` and `/offer` URLs. Passwords are stored as salted
BCrypt hashes (cost 12). Session synchronizer tokens protect every state-changing POST request,
and the session id/token are rotated after login.

## Software prerequisites

- JDK 21 or newer (`java -version`, `javac -version`)
- Maven 3.9 or newer (`mvn -v`)
- PostgreSQL 14 or newer, running on `localhost:5432`
- A terminal (PowerShell on Windows is fine)

## Database setup

The default connection values are:

| Setting | Default |
|---|---|
| URL | `jdbc:postgresql://localhost:5432/exchange` |
| User | `worinium` |
| Password | `worisoft@123` |

Create the role and database (skip steps that already exist):

```sql
CREATE USER worinium WITH PASSWORD 'worisoft@123';
CREATE DATABASE exchange OWNER worinium;
```

Then load the schema and demo data:

```powershell
psql -U worinium -d exchange -f script.sql
```

`script.sql` creates the `user`, `category`, `listing` and `interest` tables and inserts demo
users, categories and listings. Do not run it twice against the same database unless you drop the
tables first.

The automated tests use an isolated database. Create it once:

```sql
CREATE DATABASE exchange_test OWNER worinium;
```

The test suite creates its own tables inside `exchange_test` and never touches the `exchange`
database.

## Environment configuration

`BaseDao` reads these environment variables and falls back to the defaults above:

```
EXCHANGE_DB_URL=jdbc:postgresql://localhost:5432/exchange
EXCHANGE_DB_USER=worinium
EXCHANGE_DB_PASSWORD=worisoft@123
```

See `.env.example`. Environment variables must be set in the shell that starts Maven.

## Build

```powershell
mvn clean package
```

The WAR is written to `target/exchange.war`.

## Run

### One-click Windows launcher

Double-click **`run.cmd`**, or right-click **`run.ps1` → Run with PowerShell**.

The launcher checks Java and Maven, starts Jetty in a separate PowerShell window and opens
<http://localhost:9999/exchange/>. PostgreSQL and the `exchange` database must already be running
and loaded from `script.sql`.

### Manual run

```powershell
mvn jetty:run
```

Open: <http://localhost:9999/exchange/>

The Jetty plugin is configured in `pom.xml` with port `9999` and context path `/exchange`.
Because this is a JSP application, the backend and frontend are served by the same process.

## Demo account

| Email | Password |
|---|---|
| `john@doe.com` | `john.doe` |

The full academic presentation script is in `DEMO_GUIDE.md`.

A fresh `script.sql` load also creates `bill@gates.com` / `bill.gates` and
`steve@jobs.com` / `steve.jobs`. If your database was created earlier, only the accounts actually
present in the `user` table will work.

## Main features

- Login, logout and registration with server-side validation messages.
- Home page listing all visible items, excluding the signed-in user's own items.
- Category filter and "Recommended for you" block powered by `RecommendationService` (Weka).
- Authenticated listing creation, editing, detail view and removal.
- "My listings" page showing visible and closed listings owned by the signed-in user.
- Make-offer page that records an `interest` row with offered/requested quantities.
- "My Offers" page with two sections: offers you made and offers received on your listings.
- Listing owners can accept or reject pending offers. Acceptance is transactional, rejects other
  pending offers for the same item, and closes both listings.
- Duplicate pending offers from the same user for the same listing are rejected.
- Offer statuses (`pending`, `accepted`, `rejected`) persist and are shown in the UI.

## Demonstration workflow

Use two accounts:

1. Sign in as account A, open **Sell an item**, and publish a listing.
2. Sign in as account B, create a second listing, open account A's listing, and choose
   **Make an offer**.
3. Sign in as account A, open **Offers**, and accept or reject the received offer.
4. On acceptance, both listings become closed and the offer history shows `accepted`.
5. Duplicate acceptance is rejected and an unrelated user cannot accept the offer.

## Security notes

- New passwords are hashed with BCrypt (cost 12); the database never stores plaintext.
- On startup, a one-time migration hashes any legacy plaintext rows. It logs only the count.
- Every POST form includes a session CSRF token (`csrfToken`). Missing or incorrect tokens return
  HTTP 403. The token is rotated after login together with the session id.
- Login redirects are restricted to local `/exchange/` paths to prevent open redirects.
- Passwords are never logged or returned in a model.

## Testing

The project now has seven JUnit tests: four DAO workflow tests, one legacy-password migration test
and two password/CSRF security tests. They cover registration/login validation, listing CRUD and
ownership, offer creation, owner-only acceptance, duplicate protection, rejection, BCrypt
verification, one-time plaintext migration and CSRF token comparison.

Prerequisite: the `exchange_test` database described above must exist and PostgreSQL must be
running.

```powershell
mvn test
```

Expected result: `Tests run: 7, Failures: 0, Errors: 0`.

Recommended manual smoke test:

1. Open <http://localhost:9999/exchange/> and sign in with the demo account.
2. Confirm listing cards appear.
3. Use a category filter.
4. Create a listing from **Sell an item** and edit it from **My listings**.
5. Use a second account to submit an offer, then review and accept/reject it.
6. Stop Jetty with `Ctrl+C`.

## Troubleshooting

| Symptom | Fix |
|---|---|
| `Connection refused` / `FATAL: password authentication failed` | Check PostgreSQL is running and the `EXCHANGE_DB_*` values match your database. |
| `relation "user" does not exist` | Run `script.sql` against the `exchange` database. |
| Port 9999 already in use | Stop the other process or change `<port>` in `pom.xml`. |
| 404 at `/` | Use the context path: `http://localhost:9999/exchange/`. |
| JSP compilation errors after changing Java version | Use JDK 21+ and run `mvn clean package` again. |

## Known limitations

- Removing a listing is a soft delete (`visible = false`); the row is retained for offer history.
- Accepting one offer closes the item for further offers and rejects other pending offers for the
  same listing. Quantities are not decremented or split.
- Passwords are BCrypt-hashed and CSRF tokens are required. The legacy plaintext migration runs at
  startup and upgrades any imported plaintext row; the login fallback removes the last plaintext
  value on that user's next successful sign-in.
- `OfferServlet` remains a legacy placeholder; the supported offer workflow uses `HomeServlet`
  and `OfferResponseServlet`.
- Product images are external URLs and need an internet connection.
- The JUnit suite covers the DAO workflow, not the browser layer. There is no CI pipeline.
