--Création vues

CREATE OR REPLACE VIEW projetshyeld.heroperdu AS
	(SELECT sh.nom_sh AS "Nom de Super-hero",
		concat('(',r.coord_x,'-',r.coord_y,')') AS "Coordonées"
	FROM projetshyeld.superheros sh,projetshyeld.reperages r
	WHERE r.id_sh=sh.id_sh
	AND r.date_reperage = (SELECT MAX(r2.date_reperage)
				FROM projetshyeld.reperages r2
				WHERE r2.id_sh = sh.id_sh)
	AND (DATE(r.date_reperage)+INTERVAL '15 day') <= NOW());
	/*UNION
	((SELECT sh2.nom_sh ,'Jammais aperçu'
	FROM projetshyeld.superheros sh2
	WHERE sh2.id_sh NOT IN (SELECT r3.id_sh 
				FROM projetshyeld.reperages r3)));*/

CREATE OR REPLACE VIEW projetshyeld.dernierReperages AS
	SELECT r.*
	FROM projetshyeld.reperages r ,projetshyeld.superheros sh
	WHERE r.id_sh=sh.id_sh
	AND r.date_reperage = (SELECT MAX(r2.date_reperage)
				FROM projetshyeld.reperages r2
				WHERE r2.id_sh = sh.id_sh);

--DROP VIEW projetshyeld.zonededanger;
CREATE OR REPLACE VIEW projetshyeld.zonededanger AS
	SELECT DISTINCT concat('(',dr1.coord_x,'-',dr1.coord_y,')') AS "Zone 1",concat('(',dr2.coord_x,'-',dr2.coord_y,')') AS "Zone 2"
	--SELECT DISTINCT dr1.coord_x AS "Zone 1 (x)",dr1.coord_y AS "Zone 1 (y)",dr2.coord_x AS "Zone 2 (x)",dr2.coord_y AS "Zone 2 (y)"
	FROM projetshyeld.dernierReperages dr1 , projetshyeld.dernierReperages dr2 , 
		projetshyeld.superheros sh1 , projetshyeld.superheros sh2
	WHERE (DATE(dr1.date_reperage)+INTERVAL '10 day') >= NOW()
	AND (DATE(dr2.date_reperage)+INTERVAL '10 day') >= NOW()
	AND dr1.id_sh = sh1.id_sh
	AND dr2.id_sh = sh2.id_sh
	AND ((dr1.coord_x+1=dr2.coord_x AND dr1.coord_y=dr2.coord_y)
		OR (dr1.coord_x-1=dr2.coord_x AND dr1.coord_y=dr2.coord_y)
		OR (dr1.coord_y+1=dr2.coord_y AND dr1.coord_x=dr2.coord_x)
		OR (dr1.coord_y-1=dr2.coord_y AND dr1.coord_x=dr2.coord_x)
		OR (dr1.coord_x=dr2.coord_x AND dr1.coord_y=dr2.coord_y))
		
	AND sh1.faction!=sh2.faction
	AND dr1.id_reperage<dr2.id_reperage;

CREATE OR REPLACE VIEW projetshyeld.infoShVivant AS 
	(SELECT sh.nom_civil,sh.nom_sh,sh.adresse_privee,sh.origine,sh.type_pouvoir,sh.puissance_pouvoir,sh.faction,sh.nb_victoire,sh.nb_defaite,sh.nb_part,concat('(',dr.coord_x,'-',dr.coord_y,')') AS "Coordonées"
	FROM projetshyeld.superheros sh, projetshyeld.dernierReperages dr
	WHERE sh.id_sh = dr.id_sh
	AND sh.etat='vivant')
	UNION
	(SELECT sh.nom_civil,sh.nom_sh,sh.adresse_privee,sh.origine,sh.type_pouvoir,sh.puissance_pouvoir,sh.faction,sh.nb_victoire,sh.nb_defaite,sh.nb_part,'Jammais repèré'
	FROM projetshyeld.superheros sh
	WHERE sh.id_sh NOT IN (SELECT dr.id_sh FROM projetshyeld.dernierReperages dr)
	AND sh.etat='vivant')


