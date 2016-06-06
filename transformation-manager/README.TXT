README - epSOS CCD Project Template
===================================

For all the epSOS-specific NCP components there is a shared Subversion (SVN).
Each subproject/component is saved in a separate folder of the SVN.
Each subproject/component folder contains the subfolders 'trunk', 'branches', and 'tags' to support branching and tagging of the subprojects.
NOTE:
 - For each commit, the commit message MUST NOT be empty.
 - The message MUST start with the subproject name in square brackets followed by a space character and the commit message. Sample: '[configuration-manager] ...'.


To create a new subproject/component, the content of 'project-template' SHOULD be copied into the subfolder of the new subproject/component.
 
 
Folder structure of a subproject/component:
- FOLDER <project name>
  - SUBFOLDER trunk
    - FILE README.TXT
    - SUBFOLDER src	-> contains source files and test cases
	  - SUBFOLDER main -> folder that contains the source files
	  - SUBFOLDER test -> contains test cases (e.g. in JUnit)
	    - SUBFOLDER reports -> report(s) of quality checks
		- SUBFOLDER testdata -> sample test data, sample certificate data, etc.
	- SUBFOLDER lib	-> contains used 3rd party libraries (MUST be OpenSource libraries)
    - SUBFOLDER bin	-> contains the JAR(s)/executable files, compiled '.class' files are put to ignore list and MUST not be committed to the SVN
    - SUBFOLDER javadoc	-> contains JavaDoc documentation in HTML format, JavaDoc HTML files MUST be commited to SVN
    - SUBFOLDER userdoc-environment	-> contains user documentation and rollout/installation guidelines for the component
  - SUBFOLDER branches -> contains copies of the subproject
  - SUBFOLDER tags -> contains copies of the subproject