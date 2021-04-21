# Demo app for OnOff Telecom 
RESTful cryptocurrency wallet service that allows the user to add, remove
and list his transactions of various cryptocurrencies and tracks their current market value.

Using Bitfinex's public API to calculate the current market value and display it in euros.<br>
https://docs.bitfinex.com/reference

Java 11
Spring Boot 2.4.5
MySQL



### List of API end points:

- **POST** http://localhost:8080/api/wallet/create <br>
Create new wallet for transactions (title, email, fullName)

- **GET** http://localhost:8080/api/wallet/{walletId} <br>
Get wallet by walletId

- **GET** http://localhost:8080/api/wallet/{walletId}/entries <br>
Get all transactions for specific wallet

- **GET** http://localhost:8080/api/wallet/all <br>
Get all wallets

- **PUT** http://localhost:8080/api/wallet/{walletId} <br>
Update wallet by walletId (title, email, fullName)

- **DELETE** http://localhost:8080/api/wallet/{walletId} <br>
Delete wallet by walletId

- **POST** http://localhost:8080/api/entries/add?name={cryptocurrencyName}&amount={amount}&walletId={walletId} <br>
Create new entry (cryptocurrencyName, amount, walletId) <br>
List of supported cryptocurrency names: **btc, iot, eth, neo, eos, got, trx, eut**

- **GET** http://localhost:8080/api/entries/{entryId} <br>
Get entry by entryId

- **DELETE** http://localhost:8080/api/entries/{entryId} <br>
Delete entry by entryId
