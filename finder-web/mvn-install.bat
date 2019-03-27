@echo off

@echo  ******************************************************
@echo  *                                                    *
@echo  *                  MAVEN INSTALL                     *
@echo  *                                                    *
@echo  ******************************************************

@rem call mvn clean
@rem project properties
call mvn-project.bat

@echo.
@echo project properties:
@echo  artifactId: %artifactId%
@echo     groupId: %groupId%
@echo     version: %version%
@echo.
@echo 按任意键开始安装, 否则请关闭窗口.
@pause
@echo.

@rem clean & package
call mvn clean package -Dmaven.test.skip=true -DfailIfNoTests=false

@rem install
call mvn install:install-file -Dfile=target/%artifactId%-%version%.jar -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%version% -Dpackaging=jar
call mvn install:install-file -Dfile=target/%artifactId%-%version%-sources.jar -DgroupId=%groupId% -DartifactId=%artifactId% -Dversion=%version% -Dpackaging=jar -Dclassifier=sources
pause
