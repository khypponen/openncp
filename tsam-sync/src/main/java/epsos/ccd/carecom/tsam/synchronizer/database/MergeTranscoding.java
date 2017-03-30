package epsos.ccd.carecom.tsam.synchronizer.database;

import epsos.ccd.carecom.tsam.synchronizer.database.model.CodeSystemConceptEntity;
import epsos.ccd.carecom.tsam.synchronizer.database.model.Constants;
import epsos.ccd.carecom.tsam.synchronizer.database.model.TranscodingAssociationEntity;
import epsos.ccd.carecom.tsam.synchronizer.webservice.client.Transcoding;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.type.StandardBasicTypes;

import java.util.Date;
import java.util.List;

/**
 * Encapsulates a Hibernate unit of work that persists a transcoding entity.
 */
public class MergeTranscoding extends HibernateUnitOfWork {

    private Transcoding transcoding;

    /**
     * Default constructor.
     *
     * @param transcoding     A source transcoding object that holds the data that may be persisted.
     * @param isDebug         Indicates if the unit of work should be executed in debug mode.
     * @param truncateStrings Indicates if string values should be truncated.
     */
    public MergeTranscoding(Transcoding transcoding, boolean isDebug, boolean truncateStrings) {
        super(isDebug, truncateStrings);
        this.transcoding = transcoding;
    }

    @Override
    protected int unitOfWork(Session session) {
        Query query = session.createQuery("from TranscodingAssociationEntity t " +
                "where t.transcodingAssociasionId=:transcodingAssociasionId" +
                " and t.sourceConcept.id=:sourceConceptId " +
                "and t.targetConcept.id=:targetConceptId");
        query.setParameter("transcodingAssociasionId", this.transcoding.getId(), StandardBasicTypes.LONG);
        query.setParameter("sourceConceptId", this.transcoding.getSourceId(), StandardBasicTypes.LONG);
        query.setParameter("targetConceptId", this.transcoding.getTargetId(), StandardBasicTypes.LONG);
        List<TranscodingAssociationEntity> transcodingEntities = query.list();

        if (transcodingEntities == null || transcodingEntities.isEmpty()) {
            TranscodingAssociationEntity transcodingEntity = new TranscodingAssociationEntity();
            transcodingEntity.setTranscodingAssociasionId(this.transcoding.getId());
            CodeSystemConceptEntity sourceConcept = (CodeSystemConceptEntity) session
                    .get(CodeSystemConceptEntity.class,
                            this.transcoding.getSourceId());
            if (sourceConcept == null) {
                // This is probably caused by a concept that is not in any value set and hence is
                // not present in the local repository database.
                // Ignore the transcoding, clean up (close session), and return without changing
                // the database.
                return 0; // Do not need to clean up beacause we are running in a try-catch-finally setup.
            }
            CodeSystemConceptEntity targetConcept = (CodeSystemConceptEntity) session
                    .get(CodeSystemConceptEntity.class,
                            this.transcoding.getTargetId());
            if (targetConcept == null) {
                // This is probably caused by a concept that is not in any value set and hence is
                // not present in the local repository database.
                // Ignore the transcoding, clean up (close session), and return without changing
                // the database.
                return 0; // Do not need to clean up beacause we are running in a try-catch-finally setup.
            }
            transcodingEntity.setSourceConcept(sourceConcept);
            transcodingEntity.setTargetConcept(targetConcept);
            List<String> quality = this.transcoding.getQuality();
            if (quality != null && quality.size() > 0) {
                // TODO: Taking the first argument, because this is not specified to the needed detail.
                transcodingEntity.setQuality(quality.get(0));
            }
            transcodingEntity
                    .setStatus(safeStringEnum(Constants.MAP_TRANSCODING,
                            this.transcoding.getStatus(), ""));
            transcodingEntity.setStatusDate(safeDate(
                    this.transcoding.getStatusDate(), new Date()));
            registerEntity(transcodingEntity);
            session.save(transcodingEntity);
            return 1;
        }
        if (this.transcoding.getStatusDate() != null && transcodingEntities.size() == 1) {
            TranscodingAssociationEntity transcodingEntity = transcodingEntities.get(0);
            if (this.transcoding.getStatusDate().toGregorianCalendar().getTime().compareTo(transcodingEntity.getStatusDate()) > 0) {
                registerEntity(transcodingEntity);
                transcodingEntity.setStatusDate(this.transcoding.getStatusDate().toGregorianCalendar().getTime());
                transcodingEntity.setStatus(safeStringEnum(Constants.MAP_TRANSCODING, this.transcoding.getStatus(), ""));
                session.update(transcodingEntity);
                return 1;
            }
        }
        // Transcoding exists no need for insertion.
        logDuplicateRecord(TranscodingAssociationEntity.class.getName(), this.transcoding.getId());
        return 0;
    }
}
