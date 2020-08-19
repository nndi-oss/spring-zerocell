// [Zefaker](https://github.com/creditdatamw/zefaker) configuration file:
generateFrom([
    (column(index=0, name="Account Number")): { faker -> faker.number().numberBetween(3000, 99999) },
    (column(index=1, name="Customer Name")): { faker ->  faker.name().fullName() },
    (column(index=2, name="E-mail")): { faker ->  faker.internet().emailAddress() },
    (column(index=3, name="Address")): { faker ->  faker.country().name() }
])