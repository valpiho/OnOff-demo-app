# Demo app for OnOff Telecom 
RESTful cryptocurrency wallet service that allows the user to add, remove
and list his transactions of various cryptocurrencies and tracks their current market value.

Using Bitfinex's public API to calculate the current market value and display it in euros.<br>
https://docs.bitfinex.com/reference

Java 11
Spring Boot 2.4.5
MySQL



### List of API end points:

- **POST** http://localhost:8080/api/v1/wallets <br>
Create new wallet for transactions (title, email, fullName)

- **GET** http://localhost:8080/api/v1/wallets/{walletId} <br>
Get wallet by walletId

- **GET** http://localhost:8080/api/v1/wallets/{walletId}/entries <br>
Get all entries for specific wallet

- **GET** http://localhost:8080/api/v1/wallets <br>
Get all wallets

- **PATCH** http://localhost:8080/api/v1/wallets/{walletId} <br>
Update wallet by walletId (title, email, fullName)

- **DELETE** http://localhost:8080/api/v1/wallets/{walletId} <br>
Delete wallet by walletId

=================================================================================

- **POST** http://localhost:8080/api/v1/entries?name={cryptocurrencyName}&amount={amount}&walletId={walletId} <br>
Create new entry (cryptocurrencyName, amount, walletId) <br>
List of supported cryptocurrency names: **btc, iot, eth, neo, eos, got, trx, eut**

- **GET** http://localhost:8080/api/v1/entries <br>
Get all entries

- **GET** http://localhost:8080/api/v1/entries/{entryId} <br>
Get entry by entryId

- **DELETE** http://localhost:8080/api/v1/entries/{entryId} <br>
Delete entry by entryId
