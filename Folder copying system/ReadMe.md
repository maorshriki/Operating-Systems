<h2>C application that appends content from a file to another.
The application receive the buffer size (in bytes), source file path and target file path as command line arguments.
By default, the application does not create a target file if such does not exist, unless the -f option was specified.</h2>

<h4>Execution output examples:</h4>

$ ./ex1 <br>
Invalid number of arguments<br>
Usage:<br>
ex1 [-f] BUFFER_SIZE SOURCE DEST<br><br>
$ ./ex1 /etc/passwd /tmp/passwd<br>
Invalid number of arguments <br>
Usage:<br>
ex1 [-f] BUFFER_SIZE SOURCE DEST<br><br>
$ ./ex1 4096 /etc/passwd /tmp/passwd<br>
Unable to open destination file for writing: No such file or directory<br><br>
$ ./ex1 -f 4096 /etc/passwd /tmp/passwd<br>
Content from file /etc/passwd was successfully appended to /tmp/passwd<br><br>
$ ./ex1 -f 4096 /tmp/passwd /etc/passwd<br>
Unable to open destination file for writing: Permission denied<br><br>
$ ./ex1 4096 /etc/password /tmp/passwd<br>
Unable to open source file for reading: No such file or directory<br>
