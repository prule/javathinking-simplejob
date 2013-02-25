package com.javathinking.batch.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/integrationTestContext.xml"})
public class EventServiceIntegrationTest {

    @Autowired
    EventService eventService;

    @Test
    public void testNotif() {
        Date start = new Date();

        Event event = new Event("EVENT1", "test1", null);
        eventService.send(event);

        Date end = new Date();

        List<Event> events = eventService.list(start, end);
        assertEquals(1, events.size());
        assertEquals("EVENT1", events.get(0).getName());
        assertEquals("test1", events.get(0).getMessage());
    }
}
