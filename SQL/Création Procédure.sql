-- Procédures

--inscrireAgent
CREATE OR REPLACE FUNCTION projetshyeld.inscrireAgent(varchar(100),varchar(100),varchar(100)) RETURNS INTEGER AS 
$$
DECLARE 
	nom_agent ALIAS FOR $1;
	prenom_agent ALIAS FOR $2;
	mdp_agent ALIAS FOR $3;
	id INTEGER:=0;
BEGIN
	IF (nom_agent='')
	THEN 
		RAISE EXCEPTION 'Le nom ne peut pas etre vide!';
	END IF;

	IF (prenom_agent='')
	THEN 
		RAISE EXCEPTION 'Le prenom ne peut pas etre vide!';
	END IF;

	IF (mdp_agent='')
	THEN 
		RAISE EXCEPTION 'Le mot de passe ne peut pas etre vide!';
	END IF;
	
	INSERT INTO projetshyeld.agents VALUES
		(DEFAULT,nom_agent,prenom_agent,0,mdp_agent,'actif')
		RETURNING id_agent INTO id;

	return id;
END
$$ LANGUAGE plpgsql;

--mortAgent
CREATE OR REPLACE FUNCTION projetshyeld.mortAgent(INTEGER) RETURNS INTEGER AS 
$$
DECLARE 
	id_a ALIAS FOR $1;
BEGIN
	IF NOT EXISTS(SELECT * FROM projetshyeld.agents a
			WHERE a.id_agent = id_a 
			AND etat = 'actif' OR etat = 'retraite') THEN
		RAISE EXCEPTION 'Cet agent n existe pas ou est deja mort';
	END IF;
	
	UPDATE projetshyeld.agents
	SET etat='mort'
	WHERE id_agent=id_a;

	return id_a;
	
END
$$ LANGUAGE plpgsql;

--retraiteAgent
CREATE OR REPLACE FUNCTION projetshyeld.retraiteAgent(INTEGER) RETURNS INTEGER AS 
$$
DECLARE 
	id_a ALIAS FOR $1;
BEGIN
	IF NOT EXISTS(SELECT * FROM projetshyeld.agents a
			WHERE a.id_agent = id_a 
			AND etat = 'actif') THEN
		RAISE EXCEPTION 'Cet agent n existe pas, est mort ou est déjà retraité';
	END IF;
	
	UPDATE projetshyeld.agents
	SET etat='retraite'
	WHERE id_agent=id_a;

	return id_a;
	
END
$$ LANGUAGE plpgsql;

-- ajouterSuperheros
-- ,INTEGER,INTEGER,INTEGER,TIMESTAMP
CREATE OR REPLACE FUNCTION projetshyeld.ajoutersh(varchar(100),varchar(100),varchar(255),varchar(100),varchar(100),INTEGER,varchar(8),INTEGER,INTEGER,INTEGER,TIMESTAMP) RETURNS INTEGER AS 
$$
DECLARE 
	nom_civil_sh ALIAS FOR $1;
	nom_sh_sh ALIAS FOR $2;
	adresse_privee_sh ALIAS FOR $3;
	origine_sh ALIAS FOR $4;
	type_pouvoir_sh ALIAS FOR $5;
	puissance_pouvoir_sh ALIAS FOR $6;
	faction_sh ALIAS FOR $7;

	id_ag ALIAS FOR $8;
	c_x ALIAS FOR $9;
	c_y ALIAS FOR $10;
	date_rep ALIAS FOR $11;
	id_rep INTEGER:=0;
	id INTEGER:=0;
BEGIN
	IF EXISTS(SELECT * FROM projetshyeld.superheros sh
			WHERE sh.nom_sh = nom_sh_sh
			AND etat = 'vivant') THEN
		RAISE EXCEPTION 'un super hero vivant existe déjà avec ce nom de super heros)';
	END IF;

	IF ( faction_sh!='marvelle' AND faction_sh != 'dece' )
	THEN 
		RAISE EXCEPTION 'La faction du super hero ne peut etre que "marvelle" ou "dece"';
	END IF;
	-- ajoute un hero
	INSERT INTO projetshyeld.superheros VALUES
		(DEFAULT,nom_civil_sh,nom_sh_sh,adresse_privee_sh,origine_sh,type_pouvoir_sh,puissance_pouvoir_sh,faction_sh,0,0,0,'vivant')
		RETURNING id_sh INTO id;
	--ajoute un reperage 
	id_rep := projetshyeld.ajouterreperage(id_ag,nom_sh_sh,c_x,c_y,date_rep);

	return id;
END
$$ LANGUAGE plpgsql;

--mortSuperHeros
CREATE OR REPLACE FUNCTION projetshyeld.mortsh(varchar(100)) RETURNS INTEGER AS 
$$
DECLARE 
	nom_s ALIAS FOR $1;
BEGIN
	IF NOT EXISTS(SELECT * FROM projetshyeld.superheros sh
			WHERE sh.nom_sh = nom_s
			AND etat = 'vivant') THEN
		RAISE EXCEPTION 'Aucun super-hero vivant existe avec ce nom';
	END IF;
	
	UPDATE projetshyeld.superheros 
	SET etat='mort'
	WHERE nom_sh = nom_s
	AND etat = 'vivant';

	return 1;
	
END
$$ LANGUAGE plpgsql;

-- ajouter reperages

