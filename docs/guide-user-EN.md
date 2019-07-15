---
layout: global
title: User Guide
---

### User Interface

"HTTP server" and "TCP server" are built in Moonbox, access modes supported including rest API, shell and JDBC.  
- Rest API is used to submit batch jobs async, query job state and cancel jobs.  
- Shell is used for Ad-Hoc interactive query.  
- JDBC should be connected to programs for query, so it can be connected with Zeppelin and Davinci. 

#### Shell
```
cd $MOONBOX_HOME/bin
./moonbox-shell -u username -p password [-h localhost -P 18090 -r local]
```
Wherein,   

- `-u`, referring to user name, is a required field.
- `-p`, referring to password, is a required field.
- `-h` refers to the address of MoonboxMaster. If the client machine belongs to a cluster, `-h` is not a required field.
- `-P` refers to Moonbox Tcp Server port. If the client machine belongs to a cluster, `-P` is not a required field.
- `-r` is optional. If its value is "local", it means Moonbox client is connected to Spark Local APP.

#### Rest API

Submit batch jobs.  
```
curl -XPOST http://host:port/batch/submit -d '{
    "username" : "",
    "password" : "",
    "config" : {},
    "lang" : "mql"
    "sqls" : ["use default", "insert into ..."]
}'
```

Query job state.  
```
curl -XPOST http://host:port/batch/progress -d '{
    "username" : "",
    "password" : "",
    "jobId" : ""
}'
```

Cancel jobs.  
```
curl -XPOST http://host:port/batch/cancel -d '{
    "username" : "",
    "password" : "",
    "jobId" : ""
}'
```

#### JDBC
Moonbox provides JDBC driver and you can download at will. We'd like to take Scala as an example:  
```
Class.forName("moonbox.jdbc.MbDriver")
val url = s"jdbc:moonbox://host:port/database"
val username = "username"
val password = "password"
val connection = DriverManager.getConnection(url, username, password)
val statement = connection.createStatement()
statement.setQueryTimeout(60*10)
statement.setFetchSize(200)
val rs = statement.executeQuery("select * from mysql_test")
while (rs.next) {
    println(rs.getString(1))
}
```

### Introduction of Instructions

#### ROOT
ROOT is regarded as system administrator; it is recommended that ROOT just create and manage "Organization" and "SA (Super Admin)". The instructions that ROOT can execute include:    
```
# alter the user name of ROOT  
ALTER USER root IDENTIFIED BY newPassword

# create Organization
CREATE ORG orgname
# rename Organization
RENAME ORG orgname TO newname
ALTER ORG orgname RENAME TO newname
# delete Organization
DROP ORG orgname

# create SA in Organization  
CREATE SA saname IN ORG orgname
# alter the password of SA  
RENAME SA saname IN ORG orgname TO newpassword
ALTER SA saname IN ORG orgname RENAME TO newpassword
# delete SA
DROP SA saname IN ORG orgname
```

#### SA (Super Admin)

We have introduced the six attributes of User in the "Basic Concept" section: if User has one of the attributes, it has the permission to execute corresponding instruction. Created by ROOT, SA has all the permissions. Following are some special instructions that SA executes, and we will introduce common instructions in the User part.  
```
# SA has right to give some permissions to User, like role-based authorization. For the sake of simplicity, SA is the only one to execute the following instructions at present.  
GRANT GRANT OPTION ACCOUNT, DDL, DCL TO USER username
# Cancel User authorization. 
REVOKE GRANT OPTION ACCOUNT, DDL, DCL FROM USER username
```

#### SA and User  
We are going to introduce instructions according to User's permissions.  
- DML

    All Users can perform DML operations by fault and execute instructions like SHOW, DESC, SELECT and INSERT.  
    
    Show Users.  
    ```
    SHOW USERS
    SHOW USERS like '%'
    ```

    Show databases.  
    ```
    SHOW DATABASES
    SHOW DATABASES like '%'
    ```

    Show tables.  
    ```
    SHOW TABLES
    SHOW TABLES IN db
    SHOW TABLES IN db like '%'
    ```

    Show functions.  
    ```
    SHOW FUNTIONS
    SHOW SYSTEM FUNCTIONS like '%'
    SHOW USER FUNCTIONS like '%'
    ```

    Show procedures.  
    ```
    SHOW PROCS
    SHOW PROCS like '%'
    ```

    Show events.  
    ```
    SHOW EVENTS
    SHOW EVENTS like '%'
    ```

    Show creation mode of tables.  
    ```
    SHOW CREATE TABLE tbname
    ```

    Show schema for SQL result.  
    ```
    SHOW SCHEMA FOR SELECT ...
    ```

    Describe database information.  
    ```
    DESC DATABASE dbname
    ```

    Describe table structure.  
    ```
    DESC TABLE tbname
    ```

    Describe User information.  
    ```
    DESC USER username
    ```

    Describe function information.  
    ```
    DESC FUNTION funcname
    ```

    Describe event information.  
    ```
    DESC EVENT eventname
    ```

    Switch database.  
    ```
    USER dbname
    ```

    Query the returning results.  
    ```
    SELECT ...
    ```

    Create view.
    ```
    CREATE TEMP VIEW viewname AS SELECT ...
    ```

    Insert the returning results into storage.
    ```
    INSERT INTO/OVERWRITE tbname AS SELECT ...
    ```

