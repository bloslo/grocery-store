INSERT INTO rule_action (description, rule, action)
VALUES ('5% discount for vegetables that weight between 0g and 100g', 'name = ''Vegetables'' and quantity > 0 and quantity <= 100', '5 / 100');

INSERT INTO rule_action (description, rule, action)
VALUES ('7% discount for vegetables that weight between 100g and 500g', 'name = ''Vegetables'' and quantity > 100 and quantity <= 500', '7 / 100');

INSERT INTO rule_action (description, rule, action)
VALUES ('10% discount for vegetables that weight more than 500g', 'name = ''Vegetables'' and quantity > 500', '10 / 100');

INSERT INTO rule_action (description, rule, action)
VALUES ('If the bread is 3 days old, buy 1 take 2', 'name = ''Bread'' and quantity > 1 and produced between ''3days''', 'unitPrice');

INSERT INTO rule_action (description, rule, action)
VALUES ('If the bread is 6 days old, buy 1 take 3', 'name = ''Bread'' and quantity >= 3 and produced between ''6days''', 'unitPrice * 2');

INSERT INTO rule_action (description, rule, action)
VALUES ('€2,00 for a Dutch beer pack', 'name = ''Dutch beer'' and quantity >= 6', '2.00 * nrOfBeerPacks');

INSERT INTO rule_action (description, rule, action)
VALUES ('€3,00 for a Belgium beer pack', 'name = ''Belgium beer'' and quantity >= 6', '3.00 * nrOfBeerPacks');

INSERT INTO rule_action (description, rule, action)
VALUES ('€4,00 for a German beer pack', 'name = ''German beer'' and quantity >= 6', '4.00 * nrOfBeerPacks');

INSERT INTO item (name, price, unit)
VALUES ('Bread', 2.00, 'none');

INSERT INTO item (name, price, unit)
VALUES ('Vegetables', 1.00, '100g');

INSERT INTO item (name, price, unit)
VALUES ('Dutch beer', 0.50, 'bottle');

INSERT INTO item (name, price, unit)
VALUES ('Belgium beer', 1.00, 'bottle');

INSERT INTO item (name, price, unit)
VALUES ('German beer', 1.50, 'bottle');
