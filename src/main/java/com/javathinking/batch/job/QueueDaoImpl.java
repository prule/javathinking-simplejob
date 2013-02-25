package com.javathinking.batch.job;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author paul
 */
@Transactional
public class QueueDaoImpl extends JpaDaoSupport implements QueueDao {

    public void save(Queue queue) {
        getJpaTemplate().persist(queue);
    }

    @SuppressWarnings("unchecked")
    public List<Queue> list(final String type, final String discriminator, final TaskResult.Result result) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("discriminator", discriminator);

        StringBuilder query = new StringBuilder(
                " from Queue as q where " +
                        " q.type = :type and " +
                        " q.discriminator = :discriminator ");
        if (result != null) {
            query.append(" and q.endresult = :endresult ");
            params.put("endresult", result);
        }
        query.append(" order by timeStamp asc");


        return (List<Queue>) getJpaTemplate().findByNamedParams(query.toString(), params);
    }

    public void delete(Queue queue) {
        // TODO fix this
        getJpaTemplate().remove(getJpaTemplate().find(Queue.class, queue.getId()));
    }
}
