package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        int interval = getInterval();
        try {
            List<Long> store = new ArrayList<>();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", getConnection());

            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();

            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();

            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();

            scheduler.scheduleJob(job, trigger);

            Thread.sleep(interval);
            scheduler.shutdown();
            System.out.println(store);
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    /**
     * Метод возвращает время интервало в секундах
     * полученно из файла свойств rabbit.properties.
     *
     * @return int время интервала в секундах
     */
    private static int getInterval() {
        int interval = 1;
        try (InputStream inputStream =
                     AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(inputStream);
            interval = Integer.parseInt(config.getProperty("rabbit.interval"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interval;
    }

    public static Connection getConnection() {
        Connection connection;
        try (InputStream in = AlertRabbit.class.
                getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
            try (Statement statement = connection.createStatement()) {
                String sql = "create table if not exists "
                                + "rabbit("
                                + "id serial primary key,"
                                + "created_date timestamp NOT NULL"
                                + ");";
                statement.execute(sql);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return connection;
    }

    /**
     * Объект (класс) с типом org.quartz.Job
     * Внутри класса содержаться требуемые действия.
     */
    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            System.out.println("Rabbit runs here ...");
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into rabbit (created_date) values (?)")) {
                statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}