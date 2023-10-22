import Service.EmployeeService;
import Service.JobService;
import Service.ServiceImpl.EmployeeServImpl;
import Service.ServiceImpl.JobServImpl;
import model.Employee;
import model.Job;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServImpl();
        JobService jobService = new JobServImpl();
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("Меню:");
            System.out.println("1. Создать сотрудника");
            System.out.println("2. Добавить сотрудника");
            System.out.println("3. Удалить таблицу сотрудников");
            System.out.println("4. Очистить таблицу сотрудников");
            System.out.println("5. Обновить сотрудника");
            System.out.println("6. Получить всех сотрудников");
            System.out.println("7. Найти сотрудника по email");
            System.out.println("8. Получить сотрудника и его работу по ID");
            System.out.println("9. Получить сотрудников по должности");
            System.out.println("10. Создать таблицу работ");
            System.out.println("11. Добавить работу");
            System.out.println("12. Получить работу по ID");
            System.out.println("13. Сортировать работы по опыту");
            System.out.println("14. Получить работу по ID сотрудника");
            System.out.println("15. Удалить столбец с описанием в таблице работ");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    employeeService.createEmployee();
                    break;
                case 2:
                    employeeService.addEmployee(new Employee("Ainazik","Toktomamatova",20,"a@gmail.com",1));
                    break;
                case 3:
                     employeeService.dropTable();
                    break;
                case 4:
                     employeeService.cleanTable();
                    break;
                case 5:
                    employeeService.updateEmployee(1L,new Employee("Datka","M",22,"d@gmail.com",1));
                    break;
                case 6:
                    System.out.println(employeeService.getAllEmployees());
                    break;
                case 7:
                    System.out.print("Введите email: ");
                    String email = scanner.next();
                    System.out.println(employeeService.findByEmail(email));
                    break;
                case 8:
                    System.out.println(employeeService.getEmployeeById(2L));
                    break;
                case 9:
                    System.out.print("Введите должность: ");
                    String position = scanner.next();
                    System.out.println(employeeService.getEmployeeByPosition(position));
                    break;
                case 10:
                    jobService.createJobTable();
                    break;
                case 11:
                    jobService.addJob(new Job("Instructor", "Java", "BackEnd", 2));
                    break;
                case 12:
                    System.out.println("Enter the job id");
                    Long jobId = scanner.nextLong();
                    System.out.println(jobService.getJobById(jobId));
                    break;
                case 13:
                    System.out.print("Введите 'asc' или 'desc' для сортировки: ");
                    String ascOrDesc = scanner.next();
                    System.out.println(jobService.sortByExperience(ascOrDesc));
                    break;
                case 14:
                    System.out.println(jobService.getJobByEmployeeId(1L));
                    break;
                case 15:
                    jobService.deleteDescriptionColumn();
                    break;
            }
        }
    }
}
