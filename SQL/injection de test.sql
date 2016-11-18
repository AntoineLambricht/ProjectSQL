-- Injection de test

SELECT * FROM projetshyeld.inscrireAgent('Lambricht','Antoine');
SELECT * FROM projetshyeld.inscrireAgent('Fury','Nick');
SELECT * FROM projetshyeld.inscrireAgent('Coulson','Phil');

SELECT * FROM projetshyeld.agents;

INSERT INTO projetshyeld.superheros VALUES(DEFAULT,'Bruce Wane','Batman','Manoir Wane','Terre','Art Martiaux',100,'dece','vivant');

SELECT * FROM projetshyeld.superheros;

INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,1,45,85,'2013-08-05 18:19:03');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,1,45,85,'2014-08-05 18:19:03');

SELECT * FROM projetshyeld.reperages;