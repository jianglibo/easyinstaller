## EasyInstaller

I once wrote a project using tcl script language to simplify common server side software installation, like hadoop, solr cluster, mysql cluster etc. [https://github.com/jianglibo/easy-installer](https://github.com/jianglibo/easy-installer).

This project use Vaadin technology, web interface, so it's far more easy to use.

### Abstraction

Thinking of software install progress, The essential step involved:

1. upload some file to server.
2. upload code file to server and run it. code file may be wrote by any language, of course you start from bash.
3. for cluster installation, need a way to know all member's information for configuration.

### How to setup project

1. Clone project
2. Copy application.yaml.template to application.yaml in src/main/resources folder. Edit content if needed.
3. run .\gradlew eclipse
4. import into eclipse run the VaadinApplication class. Or in command line just type .\gradlew bootRun. Visit http://localhost

### It's also a project help you learning vaadin

The project name is first-vaadin, Yes, It's my first vaadin project. Because I had experiences of Gwt, Emberjs, Spring boot etc, So use some pattern from these projects.
In this project, I also wrote some custom fields, like TwinGridField. And heavily use java annotation to minify reduplicative code.

### Some screenshots

The development is in progress.

![screenshot](https://github.com/jianglibo/first-vaadin/blob/master/wiki/20160908075551.png)

![screenshot](https://github.com/jianglibo/first-vaadin/blob/master/wiki/20160908075610.png)

![screenshot](https://github.com/jianglibo/first-vaadin/blob/master/wiki/20160908075633.png)
