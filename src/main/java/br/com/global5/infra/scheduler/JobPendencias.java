package br.com.global5.infra.scheduler;

/**
 * Created by zielinski on 20/12/17.
 */

import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.persistence.*;

@Scheduled(cronExpression = "0 1 * * * ?")
public class JobPendencias implements org.quartz.Job {

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            String sql =
                    "UPDATE analise_cadastral_pendencia AS acp" +
                            "   SET apen_pendencia_status = 212 " +
                            "  FROM analise_cadastral AS ac " +
                            " WHERE ac.anacoid = acp.apen_anacoid " +
                            "   AND ac.anac_status IN (7,9) " +
                            "   AND acp.apen_pendencia_status != 212;";

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("localhost");

            EntityManager entityManager = emf.createEntityManager();

            if (null != entityManager) {
                EntityTransaction updateTransaction = entityManager.getTransaction();
                updateTransaction.begin();
                Query query = entityManager.createNativeQuery(sql);
                int updateCount = query.executeUpdate();
                updateTransaction.commit();
            }

            entityManager.close();
            emf.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}