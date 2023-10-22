package Dao.DaoImpl;

import Config.Config;
import Dao.JobDao;
import model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {
    private final Connection connection = Config.getConnection();
    @Override
    public void createJobTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS jobs ("
                + "id serial PRIMARY KEY,"
                + "positionn VARCHAR(50) NOT NULL,"
                + "profecion varchar NOT NULL,"
                + "description TEXT,"
                + "experience int"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Таблица успешно создана.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addJob(Job job) {
        String insertJobSQL = "INSERT INTO jobs (positionn,profecion,description,experience) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertJobSQL)) {
            preparedStatement.setString(1, job.getPosition());
            preparedStatement.setString(2, job.getProfession());
            preparedStatement.setString(3, job.getDescription());
            preparedStatement.setInt(4,job.getExperience());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Job getJobById(Long jobId) {
        String selectJobSQL = "SELECT id, positionn, profecion,description, experience FROM jobs WHERE id = ?";
        Job job = new Job();
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectJobSQL)) {
            preparedStatement.setLong(1, jobId);
            ResultSet resultSet = preparedStatement.executeQuery();
            job.setId(resultSet.getLong("id"));
            job.setPosition(resultSet.getString("positionn"));
            job.setProfession(resultSet.getString("profecion"));
            job.setDescription(resultSet.getString("description"));
            job.setExperience(resultSet.getInt("experience"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return job;
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        String selectJobsSQL = "SELECT positionn,profecion,description,experience FROM jobs ORDER BY experience " + ascOrDesc;
        List<Job> sortedJobs = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectJobsSQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                sortedJobs.add(new Job(
                resultSet.getString("positionn"),
                resultSet.getString("profecion"),
                resultSet.getString("description"),
                resultSet.getInt("experience")));
            }
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        return sortedJobs;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        Job job = new Job();
        String selectJobSQL = "SELECT j.id, j.positionn,j.profecion, j.experience, j.description "
                + "FROM jobs j "
                + "INNER JOIN employee e ON j.id = e.job_id "
                + "WHERE e.id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectJobSQL)) {
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("positionn"));
                job.setProfession(resultSet.getString("profecion"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));
            } else {
                System.err.println("Работа для сотрудника с ID " + employeeId + " не найдена в базе данных.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return job;
    }

    @Override
    public void deleteDescriptionColumn() {
        String alterTableSQL = "ALTER TABLE jobs DROP COLUMN description";
        try (Statement statement = connection.createStatement()) {
            statement.execute(alterTableSQL);
            System.out.println("Столбец 'description' удален из таблицы");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка при удалении столбца 'description': " + e.getMessage());
        }
    }
}
