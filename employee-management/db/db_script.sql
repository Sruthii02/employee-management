-- Drop tables if they exist
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;

-- ----------------------------
-- Table structure for Department
-- ----------------------------
CREATE TABLE department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creation_date DATE,
    department_head_id BIGINT
);

-- ----------------------------
-- Table structure for Employee
-- ----------------------------
CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    salary DOUBLE,
    address VARCHAR(255),
    role VARCHAR(100),
    joining_date DATE,
    yearly_bonus DOUBLE,
    department_id BIGINT,
    reporting_manager_id BIGINT,
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES department(id),
    CONSTRAINT fk_reporting_manager FOREIGN KEY (reporting_manager_id) REFERENCES employee(id)
);

-- ----------------------------
-- Insert sample departments
-- ----------------------------
INSERT INTO department (name, creation_date) VALUES
('HR', '2024-01-01'),
('Finance', '2024-02-01'),
('IT', '2024-03-01');

-- ----------------------------
-- Insert sample employees
-- ----------------------------
-- Top-level manager (no reporting manager)
INSERT INTO employee (name, date_of_birth, salary, address, role, joining_date, yearly_bonus, department_id, reporting_manager_id)
VALUES ('Alice', '1985-01-10', 90000, '123 Main St', 'CEO', '2024-01-01', 15, 1, NULL);

-- HR Department
INSERT INTO employee (name, date_of_birth, salary, address, role, joining_date, yearly_bonus, department_id, reporting_manager_id)
VALUES 
('Bob', '1990-02-15', 50000, '234 Elm St', 'HR Manager', '2024-01-10', 10, 1, 1),
('Carol', '1992-03-20', 40000, '345 Oak St', 'HR Executive', '2024-01-15', 5, 1, 2),
('David', '1991-04-25', 42000, '456 Pine St', 'HR Executive', '2024-01-18', 6, 1, 2),
('Eve', '1993-05-30', 43000, '567 Cedar St', 'HR Executive', '2024-01-20', 7, 1, 2),
('Frank', '1994-06-05', 41000, '678 Spruce St', 'HR Executive', '2024-01-22', 5, 1, 2),
('Grace', '1995-07-10', 44000, '789 Birch St', 'HR Executive', '2024-01-25', 6, 1, 2),
('Heidi', '1996-08-15', 45000, '890 Maple St', 'HR Executive', '2024-01-28', 7, 1, 2),
('Ivan', '1997-09-20', 42000, '901 Walnut St', 'HR Executive', '2024-01-30', 5, 1, 2);

-- Finance Department
INSERT INTO employee (name, date_of_birth, salary, address, role, joining_date, yearly_bonus, department_id, reporting_manager_id)
VALUES 
('Judy', '1988-02-10', 60000, '1010 Elm St', 'Finance Manager', '2024-02-05', 10, 2, 1),
('Ken', '1990-03-15', 48000, '1111 Oak St', 'Finance Executive', '2024-02-10', 5, 2, 9),
('Leo', '1991-04-20', 47000, '1212 Pine St', 'Finance Executive', '2024-02-12', 5, 2, 9),
('Mona', '1992-05-25', 49000, '1313 Cedar St', 'Finance Executive', '2024-02-15', 6, 2, 9),
('Nina', '1993-06-30', 50000, '1414 Spruce St', 'Finance Executive', '2024-02-18', 7, 2, 9),
('Oscar', '1994-07-05', 48000, '1515 Birch St', 'Finance Executive', '2024-02-20', 5, 2, 9),
('Peggy', '1995-08-10', 47000, '1616 Maple St', 'Finance Executive', '2024-02-22', 5, 2, 9),
('Quentin', '1996-09-15', 49000, '1717 Walnut St', 'Finance Executive', '2024-02-25', 6, 2, 9);

-- IT Department
INSERT INTO employee (name, date_of_birth, salary, address, role, joining_date, yearly_bonus, department_id, reporting_manager_id)
VALUES 
('Rita', '1989-03-10', 70000, '1818 Main St', 'IT Manager', '2024-03-05', 12, 3, 1),
('Steve', '1990-04-15', 52000, '1919 Elm St', 'Developer', '2024-03-10', 6, 3, 17),
('Trudy', '1991-05-20', 53000, '2020 Oak St', 'Developer', '2024-03-12', 6, 3, 17),
('Uma', '1992-06-25', 54000, '2121 Pine St', 'Developer', '2024-03-15', 7, 3, 17),
('Victor', '1993-07-30', 55000, '2222 Cedar St', 'Developer', '2024-03-18', 7, 3, 17),
('Wendy', '1994-08-05', 56000, '2323 Spruce St', 'Developer', '2024-03-20', 7, 3, 17);
