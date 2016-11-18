--Création vues

CREATE OR REPLACE VIEW projetshyeld.heroperdu AS
	SELECT sh.nom_sh AS "Nom de Super-hero",
		concat('(',r.coord_x,'-',r.coord_y,')') AS "Coordonées"
	FROM projetshyeld.superheros sh,projetshyeld.reperages r
	WHERE r.id_sh=sh.id_sh
	AND r.date_reperage = (SELECT MAX(r2.date_reperage)
				FROM projetshyeld.reperages r2
				WHERE r2.id_sh = sh.id_sh)
	AND (DATE(r.date_reperage)+INTERVAL '15 day') <= NOW();

SELECT * FROM projetshyeld.heroperdu