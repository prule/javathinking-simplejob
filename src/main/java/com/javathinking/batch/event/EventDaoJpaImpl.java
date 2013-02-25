package com.javathinking.batch.event;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author paul
 */
@Transactional
public class EventDaoJpaImpl extends JpaDaoSupport implements EventDao {

    public void save(Event event) {
        getJpaTemplate().persist(event);
    }

    @SuppressWarnings("unchecked")
    public List<Event> list(final String name, final String message) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("message", message);
        return (List<Event>) getJpaTemplate().findByNamedParams("from Event as e where e.name = :name and e.message like :message order by createtimestamp asc", params);
    }

    public List<Event> list(Date from, Date to) {
        Map<String, Date> params = new HashMap<String, Date>();
        params.put("from", from);
        params.put("to", to);
        return (List<Event>) getJpaTemplate().findByNamedParams("from Event as e where e.createtimestamp between :from and :to order by createtimestamp asc", params);
    }
}
