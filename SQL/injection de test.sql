-- Injection de test

-- ajout agent

SELECT * FROM projetshyeld.inscrireAgent('Lambricht','Antoine');
SELECT * FROM projetshyeld.inscrireAgent('Fury','Nick');
SELECT * FROM projetshyeld.inscrireAgent('Coulson','Phil');


-- ajout sh
SELECT * FROM projetshyeld.ajoutersh('Bruce Wane','Batman','Manoir Wane','Terre','Art Martiaux',100,'dece');
SELECT * FROM projetshyeld.ajoutersh('Parker','Spiderman','chez sa tante','Terre','mutation génétique',72,'marvelle');

--ajout de reperages 
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,1,45,85,'2013-08-05 18:19:03');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,1,45,85,'2014-08-05 18:19:03');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,1,45,88,'2016-08-05 12:19:05');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,1,0,0,'2016-11-15 12:19:05');

INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,2,0,0,'2016-11-15 16:19:05');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,2,45,88,'2016-08-05 12:19:05');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,2,45,87,'2016-08-05 19:19:05');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,2,45,87,'2016-08-05 18:19:03');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,2,45,87,'2016-08-05 16:19:05');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,2,49,87,'2015-08-05 18:19:03');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,2,47,87,'2014-08-05 18:19:03');


SELECT * FROM projetshyeld.reperages;
SELECT * FROM projetshyeld.agents;
SELECT * FROM projetshyeld.superheros;
SELECT * FROM projetshyeld.dernierReperages;
SELECT * FROM projetshyeld.zonededanger;