--DROP SCHEMA IF EXISTS projetshyeld CASCADE;

--Création du shema
CREATE SCHEMA projetshyeld;

--Création des pk auto-inc

CREATE SEQUENCE projetshyeld.pk_agent;
CREATE SEQUENCE projetshyeld.pk_sh;
CREATE SEQUENCE projetshyeld.pk_combat;
CREATE SEQUENCE projetshyeld.pk_reperage;

--Create Table

CREATE TABLE projetshyeld.agents(
	id_agent INTEGER PRIMARY KEY 
		DEFAULT NEXTVAL ('projetshyeld.pk_agent'),
	nom VARCHAR(100) NOT NULL CHECK (nom<>''),
	prenom VARCHAR(100) NOT NULL CHECK (prenom<>''),
	etat VARCHAR(8) NOT NULL CHECK (etat IN ('actif','mort','retraite')),
	nb_reperage INTEGER NOT NULL CHECK (nb_reperage>=0)
);

CREATE TABLE projetshyeld.superheros(
	id_sh INTEGER PRIMARY KEY 
		DEFAULT NEXTVAL ('projetshyeld.pk_sh'),
	nom_civil VARCHAR(100),
	nom_sh VARCHAR(100) NOT NULL CHECK (nom_sh<>''),
	adresse_privee VARCHAR(255),
	origine VARCHAR(100),
	type_pouvoir VARCHAR(100),
	puissance_pouvoir INTEGER,
	faction VARCHAR(8) NOT NULL CHECK (faction IN ('marvelle','dece')), 
	etat VARCHAR(6) NOT NULL CHECK (etat IN ('vivant','mort'))
);


CREATE TABLE projetshyeld.combats(
	id_combat INTEGER PRIMARY KEY 
		DEFAULT NEXTVAL ('projetshyeld.pk_combat'),
	id_agent INTEGER REFERENCES projetshyeld.agents (id_agent),
	coord_x INTEGER NOT NULL CHECK (coord_x >= 0 AND coord_x <= 100),
	coord_y INTEGER NOT NULL CHECK (coord_y >= 0 AND coord_y <= 100),
	date_combat TIMESTAMP NOT NULL
);

CREATE TABLE projetshyeld.reperages(
	id_reperage INTEGER PRIMARY KEY 
		DEFAULT NEXTVAL ('projetshyeld.pk_reperage'),
	id_agent INTEGER REFERENCES projetshyeld.agents (id_agent),
	id_sh INTEGER REFERENCES projetshyeld.superheros (id_sh),
	coord_x INTEGER NOT NULL CHECK (coord_x >= 0 AND coord_x <= 100),
	coord_y INTEGER NOT NULL CHECK (coord_y >= 0 AND coord_y <= 100),
	date_reperage TIMESTAMP NOT NULL,
	UNIQUE(id_sh,date_reperage)
);

CREATE TABLE projetshyeld.participation(
	id_combat INTEGER NOT NULL REFERENCES projetshyeld.combats (id_combat),
	id_sh INTEGER REFERENCES projetshyeld.superheros (id_sh),
	resultat VARCHAR(8) CHECK (resultat IN ('victoire','defaite') OR resultat = NULL),
	PRIMARY KEY (id_combat,id_sh)
);