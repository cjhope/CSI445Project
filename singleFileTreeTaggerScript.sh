#!/bin/bash
#file to convert with tree-tagger passed to script
#by argument
#First argument is the name of input file
#Second argument is the name of the output file



cat $1 | /Users/cjh/TreeTagger/cmd/tree-tagger-english > $2 

