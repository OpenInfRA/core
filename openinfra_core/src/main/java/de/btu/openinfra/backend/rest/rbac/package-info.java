/**
 * This package contains REST interfaces for role-based access control (rbac).
 * <a href="https://en.wikipedia.org/wiki/Role-based_access_control"/>
 * 
 * In order to test the RBAC API you could use curl like so:
 * 
 * -- Login
 * curl -i -c cookie.txt -d "username=root" -d "password=root" -X POST http://localhost:8080/openinfra_core/login.jsp
 * 
 * -- Subjects
 * curl -i -b cookie.txt -X POST -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\Subject.xml http://localhost:8080/openinfra_core/rest/v1/rbac/subjects
 * curl -i -b cookie.txt -X PUT -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\Subject.xml http://localhost:8080/openinfra_core/rest/v1/rbac/subjects/d2d0c86d-750e-438f-8f54-60699d30a29c
 * curl -i -b cookie.txt -X DELETE http://localhost:8080/openinfra_core/rest/v1/rbac/subjects/8d5860f3-0a06-4e71-9bfe-b8e3e7eb1cba
 * 
 * -- Passwordblacklist
 * curl -i -b cookie.txt -X POST -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\Passwordblacklist.xml http://localhost:8080/openinfra_core/rest/v1/rbac/passwordblacklist
 * curl -i -b cookie.txt -X PUT -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\Passwordblacklist.xml http://localhost:8080/openinfra_core/rest/v1/rbac/passwordblacklist/704de597-6688-4d94-89fc-b2ba71114cd8
 * curl -i -b cookie.txt -X DELETE http://localhost:8080/openinfra_core/rest/v1/rbac/passwordblacklist/8d5860f3-0a06-4e71-9bfe-b8e3e7eb1cba
 * 
 * -- Permission
 * curl -i -b cookie.txt -X POST -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\Permission.xml http://localhost:8080/openinfra_core/rest/v1/rbac/permissions
 * curl -i -b cookie.txt -X PUT -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\Permission.xml http://localhost:8080/openinfra_core/rest/v1/rbac/permissions/fc41683a-ac78-46ce-8c2f-d54778093091
 * curl -i -b cookie.txt -X DELETE http://localhost:8080/openinfra_core/rest/v1/rbac/permissions/fc41683a-ac78-46ce-8c2f-d54778093091
 * 
 * -- SubjectObjects
 * curl -i -b cookie.txt -X POST -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\SubjectObject.xml http://localhost:8080/openinfra_core/rest/v1/rbac/subjectobjects
 * curl -i -b cookie.txt -X PUT -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\SubjectObject.xml http://localhost:8080/openinfra_core/rest/v1/rbac/subjectobjects/70045f6c-95e8-43cc-a6d1-070a3ecf6054
 * curl -i -b cookie.txt -X DELETE http://localhost:8080/openinfra_core/rest/v1/rbac/subjectobjects/70045f6c-95e8-43cc-a6d1-070a3ecf6054
 * 
 * -- SubjectProjects
 * curl -i -b cookie.txt -X POST -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\SubjectProject.xml http://localhost:8080/openinfra_core/rest/v1/rbac/subjectprojects
 * curl -i -b cookie.txt -X PUT -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\SubjectProject.xml http://localhost:8080/openinfra_core/rest/v1/rbac/subjectprojects/31150304-cf44-43a4-813b-d0e1c95a0b09
 * curl -i -b cookie.txt -X DELETE http://localhost:8080/openinfra_core/rest/v1/rbac/subjectprojects/31150304-cf44-43a4-813b-d0e1c95a0b09
 *
 * -- SubjectProjects
 * curl -i -b cookie.txt -X POST -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\SubjectRole.xml http://localhost:8080/openinfra_core/rest/v1/rbac/subjectroles
 * curl -i -b cookie.txt -X PUT -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\SubjectRole.xml http://localhost:8080/openinfra_core/rest/v1/rbac/subjectroles/cd727a05-8100-44e9-8723-1a36bed8338a
 * curl -i -b cookie.txt -X DELETE http://localhost:8080/openinfra_core/rest/v1/rbac/subjectroles/cd727a05-8100-44e9-8723-1a36bed8338a
 *
 * -- RolePermission
 * curl -i -b cookie.txt -X POST -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\RolePermission.xml http://localhost:8080/openinfra_core/rest/v1/rbac/rolepermissions
 * curl -i -b cookie.txt -X PUT -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\RolePermission.xml http://localhost:8080/openinfra_core/rest/v1/rbac/rolepermissions/f6f0cfc3-59d3-4467-8e8e-3e6396a1090b
 * curl -i -b cookie.txt -X DELETE http://localhost:8080/openinfra_core/rest/v1/rbac/rolepermissions/f6f0cfc3-59d3-4467-8e8e-3e6396a1090b
 * 
 * -- RolePermission
 * curl -i -b cookie.txt -X POST -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\Role.xml http://localhost:8080/openinfra_core/rest/v1/rbac/roles
 * curl -i -b cookie.txt -X PUT -H "Content-Type: application/xml" -d @C:\Users\tino\git\core\openinfra_core\src\test\java\de\btu\openinfra\backend\rest\project\test\xml\Role.xml http://localhost:8080/openinfra_core/rest/v1/rbac/roles/93d94e22-17df-4743-b002-ca0ac5c769b0
 * curl -i -b cookie.txt -X DELETE http://localhost:8080/openinfra_core/rest/v1/rbac/roles/93d94e22-17df-4743-b002-ca0ac5c769b0
 * 
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
package de.btu.openinfra.backend.rest.rbac;