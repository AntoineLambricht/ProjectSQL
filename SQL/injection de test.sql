-- Injection de test

-- ajout agent

SELECT * FROM projetshyeld.inscrireAgent('Lambricht','Antoine');
SELECT * FROM projetshyeld.inscrireAgent('Fury','Nick');
SELECT * FROM projetshyeld.inscrireAgent('Coulson','Phil');


-- ajout sh
INSERT INTO projetshyeld.superheros VALUES(DEFAULT,'Bruce Wane','Batman','Manoir Wane','Terre','Art Martiaux',100,'dece','vivant');

--ajout de reperages 
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,1,45,85,'2013-08-05 18:19:03');
INSERT INTO projetshyeld.reperages VALUES(DEFAULT,1,1,45,85,'2014-08-05 18:19:03');


SELECT * FROM projetshyeld.reperages;
SELECT * FROM projetshyeld.agents;
SELECT * FROM projetshyeld.superheros;