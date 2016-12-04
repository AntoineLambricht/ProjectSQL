--Création de triger

-- triger nbReperages agents

CREATE TRIGGER triger_incNbReperages AFTER INSERT ON projetshyeld.reperages
    FOR EACH ROW EXECUTE PROCEDURE triger_incNbReperages();