CREATE OR REPLACE FUNCTION projetshyeld.ajouterreperage(INTEGER,VARCHAR(100),INTEGER,INTEGER,TIMESTAMP) RETURNS INTEGER AS 
$$
DECLARE 
	id_ag ALIAS FOR $1;
	nom_suhe ALIAS FOR $2;
	c_x ALIAS FOR $3;
	c_y ALIAS FOR $4;
	date_rep ALIAS FOR $5;
	id_suhe INTEGER:=-1;
	id INTEGER:=0;
BEGIN
	IF (c_x > 100 OR c_x <0)
	THEN 
		RAISE EXCEPTION 'Coordonée x doit etre comprise entre 0 et 100';
	END IF;

	IF (c_y > 100 OR c_y <0)
	THEN 
		RAISE EXCEPTION 'Coordonée y doit etre comprise entre 0 et 100';
	END IF;
	SELECT sh.id_sh INTO id_suhe
	FROM projetshyeld.superheros sh
	WHERE sh.nom_sh=nom_suhe AND etat='vivant';
	
	IF (id_suhe IS NULL)
	THEN 
		RAISE EXCEPTION 'Aucun hero vivant avec ce nom';
	END IF;

	IF NOT EXISTS(SELECT * FROM projetshyeld.agents a
			WHERE a.id_agent = id_ag
			AND a.etat = 'actif') THEN
		RAISE EXCEPTION 'Auncun agent actif avec cet id';
	END IF;

	INSERT INTO projetshyeld.reperages 
	VALUES(DEFAULT,id_ag,id_suhe,c_x,c_y,date_rep)
	RETURNING id_reperage INTO id;

	return id;
END
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION projetshyeld.ajouterCombat(INTEGER,INTEGER,INTEGER,TIMESTAMP) RETURNS INTEGER AS 
$$
DECLARE
	id_ag ALIAS FOR $1;
	c_x ALIAS FOR $2;
	c_y ALIAS FOR $3;
	date_c ALIAS FOR $4;
	id INTEGER:=0;
BEGIN
	IF (c_x > 100 OR c_x <0)
	THEN 
		RAISE EXCEPTION 'Coordonée x doit etre comprise entre 0 et 100';
	END IF;

	IF (c_y > 100 OR c_y <0)
	THEN 
		RAISE EXCEPTION 'Coordonée y doit etre comprise entre 0 et 100';
	END IF;

	IF NOT EXISTS(SELECT * FROM projetshyeld.agents a
			WHERE a.id_agent = id_ag
			AND a.etat = 'actif') THEN
		RAISE EXCEPTION 'Auncun agent actif avec cet id';
	END IF;

	INSERT INTO projetshyeld.combats
	VALUES (DEFAULT,id_ag,c_x,c_y,date_c)
	RETURNING id_combat INTO id;

	return id;
	
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION projetshyeld.ajouterParticipation(INTEGER,VARCHAR(100),varchar(8))RETURNS INTEGER AS 
$$
DECLARE
	id_c ALIAS FOR $1;
	nom_suhe ALIAS FOR $2;
	result ALIAS FOR $3;
	
	id_suhe INTEGER:=-1;
	faction_sh VARCHAR(8):='';
	
BEGIN
	IF( result NOT IN ('victoire','defaite') AND result != NULL)
	THEN
		RAISE EXCEPTION 'Le resultat du combat ne peut etre que "victoire", "defaite" ou null' ;
	END IF;

	IF NOT EXISTS(SELECT * FROM projetshyeld.combats c
			WHERE c.id_combat = id_c) THEN
		RAISE EXCEPTION 'Aucun combat avec cet id';
	END IF;

	SELECT sh.id_sh,sh.faction INTO id_suhe,faction_sh
	FROM projetshyeld.superheros sh
	WHERE sh.nom_sh=nom_suhe AND etat='vivant';

	
	
	IF (id_suhe IS NULL)
	THEN 
		RAISE EXCEPTION 'Aucun hero vivant avec ce nom';
	END IF;

	INSERT INTO projetshyeld.participations
	VALUES (id_c,id_suhe,result);

	IF(faction_sh = 'marvelle')THEN
		return 1;
	ELSIF (faction_sh = 'dece')THEN
		return 2;
	END IF;
	
	return 0;
	
END;
$$ LANGUAGE plpgsql;

--Procedure pour triger

-- augmenter nbReperages
CREATE OR REPLACE FUNCTION triger_incNbReperages() RETURNS TRIGGER AS $triger_incNbReperages$
BEGIN
	UPDATE projetshyeld.agents
	SET nb_reperage = nb_reperage + 1
	WHERE id_agent = NEW.id_agent;
	RETURN NULL;
END;
$triger_incNbReperages$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION triger_incParticipations() RETURNS TRIGGER AS $triger_incParticipations$
BEGIN

	IF (NEW.resultat = 'victoire') THEN
		UPDATE projetshyeld.superheros
		SET nb_victoire = nb_victoire + 1,
			nb_part = nb_part + 1
		WHERE id_sh = NEW.id_sh;
	ELSIF (NEW.resultat = 'defaite') THEN
		UPDATE projetshyeld.superheros
		SET nb_defaite = nb_defaite + 1,
			nb_part = nb_part + 1
		WHERE id_sh = NEW.id_sh;
	ELSE
		UPDATE projetshyeld.superheros
		SET nb_part = nb_part + 1
		WHERE id_sh = NEW.id_sh;
	END IF;
	
	RETURN NULL;
	
END;
$triger_incParticipations$ LANGUAGE plpgsql;

--SELECT * FROM projetshyeld.mortAgent(1);

--SELECT * FROM projetshyeld.agents;