package Dao.DaoImpl;

import Config.Config;
import Dao.EmployeeDao;
import model.Employee;
import model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    private final Connection connection = Config.getConnection();
    @Override
    public void createEmployee() {
     String sql = "create table if not exists employee("+
             "id serial primary key,"+
             "first_name varchar,"+
             "last_name varchar,"+
             "age int,"+
             "email varchar,"+
             "job_id int references jobs(id))";
             try(Statement statement = connection.createStatement()){
                 statement.execute(sql);
                 System.out.println("Таблица успешно создана.");
             } catch (SQLException e) {
                 throw new RuntimeException(e);
             }
    }

    @Override
    public void addEmployee(Employee employee) {
    String sql = " insert into employee(first_name,last_name,age,email,job_id)values (?,?,?,?,?)";
    try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
        preparedStatement.setString(1,employee.getFirstName());
        preparedStatement.setString(2,employee.getLastName());
        preparedStatement.setInt(3,employee.getAge());
        preparedStatement.setString(4, employee.getEmail());
        preparedStatement.setInt(5,employee.getJobId());
        preparedStatement.executeUpdate();
        System.out.println("employee saved");
    }catch (SQLException e){
        throw new RuntimeException(e);
    }
    }

    @Override
    public void dropTable() {
    String sql = "drop table if exists employee";
    try(Statement statement = connection.createStatement()){
        statement.executeUpdate(sql);
        System.out.println("table droped");
    }catch (SQLException e){
        throw new RuntimeException(e);
    }
    }

    @Override
    public void cleanTable() {
    String sql = "truncate table employee";
    try(Statement statement = connection.createStatement()){
        statement.executeUpdate(sql);
        System.out.println("table cleaned successfully");
    }catch (SQLException e){
        throw new RuntimeException(e);
    }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        String sql = "UPDATE employee SET first_name=?,last_name=?,age=?, email=?, job_id=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.setLong(6,id);
            preparedStatement.executeUpdate();
            System.out.println("Сотрудник успешно обновлен в базе данных.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee>employees = new ArrayList<>();
        String sql = "select * from employee";
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                employees.add(new Employee(resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public Employee findByEmail(String email) {
        String sql = "select * from employee where email=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String f = resultSet.getString("first_name");
                String l = resultSet.getString("last_name");
                int a = resultSet.getInt("age");
                String e = resultSet.getString("email");
                int j = resultSet.getInt("job_id");
                Employee employee = new Employee(f,l,a,e,j);
                return employee;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        String selectEmployeeJobSQL = "SELECT e.id, e.first_name,e.last_name,e.age, e.email,e.job_id, j.id, j.positionn, j.profecion, j.experience, j.description "
                + "FROM employee e "
                + "INNER JOIN jobs j ON e.job_id = j.id "
                + "WHERE e.id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectEmployeeJobSQL)) {
            preparedStatement.setLong(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            Map<Employee, Job> employeeJobMap = new HashMap<>();
            while (resultSet.next()) {
                String f = resultSet.getString("first_name");
                String l = resultSet.getString("last_name");
                int a = resultSet.getInt("age");
                String email = resultSet.getString("email");
                int j = resultSet.getInt("job_id");

                String position = resultSet.getString("positionn");
                String profecion = resultSet.getString("profecion");
                int experience = resultSet.getInt("experience");
                String description = resultSet.getString("description");

                Employee employee = new Employee(f, l, a, email, j);
                Job job = new Job(position, profecion, description, experience);

                employeeJobMap.put(employee, job);
            }
            return employeeJobMap;
        } catch (SQLException e) {
              throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        String sql = "select e.* from employee e join jobs j on e.job_id=j.id where j.positionn=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, position);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
               employees.add(new Employee(resultSet.getString("first_name"),
                 resultSet.getString("last_name"),
                 resultSet.getInt("age"),
                 resultSet.getString("email"),
                 resultSet.getInt("job_id")));
            }
            return employees;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
