/*
 * ex1.c
 */

#include <sys/types.h>
#include <sys/stat.h>

#include <fcntl.h>
#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define MAX_BUFFER_SIZE 65536
#define DESTINATION_FILE_MODE S_IRUSR|S_IWUSR|S_IRGRP|S_IROTH

extern int opterr, optind;

void exit_with_usage(const char *message) {
	fprintf (stderr, "%s\n", message);
	fprintf (stderr, "Usage:\n\tex1 [-f] BUFFER_SIZE SOURCE DEST\n");
	exit(EXIT_FAILURE);
}

void append_file(const char *source_file, const char *dest_file, int buffer_size, int force_flag) {
	
	int src, dst;
   	char buffer[buffer_size];
   	int n;

 	if((src = open(source_file, O_RDONLY)) == -1){
 		perror("Unable to open source file for reading");
		exit(EXIT_FAILURE);
	}
	if(force_flag){
		dst = open(dest_file,O_WRONLY|O_APPEND, DESTINATION_FILE_MODE);
 		if(dst < 0 ){
			if(dst = open(dest_file,O_WRONLY|O_CREAT|O_APPEND, DESTINATION_FILE_MODE ) < 0){
 				perror("Unable to open destination file for writing");
      			exit(EXIT_FAILURE);
 			}
    	}
	}else{
		dst = open(dest_file,O_WRONLY|O_APPEND, DESTINATION_FILE_MODE);
		if(dst == -1){
			perror("Unable to open destination file for writing");
			exit(EXIT_FAILURE);	
		}
	}


 	while((n = read(src, buffer, buffer_size))>0){
	
    	if(write(dst, buffer, n) != n){
        	perror("Unable to open destination file for writing");
        	exit(EXIT_FAILURE);
    	}
    	if(n == -1){
           perror("Unable to read source file");
           exit(EXIT_FAILURE);
       }
    }

    if(close(src) < 0){
    	perror("Unable to close source file");
    	exit(EXIT_FAILURE);
    }
    
    if(close(dst) < 0){
	perror("Unable to close destination file");
	exit(EXIT_FAILURE);
    }

	printf("Content from file %s was successfully appended to %s\n", source_file, dest_file);
    exit(EXIT_SUCCESS);
    }

void parse_arguments(
		int argc, char **argv,
		char **source_file, char **dest_file, int *buffer_size, int *force_flag) {
	/*
	 * parses command line arguments and set the arguments required for append_file
	 */
	int option_character;

	opterr = 0; /* Prevent getopt() from printing an error message to stderr */

	while ((option_character = getopt(argc, argv, "f")) != -1) {
		switch (option_character) {
		case 'f':
			*force_flag = 1;
			break;
		default:  /* '?' */
			exit_with_usage("Unknown option specified");
		}
	}

	if (argc - optind != 3) {
		exit_with_usage("Invalid number of arguments");
	} else {
		*source_file = argv[argc-2];
		*dest_file = argv[argc-1];
		*buffer_size = atoi(argv[argc-3]);

		if (strlen(*source_file) == 0 || strlen(*dest_file) == 0) {
			exit_with_usage("Invalid source / destination file name");
		} else if (*buffer_size < 1 || *buffer_size > MAX_BUFFER_SIZE) {
			exit_with_usage("Invalid buffer size");
		}
	}
}

int main(int argc, char **argv) {
	int force_flag = 0; /* force flag default: false */
	char *source_file = NULL;
	char *dest_file = NULL;
	int buffer_size = MAX_BUFFER_SIZE;

	parse_arguments(argc, argv, &source_file, &dest_file, &buffer_size, &force_flag);

	append_file(source_file, dest_file, buffer_size, force_flag);

	return EXIT_SUCCESS;
}