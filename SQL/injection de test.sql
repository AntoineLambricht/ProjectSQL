-- Injection de test

-- ajout agent

SELECT * FROM projetshyeld.inscrireAgent('Lambricht','Antoine','mdpnul');
SELECT * FROM projetshyeld.inscrireAgent('Fury','Nick','mdpnul');
SELECT * FROM projetshyeld.inscrireAgent('Coulson','Phil','mdpnul');


-- ajout sh
SELECT * FROM projetshyeld.ajoutersh('Bruce Wane','Batman','Manoir Wane','Terre','Art Martiaux',100,'dece');
SELECT * FROM projetshyeld.ajoutersh('Parker','Spiderman','chez sa tante','Terre','mutation génétique',72,'marvelle');

--ajout de reperages 
SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:05');
SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:04');
SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:03');
SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:02');
SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:01');

SELECT * FROM projetshyeld.ajouterreperage(1,'Spiderman',54,98,'2016-11-23 10:15:08');
SELECT * FROM projetshyeld.ajouterreperage(2,'Spiderman',54,98,'2016-11-23 10:15:07');
SELECT * FROM projetshyeld.ajouterreperage(1,'Spiderman',54,98,'2016-11-23 10:15:06');
SELECT * FROM projetshyeld.ajouterreperage(2,'Spiderman',54,98,'2016-11-23 10:15:05');
SELECT * FROM projetshyeld.ajouterreperage(1,'Spiderman',54,98,'2016-11-23 10:15:04');
SELECT * FROM projetshyeld.ajouterreperage(2,'Spiderman',54,98,'2016-11-23 10:15:03');
SELECT * FROM projetshyeld.ajouterreperage(1,'Spiderman',54,98,'2016-11-23 10:15:02');
SELECT * FROM projetshyeld.ajouterreperage(2,'Spiderman',54,98,'2016-11-23 10:15:01');



SELECT * FROM projetshyeld.reperages;
SELECT * FROM projetshyeld.agents;
SELECT * FROM projetshyeld.superheros;
SELECT * FROM projetshyeld.dernierReperages;
SELECT * FROM projetshyeld.zonededanger;