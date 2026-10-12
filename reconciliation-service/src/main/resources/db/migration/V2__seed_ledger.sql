-- V2__seed_ledger.sql: test ledger for sample-transaction-batch-50.xml
-- Balances are in INR. Account numbers come from the sample file.
INSERT INTO ledger_balances (account_number, expected_balance) VALUES
    ('ACC2867825', 27700.00),  -- TXN-00001 THIN -> expect OVERDRAFT (1260.29 AUD)
    ('ACC2458591', 12255000.00),  -- TXN-00002 well funded
    ('ACC1445199', 1123000.00),  -- TXN-00003 well funded
    ('ACC1109031', 19920000.00),  -- TXN-00004 well funded
    ('ACC6647119', 2432000.00),  -- TXN-00005 well funded
    ('ACC1728977', 1460000.00),  -- TXN-00006 well funded
    ('ACC2338687', 19246000.00),  -- TXN-00008 well funded
    ('ACC7210606', 4887000.00),  -- TXN-00009 well funded
    ('ACC8755439', 60700.00),  -- TXN-00010 THIN -> expect OVERDRAFT (18978.98 SEK)
    ('ACC1538552', 19121000.00),  -- TXN-00011 well funded
    ('ACC9375710', 1138000.00),  -- TXN-00012 well funded
    ('ACC5408072', 17743000.00),  -- TXN-00013 well funded
    ('ACC9279821', 1891000.00),  -- TXN-00014 well funded
    ('ACC2065818', 4812000.00),  -- TXN-00015 well funded
    ('ACC2921859', 1364000.00),  -- TXN-00016 well funded
    ('ACC8294150', 3286000.00),  -- TXN-00017 well funded
    ('ACC6007072', 134600.00),  -- TXN-00018 THIN -> expect OVERDRAFT (42085.05 SEK)
    ('ACC7089806', 12083000.00),  -- TXN-00020 well funded
    ('ACC9153566', 1633000.00),  -- TXN-00021 well funded
    ('ACC9852897', 20721000.00),  -- TXN-00022 well funded
    ('ACC7264956', 877000.00),  -- TXN-00023 well funded
    ('ACC6672134', 100000.00),  -- TXN-00024 well funded
    ('ACC1987737', 4754000.00),  -- TXN-00025 well funded
    ('ACC9143903', 5628000.00),  -- TXN-00026 well funded
    ('ACC2582524', 2307000.00),  -- TXN-00027 well funded
    ('ACC1908841', 1212100.00),  -- TXN-00028 THIN -> expect OVERDRAFT (33671.49 EUR)
    ('ACC4214348', 548000.00),  -- TXN-00029 well funded
    ('ACC5191056', 19677000.00),  -- TXN-00030 well funded
    ('ACC1247595', 134000.00),  -- TXN-00031 well funded
    ('ACC7728339', 23690000.00),  -- TXN-00032 well funded
    ('ACC1981186', 1159000.00),  -- TXN-00034 well funded
    ('ACC8999183', 1006000.00),  -- TXN-00035 well funded
    ('ACC2140194', 100000.00),  -- TXN-00036 well funded
    ('ACC2375453', 5242000.00),  -- TXN-00037 well funded
    ('ACC5456272', 435400.00),  -- TXN-00038 THIN -> expect OVERDRAFT (19795.33 AUD)
    ('ACC2677407', 100000.00),  -- TXN-00039 well funded
    ('ACC5098412', 9704000.00),  -- TXN-00040 well funded
    ('ACC6022741', 20965000.00),  -- TXN-00041 well funded
    ('ACC3607983', 3407000.00),  -- TXN-00042 well funded
    ('ACC5428914', 6951000.00),  -- TXN-00043 well funded
    ('ACC1059486', 8760000.00),  -- TXN-00044 well funded
    ('ACC1162230', 2942000.00),  -- TXN-00045 well funded
    ('ACC3138181', 641000.00),  -- TXN-00046 well funded
    ('ACC2724590', 708000.00),  -- TXN-00047 well funded
    ('ACC3726327', 25626000.00),  -- TXN-00048 well funded
    ('ACC7907400', 111000.00),  -- TXN-00049 well funded
    ('ACC1649691', 119000.00);  -- TXN-00050 well funded

-- Deliberately NOT seeded (expect UNKNOWN_ACCOUNT): TXN-00007 (ACC7067228)
-- TXN-00019 (ACC9897858)
-- TXN-00033 (ACC8633773)