<h2>C application that checks the response time of Internet URLs. 
The application should receive a file containing a list of URLs and the number of workers we want to use. 
After a successful run the application prints a numeric average of the successful URL queries,
number of sites reached and the number of unknowns (for URLs it failed to test).
implement in both methods - PIPE and Memory mapping.
Add a flag “-f” in case you run it with pipe and without this flag the program run using memory mapping.
</h2>

<h4>Execution output examples (numbers might vary between runs):</h4>

$ ./ex2<br>
usage:<br>
./ex2 NUMBER_OF_PROCESSES FILENAME<br><br>
$ ./ex2 1 top10.txt<br>
0.0146 Average response time from 8 sites, 2 Unknown<br><br>
$ ./ex2 2 top10.txt -f<br>
0.1002 Average response time from 9 sites, 1 Unknown<br><br>
$ ./ex2 10 top10.txt<br>
Illegal url detected, exiting now<br><br>


