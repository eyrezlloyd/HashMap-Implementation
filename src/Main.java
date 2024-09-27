import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;



class Department {
    private String deptCode;
    private String deptName;
    private String location;
    private double depTotalSalary;

    public Department(String deptCode, String deptName, String location) {
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.location = location;
        this.depTotalSalary = 0.0;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getLocation() {
        return location;
    }

    public double getDepTotalSalary() {
        return depTotalSalary;
    }

    public void setDepTotalSalary(double depTotalSalary) {
        this.depTotalSalary = depTotalSalary;
    }
}

class Employee {
    private String empNo;
    private String lastName;
    private String firstName;
    private double salary;

    public Employee(String empNo, String lastName, String firstName, double salary) {
        this.empNo = empNo;
        this.lastName = lastName;
        this.firstName = firstName;
        this.salary = salary;
    }

    public String getEmpNo() {
        return empNo;
    }

    public String getFullName() {
        return lastName + ", " + firstName;
    }

    public double getSalary() {
        return salary;
    }
}

class EmployeeDA {
    private Map<String, Department> departments;
    private Map<String, Employee> employees;

    public EmployeeDA() {
        departments = new HashMap<>();
        employees = new HashMap<>();
    }

    public void loadDepartments(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        boolean firstLineSkipped = false;
        while ((line = reader.readLine()) != null) {
            if (!firstLineSkipped) {
                firstLineSkipped = true;
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                Department department = new Department(parts[0], parts[1], parts[2]);
                departments.put(parts[0], department);
            } else {
                System.err.println("Skipping line: " + line + " - Insufficient data.");
            }
        }
        reader.close();
    }

    public void loadEmployees(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                String deptCode = parts[0].trim();
                String empNo = parts[1].trim();
                String salaryStr = parts[2].trim();
                System.out.println("DeptCode: " + deptCode + ", EmpNo: " + empNo + ", Salary: " + salaryStr);
                try {
                    double salary = Double.parseDouble(salaryStr);
                    departments.get(deptCode).setDepTotalSalary(departments.get(deptCode).getDepTotalSalary() + salary);
                    Employee employee = new Employee(empNo, null, null, salary);
                    employees.put(empNo, employee);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid salary format for line: " + line);
                }
            }
        }
        reader.close();
    }

    public void loadEmployeeDetails(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String empNo = parts[0].trim();
                String lastName = parts[1].trim();
                String firstName = parts[2].trim();
                try {
                    double salary = Double.parseDouble(parts[3].trim());
                    Employee employee = employees.get(empNo);
                    if (employee != null) {
                        employee = new Employee(empNo, lastName, firstName, salary);
                        employees.put(empNo, employee);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid salary format for line: " + line);
                }
            }
        }
        reader.close();
    }



    public void printDepartmentReport() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        for (Department department : departments.values()) {
            System.out.println("Department code: " + department.getDeptCode());
            System.out.println("Department name: " + department.getDeptName());
            System.out.printf("Department total salary: %s%n", df.format(department.getDepTotalSalary()));
            System.out.println("---------------------Details -------------------------");
            System.out.println("EmpNo\t\t Employee Name\t\tSalary");

            List<Employee> sortedEmployees = new ArrayList<>(employees.values());
            sortedEmployees.sort(Comparator.comparing(Employee::getEmpNo));

            for (Employee employee : sortedEmployees) {
                if (employee.getEmpNo().startsWith(department.getDeptCode())) {
                    System.out.printf("%s\t\t%s\t\t%s%n", employee.getEmpNo(),
                            employee.getFullName(), df.format(employee.getSalary()));
                }
            }
            System.out.println();
        }
    }

}

public class Main {
    public static void main(String[] args) {
        EmployeeDA employeeDA = new EmployeeDA();
        try {
            employeeDA.loadDepartments("C:\\Users\\mikel\\IdeaProjects\\untitled1\\src\\dep.csv");
            employeeDA.loadEmployees("C:\\Users\\mikel\\IdeaProjects\\untitled1\\src\\deptemp\\deptemp.csv");
            employeeDA.loadEmployeeDetails("C:\\Users\\mikel\\IdeaProjects\\untitled1\\src\\emp.csv");
            employeeDA.printDepartmentReport();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
