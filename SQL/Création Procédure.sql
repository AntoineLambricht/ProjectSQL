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
CREATE OR REPLACE FUNCTION projetshyeld.ajoutersh(varchar(100),varchar(100),varchar(255),varchar(100),varchar(100),INTEGER,varchar(8)) RETURNS INTEGER AS 
$$
DECLARE 
	nom_civil_sh ALIAS FOR $1;
	nom_sh_sh ALIAS FOR $2;
	adresse_privee_sh ALIAS FOR $3;
	origine_sh ALIAS FOR $4;
	type_pouvoir_sh ALIAS FOR $5;
	puissance_pouvoir_sh ALIAS FOR $6;
	faction_sh ALIAS FOR $7;
	id INTEGER:=0;
BEGIN
	IF EXISTS(SELECT * FROM projetshyeld.superheros sh
			WHERE sh.nom_sh = nom_sh_sh
			AND etat = 'vivant') THEN
		RAISE EXCEPTION 'un super hero vivant existe déjà avec ce nom de super heros)';
	END IF;
	INSERT INTO projetshyeld.superheros VALUES
		(DEFAULT,nom_civil_sh,nom_sh_sh,adresse_privee_sh,origine_sh,type_pouvoir_sh,puissance_pouvoir_sh,faction_sh,'vivant')
		RETURNING id_sh INTO id;

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
--SELECT * FROM projetshyeld.mortAgent(1);

--SELECT * FROM projetshyeld.agents;