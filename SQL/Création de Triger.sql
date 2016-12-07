--Création de triger

-- triger nbReperages agents

CREATE TRIGGER triger_incNbReperages AFTER INSERT ON projetshyeld.reperages
    FOR EACH ROW EXECUTE PROCEDURE triger_incNbReperages();
-- triger on participation
CREATE TRIGGER triger_incParticipations AFTER INSERT ON projetshyeld.participations
    FOR EACH ROW EXECUTE PROCEDURE triger_incParticipations();