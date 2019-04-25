# fsClient
Rest client to access file system in DropBox

## Steps to deploy in Tomcat
1. Download the complete source code.
2. Zip the source code to a war file using cmd.
```
	jar -cvf fsClient.war *
```
3. Place the war under CATALINA_HOME/webapps
4. Add following servlet and servlet mapping to CATALINA_HOME/conf/web.xml
  ```xml
	<servlet>
		<servlet-name>FileSystem REST Service</servlet-name>
		<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
		<init-param>
			<param-name>jersey.config.server.provider.packages</param-name>
			<param-value>fsClient.service</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>FileSystem REST Service</servlet-name>
		<url-pattern>/rest/*</url-pattern>
	</servlet-mapping>
```  
5. Start tomcat

## Rest service endpoints
 - Base URL for all endpoints would be http://\<hostname\>:\<port\>/\<warName\>/rest

1. **/listContents** : Lists contents of a folder in DropBox.  
	- Parameters
  		- token: OAuth token
  		- path: path to folder in DropBox separated by "/", blank for root.
2. **/download**  : Download a file in DropBox.  
	- Parameters
  		- token: OAuth token
  		- fileToDownload: path to file in DropBox separated by "/"
  		- path: path to local file (will be overwritten if already exists)
3. **/upload**  : Upload a file to DropBox.  
	- Parameters
  		- token: OAuth token
  		- path: path to file in DropBox separated by "/".  	    
	- Request Body: This will be the contents of the file.
