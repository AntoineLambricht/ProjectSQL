-- Procédures

--inscrireAgent
CREATE OR REPLACE FUNCTION projetshyeld.inscrireAgent(varchar(100),varchar(100)) RETURNS INTEGER AS 
$$
DECLARE 
	nom_agent ALIAS FOR $1;
	prenom_agent ALIAS FOR $2;
	id INTEGER:=0;
BEGIN
	INSERT INTO projetshyeld.agents VALUES
		(DEFAULT,nom_agent,prenom_agent,'actif')
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
		RAISE 'agent n existe pas ou est deja mort';
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
		RAISE 'agent n existe pas, est déja retraité ou mort';
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
	--etat_sh ALIAS FOR $8;
	id INTEGER:=0;
BEGIN
	IF EXISTS(SELECT * FROM projetshyeld.superheros sh
			WHERE sh.nom_sh = nom_sh_sh
			AND etat = 'vivant') THEN
		RAISE 'un super hero vivant existe déjà avec ce nom de super heros)';
	END IF;
	INSERT INTO projetshyeld.superheros VALUES
		(DEFAULT,nom_civil_sh,nom_sh_sh,adresse_privee_sh,origine_sh,type_pouvoir_sh,puissance_pouvoir_sh,faction_sh,'vivant')
		RETURNING id_sh INTO id;

	return id;
END
$$ LANGUAGE plpgsql;

--mortSuperHeros
CREATE OR REPLACE FUNCTION projetshyeld.mortsh(INTEGER) RETURNS INTEGER AS 
$$
DECLARE 
	id_s ALIAS FOR $1;
BEGIN
	IF NOT EXISTS(SELECT * FROM projetshyeld.superheros sh
			WHERE sh.id_sh = id_s
			AND etat = 'vivant') THEN
		RAISE 'super hero n existe pas ou est deja mort';
	END IF;
	
	UPDATE projetshyeld.superheros
	SET etat='mort'
	WHERE id_sh=id_s;

	return id_s;
	
END
$$ LANGUAGE plpgsql;

--création du type de retour de la fonction zones de dangers
CREATE TYPE projetshyeld.zonededangerReturn
AS (x1 INTEGER, y1 INTEGER,x2 INTEGER,y2 INTEGER);


--Zones de dangers
CREATE OR REPLACE FUNCTION projetshyeld.zonededanger()
	RETURNS SETOF exercice.evolutionCompteReturn AS $$
DECLARE
	reperage RECORD;
	sortie projetshyeld.zonededangerReturn;
BEGIN
	FOR reperage IN SELECT * FROM projetshyeld.reperages r 
		WHERE (DATE(r.date_reperage)+INTERVAL '10 day') <= NOW()
		AND 
	LOOP
	
	END LOOP;
END;
$$ LANGUAGE 'plpgsql';



--SELECT * FROM projetshyeld.mortAgent(1);

--SELECT * FROM projetshyeld.agents;