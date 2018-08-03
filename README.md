S6-PortalTransportador
====================
Sistema S6-PortalTransportador


Introdução
------------

	Projeto do Portal do Transportador
	
	Projeto responsável por fornecer uma interface Rest entre os serviços do Portal do Transportador e o MongoDB.
	
Definição projeto
-------------------------------------
	TODO

Requisitos do Sistema Ambiente de desenvolvimento 
-------------------


Windows:

	1. jdk-8u141-windows-x64.exe.
	 
	2. spring-tool-suite-3.9.0.RELEASE-e4.7.0-win32.zip
	
	3. MongoDB
	

MongoDB
-------------------

	Ambiente de desenvolvimento
	IP: 10.128.132.70:27017
	Database: portransp

Rodando o sistema com maven
-------------------
Entrar no diretorio do projeto
		
	mvn spring-boot:run

          
### Executando fat jar.

	1. java -Xmx256m -jar s6-portaltransportador.jar --spring.profiles.active=hom


Utilização do Jacoco para metricas de cobertura de testes unitários
-------------------------------------------------------------------
	
	https://docs.sonarqube.org/display/PLUG/Usage+of+JaCoCo+with+Java+Plugin

	mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true
	
	relatório em s6-portaltransportador/target/site/jacoco/index.html
	

Utilização do Sonar para qualidade do código
-------------------------------------------------------------------	

	mvn clean test
	mvn sonar:sonar
	
	http://sonar.viavarejo.com.br/dashboard/index/br.com.viavarejo:s6-portaltransportado