- Account

    User with Account permission can execute instructions related to account.
    ```
    CREATE USER username IDENTIFIED BY password # Create User  
    RENAME USER username TO newname # Alter User name  
    ALTER USER username RENAME TO newname # Alter User name  
    ALTER USER username IDENTIFIED BY newpassword
    DROP USER username
    ```
- DDL

    After system initialization, just a TYPE 1 database named default exists in Organization. User with DDL permission can perform the following operations:  
    
    Create and delete TYPE 1 database.  
    ```
    CREATE DATABASE dbname
    DROP DATABASE dbname
    ```
    Mount/unmount virtual table in TYPE 1 database.  
    ```
    USE dbname
    MOUNT TABLE tbname OPTIONS(key 'value', key 'value')
    RENAME TABLE tbname TO newname
    ALTER TABLE tbname RENAME TO newname
    ALTER TABLE tbname SET OPTIONS(key 'newvalue')
    UNMOUT TABLE tbname
    ```
    Mount/unmount TYPE 2 database.  
    ```
    MOUNT DATABASE dbname OPTIONS(key 'value', key 'value')
    RENAME DATABASE dbname TO newname
    ALTER DATABASE dbname RENAME TO newname
    ALTER DATABASE dbname SET OPTIONS(key 'newvalue')
    UNMOUNT DATABASE dbname
    ```
    Create, alter or delete procedure. Procedure is the encapsulation for SQL and used for event.  
    ```
    CREATE PROC procname USING MQL AS (USE default; INSERT INTO oracle_external AS SELECT * FROM mysql_test)
    RENAME PROC procname TO newname
    ALTER PROC procname RENAME TO newname
    ALTER PROC procname AS (USE default; INSERT INTO oracle_external AS SELECT * FROM mysql_test LIMIT 100)
    DROP PROC procname
    ```
    Create, alter, start, stop and delete event. Event should be connected to a procedure and the executed event is procedure. Crontab schedule expression is used here.  
    ```
    CREATE EVENT eventname ON SCHEDULE AT '0/50 * * * * ?' DO CALL procname
    RENAME EVENT eventname TO newname
    ALTER EVENT eventname RENAME TO newname
    ALTER EVENT eventname ON SCHEDULE AT '0/40 * * * * ?'
    ALTER EVENT eventname ENABLE
    ALTER EVENT eventname DISABLE
    DROP EVENT eventname
    ```
    Create and delete function. Moonbox supports UDF created by not only Jar but also source code (Java and Scala). You can also write multiple functions into a class with specified function name when necessary.  
    ```
    # Create function with Scala source code  
    CREATE FUNCTION funcname AS 'PersonData' 'mutiply1' USING scala '(
        class PersonData {
            val field = 16
            def mutiply1(i: Int): Int = i * field
        }
    )'
    # Create function using jar, with multiple functions in a class  
    CREATE FUNCTION funcname AS 'zoo.Panda' 'multiply' USING jar 'hdfs://host:8020/tmp/tortoise-1.0-SNAPSHOT.jar'
    # Create function using jar, integrating with Scala FunctionN interface or Java UDFN interface  
    CREATE FUNCTION funcname AS 'zoo.Single' USING jar 'hdfs://host:8020/tmp/single-1.0-SNAPSHOT.jar'
    DROP FUNCITON funcname
    ```
- DCL

    User with DCL permission can execute instructions that authorize users to access some sources.  
    ```
    GRANT SELECT ON dbname.* TO USER username
    REVOKE SELECT ON dbname.* FROM USER username
    GRANT SELECT ON dbname.tbname TO USER username
    REVOKE SELECT ON dbname.tbname FROM USER username
    GRANT SELECT(col1,col2...) ON dbname.tbname TO USER username
    REVOKE SELECT(col1) ON dbname.tbname FROM USER username
    GRANT UPDATE ON dbname.* TO USER username
    REVOKE UPDATE ON dbname.* FROM USER username
    GRANT UPDATE ON dbname.tbname TO USER username
    REVOKE UPDATE ON dbname.tbname FROM USER username
    GRANT UPDATE(col1,col2...) ON dbname.tbname TO USER username
    REVOKE UPDATE(col1) ON dbname.tbname FROM USER username
    ```

- GrantAccount

    User with GrantAccount permission can execute instructions that grant Account permission to other users.  
    ```
    GRANT ACCOUNT TO USER username
    REVOKE ACCOUNT FROM USER username
    ```

- GrantDDL

    User with GrantDDL permission can execute instructions that grant DDL permission to other users.  
    ```
    GRANT DDL TO USER username
    REVOKE DDL FROM USER username
    ```

- GrantDCL

    User with GrantDCL permission can execute instructions that grant DCL permission to other users.  
    ```
    GRANT DCL TO USER username
    REVOKE DCL FROM USER username
    ```
    It seems that above-mentioned attributes are complicated. However, we can classify the six attributes. ACCOUNT, DDL and DCL can be regarded as the first-order attributes, GrantAccount, GrantDDL and GrantDCL the second-order. The second-order attributes take charge of the grant and revocation of the first-order attributes, while SA takes charge of the grant and revocation of the second-order attributes.
