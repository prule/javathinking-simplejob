/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javathinking.batch.event;

import java.util.Date;
import java.util.List;

/**
 * @author paul
 */
public interface EventDao {

    @SuppressWarnings(value = "unchecked")
    List<Event> list(final String name, final String message);

    void save(Event event);

    List<Event> list(Date from, Date to);

}
