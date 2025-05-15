```
-- insert into
INSERT INTO dept (
    id,
    name,
    region_id
)
    SELECT
        id + 100,
        substr(
            name, 0, 4
        ),
        region_id
    FROM
        dept;

-- update
UPDATE item
SET
    price = price * 1.10;

-- delete
DELETE FROM emp
WHERE
    id > 25;


ROLLBACK;
-- Zmiany poprawnie wycofano

-- 
-- tworzenie tabeli
CREATE TABLE region_kopia (
    id   NUMBER(7),
    name VARCHAR2(50)
);

INSERT INTO region_kopia (
    id,
    name
)
    SELECT
        id,
        name
    FROM
        region;

-- drop table
DROP TABLE regiony CASCADE CONSTRAINTS;

SELECT table_name FROM user_tables;

-- Alternatywnie skrypt PL/SQL
BEGIN
   FOR i IN (SELECT table_name FROM user_tables) LOOP
      EXECUTE IMMEDIATE 'DROP TABLE ' || i.table_name || ' CASCADE CONSTRAINTS';
   END LOOP;
END;

-- przykład exception
SET SERVEROUTPUT ON;

DECLARE
    var_first_name VARCHAR2(25);
    var_last_name  VARCHAR2(25);
    var_min_salary NUMBER;
    var_max_salary NUMBER;
BEGIN
    SELECT
        MIN(salary),
        MAX(salary)
    INTO
        var_min_salary,
        var_max_salary
    FROM
        emp;

    -- Pensja MAX
    BEGIN
        SELECT
            first_name,
            last_name
        INTO
            var_first_name,
            var_last_name
        FROM
            emp
        WHERE
            salary = var_max_salary;

        dbms_output.put_line('Największa pensja: '
                             || var_max_salary
                             || ', '
                             || var_first_name
                             || ' '
                             || var_last_name);

    EXCEPTION
        WHEN no_data_found THEN
            dbms_output.put_line('Nie znaleziono pracownika z największą pensją.');
        WHEN too_many_rows THEN
            dbms_output.put_line('Znaleziono więcej niż jednego pracownika z największą pensją.');
    END;

-- KLUCZ OBCY
Dodanie nowej tabeli i wprowadzenie klucza obcego do istniejącej tabeli

-- Sposób 1: ALTER TABLE
CREATE TABLE degrees (
    academic_degree VARCHAR2(25)
        CONSTRAINT degrees_academic_degree_nn NOT NULL,
    CONSTRAINT degrees_academic_degree_pk PRIMARY KEY ( academic_degree )
);

ALTER TABLE teachers ADD academic_degree VARCHAR2(25);

ALTER TABLE teachers
    ADD CONSTRAINT teachers_degree_fk FOREIGN KEY ( academic_degree )
        REFERENCES degrees ( academic_degree );
        
-- Sposób 2: Ograniczenie kolumnowe
CREATE TABLE degrees (
    academic_degree VARCHAR2(25)
        CONSTRAINT degrees_academic_degree_nn NOT NULL,
    CONSTRAINT degrees_academic_degree_pk PRIMARY KEY ( academic_degree )
);

CREATE TABLE teachers (
    teacher_id      NUMBER(6) NOT NULL,
    first_name      NUMBER(9) NOT NULL,
    last_name       NUMBER(9) NOT NULL,
    email           VARCHAR2(100),
    phone_number    VARCHAR2(15),
    hire_date       DATE NOT NULL,
    academic_degree VARCHAR2(25)
        CONSTRAINT teachers_degree_fk
            REFERENCES degrees ( academic_degree )
);
        
-- Sposób 3: Ograniczenie tablicowe
CREATE TABLE degrees (
    academic_degree VARCHAR2(25)
        CONSTRAINT degrees_academic_degree_nn NOT NULL,
    CONSTRAINT degrees_academic_degree_pk PRIMARY KEY ( academic_degree )
);

CREATE TABLE teachers (
    teacher_id   NUMBER(6) NOT NULL,
    first_name   NUMBER(9) NOT NULL,
    last_name    NUMBER(9) NOT NULL,
    email        VARCHAR2(100),
    phone_number VARCHAR2(15),
    hire_date    DATE NOT NULL,
    academic_degree VARCHAR2(25)
        CONSTRAINT teachers_degree_fk
        FOREIGN KEY (academic_degree)
        REFERENCES degrees (academic_degree),
);

-- constant
SET SERVEROUTPUT ON;

DECLARE
    var_number   NUMBER := 100;
    var_varchar2 VARCHAR2(50) := 'Hello World!';
    var_date     CONSTANT DATE := TO_DATE ( '01-01-1970', 'DD-MM-YYYY' );
BEGIN
    dbms_output.put_line('Liczba: ' || var_number);
    dbms_output.put_line('Tekst: ' || var_varchar2);
    dbms_output.put_line('Data: ' || var_date);
END;
/

-- Kursor niejawny
-- SQL%FOUND - true jeśli jest, false jeśli nie ma
-- SQL%NOTFOUND - true jeśli nie ma, false jeśli jest
-- SQL%ROWCOUNT - liczba wierszy zmodyfikowanych przez ostatnią operację DML
DECLARE  
   total_rows number(2); 
BEGIN 
   UPDATE customers 
   SET salary = salary + 500; 
   IF sql%notfound THEN 
      dbms_output.put_line('no customers selected'); 
   ELSIF sql%found THEN 
      total_rows := sql%rowcount;
      dbms_output.put_line( total_rows || ' customers selected '); 
   END IF;  
END; 
/      

-- Kursor jawny
DECLARE 
   c_id customers.id%type; 
   c_name customers.name%type; 
   c_addr customers.address%type; 
   CURSOR c_customers is 
      SELECT id, name, address FROM customers; 
BEGIN 
   OPEN c_customers; 
   LOOP 
   FETCH c_customers into c_id, c_name, c_addr; 
      EXIT WHEN c_customers%notfound; 
      dbms_output.put_line(c_id || ' ' || c_name || ' ' || c_addr); 
   END LOOP; 
   CLOSE c_customers; 
END; 
/

-- IF-THEN-ELSIF
DECLARE 
   a number(3) := 100; 
BEGIN 
   IF ( a = 10 ) THEN 
      dbms_output.put_line('Value of a is 10' ); 
   ELSIF ( a = 20 ) THEN 
      dbms_output.put_line('Value of a is 20' ); 
   ELSIF ( a = 30 ) THEN 
      dbms_output.put_line('Value of a is 30' ); 
   ELSE 
       dbms_output.put_line('None of the values is matching'); 
   END IF; 
   dbms_output.put_line('Exact value of a is: '|| a );  
END; 
/ 


-- exit loops
DECLARE 
   a number(2) := 10; 
BEGIN 
   -- while loop execution  
   WHILE a < 20 LOOP 
      dbms_output.put_line ('value of a: ' || a); 
      a := a + 1; 
      IF a > 15 THEN 
         -- terminate the loop using the exit statement 
         EXIT; 
      END IF; 
   END LOOP; 
END; 
/ 

DECLARE 
   a number(2) := 10; 
BEGIN 
   -- while loop execution  
   WHILE a < 20 LOOP 
      dbms_output.put_line ('value of a: ' || a);  
      a := a + 1; 
      -- terminate the loop using the exit when statement 
   EXIT WHEN a > 15; 
   END LOOP; 
END;   
/


-- For loop
DECLARE 
   a number(2); 
BEGIN 
   FOR a in 10 .. 20 LOOP 
      dbms_output.put_line('value of a: ' || a); 
  END LOOP; 
END; 
/

-- Reverse for loop
DECLARE 
   a number(2) ; 
BEGIN 
   FOR a IN REVERSE 10 .. 20 LOOP 
      dbms_output.put_line('value of a: ' || a); 
   END LOOP; 
END; 
/

-- Step 
DECLARE
  l_step  PLS_INTEGER := 2;
BEGIN
  FOR l_counter IN 1..5 LOOP
    dbms_output.put_line (l_counter*l_step);
  END LOOP;
END;

-- funkcja
CREATE OR REPLACE FUNCTION totalCustomers 
RETURN number IS 
   total number(2) := 0; 
BEGIN 
   SELECT count(*) into total 
   FROM customers; 
    
   RETURN total; 
END; 
/ 

DECLARE 
   c number(2); 
BEGIN 
   c := totalCustomers(); 
   dbms_output.put_line('Total no. of Customers: ' || c); 
END; 
/

-- procedura
CREATE OR REPLACE PROCEDURE print_contact(
    in_customer_id NUMBER 
)
IS
  r_contact contacts%ROWTYPE;
BEGIN
  -- get contact based on customer id
  SELECT *
  INTO r_contact
  FROM contacts
  WHERE customer_id = p_customer_id;

  -- print out contact's information
  dbms_output.put_line( r_contact.first_name || ' ' ||
  r_contact.last_name || '<' || r_contact.email ||'>' );

EXCEPTION
   WHEN OTHERS THEN
      dbms_output.put_line( SQLERRM );
END;

EXEC print_contact(100);

--- package
CREATE OR REPLACE PACKAGE pracownicy AS
    PROCEDURE dodaj_emp (
      in_last_name   IN emp.last_name%TYPE,
      in_first_name  IN emp.first_name%TYPE,
      in_userid      IN emp.userid%TYPE,
      in_start_date  IN emp.start_date%TYPE,
      in_comments    IN emp.comments%TYPE,
      in_manager_id  IN emp.manager_id%TYPE,
      in_title       IN emp.title%TYPE,
      in_dept_id     IN emp.dept_id%TYPE,
      in_salary      IN emp.salary%TYPE,
      in_commission_pct IN emp.commission_pct%TYPE
    );

    PROCEDURE zmien_emp (
        in_id             IN NUMBER DEFAULT NULL,
        in_last_name      IN VARCHAR2,
        in_first_name     IN VARCHAR2,
        in_userid         IN VARCHAR2 DEFAULT NULL,
        in_start_date     IN DATE DEFAULT NULL,
        in_comments       IN VARCHAR2 DEFAULT NULL,
        in_manager_id     IN NUMBER DEFAULT NULL,
        in_title          IN VARCHAR2 DEFAULT NULL,
        in_dept_id        IN NUMBER DEFAULT NULL,
        in_salary         IN NUMBER DEFAULT NULL,
        in_commission_pct IN NUMBER DEFAULT NULL
    );

    PROCEDURE kasuj_emp (
        in_id IN NUMBER DEFAULT NULL
    );

    PROCEDURE zmiana_salary (
        in_id      IN NUMBER DEFAULT NULL,
        in_percent IN NUMBER
    );

    PROCEDURE top_n_emp (
        in_n IN NUMBER
    );

    PROCEDURE zmiana_dept (
        in_id      IN NUMBER DEFAULT NULL,
        in_dept_id IN NUMBER
    );

    FUNCTION stat_emp(
        in_stat IN VARCHAR2
    ) RETURN NUMBER;
END pracownicy;
/

CREATE OR REPLACE PACKAGE BODY pracownicy AS
    PROCEDURE dodaj_emp (
      in_last_name   IN emp.last_name%TYPE,
      in_first_name  IN emp.first_name%TYPE,
      in_userid      IN emp.userid%TYPE,
      in_start_date  IN emp.start_date%TYPE,
      in_comments    IN emp.comments%TYPE,
      in_manager_id  IN emp.manager_id%TYPE,
      in_title       IN emp.title%TYPE,
      in_dept_id     IN emp.dept_id%TYPE,
      in_salary      IN emp.salary%TYPE,
      in_commission_pct IN emp.commission_pct%TYPE
    ) AS
        uv_max_id emp.id%TYPE;
    BEGIN
        SELECT MAX(id) INTO uv_max_id FROM emp;

        INSERT INTO emp VALUES (
            uv_max_id + 1,
            in_last_name,
            in_first_name,
            in_userid,
            in_start_date,
            in_comments,
            in_manager_id,
            in_title,
            in_dept_id,
            in_salary,
            in_commission_pct
        );

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM);
    END dodaj_emp;

    PROCEDURE zmien_emp (
        in_id             IN NUMBER DEFAULT NULL,
        in_last_name      IN VARCHAR2,
        in_first_name     IN VARCHAR2,
        in_userid         IN VARCHAR2 DEFAULT NULL,
        in_start_date     IN DATE DEFAULT NULL,
        in_comments       IN VARCHAR2 DEFAULT NULL,
        in_manager_id     IN NUMBER DEFAULT NULL,
        in_title          IN VARCHAR2 DEFAULT NULL,
        in_dept_id        IN NUMBER DEFAULT NULL,
        in_salary         IN NUMBER DEFAULT NULL,
        in_commission_pct IN NUMBER DEFAULT NULL
    ) AS
    BEGIN
        UPDATE emp
        SET
            last_name = in_last_name,
            first_name = in_first_name,
            userid = in_userid,
            start_date = in_start_date,
            comments = in_comments,
            manager_id = in_manager_id,
            title = in_title,
            dept_id = in_dept_id,
            salary = in_salary,
            commission_pct = in_commission_pct
        WHERE id = in_id;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM);
    END zmien_emp;

    PROCEDURE kasuj_emp (
        in_id IN NUMBER DEFAULT NULL
    ) AS
    BEGIN
        DELETE FROM emp WHERE id = in_id;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM);
    END kasuj_emp;

    PROCEDURE zmiana_salary (
        in_id      IN NUMBER DEFAULT NULL,
        in_percent IN NUMBER
    ) AS
        uv_new_salary NUMBER;
    BEGIN
        SELECT salary * ( ( in_percent / 100 ) + 1 )
        INTO uv_new_salary
        FROM emp
        WHERE id = in_id;

        UPDATE emp
        SET salary = uv_new_salary
        WHERE id = in_id;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM);
    END zmiana_salary;

    PROCEDURE top_n_emp (
        in_n IN NUMBER
    ) AS
        CURSOR c_emp IS
            SELECT last_name, first_name, salary
            FROM emp
            WHERE ROWNUM BETWEEN 1 AND in_n
            ORDER BY salary DESC;

        uv_emp c_emp%rowtype;
    BEGIN
        OPEN c_emp;
        DELETE FROM top_n_emp;

        LOOP
            FETCH c_emp INTO uv_emp;
            EXIT WHEN c_emp%notfound;
            INSERT INTO top_n_emp (last_name, first_name, salary)
            VALUES (uv_emp.last_name, uv_emp.first_name, uv_emp.salary);
        END LOOP;

        CLOSE c_emp;
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM);
    END top_n_emp;

    PROCEDURE zmiana_dept (
        in_id      IN NUMBER DEFAULT NULL,
        in_dept_id IN NUMBER
    ) AS
    BEGIN
        UPDATE emp
        SET dept_id = in_dept_id
        WHERE id = in_id;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM);
    END zmiana_dept;

    FUNCTION stat_emp (
        in_stat IN VARCHAR2
    ) RETURN NUMBER AS
        uv_result NUMBER;
    BEGIN
        IF UPPER(in_stat) = 'MAX' THEN
            SELECT MAX(salary) INTO uv_result FROM emp;
        ELSIF UPPER(in_stat) = 'MIN' THEN
            SELECT MIN(salary) INTO uv_result FROM emp;
        ELSIF UPPER(in_stat) = 'AVG' THEN
            SELECT AVG(salary) INTO uv_result FROM emp;
        ELSIF UPPER(in_stat) = 'SUM' THEN
            SELECT SUM(salary) INTO uv_result FROM emp;
        ELSE
            RAISE_APPLICATION_ERROR(-20555, 'Niewłaściwy parametr funkcji.');
        END IF;
        RETURN uv_result;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM);
    END stat_emp;
END pracownicy;
/

-- Testing the package functions
SET SERVEROUTPUT ON;

BEGIN
  pracownicy.dodaj_emp(
    in_last_name => 'Kowalski',
    in_first_name => 'Jan',
    in_userid => 'jkow',
    in_start_date => TO_DATE('01-01-1970', 'DD-MM-YYYY'),
    in_comments => 'Komentarz',
    in_manager_id => NULL,
    in_title => 'Sales Representative',
    in_dept_id => 31,
    in_salary => 2000,
    in_commission_pct => 10
  );
END;
/

SET SERVEROUTPUT ON;

BEGIN
  pracownicy.zmien_emp(
    in_id => 1,
    in_last_name => 'Nowak',
    in_first_name => 'Piotr',
    in_userid => 'pnowak',
    in_start_date => TO_DATE('01-01-1980', 'DD-MM-YYYY'),
    in_comments => 'Updated information',
    in_manager_id => 2,
    in_title => 'VP, Sales',
    in_dept_id => 31,
    in_salary => 3000,
    in_commission_pct => 15
  );
END;
/

SET SERVEROUTPUT ON;

BEGIN
  pracownicy.kasuj_emp(1);
END;
/

SET SERVEROUTPUT ON;

BEGIN
  pracownicy.zmiana_salary(2, 10);
END;
/

SET SERVEROUTPUT ON;

BEGIN
  pracownicy.top_n_emp(5);
END;
/

SELECT * FROM top_n_emp;

BEGIN
  pracownicy.zmiana_dept(3, 42);
END;
/

SET SERVEROUTPUT ON;

DECLARE
    uv_result NUMBER;
BEGIN
    uv_result := pracownicy.stat_emp('MAX');
    DBMS_OUTPUT.PUT_LINE('Pensja MAX: ' || uv_result);
    uv_result := pracownicy.stat_emp('MIN');
    DBMS_OUTPUT.PUT_LINE('Pensja MIN: ' || uv_result);
    uv_result := pracownicy.stat_emp('AVG');
    DBMS_OUTPUT.PUT_LINE('Pensja AVG: ' || uv_result);
    uv_result := pracownicy.stat_emp('SUM');
    DBMS_OUTPUT.PUT_LINE('Pensja SUM: ' || uv_result);
END;
/

--expetions
DECLARE 
   c_id customers.id%type := &cc_id; 
   c_name customerS.Name%type; 
   c_addr customers.address%type;  
   -- user defined exception 
   ex_invalid_id  EXCEPTION; 
BEGIN 
   IF c_id <= 0 THEN 
      RAISE ex_invalid_id; 
   ELSE 
      SELECT  name, address INTO  c_name, c_addr 
      FROM customers 
      WHERE id = c_id;
      DBMS_OUTPUT.PUT_LINE ('Name: '||  c_name);  
      DBMS_OUTPUT.PUT_LINE ('Address: ' || c_addr); 
   END IF; 

EXCEPTION 
   WHEN ex_invalid_id THEN 
      dbms_output.put_line('ID must be greater than zero!'); 
   WHEN no_data_found THEN 
      dbms_output.put_line('No such customer!'); 
   WHEN others THEN 
      dbms_output.put_line('Error!');  
END; 
/
```