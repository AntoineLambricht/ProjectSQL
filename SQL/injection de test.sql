-- Injection de test

-- ajout agent

--SELECT * FROM projetshyeld.inscrireAgent('Lambricht','Antoine','mdpnul');
--SELECT * FROM projetshyeld.inscrireAgent('Fury','Nick','mdpnul');
--SELECT * FROM projetshyeld.inscrireAgent('Coulson','Phil','mdpnul');



-- ajout sh
--SELECT * FROM projetshyeld.ajoutersh('Christophe Damas','Docteur Dramas','Dans son labo secret','Terre','Scientifique fous',50,'dece',1,0,0,'2016-1-1 10:00:00');
--SELECT * FROM projetshyeld.ajoutersh('Stéphanie Ferneeuw','Madame Faire Mieux','30 rue du pas parfais','Pluton','Mutations génétique',72,'dece',1,0,0,'2016-1-1 10:00:00');
--SELECT * FROM projetshyeld.ajoutersh('Donatien Grolaux','Gloriaux','Monoir Grolaux','Terre','arts-martiaux',86,'marvelle',1,0,0,'2016-1-1 10:00:00');
--SELECT * FROM projetshyeld.ajoutersh('Bernard Henriet','Hulkriet','Inde','Terre','Force et Vert',185,'marvelle',1,0,0,'2016-1-1 10:00:00');

INSERT INTO projetshyeld.superheros 
VALUES (DEFAULT,'Christophe Damas','Docteur Dramas','Dans son labo secret','Terre','Scientifique fous',50,'dece',0,0,0,'vivant');
INSERT INTO projetshyeld.superheros 
VALUES (DEFAULT,'Stéphanie Ferneeuw','Faire Mieux','30 rue du pas parfais','Pluton','Mutations génétique',72,'dece',0,0,0,'vivant');
INSERT INTO projetshyeld.superheros 
VALUES (DEFAULT,'Donatien Grolaux','Gloriaux','Monoir Grolaux','Terre','arts-martiaux',86,'marvelle',0,0,0,'vivant');
INSERT INTO projetshyeld.superheros 
VALUES (DEFAULT,'Bernard Henriet','Hulkriet','Inde','Terre','Force et Vert',185,'marvelle',0,0,0,'vivant');

--ajout de reperages 
--SELECT * FROM projetshyeld.ajouterreperage(2,'Docteur Dramas',2,3,'2016-12-2 10:15:06');
--SELECT * FROM projetshyeld.ajouterreperage(3,'Gloriaux',1,3,'2016-12-3 10:15:05');
--SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:04');
--SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:03');
--SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:02');
--SELECT * FROM projetshyeld.ajouterreperage(1,'Batman',54,98,'2016-11-23 10:15:01');

--SELECT * FROM projetshyeld.ajouterreperage(1,'Spiderman',54,98,'2016-11-23 10:15:08');
--SELECT * FROM projetshyeld.ajouterreperage(2,'Spiderman',54,98,'2016-11-23 10:15:07');
--SELECT * FROM projetshyeld.ajouterreperage(1,'Spiderman',54,98,'2016-11-23 10:15:06');
--SELECT * FROM projetshyeld.ajouterreperage(2,'Spiderman',54,98,'2016-11-23 10:15:05');
--SELECT * FROM projetshyeld.ajouterreperage(1,'Spiderman',54,98,'2016-11-23 10:15:04');
--SELECT * FROM projetshyeld.ajouterreperage(2,'Spiderman',54,98,'2016-11-23 10:15:03');
--SELECT * FROM projetshyeld.ajouterreperage(1,'Spiderman',54,98,'2016-11-23 10:15:02');
--SELECT * FROM projetshyeld.ajouterreperage(2,'Spiderman',54,98,'2016-11-23 10:15:01');



SELECT * FROM projetshyeld.reperages;
SELECT * FROM projetshyeld.agents;
SELECT * FROM projetshyeld.superheros;
SELECT * FROM projetshyeld.heroperdu;
SELECT * FROM projetshyeld.dernierReperages;
SELECT * FROM projetshyeld.zonededanger;
SELECT * FROM projetshyeld.infoShVivant;
SELECT * FROM projetshyeld.combats;