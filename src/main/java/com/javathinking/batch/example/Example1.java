package com.javathinking.batch.example;

import com.javathinking.batch.event.EmailNotification;
import com.javathinking.batch.event.EmailNotificationFactory;
import com.javathinking.batch.event.EventService;
import com.javathinking.batch.event.NotificationHandler;
import com.javathinking.batch.job.DirectoryMonitor;
import com.javathinking.batch.job.Monitor;
import com.javathinking.batch.job.QueueService;
import com.javathinking.batch.job.TaskResult.Result;
import com.javathinking.batch.job.ThreadedTaskManager;
import com.javathinking.batch.support.ArchiveService;
import com.javathinking.batch.validation.FilenameExtensionRule;
import com.javathinking.batch.validation.FilenameRegExRule;
import com.javathinking.batch.validation.ZipFileRule;
import com.javathinking.commons.validation.Validator;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.net.URL;

/**
 * A sample application showing how to use 'simplejob' to process files from partners:
 * <p/>
 * For each partner:
 * 1. A DirectoryMonitor is used to poll a partner directory, looking for only '*.zip' files
 * 2. A FileReceiveTask handles each incoming file and archives it, then adds to the queue for further processing
 * <p/>
 * 3. A QueueMonitor is used to poll for 'FILE_RX' events
 * 4. A FileValidateTask handles each event, processes it, and then deletes the event
 * <p/>
 * These Monitors and Tasks are created for each partner, so that processing happens in PARALLEL ACROSS PARTNERS
 * but SEQUENTIALLY WITHIN PARTNERS.
 */
public class Example1 {
    private static final Logger log = Logger.getLogger(Example1.class);

    private static final String[] EXTENSIONS = new String[]{"zip", "xml"};
    private static final String BASEDIR = "/tmp/test";
    private static final String WORKDIR = "/tmp/work";
    private static final String SHIP_ORDER_ZIP = "com/javathinking/batch/example/shiporder.zip";
    private static final String SHIP_ORDER_INVALID_XML_ZIP = "com/javathinking/batch/example/shiporderInvalidXml.zip";
    private static final String EMPTY_ZIP = "com/javathinking/batch/example/empty.zip";
    private static final String INVALID_NAME = "com/javathinking/batch/example/invalidname";
    private static final String SHIP_ORDER_XML = "com/javathinking/batch/example/shiporder.xml";

    public static final String PROC_FAILED_EMAIL = "PROCFAILED";
    public static final String PROC_SUCCESS_EMAIL = "PROCSUCCESS";

    public enum EVENTS {FILE_RX, FILE_VALIDATE, FILE_UNZIP}

    ;

    public enum QUEUES {FILE_RX, FILE_VALIDATE, FILE_PROCESSED, FILE_UNZIP}

    ;

    public static void main(String[] args) throws Exception {
        File basedir = new File(BASEDIR);
        File workdir = new File(WORKDIR);
        File dbdir = new File("./test/db");

        basedir.mkdirs();
        workdir.mkdirs();
        dbdir.mkdirs();

        FileUtils.cleanDirectory(basedir);
        FileUtils.cleanDirectory(workdir);
        FileUtils.cleanDirectory(dbdir);

        new Example1().go();
    }

    private void go() throws Exception {
//        BasicConfigurator.configure();

        System.out.println("SimpleJob Sample application starting");
        log.info("SimpleJob Sample application starting");

        final String[] partners = new String[]{"1"};//, "2"};

        final File baseDir = new File(BASEDIR);

        ApplicationContext context = new ClassPathXmlApplicationContext("com/javathinking/batch/example/exampleContext.xml");
        ArchiveService archiveService = (ArchiveService) context.getBean("archiveService");
        QueueService queueService = (QueueService) context.getBean("queueService");
        EventService eventService = (EventService) context.getBean("eventService");

        // set up emails
        EmailNotificationFactory emailNotificationFactory = (EmailNotificationFactory) context.getBean("emailNotificationFactory");

        EmailNotification processingFailedExampleEmail = new EmailNotification();
        processingFailedExampleEmail.setSubject("Processing file failed");
        processingFailedExampleEmail.addTo("paul@javathinking.com");
        processingFailedExampleEmail.setFrom("paul@javathinking.com");
        emailNotificationFactory.addType(PROC_FAILED_EMAIL, processingFailedExampleEmail);

        EmailNotification processingSuccessExampleEmail = new EmailNotification();
        processingSuccessExampleEmail.setSubject("Processing successful");
        processingSuccessExampleEmail.addTo("paul@javathinking.com");
        processingSuccessExampleEmail.setFrom("paul@javathinking.com");
        emailNotificationFactory.addType(PROC_SUCCESS_EMAIL, processingSuccessExampleEmail);

        eventService.registerNotificationHandler(EmailNotification.class, (NotificationHandler) context.getBean("emailNotificationHandler"));

        ThreadedTaskManager manager = new ThreadedTaskManager();
        manager.setQueueService(queueService);

        // Set up the task pipeline for each partner
        for (String dir : partners) {
            // Set up a directory monitor
            File directory = new File(baseDir, dir);
            String id = directory.getName();
            Monitor<File> dirMon1 = new DirectoryMonitor(directory, EXTENSIONS, null, true);

            // receive the file
            final FileReceiveTask fileReceiveTask = new FileReceiveTask(id, "FILERX");
            fileReceiveTask.setArchiveService(archiveService);
            fileReceiveTask.setEventService(eventService);
            manager.addTask(5000, dirMon1, fileReceiveTask);

            // check the zip file we get looks okay
            FileValidateTask fileValidateTask = new FileValidateTask(id, "FILEVALIDATE", new Validator(true,
                    new FilenameExtensionRule("zip", true), // check file name ext is .zip
                    new FilenameRegExRule("(?i)[a-z]+\\.zip$"), // check file name only has alpha
                    new ZipFileRule(1, 1) // check zip only has one file
            ));
            fileValidateTask.setEventService(eventService);
            manager.addTask(5000, fileValidateTask, fileReceiveTask, Result.SUCCESS);

            // unzip so we can work with the payload (xml)
            UnzipTask unzipTask = new UnzipTask(id, "UNZIP", new File(WORKDIR));
            unzipTask.setEventService(eventService);
            manager.addTask(5000, unzipTask, fileValidateTask, Result.SUCCESS);

            // process the xml file
            ShipOrderProcessorTask processTask = new ShipOrderProcessorTask(id, "XMLProcessor");
            processTask.setEmailNotificationFactory(emailNotificationFactory);
            processTask.setEventService(eventService);
            manager.addTask(5000, processTask, unzipTask, Result.SUCCESS);
        }

        copyFile(getFile(this, SHIP_ORDER_ZIP), new File(BASEDIR, partners[0]));
        copyFile(getFile(this, SHIP_ORDER_INVALID_XML_ZIP), new File(BASEDIR, partners[0]));
//        copyFile(getFile(this,EMPTY_ZIP), new File(BASEDIR, partners[0]));
//        copyFile(getFile(this,INVALID_NAME), new File(BASEDIR, partners[0]));
//        copyFile(getFile(this,SHIP_ORDER_XML), new File(BASEDIR, partners[0]));

        Thread.sleep(30000);

        manager.shutdown();
    }

    private static void copyFile(File from, File to) throws Exception {
        FileUtils.copyFileToDirectory(from, to);
        Thread.sleep(1000);
    }

    public static File getFile(Object obj, String filename) {
        URL url = obj.getClass().getClassLoader().getResource(filename);
        return new File(url.getFile());
    }

}
