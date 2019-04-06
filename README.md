# sebakj-util

`sebakj-util` will introduce the basic usage of SEBAK for JAVA.

## Create new transaction

### Prerequisite
Before creating new transaction, you should check these,

* 'secret seed' of source account
* 'public address' of target account
* 'network id'

You can simply check 'network id' from SEBAK node information. If the address of your sebak node is 'https://testnet-sebak.blockchainos.org',
```sh
$ curl -v https://testnet-sebak.blockchainos.org
...
  "policy": {
    "network-id": "sebak-test-network",
    "initial-balance": "10000000000000000000",
    "base-reserve": "1000000",
...
```

The value of `"network-id"`, `sebak-test-network` is the 'network id'.

### Operation

For the nature of transaction of SEBAK, one `Transction` can have multple `Operation`s. `Operation` is the base unit of operating accounts like creating new account and payment. Currently SEBAK supports various kind of operations, but most of users will use `CreateAccount` and `Payment` operations.

```
String type1 = "payment";
String type2 = "create-account";
String amount1 = "100";
String amount2 = "1000000";
String targetAddress1 = "GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE";
String targetAddress2 = "GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV";
Operation op1 = new Operation(type1, amount1, targetAddress1);
Operation op2 = new Operation(type2, amount2, targetAddress2);
```

* `amount` must be `String`.
* the unit of `amount` must be `GON`, `1BOS` is `10000000GON`.
* `target` should be valid address of keypair.

At this time, you can up to `100` operations in one transaction. This number can be adjusted. This limit also can be checked by node information.

### Transaction

* `sequence_id` is the last state of account. It will be varied when your account state is changed, so you should check the latest `sequence_id` from network. You can get the laetst `sequence_id` from https://bosnet.github.io/sebak/api/#accounts-account-details-get .

#### Sending Transation

You must sign you transaction instance before sengding transaction.
```
Transaction tx = new Transaction();
tx.AddOperation(op1);
tx.AddOperation(op2);
String secretSeed = "SDJLIFJ3PMT22C2IZAR4PY2JKTGPTACPX2NMT5NPERC2SWRWUE4HWOEE";
String sequence_id = "1";
String newtork_id = "sebak-test-network";
tx.Sign(secretSeed, sequence_id, newtork_id);
```

If you successfully sign your transaction, you can serialize your transaction instance to 'json'

```
String json = tx.ToJson();

{
  "H": {
    "version": "1",
    "created": "",
    "signature": "nU46BuF6f1PUUCoHoy3EXMxdibvRC6ZYyzLPsr4aNJYJnDDvSdcn52Qf9CGy5R9UbkMgW6mdKGwrHNvd3oCoRsp"
  },
  "B": {
    "source": "GAG5EESGOZIHTKK5N2NBHX25EWRC3S3TWZT7RMCSBX65A3KTJKILQKCF",
    "fee": "20000",
    "sequence_id": 1,
    "operations": [
      {
        "H": {
          "type": "payment"
        },
        "B": {
          "amount": "100",
          "target": "GB3AOQD2M5AKMNWUP2HFCVQYFQNGTAFTJ24BHZ56ONSGGOXMG3EBO6OE"
        }
      },
      {
        "H": {
          "type": "create-account"
        },
        "B": {
          "amount": "1000000",
          "target": "GD54SAKFHJ2QSBLEHZIQV3UWQ42OD6VQ6HKF6TN6F72US3AUQNDSONEV",
          "linked": ""
        }
      }
    ]
  }
}
```

So you are ready to submit transactons, you can just post your json to SEBAK node.

```sh
$ curl -v \
    -XPOST \
    -H 'Content-Type: application/json' \
    -d @your-transaction.json \
    https://testnet-sebak.blockchainos.org/api/v1/transactions
...
> POST /api/v1/transactions HTTP/2
> Host: testnet-sebak.blockchainos.org
> User-Agent: curl/7.62.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 749
>
* Connection state changed (MAX_CONCURRENT_STREAMS == 100)!
* We are completely uploaded and fine
< HTTP/2 200
< content-type: application/problem+json
< x-ratelimit-limit: 100
< x-ratelimit-remaining: 98
< x-ratelimit-reset: 1542432619
< date: Sat, 17 Nov 2018 05:30:08 GMT
< content-length: 101
< via: 1.1 google
< alt-svc: clear
<
* Connection #0 to host testnet-sebak.blockchainos.org left intact
...
```

The API of sending transaction, please see https://bosnet.github.io/sebak/api/#trasactions-transactions-post .

## Operations

* In SEBAK, like other cryptocurrencies there is 'fee'. 'fee' is multiplied by the number of operations in one transaction.

### `CreateAccount`

* `target` address must new account, this means, it does not exist in the SEBAK network. You can check the account status thru account API of SEBAK. Please see https://bosnet.github.io/sebak/api/#accounts-account-details-get .
* `amount` for creating account must be bigger than base reserve, you can check the amount from SEBAK node information like 'network-id'

### `Payment`

* `target` address must exist in network.
